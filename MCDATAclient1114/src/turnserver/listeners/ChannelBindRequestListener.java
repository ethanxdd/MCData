package turnserver.listeners;

import java.util.logging.*;

import org.ice4j.*;
import org.ice4j.attribute.*;
import org.ice4j.message.*;
import org.ice4j.stack.*;
import turnserver.stack.*;

/**
 * The class that would be handling and responding to incoming ChannelBind
 * requests that are validated and sends a success or error response
 * 
 * @author Aakash Garg
 */
public class ChannelBindRequestListener
    implements RequestListener
{

    /**
     * The <tt>Logger</tt> used by the <tt>ChannelBindRequestListener</tt> class
     * and its instances for logging output.
     */
    private static final Logger logger = Logger
        .getLogger(ChannelBindRequestListener.class.getName());

    private final TurnStack turnStack;

    /**
     * The indicator which determines whether this
     * <tt>ChannelBindrequestListener</tt> is currently started.
     */
    private boolean started = false;

    /**
     * Creates a new ChannelBindRequestListener
     * 
     * @param turnStack
     */
    public ChannelBindRequestListener(StunStack turnStack)
    {
        if (turnStack instanceof TurnStack)
        {
            this.turnStack = (TurnStack) turnStack;
        }
        else
        {
            throw new IllegalArgumentException("This is not a TurnStack!");
        }
    }

    @Override
    public void processRequest(StunMessageEvent evt)
        throws IllegalArgumentException
    {
        if (logger.isLoggable(Level.FINER))
        {
            logger.setLevel(Level.FINEST);
//            logger.finer("Received request " + evt);
        }
        
        Message message = evt.getMessage();
        if (message.getMessageType() == Message.CHANNELBIND_REQUEST)
        {
            logger.finest("Received Channel Bind request ");
            logger.finest("Event tran : "+evt.getTransactionID());
            
            Response response = null;
 
            TransportAddress clientAddress = evt.getRemoteAddress();
            TransportAddress serverAddress = evt.getLocalAddress();
            Transport transport = serverAddress.getTransport();
            FiveTuple fiveTuple =
                new FiveTuple(clientAddress, serverAddress, transport);
            
            Allocation allocation 
                = this.turnStack.getServerAllocation(fiveTuple);
            
            ChannelNumberAttribute channelNo =
                (ChannelNumberAttribute) message.getAttribute(
                    Attribute.CHANNEL_NUMBER);
            XorPeerAddressAttribute xorPeerAddress =
                (XorPeerAddressAttribute) message
                    .getAttribute(Attribute.XOR_PEER_ADDRESS);
            if(xorPeerAddress!=null)
            {
                xorPeerAddress.setAddress(xorPeerAddress.getAddress(),
                    evt.getTransactionID().getBytes());
            }
            
            logger.finest("Adding ChannelBind : "
                                    +(int)(channelNo.getChannelNumber())
                                    +", "+xorPeerAddress.getAddress());
            ChannelBind channelBind = new ChannelBind(
                                            xorPeerAddress.getAddress(),
                                            channelNo.getChannelNumber());

            Character errorCode = null;
            if(channelNo==null || xorPeerAddress==null)
            {
                errorCode = ErrorCodeAttribute.BAD_REQUEST;
            }
            else if(!ChannelNumberAttribute.isValidRange(
                channelNo.getChannelNumber()))
            {
                errorCode = ErrorCodeAttribute.BAD_REQUEST;
            }
            else if (allocation == null
                || allocation.isBadChannelRequest(channelBind))
            {
                errorCode = ErrorCodeAttribute.BAD_REQUEST;
            }
            else if(!TurnStack.isIPAllowed(xorPeerAddress.getAddress()))
            {
                errorCode = ErrorCodeAttribute.FORBIDDEN;
            }
            else if(!allocation.canHaveMoreChannels())
            {
                errorCode = ErrorCodeAttribute.INSUFFICIENT_CAPACITY;
            }
            
            if(errorCode != null)
            {
                logger.finest("Creating ChannelBindError response : "
                                        +(int)errorCode);
                response  
                    = MessageFactory.createChannelBindErrorResponse(errorCode);
            }
            else
            {
                logger.finest("Creating ChannelBind sucess response");
                try
                {
                    logger.finer("Adding ChannelBind : "+channelBind);
                    allocation.addChannelBind(channelBind);
                }
                catch(IllegalArgumentException ex)
                {
                    logger.log(Level.FINEST,ex.getMessage());
                }
                response = MessageFactory.createChannelBindResponse();
            }
            
            try
            {
                turnStack.sendResponse(evt.getTransactionID().getBytes(),
                    response, evt.getLocalAddress(), evt.getRemoteAddress());
            }
            catch (Exception e)
            {
                logger.log(Level.INFO, "Failed to send " + response
                    + " through " + evt.getLocalAddress(), e);
                // try to trigger a 500 response although if this one failed,
                throw new RuntimeException("Failed to send a response", e);
            }
        }
        else
        {
            return;
        }
    }

    /**
     * Starts this <tt>ChannelBindRequestListener</tt>. If it is not currently
     * running, does nothing.
     */
    public void start()
    {
        if (!started)
        {
            turnStack.addRequestListener(this);
            started = true;
        }
    }

    /**
     * Stops this <tt>ChannelBindRequestListenerr</tt>. A stopped
     * <tt>ChannelBindRequestListenerr</tt> can be restarted by calling
     * {@link #start()} on it.
     */
    public void stop()
    {
        turnStack.removeRequestListener(this);
        started = false;
    }
}