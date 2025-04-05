/*     */ package media.rtplib;
/*     */ 
/*     */ import java.net.InetSocketAddress;
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
/*     */ public class Participant
/*     */ {
/*     */   protected boolean unexpected = false;
/*  32 */   protected InetSocketAddress rtpAddress = null;
/*     */   
/*  34 */   protected InetSocketAddress rtcpAddress = null;
/*     */   
/*  36 */   protected InetSocketAddress rtpReceivedFromAddress = null;
/*     */   
/*  38 */   protected InetSocketAddress rtcpReceivedFromAddress = null;
/*     */ 
/*     */   
/*  41 */   protected long ssrc = -1L;
/*     */   
/*  43 */   protected String cname = null;
/*     */   
/*  45 */   protected String name = null;
/*     */   
/*  47 */   protected String email = null;
/*     */   
/*  49 */   protected String phone = null;
/*     */   
/*  51 */   protected String loc = null;
/*     */   
/*  53 */   protected String tool = null;
/*     */   
/*  55 */   protected String note = null;
/*     */   
/*  57 */   protected String priv = null;
/*     */ 
/*     */ 
/*     */   
/*  61 */   protected int firstSeqNumber = -1;
/*     */   
/*  63 */   protected int lastSeqNumber = 0;
/*     */   
/*  65 */   protected long seqRollOverCount = 0L;
/*     */   
/*  67 */   protected long receivedPkts = 0L;
/*     */   
/*  69 */   protected long receivedOctets = 0L;
/*     */   
/*  71 */   protected int receivedSinceLastSR = 0;
/*     */   
/*  73 */   protected int lastSRRseqNumber = 0;
/*     */   
/*  75 */   protected double interArrivalJitter = -1.0D;
/*     */   
/*  77 */   protected long lastRtpTimestamp = 0L;
/*     */ 
/*     */   
/*  80 */   protected long timeStampLSR = 0L;
/*     */   
/*  82 */   protected long timeReceivedLSR = 0L;
/*     */ 
/*     */   
/*  85 */   protected double ntpGradient = -1.0D;
/*     */   
/*  87 */   protected long ntpOffset = -1L;
/*     */   
/*  89 */   protected long lastNtpTs1 = 0L;
/*     */   
/*  91 */   protected long lastNtpTs2 = 0L;
/*     */   
/*  93 */   protected long lastSRRtpTs = 0L;
/*     */ 
/*     */   
/*  96 */   protected long timestampBYE = -1L;
/*     */ 
/*     */   
/*  99 */   protected PktBuffer pktBuffer = null;
/*     */ 
/*     */   
/* 102 */   protected long lastRtpPkt = -1L;
/*     */   
/* 104 */   protected long lastRtcpPkt = -1L;
/*     */   
/* 106 */   protected long addedByApp = -1L;
/*     */   
/* 108 */   protected long lastRtcpRRPkt = -1L;
/*     */   
/* 110 */   protected long secondLastRtcpRRPkt = -1L;
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
/*     */   public Participant(String networkAddress, int rtpPort, int rtcpPort) {
/* 127 */     if (rtpPort > 0) {
/*     */       try {
/* 129 */         this.rtpAddress = new InetSocketAddress(networkAddress, rtpPort);
/* 130 */       } catch (Exception e) {
/* 131 */         System.out.println("Couldn't resolve " + networkAddress);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 137 */     if (rtcpPort > 0) {
/*     */       try {
/* 139 */         this.rtcpAddress = new InetSocketAddress(networkAddress, rtcpPort);
/* 140 */       } catch (Exception e) {
/* 141 */         System.out.println("Couldn't resolve " + networkAddress);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Participant(InetSocketAddress rtpAdr, InetSocketAddress rtcpAdr, long SSRC) {
/* 151 */     this.rtpReceivedFromAddress = rtpAdr;
/* 152 */     this.rtcpReceivedFromAddress = rtcpAdr;
/* 153 */     this.ssrc = SSRC;
/* 154 */     this.unexpected = true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Participant() {
/* 159 */     System.out.println("Don't use the Participan(void) Constructor!");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   InetSocketAddress getRtpSocketAddress() {
/* 168 */     return this.rtpAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   InetSocketAddress getRtcpSocketAddress() {
/* 178 */     return this.rtcpAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   InetSocketAddress getRtpReceivedFromAddress() {
/* 188 */     return this.rtpAddress;
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
/*     */   InetSocketAddress getRtcpReceivedFromAddress() {
/* 200 */     return this.rtcpAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCNAME() {
/* 210 */     return this.cname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNAME() {
/* 220 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEmail() {
/* 229 */     return this.email;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPhone() {
/* 238 */     return this.phone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLocation() {
/* 247 */     return this.loc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNote() {
/* 256 */     return this.note;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPriv() {
/* 265 */     return this.priv;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTool() {
/* 274 */     return this.tool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getSSRC() {
/* 283 */     return this.ssrc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updateRRStats(int packetLength, RtpPkt pkt) {
/* 293 */     int curSeqNum = pkt.getSeqNumber();
/*     */     
/* 295 */     if (this.firstSeqNumber < 0) {
/* 296 */       this.firstSeqNumber = curSeqNum;
/*     */     }
/*     */     
/* 299 */     this.receivedOctets += packetLength;
/* 300 */     this.receivedSinceLastSR++;
/* 301 */     this.receivedPkts++;
/*     */     
/* 303 */     long curTime = System.currentTimeMillis();
/*     */     
/* 305 */     if (this.lastSeqNumber < curSeqNum) {
/*     */       
/* 307 */       this.lastSeqNumber = curSeqNum;
/*     */     }
/* 309 */     else if (this.lastSeqNumber - this.lastSeqNumber < -100) {
/*     */       
/* 311 */       this.lastSeqNumber = curSeqNum;
/* 312 */       this.seqRollOverCount++;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 319 */     if (this.lastRtpPkt > 0L) {
/*     */       
/* 321 */       long D = pkt.getTimeStamp() - curTime - this.lastRtpTimestamp - this.lastRtpPkt;
/* 322 */       if (D < 0L) {
/* 323 */         D *= -1L;
/*     */       }
/* 325 */       this.interArrivalJitter += (D - this.interArrivalJitter) / 16.0D;
/*     */     } 
/*     */     
/* 328 */     this.lastRtpPkt = curTime;
/* 329 */     this.lastRtpTimestamp = pkt.getTimeStamp();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long getExtHighSeqRecv() {
/* 340 */     return 65536L * this.seqRollOverCount + this.lastSeqNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getFractionLost() {
/* 350 */     int expected = this.lastSeqNumber - this.lastSRRseqNumber;
/* 351 */     if (expected < 0) {
/* 352 */       expected += 65536;
/*     */     }
/* 354 */     int fraction = 256 * (expected - this.receivedSinceLastSR);
/* 355 */     if (expected > 0) {
/* 356 */       fraction /= expected;
/*     */     } else {
/* 358 */       fraction = 0;
/*     */     } 
/*     */ 
/*     */     
/* 362 */     this.receivedSinceLastSR = 0;
/* 363 */     this.lastSRRseqNumber = this.lastSeqNumber;
/*     */     
/* 365 */     return fraction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long getLostPktCount() {
/* 376 */     long lost = getExtHighSeqRecv() - this.firstSeqNumber - this.receivedPkts;
/*     */     
/* 378 */     if (lost < 0L)
/* 379 */       lost = 0L; 
/* 380 */     return lost;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected double getInterArrivalJitter() {
/* 388 */     return this.interArrivalJitter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setTimeStampLSR(long ntp1, long ntp2) {
/* 399 */     byte[] high = StaticProcs.uIntLongToByteWord(ntp1);
/* 400 */     byte[] low = StaticProcs.uIntLongToByteWord(ntp2);
/* 401 */     low[3] = low[1];
/* 402 */     low[2] = low[0];
/* 403 */     low[1] = high[3];
/* 404 */     low[0] = high[2];
/*     */     
/* 406 */     this.timeStampLSR = StaticProcs.bytesToUIntLong(low, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long delaySinceLastSR() {
/* 416 */     if (this.timeReceivedLSR < 1L) {
/* 417 */       return 0L;
/*     */     }
/* 419 */     long delay = System.currentTimeMillis() - this.timeReceivedLSR;
/*     */ 
/*     */     
/* 422 */     return (long)(delay * 65.536D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void debugPrint() {
/* 429 */     System.out.print(" Participant.debugPrint() SSRC:" + this.ssrc + " CNAME:" + this.cname);
/* 430 */     if (this.rtpAddress != null)
/* 431 */       System.out.print(" RTP:" + this.rtpAddress.toString()); 
/* 432 */     if (this.rtcpAddress != null)
/* 433 */       System.out.print(" RTCP:" + this.rtcpAddress.toString()); 
/* 434 */     System.out.println("");
/*     */     
/* 436 */     System.out.println("                          Packets received:" + this.receivedPkts);
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\rtplib\Participant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */