package turnserver.listeners;

import java.io.*;
import java.net.*;
import java.util.logging.*;

import org.ice4j.*;
import org.ice4j.attribute.*;
import org.ice4j.message.*;
import org.ice4j.socket.*;
import org.ice4j.stack.*;
import org.ice4j.stunclient.*;

import turnserver.*;
import turnserver.stack.*;

/**
 * Class to handle events when a Connection Attempt Indication is received on
 * CLient Side.
 * 
 * @author Aakash Garg
 * 
 */
public class ConnectionAttemptIndicationListener
    extends IndicationListener
{
    /**
     * The <tt>Logger</tt> used by the <tt>DataIndicationListener</tt> class and
     * its instances for logging output.
     */
    private static final Logger logger = Logger
        .getLogger(ConnectionAttemptIndicationListener.class.getName());

    /**
     * The request sender to use to send request to Turn server.
     */
    private BlockingRequestSender requestSender;

    /**
     * Constructor to create a ConnectionAttemptIndicationListener with
     * specified turnStack to use the requestSender will be null and a new
     * {@link BlockingRequestSender} will be created with new TCP socket to send
     * request to server.
     * 
     * @param turnStack the turnStack to use for processing.
     */
    public ConnectionAttemptIndicationListener(TurnStack turnStack)
    {
        super(turnStack);
    }

    /**
     * Constructor to create a ConnectionAttemptIndicationListener with
     * specified turnStack to use.
     * 
     * @param turnStack the turnStack to use for processing.
     * @param requestSender the requestSender to use to send request to server.
     */
    public ConnectionAttemptIndicationListener(TurnStack turnStack,
        BlockingRequestSender requestSender)
    {
        super(turnStack);
        this.requestSender = requestSender;
    }

    /**
     * The method to handle the incoming ConnectionAttempt Indications from Turn
     * Server.
     */
    @Override
    public void handleIndication(Indication ind, Allocation alloc)
    {
        if (ind.getMessageType() == Message.CONNECTION_ATTEMPT_INDICATION)
        {
            logger.finest("Received a connection attempt indication.");
            byte[] tranId = ind.getTransactionID();
            ConnectionIdAttribute connectionId =
                (ConnectionIdAttribute) ind
                    .getAttribute(Attribute.CONNECTION_ID);
            XorPeerAddressAttribute peerAddress =
                (XorPeerAddressAttribute) ind
                    .getAttribute(Attribute.XOR_PEER_ADDRESS);
            peerAddress.setAddress(
                peerAddress.getAddress(), tranId);
            logger
                .finest("Received a Connection Attempt Indication with connectionId-"
                    + connectionId.getConnectionIdValue()
                    + ", for peerAddress-" + peerAddress.getAddress());
            Socket socket;
            try
            {
                socket =
                    new Socket(InetAddress.getLocalHost().getHostAddress(),
                        3478);
                IceTcpSocketWrapper sockWrapper =
                    new IceTcpSocketWrapper(socket);
                this.getTurnStack().addSocket(
                    sockWrapper);
                TransportAddress localAddr =
                    new TransportAddress(sockWrapper.getLocalAddress(),
                        sockWrapper.getLocalPort(), Transport.TCP);
                logger.finest("New Local TCP socket chosen - " + localAddr);
                TransportAddress serverAddress =
                    new TransportAddress(InetAddress.getLocalHost(), 3478,
                        Transport.TCP);
                StunMessageEvent evt = null;
                try
                {
                    Request connectionBindRequest =
                        MessageFactory.createConnectionBindRequest(connectionId
                            .getConnectionIdValue());
                    TransactionID tranID =
                        TransactionID.createNewTransactionID();
                    connectionBindRequest.setTransactionID(tranID.getBytes());
                    if (this.requestSender == null)
                    {
                        logger.finest("Setting RequestSender");
                        this.requestSender =
                            new BlockingRequestSender(this.getTurnStack(),
                                localAddr);
                    }
                    logger.finest("Sending ConnectionBind Request to "
                        + serverAddress);
                    evt = requestSender.sendRequestAndWaitForResponse(
                        connectionBindRequest, serverAddress);
                    if (evt != null)
                    {
                        Message msg = evt.getMessage();
                        if (msg.getMessageType() == Message.CONNECTION_BIND_SUCCESS_RESPONSE)
                        {
                            logger
                                .finest("Received a ConnectionBind Sucess Response.");
                            String myMessage = "Aakash Garg";
                            RawMessage rawMessage =
                                RawMessage.build(myMessage.getBytes(),
                                    myMessage.length(), serverAddress,
                                    localAddr);
                            try
                            {
                                logger
                                    .finest("--------------- Thread will now sleep for 20 sec.");
                                Thread.sleep(20 * 1000);
                            }
                            catch (InterruptedException e)
                            {
                                System.err.println("Unable to stop thread");
                            }
                            logger
                                .finest(">>>>>>>>>>>> Sending a Test message : ");
                            byte[] data = myMessage.getBytes();
                            for (int i = 0; i < data.length; i++)
                            {
                                System.out.print(String.format(
                                    "%02X, ", data[i]));
                            }
                            this.getTurnStack().sendUdpMessage(
                                rawMessage, serverAddress,
                                requestSender.getLocalAddress());
                        }
                        else
                        {
                            logger
                                .finest("Received a ConnectionBind error Response - "
                                    + msg.getAttribute(Attribute.ERROR_CODE));
                        }
                    }
                    else
                    {
                        System.err
                            .println("No response received to ConnectionBind request");
                    }
                }
                catch (StunException e)
                {
                    logger.finest("Unable to send ConnectionBind request");
                }
            }
            catch (UnknownHostException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}