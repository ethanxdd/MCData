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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PktBuffer
/*     */ {
/*     */   RTPSession rtpSession;
/*     */   long SSRC;
/*     */   Participant p;
/*  43 */   int length = 0;
/*     */   
/*  45 */   PktBufNode oldest = null;
/*     */   
/*  47 */   PktBufNode newest = null;
/*     */ 
/*     */   
/*  50 */   int lastSeqNumber = -1;
/*     */   
/*  52 */   long lastTimestamp = -1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PktBuffer(RTPSession rtpSession, Participant p, RtpPkt aPkt) {
/*  62 */     this.rtpSession = rtpSession;
/*  63 */     this.p = p;
/*  64 */     this.SSRC = aPkt.getSsrc();
/*  65 */     PktBufNode newNode = new PktBufNode(aPkt);
/*  66 */     this.oldest = newNode;
/*  67 */     this.newest = newNode;
/*     */ 
/*     */     
/*  70 */     this.length = 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized int addPkt(RtpPkt aPkt) {
/*  81 */     if (aPkt == null) {
/*  82 */       System.out.println("! PktBuffer.addPkt(aPkt) aPkt was null");
/*  83 */       return -5;
/*     */     } 
/*     */     
/*  86 */     long timeStamp = aPkt.getTimeStamp();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  92 */     PktBufNode newNode = new PktBufNode(aPkt);
/*  93 */     if (aPkt.getSsrc() != this.SSRC) {
/*  94 */       System.out.println("PktBuffer.addPkt() SSRCs don't match!");
/*     */     }
/*     */     
/*  97 */     int retVal = 0;
/*  98 */     if (this.rtpSession.pktBufBehavior > 0) {
/*  99 */       retVal = bufferedAddPkt(newNode);
/* 100 */     } else if (this.rtpSession.pktBufBehavior == 0) {
/* 101 */       retVal = filteredAddPkt(newNode);
/* 102 */     } else if (this.rtpSession.pktBufBehavior == -1) {
/* 103 */       retVal = unfilteredAddPkt(newNode);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 113 */     return retVal;
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
/*     */   private int unfilteredAddPkt(PktBufNode newNode) {
/* 128 */     if (this.oldest != null) {
/* 129 */       this.oldest.nextFrameQueueNode = newNode;
/* 130 */       newNode.prevFrameQueueNode = this.oldest;
/* 131 */       this.oldest = newNode;
/*     */     } else {
/* 133 */       this.oldest = newNode;
/* 134 */       this.newest = newNode;
/*     */     } 
/* 136 */     return 0;
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
/*     */   private int filteredAddPkt(PktBufNode newNode) {
/* 150 */     if (this.length == 0) {
/*     */       
/* 152 */       this.newest = newNode;
/* 153 */       this.oldest = newNode;
/* 154 */       this.length = 1;
/*     */     
/*     */     }
/* 157 */     else if (newNode.timeStamp > this.newest.timeStamp || (newNode.seqNum > this.newest.seqNum && newNode.seqNum - this.newest.seqNum < 10)) {
/*     */       
/* 159 */       newNode.nextFrameQueueNode = this.newest;
/* 160 */       this.newest.prevFrameQueueNode = newNode;
/* 161 */       this.newest = newNode;
/* 162 */       this.length++;
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 168 */       return -1;
/*     */     } 
/*     */ 
/*     */     
/* 172 */     return 0;
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
/*     */   private int bufferedAddPkt(PktBufNode newNode) {
/* 189 */     if (this.length == 0) {
/*     */       
/* 191 */       this.newest = newNode;
/* 192 */       this.oldest = newNode;
/*     */     
/*     */     }
/* 195 */     else if (newNode.timeStamp > this.newest.timeStamp || newNode.seqNum > this.newest.seqNum) {
/*     */       
/* 197 */       newNode.nextFrameQueueNode = this.newest;
/* 198 */       this.newest.prevFrameQueueNode = newNode;
/* 199 */       this.newest = newNode;
/*     */     } else {
/*     */       
/* 202 */       if (!pktOnTime(newNode.timeStamp, newNode.seqNum) && this.rtpSession.pktBufBehavior > -1)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 208 */         return -1;
/*     */       }
/*     */ 
/*     */       
/* 212 */       PktBufNode tmpNode = this.newest;
/* 213 */       while (tmpNode.timeStamp > newNode.timeStamp) {
/* 214 */         tmpNode = tmpNode.nextFrameQueueNode;
/*     */       }
/*     */       
/* 217 */       if (tmpNode.timeStamp == newNode.timeStamp && 
/* 218 */         this.rtpSession.frameReconstruction && 
/* 219 */         newNode.seqNum != tmpNode.seqNum) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 224 */         int ret = addToFrame(tmpNode, newNode);
/* 225 */         if (ret != 0) {
/* 226 */           return ret;
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/* 231 */         if (tmpNode.timeStamp == newNode.timeStamp && newNode.seqNum == tmpNode.seqNum)
/*     */         {
/*     */ 
/*     */ 
/*     */           
/* 236 */           return -1;
/*     */         }
/*     */ 
/*     */         
/* 240 */         newNode.nextFrameQueueNode = tmpNode;
/* 241 */         newNode.prevFrameQueueNode = tmpNode.prevFrameQueueNode;
/*     */ 
/*     */         
/* 244 */         if (newNode.prevFrameQueueNode != null) {
/* 245 */           newNode.prevFrameQueueNode.nextFrameQueueNode = newNode;
/*     */         }
/* 247 */         tmpNode.prevFrameQueueNode = newNode;
/*     */         
/* 249 */         if (newNode.timeStamp > this.newest.timeStamp) {
/* 250 */           this.newest = newNode;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 256 */     this.length++;
/* 257 */     return 0;
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
/*     */   private int addToFrame(PktBufNode frameNode, PktBufNode newNode) {
/* 269 */     if (frameNode.seqNum < newNode.seqNum) {
/*     */       
/* 271 */       frameNode.pktCount++;
/*     */ 
/*     */       
/* 274 */       while (frameNode.nextFrameNode != null && 
/* 275 */         frameNode.nextFrameNode.seqNum < newNode.seqNum) {
/* 276 */         frameNode = frameNode.nextFrameNode;
/*     */       }
/*     */ 
/*     */       
/* 280 */       if (frameNode.nextFrameNode != null && 
/* 281 */         frameNode.nextFrameNode.seqNum == newNode.seqNum)
/*     */       {
/*     */ 
/*     */         
/* 285 */         return -2;
/*     */       }
/*     */       
/* 288 */       newNode.nextFrameNode = frameNode.nextFrameNode;
/* 289 */       frameNode.nextFrameNode = newNode;
/*     */     }
/*     */     else {
/*     */       
/* 293 */       newNode.nextFrameNode = frameNode;
/* 294 */       frameNode.pktCount++;
/*     */ 
/*     */       
/* 297 */       if (frameNode.nextFrameQueueNode != null) {
/* 298 */         frameNode.nextFrameQueueNode.prevFrameQueueNode = newNode;
/* 299 */         newNode.nextFrameQueueNode = frameNode.nextFrameQueueNode;
/* 300 */         frameNode.nextFrameQueueNode = null;
/*     */       } 
/* 302 */       if (frameNode.prevFrameQueueNode != null) {
/* 303 */         frameNode.prevFrameQueueNode.nextFrameQueueNode = newNode;
/* 304 */         newNode.prevFrameQueueNode = frameNode.prevFrameQueueNode;
/* 305 */         frameNode.prevFrameQueueNode = null;
/*     */       } 
/* 307 */       if (this.newest.timeStamp == newNode.timeStamp) {
/* 308 */         this.newest = newNode;
/*     */       }
/*     */     } 
/*     */     
/* 312 */     return 0;
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
/*     */   protected synchronized DataFrame popOldestFrame() {
/* 327 */     if (this.rtpSession.pktBufBehavior > 0) {
/* 328 */       return bufferedPopFrame();
/*     */     }
/* 330 */     return unbufferedPopFrame();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DataFrame unbufferedPopFrame() {
/* 341 */     if (this.oldest != null) {
/* 342 */       PktBufNode retNode = this.oldest;
/*     */       
/* 344 */       popFrameQueueCleanup(retNode, retNode.seqNum);
/*     */       
/* 346 */       return new DataFrame(retNode, this.p, 
/* 347 */           this.rtpSession.appIntf.frameSize(retNode.pkt.getPayloadType()));
/*     */     } 
/* 349 */     return null;
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
/*     */   private DataFrame bufferedPopFrame() {
/* 361 */     PktBufNode retNode = this.oldest;
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
/* 375 */     if (retNode != null && (retNode.seqNum == this.lastSeqNumber + 1 || retNode.seqNum == 0 || 
/* 376 */       this.length > this.rtpSession.pktBufBehavior || this.lastSeqNumber < 0)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 384 */       DataFrame df = new DataFrame(retNode, this.p, 
/* 385 */           this.rtpSession.appIntf.frameSize(this.oldest.pkt.getPayloadType()));
/*     */ 
/*     */       
/* 388 */       popFrameQueueCleanup(retNode, df.lastSeqNum);
/*     */       
/* 390 */       return df;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 398 */     return null;
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
/*     */   private void popFrameQueueCleanup(PktBufNode retNode, int highestSeq) {
/* 410 */     if (1 == this.length) {
/*     */       
/* 412 */       this.newest = null;
/* 413 */       this.oldest = null;
/*     */     } else {
/*     */       
/* 416 */       this.oldest = this.oldest.prevFrameQueueNode;
/* 417 */       this.oldest.nextFrameQueueNode = null;
/*     */     } 
/*     */ 
/*     */     
/* 421 */     this.length--;
/*     */ 
/*     */     
/* 424 */     this.lastSeqNumber = highestSeq;
/* 425 */     this.lastTimestamp = retNode.timeStamp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getLength() {
/* 433 */     return this.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean pktOnTime(long timeStamp, int seqNum) {
/* 443 */     if (this.lastSeqNumber == -1)
/*     */     {
/* 445 */       return true;
/*     */     }
/* 447 */     if (seqNum >= this.lastSeqNumber) {
/* 448 */       if (this.lastSeqNumber < 3 && timeStamp < this.lastTimestamp) {
/* 449 */         return false;
/*     */       }
/*     */     }
/* 452 */     else if (seqNum > 3 || timeStamp < this.lastTimestamp) {
/* 453 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 457 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void debugPrint() {
/* 464 */     System.out.println("PktBuffer.debugPrint() : length " + this.length + " SSRC " + this.SSRC + " lastSeqNum:" + this.lastSeqNumber);
/* 465 */     PktBufNode tmpNode = this.oldest;
/* 466 */     int i = 0;
/* 467 */     while (tmpNode != null) {
/*     */       
/* 469 */       System.out.println("   " + i + " seqNum:" + tmpNode.seqNum + " timeStamp: " + tmpNode.timeStamp + " pktCount:" + tmpNode.pktCount);
/* 470 */       i++;
/* 471 */       tmpNode = tmpNode.prevFrameQueueNode;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\rtplib\PktBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */