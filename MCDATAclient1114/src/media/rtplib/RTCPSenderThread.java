/*     */ package media.rtplib;
/*     */ 
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
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
/*     */ public class RTCPSenderThread
/*     */   extends Thread
/*     */ {
/*  37 */   private RTPSession rtpSession = null;
/*     */   
/*  39 */   private RTCPSession rtcpSession = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean byesSent = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RTCPSenderThread(RTCPSession rtcpSession, RTPSession rtpSession) {
/*  50 */     this.rtpSession = rtpSession;
/*  51 */     this.rtcpSession = rtcpSession;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sendByes() {
/*     */     byte[] reasonBytes;
/*  63 */     CompRtcpPkt compPkt = new CompRtcpPkt();
/*     */ 
/*     */     
/*  66 */     RtcpPktSR srPkt = new RtcpPktSR(this.rtpSession.ssrc, 
/*  67 */         this.rtpSession.sentPktCount, this.rtpSession.sentOctetCount, null);
/*  68 */     compPkt.addPacket(srPkt);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  73 */     long[] ssrcArray = { this.rtpSession.ssrc };
/*  74 */     if (this.rtpSession.conflict) {
/*  75 */       reasonBytes = "SSRC collision".getBytes();
/*     */     } else {
/*  77 */       reasonBytes = "jlibrtp says bye bye!".getBytes();
/*     */     } 
/*  79 */     RtcpPktBYE byePkt = new RtcpPktBYE(ssrcArray, reasonBytes);
/*     */     
/*  81 */     compPkt.addPacket(byePkt);
/*     */ 
/*     */     
/*  84 */     if (this.rtpSession.mcSession) {
/*  85 */       mcSendCompRtcpPkt(compPkt);
/*     */     } else {
/*  87 */       Iterator<Participant> iter = this.rtpSession.partDb.getUnicastReceivers();
/*     */       
/*  89 */       while (iter.hasNext()) {
/*  90 */         Participant part = iter.next();
/*  91 */         if (part.rtcpAddress != null) {
/*  92 */           sendCompRtcpPkt(compPkt, part.rtcpAddress);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int mcSendCompRtcpPkt(CompRtcpPkt pkt) {
/*     */     DatagramPacket packet;
/* 105 */     byte[] pktBytes = pkt.encode();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 110 */       packet = new DatagramPacket(pktBytes, pktBytes.length, this.rtpSession.mcGroup, this.rtcpSession.rtcpMCSock.getPort());
/* 111 */     } catch (Exception e) {
/* 112 */       System.out.println("RCTPSenderThread.MCSendCompRtcpPkt() packet creation failed.");
/* 113 */       e.printStackTrace();
/* 114 */       return -1;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 122 */       this.rtcpSession.rtcpMCSock.send(packet);
/*     */       
/* 124 */       if (this.rtpSession.debugAppIntf != null) {
/* 125 */         this.rtpSession.debugAppIntf.packetSent(3, (InetSocketAddress)packet.getSocketAddress(), 
/* 126 */             new String("Sent multicast RTCP packet of size " + packet.getLength() + 
/* 127 */               " to " + packet.getSocketAddress().toString() + " via " + 
/* 128 */               this.rtcpSession.rtcpMCSock.getLocalSocketAddress().toString()));
/*     */       }
/* 130 */     } catch (Exception e) {
/* 131 */       System.out.println("RCTPSenderThread.MCSendCompRtcpPkt() multicast failed.");
/* 132 */       e.printStackTrace();
/* 133 */       return -1;
/*     */     } 
/* 135 */     return packet.getLength();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int sendCompRtcpPkt(CompRtcpPkt pkt, InetSocketAddress receiver) {
/*     */     DatagramPacket packet;
/* 146 */     byte[] pktBytes = pkt.encode();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 152 */       packet = new DatagramPacket(pktBytes, pktBytes.length, receiver);
/* 153 */     } catch (Exception e) {
/* 154 */       System.out.println("RCTPSenderThread.SendCompRtcpPkt() packet creation failed.");
/* 155 */       e.printStackTrace();
/* 156 */       return -1;
/*     */     } 
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
/*     */     try {
/* 170 */       this.rtcpSession.rtcpSock.send(packet);
/*     */       
/* 172 */       if (this.rtpSession.debugAppIntf != null) {
/* 173 */         this.rtpSession.debugAppIntf.packetSent(2, (InetSocketAddress)packet.getSocketAddress(), 
/* 174 */             new String("Sent unicast RTCP packet of size " + packet.getLength() + 
/* 175 */               " to " + packet.getSocketAddress().toString() + " via " + 
/* 176 */               this.rtcpSession.rtcpSock.getLocalSocketAddress().toString()));
/*     */       }
/* 178 */     } catch (Exception e) {
/* 179 */       System.out.println("RTCPSenderThread.SendCompRtcpPkt() unicast failed.");
/* 180 */       e.printStackTrace();
/* 181 */       return -1;
/*     */     } 
/* 183 */     return packet.getLength();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void reconsiderTiming(long ssrc) {
/* 191 */     Participant part = this.rtpSession.partDb.getParticipant(ssrc);
/*     */     
/* 193 */     if (part != null && this.rtcpSession.fbSendImmediately()) {
/* 194 */       int datagramLength; CompRtcpPkt compPkt = preparePacket(part, false);
/*     */ 
/*     */ 
/*     */       
/* 198 */       if (this.rtpSession.mcSession) {
/* 199 */         datagramLength = mcSendCompRtcpPkt(compPkt);
/*     */       } else {
/*     */         
/* 202 */         datagramLength = sendCompRtcpPkt(compPkt, part.rtcpAddress);
/*     */       } 
/*     */ 
/*     */       
/* 206 */       if (datagramLength > 0) {
/* 207 */         this.rtcpSession.updateAvgPacket(datagramLength);
/*     */       }
/* 209 */     } else if (part != null && 
/* 210 */       this.rtcpSession.fbAllowEarly && 
/* 211 */       this.rtcpSession.fbSendEarly()) {
/*     */       int datagramLength;
/*     */       
/* 214 */       this.rtcpSession.fbAllowEarly = false;
/*     */       
/* 216 */       CompRtcpPkt compPkt = preparePacket(part, true);
/*     */ 
/*     */ 
/*     */       
/* 220 */       if (this.rtpSession.mcSession) {
/* 221 */         datagramLength = mcSendCompRtcpPkt(compPkt);
/*     */       } else {
/*     */         
/* 224 */         datagramLength = sendCompRtcpPkt(compPkt, part.rtcpAddress);
/*     */       } 
/*     */ 
/*     */       
/* 228 */       if (datagramLength > 0) {
/* 229 */         this.rtcpSession.updateAvgPacket(datagramLength);
/*     */       }
/* 231 */       this.rtcpSession.calculateDelay();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 236 */     this.rtcpSession.nextDelay = (int)(this.rtcpSession.nextDelay - System.currentTimeMillis() - this.rtcpSession.prevTime);
/* 237 */     if (this.rtcpSession.nextDelay < 0) {
/* 238 */       this.rtcpSession.nextDelay = 0;
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
/*     */   protected CompRtcpPkt preparePacket(Participant part, boolean regular) {
/* 253 */     boolean incRR = false;
/* 254 */     if (part.secondLastRtcpRRPkt > part.lastRtcpRRPkt) {
/* 255 */       incRR = true;
/* 256 */       part.secondLastRtcpRRPkt = part.lastRtcpRRPkt;
/* 257 */       part.lastRtcpRRPkt = System.currentTimeMillis();
/*     */     } 
/*     */ 
/*     */     
/* 261 */     boolean incSR = false;
/* 262 */     if (this.rtpSession.sentPktCount > 0 && regular) {
/* 263 */       incSR = true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 269 */     CompRtcpPkt compPkt = new CompRtcpPkt();
/*     */ 
/*     */     
/* 272 */     if (incSR) {
/* 273 */       RtcpPktSR srPkt = new RtcpPktSR(this.rtpSession.ssrc, 
/* 274 */           this.rtpSession.sentPktCount, this.rtpSession.sentOctetCount, null);
/* 275 */       compPkt.addPacket(srPkt);
/*     */ 
/*     */       
/* 278 */       if (part.ssrc > 0L) {
/* 279 */         RtcpPkt[] ar = this.rtcpSession.getFromFbQueue(part.ssrc);
/* 280 */         if (ar != null) {
/* 281 */           for (int i = 0; i < ar.length; i++) {
/* 282 */             compPkt.addPacket(ar[i]);
/*     */           }
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 290 */     if (incRR || !incSR) {
/* 291 */       Participant[] partArray = { part };
/*     */       
/* 293 */       if (part.receivedPkts < 1L) {
/* 294 */         partArray = null;
/*     */       }
/* 296 */       RtcpPktRR rrPkt = new RtcpPktRR(partArray, this.rtpSession.ssrc);
/* 297 */       compPkt.addPacket(rrPkt);
/*     */       
/* 299 */       if (!incSR && part.ssrc > 0L) {
/* 300 */         RtcpPkt[] ar = this.rtcpSession.getFromFbQueue(part.ssrc);
/* 301 */         if (ar != null) {
/* 302 */           for (int i = 0; i < ar.length; i++) {
/* 303 */             compPkt.addPacket(ar[i]);
/*     */           }
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 310 */     if (regular && part.ssrc > 0L) {
/* 311 */       RtcpPktAPP[] arrayOfRtcpPktAPP = this.rtcpSession.getFromAppQueue(part.ssrc);
/* 312 */       if (arrayOfRtcpPktAPP != null) {
/* 313 */         for (int i = 0; i < arrayOfRtcpPktAPP.length; i++) {
/* 314 */           compPkt.addPacket(arrayOfRtcpPktAPP[i]);
/*     */         }
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 324 */     RtcpPktSDES sdesPkt = new RtcpPktSDES(true, this.rtpSession, null);
/* 325 */     compPkt.addPacket(sdesPkt);
/*     */ 
/*     */     
/* 328 */     return compPkt;
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
/*     */   public void run() {
/*     */     
/* 347 */     try { Thread.sleep(10L); }
/* 348 */     catch (Exception e) { System.out.println("RTCPSenderThread didn't get any initial rest."); }
/*     */ 
/*     */     
/* 351 */     Enumeration<Participant> enu = null;
/* 352 */     Iterator<Participant> iter = null;
/*     */ 
/*     */     
/* 355 */     if (this.rtpSession.mcSession) {
/* 356 */       enu = this.rtpSession.partDb.getParticipants();
/*     */     } else {
/* 358 */       iter = this.rtpSession.partDb.getUnicastReceivers();
/*     */     } 
/* 360 */     while (!this.rtpSession.endSession) {
/*     */       int datagramLength;
/*     */ 
/*     */       
/*     */       try {
/* 365 */         Thread.sleep(this.rtcpSession.nextDelay);
/* 366 */       } catch (Exception e) {
/* 367 */         System.out.println("RTCPSenderThread Exception message:" + e.getMessage());
/*     */         
/* 369 */         if (this.rtpSession.endSession) {
/*     */           continue;
/*     */         }
/*     */         
/* 373 */         if (this.rtcpSession.fbWaiting != -1L) {
/* 374 */           reconsiderTiming(this.rtcpSession.fbWaiting);
/*     */           
/*     */           continue;
/*     */         } 
/*     */       } 
/*     */       
/* 380 */       this.rtcpSession.fbAllowEarly = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 388 */       this.rtcpSession.calculateDelay();
/*     */ 
/*     */ 
/*     */       
/* 392 */       if (this.rtpSession.conflict) {
/* 393 */         if (!this.byesSent) {
/* 394 */           sendByes();
/* 395 */           this.byesSent = true;
/*     */         } 
/*     */         continue;
/*     */       } 
/* 399 */       this.byesSent = false;
/*     */ 
/*     */       
/* 402 */       Participant part = null;
/*     */ 
/*     */       
/* 405 */       if (this.rtpSession.mcSession) {
/* 406 */         if (!enu.hasMoreElements()) {
/* 407 */           enu = this.rtpSession.partDb.getParticipants();
/*     */         }
/* 409 */         if (enu.hasMoreElements()) {
/* 410 */           part = enu.nextElement();
/*     */         } else {
/*     */           
/*     */           continue;
/*     */         } 
/*     */       } else {
/*     */         
/* 417 */         if (!iter.hasNext()) {
/* 418 */           iter = this.rtpSession.partDb.getUnicastReceivers();
/*     */         }
/*     */         
/* 421 */         if (iter.hasNext()) {
/* 422 */           while (iter.hasNext() && (part == null || part.rtcpAddress == null)) {
/* 423 */             part = iter.next();
/*     */           }
/*     */         }
/*     */         
/* 427 */         if (part == null || part.rtcpAddress == null) {
/*     */           continue;
/*     */         }
/*     */       } 
/* 431 */       CompRtcpPkt compPkt = preparePacket(part, true);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 436 */       if (this.rtpSession.mcSession) {
/* 437 */         datagramLength = mcSendCompRtcpPkt(compPkt);
/*     */       } else {
/*     */         
/* 440 */         datagramLength = sendCompRtcpPkt(compPkt, part.rtcpAddress);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 445 */       if (datagramLength > 0) {
/* 446 */         this.rtcpSession.updateAvgPacket(datagramLength);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 451 */     sendByes(); 
/* 452 */     try { Thread.sleep(200L); } catch (Exception exception) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\rtplib\RTCPSenderThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */