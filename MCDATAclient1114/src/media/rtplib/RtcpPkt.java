/*     */ package media.rtplib;
/*     */ 
/*     */ import java.net.InetAddress;
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
/*     */ public class RtcpPkt
/*     */ {
/*  30 */   protected int problem = 0;
/*     */   
/*  32 */   protected int version = 2;
/*     */   
/*  34 */   protected int padding = 0;
/*     */   
/*  36 */   protected int itemCount = 0;
/*     */   
/*  38 */   protected int packetType = -1;
/*     */   
/*  40 */   protected int length = -1;
/*     */   
/*  42 */   protected long ssrc = -1L;
/*     */ 
/*     */   
/*  45 */   protected byte[] rawPkt = null;
/*     */ 
/*     */   
/*  48 */   protected long time = -1L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean received = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean parseHeaders(int start) {
/*  60 */     this.version = (this.rawPkt[start + 0] & 0xC0) >>> 6;
/*  61 */     this.padding = (this.rawPkt[start + 0] & 0x20) >>> 5;
/*  62 */     this.itemCount = this.rawPkt[start + 0] & 0x1F;
/*  63 */     this.packetType = this.rawPkt[start + 1];
/*  64 */     if (this.packetType < 0) {
/*  65 */       this.packetType += 256;
/*     */     }
/*  67 */     this.length = StaticProcs.bytesToUIntInt(this.rawPkt, start + 2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     if (this.packetType > 207 || this.packetType < 200) {
/*  75 */       System.out.println("RtcpPkt.parseHeaders problem discovered, packetType " + this.packetType);
/*     */     }
/*  77 */     if (this.version == 2 && this.length < 65536) {
/*  78 */       return true;
/*     */     }
/*  80 */     System.out.println("RtcpPkt.parseHeaders() failed header checks, check size and version");
/*  81 */     this.problem = -1;
/*  82 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeHeaders() {
/*  91 */     byte aByte = 0;
/*  92 */     aByte = (byte)(aByte | this.version << 6);
/*  93 */     aByte = (byte)(aByte | this.padding << 5);
/*  94 */     aByte = (byte)(aByte | this.itemCount);
/*  95 */     this.rawPkt[0] = aByte;
/*  96 */     aByte = 0;
/*  97 */     aByte = (byte)(aByte | this.packetType);
/*  98 */     this.rawPkt[1] = aByte;
/*  99 */     if (this.rawPkt.length % 4 != 0)
/* 100 */       System.out.println("!!!! RtcpPkt.writeHeaders() rawPkt was not a multiple of 32 bits / 4 octets!"); 
/* 101 */     byte[] someBytes = StaticProcs.uIntIntToByteWord(this.rawPkt.length / 4 - 1);
/* 102 */     this.rawPkt[2] = someBytes[0];
/* 103 */     this.rawPkt[3] = someBytes[1];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void encode() {
/* 110 */     System.out.println("RtcpPkt.encode() should never be invoked!! " + this.packetType);
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
/*     */   protected boolean check(InetAddress adr, ParticipantDatabase partDb) {
/* 124 */     if (partDb.rtpSession.mcSession && adr.equals(partDb.rtpSession.mcGroup)) {
/* 125 */       return true;
/*     */     }
/*     */     
/* 128 */     Participant part = partDb.getParticipant(this.ssrc);
/* 129 */     if (part != null && part.rtcpAddress.getAddress().equals(adr)) {
/* 130 */       return true;
/*     */     }
/*     */     
/* 133 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\rtplib\RtcpPkt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */