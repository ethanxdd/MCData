package proxy;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;

import session.MediaSipSession;
import sip.SipClient;

public class ProxyMCDATA extends Thread{
	private static final String TAG = "Proxy";
	  
	  private static ProxyMCDATA instance;
	  
	  private MSRPProxy msrpProxy;
	  
	  private int lastSeq;
	  
	  private String stunServer;
	  
	  private ProxyMCDATA() {
	    instance = this;
	  }
	  
	  public static ProxyMCDATA getInstance() {
	    if (instance == null)
	      return new ProxyMCDATA(); 
	    return instance;
	  }
	  
	  public void setting(String localIp, int port, String stunServer, String senderport) throws IOException {
	    this.msrpProxy = new MSRPProxy(localIp, port, stunServer, senderport);
	  }
	  
	  public void setting(String localIp, int port, String stunServer) throws IOException {
		    this.msrpProxy = new MSRPProxy(localIp, port, stunServer);
		  }
	  
	  public void setting(String localIp, int port, String stunServer, String username, String password) throws IOException {
		  System.out.println("[getMSRPport]");
		    this.msrpProxy = new MSRPProxy(localIp, port, stunServer, username, password);
		  }
	  
	  public void run() {
	    startProxy();
	  }
	  
	  public void startProxy() {
	    this.msrpProxy.start();
	  }
	  
	  public void stopProxy() {
//	    if (this.rtpProxy != null)
//	      this.rtpProxy.stopProxy(); 
//	    if (this.rtcpProxy != null)
//	      this.rtcpProxy.closeProxy(); 
//	    this.rtpProxy = null;
//	    this.rtcpProxy = null;
//	    instance = null;
	  }
	  
	  public void sendMsrpRequest(MediaSipSession mediaSipSession, int priority) {
	    if (mediaSipSession.isConnect()) {
	      this.msrpProxy.sendRequest(mediaSipSession, priority);
	    } else {
	      SipClient.getInstance().sendRefer(mediaSipSession, "prearranged");
	    } 
	  }
	  
	  public void sendMsrpRelease(MediaSipSession mediaSipSession) {
	    stopRtpSending();
	    this.msrpProxy.sendRelease(mediaSipSession, this.lastSeq);
	  }
	  
	  private void stopRtpSending() {}
	  
	  public void sendRTCPQueueRequest(MediaSipSession mediaSipSession) {
	    this.msrpProxy.sendRtcpQueueRequest(mediaSipSession);
	  }
	  
	  public void setStunServer(String stunServer) {
	    this.stunServer = stunServer;
	  }
	  
//	  public DatagramSocket getRtpSocket() {
////	    return this.msrpProxy.getRtpSocket();
//	  }
//	  
//	  public DatagramSocket getRTCPSocket() {
////	    return this.msrpProxy.getRTCPSocket();
//	  }
//	  
	  public String getMsrpProxyIp() {
	    return this.msrpProxy.getPublicAddr();
	  }
	  
	  public int getMsrpProxyPort() {
	    return this.msrpProxy.getPublicPort();
	  }
//	  
//	  public String getRTCPProxyIp() {
////	    return this.msrpProxy.getPublicAddr();
//	  }
//	  
//	  public int getRTCPProxyPort() {
////	    return this.msrpProxy.getPublicPort();
//	  }
//	  
	  public void onT10TimerStart(MediaSipSession mediaSipSession) {}
	  
//	  private void checkProxyisStarted() {
//	    if (!isProxyActive())
//	      startProxy(); 
//	  }
	  
	  public MSRPProxy getMSRPProxy() {
	    return this.msrpProxy;
	  }
	  
//	  private boolean isProxyActive() {
////	    return !(this.rtpProxy == null || getRtpSocket().isClosed() || this.rtcpProxy == null || getRTCPSocket().isClosed());
//	  }
	  
	  public void setAdjustMicGainFactor(int factor) {}
	}
