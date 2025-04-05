package dtmf;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class udpsender {
  public String EMRIP;
  
  public int EMRPort;
  
  public DatagramSocket socket;
  
  public DatagramPacket DPkt;
  
  public byte[] rawpkt;
  
  public final int buffer_max = 1500;
  
  public udpsender() {}
  
  public udpsender(String emrIP, int emrPort) {
    this.EMRIP = emrIP;
    this.EMRPort = emrPort;
    this.rawpkt = new byte[1500];
  }
  
  public void createDataSocket(String lMRLocalIp, int port) {
    if (this.socket == null)
      try {
        InetSocketAddress address = new InetSocketAddress(lMRLocalIp, port);
        this.socket = new DatagramSocket(null);
        this.socket.bind(address);
      } catch (SocketException e) {
        e.printStackTrace();
      }  
  }
  
  public void sendpacket(byte[] pkt) {
    try {
      this.DPkt = new DatagramPacket(pkt, pkt.length, InetAddress.getByName(this.EMRIP), this.EMRPort);
      this.socket.send(this.DPkt);
    } catch (SocketException e) {
      e.printStackTrace();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
  
  public DatagramSocket getSocket() {
    return this.socket;
  }
  
  public void closedSocket() {
    if (!this.socket.isClosed())
      this.socket.close(); 
  }
}
