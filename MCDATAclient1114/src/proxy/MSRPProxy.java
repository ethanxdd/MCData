package proxy;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import RTCP.RTCPSender;
import network.StunRequestInfo;
import network.TurnRequestInfo;
import session.MediaSipSession;
import session.PermissionState;

public class MSRPProxy {
	private int port;
	  
	  private String pIp;
	  
	  private int pPort;
	  
//	  private RTCPThread rtcpThread;
	  
	  private DatagramSocket msrpSocket;
	  
	  MSRPProxy(String localIp, int Port, String stunServer, String senderport) throws IOException {
	    initialize(localIp, Port, stunServer, senderport);
	  }
	  
	  MSRPProxy(String localIp, int Port, String stunServer) throws IOException {
		    initialize(localIp, Port, stunServer);
		  }
	  
	  MSRPProxy(String localIp, int Port, String stunServer, String username, String password) throws IOException {
		    initialize(localIp, Port, stunServer, username, password);
		  }
	  
	  public void initialize(String localIp, int Port, String stunServer, String senderport) throws IOException {
	    this.port = Port ;
	    this.pIp = null;
	    this.pPort = 0;
	    System.out.println("[MSRPproxy]ip "+localIp);
	    InetSocketAddress address = new InetSocketAddress(localIp, this.port);
	    System.out.println("[MSRPproxy]address "+address);
	    this.msrpSocket = new DatagramSocket(null);
	    this.msrpSocket.bind(address);
	    StunRequestInfo stunRequestInfo = new StunRequestInfo(this.msrpSocket, stunServer);
	    stunRequestInfo.establish();
	    if (stunRequestInfo.isEstablishmentSuccess()) {
	      this.pIp = stunRequestInfo.getResponseHost();
	      this.pPort = stunRequestInfo.getResponsePort();
	      System.out.println("[MSRPproxy]ip "+this.pIp);
	      System.out.println("[MSRPproxy]port "+this.pPort);
	    } 
	    System.out.println("[MSRPProxy] finish " );
	  }
	  
	  public void initialize(String localIp, int Port, String stunServer) throws IOException {
		    this.port = Port ;
		    this.pIp = null;
		    this.pPort = 0;
		    System.out.println("ip"+localIp);
		    InetSocketAddress address = new InetSocketAddress(localIp, this.port);
		    this.msrpSocket = new DatagramSocket(null);
		    System.out.println("address"+address+"----"+ this.port);
		    this.msrpSocket.bind(address);
		    StunRequestInfo stunRequestInfo = new StunRequestInfo(this.msrpSocket, stunServer);
		    stunRequestInfo.establish();
		    if (stunRequestInfo.isEstablishmentSuccess()) {
		      this.pIp = stunRequestInfo.getResponseHost();
		      this.pPort = stunRequestInfo.getResponsePort();
		    } 
		    
//		    String message = "hello";
//		    String serverIp = "140.113.110.221";
//		    int serverPort = Integer.valueOf(senderport);
//		    byte[] data = message.getBytes();
//		    InetSocketAddress serverAddress = new InetSocketAddress(serverIp, serverPort);
//		    DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress);
//		    this.msrpSocket.send(packet);
//		    System.out.println("Message sent to server: " + message);


		    //	    this.rtcpThread = new RTCPThread(this.rtcpSocket);
//		    this.rtcpThread.setName("RTCPThread");
//		    this.rtcpThread.start();
		  }
	  
	  public void initialize(String localIp, int Port, String turnServer, String username, String password) throws IOException {
		  	System.out.println("[getMSRPport]");
		    this.port = Port;
		    this.pIp = null;
		    this.pPort = 0;

		    // 创建并绑定 UDP Socket
		    InetSocketAddress address = new InetSocketAddress(localIp, this.port);
		    this.msrpSocket = new DatagramSocket(null);
		    this.msrpSocket.bind(address);
		    System.out.println("[getMSRPport]-1");
		    // 创建 TURN 请求实例
		    TurnRequestInfo turnRequestInfo = new TurnRequestInfo(this.msrpSocket, turnServer, username, password);
		    turnRequestInfo.establish();
		    System.out.println("[getMSRPport]-2");
		    // 判断是否成功
		    if (turnRequestInfo.isEstablishmentSuccess()) {
		        this.pIp = turnRequestInfo.getResponseHost();
		        this.pPort = turnRequestInfo.getResponsePort();
		        System.out.println("[tuenserver]Relayed Address: " + this.pIp + ":" + this.pPort);
		    } else {
		        System.out.println("[turnserver]Failed to establish TURN connection.");
		    }
		}
	  
	  public void start() {
//	    this.rtcpThread.start();
//	    this.rtcpThread.run();
	  }
	  
	  public DatagramSocket getRTCPSocket() {
	    return this.msrpSocket;
	  }
	  
	  public String getRTCPProxyIp() {
	    return this.msrpSocket.getLocalAddress().getHostAddress();
	  }
	  
	  public int getRTCPProxyPort() {
	    return this.msrpSocket.getLocalPort();
	  }
	  
	  public String getPublicAddr() {
	    if (this.pIp != null)
	      return this.pIp; 
	    return this.msrpSocket.getLocalAddress().toString();
	  }
	  
	  public int getPublicPort() {
	    if (this.pPort != 0)
	      return this.pPort; 
	    return this.msrpSocket.getLocalPort();
	  }
	  
//	  public RTCPThread getRTCPThread() {
////	    return this.rtcpThread;
//	  }
	  
	  public void closeProxy() {
//	    if (this.rtcpThread != null) {
//	      this.rtcpThread.stopThread();
//	      this.rtcpThread.clearsocket();
//	    } 
//	    this.rtcpSocket = null;
//	    this.rtcpThread = null;
	  }
	  
	  public void sendRequest(MediaSipSession mediaSipSession, int priority) {
	    String peerIp = mediaSipSession.getPeerInfo().getPeerIp();
	    int peerPort = mediaSipSession.getPeerInfo().getPeerRtcpPort();
	    PermissionState permissionState = MediaSipSession.getPermissionState();
	    RTCPSender sender = new RTCPSender(peerIp, peerPort, this.msrpSocket, 0, permissionState);
	    sender.setName("RTCP Sender send Request");
	    sender.setTBPriority(priority);
	    sender.start();
	  }
	  
	  public void sendRelease(MediaSipSession mediaSipSession, int lastSeq) {
	    String peerIP = mediaSipSession.getPeerInfo().getPeerIp();
	    int peerPort = mediaSipSession.getPeerInfo().getPeerRtcpPort();
	    PermissionState permissionState = MediaSipSession.getPermissionState();
	    RTCPSender sender = new RTCPSender(peerIP, peerPort, this.msrpSocket, 4, permissionState);
	    sender.setLastSeq(lastSeq);
	    sender.setName("RTCP Sender send Release");
	    sender.start();
	  }
	  
	  public void sendRtcpQueueRequest(MediaSipSession mediaSipSession) {
	    String peerIP = mediaSipSession.getPeerInfo().getPeerIp();
	    int peerPort = mediaSipSession.getPeerInfo().getPeerRtcpPort();
	    PermissionState permissionState = MediaSipSession.getPermissionState();
	    RTCPSender sender = new RTCPSender(peerIP, peerPort, this.msrpSocket, 8, permissionState);
	    sender.setName("RTCP Sender send Queue Request");
	    sender.start();
	  }
	}
