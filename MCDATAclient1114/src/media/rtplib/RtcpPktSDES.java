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
/*     */ public class RtcpPktSDES
/*     */   extends RtcpPkt
/*     */ {
/*     */   boolean reportSelf = true;
/*  33 */   RTPSession rtpSession = null;
/*     */   
/*  35 */   protected Participant[] participants = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RtcpPktSDES(boolean reportThisSession, RTPSession rtpSession, Participant[] additionalParticipants) {
/*  50 */     this.packetType = 202;
/*     */     
/*  52 */     this.reportSelf = reportThisSession;
/*  53 */     this.participants = additionalParticipants;
/*  54 */     this.rtpSession = rtpSession;
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
/*     */   protected RtcpPktSDES(byte[] aRawPkt, int start, InetSocketAddress socket, ParticipantDatabase partDb) {
/*  69 */     this.rawPkt = aRawPkt;
/*     */     
/*  71 */     if (!parseHeaders(start) || this.packetType != 202) {
/*     */ 
/*     */ 
/*     */       
/*  75 */       this.problem = -202;
/*     */     }
/*     */     else {
/*     */       
/*  79 */       int curPos = 4 + start;
/*     */ 
/*     */ 
/*     */       
/*  83 */       boolean endReached = false;
/*     */       
/*  85 */       this.participants = new Participant[this.itemCount];
/*     */ 
/*     */       
/*  88 */       for (int i = 0; i < this.itemCount; i++) {
/*  89 */         boolean newPart; long ssrc = StaticProcs.bytesToUIntLong(aRawPkt, curPos);
/*  90 */         Participant part = partDb.getParticipant(ssrc);
/*  91 */         if (part == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  96 */           part = new Participant(socket, socket, ssrc);
/*  97 */           newPart = true;
/*     */         } else {
/*  99 */           newPart = false;
/*     */         } 
/*     */         
/* 102 */         curPos += 4;
/*     */ 
/*     */ 
/*     */         
/* 106 */         while (!endReached && curPos / 4 <= this.length) {
/*     */           
/* 108 */           int curType = aRawPkt[curPos];
/*     */           
/* 110 */           if (curType == 0) {
/* 111 */             curPos += 4 - curPos % 4;
/* 112 */             endReached = true; continue;
/*     */           } 
/* 114 */           int curLength = aRawPkt[curPos + 1];
/*     */ 
/*     */           
/* 117 */           if (curLength > 0) {
/* 118 */             byte[] item = new byte[curLength];
/*     */             
/* 120 */             System.arraycopy(aRawPkt, curPos + 2, item, 0, curLength);
/*     */             
/* 122 */             switch (curType) { case 1:
/* 123 */                 part.cname = new String(item); break;
/* 124 */               case 2: part.name = new String(item); break;
/* 125 */               case 3: part.email = new String(item); break;
/* 126 */               case 4: part.phone = new String(item); break;
/* 127 */               case 5: part.loc = new String(item); break;
/* 128 */               case 6: part.tool = new String(item); break;
/* 129 */               case 7: part.note = new String(item); break;
/* 130 */               case 8: part.priv = new String(item);
/*     */                 break; }
/*     */ 
/*     */           
/*     */           } else {
/* 135 */             switch (curType) { case 1:
/* 136 */                 part.cname = null; break;
/* 137 */               case 2: part.name = null; break;
/* 138 */               case 3: part.email = null; break;
/* 139 */               case 4: part.phone = null; break;
/* 140 */               case 5: part.loc = null; break;
/* 141 */               case 6: part.tool = null; break;
/* 142 */               case 7: part.note = null; break;
/* 143 */               case 8: part.priv = null;
/*     */                 break; }
/*     */           
/*     */           } 
/* 147 */           curPos = curPos + curLength + 2;
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 152 */         this.participants[i] = part;
/* 153 */         if (newPart) {
/* 154 */           partDb.addParticipant(2, part);
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
/*     */   protected void encode() {
/* 170 */     byte[] temp = new byte[1450];
/* 171 */     byte[] someBytes = StaticProcs.uIntLongToByteWord(this.rtpSession.ssrc);
/* 172 */     System.arraycopy(someBytes, 0, temp, 4, 4);
/* 173 */     int pos = 8;
/*     */     
/* 175 */     String tmpString = null;
/* 176 */     for (int i = 1; i < 9; i++) {
/* 177 */       switch (i) { case 1:
/* 178 */           tmpString = this.rtpSession.cname; break;
/* 179 */         case 2: tmpString = this.rtpSession.name; break;
/* 180 */         case 3: tmpString = this.rtpSession.email; break;
/* 181 */         case 4: tmpString = this.rtpSession.phone; break;
/* 182 */         case 5: tmpString = this.rtpSession.loc; break;
/* 183 */         case 6: tmpString = this.rtpSession.tool; break;
/* 184 */         case 7: tmpString = this.rtpSession.note; break;
/* 185 */         case 8: tmpString = this.rtpSession.priv;
/*     */           break; }
/*     */       
/* 188 */       if (tmpString != null) {
/* 189 */         someBytes = tmpString.getBytes();
/* 190 */         temp[pos] = (byte)i;
/* 191 */         temp[pos + 1] = (byte)someBytes.length;
/* 192 */         System.arraycopy(someBytes, 0, temp, pos + 2, someBytes.length);
/*     */         
/* 194 */         pos = pos + someBytes.length + 2;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 200 */     int leftover = pos % 4;
/* 201 */     if (leftover == 1) {
/* 202 */       temp[pos] = 0;
/* 203 */       temp[pos + 1] = 1;
/* 204 */       pos += 3;
/* 205 */     } else if (leftover == 2) {
/* 206 */       temp[pos] = 0;
/* 207 */       temp[pos + 1] = 0;
/* 208 */       pos += 2;
/* 209 */     } else if (leftover == 3) {
/* 210 */       temp[pos] = 0;
/* 211 */       temp[pos + 1] = 3;
/* 212 */       pos += 5;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 217 */     this.rawPkt = new byte[pos];
/* 218 */     this.itemCount = 1;
/*     */     
/* 220 */     System.arraycopy(temp, 0, this.rawPkt, 0, pos);
/* 221 */     writeHeaders();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void debugPrint() {
/* 228 */     System.out.println("RtcpPktSDES.debugPrint() ");
/* 229 */     if (this.participants != null) {
/* 230 */       for (int i = 0; i < this.participants.length; i++) {
/* 231 */         Participant part = this.participants[i];
/* 232 */         System.out.println("     part.ssrc: " + part.ssrc + "  part.cname: " + part.cname + " part.loc: " + part.loc);
/*     */       } 
/*     */     } else {
/* 235 */       System.out.println("     nothing to report (only valid for received packets)");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\rtplib\RtcpPktSDES.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */