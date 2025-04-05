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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DataFrame
/*     */ {
/*     */   private long rtpTimestamp;
/*  35 */   private long timestamp = -1L;
/*     */   
/*     */   private long SSRC;
/*     */   
/*     */   private long[] CSRCs;
/*     */   
/*     */   private int payloadType;
/*     */   
/*     */   private boolean[] marks;
/*     */   
/*     */   private boolean anyMarked = false;
/*     */   
/*  47 */   private int isComplete = 0;
/*     */ 
/*     */   
/*     */   private byte[][] data;
/*     */   
/*     */   private int[] seqNum;
/*     */   
/*  54 */   private int totalLength = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   protected int lastSeqNum;
/*     */ 
/*     */ 
/*     */   
/*     */   protected int firstSeqNum;
/*     */ 
/*     */   
/*     */   protected int noPkts;
/*     */ 
/*     */ 
/*     */   
/*     */   protected DataFrame(PktBufNode aBufNode, Participant p, int noPkts) {
/*  70 */     this.noPkts = noPkts;
/*  71 */     RtpPkt aPkt = aBufNode.pkt;
/*  72 */     int pktCount = aBufNode.pktCount;
/*  73 */     this.firstSeqNum = aBufNode.pktCount;
/*     */ 
/*     */     
/*  76 */     this.rtpTimestamp = aBufNode.timeStamp;
/*  77 */     this.SSRC = aPkt.getSsrc();
/*  78 */     this.CSRCs = aPkt.getCsrcArray();
/*     */ 
/*     */     
/*  81 */     if (p.ntpGradient > 0.0D)
/*     */     {
/*  83 */       this.timestamp = p.ntpOffset + (long)(p.ntpGradient * (this.rtpTimestamp - p.lastSRRtpTs));
/*     */     }
/*     */ 
/*     */     
/*  87 */     int payloadLength = aPkt.getPayloadLength();
/*     */     
/*  89 */     this.data = new byte[aBufNode.pktCount][payloadLength];
/*  90 */     this.seqNum = new int[aBufNode.pktCount];
/*  91 */     this.marks = new boolean[aBufNode.pktCount];
/*     */     
/*     */     int i;
/*     */     
/*  95 */     for (i = 0; i < pktCount; i++) {
/*  96 */       aPkt = aBufNode.pkt;
/*  97 */       byte[] temp = aPkt.getPayload();
/*  98 */       this.totalLength += temp.length;
/*  99 */       if (temp.length == payloadLength) {
/* 100 */         this.data[i] = temp;
/* 101 */       } else if (temp.length < payloadLength) {
/* 102 */         System.arraycopy(temp, 0, this.data[i], 0, temp.length);
/*     */       } else {
/* 104 */         System.out.println("DataFrame() received node structure with increasing packet payload size.");
/*     */       } 
/*     */       
/* 107 */       this.seqNum[i] = aBufNode.seqNum;
/* 108 */       this.marks[i] = aBufNode.pkt.isMarked();
/* 109 */       if (this.marks[i]) {
/* 110 */         this.anyMarked = true;
/*     */       }
/*     */       
/* 113 */       aBufNode = aBufNode.nextFrameNode;
/*     */     } 
/*     */     
/* 116 */     this.lastSeqNum = this.seqNum[i - 1];
/*     */     
/* 118 */     if (noPkts > 0) {
/* 119 */       int seqDiff = this.firstSeqNum - this.lastSeqNum;
/* 120 */       if (seqDiff < 0)
/* 121 */         seqDiff = Integer.MAX_VALUE - this.firstSeqNum + this.lastSeqNum; 
/* 122 */       if (seqDiff == pktCount && pktCount == noPkts)
/* 123 */         this.isComplete = 1; 
/*     */     } else {
/* 125 */       this.isComplete = -1;
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
/*     */   public byte[][] getData() {
/* 141 */     return this.data;
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
/*     */   public byte[] getConcatenatedData() {
/* 153 */     if (this.noPkts < 2) {
/* 154 */       byte[] ret = new byte[this.totalLength];
/* 155 */       int pos = 0;
/*     */       
/* 157 */       for (int i = 0; i < this.data.length; i++) {
/* 158 */         int length = (this.data[i]).length;
/*     */ 
/*     */         
/* 161 */         if (pos + length > this.totalLength) {
/* 162 */           length = this.totalLength - pos;
/*     */         }
/* 164 */         System.arraycopy(this.data[i], 0, ret, pos, length);
/* 165 */         pos += (this.data[i]).length;
/*     */       } 
/* 167 */       return ret;
/*     */     } 
/* 169 */     return this.data[0];
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
/*     */   public long timestamp() {
/* 185 */     return this.timestamp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long rtpTimestamp() {
/* 195 */     return this.rtpTimestamp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int payloadType() {
/* 204 */     return this.payloadType;
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
/*     */   public int[] sequenceNumbers() {
/* 217 */     return this.seqNum;
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
/*     */   public boolean[] marks() {
/* 229 */     return this.marks;
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
/*     */   public boolean marked() {
/* 241 */     return this.anyMarked;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long ssrc() {
/* 250 */     return this.SSRC;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long[] csrcs() {
/* 259 */     return this.CSRCs;
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
/*     */   public int complete() {
/* 271 */     return this.isComplete;
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\rtplib\DataFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */