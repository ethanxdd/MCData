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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RtcpPktRTPFB
/*     */   extends RtcpPkt
/*     */ {
/*     */   protected boolean notRelevant = false;
/*  33 */   protected long ssrcMediaSource = -1L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int[] PID;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int[] BLP;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RtcpPktRTPFB(long ssrcPacketSender, long ssrcMediaSource, int FMT, int[] PID, int[] BLP) {
/*  49 */     this.packetType = 205;
/*  50 */     this.itemCount = FMT;
/*  51 */     this.PID = PID;
/*  52 */     this.BLP = BLP;
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
/*     */   protected RtcpPktRTPFB(byte[] aRawPkt, int start, RTPSession rtpSession) {
/*  67 */     this.rawPkt = aRawPkt;
/*     */     
/*  69 */     if (!parseHeaders(start) || this.packetType != 205 || this.length < 2) {
/*     */ 
/*     */ 
/*     */       
/*  73 */       this.problem = -205;
/*     */     }
/*     */     else {
/*     */       
/*  77 */       this.ssrcMediaSource = StaticProcs.bytesToUIntLong(aRawPkt, 8 + start);
/*     */       
/*  79 */       if (this.ssrcMediaSource == rtpSession.ssrc) {
/*  80 */         this.ssrc = StaticProcs.bytesToUIntLong(aRawPkt, 4 + start);
/*  81 */         int loopStop = this.length - 2;
/*  82 */         this.PID = new int[loopStop];
/*  83 */         this.BLP = new int[loopStop];
/*  84 */         int curStart = 12;
/*     */ 
/*     */         
/*  87 */         for (int i = 0; i < loopStop; i++) {
/*  88 */           this.PID[i] = StaticProcs.bytesToUIntInt(aRawPkt, curStart);
/*  89 */           this.BLP[i] = StaticProcs.bytesToUIntInt(aRawPkt, curStart + 2);
/*  90 */           curStart += 4;
/*     */         } 
/*     */         
/*  93 */         rtpSession.rtcpAVPFIntf.RTPFBPktReceived(
/*  94 */             this.ssrc, this.itemCount, this.PID, this.BLP);
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void encode() {
/* 111 */     this.rawPkt = new byte[12 + this.PID.length * 4];
/*     */     
/* 113 */     byte[] someBytes = StaticProcs.uIntLongToByteWord(this.ssrc);
/* 114 */     System.arraycopy(someBytes, 0, this.rawPkt, 4, 4);
/* 115 */     someBytes = StaticProcs.uIntLongToByteWord(this.ssrcMediaSource);
/* 116 */     System.arraycopy(someBytes, 0, this.rawPkt, 8, 4);
/*     */ 
/*     */     
/* 119 */     int curStart = 12;
/* 120 */     for (int i = 0; i < this.PID.length; i++) {
/* 121 */       someBytes = StaticProcs.uIntIntToByteWord(this.PID[i]);
/* 122 */       this.rawPkt[curStart++] = someBytes[0];
/* 123 */       this.rawPkt[curStart++] = someBytes[1];
/* 124 */       someBytes = StaticProcs.uIntIntToByteWord(this.BLP[i]);
/* 125 */       this.rawPkt[curStart++] = someBytes[0];
/* 126 */       this.rawPkt[curStart++] = someBytes[1];
/*     */     } 
/* 128 */     writeHeaders();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getFMT() {
/* 136 */     return this.itemCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void debugPrint() {
/* 143 */     System.out.println("->RtcpPktRTPFB.debugPrint() ");
/* 144 */     System.out.println("  ssrcPacketSender: " + this.ssrc + "  ssrcMediaSource: " + this.ssrcMediaSource);
/*     */     
/* 146 */     if (this.PID != null && this.PID.length < 1) {
/* 147 */       System.out.println("  No Feedback Control Information (FCI) fields");
/*     */     }
/*     */     
/* 150 */     for (int i = 0; i < this.PID.length; i++) {
/* 151 */       System.out.println("  FCI -> PID: " + this.PID[i] + "  BLP: " + this.BLP[i]);
/*     */     }
/* 153 */     System.out.println("<-RtcpPktRTPFB.debugPrint() ");
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\rtplib\RtcpPktRTPFB.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */