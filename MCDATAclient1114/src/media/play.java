/*     */ package media;
/*     */ 
/*     */ import java.net.DatagramSocket;
/*     */ import java.util.Enumeration;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import javax.sound.sampled.AudioSystem;
/*     */ import javax.sound.sampled.LineUnavailableException;
/*     */ import javax.sound.sampled.Port;
/*     */ import javax.sound.sampled.SourceDataLine;
/*     */ import javax.sound.sampled.TargetDataLine;
/*     */ import media.rtplib.DataFrame;
/*     */ import media.rtplib.Participant;
/*     */ import media.rtplib.RTPAppIntf;
/*     */ import media.rtplib.RTPSession;
/*     */ 
/*     */ public class play implements RTPAppIntf {
/*  17 */   private final int BUFFER_SIZE = 160;
/*     */   
/*     */   private AudioFormat format;
/*     */   
/*     */   private TargetDataLine microphone;
/*     */   
/*     */   private SourceDataLine speaker;
/*     */   
/*     */   private int remoteRtpPort;
/*     */   
/*     */   private int remoteRtcpPort;
/*     */   
/*     */   private int localRtpPort;
/*     */   
/*     */   private int localRtcpPort;
/*     */   
/*     */   private String netaddress;
/*     */   private RTPSession rtpSession;
/*     */   private DatagramSocket rtpSocket;
/*     */   private DatagramSocket rtcpSocket;
/*     */   private boolean isRegistered = false;
/*     */   private boolean isReceived = false;
/*     */   private boolean state = false;
/*     */   
/*     */   public void set(String address, int localPort, int remotePort) {
/*  42 */     this.netaddress = address;
/*  43 */     setPort(localPort, remotePort);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPort(int localPort, int remotePort) {
/*  48 */     this.remoteRtpPort = remotePort;
/*  49 */     this.remoteRtcpPort = remotePort + 3;
/*  50 */     this.localRtpPort = localPort;
/*  51 */     this.localRtcpPort = localPort + 3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkDeviceIsOK() {
/*  59 */     if (!AudioSystem.isLineSupported(Port.Info.SPEAKER) && !AudioSystem.isLineSupported(Port.Info.HEADPHONE)) {
/*  60 */       System.out.println("Error! Please make sure that your speaker or headphone is available!");
/*  61 */       System.exit(-1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAudioFormat() {
/*  67 */     AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
/*  68 */     float rate = 8000.0F;
/*  69 */     int channels = 1;
/*  70 */     int sampleSize = 16;
/*  71 */     boolean bigEndian = false;
/*     */     
/*  73 */     this.format = new AudioFormat(encoding, rate, 8, channels, 1, rate, bigEndian);
/*     */   }
/*     */   
/*     */   public void initPlayer() {
/*     */     try {
/*  78 */       this.speaker = AudioSystem.getSourceDataLine(this.format);
/*  79 */       this.speaker.open();
/*  80 */       this.speaker.start();
/*  81 */     } catch (LineUnavailableException e) {
/*  82 */       e.printStackTrace();
/*  83 */       System.exit(-1);
/*     */     } 
/*     */   }
/*     */   public void addNewParticipant(String networkAddress, int dstRtpPort, int dstRtcpPort, int srcRtpPort, int srcRtcpPort) {
/*     */     try {
/*  88 */       this.rtpSocket = new DatagramSocket(srcRtpPort);
/*  89 */       this.rtcpSocket = new DatagramSocket(srcRtcpPort);
/*  90 */       this.rtpSocket.setReuseAddress(true);
/*  91 */       this.rtcpSocket.setReuseAddress(true);
/*  92 */     } catch (Exception e) {
/*  93 */       System.out.println("RTPSession failed to obtain port");
/*  94 */       System.exit(-1);
/*     */     } 
/*     */     
/*  97 */     this.rtpSession = new RTPSession(this.rtpSocket, this.rtcpSocket);
/*  98 */     Participant p = new Participant(networkAddress, dstRtpPort, dstRtcpPort);
/*  99 */     this.rtpSession.addParticipant(p);
/* 100 */     this.rtpSession.RTPSessionRegister(this, null, null);
/* 101 */     this.isRegistered = true;
/*     */ 
/*     */     
/* 104 */     try { Thread.sleep(1000L); } catch (Exception exception) {}
/*     */   }
/*     */   
/*     */   public void receiveData(DataFrame frame, Participant participant) {
/* 108 */     if (this.speaker != null) {
/* 109 */       byte[] data = frame.getConcatenatedData();
/* 110 */       this.speaker.write(data, 0, data.length);
/* 111 */       if (!this.isReceived) {
/* 112 */         System.out.println("Received caller's data");
/* 113 */         this.isReceived = true;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void stopPlay() {
/* 119 */     this.isReceived = false;
/* 120 */     this.isRegistered = false;
/* 121 */     this.speaker.close();
/* 122 */     this.speaker.stop();
/* 123 */     this.speaker.flush();
/* 124 */     this.state = false;
/* 125 */     if (this.rtpSession != null)
/*     */     {
/* 127 */       this.rtpSession.endSession();
/*     */     }
/* 129 */     this.rtpSession = null;
/*     */   }
/*     */   
/*     */   public void checkstate(boolean run) {
/* 133 */     if (run) {
/*     */       
/* 135 */       addNewParticipant(this.netaddress, this.remoteRtpPort, this.remoteRtcpPort, this.localRtpPort, this.localRtcpPort);
/*     */ 
/*     */     
/*     */     }
/* 139 */     else if (this.isRegistered) {
/* 140 */       Enumeration<Participant> list = this.rtpSession.getParticipants();
/* 141 */       while (list.hasMoreElements()) {
/* 142 */         Participant p = list.nextElement();
/* 143 */         this.rtpSession.removeParticipant(p);
/*     */       } 
/* 145 */       this.isReceived = false;
/* 146 */       this.isRegistered = false;
/* 147 */       this.rtpSession.endSession();
/* 148 */       this.rtpSession = null;
/* 149 */       System.out.println("end session");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void userEvent(int type, Participant[] participant) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public int frameSize(int payloadType) {
/* 161 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 168 */     addNewParticipant(this.netaddress, this.remoteRtpPort, this.remoteRtcpPort, this.localRtpPort, this.localRtcpPort);
/*     */     
/* 170 */     setAudioFormat();
/*     */     
/* 172 */     initPlayer();
/* 173 */     this.state = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getstate() {
/* 178 */     return this.state;
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\play.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */