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
/*     */ public class records
/*     */   implements RTPAppIntf {
/*  18 */   private final int BUFFER_SIZE = 160;
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
/*     */   private int localRtpPort;
/*     */   private int localRtcpPort;
/*     */   private String netaddress;
/*     */   private RTPSession rtpSession;
/*     */   private DatagramSocket rtpSocket;
/*     */   private DatagramSocket rtcpSocket;
/*     */   private boolean isRegistered = false;
/*     */   private boolean isReceived = false;
/*     */   private boolean state = false;
/*     */   private Thread thread;
/*     */   
/*     */   public void set(String address, int localPort, int remotePort) {
/*  41 */     this.netaddress = address;
/*  42 */     setPort(localPort, remotePort);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPort(int localPort, int remotePort) {
/*  47 */     this.remoteRtpPort = remotePort;
/*  48 */     this.remoteRtcpPort = remotePort + 3;
/*  49 */     this.localRtpPort = localPort;
/*  50 */     this.localRtcpPort = localPort + 3;
/*     */   }
/*     */   public void checkDeviceIsOK() {
/*  53 */     if (!AudioSystem.isLineSupported(Port.Info.MICROPHONE)) {
/*  54 */       System.out.println("Error! Please make sure that your microphone is available!");
/*  55 */       System.exit(-1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAudioFormat() {
/*  64 */     AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
/*  65 */     float rate = 8000.0F;
/*  66 */     int channels = 1;
/*  67 */     int sampleSize = 16;
/*  68 */     boolean bigEndian = true;
/*     */     
/*  70 */     this.format = new AudioFormat(encoding, rate, 8, channels, 1, rate, bigEndian);
/*     */   }
/*     */ 
/*     */   
/*     */   public void initRecorder() {
/*     */     try {
/*  76 */       this.microphone = AudioSystem.getTargetDataLine(this.format);
/*  77 */       this.microphone.open();
/*  78 */       this.microphone.start();
/*  79 */     } catch (LineUnavailableException e) {
/*  80 */       e.printStackTrace();
/*  81 */       System.exit(-1);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addNewParticipant(String networkAddress, int dstRtpPort, int dstRtcpPort, int srcRtpPort, int srcRtcpPort) {
/*     */     try {
/*  87 */       this.rtpSocket = new DatagramSocket(srcRtpPort);
/*  88 */       this.rtcpSocket = new DatagramSocket(srcRtcpPort);
/*  89 */       this.rtpSocket.setReuseAddress(true);
/*  90 */       this.rtcpSocket.setReuseAddress(true);
/*  91 */     } catch (Exception e) {
/*  92 */       System.out.println("RTPSession failed to obtain port");
/*  93 */       System.exit(-1);
/*     */     } 
/*     */     
/*  96 */     this.rtpSession = new RTPSession(this.rtpSocket, this.rtcpSocket);
/*  97 */     Participant p = new Participant(networkAddress, dstRtpPort, dstRtcpPort);
/*  98 */     this.rtpSession.addParticipant(p);
/*  99 */     this.rtpSession.RTPSessionRegister(this, null, null);
/* 100 */     this.isRegistered = true;
/*     */ 
/*     */     
/* 103 */     try { Thread.sleep(1000L); } catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */   
/*     */   public void startTalking() {
/* 108 */     this.thread = new Thread(new Runnable()
/*     */         {
/*     */           public void run() {
/* 111 */             System.out.println("start talking");
/* 112 */             byte[] data = new byte[160];
/* 113 */             int packetCount = 0;
/* 114 */             int nBytesRead = 0;
/* 115 */             while (nBytesRead != -1) {
/* 116 */               nBytesRead = records.this.microphone.read(data, 0, data.length);
/* 117 */               System.out.println("speaking record");
/* 118 */               if (!records.this.isRegistered)
/* 119 */                 nBytesRead = -1; 
/* 120 */               if (nBytesRead >= 0) {
/* 121 */                 records.this.rtpSession.sendData(data);
/*     */                 
/* 123 */                 packetCount++;
/*     */               } 
/*     */             } 
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 147 */     this.thread.start();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void stoptalking() {
/* 153 */     this.thread.interrupt();
/*     */     try {
/* 155 */       Thread.sleep(500L);
/* 156 */     } catch (InterruptedException e) {
/*     */       
/* 158 */       e.printStackTrace();
/*     */     } 
/* 160 */     this.microphone.close();
/* 161 */     this.microphone.stop();
/* 162 */     this.microphone.flush();
/* 163 */     this.isReceived = false;
/* 164 */     this.isRegistered = false;
/* 165 */     this.state = false;
/* 166 */     if (this.rtpSession != null)
/*     */     {
/* 168 */       this.rtpSession.endSession();
/*     */     }
/*     */     
/* 171 */     this.rtpSession = null;
/* 172 */     System.out.println("Stop speaking");
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkstate(boolean run) {
/* 177 */     if (run) {
/*     */       
/* 179 */       addNewParticipant(this.netaddress, this.remoteRtpPort, this.remoteRtcpPort, this.localRtpPort, this.localRtcpPort);
/* 180 */       startTalking();
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 185 */     else if (this.isRegistered) {
/* 186 */       Enumeration<Participant> list = this.rtpSession.getParticipants();
/* 187 */       while (list.hasMoreElements()) {
/* 188 */         Participant p = list.nextElement();
/* 189 */         this.rtpSession.removeParticipant(p);
/*     */       } 
/* 191 */       this.isReceived = false;
/* 192 */       this.isRegistered = false;
/* 193 */       this.rtpSession.endSession();
/* 194 */       this.rtpSession = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 206 */     System.out.println(String.valueOf(this.remoteRtpPort) + "+" + this.localRtpPort + "+" + this.netaddress);
/*     */ 
/*     */     
/* 209 */     setAudioFormat();
/* 210 */     initRecorder();
/* 211 */     this.state = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void userEvent(int type, Participant[] participant) {}
/*     */ 
/*     */   
/*     */   public int frameSize(int payloadType) {
/* 220 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void receiveData(DataFrame frame, Participant participant) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getstate() {
/* 230 */     return this.state;
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\record.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */