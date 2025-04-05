/*     */ package media.rtplib;
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
/*     */ public class RtcpPktBYE
/*     */   extends RtcpPkt
/*     */ {
/*  28 */   protected long[] ssrcArray = null;
/*     */   
/*  30 */   protected byte[] reason = null;
/*     */   
/*     */   protected RtcpPktBYE(long[] ssrcs, byte[] aReason) {
/*  33 */     this.packetType = 203;
/*     */     
/*  35 */     this.reason = aReason;
/*  36 */     this.ssrcArray = ssrcs;
/*  37 */     if (ssrcs.length < 1) {
/*  38 */       System.out.println("RtcpBYE.RtcpPktBYE(long[] ssrcs, byte[] aReason) requires at least one SSRC!");
/*     */     }
/*     */   }
/*     */   
/*     */   protected RtcpPktBYE(byte[] aRawPkt, int start) {
/*  43 */     this.rawPkt = aRawPkt;
/*  44 */     if (!parseHeaders(start) || this.packetType != 203) {
/*     */ 
/*     */ 
/*     */       
/*  48 */       this.problem = -203;
/*     */     } else {
/*  50 */       this.ssrcArray = new long[this.itemCount];
/*     */       
/*  52 */       for (int i = 0; i < this.itemCount; i++) {
/*  53 */         this.ssrcArray[i] = StaticProcs.bytesToUIntLong(aRawPkt, start + (i + 1) * 4);
/*     */       }
/*  55 */       if (this.length > this.itemCount + 1) {
/*  56 */         int reasonLength = aRawPkt[start + (this.itemCount + 1) * 4];
/*     */         
/*  58 */         this.reason = new byte[reasonLength];
/*  59 */         System.arraycopy(aRawPkt, start + (this.itemCount + 1) * 4 + 1, this.reason, 0, reasonLength);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void encode() {
/*  66 */     this.itemCount = this.ssrcArray.length;
/*  67 */     this.length = 4 * this.ssrcArray.length;
/*     */     
/*  69 */     if (this.reason != null) {
/*  70 */       this.length += (this.reason.length + 1) / 4;
/*  71 */       if ((this.reason.length + 1) % 4 != 0) {
/*  72 */         this.length++;
/*     */       }
/*     */     } 
/*  75 */     this.rawPkt = new byte[this.length * 4 + 4];
/*     */ 
/*     */     
/*     */     int i;
/*     */ 
/*     */     
/*  81 */     for (i = 0; i < this.ssrcArray.length; i++) {
/*  82 */       byte[] someBytes = StaticProcs.uIntLongToByteWord(this.ssrcArray[i]);
/*  83 */       System.arraycopy(someBytes, 0, this.rawPkt, 4 + 4 * i, 4);
/*     */     } 
/*     */ 
/*     */     
/*  87 */     if (this.reason != null) {
/*     */       
/*  89 */       this.rawPkt[4 + 4 * this.ssrcArray.length] = (byte)this.reason.length;
/*  90 */       System.arraycopy(this.reason, 0, this.rawPkt, 4 + 4 * i + 1, this.reason.length);
/*     */     } 
/*  92 */     writeHeaders();
/*     */   }
/*     */   
/*     */   public void debugPrint() {
/*  96 */     System.out.println("RtcpPktBYE.debugPrint() ");
/*  97 */     if (this.ssrcArray != null) {
/*  98 */       for (int i = 0; i < this.ssrcArray.length; i++) {
/*  99 */         long anSsrc = this.ssrcArray[i];
/* 100 */         System.out.println("     ssrc: " + anSsrc);
/*     */       } 
/*     */     }
/* 103 */     if (this.reason != null)
/* 104 */       System.out.println("     Reason: " + new String(this.reason)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\rtplib\RtcpPktBYE.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */