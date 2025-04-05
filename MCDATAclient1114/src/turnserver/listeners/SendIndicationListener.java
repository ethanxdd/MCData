package turnserver.listeners;

import java.util.logging.Logger;

import org.ice4j.*;
import org.ice4j.attribute.*;
import org.ice4j.message.*;
import org.ice4j.stack.*;

import turnserver.*;
import turnserver.stack.*;

/**
 * Class to handle the incoming Send indications.
 * 
 * @author Aakash Garg
 * 
 */
public class SendIndicationListener extends IndicationListener 
{
    /**
     * The <tt>Logger</tt> used by the <tt>SendIndicationListener</tt>
     * class and its instances for logging output.
     */
    private static final Logger logger = Logger
        .getLogger(SendIndicationListener.class.getName());

    /**
     * parametrised constructor.
     * 
     * @param turnStack
     *            the turnStack to set for this class.
     */
    public SendIndicationListener(TurnStack turnStack) 
    {
	super(turnStack);
    }

    /**
     * Handles the incoming send indication.
     * 
     * @param ind
     *            the indication to handle.
     * @param alloc
     *            the allocation associated with message.
     */
    @Override
    public void handleIndication(Indication ind, Allocation alloc) 
    {
	if(ind.getMessageType()==Message.SEND_INDICATION)
	{
	    logger.finest("Received a Send Indication message.");
	    byte[] tran = ind.getTransactionID();
	    XorPeerAddressAttribute xorPeerAddress
	    	= (XorPeerAddressAttribute) ind
	    		.getAttribute(Attribute.XOR_PEER_ADDRESS);
	    xorPeerAddress.setAddress(xorPeerAddress.getAddress(), tran);
	    DataAttribute data 
	    	= (DataAttribute) ind.getAttribute(Attribute.DATA);
	    TransportAddress peerAddr = xorPeerAddress.getAddress();
	    if(alloc!=null && alloc.isPermitted(peerAddr))
	    {
		RawMessage udpMessage = RawMessage.build(data.getData(),
			data.getDataLength(), peerAddr, alloc.getRelayAddress());
		try 
		{
		    this.getTurnStack().sendUdpMessage(udpMessage, peerAddr,
			    alloc.getRelayAddress());
		    logger.finest("Sent SendIndiaction to " + peerAddr
			    + " from " + alloc.getRelayAddress());
		} catch (StunException e) {
		    System.err.println("Unable to send message.");
		}
	    }
	    // else silently ignore the indication.
	}
    }

}