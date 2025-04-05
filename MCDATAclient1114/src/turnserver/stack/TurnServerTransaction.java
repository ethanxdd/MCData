package turnserver.stack;

import java.io.IOException;

import org.ice4j.StunException;
import org.ice4j.TransportAddress;
import org.ice4j.message.Response;
import org.ice4j.stack.*;

/**
 * The class represents the TURN server Transaction in turnserver.
 * It is just an inheritance of StunServerTransaction.
 * 
 * @author Aakash Garg
 *
 */
public class TurnServerTransaction
    extends StunServerTransaction
{

    /**
     * {@inheritDoc}
     */
    public TurnServerTransaction(   StunStack stackCallback, 
                                    TransactionID tranID,
                                    TransportAddress localListeningAddress, 
                                    TransportAddress requestSource)
    {
        super(stackCallback, tranID, localListeningAddress, requestSource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void retransmitResponse()
        throws StunException,
        IOException,
        IllegalArgumentException
    {
        super.retransmitResponse();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Response getResponse()
    {
        return super.getResponse();
    }

}