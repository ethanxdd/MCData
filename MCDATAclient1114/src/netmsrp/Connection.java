package netmsrp;

/*
 * Copyright ? Jo?o Antunes 2008 This file is part of MSRP Java Stack.
 * 
 * MSRP Java Stack is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * MSRP Java Stack is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with MSRP Java Stack. If not, see <http://www.gnu.org/licenses/>.
 */


import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import netmsrp.exceptions.ConnectionLostException;
import netmsrp.exceptions.ParseException;
import netmsrp.exceptions.ConnectionReadException;
import netmsrp.exceptions.ConnectionWriteException;
import netmsrp.exceptions.IllegalUseException;
import netmsrp.utils.NetworkUtils;
import netmsrp.utils.TextUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Connection extends Observable implements Runnable
{
    private static final Logger logger =
        LoggerFactory.getLogger(Connection.class);

    public static final int OUTPUTBUFFERLENGTH = 2048;

    public Connection(SocketChannel newSocketChannel)
        throws URISyntaxException
    {
        socketChannel = newSocketChannel;
        random = new Random();
        Socket socket = socketChannel.socket();
        URI newLocalURI =
            new URI("msrp", null, socket.getInetAddress().getHostAddress(),
                socket.getPort(), null, null, null);
        localURI = newLocalURI;
        transactionManager = new TransactionManager(this);
        // this.addObserver(transactionManager);

    }

    public Connection(InetAddress address) throws URISyntaxException, IOException
    {
    	System.out.println("[Connection]");
        transactionManager = new TransactionManager(this);
        random = new Random();
        // activate the connection:

        if (NetworkUtils.isLinkLocalIPv4Address(address))
        {
            logger.info("Connection: given address is a local one: " + address);
        }

        // bind a socket to a local TEMP port.
        socketChannel = SelectorProvider.provider().openSocketChannel();
        Socket socket = socketChannel.socket();
        InetSocketAddress socketAddr = new InetSocketAddress(address, 0);
        socket.bind(socketAddr);
        
        // fill the localURI variable that contains the uri parts that are
        // associated with this connection (scheme[protocol], host and port)
        URI newLocalURI =
            new URI("msrp", null, address.getHostAddress(), socket.getLocalPort(),
            		null, null, null);
        localURI = newLocalURI;
    }

//    public Connection(InetAddress address) throws URISyntaxException, IOException {
//        this.transactionManager = new TransactionManager(this);
//        this.random = new Random();
//
//        socketChannel = SelectorProvider.provider().openSocketChannel();
//        Socket socket = socketChannel.socket();
//        InetSocketAddress socketAddr = new InetSocketAddress(address, 0);
//        socket.bind(socketAddr);
//        
//        this.localURI = new URI("msrp", null, address.getHostAddress(), socket.getLocalPort(), null, null, null);
//        
//        // 設置為非阻塞模式
//        socketChannel.configureBlocking(false);
//
//        // 初始化 Selector
//        selector = Selector.open();
//        socketChannel.register(selector, SelectionKey.OP_READ);
//    }
    
    private static final int BUFFER_SIZE = 2048;
    
    private TransactionManager transactionManager;

    private HashSet<URI> sessions = new HashSet<URI>();

    private SocketChannel socketChannel = null;
    
    private Selector selector; // 用於非阻塞 I/O

    protected Random random;

    protected URI localURI = null;
    
    private volatile boolean running = true; // 控制連線狀態

    /*
     * @uml.property name="_connections"
     * @uml.associationEnd inverse="connection1:netmsrp.Connections"
     */
    protected TransactionManager getTransactionManager()
    {
        return transactionManager;
    }

    /**
     * @param localURI sets the local URI associated with the connection
     */
    protected void setLocalURI(URI localURI)
    {
        this.localURI = localURI;
    }

    protected boolean isEstablished()
    {
        if (socketChannel == null)
            return false;
        return socketChannel.isConnected();
    }

    /**
     * 
     * @return returns the associated local uri (relevant parts of the uri for
     *         the connection)
     */
    protected URI getLocalURI()
    {
        return localURI;
    }

    protected HashSet<URI> getSessionURIs()
    {
        return sessions;
    }
    
    private void receiveData() {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            int bytesRead = socketChannel.read(buffer);

            if (bytesRead == -1) {
                stop(); // 連線關閉
                return;
            }

            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            String message = new String(data, "UTF-8");
            System.out.println("Received: " + message);

            // 這裡可以根據需求來處理收到的數據
        } catch (IOException e) {
            e.printStackTrace();
            stop();
        }
    }

    /**
     * 發送數據
     */
    public void sendData(String message) {
        try {
            ByteBuffer buffer = ByteBuffer.wrap(message.getBytes("UTF-8"));
            socketChannel.write(buffer);
            System.out.println("Sent: " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止連線
     */
    public void stop() {
        running = false;
        try {
            if (socketChannel != null) {
                socketChannel.close();
            }
            if (selector != null) {
                selector.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Generates the new URI and validates it against the existing URIs of the
     * sessions that this connection handles
     * 
     * @return  the URI
     * @throws  URISyntaxException If there is a problem with the generation of
     *          the new URI
     */
    protected synchronized URI generateNewURI() throws URISyntaxException
    {
        URI newURI = newUri();
        System.out.println("[session]newURI"+newURI);
        /* The generated URI must be unique, if not, generate another.
         * This is what the following while does
         */
        int i = 0;
        while (sessions.contains(newURI))
        {
            i++;
            newURI = newUri();
        }
        sessions.add(newURI);
        logger.trace("generated new URI, value of i=" + i);
        return newURI;
    }

    /** Generate a new local URI with a unique session-path.
     * @return the generated URI
     * @throws URISyntaxException @see java.net.URI
     */
    protected URI newUri() throws URISyntaxException {
        byte[] randomBytes = new byte[8];

        generateRandom(randomBytes);

        if (logger.isTraceEnabled())
	        logger.trace("Random bytes generated: "
	            + (new String(randomBytes, TextUtils.utf8))
	            + ":END");

        // Generate new using current local URI.
        return
            new URI(localURI.getScheme(), localURI.getUserInfo(),
            		localURI.getHost(), localURI.getPort(),
            		 "/" + (new String(randomBytes, TextUtils.utf8))
            		 + ";tcp", localURI.getQuery(), localURI.getFragment());
    }

    // TODO: IMPROVE add the rest of the unreserved characters according rfc3986 (-._~)
    // TODO: IMPROVE speed by not doing so much calls to the Random class
    /**
     * Generates a number of random alpha-numeric codes in US-ASCII
     * 
     * @param byteArray	array that receives the newly generated bytes.
     *            	The number of generated bytes is given by the length of
     *            	the array.
     */
    protected void generateRandom(byte[] byteArray)
    {
        random.nextBytes(byteArray);
        for (int i = 0; i < byteArray.length; i++)
        {
            if (byteArray[i] < 0)
                byteArray[i] *= -1;

            while (!((byteArray[i] >= 65 && byteArray[i] <= 90)
                || (byteArray[i] >= 97 && byteArray[i] <= 122)
                || (byteArray[i] <= 57 && byteArray[i] >= 48)))
            {
                if (byteArray[i] > 122)
                    byteArray[i] -= random.nextInt(byteArray[i]);
                if (byteArray[i] < 48)
                    byteArray[i] += random.nextInt(5);
                else
                    byteArray[i] += random.nextInt(10);
            }
        }
    }

    /**
     * @return if the socket associated with the connection is bound
     */
    protected boolean isBound()
    {
        if (socketChannel == null)
            return false;
        return socketChannel.socket().isBound();
    }

    /*
     * @uml.property name="_session"
     * @uml.associationEnd inverse="_connectoin:netmsrp.Session"
     */
    private netmsrp.Session _session = null;

    /**
     * Getter of the property <tt>_session</tt>
     * 
     * @return Returns the _session.
     * uml.property name="_session"
     */
    public netmsrp.Session get_session()
    {
        return _session;
    }

    /**
     * Getter of the property <tt>_transactions</tt>
     * 
     * @return Returns the _transactions.
     * uml.property name="_transactions"
     */

    /**
     * close this connection/these threads
     */
    public void close()
    {
    	if (closing)
    		return;						// already closed
    	closing = true;
    	try
    	{
    		if (socketChannel != null)
    			socketChannel.close();
		}
    	catch (IOException e) { /* empty */; }
    }

    public void messageInterrupt(Message message)
    {
    	/* empty */
    }

    public void newTransaction(Session session, Message message,
        TransactionManager transactionManager, String transactionCode)
    {
    	/* empty */
    }

    /*
     * @desc - Read (reads from the stream of the socket)
     * @desc - Validation of what is being read
     * @desc - Misc. Interrupts due to read errors (mem, buffers etc)
     * 
     */
    public void read()
    {
    	/* empty */
    }

    public void sessionClose(Session session)
    {
    	/* empty */
    }

    /**
     * Setter of the property <tt>_session</tt>
     * 
     * @param _session The _session to set.
     * uml.property name="_session"
     */
    public void set_session(netmsrp.Session _session)
    {
        this._session = _session;
    }

    /**
     * Defines which block of data should be sent over the connection, according
     * to the priority Currently it's implemented an FIFO by the transaction,
     * however it could be later used
     * 
     * @return
     * @throws Exception
     */
    public Connection()
    {
    }

    protected volatile boolean closing = false; // connection closing?

    private Thread writeThread = null;
    private Thread readThread = null;

    private void writeCycle() throws ConnectionWriteException
    {
        /*
         * TODO FIXME should remove this line here when we get a better model
         * for the threads
         */
        Thread.currentThread().setName("Connection: " + localURI + " writeCycle thread");

        byte[] outData = new byte[OUTPUTBUFFERLENGTH];
        ByteBuffer outByteBuffer = ByteBuffer.wrap(outData);

        int wroteNrBytes = 0;
        while (!closing)
        {
            try
            {
                if (transactionManager.hasDataToSend())
                {
                    int toWriteNrBytes;

                    outByteBuffer.clear();
                    // toWriteNrBytes = transactionManager.dataToSend(outData);
                    // FIXME remove comment and change method name after the
                    // tests go well
                    toWriteNrBytes = transactionManager.getDataToSend(outData);

                    outByteBuffer.limit(toWriteNrBytes);
                    wroteNrBytes = 0;
                    while (wroteNrBytes != toWriteNrBytes)
                        wroteNrBytes += socketChannel.write(outByteBuffer);
                }
                else
                {
                    // TODO FIXME do this in another way, maybe with notify!
                    synchronized (writeThread)
                    {
                        writeThread.wait(200);
                    }
                }
            }
            catch (Exception e)
            {
            	if (!closing)
            		throw new ConnectionWriteException(e);
            }
        }
    }

    /**
     * Used to pre-parse the received data by the read cycle
     * 
     * @see #readCycle()
     * @see PreParser#preParse(byte[])
     */
    PreParser preParser = new PreParser(this);

    private void readCycle() throws ConnectionReadException
    {
        byte[] inData = new byte[OUTPUTBUFFERLENGTH];
        ByteBuffer inByteBuffer = ByteBuffer.wrap(inData);
        int readNrBytes = 0;
        while (readNrBytes != -1 && !closing)
        {
            inByteBuffer.clear();
            try
            {
                readNrBytes = socketChannel.read(inByteBuffer);

                if (readNrBytes != -1 && readNrBytes != 0)
                {
                    inByteBuffer.flip();
                    preParser.preParse(inData, inByteBuffer.limit());
                }
            }
            catch (Exception e)
            {
            	if (!closing)
            		throw new ConnectionReadException(e);
            }
        }
    }

    /**
     * Constantly receives and sends new transactions
     */
    @Override
	public void run()
    {
        // Sanity checks
        if (!this.isBound() && !this.isEstablished())
        {
            // if the socket is not bound to a local address or is
            // not connected, it shouldn't be running
            logger.error("Error!, Connection shouldn't be running yet");
            return;
        }
        if (!this.isEstablished() || !this.isBound())
        {
            logger.error("Error! got a unestablished either or "
                + "unbound connection");
            return;
        }

        if (writeThread == null && readThread == null)
        {
            writeThread = Thread.currentThread();
            readThread = new Thread(ioOperationGroup, this);
            readThread.setName("Connection: " + localURI + " readThread");
            readThread.start();

        }
        try
        {
            if (writeThread == Thread.currentThread())
            {
                writeCycle();
                writeThread = null;
            }
            if (readThread == Thread.currentThread())
            {
                readCycle();
                readThread = null;
            }
        } catch (ConnectionLostException cle) {
        	notifyConnectionLoss(cle);
        } catch (Exception e) {
        	logger.error(e.getLocalizedMessage());
        }
        return;
    }
    
//    @Override
//    public void run() {
//        while (running) {
//            try {
//                selector.select(); // 監聽事件
//                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
//
//                while (keys.hasNext()) {
//                    SelectionKey key = keys.next();
//                    keys.remove();
//
//                    if (key.isReadable()) {
//                        receiveData(); // 接收數據
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                stop();
//            }
//        }
//    }

	/**
	 * Notify sessions related to this connection that this connection is lost. 
	 * @param t	the reason it was lost.
	 */
	void notifyConnectionLoss(Throwable t) {
		Collection<Session> attachedSessions = new ArrayList<Session>();
		for (Session s : Stack.getInstance().getActiveSessions()) {
			if (this.equals(s.getConnection()))
				attachedSessions.add(s);
		}
		/* 
		 * No concurrent modifications: have active sessions remove
		 * themselves from the stack
		 */
		for (Session s : attachedSessions) {
			s.triggerConnectionLost(t.getCause());
		}
	}

    private boolean receivingTransaction = false;

    private Transaction incomingTransaction = null;

    String getCurrentIncomingTid() {
    	if (incomingTransaction != null)
    		return incomingTransaction.getTID();
    	return null;
    }

    /* Find the MSRP start line.
     * the TId has to contain at least 64 bits of randomness.
     * 
     * req-start = pMSRP SP transact-id SP method CRLF
     * resp-start= pMSRP SP transact-id SP status-cdoe [SP comment CRLF
     */
    private static Pattern req_start = Pattern.compile(
                    "(^MSRP) (\\p{Alnum}[\\p{Alnum}\\-=%+.]{3,31}) ([\\p{Upper}]{1,20})\r\n(.*)",
                    Pattern.DOTALL);
    private static Pattern resp_start = Pattern.compile(
                    "(^MSRP) (\\p{Alnum}[\\p{Alnum}\\-=%+.]{3,31}) ((\\d{3})([^\r\n]*)\r\n)(.*)",
                    Pattern.DOTALL);

    /**
     * Parse the incoming data, identifying transaction start or end,
     * creating a new transaction according RFC.
     * 
     * @param incomingBytes raw byte data to be handled
     * @param offset the starting position in the given byte array we should
     *            consider for processing
     * @param length the number of bytes to process starting from the offset
     *            position
     * @param inContentStuff true if it is receiving data regarding the body
     *            of a transaction, false otherwise
     * @throws ParseException Generic parsing exception
     */
    void parser(byte[] incomingBytes, int offset, int length,
        boolean inContentStuff) throws ParseException
    {
        if (length < 1)                 /* nothing to parse, please move along  */
            return;
        if (inContentStuff)
        {
            try
            {
                incomingTransaction.parse(incomingBytes, offset, length,
                    inContentStuff);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {								// We are receiving headers.
            String incomingString =
                new String(incomingBytes, offset, length, TextUtils.utf8);
            String toParse = incomingString;
            String tID;
            /*
             * For calls containing multiple transactions in incomingString
             */
            ArrayList<String> txRest = new ArrayList<String>();

            do
            {
                /*
                 * Deal with reception of multiple transactions.
                 */
                if (txRest.size() > 0)
                {
                    toParse = txRest.remove(0);
                }
                if (txRest.size() > 0)
                    throw new RuntimeException(
                    		"restTransactions were never meant "
                            + "to have more than one element!");

                if (!receivingTransaction)
                {
                    Matcher matchRequest = req_start.matcher(toParse);
                    Matcher matchResponse = resp_start.matcher(toParse);

                    if (matchRequest.matches())
                    {					// Retrieve TID and create new transaction
                        receivingTransaction = true;
                        tID = matchRequest.group(2);
                        toParse = matchRequest.group(4);
                        String type = matchRequest.group(3).toUpperCase();
                        TransactionType tType;
                        try
                        {
                            tType = TransactionType.valueOf(type);
                            logger.debug(String.format(
                            		"Parsing incoming request Tx-%s[%s]", tType, tID));
                        }
                        catch (IllegalArgumentException iae)
                        {
                            tType = TransactionType.UNSUPPORTED;
                            logger.warn("Unsupported transaction type: Tx-"
                        			+ type + "[" + tID + "]");
                        }
                        try
                        {
                            incomingTransaction = new Transaction(tID, tType,
                            				transactionManager, Direction.IN);
                        }
                        catch (IllegalUseException e)
                        {
                            logger.error("Cannot create an incoming transaction", e);
                        }
                        if (tType == TransactionType.UNSUPPORTED)
                        {
                            incomingTransaction.signalizeEnd('$');
                            logger.warn(
                            		"Found an unsupported transaction type for["
                                    + tID
                                    + "] signalised end and called update");
                            setChanged();
                            notifyObservers(tType);
                            // XXX:? receivingTransaction = false;
                        }
                    }
                    else if (matchResponse.matches())
                    {
                        receivingTransaction = true;
                        tID = matchResponse.group(2);
                        int status = Integer.parseInt(matchResponse.group(4));
                        String comment = matchResponse.group(5);
                        if (matchResponse.group(6) != null)
                            toParse = matchResponse.group(6);

                        incomingTransaction =
                            transactionManager.getTransaction(tID);
                        if (incomingTransaction == null)
                        {
                        	String reason =
                        			String.format("No outstanding Tx matches this response[%s]", tID);
                            logger.error(reason);
                            throw new ParseException(reason);
                        }
                        logger.debug("Found response to " + incomingTransaction);
                        try
                        {
                            Transaction trResponse =
                                new TransactionResponse(incomingTransaction,
                                			status, comment, Direction.IN);
                            incomingTransaction = trResponse;
                        }
                        catch (IllegalUseException e)
                        {
                            throw new ParseException(
                                "Cannot create transaction response", e);
                        }
                    }
                    else
                    {
                        logger.error("Start of transaction not found while parsing:\n"
                                + incomingString);
                        throw new ParseException(
                            "Error, start of the transaction not found on thread: "
                            + Thread.currentThread().getName());
                    }
                }
                if (receivingTransaction)
                {
                    /*
                     * Split multiple transactions.
                     */
                    tID = incomingTransaction.getTID();
                    Pattern endTransaction = Pattern.compile(
                        		"(.*)(-------" + tID.replace("+", "\\+") + ")([$+#])(\r\n)(.*)?",
                        		Pattern.DOTALL);
                    Matcher matcher = endTransaction.matcher(toParse);
                    if (matcher.matches())
                    {
                        logger.trace("found end of " + incomingTransaction);
                        toParse = matcher.group(1) + matcher.group(2)
                                + matcher.group(3) + matcher.group(4);
                        /*
                         * add any remaining data to restTransactions
                         */
                        if ((matcher.group(5) != null) &&
                    		(matcher.group(5).length() > 0)) {
                            txRest.add(matcher.group(5));
                        }
                    }
                    /*
                     * identify if transaction has content-stuff or not:
                     * 'Content-Type 2CRLF' from formal syntax.
                     */
                    Pattern contentStuff = Pattern.compile(
                    		"(.*)(Content-Type:) (" + RegEx.token
                            + "/" + RegEx.token + ")(\r\n\r\n)(.*)",
                            Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
                    matcher = contentStuff.matcher(toParse);
                    if (matcher.matches())
                    {
                        logger.trace(incomingTransaction +
                        			" was found to have content-stuff");
                        incomingTransaction.hasContentStuff = true;
                    }
                    if (incomingTransaction.hasContentStuff) {
                    	// strip 1 CRLF from string to parse...
                        endTransaction = Pattern.compile(
                    		"(.*)(\r\n)(-------" + tID.replace("+", "\\+") + ")([$+#])(\r\n)(.*)?",
                            Pattern.DOTALL);
                    }
                    matcher = endTransaction.matcher(toParse);

                    if (matcher.matches())
                    {					// we have a complete end of transaction
                        try
                        {
                            incomingTransaction.parse(
                        		matcher.group(1).getBytes(TextUtils.utf8), 0,
                                matcher.group(1).length(),
                                inContentStuff);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        if (incomingTransaction.hasContentStuff)
                            incomingTransaction
                                .signalizeEnd(matcher.group(4).charAt(0));
                        else
                            incomingTransaction
                                .signalizeEnd(matcher.group(3).charAt(0));

                        setChanged();
                        notifyObservers(incomingTransaction);
                        receivingTransaction = false;
                    }
                    else
                    {
                        try
                        {
                            incomingTransaction.parse(
                                toParse.getBytes(TextUtils.utf8), 0, toParse.length(),
                                inContentStuff);
                        }
                        catch (Exception e)
                        {
                            logger.error(
	                            "Exception parsing data to a transaction:", e);
                        }
                    }
                }
            }
            while (txRest.size() > 0);
        }
    }

    private ThreadGroup ioOperationGroup;

    public void addEndPoint(URI uri, InetAddress address) throws IOException
    {

        SocketAddress remoteAddress =
            new InetSocketAddress(uri.getHost(), uri.getPort());


        socketChannel.connect(remoteAddress);
        Connections connectionsInstance =
            Stack.getConnectionsInstance(address);

        ioOperationGroup =
            new ThreadGroup(connectionsInstance.getConnectionsGroup(),
                "IO OP connection " + uri.toString() + " group");
        connectionsInstance.startConnectionThread(this, ioOperationGroup);
    }

    /**
     * @return the InetAddress of the locally bound IP
     */
    public InetAddress getLocalAddress()
    {
        return socketChannel.socket().getLocalAddress();
    }

    /**
     * Method used to notify the write cycle thread
     */
    public void notifyWriteThread()
    {
        if (writeThread != null)
        {
            synchronized (writeThread)
            {
                writeThread.notify();
            }
        }
    }
}
