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
/*     */ public class RtcpPktPSFB
/*     */   extends RtcpPkt
/*     */ {
/*     */   protected boolean notRelevant = false;
/*     */   private RTPSession rtpSession;
/*  33 */   protected long ssrcMediaSource = -1L;
/*     */ 
/*     */   
/*     */   protected int[] sliFirst;
/*     */ 
/*     */   
/*     */   protected int[] sliNumber;
/*     */ 
/*     */   
/*     */   protected int[] sliPictureId;
/*     */   
/*  44 */   protected int rpsiPadding = -1;
/*     */   
/*  46 */   protected int rpsiPayloadType = -1;
/*     */ 
/*     */ 
/*     */   
/*     */   protected byte[] rpsiBitString;
/*     */ 
/*     */ 
/*     */   
/*     */   protected byte[] alfBitString;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RtcpPktPSFB(long ssrcPacketSender, long ssrcMediaSource) {
/*  60 */     this.ssrc = ssrcPacketSender;
/*  61 */     this.ssrcMediaSource = ssrcMediaSource;
/*  62 */     this.packetType = 206;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void makePictureLossIndication() {
/*  69 */     this.itemCount = 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void makeSliceLossIndication(int[] sliFirst, int[] sliNumber, int[] sliPictureId) {
/*  80 */     this.itemCount = 2;
/*  81 */     this.sliFirst = sliFirst;
/*  82 */     this.sliNumber = sliNumber;
/*  83 */     this.sliPictureId = sliPictureId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void makeRefPictureSelIndic(int bitPadding, int payloadType, byte[] bitString) {
/*  94 */     this.itemCount = 3;
/*  95 */     this.rpsiPadding = bitPadding;
/*  96 */     this.rpsiPayloadType = payloadType;
/*  97 */     this.rpsiBitString = bitString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void makeAppLayerFeedback(byte[] bitString) {
/* 106 */     this.itemCount = 15;
/* 107 */     this.alfBitString = bitString;
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
/*     */   protected RtcpPktPSFB(byte[] aRawPkt, int start, RTPSession rtpSession) {
/* 121 */     this.rtpSession = rtpSession;
/*     */     
/* 123 */     this.rawPkt = aRawPkt;
/*     */     
/* 125 */     if (!parseHeaders(start) || this.packetType != 206 || this.length < 2) {
/*     */ 
/*     */ 
/*     */       
/* 129 */       this.problem = -206;
/*     */     } else {
/*     */       
/* 132 */       this.ssrcMediaSource = StaticProcs.bytesToUIntLong(aRawPkt, 8 + start);
/*     */       
/* 134 */       if (this.ssrcMediaSource == rtpSession.ssrc) {
/* 135 */         this.ssrc = StaticProcs.bytesToUIntLong(aRawPkt, 4 + start);
/*     */         
/* 137 */         switch (this.itemCount) {
/*     */           case 1:
/* 139 */             decPictureLossIndic();
/*     */             return;
/*     */           case 2:
/* 142 */             decSliceLossIndic(aRawPkt, start + 12);
/*     */             return;
/*     */           case 3:
/* 145 */             decRefPictureSelIndic(aRawPkt, start + 12);
/*     */             return;
/*     */           case 15:
/* 148 */             decAppLayerFB(aRawPkt, start + 12);
/*     */             return;
/*     */         } 
/* 151 */         System.out.println("!!!! RtcpPktPSFB(byte[], int start) unexpected FMT " + this.itemCount);
/*     */       } else {
/*     */         
/* 154 */         this.notRelevant = true;
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
/*     */   private void decPictureLossIndic() {
/* 167 */     if (this.rtpSession.rtcpAVPFIntf != null) {
/* 168 */       this.rtpSession.rtcpAVPFIntf.PSFBPktPictureLossReceived(
/* 169 */           this.ssrc);
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
/*     */   private void decSliceLossIndic(byte[] aRawPkt, int start) {
/* 186 */     int count = this.length - 2;
/*     */     
/* 188 */     this.sliFirst = new int[count];
/* 189 */     this.sliNumber = new int[count];
/* 190 */     this.sliPictureId = new int[count];
/*     */ 
/*     */     
/* 193 */     for (int i = 0; i < count; i++) {
/* 194 */       this.sliFirst[i] = StaticProcs.bytesToUIntInt(aRawPkt, start) >> 3;
/* 195 */       this.sliNumber[i] = (StaticProcs.bytesToUIntInt(aRawPkt, start) & 0x7FFC0) >> 6;
/* 196 */       this.sliPictureId[i] = StaticProcs.bytesToUIntInt(aRawPkt, start + 2) & 0x3F;
/* 197 */       start += 4;
/*     */     } 
/*     */     
/* 200 */     if (this.rtpSession.rtcpAVPFIntf != null) {
/* 201 */       this.rtpSession.rtcpAVPFIntf.PSFBPktSliceLossIndic(
/* 202 */           this.ssrc, 
/* 203 */           this.sliFirst, this.sliNumber, this.sliPictureId);
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
/*     */   private void decRefPictureSelIndic(byte[] aRawPkt, int start) {
/* 222 */     this.rpsiPadding = aRawPkt[start];
/*     */     
/* 224 */     if (this.rpsiPadding > 32) {
/* 225 */       System.out.println("!!!! RtcpPktPSFB.decRefPictureSelcIndic paddingBits: " + 
/* 226 */           this.rpsiPadding);
/*     */     }
/*     */     
/* 229 */     this.rpsiPayloadType = this.rawPkt[start];
/* 230 */     if (this.rpsiPayloadType < 0) {
/* 231 */       System.out.println("!!!! RtcpPktPSFB.decRefPictureSelcIndic 8th bit not zero: " + 
/* 232 */           this.rpsiPayloadType);
/*     */     }
/*     */     
/* 235 */     this.rpsiBitString = new byte[(this.length - 2) * 4 - 2];
/* 236 */     System.arraycopy(aRawPkt, start + 2, this.rpsiBitString, 0, this.rpsiBitString.length);
/*     */     
/* 238 */     if (this.rtpSession.rtcpAVPFIntf != null) {
/* 239 */       this.rtpSession.rtcpAVPFIntf.PSFBPktRefPictureSelIndic(
/* 240 */           this.ssrc, 
/* 241 */           this.rpsiPayloadType, this.rpsiBitString, this.rpsiPadding);
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
/*     */   private void decAppLayerFB(byte[] aRawPkt, int start) {
/* 254 */     int stringLength = (this.length - 2) * 4;
/*     */     
/* 256 */     this.alfBitString = new byte[stringLength];
/*     */     
/* 258 */     System.arraycopy(aRawPkt, start, this.alfBitString, 0, stringLength);
/*     */     
/* 260 */     if (this.rtpSession.rtcpAVPFIntf != null) {
/* 261 */       this.rtpSession.rtcpAVPFIntf.PSFBPktAppLayerFBReceived(
/* 262 */           this.ssrc, this.alfBitString);
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
/*     */   private void encSliceLossIndic() {
/* 276 */     int offset = 8;
/*     */     
/* 278 */     for (int i = 0; i < this.sliFirst.length; i++) {
/* 279 */       offset = 8 + 8 * i;
/* 280 */       byte[] firstBytes = StaticProcs.uIntLongToByteWord((this.sliFirst[i] << 3));
/* 281 */       byte[] numbBytes = StaticProcs.uIntLongToByteWord((this.sliNumber[i] << 2));
/* 282 */       byte[] picBytes = StaticProcs.uIntIntToByteWord(this.sliPictureId[i]);
/*     */       
/* 284 */       this.rawPkt[offset] = firstBytes[2];
/* 285 */       this.rawPkt[offset + 1] = (byte)(firstBytes[3] | numbBytes[2]);
/* 286 */       this.rawPkt[offset + 2] = numbBytes[3];
/* 287 */       this.rawPkt[offset + 3] = (byte)(numbBytes[3] | picBytes[1]);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void encRefPictureSelIndic() {
/* 297 */     byte[] someBytes = StaticProcs.uIntIntToByteWord(this.rpsiPadding);
/* 298 */     this.rawPkt[8] = someBytes[1];
/* 299 */     someBytes = StaticProcs.uIntIntToByteWord(this.rpsiPayloadType);
/* 300 */     this.rawPkt[9] = someBytes[1];
/*     */     
/* 302 */     System.arraycopy(this.rpsiBitString, 0, this.rawPkt, 10, this.rpsiBitString.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void encAppLayerFB() {
/* 312 */     System.arraycopy(this.alfBitString, 0, this.rawPkt, 8, this.alfBitString.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getFMT() {
/* 320 */     return this.itemCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void encode() {
/* 329 */     switch (this.itemCount) {
/*     */       
/*     */       case 1:
/* 332 */         this.rawPkt = new byte[24];
/*     */         break;
/*     */       case 2:
/* 335 */         this.rawPkt = new byte[24 + 4 * this.sliFirst.length];
/* 336 */         encSliceLossIndic();
/*     */         break;
/*     */       case 3:
/* 339 */         this.rawPkt = new byte[26 + this.rpsiBitString.length / 4];
/* 340 */         encRefPictureSelIndic();
/*     */         break;
/*     */       case 15:
/* 343 */         this.rawPkt = new byte[24 + this.alfBitString.length / 4];
/* 344 */         encAppLayerFB();
/*     */         break;
/*     */     } 
/*     */     
/* 348 */     byte[] someBytes = StaticProcs.uIntLongToByteWord(this.ssrc);
/* 349 */     System.arraycopy(someBytes, 0, this.rawPkt, 4, 4);
/* 350 */     someBytes = StaticProcs.uIntLongToByteWord(this.ssrcMediaSource);
/* 351 */     System.arraycopy(someBytes, 0, this.rawPkt, 8, 4);
/*     */     
/* 353 */     writeHeaders();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void debugPrint() {
/*     */     String str;
/* 360 */     System.out.println("->RtcpPktPSFB.debugPrint() ");
/*     */ 
/*     */     
/* 363 */     switch (this.itemCount) {
/*     */       case 1:
/* 365 */         System.out.println("  FMT: Picture Loss Indication");
/*     */         break;
/*     */       case 2:
/* 368 */         if (this.sliFirst != null) {
/* 369 */           str = "sliFirst[].length: " + this.sliFirst.length;
/*     */         } else {
/* 371 */           str = "sliFirst[] is null";
/*     */         } 
/* 373 */         System.out.println("  FMT: Slice Loss Indication, " + str);
/*     */         break;
/*     */       case 3:
/* 376 */         if (this.rpsiBitString != null) {
/* 377 */           str = "rpsiBitString[].length: " + this.rpsiBitString.length;
/*     */         } else {
/* 379 */           str = "rpsiBitString[] is null";
/*     */         } 
/* 381 */         System.out.println("  FMT: Reference Picture Selection Indication, " + 
/* 382 */             str + " payloadType: " + this.rpsiPayloadType);
/*     */         break;
/*     */       case 15:
/* 385 */         if (this.alfBitString != null) {
/* 386 */           str = "alfBitString[].length: " + this.alfBitString.length;
/*     */         } else {
/* 388 */           str = "alfBitString[] is null";
/*     */         } 
/* 390 */         System.out.println("  FMT: Application Layer Feedback Messages, " + str);
/*     */         break;
/*     */     } 
/*     */     
/* 394 */     System.out.println("<-RtcpPktPSFB.debugPrint() ");
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\rtplib\RtcpPktPSFB.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */