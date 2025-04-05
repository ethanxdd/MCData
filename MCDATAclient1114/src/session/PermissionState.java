/*     */ package session;
/*     */ 
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
/*     */ import proxy.Proxy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PermissionState
/*     */ {
/*     */   public static final int HAS_PERMISSION = 1;
/*     */   public static final int HAS_NO_PERMISSION = 2;
/*     */   public static final int PENDING_REQUEST = 3;
/*     */   public static final int PENDING_RELEASE = 4;
/*     */   public static final int PENDING_REVOKE = 5;
/*     */   public static final int QUEUE = 6;
/*     */   private static final String TAG = "PermissionState";
/*  19 */   private int currentState = -1;
/*     */   private MediaSipSession mediaSipSession;
/*     */   private Timer timer;
/*     */   private Timer queueHintTimer;
/*     */   private Timer queueRequestTimer;
/*  24 */   private int queueNumber = 0;
/*     */   
/*     */   public PermissionState(MediaSipSession mediaSipSession) {
/*  27 */     this.mediaSipSession = mediaSipSession;
/*  28 */     this.currentState = 2;
/*     */   }
/*     */   
/*     */   public void changeState(int state) {
/*  32 */     stopReleaseTimer();
/*  33 */     stopQueueHintTimer();
/*  34 */     switch (state) {
/*     */       case 1:
/*  36 */         this.currentState = 1;
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/*  41 */         this.currentState = 2;
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 3:
/*  47 */         this.currentState = 3;
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/*  52 */         startReleaseTimer();
/*  53 */         this.currentState = 4;
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/*  58 */         this.currentState = 5;
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/*  63 */         startQueueHintTimer();
/*  64 */         sendRTCPQueueRequest();
/*  65 */         this.currentState = 3;
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  70 */     this.currentState = -1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setQueueNumber(int queueNumber) {
/*  76 */     this.queueNumber = queueNumber;
/*     */   }
/*     */   
/*     */   private void sendRTCPQueueRequest() {
/*  80 */     if (this.queueRequestTimer == null)
/*  81 */       this.queueRequestTimer = new Timer(); 
/*  82 */     this.queueRequestTimer.schedule(new TimerTask()
/*     */         {
/*     */           public void run() {
/*  85 */             Proxy.getInstance().sendRTCPQueueRequest(PermissionState.this.mediaSipSession);
/*     */           }
/*  87 */         },  5000L);
/*     */   }
/*     */   
/*     */   private void startQueueHintTimer() {
/*  91 */     stopQueueHintTimer();
/*  92 */     this.queueHintTimer = new Timer();
/*  93 */     TimerTask timerTask = new QueueHintTimerTask();
/*  94 */     this.queueHintTimer.schedule(timerTask, 0L, 1500L);
/*     */   }
/*     */   
/*     */   public void stopQueueHintTimer() {
/*  98 */     closeTimer(this.queueHintTimer);
/*  99 */     closeTimer(this.queueRequestTimer);
/* 100 */     this.queueHintTimer = null;
/* 101 */     this.queueRequestTimer = null;
/*     */   }
/*     */   
/*     */   private void closeTimer(Timer timer) {
/* 105 */     if (timer != null) {
/* 106 */       timer.cancel();
/* 107 */       timer.purge();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void startReleaseTimer() {
/* 112 */     if (this.timer == null && (this.currentState == 4 || this.currentState == 1)) {
/* 113 */       this.timer = new Timer();
/* 114 */       TimerTask timerTask = new ReleaseTimerTask(this);
/* 115 */       this.timer.schedule(timerTask, 1000L, 1000L);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void stopReleaseTimer() {
/* 120 */     if (this.timer != null) {
/* 121 */       this.timer.cancel();
/* 122 */       this.timer.purge();
/* 123 */       this.timer = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getCurrentState() {
/* 128 */     return this.currentState;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void sendMSGToTalkHandler(int permissionState) {}
/*     */ 
/*     */ 
/*     */   
/*     */   class QueueHintTimerTask
/*     */     extends TimerTask
/*     */   {
/*     */     public void run() {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   class ReleaseTimerTask
/*     */     extends TimerTask
/*     */   {
/*     */     private PermissionState permissionState;
/*     */ 
/*     */     
/*     */     private int count;
/*     */ 
/*     */     
/*     */     ReleaseTimerTask(PermissionState permissionState) {
/* 155 */       this.permissionState = permissionState;
/* 156 */       this.count = 0;
/*     */     }
/*     */     
/*     */     public void run() {
/* 160 */       String peerIp = "120.101.9.174";
/* 161 */       int peerRtcpPort = 5090;
/*     */ 
/*     */       
/* 164 */       if (this.permissionState.getCurrentState() != 2) {
/*     */         
/* 166 */         if (this.count >= 4) {
/*     */           
/* 168 */           this.permissionState.stopReleaseTimer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 174 */           cancel();
/*     */         } else {
/* 176 */           Proxy.getInstance().sendRtcpRelease(PermissionState.this.mediaSipSession);
/* 177 */           this.count++;
/*     */         } 
/*     */       } else {
/* 180 */         cancel();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void sendMSGToTalkHandler(int mbcpState, int peerPort) {}
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\session\PermissionState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */