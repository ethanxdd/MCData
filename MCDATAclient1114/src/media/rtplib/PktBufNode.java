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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PktBufNode
/*    */ {
/* 34 */   protected PktBufNode nextFrameQueueNode = null;
/*    */   
/* 36 */   protected PktBufNode prevFrameQueueNode = null;
/*    */   
/* 38 */   protected PktBufNode nextFrameNode = null;
/*    */   
/*    */   protected int pktCount;
/*    */   
/*    */   protected long timeStamp;
/*    */   
/*    */   protected int seqNum;
/*    */   
/* 46 */   protected RtpPkt pkt = null;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected PktBufNode(RtpPkt aPkt) {
/* 53 */     this.pkt = aPkt;
/* 54 */     this.timeStamp = aPkt.getTimeStamp();
/* 55 */     this.seqNum = aPkt.getSeqNumber();
/* 56 */     this.pktCount = 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\rtplib\PktBufNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */