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
/*     */ public class RtcpPktRR
/*     */   extends RtcpPkt
/*     */ {
/*     */   protected Participant[] reportees;
/*     */   protected long[] reporteeSsrc;
/*     */   protected int[] lossFraction;
/*     */   protected int[] lostPktCount;
/*     */   protected long[] extHighSeqRecv;
/*     */   protected long[] interArvJitter;
/*     */   protected long[] timeStampLSR;
/*     */   protected long[] delaySR;
/*     */   
/*     */   protected RtcpPktRR(Participant[] reportees, long ssrc) {
/*  29 */     this.reportees = null;
/*     */     
/*  31 */     this.reporteeSsrc = null;
/*     */     
/*  33 */     this.lossFraction = null;
/*     */     
/*  35 */     this.lostPktCount = null;
/*     */     
/*  37 */     this.extHighSeqRecv = null;
/*     */     
/*  39 */     this.interArvJitter = null;
/*     */     
/*  41 */     this.timeStampLSR = null;
/*     */     
/*  43 */     this.delaySR = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  52 */     this.packetType = 201;
/*     */     
/*  54 */     this.ssrc = ssrc;
/*  55 */     this.reportees = reportees;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RtcpPktRR(byte[] aRawPkt, int start, int rrCount) {
/*     */     int base;
/*     */     this.reportees = null;
/*     */     this.reporteeSsrc = null;
/*     */     this.lossFraction = null;
/*     */     this.lostPktCount = null;
/*     */     this.extHighSeqRecv = null;
/*     */     this.interArvJitter = null;
/*     */     this.timeStampLSR = null;
/*     */     this.delaySR = null;
/*  72 */     this.rawPkt = aRawPkt;
/*     */     
/*  74 */     if (rrCount < 0 && (!parseHeaders(start) || this.packetType != 201 || this.length < 1))
/*     */     {
/*     */ 
/*     */       
/*  78 */       this.problem = -201;
/*     */     }
/*     */ 
/*     */     
/*  82 */     if (rrCount > 0) {
/*  83 */       base = start + 28;
/*     */     } else {
/*  85 */       base = start + 8;
/*  86 */       rrCount = this.itemCount;
/*  87 */       this.ssrc = StaticProcs.bytesToUIntLong(aRawPkt, start + 4);
/*     */     } 
/*     */     
/*  90 */     if (rrCount > 0) {
/*  91 */       this.reporteeSsrc = new long[rrCount];
/*  92 */       this.lossFraction = new int[rrCount];
/*  93 */       this.lostPktCount = new int[rrCount];
/*  94 */       this.extHighSeqRecv = new long[rrCount];
/*  95 */       this.interArvJitter = new long[rrCount];
/*  96 */       this.timeStampLSR = new long[rrCount];
/*  97 */       this.delaySR = new long[rrCount];
/*     */       
/*  99 */       for (int i = 0; i < rrCount; i++) {
/* 100 */         int pos = base + i * 24;
/* 101 */         this.reporteeSsrc[i] = StaticProcs.bytesToUIntLong(aRawPkt, pos);
/* 102 */         this.lossFraction[i] = aRawPkt[pos + 4];
/* 103 */         aRawPkt[pos + 4] = 0;
/* 104 */         this.lostPktCount[i] = (int)StaticProcs.bytesToUIntLong(aRawPkt, pos + 4);
/* 105 */         this.extHighSeqRecv[i] = StaticProcs.bytesToUIntLong(aRawPkt, pos + 8);
/* 106 */         this.interArvJitter[i] = StaticProcs.bytesToUIntLong(aRawPkt, pos + 12);
/* 107 */         this.timeStampLSR[i] = StaticProcs.bytesToUIntLong(aRawPkt, pos + 16);
/* 108 */         this.delaySR[i] = StaticProcs.bytesToUIntLong(aRawPkt, pos + 20);
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
/*     */   protected void encode() {
/* 123 */     byte[] rRs = null;
/*     */     
/* 125 */     if (this.reportees != null) {
/* 126 */       rRs = encodeRR();
/* 127 */       this.rawPkt = new byte[rRs.length + 8];
/* 128 */       System.arraycopy(rRs, 0, this.rawPkt, 8, rRs.length);
/* 129 */       this.itemCount = this.reportees.length;
/*     */     } else {
/* 131 */       this.rawPkt = new byte[8];
/* 132 */       this.itemCount = 0;
/*     */     } 
/*     */ 
/*     */     
/* 136 */     writeHeaders();
/*     */ 
/*     */ 
/*     */     
/* 140 */     byte[] someBytes = StaticProcs.uIntLongToByteWord(this.ssrc);
/* 141 */     System.arraycopy(someBytes, 0, this.rawPkt, 4, 4);
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
/*     */   
/*     */   protected byte[] encodeRR() {
/* 161 */     byte[] ret = new byte[24 * this.reportees.length];
/*     */ 
/*     */     
/* 164 */     for (int i = 0; i < this.reportees.length; i++) {
/* 165 */       int offset = 24 * i;
/* 166 */       byte[] someBytes = StaticProcs.uIntLongToByteWord((this.reportees[i]).ssrc);
/* 167 */       System.arraycopy(someBytes, 0, ret, offset, 4);
/*     */ 
/*     */       
/* 170 */       someBytes = StaticProcs.uIntLongToByteWord(this.reportees[i].getLostPktCount());
/*     */       
/* 172 */       someBytes[0] = (byte)this.reportees[i].getFractionLost();
/*     */ 
/*     */       
/* 175 */       System.arraycopy(someBytes, 0, ret, 4 + offset, 4);
/*     */ 
/*     */       
/* 178 */       someBytes = StaticProcs.uIntLongToByteWord(this.reportees[i].getExtHighSeqRecv());
/* 179 */       System.arraycopy(someBytes, 0, ret, 8 + offset, 4);
/*     */ 
/*     */       
/* 182 */       if ((this.reportees[i]).interArrivalJitter >= 0.0D) {
/* 183 */         someBytes = StaticProcs.uIntLongToByteWord((long)(this.reportees[i]).interArrivalJitter);
/*     */       } else {
/* 185 */         someBytes = StaticProcs.uIntLongToByteWord(0L);
/*     */       } 
/* 187 */       System.arraycopy(someBytes, 0, ret, 12 + offset, 4);
/*     */ 
/*     */       
/* 190 */       someBytes = StaticProcs.uIntLongToByteWord((this.reportees[i]).timeStampLSR);
/* 191 */       System.arraycopy(someBytes, 0, ret, 16 + offset, 4);
/*     */ 
/*     */       
/* 194 */       if ((this.reportees[i]).timeReceivedLSR > 0L) {
/* 195 */         someBytes = StaticProcs.uIntLongToByteWord(this.reportees[i].delaySinceLastSR());
/*     */       } else {
/* 197 */         someBytes = StaticProcs.uIntLongToByteWord(0L);
/*     */       } 
/* 199 */       System.arraycopy(someBytes, 0, ret, 20 + offset, 4);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 204 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void debugPrint() {
/* 211 */     System.out.println("RtcpPktRR.debugPrint() ");
/* 212 */     if (this.reportees != null) {
/* 213 */       for (int i = 0; i < this.reportees.length; i++) {
/* 214 */         Participant part = this.reportees[i];
/* 215 */         System.out.println("     part.ssrc: " + part.ssrc + "  part.cname: " + part.cname);
/*     */       } 
/*     */     } else {
/* 218 */       for (int i = 0; i < this.reporteeSsrc.length; i++)
/* 219 */         System.out.println("     reporteeSSRC: " + this.reporteeSsrc[i] + "  timeStampLSR: " + this.timeStampLSR[i]); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\rtplib\RtcpPktRR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */