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
/*     */ 
/*     */ 
/*     */ public class RtpPkt
/*     */ {
/*     */   private boolean rawPktCurrent = false;
/*  43 */   private int version = 2;
/*     */   
/*     */   private int padding;
/*     */   
/*  47 */   private int extension = 0;
/*     */   
/*  49 */   private int marker = 0;
/*     */   
/*     */   private int payloadType;
/*     */   
/*     */   private int seqNumber;
/*     */   
/*     */   private long timeStamp;
/*     */   
/*     */   private long ssrc;
/*     */   
/*  59 */   private long[] csrcArray = null;
/*     */ 
/*     */   
/*  62 */   private byte[] rawPkt = null;
/*     */   
/*  64 */   private byte[] payload = null;
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
/*     */   protected RtpPkt(long aTimeStamp, long syncSource, int seqNum, int plt, byte[] pl) {
/*  76 */     int test = 0;
/*  77 */     test += setTimeStamp(aTimeStamp);
/*  78 */     test += setSsrc(syncSource);
/*  79 */     test += setSeqNumber(seqNum);
/*  80 */     test += setPayloadType(plt);
/*  81 */     test += setPayload(pl);
/*  82 */     if (test != 0) {
/*  83 */       System.out.println("RtpPkt() failed, check with checkPkt()");
/*     */     }
/*  85 */     this.rawPktCurrent = true;
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
/*     */   protected RtpPkt(byte[] aRawPkt, int packetSize) {
/* 101 */     if (aRawPkt == null) {
/* 102 */       System.out.println("RtpPkt(byte[]) Packet null");
/*     */     }
/*     */     
/* 105 */     int remOct = packetSize - 12;
/* 106 */     if (remOct >= 0) {
/* 107 */       this.rawPkt = aRawPkt;
/*     */       
/* 109 */       sliceFirstLine();
/* 110 */       if (this.version == 2) {
/* 111 */         sliceTimeStamp();
/* 112 */         sliceSSRC();
/* 113 */         if (remOct > 4 && getCsrcCount() > 0) {
/* 114 */           sliceCSRCs();
/* 115 */           remOct -= this.csrcArray.length * 4;
/*     */         } 
/*     */         
/* 118 */         if (remOct > 0) {
/* 119 */           slicePayload(remOct);
/*     */         }
/*     */ 
/*     */         
/* 123 */         checkPkt();
/*     */ 
/*     */         
/* 126 */         this.rawPktCurrent = true;
/*     */       } else {
/* 128 */         System.out.println("RtpPkt(byte[]) Packet is not version 2, giving up.");
/*     */       } 
/*     */     } else {
/* 131 */       System.out.println("RtpPkt(byte[]) Packet too small to be sliced");
/*     */     } 
/* 133 */     this.rawPktCurrent = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int checkPkt() {
/* 144 */     return 0;
/*     */   }
/*     */   
/*     */   protected int getHeaderLength() {
/* 148 */     return 12 + 4 * getCsrcCount();
/*     */   }
/*     */   protected int getPayloadLength() {
/* 151 */     return this.payload.length;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getVersion() {
/* 157 */     return this.version;
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
/*     */   protected boolean isMarked() {
/* 170 */     return (this.marker != 0);
/*     */   }
/*     */   protected int getPayloadType() {
/* 173 */     return this.payloadType;
/*     */   }
/*     */   
/*     */   protected int getSeqNumber() {
/* 177 */     return this.seqNumber;
/*     */   }
/*     */   protected long getTimeStamp() {
/* 180 */     return this.timeStamp;
/*     */   }
/*     */   protected long getSsrc() {
/* 183 */     return this.ssrc;
/*     */   }
/*     */   
/*     */   protected int getCsrcCount() {
/* 187 */     if (this.csrcArray != null) {
/* 188 */       return this.csrcArray.length;
/*     */     }
/* 190 */     return 0;
/*     */   }
/*     */   
/*     */   protected long[] getCsrcArray() {
/* 194 */     return this.csrcArray;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected byte[] encode() {
/* 201 */     if (!this.rawPktCurrent || this.rawPkt == null) {
/* 202 */       writePkt();
/*     */     }
/* 204 */     return this.rawPkt;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void printPkt() {
/* 209 */     System.out.print("V:" + this.version + " P:" + this.padding + " EXT:" + this.extension);
/* 210 */     System.out.println(" CC:" + getCsrcCount() + " M:" + this.marker + " PT:" + this.payloadType + " SN: " + this.seqNumber);
/* 211 */     System.out.println("Timestamp:" + this.timeStamp + "(long output as int, may be 2s complement)");
/* 212 */     System.out.println("SSRC:" + this.ssrc + "(long output as int, may be 2s complement)");
/* 213 */     for (int i = 0; i < getCsrcCount(); i++) {
/* 214 */       System.out.println("CSRC:" + this.csrcArray[i] + "(long output as int, may be 2s complement)");
/*     */     }
/*     */     
/* 217 */     System.out.println("Payload, first four bytes: " + this.payload[0] + " " + this.payload[1] + " " + this.payload[2] + " " + this.payload[3]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setMarked(boolean mark) {
/* 223 */     this.rawPktCurrent = false;
/* 224 */     if (mark) {
/* 225 */       this.marker = 1;
/*     */     } else {
/* 227 */       this.marker = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int setPayloadType(int plType) {
/* 234 */     int temp = plType & 0x7F;
/* 235 */     if (temp == plType) {
/* 236 */       this.rawPktCurrent = false;
/* 237 */       this.payloadType = temp;
/* 238 */       return 0;
/*     */     } 
/* 240 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int setSeqNumber(int number) {
/* 245 */     if (number <= 65536 && number >= 0) {
/* 246 */       this.rawPktCurrent = false;
/* 247 */       this.seqNumber = number;
/* 248 */       return 0;
/*     */     } 
/* 250 */     System.out.println("RtpPkt.setSeqNumber: invalid number");
/* 251 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int setTimeStamp(long time) {
/* 256 */     this.rawPktCurrent = false;
/* 257 */     this.timeStamp = time;
/* 258 */     return 0;
/*     */   }
/*     */   
/*     */   protected int setSsrc(long source) {
/* 262 */     this.rawPktCurrent = false;
/* 263 */     this.ssrc = source;
/* 264 */     return 0;
/*     */   }
/*     */   
/*     */   protected int setCsrcs(long[] contributors) {
/* 268 */     if (contributors.length <= 16) {
/* 269 */       this.csrcArray = contributors;
/* 270 */       return 0;
/*     */     } 
/* 272 */     System.out.println("RtpPkt.setCsrcs: Cannot have more than 16 CSRCs");
/* 273 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int setPayload(byte[] data) {
/* 279 */     if (data.length < 1488) {
/* 280 */       this.rawPktCurrent = false;
/* 281 */       this.payload = data;
/* 282 */       return 0;
/*     */     } 
/* 284 */     System.out.println("RtpPkt.setPayload: Cannot carry more than 1480 bytes for now.");
/* 285 */     return -1;
/*     */   }
/*     */   
/*     */   protected byte[] getPayload() {
/* 289 */     return this.payload;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writePkt() {
/* 297 */     int bytes = getPayloadLength();
/* 298 */     int headerLen = getHeaderLength();
/* 299 */     int csrcLen = getCsrcCount();
/* 300 */     this.rawPkt = new byte[headerLen + bytes];
/*     */ 
/*     */     
/* 303 */     writeFirstLine();
/* 304 */     byte[] someBytes = StaticProcs.uIntLongToByteWord(this.timeStamp); int i;
/* 305 */     for (i = 0; i < 4; i++) {
/* 306 */       this.rawPkt[i + 4] = someBytes[i];
/*     */     }
/*     */ 
/*     */     
/* 310 */     someBytes = StaticProcs.uIntLongToByteWord(this.ssrc);
/* 311 */     System.arraycopy(someBytes, 0, this.rawPkt, 8, 4);
/*     */ 
/*     */     
/* 314 */     for (i = 0; i < csrcLen; i++) {
/* 315 */       someBytes = StaticProcs.uIntLongToByteWord(this.csrcArray[i]);
/* 316 */       System.arraycopy(someBytes, 0, this.rawPkt, 12 + 4 * i, 4);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 321 */     System.arraycopy(this.payload, 0, this.rawPkt, headerLen, bytes);
/* 322 */     this.rawPktCurrent = true;
/*     */   }
/*     */   
/*     */   private void writeFirstLine() {
/* 326 */     byte aByte = 0;
/* 327 */     aByte = (byte)(aByte | this.version << 6);
/* 328 */     aByte = (byte)(aByte | this.padding << 5);
/* 329 */     aByte = (byte)(aByte | this.extension << 4);
/* 330 */     aByte = (byte)(aByte | getCsrcCount());
/* 331 */     this.rawPkt[0] = aByte;
/* 332 */     aByte = 0;
/* 333 */     aByte = (byte)(aByte | this.marker << 7);
/* 334 */     aByte = (byte)(aByte | this.payloadType);
/* 335 */     this.rawPkt[1] = aByte;
/* 336 */     byte[] someBytes = StaticProcs.uIntIntToByteWord(this.seqNumber);
/* 337 */     this.rawPkt[2] = someBytes[0];
/* 338 */     this.rawPkt[3] = someBytes[1];
/*     */   }
/*     */   
/*     */   private void sliceFirstLine() {
/* 342 */     this.version = (this.rawPkt[0] & 0xC0) >>> 6;
/* 343 */     this.padding = (this.rawPkt[0] & 0x20) >>> 5;
/* 344 */     this.extension = (this.rawPkt[0] & 0x10) >>> 4;
/* 345 */     this.csrcArray = new long[this.rawPkt[0] & 0xF];
/* 346 */     this.marker = (this.rawPkt[1] & 0x80) >> 7;
/* 347 */     this.payloadType = this.rawPkt[1] & Byte.MAX_VALUE;
/* 348 */     this.seqNumber = StaticProcs.bytesToUIntInt(this.rawPkt, 2);
/*     */   }
/*     */   
/*     */   private void sliceTimeStamp() {
/* 352 */     this.timeStamp = StaticProcs.bytesToUIntLong(this.rawPkt, 4);
/*     */   }
/*     */   
/*     */   private void sliceSSRC() {
/* 356 */     this.ssrc = StaticProcs.bytesToUIntLong(this.rawPkt, 8);
/*     */   }
/*     */   
/*     */   private void sliceCSRCs() {
/* 360 */     for (int i = 0; i < this.csrcArray.length; i++) {
/* 361 */       this.ssrc = StaticProcs.bytesToUIntLong(this.rawPkt, i * 4 + 12);
/*     */     }
/*     */   }
/*     */   
/*     */   private void slicePayload(int bytes) {
/* 366 */     this.payload = new byte[bytes];
/* 367 */     int headerLen = getHeaderLength();
/*     */     
/* 369 */     System.arraycopy(this.rawPkt, headerLen, this.payload, 0, bytes);
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\rtplib\RtpPkt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */