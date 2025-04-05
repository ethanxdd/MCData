package turnserver.socket;

import org.ice4j.TransportAddress;

/**
 * Represents the TCP connect event on the Server Socket.
 * 
 * @author Aakash Garg
 * 
 */
public class TcpConnectEvent
{
    private final TransportAddress localAdress;
    
    private final TransportAddress remoteAdress;

    /**
     * @param localAdress
     * @param remoteAdress
     */
    public TcpConnectEvent(TransportAddress localAdress,
        TransportAddress remoteAdress)
    {
        this.localAdress = localAdress;
        this.remoteAdress = remoteAdress;
    }

    public TransportAddress getLocalAdress()
    {
        return localAdress;
    }

    public TransportAddress getRemoteAdress()
    {
        return remoteAdress;
    }   

    
    
}
