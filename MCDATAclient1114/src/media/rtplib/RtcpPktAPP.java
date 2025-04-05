/*    */ package media.rtplib;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RtcpPktAPP
/*    */   extends RtcpPkt
/*    */ {
/* 28 */   protected byte[] pktName = null;
/*    */   
/* 30 */   protected byte[] pktData = null;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected RtcpPktAPP(long ssrc, int subtype, byte[] pktName, byte[] pktData) {
/* 42 */     this.ssrc = ssrc;
/* 43 */     this.packetType = 204;
/* 44 */     this.itemCount = subtype;
/* 45 */     this.pktName = pktName;
/* 46 */     this.pktData = pktData;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected RtcpPktAPP(byte[] aRawPkt, int start) {
/* 56 */     this.ssrc = StaticProcs.bytesToUIntLong(aRawPkt, 4);
/* 57 */     this.rawPkt = aRawPkt;
/*    */     
/* 59 */     if (!parseHeaders(start) || this.packetType != 204) {
/*    */ 
/*    */ 
/*    */       
/* 63 */       this.problem = -204;
/*    */     } else {
/*    */       
/* 66 */       if (this.length > 1) {
/* 67 */         this.pktName = new byte[4];
/* 68 */         System.arraycopy(aRawPkt, 8, this.pktName, 0, 4);
/*    */       } 
/* 70 */       if (this.length > 2) {
/* 71 */         this.pktData = new byte[(this.length + 1) * 4 - 12];
/* 72 */         System.arraycopy(aRawPkt, 12, this.pktData, 0, this.pktData.length);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void encode() {
/* 83 */     this.rawPkt = new byte[12 + this.pktData.length];
/* 84 */     byte[] tmp = StaticProcs.uIntLongToByteWord(this.ssrc);
/* 85 */     System.arraycopy(tmp, 0, this.rawPkt, 4, 4);
/* 86 */     System.arraycopy(this.pktName, 0, this.rawPkt, 8, 4);
/* 87 */     System.arraycopy(this.pktData, 0, this.rawPkt, 12, this.pktData.length);
/* 88 */     writeHeaders();
/*    */   }
/*    */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\rtplib\RtcpPktAPP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */