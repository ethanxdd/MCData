/*     */ package media.rtplib;
/*     */ 
/*     */ import java.io.IOException;
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
/*     */ public class RTCPReceiverThread
/*     */   extends Thread
/*     */ {
/*  35 */   private RTPSession rtpSession = null;
/*     */   
/*  37 */   private RTCPSession rtcpSession = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   RTCPReceiverThread(RTCPSession rtcpSession, RTPSession rtpSession) {
/*  45 */     this.rtpSession = rtpSession;
/*  46 */     this.rtcpSession = rtcpSession;
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
/*     */ 
/*     */   
/*     */   private Participant findParticipant(long ssrc, DatagramPacket packet) {
/*  66 */     Participant p = this.rtpSession.partDb.getParticipant(ssrc);
/*  67 */     if (p == null) {
/*  68 */       Enumeration<Participant> enu = this.rtpSession.partDb.getParticipants();
/*  69 */       while (enu.hasMoreElements()) {
/*  70 */         Participant tmp = enu.nextElement();
/*  71 */         if (tmp.ssrc < 0L && (
/*  72 */           tmp.rtcpAddress.getAddress().equals(packet.getAddress()) || 
/*  73 */           tmp.rtpAddress.getAddress().equals(packet.getAddress()))) {
/*     */ 
/*     */           
/*  76 */           System.out.println("RTCPReceiverThread: Got an unexpected packet from SSRC:" + 
/*  77 */               ssrc + " @" + packet.getAddress().toString() + ", WAS able to match it.");
/*     */           
/*  79 */           tmp.ssrc = ssrc;
/*  80 */           return tmp;
/*     */         } 
/*     */       } 
/*     */       
/*  84 */       System.out.println("RTCPReceiverThread: Got an unexpected packet from SSRC:" + 
/*  85 */           ssrc + " @" + packet.getAddress().toString() + ", was NOT able to match it.");
/*  86 */       p = new Participant(null, (InetSocketAddress)packet.getSocketAddress(), ssrc);
/*  87 */       this.rtpSession.partDb.addParticipant(2, p);
/*     */     } 
/*  89 */     return p;
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
/*     */   private int parsePacket(DatagramPacket packet) {
/* 103 */     if (packet.getLength() % 4 != 0)
/*     */     {
/*     */ 
/*     */       
/* 107 */       return -1;
/*     */     }
/* 109 */     byte[] rawPkt = packet.getData();
/*     */ 
/*     */     
/* 112 */     CompRtcpPkt compPkt = new CompRtcpPkt(rawPkt, packet.getLength(), 
/* 113 */         (InetSocketAddress)packet.getSocketAddress(), this.rtpSession);
/*     */     
/* 115 */     if (this.rtpSession.debugAppIntf != null) {
/*     */       String intfStr;
/*     */       
/* 118 */       if (this.rtpSession.mcSession) {
/* 119 */         intfStr = this.rtcpSession.rtcpMCSock.getLocalSocketAddress().toString();
/*     */       } else {
/* 121 */         intfStr = this.rtpSession.rtpSock.getLocalSocketAddress().toString();
/*     */       } 
/*     */       
/* 124 */       if (compPkt.problem == 0) {
/* 125 */         String str = new String("Received compound RTCP packet of size " + packet.getLength() + 
/* 126 */             " from " + packet.getSocketAddress().toString() + " via " + intfStr + 
/* 127 */             " containing " + compPkt.rtcpPkts.size() + " packets");
/*     */         
/* 129 */         this.rtpSession.debugAppIntf.packetReceived(1, 
/* 130 */             (InetSocketAddress)packet.getSocketAddress(), str);
/*     */       } else {
/* 132 */         String str = new String("Received invalid RTCP packet of size " + packet.getLength() + 
/* 133 */             " from " + packet.getSocketAddress().toString() + " via " + intfStr + 
/* 134 */             ": " + debugErrorString(compPkt.problem));
/*     */         
/* 136 */         this.rtpSession.debugAppIntf.packetReceived(-2, 
/* 137 */             (InetSocketAddress)packet.getSocketAddress(), str);
/*     */       } 
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
/*     */ 
/*     */     
/* 153 */     Iterator<RtcpPkt> iter = compPkt.rtcpPkts.iterator();
/*     */     
/* 155 */     long curTime = System.currentTimeMillis();
/*     */     
/* 157 */     while (iter.hasNext()) {
/* 158 */       RtcpPkt aPkt = iter.next();
/*     */ 
/*     */       
/* 161 */       if (aPkt.ssrc == this.rtpSession.ssrc) {
/* 162 */         System.out.println("RTCPReceiverThread() received RTCP packet with conflicting SSRC from " + 
/* 163 */             packet.getSocketAddress().toString());
/* 164 */         this.rtpSession.resolveSsrcConflict();
/* 165 */         return -1;
/*     */       } 
/*     */ 
/*     */       
/* 169 */       if (aPkt.getClass() == RtcpPktRR.class) {
/* 170 */         RtcpPktRR rrPkt = (RtcpPktRR)aPkt;
/*     */         
/* 172 */         Participant p = findParticipant(rrPkt.ssrc, packet);
/* 173 */         p.lastRtcpPkt = curTime;
/*     */         
/* 175 */         if (this.rtpSession.rtcpAppIntf != null) {
/* 176 */           this.rtpSession.rtcpAppIntf.RRPktReceived(rrPkt.ssrc, rrPkt.reporteeSsrc, 
/* 177 */               rrPkt.lossFraction, rrPkt.lostPktCount, rrPkt.extHighSeqRecv, 
/* 178 */               rrPkt.interArvJitter, rrPkt.timeStampLSR, rrPkt.delaySR);
/*     */         }
/*     */         continue;
/*     */       } 
/* 182 */       if (aPkt.getClass() == RtcpPktSR.class) {
/* 183 */         RtcpPktSR srPkt = (RtcpPktSR)aPkt;
/*     */         
/* 185 */         Participant p = findParticipant(srPkt.ssrc, packet);
/* 186 */         p.lastRtcpPkt = curTime;
/*     */         
/* 188 */         if (p != null) {
/*     */           
/* 190 */           if (p.ntpGradient < 0.0D && p.lastNtpTs1 > -1L) {
/*     */             
/* 192 */             long newTime = StaticProcs.undoNtpMess(srPkt.ntpTs1, srPkt.ntpTs2);
/* 193 */             p.ntpGradient = (newTime - p.ntpOffset) / (srPkt.rtpTs - p.lastSRRtpTs);
/*     */           
/*     */           }
/*     */           else {
/*     */ 
/*     */             
/* 199 */             p.ntpOffset = StaticProcs.undoNtpMess(srPkt.ntpTs1, srPkt.ntpTs2);
/* 200 */             p.lastNtpTs1 = srPkt.ntpTs1;
/* 201 */             p.lastNtpTs2 = srPkt.ntpTs2;
/* 202 */             p.lastSRRtpTs = srPkt.rtpTs;
/*     */           } 
/*     */ 
/*     */           
/* 206 */           p.timeReceivedLSR = curTime;
/* 207 */           p.setTimeStampLSR(srPkt.ntpTs1, srPkt.ntpTs2);
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 212 */         if (this.rtpSession.rtcpAppIntf != null) {
/* 213 */           if (srPkt.rReports != null) {
/* 214 */             this.rtpSession.rtcpAppIntf.SRPktReceived(srPkt.ssrc, srPkt.ntpTs1, srPkt.ntpTs2, 
/* 215 */                 srPkt.rtpTs, srPkt.sendersPktCount, srPkt.sendersPktCount, 
/* 216 */                 srPkt.rReports.reporteeSsrc, srPkt.rReports.lossFraction, srPkt.rReports.lostPktCount, 
/* 217 */                 srPkt.rReports.extHighSeqRecv, srPkt.rReports.interArvJitter, srPkt.rReports.timeStampLSR, 
/* 218 */                 srPkt.rReports.delaySR); continue;
/*     */           } 
/* 220 */           this.rtpSession.rtcpAppIntf.SRPktReceived(srPkt.ssrc, srPkt.ntpTs1, srPkt.ntpTs2, 
/* 221 */               srPkt.rtpTs, srPkt.sendersPktCount, srPkt.sendersPktCount, 
/* 222 */               null, null, null, 
/* 223 */               null, null, null, 
/* 224 */               null);
/*     */         } 
/*     */         
/*     */         continue;
/*     */       } 
/* 229 */       if (aPkt.getClass() == RtcpPktSDES.class) {
/* 230 */         RtcpPktSDES sdesPkt = (RtcpPktSDES)aPkt;
/*     */ 
/*     */ 
/*     */         
/* 234 */         if (this.rtpSession.rtcpAppIntf != null) {
/* 235 */           this.rtpSession.rtcpAppIntf.SDESPktReceived(sdesPkt.participants);
/*     */         }
/*     */         continue;
/*     */       } 
/* 239 */       if (aPkt.getClass() == RtcpPktBYE.class) {
/* 240 */         RtcpPktBYE byePkt = (RtcpPktBYE)aPkt;
/*     */         
/* 242 */         long time = System.currentTimeMillis();
/* 243 */         Participant[] partArray = new Participant[byePkt.ssrcArray.length];
/*     */         
/* 245 */         for (int i = 0; i < byePkt.ssrcArray.length; i++) {
/* 246 */           partArray[i] = this.rtpSession.partDb.getParticipant(byePkt.ssrcArray[i]);
/* 247 */           if (partArray[i] != null) {
/* 248 */             (partArray[i]).timestampBYE = time;
/*     */           }
/*     */         } 
/* 251 */         if (this.rtpSession.rtcpAppIntf != null) {
/* 252 */           this.rtpSession.rtcpAppIntf.BYEPktReceived(partArray, new String(byePkt.reason));
/*     */         }
/*     */         continue;
/*     */       } 
/* 256 */       if (aPkt.getClass() == RtcpPktAPP.class) {
/* 257 */         RtcpPktAPP appPkt = (RtcpPktAPP)aPkt;
/*     */         
/* 259 */         Participant part = findParticipant(appPkt.ssrc, packet);
/*     */         
/* 261 */         if (this.rtpSession.rtcpAppIntf != null) {
/* 262 */           this.rtpSession.rtcpAppIntf.APPPktReceived(part, appPkt.itemCount, appPkt.pktName, appPkt.pktData);
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 270 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String debugErrorString(int errorCode) {
/* 280 */     String aStr = "";
/* 281 */     switch (errorCode) { case -1:
/* 282 */         aStr = "The first packet was not of type SR or RR.";
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
/* 295 */         return aStr;case -2: aStr = "The padding bit was set for the first packet."; return aStr;case -200: aStr = " Error parsing Sender Report packet."; return aStr;case -201: aStr = " Error parsing Receiver Report packet."; return aStr;case -202: aStr = " Error parsing SDES packet"; return aStr;case -203: aStr = " Error parsing BYE packet."; return aStr;case -204: aStr = " Error parsing Application specific packet."; return aStr;case -205: aStr = " Error parsing RTP Feedback packet."; return aStr;case -206: aStr = " Error parsing Payload-Specific Feedback packet."; return aStr; }  aStr = "Unknown error code " + errorCode + "."; return aStr;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 316 */     while (!this.rtpSession.endSession) {
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
/* 327 */       byte[] rawPkt = new byte[1500];
/* 328 */       DatagramPacket packet = new DatagramPacket(rawPkt, rawPkt.length);
/*     */ 
/*     */       
/* 331 */       if (!this.rtpSession.mcSession) {
/*     */         
/*     */         try {
/* 334 */           this.rtcpSession.rtcpSock.receive(packet);
/* 335 */         } catch (IOException e) {
/* 336 */           if (!this.rtpSession.endSession) {
/* 337 */             e.printStackTrace();
/*     */           } else {
/*     */             
/*     */             continue;
/*     */           } 
/*     */         } 
/*     */       } else {
/*     */         try {
/* 345 */           this.rtcpSession.rtcpMCSock.receive(packet);
/* 346 */         } catch (IOException e) {
/* 347 */           if (!this.rtpSession.endSession) {
/* 348 */             e.printStackTrace();
/*     */           } else {
/*     */             continue;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 356 */       if ((this.rtpSession.mcSession && !packet.getSocketAddress().equals(this.rtcpSession.rtcpMCSock)) || 
/* 357 */         !packet.getSocketAddress().equals(this.rtcpSession.rtcpSock))
/*     */       {
/* 359 */         parsePacket(packet);
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\rtplib\RTCPReceiverThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */