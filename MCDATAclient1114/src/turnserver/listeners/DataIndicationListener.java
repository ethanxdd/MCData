package turnserver.listeners;

import java.io.*;
import java.util.logging.Logger;

import org.ice4j.*;
import org.ice4j.attribute.*;
import org.ice4j.message.*;

import turnserver.*;
import turnserver.stack.*;

/**
 * Class to handle the incoming Data indications.
 * 
 * @author Aakash Garg
 * 
 */
public class DataIndicationListener extends IndicationListener 
{
    /**
     * The <tt>Logger</tt> used by the <tt>DataIndicationListener</tt>
     * class and its instances for logging output.
     */
    private static final Logger logger = Logger
        .getLogger(DataIndicationListener.class.getName());

    /**
     * parametrised constructor.
     * 
     * @param turnStack
     *            the turnStack to set for this class.
     */
    public DataIndicationListener(TurnStack turnStack) 
    {
	super(turnStack);
    }

    /**
     * Handles the incoming data indication.
     * 
     * @param ind
     *            the indication to handle.
     * @param alloc
     *            the allocation associated with message.
     */
    @Override
    public void handleIndication(Indication ind, Allocation alloc) 
    {
	if (ind.getMessageType() == Message.DATA_INDICATION) 
	{
	    logger.finest("Received a Data Indication message.");
	    byte[] tran = ind.getTransactionID();

	    XorPeerAddressAttribute xorPeerAddress 
	    	= (XorPeerAddressAttribute) ind
		    .getAttribute(Attribute.XOR_PEER_ADDRESS);
	    xorPeerAddress.setAddress(xorPeerAddress.getAddress(), tran);
	    DataAttribute data = (DataAttribute) ind
		    .getAttribute(Attribute.DATA);

	    TransportAddress peerAddr = xorPeerAddress.getAddress();
	    try 
	    {
		String line = new String(data.getData(), "UTF-8");
//		System.out.println(line);
		logger.finest("Data Indiaction message from  " + peerAddr
			+ " is " + line);
/*		System.out.println("Received a Data indiction from " + peerAddr
			+ ", message : " + line);
*/	    } catch (UnsupportedEncodingException e) 
{
		System.err.println("Unable to convert to String!");
	    }
	}
    }

}