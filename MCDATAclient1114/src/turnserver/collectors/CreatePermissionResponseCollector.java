package turnserver.collectors;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.ice4j.*;
import org.ice4j.attribute.*;
import org.ice4j.message.*;
import org.ice4j.stack.*;

/**
 * The class that would be handling and responding to incoming CreatePermission
 * response.
 * 
 * @author Aakash Garg
 */
public class CreatePermissionResponseCollector
    implements ResponseCollector
{
    /**
     * The <tt>Logger</tt> used by the
     * <tt>CreatePermissionResponseCollector</tt> class and its instances for
     * logging output.
     */
    private static final Logger logger = Logger
        .getLogger(CreatePermissionResponseCollector.class.getName());

    private final StunStack stunStack;

    /**
     * Creates a new CreatePermissionResponseCollector
     * 
     * @param turnStack
     */
    public CreatePermissionResponseCollector(StunStack stunStack)
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
        if (message.getMessageType() == Message.ALLOCATE_ERROR_RESPONSE)
        {
            ErrorCodeAttribute errorCodeAttribute =
                (ErrorCodeAttribute) message.getAttribute(Attribute.ERROR_CODE);
            switch (errorCodeAttribute.getErrorCode())
            {
            case ErrorCodeAttribute.BAD_REQUEST:
                // logic for processing bad request error
                break;
            case ErrorCodeAttribute.INSUFFICIENT_CAPACITY:
                // logic for processing insufficient capacity error
                break;
            case ErrorCodeAttribute.FORBIDDEN:
                // logic for processing forbidden error code
                break;
            default:
                logger.finer("error : Received error response with error code "
                    + errorCodeAttribute.getErrorCode() + evt);
            }
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