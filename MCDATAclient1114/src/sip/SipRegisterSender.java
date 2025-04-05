/*    */ package sip;
/*    */ 
/*    */ 
/*    */ public class SipRegisterSender
/*    */   extends Thread
/*    */ {
/*    */   private static final String TAG = "SipRegisterSender";
/*    */   private boolean registerCtrl;
/*    */   private SipClient sipClient;
/*    */   private static int expires;
/*    */   
/*    */   public SipRegisterSender(SipClient sipClient, int expires) {
/* 13 */     this.registerCtrl = true;
/* 14 */     this.sipClient = sipClient;
/* 15 */     SipRegisterSender.expires = expires;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/* 20 */     while (this.registerCtrl) {
/* 21 */       this.sipClient.sendRegister(expires);
/*    */       try {
/* 23 */         if (expires >= 60) {
/*    */ 
/*    */           
/* 26 */           Thread.sleep((expires / 2) * 1000L); continue;
/* 27 */         }  if (expires < 60);
/*    */ 
/*    */ 
/*    */       
/*    */       }
/* 32 */       catch (InterruptedException interruptedException) {}
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void stopSend() {
/* 41 */     this.registerCtrl = false;
/* 42 */     expires = 0;
/* 43 */     if (!isInterrupted()) {
/* 44 */       interrupt();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public static int getExpires() {
/* 50 */     return expires;
/*    */   }
/*    */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\sip\SipRegisterSender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */