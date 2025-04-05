/*     */ package media.rtplib;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramPacket;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RTPReceiverThread
/*     */   extends Thread
/*     */ {
/*  37 */   RTPSession rtpSession = null;
/*     */   
/*     */   RTPReceiverThread(RTPSession session) {
/*  40 */     this.rtpSession = session;
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
/*     */   public void run() {
/*  55 */     while (!this.rtpSession.endSession) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  65 */       byte[] rawPkt = new byte[1500];
/*  66 */       DatagramPacket packet = new DatagramPacket(rawPkt, rawPkt.length);
/*     */       
/*  68 */       if (!this.rtpSession.mcSession) {
/*     */         
/*     */         try {
/*  71 */           this.rtpSession.rtpSock.receive(packet);
/*  72 */         } catch (IOException e) {
/*  73 */           if (!this.rtpSession.endSession) {
/*  74 */             e.printStackTrace();
/*     */           } else {
/*     */             
/*     */             continue;
/*     */           } 
/*     */         } 
/*     */       } else {
/*     */         try {
/*  82 */           this.rtpSession.rtpMCSock.receive(packet);
/*  83 */         } catch (IOException e) {
/*  84 */           if (!this.rtpSession.endSession) {
/*  85 */             e.printStackTrace();
/*     */           } else {
/*     */             continue;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/*  93 */       RtpPkt pkt = new RtpPkt(rawPkt, packet.getLength());
/*     */ 
/*     */       
/*  96 */       if (pkt == null) {
/*  97 */         System.out.println("Received invalid RTP packet. Ignoring");
/*     */         
/*     */         continue;
/*     */       } 
/* 101 */       long pktSsrc = pkt.getSsrc();
/*     */ 
/*     */       
/* 104 */       if (this.rtpSession.ssrc == pktSsrc) {
/* 105 */         this.rtpSession.resolveSsrcConflict();
/*     */       }
/* 107 */       long[] csrcArray = pkt.getCsrcArray();
/* 108 */       if (csrcArray != null) {
/* 109 */         for (int i = 0; i < csrcArray.length; i++) {
/* 110 */           if (csrcArray[i] == this.rtpSession.ssrc);
/* 111 */           this.rtpSession.resolveSsrcConflict();
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 124 */       Participant part = this.rtpSession.partDb.getParticipant(pktSsrc);
/*     */       
/* 126 */       if (part == null) {
/* 127 */         InetSocketAddress nullSocket = null;
/* 128 */         part = new Participant((InetSocketAddress)packet.getSocketAddress(), nullSocket, pkt.getSsrc());
/* 129 */         part.unexpected = true;
/* 130 */         this.rtpSession.partDb.addParticipant(1, part);
/*     */       } 
/*     */ 
/*     */       
/* 134 */       if (part.rtpAddress == null || packet.getAddress().equals(part.rtpAddress.getAddress())) {
/* 135 */         PktBuffer pktBuffer = part.pktBuffer;
/*     */         
/* 137 */         if (pktBuffer != null) {
/*     */           
/* 139 */           pktBuffer.addPkt(pkt);
/*     */         } else {
/*     */           
/* 142 */           pktBuffer = new PktBuffer(this.rtpSession, part, pkt);
/* 143 */           part.pktBuffer = pktBuffer;
/*     */         } 
/*     */       } else {
/* 146 */         System.out.println("RTPReceiverThread: Got an unexpected packet from " + pkt.getSsrc() + 
/* 147 */             " the sending ip-address was " + packet.getAddress().toString() + 
/* 148 */             ", we expected from " + part.rtpAddress.toString());
/*     */       } 
/*     */ 
/*     */       
/* 152 */       part.updateRRStats(packet.getLength(), pkt);
/*     */       
/* 154 */       part.lastRtpPkt = System.currentTimeMillis();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 161 */       this.rtpSession.pktBufLock.lock(); 
/* 162 */       try { this.rtpSession.pktBufDataReady.signalAll(); }
/* 163 */       finally { this.rtpSession.pktBufLock.unlock(); }
/*     */     
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\rtplib\RTPReceiverThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */