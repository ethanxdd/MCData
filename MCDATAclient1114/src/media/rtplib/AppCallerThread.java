/*     */ package media.rtplib;
/*     */ 
/*     */ import java.util.Enumeration;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AppCallerThread
/*     */   extends Thread
/*     */ {
/*     */   RTPSession rtpSession;
/*     */   RTPAppIntf appl;
/*     */   
/*     */   protected AppCallerThread(RTPSession session, RTPAppIntf rtpApp) {
/*  47 */     this.rtpSession = session;
/*  48 */     this.appl = rtpApp;
/*     */   }
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
/*     */   public void run() {
/*  67 */     while (!this.rtpSession.endSession) {
/*     */       
/*  69 */       this.rtpSession.pktBufLock.lock();
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*     */         
/*  75 */         try { this.rtpSession.pktBufDataReady.await(); }
/*  76 */         catch (Exception e) { System.out.println("AppCallerThread:" + e.getMessage()); }
/*     */ 
/*     */         
/*  79 */         Enumeration<Participant> enu = this.rtpSession.partDb.getParticipants();
/*     */         
/*  81 */         while (enu.hasMoreElements()) {
/*  82 */           Participant p = enu.nextElement();
/*     */           
/*  84 */           boolean done = false;
/*     */ 
/*     */ 
/*     */           
/*  88 */           while (!done && (!p.unexpected || this.rtpSession.naiveReception) && 
/*  89 */             p.pktBuffer != null && p.pktBuffer.length > 0)
/*     */           {
/*  91 */             DataFrame aFrame = p.pktBuffer.popOldestFrame();
/*  92 */             if (aFrame == null) {
/*  93 */               done = true; continue;
/*     */             } 
/*  95 */             this.appl.receiveData(aFrame, p);
/*     */           }
/*     */         
/*     */         } 
/*     */       } finally {
/*     */         
/* 101 */         this.rtpSession.pktBufLock.unlock();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\rtplib\AppCallerThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */