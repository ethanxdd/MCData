/*      */ package media.rtplib;
/*      */ 
/*      */ import java.net.DatagramPacket;
/*      */ import java.net.DatagramSocket;
/*      */ import java.net.InetAddress;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.net.MulticastSocket;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Iterator;
/*      */ import java.util.Random;
/*      */ import java.util.concurrent.locks.Condition;
/*      */ import java.util.concurrent.locks.Lock;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class RTPSession
/*      */ {
/*      */   public static final int rtpDebugLevel = 0;
/*      */   public static final int rtcpDebugLevel = 0;
/*   58 */   protected DatagramSocket rtpSock = null;
/*      */   
/*   60 */   protected MulticastSocket rtpMCSock = null;
/*      */   
/*   62 */   protected InetAddress mcGroup = null;
/*      */ 
/*      */   
/*      */   protected boolean mcSession = false;
/*      */ 
/*      */   
/*   68 */   protected int payloadType = 0;
/*      */   
/*      */   protected long ssrc;
/*      */   
/*   72 */   protected long lastTimestamp = 0L;
/*      */   
/*   74 */   protected int seqNum = 0;
/*      */   
/*   76 */   protected int sentPktCount = 0;
/*      */   
/*   78 */   protected int sentOctetCount = 0;
/*      */ 
/*      */   
/*   81 */   protected Random random = null;
/*      */ 
/*      */   
/*   84 */   protected int bandwidth = 8000;
/*      */ 
/*      */   
/*      */   protected boolean naiveReception = false;
/*      */ 
/*      */   
/*      */   protected boolean frameReconstruction = true;
/*      */ 
/*      */   
/*   93 */   protected int pktBufBehavior = 3;
/*      */ 
/*      */   
/*   96 */   protected ParticipantDatabase partDb = new ParticipantDatabase(this);
/*      */   
/*   98 */   protected RTPAppIntf appIntf = null;
/*      */   
/*  100 */   protected RTCPAppIntf rtcpAppIntf = null;
/*      */   
/*  102 */   protected RTCPAVPFIntf rtcpAVPFIntf = null;
/*      */   
/*  104 */   protected DebugAppIntf debugAppIntf = null;
/*      */ 
/*      */   
/*  107 */   protected RTCPSession rtcpSession = null;
/*      */   
/*  109 */   protected RTPReceiverThread recvThrd = null;
/*      */   
/*  111 */   protected AppCallerThread appCallerThrd = null;
/*      */ 
/*      */   
/*  114 */   protected final Lock pktBufLock = new ReentrantLock();
/*      */   
/*  116 */   protected final Condition pktBufDataReady = this.pktBufLock.newCondition();
/*      */ 
/*      */   
/*      */   protected boolean endSession = false;
/*      */   
/*      */   protected boolean registered = false;
/*      */   
/*      */   protected boolean conflict = false;
/*      */   
/*  125 */   protected int conflictCount = 0;
/*      */ 
/*      */   
/*  128 */   protected String cname = null;
/*      */   
/*  130 */   public String name = null;
/*      */   
/*  132 */   public String email = null;
/*      */   
/*  134 */   public String phone = null;
/*      */   
/*  136 */   public String loc = null;
/*      */   
/*  138 */   public String tool = null;
/*      */   
/*  140 */   public String note = null;
/*      */   
/*  142 */   public String priv = null;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  147 */   protected int rtcpMode = 0;
/*  148 */   protected int fbEarlyThreshold = -1;
/*  149 */   protected int fbRegularThreshold = -1;
/*  150 */   protected int minInterval = 5000;
/*  151 */   protected int fbMaxDelay = 1000;
/*      */   
/*  153 */   protected int rtcpBandwidth = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public RTPSession(DatagramSocket rtpSocket, DatagramSocket rtcpSocket) {
/*  167 */     this.mcSession = false;
/*  168 */     this.rtpSock = rtpSocket;
/*  169 */     generateCNAME();
/*  170 */     generateSsrc();
/*  171 */     this.rtcpSession = new RTCPSession(this, rtcpSocket);
/*      */ 
/*      */     
/*  174 */     try { Thread.sleep(1L); } catch (InterruptedException e) { System.out.println("RTPSession sleep failed"); }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public RTPSession(MulticastSocket rtpSock, MulticastSocket rtcpSock, InetAddress multicastGroup) throws Exception {
/*  189 */     this.mcSession = true;
/*  190 */     this.rtpMCSock = rtpSock;
/*  191 */     this.mcGroup = multicastGroup;
/*  192 */     this.rtpMCSock.joinGroup(this.mcGroup);
/*  193 */     rtcpSock.joinGroup(this.mcGroup);
/*  194 */     generateCNAME();
/*  195 */     generateSsrc();
/*  196 */     this.rtcpSession = new RTCPSession(this, rtcpSock, this.mcGroup);
/*      */ 
/*      */     
/*  199 */     try { Thread.sleep(1L); } catch (InterruptedException e) { System.out.println("RTPSession sleep failed"); }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int RTPSessionRegister(RTPAppIntf rtpApp, RTCPAppIntf rtcpApp, DebugAppIntf debugApp) {
/*  213 */     if (this.registered) {
/*  214 */       System.out.println("RTPSessionRegister(): Can't register another application!");
/*  215 */       return -1;
/*      */     } 
/*  217 */     this.registered = true;
/*  218 */     generateSeqNum();
/*      */ 
/*      */ 
/*      */     
/*  222 */     this.appIntf = rtpApp;
/*  223 */     this.rtcpAppIntf = rtcpApp;
/*  224 */     this.debugAppIntf = debugApp;
/*      */     
/*  226 */     this.recvThrd = new RTPReceiverThread(this);
/*  227 */     this.appCallerThrd = new AppCallerThread(this, rtpApp);
/*  228 */     this.recvThrd.start();
/*  229 */     this.appCallerThrd.start();
/*  230 */     this.rtcpSession.start();
/*  231 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long[] sendData(byte[] buf) {
/*  243 */     byte[][] tmp = { buf };
/*  244 */     long[][] ret = sendData(tmp, null, null, -1L, null);
/*      */     
/*  246 */     if (ret != null) {
/*  247 */       return ret[0];
/*      */     }
/*  249 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long[] sendData(byte[] buf, long rtpTimestamp, long seqNum) {
/*  262 */     byte[][] tmp = { buf };
/*  263 */     long[][] ret = sendData(tmp, null, null, -1L, null);
/*      */     
/*  265 */     if (ret != null) {
/*  266 */       return ret[0];
/*      */     }
/*  268 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long[][] sendData(byte[][] buffers, long[] csrcArray, boolean[] markers, long rtpTimestamp, long[] seqNumbers) {
/*  288 */     if (rtpTimestamp < 0L) {
/*  289 */       rtpTimestamp = System.currentTimeMillis();
/*      */     }
/*      */     
/*  292 */     long[][] ret = new long[buffers.length][2];
/*      */     
/*  294 */     for (int i = 0; i < buffers.length; i++) {
/*  295 */       byte[] buf = buffers[i];
/*      */       
/*  297 */       boolean marker = false;
/*  298 */       if (markers != null) {
/*  299 */         marker = markers[i];
/*      */       }
/*  301 */       if (buf.length > 1500) {
/*  302 */         System.out.println("RTPSession.sendData() called with buffer exceeding 1500 bytes (" + buf.length + ")");
/*      */       }
/*      */ 
/*      */       
/*  306 */       ret[i][0] = rtpTimestamp;
/*  307 */       if (seqNumbers == null) {
/*  308 */         ret[i][1] = getNextSeqNum();
/*      */       } else {
/*  310 */         ret[i][1] = seqNumbers[i];
/*      */       } 
/*      */       
/*  313 */       RtpPkt pkt = new RtpPkt(rtpTimestamp, this.ssrc, (int)ret[i][1], this.payloadType, buf);
/*      */       
/*  315 */       if (csrcArray != null) {
/*  316 */         pkt.setCsrcs(csrcArray);
/*      */       }
/*  318 */       pkt.setMarked(marker);
/*      */ 
/*      */       
/*  321 */       byte[] pktBytes = pkt.encode();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  326 */       if (this.conflict) {
/*  327 */         System.out.println("RTPSession.sendData() called while trying to resolve conflict.");
/*  328 */         return null;
/*      */       } 
/*      */ 
/*      */       
/*  332 */       if (this.mcSession) {
/*  333 */         DatagramPacket packet = null;
/*      */ 
/*      */         
/*      */         try {
/*  337 */           packet = new DatagramPacket(pktBytes, pktBytes.length, this.mcGroup, this.rtpMCSock.getPort());
/*  338 */         } catch (Exception e) {
/*  339 */           System.out.println("RTPSession.sendData() packet creation failed.");
/*  340 */           e.printStackTrace();
/*  341 */           return null;
/*      */         } 
/*      */         
/*      */         try {
/*  345 */           this.rtpMCSock.send(packet);
/*      */           
/*  347 */           if (this.debugAppIntf != null) {
/*  348 */             this.debugAppIntf.packetSent(1, (InetSocketAddress)packet.getSocketAddress(), 
/*  349 */                 new String("Sent multicast RTP packet of size " + packet.getLength() + 
/*  350 */                   " to " + packet.getSocketAddress().toString() + " via " + 
/*  351 */                   this.rtpMCSock.getLocalSocketAddress().toString()));
/*      */           }
/*  353 */         } catch (Exception e) {
/*  354 */           System.out.println("RTPSession.sendData() multicast failed.");
/*  355 */           e.printStackTrace();
/*  356 */           return null;
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  361 */         Iterator<Participant> iter = this.partDb.getUnicastReceivers();
/*  362 */         while (iter.hasNext()) {
/*  363 */           InetSocketAddress receiver = ((Participant)iter.next()).rtpAddress;
/*  364 */           DatagramPacket packet = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           try {
/*  371 */             packet = new DatagramPacket(pktBytes, pktBytes.length, receiver);
/*  372 */           } catch (Exception e) {
/*  373 */             System.out.println("RTPSession.sendData() packet creation failed.");
/*  374 */             e.printStackTrace();
/*  375 */             return null;
/*      */           } 
/*      */ 
/*      */           
/*      */           try {
/*  380 */             this.rtpSock.send(packet);
/*      */             
/*  382 */             if (this.debugAppIntf != null) {
/*  383 */               this.debugAppIntf.packetSent(0, (InetSocketAddress)packet.getSocketAddress(), 
/*  384 */                   new String("Sent unicast RTP packet of size " + packet.getLength() + 
/*  385 */                     " to " + packet.getSocketAddress().toString() + " via " + 
/*  386 */                     this.rtpSock.getLocalSocketAddress().toString()));
/*      */             }
/*  388 */           } catch (Exception e) {
/*  389 */             System.out.println("RTPSession.sendData() unicast failed.");
/*  390 */             e.printStackTrace();
/*  391 */             return null;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  397 */       this.sentPktCount++;
/*  398 */       this.sentOctetCount++;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  405 */     return ret;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int sendRTCPAppPacket(long ssrc, int type, byte[] name, byte[] data) {
/*  432 */     if (this.rtcpSession == null) {
/*  433 */       return -1;
/*      */     }
/*  435 */     if (name.length != 4) {
/*  436 */       return -2;
/*      */     }
/*  438 */     if (data.length % 4 != 0) {
/*  439 */       return -3;
/*      */     }
/*  441 */     if (type > 63 || type < 0) {
/*  442 */       return -4;
/*      */     }
/*  444 */     RtcpPktAPP pkt = new RtcpPktAPP(ssrc, type, name, data);
/*  445 */     this.rtcpSession.addToAppQueue(ssrc, pkt);
/*      */     
/*  447 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int addParticipant(Participant p) {
/*  458 */     p.unexpected = false;
/*  459 */     return this.partDb.addParticipant(0, p);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeParticipant(Participant p) {
/*  468 */     this.partDb.removeParticipant(p);
/*      */   }
/*      */   
/*      */   public Iterator<Participant> getUnicastReceivers() {
/*  472 */     return this.partDb.getUnicastReceivers();
/*      */   }
/*      */   
/*      */   public Enumeration<Participant> getParticipants() {
/*  476 */     return this.partDb.getParticipants();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endSession() {
/*  485 */     this.endSession = true;
/*      */ 
/*      */     
/*  488 */     if (this.mcSession) {
/*  489 */       this.rtpMCSock.close();
/*      */     } else {
/*  491 */       this.rtpSock.close();
/*      */     } 
/*      */ 
/*      */     
/*  495 */     this.pktBufLock.lock(); 
/*  496 */     try { this.pktBufDataReady.signalAll(); }
/*  497 */     finally { this.pktBufLock.unlock(); }
/*      */ 
/*      */     
/*  500 */     this.rtcpSession.senderThrd.interrupt();
/*      */ 
/*      */     
/*  503 */     try { Thread.sleep(50L); } catch (Exception exception) {}
/*      */     
/*  505 */     this.appCallerThrd.interrupt();
/*      */ 
/*      */     
/*  508 */     try { Thread.sleep(50L); } catch (Exception exception) {}
/*      */     
/*  510 */     if (this.rtcpSession != null)
/*      */     {
/*  512 */       if (this.mcSession) {
/*  513 */         this.rtcpSession.rtcpMCSock.close();
/*      */       } else {
/*  515 */         this.rtcpSession.rtcpSock.close();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isEnding() {
/*  527 */     return this.endSession;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void CNAME(String cname) {
/*  536 */     this.cname = cname;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String CNAME() {
/*  543 */     return this.cname;
/*      */   }
/*      */   
/*      */   public long getSsrc() {
/*  547 */     return this.ssrc;
/*      */   }
/*      */ 
/*      */   
/*      */   private void generateCNAME() {
/*      */     String hostname;
/*  553 */     if (this.mcSession) {
/*  554 */       hostname = this.rtpMCSock.getLocalAddress().getCanonicalHostName();
/*      */     } else {
/*  556 */       hostname = this.rtpSock.getLocalAddress().getCanonicalHostName();
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  563 */     this.cname = String.valueOf(System.getProperty("user.name")) + "@" + hostname;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int updateRTPSock(DatagramSocket newSock) {
/*  574 */     if (!this.mcSession) {
/*  575 */       this.rtpSock = newSock;
/*  576 */       return 0;
/*      */     } 
/*  578 */     System.out.println("Can't switch from multicast to unicast.");
/*  579 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int updateRTCPSock(DatagramSocket newSock) {
/*  591 */     if (!this.mcSession) {
/*  592 */       this.rtcpSession.rtcpSock = newSock;
/*  593 */       return 0;
/*      */     } 
/*  595 */     System.out.println("Can't switch from multicast to unicast.");
/*  596 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int updateRTPSock(MulticastSocket newSock) {
/*  608 */     if (this.mcSession) {
/*  609 */       this.rtpMCSock = newSock;
/*  610 */       return 0;
/*      */     } 
/*  612 */     System.out.println("Can't switch from unicast to multicast.");
/*  613 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int updateRTCPSock(MulticastSocket newSock) {
/*  625 */     if (this.mcSession) {
/*  626 */       this.rtcpSession.rtcpMCSock = newSock;
/*  627 */       return 0;
/*      */     } 
/*  629 */     System.out.println("Can't switch from unicast to multicast.");
/*  630 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int payloadType(int payloadT) {
/*  640 */     if (payloadT > 128 || payloadT < 0) {
/*  641 */       return -1;
/*      */     }
/*  643 */     this.payloadType = payloadT;
/*  644 */     return this.payloadType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int payloadType() {
/*  654 */     return this.payloadType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void naivePktReception(boolean doAccept) {
/*  663 */     this.naiveReception = doAccept;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean naivePktReception() {
/*  672 */     return this.naiveReception;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int packetBufferBehavior(int behavior) {
/*  696 */     if (behavior > -2) {
/*  697 */       this.pktBufBehavior = behavior;
/*      */       
/*  699 */       this.pktBufLock.lock(); 
/*  700 */       try { this.pktBufDataReady.signalAll(); }
/*  701 */       finally { this.pktBufLock.unlock(); }
/*      */       
/*  703 */       return this.pktBufBehavior;
/*      */     } 
/*  705 */     return this.pktBufBehavior;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int packetBufferBehavior() {
/*  720 */     return this.pktBufBehavior;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int registerAVPFIntf(RTCPAVPFIntf rtcpAVPFIntf, int maxDelay, int earlyThreshold, int regularThreshold) {
/*  733 */     if (this.rtcpSession != null) {
/*  734 */       packetBufferBehavior(-1);
/*  735 */       this.frameReconstruction = false;
/*  736 */       this.rtcpAVPFIntf = rtcpAVPFIntf;
/*  737 */       this.fbEarlyThreshold = earlyThreshold;
/*  738 */       this.fbRegularThreshold = regularThreshold;
/*  739 */       return 0;
/*      */     } 
/*  741 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void unregisterAVPFIntf() {
/*  754 */     this.fbEarlyThreshold = -1;
/*  755 */     this.fbRegularThreshold = -1;
/*  756 */     this.rtcpAVPFIntf = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void frameReconstruction(boolean toggle) {
/*  766 */     this.frameReconstruction = toggle;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean frameReconstruction() {
/*  776 */     return this.frameReconstruction;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int sessionBandwidth() {
/*  796 */     return this.bandwidth;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int sessionBandwidth(int bandwidth) {
/*  808 */     if (bandwidth < 1) {
/*  809 */       this.bandwidth = 8000;
/*      */     } else {
/*  811 */       this.bandwidth = bandwidth;
/*      */     } 
/*  813 */     return this.bandwidth;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int rtcpBandwidth() {
/*  831 */     return this.rtcpBandwidth;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int rtcpBandwidth(int bandwidth) {
/*  843 */     if (bandwidth < -1) {
/*  844 */       this.rtcpBandwidth = -1;
/*      */     } else {
/*  846 */       this.rtcpBandwidth = bandwidth;
/*      */     } 
/*  848 */     return this.rtcpBandwidth;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int fbPictureLossIndication(long ssrcMediaSource) {
/*  860 */     int ret = 0;
/*      */     
/*  862 */     if (this.rtcpAVPFIntf == null) {
/*  863 */       return -1;
/*      */     }
/*  865 */     RtcpPktPSFB pkt = new RtcpPktPSFB(this.ssrc, ssrcMediaSource);
/*  866 */     pkt.makePictureLossIndication();
/*  867 */     ret = this.rtcpSession.addToFbQueue(ssrcMediaSource, pkt);
/*  868 */     if (ret == 0)
/*  869 */       this.rtcpSession.wakeSenderThread(ssrcMediaSource); 
/*  870 */     return ret;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int fbSlicLossIndication(long ssrcMediaSource, int[] sliFirst, int[] sliNumber, int[] sliPictureId) {
/*  883 */     int ret = 0;
/*  884 */     if (this.rtcpAVPFIntf == null) {
/*  885 */       return -1;
/*      */     }
/*  887 */     RtcpPktPSFB pkt = new RtcpPktPSFB(this.ssrc, ssrcMediaSource);
/*  888 */     pkt.makeSliceLossIndication(sliFirst, sliNumber, sliPictureId);
/*      */     
/*  890 */     ret = this.rtcpSession.addToFbQueue(ssrcMediaSource, pkt);
/*  891 */     if (ret == 0)
/*  892 */       this.rtcpSession.wakeSenderThread(ssrcMediaSource); 
/*  893 */     return ret;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int fbRefPictureSelIndic(long ssrcMediaSource, int bitPadding, int payloadType, byte[] bitString) {
/*  906 */     int ret = 0;
/*      */     
/*  908 */     if (this.rtcpAVPFIntf == null) {
/*  909 */       return -1;
/*      */     }
/*  911 */     RtcpPktPSFB pkt = new RtcpPktPSFB(this.ssrc, ssrcMediaSource);
/*  912 */     pkt.makeRefPictureSelIndic(bitPadding, payloadType, bitString);
/*  913 */     ret = this.rtcpSession.addToFbQueue(ssrcMediaSource, pkt);
/*  914 */     if (ret == 0)
/*  915 */       this.rtcpSession.wakeSenderThread(ssrcMediaSource); 
/*  916 */     return ret;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int fbAppLayerFeedback(long ssrcMediaSource, byte[] bitString) {
/*  927 */     int ret = 0;
/*      */     
/*  929 */     if (this.rtcpAVPFIntf == null) {
/*  930 */       return -1;
/*      */     }
/*  932 */     RtcpPktPSFB pkt = new RtcpPktPSFB(this.ssrc, ssrcMediaSource);
/*  933 */     pkt.makeAppLayerFeedback(bitString);
/*  934 */     ret = this.rtcpSession.addToFbQueue(ssrcMediaSource, pkt);
/*  935 */     if (ret == 0)
/*  936 */       this.rtcpSession.wakeSenderThread(ssrcMediaSource); 
/*  937 */     return ret;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int fbPictureLossIndication(long ssrcMediaSource, int FMT, int[] PID, int[] BLP) {
/*  953 */     int ret = 0;
/*      */     
/*  955 */     if (this.rtcpAVPFIntf == null) {
/*  956 */       return -1;
/*      */     }
/*  958 */     RtcpPktRTPFB pkt = new RtcpPktRTPFB(this.ssrc, ssrcMediaSource, FMT, PID, BLP);
/*  959 */     ret = this.rtcpSession.addToFbQueue(ssrcMediaSource, pkt);
/*  960 */     if (ret == 0)
/*  961 */       this.rtcpSession.wakeSenderThread(ssrcMediaSource); 
/*  962 */     return ret;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int getNextSeqNum() {
/*  970 */     this.seqNum++;
/*      */     
/*  972 */     if (this.seqNum > 65536) {
/*  973 */       this.seqNum = 0;
/*      */     }
/*  975 */     return this.seqNum;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createRandom() {
/*  983 */     this.random = new Random(System.currentTimeMillis() + Thread.currentThread().getId() - 
/*  984 */         Thread.currentThread().hashCode() + this.cname.hashCode());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void generateSeqNum() {
/*  992 */     if (this.random == null) {
/*  993 */       createRandom();
/*      */     }
/*  995 */     this.seqNum = this.random.nextInt();
/*  996 */     if (this.seqNum < 0)
/*  997 */       this.seqNum = -this.seqNum; 
/*  998 */     while (this.seqNum > 65535) {
/*  999 */       this.seqNum /= 10;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void generateSsrc() {
/* 1007 */     if (this.random == null) {
/* 1008 */       createRandom();
/*      */     }
/*      */     
/* 1011 */     this.ssrc = this.random.nextInt();
/* 1012 */     if (this.ssrc < 0L) {
/* 1013 */       this.ssrc *= -1L;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void resolveSsrcConflict() {
/* 1026 */     System.out.println("!!!!!!! Beginning SSRC conflict resolution !!!!!!!!!");
/* 1027 */     this.conflictCount++;
/*      */     
/* 1029 */     if (this.conflictCount < 5) {
/*      */       
/* 1031 */       this.conflict = true;
/*      */ 
/*      */       
/* 1034 */       this.rtcpSession.sendByes();
/*      */ 
/*      */       
/* 1037 */       this.rtcpSession.calculateDelay();
/*      */ 
/*      */       
/* 1040 */       generateSsrc();
/*      */ 
/*      */       
/* 1043 */       this.rtcpSession.initial = true;
/*      */       
/* 1045 */       this.conflict = false;
/* 1046 */       System.out.println("SSRC conflict resolution complete");
/*      */     } else {
/*      */       
/* 1049 */       System.out.println("Too many conflicts. There is probably a loop in the network.");
/* 1050 */       endSession();
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\rtplib\RTPSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */