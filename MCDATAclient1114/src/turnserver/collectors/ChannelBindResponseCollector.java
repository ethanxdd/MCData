package turnserver.collectors;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.ice4j.*;
import org.ice4j.attribute.*;
import org.ice4j.message.*;
import org.ice4j.stack.*;

/**
 * The class that would be handling and responding to incoming ChannelBind
 * response.
 * 
 * @author Aakash Garg
 */
public class ChannelBindResponseCollector
    implements ResponseCollector
{

    /**
     * The <tt>Logger</tt> used by the <tt>ChannelBindresponseCollector</tt>
     * class and its instances for logging output.
     */
    private static final Logger logger = Logger
        .getLogger(ChannelBindResponseCollector.class.getName());

    private final StunStack stunStack;

    /**
     * Creates a new ChannelBindresponseCollector
     * 
     * @param turnStack
     */
    public ChannelBindResponseCollector(StunStack stunStack)
    {
        this.stunStack = stunStack;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.ice4j.ResponseCollector#processResponse(org.ice4j.StunResponseEvent)
     */
    @Override
    public void processResponse(StunResponseEvent evt)
    {
        if (logger.isLoggable(Level.FINER))
        {
            logger.finer("Received response " + evt);
        }
        Message message = evt.getMessage();
        if (message.getMessageType() == Message.CHANNELBIND_ERROR_RESPONSE)
        {
            ErrorCodeAttribute errorCodeAttribute =
                (ErrorCodeAttribute) message.getAttribute(Attribute.ERROR_CODE);
            switch (errorCodeAttribute.getErrorCode())
            {
            case ErrorCodeAttribute.BAD_REQUEST:
                // code for bad response error
                break;
            case ErrorCodeAttribute.INSUFFICIENT_CAPACITY:
                // logic for processing insufficient capacity error code
                break;
            }
        }
        else if (message.getMessageType() == Message.CHANNELBIND_RESPONSE)
        {
            // logic for processing the success response.
        }
        else
        {
            return;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.ice4j.ResponseCollector#processTimeout(org.ice4j.StunTimeoutEvent)
     */
    @Override
    public void processTimeout(StunTimeoutEvent event)
    {
        // TODO Auto-generated method stub

    }

}