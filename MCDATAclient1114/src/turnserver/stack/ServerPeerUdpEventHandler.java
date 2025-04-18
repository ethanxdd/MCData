package turnserver.stack;

import java.util.*;
import java.util.logging.*;

import org.ice4j.*;
import org.ice4j.message.*;
import org.ice4j.stack.*;

/**
 * Class to handle UDP messages coming from Peer. The class first checks if
 * there is a non-expired ChannelBind for the peer if yes it then sends a
 * ChannelData message to Client. If no it then finds if there is a non-expired
 * permission if yes then it sends a DataIndicatio to Client. All the mesages
 * sent to client here are from the address on which the allocation request was
 * received or the serverAddress of fiveTuple of corresponding Allocation.
 * 
 * @author Aakash Garg
 */
public class ServerPeerUdpEventHandler
    implements PeerUdpMessageEventHandler
{
    /**
     * The <tt>Logger</tt> used by the <tt>PeerUdpMessageEventHandler</tt> class
     * and its instances for logging output.
     */
    private static final Logger logger = Logger
        .getLogger(ServerPeerUdpEventHandler.class.getName());

    /**
     * The turnStack to call.
     */
    private TurnStack turnStack;

    /**
     * Default constructor.
     */
    public ServerPeerUdpEventHandler()
    {
    }

    /**
     * Parametrized constructor.
     * 
     * @param turnStack the turnStack to set for this class.
     */
    public ServerPeerUdpEventHandler(StunStack turnStack)
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

    public void setTurnStack(TurnStack turnStack)
    {
        this.turnStack = turnStack;
    }

    /**
     * Handles the PeerUdpMessageEvent.
     * 
     * @param evt the PeerUdpMessageEvent to handle/process.
     */
    @Override
    public void handleMessageEvent(PeerUdpMessageEvent evt)
    {
        if (logger.isLoggable(Level.FINER))
        {
            logger.setLevel(Level.FINEST);
            logger.finer("Received Peer UdP message message " + evt);
        }

        byte[] data = evt.getBytes();
        TransportAddress localAddress = evt.getLocalAddress();
        TransportAddress remoteAddress = evt.getRemoteAddress();
        logger.finest("Received a UDP message on: " + localAddress + ", data: "
            + byteArrayToHex(data));
        Allocation allocation =
            this.turnStack.getServerAllocation(localAddress);
        if (remoteAddress.getTransport() == Transport.TCP)
        {
            FiveTuple fiveTuple =
                new FiveTuple(remoteAddress, localAddress, Transport.TCP);
            logger.finest("Send message request from "+fiveTuple);
            if (allocation == null) // came from client
            {
                // get client allocation
                logger.finest("Message came from TCP Client");
                int connectionId =
                    this.turnStack.getConnectionIdForDataConn(fiveTuple);
                logger.finest("Connection Id extracted for "+fiveTuple+" is "+connectionId);
                allocation = this.turnStack.getAllocationFromConnectionId(connectionId);
                logger.finest("Allocation extracted is "+allocation+" for client-"+fiveTuple);
                FiveTuple peerTuple =
                    allocation.getPeerTCPConnection(connectionId);
                TransportAddress peerAddress =
                    peerTuple.getClientTransportAddress();
                TransportAddress relayAddress =
                    peerTuple.getServerTransportAddress();
                RawMessage rawMessage =
                    RawMessage.build(data, data.length, peerAddress,
                    relayAddress);
                try
                {
                    logger.finest("Relaying data to peer-" + peerAddress
                        + " from " + remoteAddress+" data-");
                    this.turnStack.sendUdpMessage(
                        rawMessage, peerAddress, relayAddress);
                }
                catch (StunException e)
                {
                    System.err.println("Unable to relay message to peer-"
                        + peerAddress + " from client-" + remoteAddress
                        + " message-" + Arrays.toString(data));
                }

            }
            else
            {
                // else came from peer
                logger.finest("Message came from TCP peer.");
                int connectionId =
                    this.turnStack.getConnectionIdForPeer(fiveTuple);
                if (!allocation.isPermitted(remoteAddress))
                {
                    logger.finest("No permission installed for peer-"+remoteAddress);
                    return;
                }
                else
                {
                    TransportAddress dataConn = allocation.getDataConnection(
                        connectionId).getClientTransportAddress();
                    if (dataConn != null)
                    {
                        RawMessage rawMessage =
                            RawMessage.build(data, data.length, dataConn,
                            allocation.getServerAddress());
                        try
                        {
                            logger.finest("Relaying data to client-" + dataConn
                                + " from peer-" + remoteAddress);
                            this.turnStack.sendUdpMessage(
                                rawMessage, dataConn,
                                allocation.getServerAddress());
                        }
                        catch (StunException e)
                        {
                            System.err
                                .println("Unable to relay message to client-"
                                    + dataConn + " from peer-" + remoteAddress
                                    + " message-" + Arrays.toString(data));
                        }
                    }else{
                        logger.finest("No data connection found for peer-"
                            + remoteAddress);
                    }
                }
            }
        }
        else if (allocation != null
            && allocation.getChannel(remoteAddress) != 0x1000)
        {
            char channelNo = allocation.getChannel(remoteAddress);
            ChannelData channelData = new ChannelData();
            channelData.setChannelNumber(channelNo);
            channelData.setData(data);
            try
            {
                logger.finest("Sending a ChannelData message " + channelData
                    + " from " + allocation.getServerAddress() + " to "
                    + allocation.getClientAddress());

                this.turnStack.sendChannelData(
                    channelData, allocation.getClientAddress(),
                    allocation.getServerAddress());
            }
            catch (StunException ex)
            {
                logger.finer(ex.getMessage());
            }
        }
        else if (allocation != null && allocation.isPermitted(remoteAddress))
        {
            TransactionID tranID = TransactionID.createNewTransactionID();
            Indication dataInd = MessageFactory.createDataIndication(
                remoteAddress, data, tranID.getBytes());
            try
            {
                logger.finest("Sending a ChannelData message " + dataInd
                    + " from " + allocation.getServerAddress() + " to "
                    + allocation.getClientAddress());

                this.turnStack.sendIndication(
                    dataInd, allocation.getClientAddress(),
                    allocation.getServerAddress());
            }
            catch (StunException e)
            {
                logger.finer(e.getMessage());
            }
        }else{
            logger
                .finest("unable to find allocation and the message is not on TCP.");
        }
    }
    
    private String byteArrayToHex(byte[] data){
        String arrayToHex= "";
        for(int i=0; i<data.length; i++){
            arrayToHex += String.format("%02X, ", data[i]); 
        }
        return arrayToHex;
    }

}