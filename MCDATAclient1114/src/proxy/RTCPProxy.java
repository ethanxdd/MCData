package proxy;

import RTCP.RTCPSender;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import network.StunRequestInfo;
import session.MediaSipSession;
import session.PermissionState;

public class RTCPProxy {
  private int port;
  
  private String pIp;
  
  private int pPort;
  
  private RTCPThread rtcpThread;
  
  private DatagramSocket rtcpSocket;
  
  RTCPProxy(String localIp, int Port, String stunServer) throws SocketException {
    initialize(localIp, Port, stunServer);
  }
  
  public void initialize(String localIp, int Port, String stunServer) throws SocketException {
    this.port = Port + 1;
    this.pIp = null;
    this.pPort = 0;
    InetSocketAddress address = new InetSocketAddress(localIp, this.port);
    this.rtcpSocket = new DatagramSocket(null);
    this.rtcpSocket.bind(address);
    StunRequestInfo stunRequestInfo = new StunRequestInfo(this.rtcpSocket, stunServer);
    stunRequestInfo.establish();
    if (stunRequestInfo.isEstablishmentSuccess()) {
      this.pIp = stunRequestInfo.getResponseHost();
      this.pPort = stunRequestInfo.getResponsePort();
    } 
    this.rtcpThread = new RTCPThread(this.rtcpSocket);
    this.rtcpThread.setName("RTCPThread");
    this.rtcpThread.start();
  }
  
  public void start() {
    this.rtcpThread.start();
    this.rtcpThread.run();
  }
  
  public DatagramSocket getRTCPSocket() {
    return this.rtcpSocket;
  }
  
  public String getRTCPProxyIp() {
    return this.rtcpSocket.getLocalAddress().getHostAddress();
  }
  
  public int getRTCPProxyPort() {
    return this.rtcpSocket.getLocalPort();
  }
  
  public String getPublicAddr() {
    if (this.pIp != null)
      return this.pIp; 
    return this.rtcpSocket.getLocalAddress().toString();
  }
  
  public int getPublicPort() {
    if (this.pPort != 0)
      return this.pPort; 
    return this.rtcpSocket.getLocalPort();
  }
  
  public RTCPThread getRTCPThread() {
    return this.rtcpThread;
  }
  
  public void closeProxy() {
    if (this.rtcpThread != null) {
      this.rtcpThread.stopThread();
      this.rtcpThread.clearsocket();
    } 
    this.rtcpSocket = null;
    this.rtcpThread = null;
  }
  
  public void sendRequest(MediaSipSession mediaSipSession, int priority) {
    String peerIp = mediaSipSession.getPeerInfo().getPeerIp();
    int peerPort = mediaSipSession.getPeerInfo().getPeerRtcpPort();
    PermissionState permissionState = MediaSipSession.getPermissionState();
    RTCPSender sender = new RTCPSender(peerIp, peerPort, this.rtcpSocket, 0, permissionState);
    sender.setName("RTCP Sender send Request");
    sender.setTBPriority(priority);
    sender.start();
  }
  
  public void sendRelease(MediaSipSession mediaSipSession, int lastSeq) {
    String peerIP = mediaSipSession.getPeerInfo().getPeerIp();
    int peerPort = mediaSipSession.getPeerInfo().getPeerRtcpPort();
    PermissionState permissionState = MediaSipSession.getPermissionState();
    RTCPSender sender = new RTCPSender(peerIP, peerPort, this.rtcpSocket, 4, permissionState);
    sender.setLastSeq(lastSeq);
    sender.setName("RTCP Sender send Release");
    sender.start();
  }
  
  public void sendRtcpQueueRequest(MediaSipSession mediaSipSession) {
    String peerIP = mediaSipSession.getPeerInfo().getPeerIp();
    int peerPort = mediaSipSession.getPeerInfo().getPeerRtcpPort();
    PermissionState permissionState = MediaSipSession.getPermissionState();
    RTCPSender sender = new RTCPSender(peerIP, peerPort, this.rtcpSocket, 8, permissionState);
    sender.setName("RTCP Sender send Queue Request");
    sender.start();
  }
}
