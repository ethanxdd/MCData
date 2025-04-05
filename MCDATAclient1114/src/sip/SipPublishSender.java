/*    */ package sip;
/*    */ 
/*    */ public class SipPublishSender
/*    */   extends Thread
/*    */ {
/*  6 */   private String TAG = "SipPublishSender";
/*    */   
/*    */   private boolean ctrl;
/*    */   
/*    */   private SipClient sipClient;
/*    */   
/*    */   private String status;
/*    */   private int expires;
/*    */   private String eTag;
/*    */   private String settingPublishETag;
/*    */   private String settingXml;
/*    */   
/*    */   public SipPublishSender(SipClient sipClient, String status, int expires) {
/* 19 */     this.ctrl = true;
/* 20 */     this.sipClient = sipClient;
/* 21 */     this.status = status;
/* 22 */     this.expires = expires;
/* 23 */     this.eTag = null;
/* 24 */     this.settingPublishETag = null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void run() {
/* 30 */     while (this.ctrl) {
/*    */       
/* 32 */       this.sipClient.sendPresencePublish(this.status, this.expires, this.eTag);
/*    */       
/*    */       try {
/* 35 */         if (this.expires >= 60) {
/* 36 */           Thread.sleep((this.expires / 2) * 1000L); continue;
/* 37 */         }  if (this.expires < 60);
/*    */ 
/*    */       
/*    */       }
/* 41 */       catch (InterruptedException interruptedException) {}
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setStatus(String status) {
/* 49 */     this.status = status;
/*    */   }
/*    */   
/*    */   public void setETag(String eTag) {
/* 53 */     this.eTag = eTag;
/*    */   }
/*    */   
/*    */   public String getETag() {
/* 57 */     return this.eTag;
/*    */   }
/*    */   
/*    */   public void setSettingPublishETag(String settingPublishETag) {
/* 61 */     this.settingPublishETag = settingPublishETag;
/*    */   }
/*    */   
/*    */   public void setSettingXml(String settingXml) {
/* 65 */     this.settingXml = settingXml;
/*    */   }
/*    */   
/*    */   public void stopSend() {
/* 69 */     this.ctrl = false;
/* 70 */     this.expires = 0;
/*    */     
/* 72 */     if (!isInterrupted())
/* 73 */       this.sipClient.sendPresencePublish("closed", this.expires, this.eTag); 
/* 74 */     interrupt();
/*    */   }
/*    */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\sip\SipPublishSender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */