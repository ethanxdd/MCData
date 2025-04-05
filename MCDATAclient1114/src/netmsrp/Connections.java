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

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import netmsrp.Connection;
import netmsrp.exceptions.*;
import netmsrp.utils.NetworkUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the class responsible for accepting incoming TCP connection requests
 * and generating the Connection object.
 * 
 * @author Jo?o Andr? Pereira Antunes
 */
public class Connections
    extends Connection
    implements Runnable
{
    /** The logger associated with this class */
    private static final Logger logger =
        LoggerFactory.getLogger(Connections.class);

    private Stack stack = Stack.getInstance();

    private ThreadGroup connectionsGroup =
            new ThreadGroup("MSRP Stack connections");

    private boolean hasStarted = false;

    private Thread associatedThread = null;

    private ServerSocketChannel serverSocketChannel = null;

    private HashMap<URI, Session> urisSessionsToIdentify =
        new HashMap<URI, Session>();

    /**
     * @uml.property name="_connections"
     * @uml.associationEnd multiplicity="(1 1)"
     *                     inverse="connections:netmsrp.TransactionManager"
     */
    private TransactionManager transactionManager = null;

    private HashSet<URI> existingURISessions = new HashSet<URI>();

    public Connections(InetAddress address)
    {
        try
        {
            random = new Random();
            if (NetworkUtils.isLinkLocalIPv4Address(address))
            {
                logger.info("Connections: given address is a local one: "
                    + address);
            }
            serverSocketChannel =
                SelectorProvider.provider().openServerSocketChannel();

            // bind the socket to a local temp port.
            ServerSocket socket = serverSocketChannel.socket();
            InetSocketAddress socketAddr = new InetSocketAddress(address, 0);

            socket.bind(socketAddr);

            // fill the localURI variable that contains the uri parts that are
            // associated with this connection (scheme[protocol], host and port)
            localURI =
                new URI("msrp", null, address.getHostAddress(), socket
                    .getLocalPort(), null, null, null);
            Thread server = new Thread(this);
            server.setName("Connections: " + localURI + " server");
            server.start();
            
//            SocketChannel serverChannel = SocketChannel.open();
//            serverChannel.connect(new InetSocketAddress("140.113.110.221", 55555)); // Server address and port
//            ByteBuffer buffer = ByteBuffer.wrap("INIT".getBytes()); // Data to send
//            serverChannel.write(buffer); // Send data
//            serverChannel.close(); // Close connection
//            System.out.println("[TCP Packet Sent to Server]");
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public Connections(InetAddress address, int port)
    {
        try
        {
            random = new Random();
            if (NetworkUtils.isLinkLocalIPv4Address(address))
            {
                logger.info("Connections: given address is a local one: "
                    + address);
            }
            serverSocketChannel =
                SelectorProvider.provider().openServerSocketChannel();

            // bind the socket to a local temp port.
            ServerSocket socket = serverSocketChannel.socket();
            InetSocketAddress socketAddr = new InetSocketAddress(address, 55555);

            try {
              // 創建一個 TCP Socket，並綁定到 URI 的 IP 和 Port
              Socket socket2 = new Socket();
              socket2.bind(socketAddr); // 綁定到 URI 提供的 IP 和埠
              System.out.println("Socket bound to: " + socket2.getLocalSocketAddress());
              
              System.out.println("Socket closed and resources released.");
              InetSocketAddress serverSocketAddr = new InetSocketAddress("140.113.110.221", port);
              socket2.connect(serverSocketAddr);
              System.out.println("Connected to server: " + serverSocketAddr);

              // 發送數據
              OutputStream outputStream = socket2.getOutputStream();
              outputStream.write("Hello, Server!".getBytes());
              outputStream.flush();
              System.out.println("Data sent to server.");
              socket2.close();
          } catch (IOException e) {
              e.printStackTrace();
          }
            
            socket.bind(socketAddr);

            // fill the localURI variable that contains the uri parts that are
            // associated with this connection (scheme[protocol], host and port)
            localURI =
                new URI("msrp", null, address.getHostAddress(), socket
                    .getLocalPort(), null, null, null);
            System.out.println("[connection]"+localURI);
            Thread server = new Thread(this);
            server.setName("Connections: " + localURI + " server");
            server.start();
            
//            SocketChannel serverChannel = SocketChannel.open();
//            serverChannel.connect(new InetSocketAddress("140.113.110.221", port)); // Server address and port
//            System.out.println("[Local Address and Port] " + serverChannel.getLocalAddress());
//            System.out.println("[TCP Packet Sent new]"+port);
//            ByteBuffer buffer = ByteBuffer.wrap("INIT".getBytes()); // Data to send
//            serverChannel.write(buffer); // Send data
//            serverChannel.close(); // Close connection
//            System.out.println("[TCP Packet Sent to Server]");
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // Protected constructor is sufficient to suppress unauthorised calls to the
    // constructor
    protected Connection Connection()
    {
        try
        {
            random = new Random();

            boolean localAddress = false;

            InetAddress newAddress = InetAddress.getLocalHost();
            /*
             * sanity check, check that the given address is a local one where
             * a socket can be bound
             */
            InetAddress local[] =
                InetAddress.getAllByName(InetAddress.getLocalHost()
                    .getHostName());

            for (InetAddress inetAddress : local)
            {
                if (inetAddress.equals(newAddress))
                    localAddress = true;
            }
            if (!localAddress)
                throw new UnknownHostException(
                    "the given adress is not a local one");

            serverSocketChannel =
                SelectorProvider.provider().openServerSocketChannel();

            // bind the socket to a local temp. port.
            ServerSocket socket = serverSocketChannel.socket();
            InetSocketAddress socketAddr = new InetSocketAddress(newAddress, 0);

            socket.bind(socketAddr);

            // fill the localURI variable that contains the uri parts that are
            // associated with this connection (scheme[protocol], host and port)
            localURI =
                new URI("msrp", null, newAddress.getHostAddress(), socket
                    .getLocalPort(), null, null, null);
        }
        catch (Exception e)
        {
            logger.error("Error! Connection did not get an associated socket");
        }
        return this;
    }

    @Override
	public TransactionManager getTransactionManager()
    {
        return transactionManager;
    }

    protected boolean hasStarted()
    {
        return hasStarted;
    }

    public void setTransactionManager(TransactionManager transactionManager)
    {
        this.transactionManager = transactionManager;
    }

    public Thread getAssociatedThread()
    {
        return associatedThread;
    }

    @Override
    public void run()
    {
        hasStarted = true;
        associatedThread = Thread.currentThread();
        try
        {
            // Use the current serverSocketChannel to accept new connections
            while (true)
            {
                Connection connection =
                    new Connection(serverSocketChannel.accept());
                stack.addConnection(connection);
                Thread newConnThread = new Thread(connection);
                newConnThread.setName("Connection: " + connection.getLocalURI() +
                					" by Connections.newConnThread");
                newConnThread.start();

            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
    }

    protected URI generateAndStartNewUri()
        throws ImplementationException, URISyntaxException
    {
        if (localURI == null)
            throw new ImplementationException(
            		"Absurd error, Connections don't have the needed socket info");

        URI newURI = newUri();
        
//        String ipAddress = newURI.getHost(); // 提取 URI 的主機地址
//        int port2 = newURI.getPort(); 
//        System.out.println("[Session] uri ip "+ipAddress);
//        System.out.println("[Session] uri port "+port2);
//        System.out.println("[Session]new msrp uri "+newURI);
//        try {
//            // 創建一個 TCP Socket，並綁定到 URI 的 IP 和 Port
//            Socket socket = new Socket();
//            socket.bind(new InetSocketAddress(ipAddress, port2)); // 綁定到 URI 提供的 IP 和埠
//            System.out.println("Socket bound to: " + socket.getLocalSocketAddress());
//            socket.close();
//            System.out.println("Socket closed and resources released.");
//
//        } catch (IOException e) {
//        	e.printStackTrace();
//        }
        
        int i = 0;
        while (existingURISessions.contains(newURI))
        {
            i++;
            newURI = newUri();
        }
        existingURISessions.add(newURI);

        logger.trace("generated new URI, value of i=" + i);

        if (hasStarted() && getAssociatedThread().isAlive())
        {
            ;
        }
        else
        {
            associatedThread = new Thread(this);
            associatedThread.setName("Connections: " + localURI
                + " associatedThread");
            associatedThread.start();
            hasStarted = true;
        }
        return newURI;
    }

    protected void addUriToIdentify(URI uri, Session session)
    {
        urisSessionsToIdentify.put(uri, session);
    }

    /**
     * Returns a session from the list of sessions negotiated but yet to
     * identify
     * 
     * @param uri of the session to be identified
     * @return the associated session for the given uri, taken from the list of
     *         still to identify sessions negotiated but not yet identified.
     */
    public Session sessionToIdentify(URI uri)
    {
        return urisSessionsToIdentify.get(uri);
    }

    protected void identifiedSession(Session session)
    {
    	urisSessionsToIdentify.remove(session.getURI());
        existingURISessions.add(session.getURI());
        session.setConnection(stack.getConnectionByLocalURI(
        		NetworkUtils.getCompleteAuthority(session.getNextURI())));
        stack.addActiveSession(session);
    }

    protected void startConnectionThread(Runnable connection,
        ThreadGroup ioGroup)
    {
        Thread newThread = new Thread(ioGroup, connection);
        newThread.setName("Connections: " + localURI + " newThread");
        newThread.start();
    }

    /**
     * @return the connectionsGroup
     */
    protected ThreadGroup getConnectionsGroup()
    {
        return connectionsGroup;
    }
}
