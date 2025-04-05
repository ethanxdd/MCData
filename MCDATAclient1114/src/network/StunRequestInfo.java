package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import org.ice4j.StunException;
import org.ice4j.Transport;
import org.ice4j.TransportAddress;
import org.ice4j.attribute.XorMappedAddressAttribute;
import org.ice4j.ice.Agent;
import org.ice4j.ice.harvest.CandidateHarvester;
import org.ice4j.ice.harvest.StunCandidateHarvester;
import org.ice4j.message.Message;
import org.ice4j.message.MessageFactory;
import org.ice4j.message.Request;
import org.ice4j.stack.StunStack;
import org.ice4j.stack.TransactionID;

public class StunRequestInfo {
  private DatagramSocket udpSocket;
  
  private TransactionID transactionID;
  
  private String stunServerAddr;
  
  private String stunServerHost;
  
  private int stunServerPort;
  
  private String responseHost;
  
  private int responsePort;
  
  private int retryCount;
  
  private boolean isEstablishmentSuccess;
  
  public StunRequestInfo(DatagramSocket socket, String stunServerAddr) {
    this.udpSocket = socket;
    this.stunServerAddr = stunServerAddr;
    this.isEstablishmentSuccess = false;
    this.retryCount = 0;
    initTransactionId();
    initStunServer();
  }
  
  private void initStunServer() {
    String host;
    int port;
    String[] pair = this.stunServerAddr.split(":");
    if (pair.length == 2) {
      host = pair[0];
      port = Integer.parseInt(pair[1]);
    } else {
      host = this.stunServerAddr;
      port = 3478;
    } 
    this.stunServerHost = host;
    this.stunServerPort = port;
  }
  
  private void initTransactionId() {
    this.transactionID = TransactionID.createNewTransactionID();
  }
  
  public void establish() {
    this.isEstablishmentSuccess = false;
    XorMappedAddressAttribute attribute = null;
    try {
      String host = this.stunServerHost;
      int port = this.stunServerPort;
      TransactionID id = this.transactionID;
      DatagramSocket socket = this.udpSocket;
      Agent agent = new Agent();
      agent.addCandidateHarvester((CandidateHarvester)new StunCandidateHarvester(
            new TransportAddress(host, port, 
              Transport.UDP)));
      Request stunRequest = MessageFactory.createBindingRequest();
      byte[] traId = id.getBytes();
      stunRequest.setTransactionID(traId);
      StunStack stunStack = agent.getStunStack();
      agent.startConnectivityEstablishment();
      byte[] bytes = stunRequest.encode(stunStack);
      InetAddress inetAddressTest2 = InetAddress.getByName(host);
      DatagramPacket udpPacket = new DatagramPacket(bytes, bytes.length);
      udpPacket.setAddress(inetAddressTest2);
      udpPacket.setPort(port);
      try {
        Thread.sleep(400L);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } 
      socket.send(udpPacket);
      byte[] receivedData2 = new byte[500];
      DatagramPacket packetR2 = new DatagramPacket(receivedData2, receivedData2.length);
      socket.setSoTimeout(3000);
      try {
        socket.receive(packetR2);
      } catch (SocketTimeoutException e) {
        System.out.println("getXorMappedAddress Stun Server no response");
        e.printStackTrace();
        this.retryCount++;
        stunStack.shutDown();
        this.isEstablishmentSuccess = false;
        socket.setSoTimeout(0);
        return;
      } 
      socket.setSoTimeout(0);
//      Message messagee = Message.decode(packetR2.getData(), 'f','q');//???
      Message messagee = Message.decode(packetR2.getData(), (char) 0, (char) packetR2.getLength());//???
      attribute = (XorMappedAddressAttribute)messagee.getAttribute(' ');
      if (attribute != null) {
        stunStack.shutDown();
        this.responseHost = attribute.getAddress(id.getBytes()).getHostAddress();
        this.responsePort = attribute.getAddress(id.getBytes()).getPort();
        this.isEstablishmentSuccess = true;
      } else {
        System.out.println("getXorMappedAddress Stun Server response error");
        this.isEstablishmentSuccess = false;
      } 
      this.retryCount = 0;
    } catch (StunException e) {
      e.printStackTrace();
      this.isEstablishmentSuccess = false;
    } catch (IOException e) {
      e.printStackTrace();
      this.isEstablishmentSuccess = false;
    } 
  }
  
  public boolean isEstablishmentSuccess() {
    return this.isEstablishmentSuccess;
  }
  
  public String getResponseHost() {
    return this.responseHost;
  }
  
  public int getResponsePort() {
    return this.responsePort;
  }
  
  public DatagramSocket getUdpSocket() {
    return this.udpSocket;
  }
  
  public TransactionID getTransactionID() {
    return this.transactionID;
  }
  
  public String getStunServerHost() {
    return this.stunServerHost;
  }
  
  public int getStunServerPort() {
    return this.stunServerPort;
  }
}
