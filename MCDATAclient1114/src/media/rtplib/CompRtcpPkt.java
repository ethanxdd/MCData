/*     */ package media.rtplib;
/*     */ 
/*     */ import java.net.InetSocketAddress;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompRtcpPkt
/*     */ {
/*  40 */   protected int problem = 0;
/*     */   
/*  42 */   protected LinkedList<RtcpPkt> rtcpPkts = new LinkedList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CompRtcpPkt() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addPacket(RtcpPkt aPkt) {
/*  66 */     if (aPkt.problem == 0) {
/*  67 */       this.rtcpPkts.add(aPkt);
/*     */     } else {
/*  69 */       this.problem = aPkt.problem;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CompRtcpPkt(byte[] rawPkt, int packetSize, InetSocketAddress adr, RTPSession rtpSession) {
/*  94 */     int start = 0;
/*     */     
/*  96 */     while (start < packetSize && this.problem == 0) {
/*  97 */       int length = StaticProcs.bytesToUIntInt(rawPkt, start + 2) + 1;
/*     */       
/*  99 */       if (length * 4 + start > rawPkt.length) {
/* 100 */         System.out.println("!!!! CompRtcpPkt.(rawPkt,..,..) length (" + (length * 4 + start) + 
/* 101 */             ") exceeds size of raw packet (" + rawPkt.length + ") !");
/* 102 */         this.problem = -3;
/*     */       } 
/*     */       
/* 105 */       int pktType = rawPkt[start + 1];
/*     */       
/* 107 */       if (pktType < 0) {
/* 108 */         pktType += 256;
/*     */       }
/*     */ 
/*     */       
/* 112 */       if (start == 0) {
/*     */         
/* 114 */         if (pktType != 200 && pktType != 201)
/*     */         {
/*     */ 
/*     */           
/* 118 */           this.problem = -1;
/*     */         }
/*     */ 
/*     */         
/* 122 */         if ((rawPkt[start] & 0x20) >>> 5 == 1)
/*     */         {
/*     */ 
/*     */           
/* 126 */           this.problem = -2;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 131 */       if (pktType == 200) {
/* 132 */         addPacket(new RtcpPktSR(rawPkt, start, length * 4));
/* 133 */       } else if (pktType == 201) {
/* 134 */         addPacket(new RtcpPktRR(rawPkt, start, -1));
/* 135 */       } else if (pktType == 202) {
/* 136 */         addPacket(new RtcpPktSDES(rawPkt, start, adr, rtpSession.partDb));
/* 137 */       } else if (pktType == 203) {
/* 138 */         addPacket(new RtcpPktBYE(rawPkt, start));
/* 139 */       } else if (pktType == 204) {
/* 140 */         addPacket(new RtcpPktAPP(rawPkt, start));
/* 141 */       } else if (pktType == 205) {
/* 142 */         addPacket(new RtcpPktRTPFB(rawPkt, start, rtpSession));
/* 143 */       } else if (pktType == 206) {
/* 144 */         addPacket(new RtcpPktPSFB(rawPkt, start, rtpSession));
/*     */       } else {
/* 146 */         System.out.println("!!!! CompRtcpPkt(byte[] rawPkt, int packetSize...) UNKNOWN RTCP PACKET TYPE:" + 
/* 147 */             pktType);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 152 */       start += length * 4;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected byte[] encode() {
/* 176 */     ListIterator<RtcpPkt> iter = this.rtcpPkts.listIterator();
/*     */     
/* 178 */     byte[] rawPkt = new byte[1500];
/* 179 */     int index = 0;
/*     */     
/* 181 */     while (iter.hasNext()) {
/* 182 */       RtcpPkt aPkt = iter.next();
/*     */       
/* 184 */       if (aPkt.packetType == 200) {
/* 185 */         RtcpPktSR pkt = (RtcpPktSR)aPkt;
/* 186 */         pkt.encode();
/* 187 */         System.arraycopy(pkt.rawPkt, 0, rawPkt, index, pkt.rawPkt.length);
/* 188 */         index += pkt.rawPkt.length; continue;
/* 189 */       }  if (aPkt.packetType == 201) {
/* 190 */         RtcpPktRR pkt = (RtcpPktRR)aPkt;
/* 191 */         pkt.encode();
/* 192 */         System.arraycopy(pkt.rawPkt, 0, rawPkt, index, pkt.rawPkt.length);
/* 193 */         index += pkt.rawPkt.length; continue;
/* 194 */       }  if (aPkt.packetType == 202) {
/* 195 */         RtcpPktSDES pkt = (RtcpPktSDES)aPkt;
/* 196 */         pkt.encode();
/*     */         
/* 198 */         System.arraycopy(pkt.rawPkt, 0, rawPkt, index, pkt.rawPkt.length);
/* 199 */         index += pkt.rawPkt.length; continue;
/* 200 */       }  if (aPkt.packetType == 203) {
/* 201 */         RtcpPktBYE pkt = (RtcpPktBYE)aPkt;
/* 202 */         pkt.encode();
/* 203 */         System.arraycopy(pkt.rawPkt, 0, rawPkt, index, pkt.rawPkt.length);
/* 204 */         index += pkt.rawPkt.length; continue;
/* 205 */       }  if (aPkt.packetType == 204) {
/* 206 */         RtcpPktAPP pkt = (RtcpPktAPP)aPkt;
/* 207 */         pkt.encode();
/* 208 */         System.arraycopy(pkt.rawPkt, 0, rawPkt, index, pkt.rawPkt.length);
/* 209 */         index += pkt.rawPkt.length; continue;
/* 210 */       }  if (aPkt.packetType == 205) {
/* 211 */         RtcpPktRTPFB pkt = (RtcpPktRTPFB)aPkt;
/* 212 */         pkt.encode();
/* 213 */         System.arraycopy(pkt.rawPkt, 0, rawPkt, index, pkt.rawPkt.length);
/* 214 */         index += pkt.rawPkt.length; continue;
/* 215 */       }  if (aPkt.packetType == 206) {
/* 216 */         RtcpPktPSFB pkt = (RtcpPktPSFB)aPkt;
/* 217 */         pkt.encode();
/* 218 */         System.arraycopy(pkt.rawPkt, 0, rawPkt, index, pkt.rawPkt.length);
/* 219 */         index += pkt.rawPkt.length; continue;
/*     */       } 
/* 221 */       System.out.println("CompRtcpPkt aPkt.packetType:" + aPkt.packetType);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 226 */     byte[] output = new byte[index];
/*     */     
/* 228 */     System.arraycopy(rawPkt, 0, output, 0, index);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 233 */     return output;
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\rtplib\CompRtcpPkt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */