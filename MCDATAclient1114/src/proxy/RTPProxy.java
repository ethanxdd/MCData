package proxy;

import java.net.DatagramSocket;
import java.net.SocketException;

public class RTPProxy {
  private static final String TAG = "RTPProxy";
  
  private DatagramSocket rtpSocket;
  
  private RtpSession rtpAudioSession;
  
  private String pIp;
  
  private int pPort;
  
  RTPProxy(int codec) throws SocketException {
    initialize(codec);
  }
  
  RTPProxy(int codec, String stunServer) throws SocketException {
    initialize(codec, stunServer);
  }
  
  private void initialize(int codec) throws SocketException {
    if (this.rtpSocket != null && !this.rtpSocket.isClosed())
      this.rtpSocket.close(); 
    this.pIp = null;
    this.pPort = 0;
    this.rtpSocket = new DatagramSocket();
  }
  
  private void initialize(int codec, String stunServer) throws SocketException {
    if (this.rtpSocket != null && !this.rtpSocket.isClosed())
      this.rtpSocket.close(); 
    this.pIp = null;
    this.pPort = 0;
    this.rtpSocket = new DatagramSocket();
  }
  
  public void stopProxy() {
    if (this.rtpAudioSession != null) {
      this.rtpAudioSession.stop();
    } else if (!this.rtpSocket.isClosed()) {
      this.rtpSocket.close();
    } 
  }
  
  public String getRtpProxyIp() {
    return this.rtpSocket.getLocalAddress().toString();
  }
  
  public int getRtpProxyPort() {
    return this.rtpSocket.getLocalPort();
  }
  
  public DatagramSocket getRtpSocket() {
    return this.rtpSocket;
  }
  
  public String getPublicAddr() {
    if (this.pIp != null)
      return this.pIp; 
    return "120.171.9.174";
  }
  
  public int getPublicPort() {
    if (this.pPort != 0)
      return this.pPort; 
    return this.rtpSocket.getLocalPort();
  }
}
