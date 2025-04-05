package turnserver.stack;

import org.ice4j.ResponseCollector;
import org.ice4j.StunMessageEvent;
import org.ice4j.TransportAddress;
import org.ice4j.message.Request;
import org.ice4j.stack.*;

/**
 * {@inheritDoc}
 */
public class TurnClientTransaction
    extends StunClientTransaction
{

    /**
     * {@inheritDoc}
     */
    public TurnClientTransaction(   StunStack stackCallback, 
                                    Request request,
                                    TransportAddress requestDestination, 
                                    TransportAddress localAddress,
                                    ResponseCollector responseCollector, 
                                    TransactionID transactionID)
    {
        super(stackCallback, request, requestDestination, localAddress,
            responseCollector, transactionID);
    }

    /**
     * /** {@inheritDoc}
     */
    public TurnClientTransaction(   StunStack stackCallback, 
                                    Request request,
                                    TransportAddress requestDestination, 
                                    TransportAddress localAddress,
                                    ResponseCollector responseCollector)
    {
        super(stackCallback, request, requestDestination, localAddress,
            responseCollector);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void handleResponse(StunMessageEvent evt)
    {
        super.handleResponse(evt);
    }

}