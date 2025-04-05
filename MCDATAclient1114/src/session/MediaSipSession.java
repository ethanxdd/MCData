/*     */ package session;
/*     */ 
/*     */ import gov.nist.javax.sip.address.SipUri;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.SocketException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.sip.Dialog;
/*     */ import javax.sip.Transaction;
/*     */ import proxy.Proxy;
/*     */ import sip.SipClient;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MediaSipSession
/*     */ {
/*  18 */   private static String TAG = "MediaSipSession";
/*     */   
/*     */   private static String sipTarget;
/*     */   
/*     */   private static String callId;
/*     */   
/*     */   private String myTag;
/*     */   
/*     */   private String callIdIp;
/*     */   
/*     */   private boolean isActive;
/*     */   private boolean isfirstTalk;
/*     */   private static PermissionState permissionState;
/*     */   private Dialog dialog;
/*     */   private MediaSessionState mediaSessionState;
/*     */   private Transaction inviteTransacation;
/*     */   private boolean isEmergency;
/*     */   private Map<String, String> userActiveMap;
/*     */   private PeerInfo peerInfo;
/*     */   private MeaninglessSender rtcpMeaninglesSender;
/*     */   private boolean isConnect = false;
/*     */   private boolean isGroup;
/*     */   
/*     */   public MediaSipSession(String callId, String tag, Dialog dialog, String sipTarget, SipUri peerSipUri, boolean isGroup) throws SocketException {
/*  42 */     MediaSipSession.callId = callId;
/*  43 */     this.myTag = tag;
/*  44 */     this.dialog = dialog;
/*  45 */     MediaSipSession.sipTarget = sipTarget;
/*     */     
/*  47 */     this.isGroup = isGroup;
/*  48 */     createSipSession(peerSipUri);
/*     */   }
/*     */ 
/*     */   
/*     */   private void createSipSession(SipUri peerSipUri) {
/*  53 */     this.isfirstTalk = true;
/*  54 */     this.isActive = false;
/*  55 */     permissionState = new PermissionState(this);
/*     */     
/*  57 */     this.userActiveMap = new HashMap<>();
/*  58 */     this.mediaSessionState = new MediaSessionState(this);
/*  59 */     this.peerInfo = new PeerInfo(peerSipUri);
/*     */   }
/*     */   
/*     */   public Dialog getDialog() {
/*  63 */     return this.dialog;
/*     */   }
/*     */   
/*     */   public void setDialog(Dialog dialog) {
/*  67 */     this.dialog = dialog;
/*     */   }
/*     */   
/*     */   public static String getCallId() {
/*  71 */     return callId;
/*     */   }
/*     */   
/*     */   public String getMyTag() {
/*  75 */     return this.myTag;
/*     */   }
/*     */   
/*     */   public String getCallIdIp() {
/*  79 */     return this.callIdIp;
/*     */   }
/*     */   
/*     */   public boolean isfirstTalk() {
/*  83 */     return this.isfirstTalk;
/*     */   }
/*     */   
/*     */   public void setIsfirstTalk(boolean isfirstTalk) {
/*  87 */     this.isfirstTalk = isfirstTalk;
/*     */   }
/*     */   
/*     */   public boolean isActive() {
/*  91 */     return this.isActive;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIsActive(boolean isActive) {
/*  96 */     this.isActive = isActive;
/*  97 */     if (isActive) {
/*  98 */       SipClient sipClient = SipClient.getInstance();
/*  99 */       if (sipClient != null) {
/* 100 */         subscribeGroup(sipClient);
/*     */       }
/*     */       
/* 103 */       if (this.rtcpMeaninglesSender == null) {
/* 104 */         DatagramSocket socket = Proxy.getInstance().getRTCPSocket();
/* 105 */         this.rtcpMeaninglesSender = new MeaninglessSender(socket, this.peerInfo.getPeerIp(), this.peerInfo.getPeerRtcpPort());
/* 106 */         this.rtcpMeaninglesSender.setTag("setIsActive: true  Meaningless send");
/*     */       } 
/* 108 */       this.rtcpMeaninglesSender.start();
/*     */     }
/* 110 */     else if (this.rtcpMeaninglesSender != null) {
/* 111 */       this.rtcpMeaninglesSender.stop();
/*     */     } 
/*     */ 
/*     */     
/* 115 */     if (!this.isGroup) {
/* 116 */       this.isConnect = isActive;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void subscribeGroup(final SipClient sipClient) {
/* 122 */     Runnable runnable = new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/* 126 */           sipClient.sendConferenceSubscribe(MediaSipSession.sipTarget);
/* 127 */           Thread.currentThread().interrupt();
/*     */         }
/*     */       };
/* 130 */     Thread thread = new Thread(runnable);
/* 131 */     thread.setName("subscribeGroup");
/* 132 */     thread.start();
/*     */   }
/*     */   
/*     */   public MeaninglessSender getMeaningSender() {
/* 136 */     return this.rtcpMeaninglesSender;
/*     */   }
/*     */   
/*     */   public static PermissionState getPermissionState() {
/* 140 */     return permissionState;
/*     */   }
/*     */   
/*     */   public static String getSipTarget() {
/* 144 */     return sipTarget;
/*     */   }
/*     */   
/*     */   public MediaSessionState getMediaSessionState() {
/* 148 */     return this.mediaSessionState;
/*     */   }
/*     */   
/*     */   public Transaction getInviteTransacation() {
/* 152 */     return this.inviteTransacation;
/*     */   }
/*     */   
/*     */   public void setInviteTransacation(Transaction inviteTransacation) {
/* 156 */     this.inviteTransacation = inviteTransacation;
/*     */   }
/*     */   
/*     */   public boolean isUAC() {
/* 160 */     if (this.inviteTransacation instanceof gov.nist.javax.sip.stack.SIPClientTransactionImpl) {
/* 161 */       return true;
/*     */     }
/* 163 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmergency() {
/* 169 */     return this.isEmergency;
/*     */   }
/*     */   
/*     */   public void setIsEmergency(boolean isEmergency) {
/* 173 */     this.isEmergency = isEmergency;
/*     */   }
/*     */   
/*     */   public Map<String, String> getUserActiveMap() {
/* 177 */     return this.userActiveMap;
/*     */   }
/*     */   
/*     */   public void setUserActiveMap(Map<String, String> userActiveMap) {
/* 181 */     this.userActiveMap = userActiveMap;
/*     */   }
/*     */   
/*     */   public PeerInfo getPeerInfo() {
/* 185 */     return this.peerInfo;
/*     */   }
/*     */   
/*     */   public boolean isConnect() {
/* 189 */     return this.isConnect;
/*     */   }
/*     */   
/*     */   public void setIsConnect(boolean isConnect) {
/* 193 */     this.isConnect = isConnect;
/*     */   }
/*     */   
/*     */   public boolean isGroup() {
/* 197 */     return this.isGroup;
/*     */   }
/*     */   
/*     */   public void setIsGroup(boolean isGroup) {
/* 201 */     this.isGroup = isGroup;
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\session\MediaSipSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */