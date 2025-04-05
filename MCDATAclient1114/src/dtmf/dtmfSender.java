package dtmf;

public class dtmfSender extends Thread {
  private int event;
  
  private int duration = 160;
  
  private udpsender sender;
  
  private boolean ctrl;
  
  public dtmfSender(int event, String DstIP, int DsrPort) {
    this.ctrl = true;
    this.event = event;
    this.sender = new udpsender(DstIP, DsrPort);
  }
  
  public void createDataSocket(String lMRLocalIp, int port) {
    if (this.sender.getSocket() == null)
      this.sender.createDataSocket(lMRLocalIp, port); 
  }
  
  public void run() {
    byte[] p = new byte[16];
    RTPpacket dtmf = new RTPpacket(p, this.event, true, false, this.duration);
    this.sender.sendpacket(dtmf.getRawPacket());
    this.sender.sendpacket(dtmf.getRawPacket());
    this.sender.sendpacket(dtmf.getRawPacket());
    int i = 3;
    while (this.ctrl) {
      i++;
      byte[] p1 = new byte[16];
      RTPpacket dtmf1 = new RTPpacket(p1, this.event, false, false, this.duration += 160);
      this.sender.sendpacket(dtmf1.getRawPacket());
      try {
        Thread.sleep(160L);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } 
    } 
  }
  
  public void stopSend() {
    this.ctrl = false;
    if (!isInterrupted())
      interrupt(); 
    byte[] p = new byte[16];
    RTPpacket dtmf = new RTPpacket(p, this.event, false, true, this.duration + 160);
    this.sender.sendpacket(dtmf.getRawPacket());
    this.sender.sendpacket(dtmf.getRawPacket());
    this.sender.sendpacket(dtmf.getRawPacket());
    this.sender.closedSocket();
  }
}
