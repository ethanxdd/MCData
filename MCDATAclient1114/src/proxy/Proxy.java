package proxy;

import java.net.DatagramSocket;
import java.net.SocketException;
import session.MediaSipSession;
import sip.SipClient;

public class Proxy extends Thread {
  private static final String TAG = "Proxy";
  
  private static Proxy instance;
  
  private RTPProxy rtpProxy;
  
  private RTCPProxy rtcpProxy;
  
  private int lastSeq;
  
  private String stunServer;
  
  private Proxy() {
    instance = this;
  }
  
  public static Proxy getInstance() {
    if (instance == null)
      return new Proxy(); 
    return instance;
  }
  
  public void setting(String localIp, int port, String stunServer) throws SocketException {
    this.rtcpProxy = new RTCPProxy(localIp, port, stunServer);
  }
  
  public void run() {
    startProxy();
  }
  
  public void startProxy() {
    this.rtcpProxy.start();
  }
  
  public void stopProxy() {
    if (this.rtpProxy != null)
      this.rtpProxy.stopProxy(); 
    if (this.rtcpProxy != null)
      this.rtcpProxy.closeProxy(); 
    this.rtpProxy = null;
    this.rtcpProxy = null;
    instance = null;
  }
  
  public void sendRtcpRequest(MediaSipSession mediaSipSession, int priority) {
    if (mediaSipSession.isConnect()) {
      this.rtcpProxy.sendRequest(mediaSipSession, priority);
    } else {
      SipClient.getInstance().sendRefer(mediaSipSession, "prearranged");
    } 
  }
  
  public void sendRtcpRelease(MediaSipSession mediaSipSession) {
    stopRtpSending();
    this.rtcpProxy.sendRelease(mediaSipSession, this.lastSeq);
  }
  
  private void stopRtpSending() {}
  
  public void sendRTCPQueueRequest(MediaSipSession mediaSipSession) {
    this.rtcpProxy.sendRtcpQueueRequest(mediaSipSession);
  }
  
  public void setStunServer(String stunServer) {
    this.stunServer = stunServer;
  }
  
  public DatagramSocket getRtpSocket() {
    return this.rtpProxy.getRtpSocket();
  }
  
  public DatagramSocket getRTCPSocket() {
    return this.rtcpProxy.getRTCPSocket();
  }
  
  public String getRtpProxyIp() {
    return this.rtpProxy.getPublicAddr();
  }
  
  public int getRtpProxyPort() {
    return this.rtpProxy.getPublicPort();
  }
  
  public String getRTCPProxyIp() {
    return this.rtcpProxy.getPublicAddr();
  }
  
  public int getRTCPProxyPort() {
    return this.rtcpProxy.getPublicPort();
  }
  
  public void onT10TimerStart(MediaSipSession mediaSipSession) {}
  
  private void checkProxyisStarted() {
    if (!isProxyActive())
      startProxy(); 
  }
  
  public RTCPProxy getRTCPProxy() {
    return this.rtcpProxy;
  }
  
  private boolean isProxyActive() {
    return !(this.rtpProxy == null || getRtpSocket().isClosed() || this.rtcpProxy == null || getRTCPSocket().isClosed());
  }
  
  public void setAdjustMicGainFactor(int factor) {}
}
