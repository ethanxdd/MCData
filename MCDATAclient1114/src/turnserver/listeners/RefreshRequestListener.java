package turnserver.listeners;

import java.util.logging.*;

import org.ice4j.*;
import org.ice4j.attribute.*;
import org.ice4j.message.*;
import org.ice4j.stack.*;
import turnserver.stack.*;

/**
 * The class that would be handling and responding to incoming Refresh requests
 * that are validated and sends a success or error response
 * 
 * @author Aakash Garg
 */
public class RefreshRequestListener
    implements RequestListener
{
    /**
     * The <tt>Logger</tt> used by the <tt>RefreshRequestListener</tt> class and
     * its instances for logging output.
     */
    private static final Logger logger = Logger
        .getLogger(RefreshRequestListener.class.getName());

    private final TurnStack turnStack;

    /**
     * The indicator which determines whether this
     * <tt>RefreshRequestListener</tt> is currently started.
     */
    private boolean started = false;

    /**
     * Creates a new RefreshRequestListener
     * 
     * @param turnStack
     */
    public RefreshRequestListener(StunStack turnStack)
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

    /**
     * (non-Javadoc)
     * 
     * @see org.ice4j.stack.RequestListener#processRequest(org.ice4j.StunMessageEvent)
     */
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
        if (message.getMessageType() == Message.REFRESH_REQUEST)
        {
            logger.finer("Received refresh request " + evt);

            LifetimeAttribute lifetimeAttribute =
                (LifetimeAttribute) message.getAttribute(Attribute.LIFETIME);
            
            Response response = null;
            TransportAddress clientAddress = evt.getRemoteAddress();
            TransportAddress serverAddress = evt.getLocalAddress();
            Transport transport = serverAddress.getTransport();
            FiveTuple fiveTuple =
                new FiveTuple(clientAddress, serverAddress, transport);
            
            Allocation allocation =
                this.turnStack.getServerAllocation(fiveTuple);
            if (allocation != null)
            {
                if (lifetimeAttribute != null)
                {
		    logger.finest("Refreshing allocation with relay addr "
			    + allocation.getRelayAddress() + " with lifetime "
			    + lifetimeAttribute.getLifetime());
                    allocation.refresh(lifetimeAttribute.getLifetime());
                    response = MessageFactory.createRefreshResponse(
                            (int) allocation.getLifetime());
                }
                else
                {
		    logger.finest("Refreshing allocation with relay addr "
			    + allocation.getRelayAddress()
			    + " with default lifetime");
                    allocation.refresh();
                    response = MessageFactory.createRefreshResponse(
                            (int) allocation.getLifetime());
                }
            }
            else
            {
        	logger.finest("Allocation mismatch error");
                response = MessageFactory.createRefreshErrorResponse(
                            ErrorCodeAttribute.ALLOCATION_MISMATCH);
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
     * Starts this <tt>RefreshRequestListener</tt>. If it is not currently
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
     * Stops this <tt>RefreshRequestListenerr</tt>. A stopped
     * <tt>ValidatedRequestListenerr</tt> can be restarted by calling
     * {@link #start()} on it.
     */
    public void stop()
    {
        turnStack.removeRequestListener(this);
        started = false;
    }
}
