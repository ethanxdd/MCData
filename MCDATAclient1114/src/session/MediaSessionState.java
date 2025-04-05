/*     */ package session;
/*     */ 
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.SocketException;
/*     */ import java.util.Timer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MediaSessionState
/*     */ {
/*     */   public static final int SOMEONE_TALKING = 1;
/*     */   public static final int IDLE = 2;
/*     */   public static final int REVOKE = 3;
/*     */   public static final int GRANTED = 4;
/*     */   public static final int RINGBACK = 5;
/*     */   public static final int PEERPICKUP = 6;
/*     */   public static final int ENDSESSION = 7;
/*     */   public static final int NONE = 8;
/*     */   private boolean state = false;
/*     */   private int currentState;
/*     */   private String currentMsg;
/*     */   private MediaSipSession mediaSipSession;
/*     */   private Timer connectTimer;
/*     */   private Timer idleNatTimer;
/*     */   private MeaninglessSender rtpMeaninglessSender;
/*     */   private DatagramSocket rtpSocket;
/*     */   private boolean lock;
/*     */   private int Rtpport;
/*     */   private String localip;
/*     */   
/*     */   public MediaSessionState(MediaSipSession mediaSipSession) {
/*  37 */     this.mediaSipSession = mediaSipSession;
/*  38 */     this.lock = false;
/*  39 */     changeState(5, "", null);
/*     */   }
/*     */   
/*     */   public void changeState(int stateToChange, String message, String localip) {
/*  43 */     changeStateAndSendResult(stateToChange, message, false, localip);
/*     */   }
/*     */   
/*     */   public void changeState(int stateToChange, String message, boolean isEmergency, String localip) {
/*  47 */     changeStateAndSendResult(stateToChange, message, isEmergency, localip);
/*     */   }
/*     */   
/*     */   private void changeStateAndSendResult(int stateToChange, String message, boolean isEmergency, String localip) {
/*  51 */     if (this.connectTimer != null) {
/*  52 */       this.connectTimer.cancel();
/*  53 */       this.connectTimer = null;
/*     */     } 
/*  55 */     stopSendPacketWhenIdle();
/*  56 */     switch (stateToChange) {
/*     */       case 1:
/*  58 */         if (isEmergency) {
/*  59 */           sendEmergencyResult("緊急訊息!! " + message, 1);
/*     */         } else {
/*  61 */           sendResult(message, 1);
/*  62 */           this.mediaSipSession.setIsEmergency(false);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/*  68 */         this.mediaSipSession.setIsEmergency(false);
/*  69 */         setlocalip(localip);
/*  70 */         startSendPacketWhenIdle();
/*     */         return;
/*     */       
/*     */       case 3:
/*  74 */         sendResult(message, 3);
/*  75 */         this.mediaSipSession.setIsEmergency(false);
/*     */         return;
/*     */       case 4:
/*  78 */         if (isEmergency) {
/*  79 */           sendEmergencyResult("您正在發送緊急訊息!!", 4);
/*     */         } else {
/*  81 */           sendResult("您正在講話...", 4);
/*  82 */           this.mediaSipSession.setIsEmergency(false);
/*     */         } 
/*     */         return;
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
/*     */       case 5:
/*     */       case 6:
/*     */       case 7:
/* 117 */         this.mediaSipSession.setIsActive(false);
/* 118 */         sendResult("未開始通話", 7);
/* 119 */         this.mediaSipSession.setIsEmergency(false);
/*     */         return;
/*     */     } 
/* 122 */     sendResult("None", 8);
/* 123 */     this.mediaSipSession.setIsEmergency(false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/* 129 */     startSendPacketWhenIdle();
/*     */   }
/*     */   
/*     */   public void stop() {
/* 133 */     stopSendPacketWhenIdle();
/* 134 */     this.state = false;
/*     */   }
/*     */   
/*     */   public void setstate(boolean runstate) {
/* 138 */     this.state = runstate;
/*     */   }
/*     */   
/*     */   public boolean getstate() {
/* 142 */     return this.state;
/*     */   }
/*     */   
/*     */   public void tempstop() {
/* 146 */     stopSendPacketWhenIdle();
/*     */   }
/*     */   
/*     */   private void stopSendPacketWhenIdle() {
/* 150 */     if (this.rtpMeaninglessSender != null) {
/* 151 */       this.rtpMeaninglessSender.stop();
/*     */     }
/* 153 */     this.state = false;
/*     */     
/* 155 */     if (this.idleNatTimer != null) {
/* 156 */       this.idleNatTimer.cancel();
/* 157 */       this.idleNatTimer = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void startSendPacketWhenIdle() {
/* 164 */     if (this.rtpMeaninglessSender == null) {
/*     */       
/* 166 */       String ip = this.mediaSipSession.getPeerInfo().getPeerIp();
/* 167 */       int port = this.mediaSipSession.getPeerInfo().getPeerRtpPort();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 174 */         this.rtpSocket.setReuseAddress(true);
/*     */         
/* 176 */         this.rtpMeaninglessSender = new MeaninglessSender(this.rtpSocket, ip, port);
/* 177 */         this.rtpMeaninglessSender.setTag("SendPacketWhenIdle Meaningless send");
/* 178 */       } catch (SocketException e) {
/* 179 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */     
/* 183 */     this.rtpMeaninglessSender.printInfo();
/* 184 */     this.rtpMeaninglessSender.start();
/* 185 */     this.state = true;
/*     */   }
/*     */   
/*     */   public MeaninglessSender getMeaningSender() {
/* 189 */     return this.rtpMeaninglessSender;
/*     */   }
/*     */   
/*     */   public int getCurrentStateNow() {
/* 193 */     return this.currentState;
/*     */   }
/*     */   
/*     */   public String getCurrentMsg() {
/* 197 */     return this.currentMsg;
/*     */   }
/*     */   
/*     */   public void sendResult() {
/* 201 */     sendResult(this.currentMsg, this.currentState);
/*     */   }
/*     */   
/*     */   private void sendResult(String msg, int btnState) {
/* 205 */     send(msg, btnState, false);
/*     */   }
/*     */   
/*     */   private void sendEmergencyResult() {
/* 209 */     sendEmergencyResult(this.currentMsg, this.currentState);
/*     */   }
/*     */   
/*     */   private void sendEmergencyResult(String msg, int btnState) {
/* 213 */     send(msg, btnState, true);
/* 214 */     this.mediaSipSession.setIsEmergency(true);
/*     */   }
/*     */   
/*     */   private void send(String msg, int btnState, boolean isEmergency) {
/* 218 */     this.currentMsg = msg;
/* 219 */     this.currentState = btnState;
/*     */   }
/*     */   
/*     */   public void setRtpport(int port) {
/* 223 */     this.Rtpport = port;
/*     */   }
/*     */   
/*     */   public int getRtpport() {
/* 227 */     return this.Rtpport;
/*     */   }
/*     */   
/*     */   public void lock() {
/* 231 */     this.lock = true;
/*     */   }
/*     */   
/*     */   public void unlock() {
/* 235 */     this.lock = false;
/*     */   }
/*     */   
/*     */   public void setlocalip(String localip) {
/* 239 */     this.localip = localip;
/*     */   }
/*     */   
/*     */   public String getlocalip() {
/* 243 */     return this.localip;
/*     */   }
/*     */   
/*     */   public DatagramSocket getrtpSocket() {
/* 247 */     return this.rtpSocket;
/*     */   }
/*     */   
/*     */   public void setrtpSocket(DatagramSocket rtpSocket) {
/* 251 */     this.rtpSocket = rtpSocket;
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\session\MediaSessionState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */