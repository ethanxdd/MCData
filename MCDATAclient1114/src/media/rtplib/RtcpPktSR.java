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
/*     */ public class RtcpPktSR
/*     */   extends RtcpPkt
/*     */ {
/*  29 */   protected long ntpTs1 = -1L;
/*     */   
/*  31 */   protected long ntpTs2 = -1L;
/*     */   
/*  33 */   protected long rtpTs = -1L;
/*     */   
/*  35 */   protected long sendersPktCount = -1L;
/*     */   
/*  37 */   protected long sendersOctCount = -1L;
/*     */   
/*  39 */   protected RtcpPktRR rReports = null;
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
/*     */   protected RtcpPktSR(long ssrc, long pktCount, long octCount, RtcpPktRR rReports) {
/*  51 */     this.ssrc = ssrc;
/*  52 */     this.packetType = 200;
/*  53 */     this.sendersPktCount = pktCount;
/*  54 */     this.sendersOctCount = octCount;
/*  55 */     this.rReports = rReports;
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
/*     */   protected RtcpPktSR(byte[] aRawPkt, int start, int length) {
/*  70 */     this.rawPkt = aRawPkt;
/*     */     
/*  72 */     if (!parseHeaders(start) || this.packetType != 200) {
/*     */ 
/*     */ 
/*     */       
/*  76 */       this.problem = -200;
/*     */     } else {
/*  78 */       this.ssrc = StaticProcs.bytesToUIntLong(aRawPkt, 4 + start);
/*  79 */       if (length > 11)
/*  80 */         this.ntpTs1 = StaticProcs.bytesToUIntLong(aRawPkt, 8 + start); 
/*  81 */       if (length > 15)
/*  82 */         this.ntpTs2 = StaticProcs.bytesToUIntLong(aRawPkt, 12 + start); 
/*  83 */       if (length > 19)
/*  84 */         this.rtpTs = StaticProcs.bytesToUIntLong(aRawPkt, 16 + start); 
/*  85 */       if (length > 23)
/*  86 */         this.sendersPktCount = StaticProcs.bytesToUIntLong(aRawPkt, 20 + start); 
/*  87 */       if (length > 27) {
/*  88 */         this.sendersOctCount = StaticProcs.bytesToUIntLong(aRawPkt, 24 + start);
/*     */       }
/*     */       
/*  91 */       if (this.itemCount > 0) {
/*  92 */         this.rReports = new RtcpPktRR(this.rawPkt, start, this.itemCount);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void encode() {
/* 115 */     if (this.rReports != null) {
/* 116 */       this.itemCount = this.rReports.reportees.length;
/*     */       
/* 118 */       byte[] arrayOfByte = this.rReports.encodeRR();
/* 119 */       this.rawPkt = new byte[arrayOfByte.length + 28];
/*     */ 
/*     */       
/* 122 */       System.arraycopy(arrayOfByte, 0, this.rawPkt, 28, arrayOfByte.length);
/*     */     } else {
/*     */       
/* 125 */       this.itemCount = 0;
/* 126 */       this.rawPkt = new byte[28];
/*     */     } 
/*     */ 
/*     */     
/* 130 */     writeHeaders();
/*     */ 
/*     */     
/* 133 */     long timeNow = System.currentTimeMillis();
/* 134 */     this.ntpTs1 = 2208988800L + timeNow / 1000L;
/* 135 */     long ms = timeNow % 1000L;
/* 136 */     double tmp = ms / 1000.0D;
/* 137 */     tmp *= 4.294967295E9D;
/* 138 */     this.ntpTs2 = (long)tmp;
/* 139 */     this.rtpTs = System.currentTimeMillis();
/*     */ 
/*     */ 
/*     */     
/* 143 */     byte[] someBytes = StaticProcs.uIntLongToByteWord(this.ssrc);
/* 144 */     System.arraycopy(someBytes, 0, this.rawPkt, 4, 4);
/* 145 */     someBytes = StaticProcs.uIntLongToByteWord(this.ntpTs1);
/* 146 */     System.arraycopy(someBytes, 0, this.rawPkt, 8, 4);
/* 147 */     someBytes = StaticProcs.uIntLongToByteWord(this.ntpTs2);
/* 148 */     System.arraycopy(someBytes, 0, this.rawPkt, 12, 4);
/* 149 */     someBytes = StaticProcs.uIntLongToByteWord(this.rtpTs);
/* 150 */     System.arraycopy(someBytes, 0, this.rawPkt, 16, 4);
/* 151 */     someBytes = StaticProcs.uIntLongToByteWord(this.sendersPktCount);
/* 152 */     System.arraycopy(someBytes, 0, this.rawPkt, 20, 4);
/* 153 */     someBytes = StaticProcs.uIntLongToByteWord(this.sendersOctCount);
/* 154 */     System.arraycopy(someBytes, 0, this.rawPkt, 24, 4);
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
/*     */   public void debugPrint() {
/* 166 */     System.out.println("RtcpPktSR.debugPrint() ");
/* 167 */     System.out.println("  SSRC:" + Long.toString(this.ssrc) + " ntpTs1:" + Long.toString(this.ntpTs1) + 
/* 168 */         " ntpTS2:" + Long.toString(this.ntpTs2) + " rtpTS:" + Long.toString(this.rtpTs) + 
/* 169 */         " senderPktCount:" + Long.toString(this.sendersPktCount) + " sendersOctetCount:" + 
/* 170 */         Long.toString(this.sendersOctCount));
/* 171 */     if (this.rReports != null) {
/* 172 */       System.out.print("  Part of Sender Report: ");
/* 173 */       this.rReports.debugPrint();
/* 174 */       System.out.println("  End Sender Report");
/*     */     } else {
/* 176 */       System.out.println("No Receiver Reports associated with this Sender Report.");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\rtplib\RtcpPktSR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */