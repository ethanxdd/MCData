/*     */ package media.rtplib;
/*     */ 
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.InetAddress;
/*     */ import java.net.MulticastSocket;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.LinkedList;
/*     */ import java.util.ListIterator;
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
/*     */ public class RTCPSession
/*     */ {
/*  41 */   protected RTPSession rtpSession = null;
/*     */ 
/*     */   
/*  44 */   protected DatagramSocket rtcpSock = null;
/*     */   
/*  46 */   protected MulticastSocket rtcpMCSock = null;
/*     */   
/*  48 */   protected InetAddress mcGroup = null;
/*     */ 
/*     */   
/*  51 */   protected RTCPReceiverThread recvThrd = null;
/*     */   
/*  53 */   protected RTCPSenderThread senderThrd = null;
/*     */ 
/*     */   
/*  56 */   protected long prevTime = System.currentTimeMillis();
/*     */   
/*  58 */   protected int nextDelay = -1;
/*     */   
/*  60 */   protected int avgPktSize = 200;
/*     */   
/*  62 */   protected int senderCount = 1;
/*     */   
/*     */   protected boolean fbAllowEarly = false;
/*     */   
/*  66 */   protected Hashtable<Long, LinkedList<RtcpPkt>> fbQueue = null;
/*     */   
/*  68 */   protected Hashtable<Long, LinkedList<RtcpPktAPP>> appQueue = null;
/*     */   
/*     */   protected boolean initial = true;
/*     */   
/*  72 */   protected long fbWaiting = -1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RTCPSession(RTPSession parent, DatagramSocket rtcpSocket) {
/*  81 */     this.rtcpSock = rtcpSocket;
/*  82 */     this.rtpSession = parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RTCPSession(RTPSession parent, MulticastSocket rtcpSocket, InetAddress multicastGroup) {
/*  93 */     this.mcGroup = multicastGroup;
/*  94 */     this.rtcpSock = rtcpSocket;
/*  95 */     this.rtpSession = parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void start() {
/* 104 */     calculateDelay();
/* 105 */     this.recvThrd = new RTCPReceiverThread(this, this.rtpSession);
/* 106 */     this.senderThrd = new RTCPSenderThread(this, this.rtpSession);
/* 107 */     this.recvThrd.start();
/* 108 */     this.senderThrd.start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sendByes() {
/* 116 */     this.senderThrd.sendByes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void calculateDelay() {
/* 124 */     switch (this.rtpSession.rtcpMode) { case 0:
/* 125 */         calculateRegularDelay(); return; }
/*     */     
/* 127 */     System.out.println("RTCPSession.calculateDelay() unknown .mode");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void calculateRegularDelay() {
/* 136 */     long curTime = System.currentTimeMillis();
/*     */     
/* 138 */     if (this.rtpSession.bandwidth != 0 && !this.initial && this.rtpSession.partDb.ssrcTable.size() > 4) {
/*     */       double bw;
/* 140 */       int rand = this.rtpSession.random.nextInt(10000) - 5000;
/* 141 */       double randDouble = (1000.0D + rand) / 1000.0D;
/*     */ 
/*     */       
/* 144 */       Enumeration<Participant> enu = this.rtpSession.partDb.getParticipants();
/* 145 */       while (enu.hasMoreElements()) {
/* 146 */         Participant part = enu.nextElement();
/* 147 */         if (part.lastRtpPkt > this.prevTime) {
/* 148 */           this.senderCount++;
/*     */         }
/*     */       } 
/*     */       
/* 152 */       if (this.rtpSession.rtcpBandwidth > -1) {
/* 153 */         bw = this.rtpSession.rtcpBandwidth;
/*     */       } else {
/* 155 */         bw = this.rtpSession.bandwidth * 0.05D;
/*     */       } 
/* 157 */       if (this.senderCount * 2 > this.rtpSession.partDb.ssrcTable.size()) {
/* 158 */         if (this.rtpSession.lastTimestamp > this.prevTime) {
/*     */           
/* 160 */           double numerator = this.avgPktSize * this.senderCount;
/* 161 */           double denominator = 0.25D * bw;
/* 162 */           this.nextDelay = (int)Math.round(numerator / denominator * randDouble);
/*     */         } else {
/*     */           
/* 165 */           double numerator = this.avgPktSize * this.rtpSession.partDb.ssrcTable.size();
/* 166 */           double denominator = 0.75D * bw;
/* 167 */           this.nextDelay = (int)Math.round(numerator / denominator * randDouble);
/*     */         } 
/*     */       } else {
/* 170 */         double numerator = this.avgPktSize * this.rtpSession.partDb.ssrcTable.size();
/* 171 */         double denominator = bw;
/* 172 */         this.nextDelay = (int)Math.round(1000.0D * numerator / denominator) * (1000 + rand);
/*     */       } 
/*     */     } else {
/*     */       
/* 176 */       int rand = this.rtpSession.random.nextInt(1000) - 500;
/* 177 */       if (this.initial) {
/*     */         
/* 179 */         this.nextDelay = 3000 + rand;
/* 180 */         this.initial = false;
/*     */       } else {
/*     */         
/* 183 */         this.nextDelay = 5500 + rand;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 189 */     if (this.nextDelay < 1000) {
/* 190 */       int rand = this.rtpSession.random.nextInt(1000) - 500;
/* 191 */       System.out.println("RTCPSession.calculateDelay() nextDelay was too short (" + 
/* 192 */           this.nextDelay + "ms), setting to " + (this.nextDelay = 2000 + rand));
/*     */     } 
/* 194 */     this.prevTime = curTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void updateAvgPacket(int length) {
/* 202 */     double tempAvg = this.avgPktSize;
/* 203 */     tempAvg = (15.0D * tempAvg + length) / 16.0D;
/* 204 */     this.avgPktSize = (int)tempAvg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void addToAppQueue(long targetSsrc, RtcpPktAPP aPkt) {
/* 215 */     aPkt.time = System.currentTimeMillis();
/*     */     
/* 217 */     if (this.appQueue == null) {
/* 218 */       this.appQueue = new Hashtable<>();
/*     */     }
/* 220 */     LinkedList<RtcpPktAPP> ll = this.appQueue.get(Long.valueOf(targetSsrc));
/* 221 */     if (ll == null) {
/*     */       
/* 223 */       ll = new LinkedList<>();
/* 224 */       this.appQueue.put(Long.valueOf(targetSsrc), ll);
/*     */     } 
/*     */     
/* 227 */     ll.add(aPkt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized RtcpPktAPP[] getFromAppQueue(long targetSsrc) {
/* 238 */     if (this.appQueue == null) {
/* 239 */       return null;
/*     */     }
/* 241 */     LinkedList<RtcpPktAPP> ll = this.appQueue.get(Long.valueOf(targetSsrc));
/* 242 */     if (ll == null || ll.isEmpty()) {
/* 243 */       return null;
/*     */     }
/* 245 */     RtcpPktAPP[] ret = new RtcpPktAPP[ll.size()];
/* 246 */     ListIterator<RtcpPktAPP> li = ll.listIterator();
/* 247 */     int i = 0;
/* 248 */     while (li.hasNext()) {
/* 249 */       ret[i] = li.next();
/* 250 */       i++;
/*     */     } 
/* 252 */     return ret;
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
/*     */   protected synchronized void cleanAppQueue(long ssrc) {
/* 264 */     if (this.appQueue == null) {
/*     */       return;
/*     */     }
/* 267 */     if (ssrc > 0L) {
/* 268 */       this.appQueue.remove(Long.valueOf(ssrc));
/*     */     } else {
/* 270 */       Enumeration<LinkedList<RtcpPktAPP>> enu = this.appQueue.elements();
/* 271 */       long curTime = System.currentTimeMillis();
/*     */ 
/*     */       
/* 274 */       while (enu.hasMoreElements()) {
/* 275 */         ListIterator<RtcpPktAPP> li = ((LinkedList<RtcpPktAPP>)enu.nextElement()).listIterator();
/* 276 */         while (li.hasNext()) {
/* 277 */           RtcpPkt aPkt = li.next();
/*     */           
/* 279 */           if (curTime - aPkt.time > 60000L) {
/* 280 */             li.remove();
/*     */           }
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized int addToFbQueue(long targetSsrc, RtcpPkt aPkt) {
/* 297 */     if (this.fbQueue == null) {
/* 298 */       this.fbQueue = new Hashtable<>();
/*     */     }
/* 300 */     LinkedList<RtcpPkt> ll = this.fbQueue.get(Long.valueOf(targetSsrc));
/* 301 */     if (ll == null) {
/*     */       
/* 303 */       ll = new LinkedList<>();
/* 304 */       ll.add(aPkt);
/* 305 */       this.fbQueue.put(Long.valueOf(targetSsrc), ll);
/*     */     } else {
/*     */       
/* 308 */       ListIterator<RtcpPkt> li = ll.listIterator();
/* 309 */       while (li.hasNext()) {
/* 310 */         RtcpPkt tmp = li.next();
/* 311 */         if (equivalent(tmp, aPkt))
/* 312 */           return -1; 
/*     */       } 
/* 314 */       ll.addLast(aPkt);
/*     */     } 
/* 316 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized RtcpPkt[] getFromFbQueue(long ssrc) {
/* 327 */     if (this.fbQueue == null) {
/* 328 */       return null;
/*     */     }
/* 330 */     LinkedList<RtcpPkt> ll = this.fbQueue.get(Long.valueOf(ssrc));
/*     */     
/* 332 */     if (ll == null) {
/* 333 */       return null;
/*     */     }
/* 335 */     ListIterator<RtcpPkt> li = ll.listIterator();
/* 336 */     if (li.hasNext()) {
/* 337 */       long curTime = System.currentTimeMillis();
/* 338 */       long maxDelay = curTime - this.rtpSession.fbMaxDelay;
/* 339 */       long keepDelay = curTime - 2000L;
/* 340 */       int count = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 345 */       while (li.hasNext()) {
/* 346 */         RtcpPkt aPkt = li.next();
/* 347 */         if (aPkt.received) {
/*     */ 
/*     */           
/* 350 */           if (aPkt.time < keepDelay)
/* 351 */             li.remove(); 
/*     */           continue;
/*     */         } 
/* 354 */         if (aPkt.time < maxDelay) {
/* 355 */           li.remove(); continue;
/*     */         } 
/* 357 */         count++;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 363 */       if (count != 0) {
/* 364 */         li = ll.listIterator();
/* 365 */         RtcpPkt[] ret = new RtcpPkt[count];
/*     */         
/* 367 */         while (count > 0) {
/* 368 */           RtcpPkt aPkt = li.next();
/* 369 */           if (!aPkt.received) {
/* 370 */             ret[ret.length - count] = aPkt;
/* 371 */             count--;
/*     */           } 
/*     */         } 
/* 374 */         return ret;
/*     */       } 
/*     */     } 
/*     */     
/* 378 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void cleanFbQueue(long ssrc) {
/* 388 */     if (this.fbQueue == null) {
/*     */       return;
/*     */     }
/* 391 */     if (ssrc > 0L) {
/* 392 */       this.fbQueue.remove(Long.valueOf(ssrc));
/*     */     } else {
/* 394 */       Enumeration<LinkedList<RtcpPkt>> enu = this.fbQueue.elements();
/* 395 */       long curTime = System.currentTimeMillis();
/* 396 */       long maxDelay = curTime - this.rtpSession.fbMaxDelay;
/* 397 */       long keepDelay = curTime - 2000L;
/*     */       
/* 399 */       while (enu.hasMoreElements()) {
/* 400 */         ListIterator<RtcpPkt> li = ((LinkedList<RtcpPkt>)enu.nextElement()).listIterator();
/* 401 */         while (li.hasNext()) {
/* 402 */           RtcpPkt aPkt = li.next();
/* 403 */           if (aPkt.received) {
/*     */ 
/*     */             
/* 406 */             if (aPkt.time < keepDelay)
/* 407 */               li.remove(); 
/*     */             continue;
/*     */           } 
/* 410 */           if (aPkt.time < maxDelay) {
/* 411 */             li.remove();
/*     */           }
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
/*     */   protected boolean fbSendImmediately() {
/* 424 */     if (this.rtpSession.partDb.ssrcTable.size() > this.rtpSession.fbEarlyThreshold && 
/* 425 */       this.rtpSession.partDb.receivers.size() > this.rtpSession.fbEarlyThreshold) {
/* 426 */       return false;
/*     */     }
/* 428 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean fbSendEarly() {
/* 438 */     if (this.rtpSession.partDb.ssrcTable.size() > this.rtpSession.fbRegularThreshold && 
/* 439 */       this.rtpSession.partDb.receivers.size() > this.rtpSession.fbRegularThreshold) {
/* 440 */       return false;
/*     */     }
/* 442 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void wakeSenderThread(long ssrc) {
/* 451 */     this.fbWaiting = ssrc;
/* 452 */     this.senderThrd.interrupt();
/*     */ 
/*     */     
/* 455 */     try { Thread.sleep(0L, 1); } catch (Exception exception) {}
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
/*     */   private boolean equivalent(RtcpPkt one, RtcpPkt two) {
/* 470 */     if (one.packetType != two.packetType) {
/* 471 */       return false;
/*     */     }
/* 473 */     if (one.itemCount != two.itemCount) {
/* 474 */       return false;
/*     */     }
/* 476 */     if (one.packetType == 205) {
/*     */       
/* 478 */       RtcpPktRTPFB pktone = (RtcpPktRTPFB)one;
/* 479 */       RtcpPktRTPFB pkttwo = (RtcpPktRTPFB)two;
/*     */       
/* 481 */       if (pktone.ssrcMediaSource != pkttwo.ssrcMediaSource) {
/* 482 */         return false;
/*     */       }
/* 484 */       if (Arrays.equals(pktone.BLP, pkttwo.BLP) && 
/* 485 */         Arrays.equals(pktone.BLP, pkttwo.BLP)) {
/* 486 */         return true;
/*     */       }
/* 488 */       return true;
/* 489 */     }  if (one.packetType == 206) {
/* 490 */       RtcpPktPSFB pktone = (RtcpPktPSFB)one;
/* 491 */       RtcpPktPSFB pkttwo = (RtcpPktPSFB)two;
/*     */       
/* 493 */       if (pktone.ssrcMediaSource != pkttwo.ssrcMediaSource) {
/* 494 */         return false;
/*     */       }
/* 496 */       switch (one.itemCount) {
/*     */         case 1:
/* 498 */           return true;
/*     */ 
/*     */         
/*     */         case 2:
/* 502 */           if (pktone.sliFirst.length == pkttwo.sliFirst.length && 
/* 503 */             Arrays.equals(pktone.sliFirst, pkttwo.sliFirst) && 
/* 504 */             Arrays.equals(pktone.sliNumber, pkttwo.sliNumber) && 
/* 505 */             Arrays.equals(pktone.sliPictureId, pkttwo.sliPictureId))
/* 506 */             return true; 
/*     */           break;
/*     */         case 3:
/* 509 */           if (Arrays.equals(pktone.rpsiBitString, pkttwo.rpsiBitString)) {
/* 510 */             return true;
/*     */           }
/*     */           break;
/*     */         case 15:
/* 514 */           if (pktone.sliFirst.length == pkttwo.sliFirst.length && 
/* 515 */             Arrays.equals(pktone.alfBitString, pkttwo.alfBitString)) {
/* 516 */             return true;
/*     */           }
/*     */           break;
/*     */       } 
/*     */       
/* 521 */       return true;
/*     */     } 
/* 523 */     System.out.println("!!!! RTCPSession.equivalentPackets() encountered unexpected packet type!");
/*     */     
/* 525 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\rtplib\RTCPSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */