package media.rtplib;

import java.net.InetSocketAddress;

public interface DebugAppIntf {
  void packetReceived(int paramInt, InetSocketAddress paramInetSocketAddress, String paramString);
  
  void packetSent(int paramInt, InetSocketAddress paramInetSocketAddress, String paramString);
  
  void importantEvent(int paramInt, String paramString);
}


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\rtplib\DebugAppIntf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */