package redirect;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import network.StunRequestInfo;
import session.MediaSipSession;

public class handler {
  private int BufferSize;
  
  private String SererIP;
  
  private int ServerPort;
  
  private int IwfPort;
  
  private String E_mIP;
  
  private int E_mPort;
  
  private networkcard IWFtoServer = new networkcard();
  
  private networkcard IWFtoEMR = new networkcard();
  
  private String pIp;
  
  private int pPort;
  
  private StunRequestInfo stunRequestInfo;
  
  private InetSocketAddress address;
  
  public static final int listen_mode = 0;
  
  public static final int send_mode = 1;
  
  public static final int idle_mode = 2;
  
  private DatagramSocket rtpSocket;
  
  private DatagramSocket EMRRtpSocket;
  
  public RTP_Redirect GetFromServer;
  
  public RTP_Redirect SendToServer;
  
  MediaSipSession mediasession;
  
  private String localip;
  
  public int direction;
  
  public void setAllParm(int buffer_size, String ServerIP, int ServerPort, String card1IP, int card1Port, String card2IP, int card2Port, String E_mIP, int E_mPort) {
    setBufferSize(buffer_size);
    setSererIP(ServerIP);
    setServerPort(ServerPort);
    setIwfPort(card1Port);
    setNIC1parm(this.IWFtoServer, card1IP, card1Port);
    setE_mIP(E_mIP);
    setE_mPort(E_mPort);
  }
  
  public void setTwoModeHandler(int mode, String ServerIP, int ServerPort, String card1IP, int card1Port, String card2IP, int card2Port, String E_mIP, int E_mPort) {
    setBufferSize(1024);
    setSererIP(ServerIP);
    setServerPort(ServerPort);
    setIwfPort(card1Port);
    setNIC1parm(this.IWFtoServer, card1IP, card1Port);
    setNIC2parm(this.IWFtoEMR, card2IP, card2Port);
    setE_mIP(E_mIP);
    setE_mPort(E_mPort);
    changemode(mode);
  }
  
  public void setSession(MediaSipSession session) {
    this.mediasession = session;
  }
  
  public void getPublicIpPort(String localIp, int iwfPort, String stunServer) throws SocketException {
    this.pIp = null;
    this.pPort = 0;
    this.localip = localIp;
    this.address = new InetSocketAddress(localIp, iwfPort);
    this.rtpSocket = new DatagramSocket(null);
    this.rtpSocket.bind(this.address);
    setRtpSocket(this.rtpSocket);
    this.stunRequestInfo = new StunRequestInfo(this.rtpSocket, stunServer);
    this.stunRequestInfo.establish();
    if (this.stunRequestInfo.isEstablishmentSuccess()) {
      this.pIp = this.stunRequestInfo.getResponseHost();
      this.pPort = this.stunRequestInfo.getResponsePort();
    } 
  }
  
  public void changemode(int direction) {
    this.direction = direction;
    if (direction == 0) {
      if (this.SendToServer != null && 
        this.SendToServer.getThreadState())
        ClearSendToServer(); 
      if (this.GetFromServer != null && 
        this.GetFromServer.getThreadState())
        ClearGetFromServer(); 
      this.GetFromServer = new RTP_Redirect(this.BufferSize, this.IwfPort, this.E_mIP, this.E_mPort, this.IWFtoServer, this.IWFtoEMR);
      try {
        if (this.GetFromServer.createDataSocket(this.IWFtoServer, this.IWFtoEMR, getRtpSocket(), true, this.EMRRtpSocket))
          this.GetFromServer.start(); 
      } catch (SocketException e) {
        e.printStackTrace();
      } 
    } else if (direction == 1) {
      if (this.SendToServer != null && 
        this.SendToServer.getThreadState())
        ClearSendToServer(); 
      if (this.GetFromServer != null && 
        this.GetFromServer.getThreadState())
        ClearGetFromServer(); 
      if (this.mediasession.getMediaSessionState().getMeaningSender() != null)
        this.mediasession.getMediaSessionState().stop(); 
      this.SendToServer = new RTP_Redirect(this.BufferSize, this.IwfPort, this.SererIP, this.ServerPort, this.IWFtoServer, this.IWFtoEMR);
      try {
        if (this.SendToServer.createDataSocket(this.IWFtoEMR, this.IWFtoServer, getRtpSocket(), false, this.EMRRtpSocket))
          this.SendToServer.start(); 
      } catch (SocketException e) {
        e.printStackTrace();
      } 
    } else if (direction == 2) {
      if (this.SendToServer != null && 
        this.SendToServer.getThreadState())
        ClearSendToServer(); 
      if (this.GetFromServer != null)
        if (this.GetFromServer.getThreadState())
          ClearGetFromServer();  
      if (!this.mediasession.getMediaSessionState().getstate()) {
        this.mediasession.getMediaSessionState().setrtpSocket(this.rtpSocket);
        this.mediasession.getMediaSessionState().changeState(2, "", this.localip);
      } 
    } 
  }
  
  public void stopThread() {
    if (this.SendToServer != null && 
      this.SendToServer.getThreadState())
      this.SendToServer.stopThread(); 
    if (this.GetFromServer != null && 
      this.GetFromServer.getThreadState())
      this.GetFromServer.stopThread(); 
  }
  
  public void clearsocket() {
    if (this.SendToServer != null)
      this.SendToServer.clearsocket(); 
    if (this.GetFromServer != null)
      this.GetFromServer.clearsocket(); 
  }
  
  public void ClearSendToServer() {
    if (this.SendToServer.getThreadState())
      this.SendToServer.stopThread(); 
  }
  
  public void ClearGetFromServer() {
    if (this.GetFromServer.getThreadState())
      this.GetFromServer.stopThread(); 
  }
  
  public int getBufferSize() {
    return this.BufferSize;
  }
  
  public void setBufferSize(int size) {
    this.BufferSize = size;
  }
  
  public void setSererIP(String serverip) {
    this.SererIP = serverip;
  }
  
  public String getServerIP() {
    return this.SererIP;
  }
  
  public void setServerPort(int serverport) {
    this.ServerPort = serverport;
  }
  
  public int getServerPort() {
    return this.ServerPort;
  }
  
  public void setIwfPort(int iwfport) {
    this.IwfPort = iwfport;
  }
  
  public int getIwfPort() {
    return this.IwfPort;
  }
  
  public void setE_mIP(String E_mip) {
    this.E_mIP = E_mip;
  }
  
  public String getE_mIP() {
    return this.E_mIP;
  }
  
  public void setE_mPort(int e_mport) {
    this.E_mPort = e_mport;
  }
  
  public int getE_mPort() {
    return this.E_mPort;
  }
  
  public String getPublicAddr() {
    return this.pIp;
  }
  
  public int getPublicPort() {
    return this.pPort;
  }
  
  public void setNIC1parm(networkcard card, String IP, int Port) {
    card.cardIP = IP;
    card.RTPPort = Port;
  }
  
  public void setNIC2parm(networkcard card, String IP, int Port) {
    card.cardIP = IP;
    if (card.RTPPort != Port) {
      card.RTPPort = Port;
      InetSocketAddress address = new InetSocketAddress(card.cardIP, card.RTPPort);
      try {
        this.EMRRtpSocket = new DatagramSocket(null);
        this.EMRRtpSocket.bind(address);
      } catch (SocketException e) {
        e.printStackTrace();
      } 
    } 
  }
  
  public void setRtpSocket(DatagramSocket socket) {
    this.rtpSocket = socket;
  }
  
  public DatagramSocket getRtpSocket() {
    return this.rtpSocket;
  }
  
  public void setEMRSocket(DatagramSocket socket) {
    this.EMRRtpSocket = socket;
  }
  
  public DatagramSocket getEMRSocket() {
    return this.EMRRtpSocket;
  }
}
