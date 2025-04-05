package turnserver.socket;

public interface TcpConnectEventGenerator
{
    public void setEventListener(TcpConnectEventListener listener);
    
    public void removeEventListener();
    
    public void fireConnectEvent(TcpConnectEvent event);
}