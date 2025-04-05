/*     */ package media.rtplib;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParticipantDatabase
/*     */ {
/*  42 */   RTPSession rtpSession = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   LinkedList<Participant> receivers = new LinkedList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   ConcurrentHashMap<Long, Participant> ssrcTable = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ParticipantDatabase(RTPSession parent) {
/*  61 */     this.rtpSession = parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int addParticipant(int cameFrom, Participant p) {
/*  72 */     if (this.rtpSession.mcSession) {
/*  73 */       return addParticipantMulticast(cameFrom, p);
/*     */     }
/*  75 */     return addParticipantUnicast(cameFrom, p);
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
/*     */   private int addParticipantMulticast(int cameFrom, Participant p) {
/*  88 */     if (cameFrom == 0) {
/*  89 */       System.out.println("ParticipantDatabase.addParticipant() doesnt expect application to add participants to multicast session.");
/*     */       
/*  91 */       return -1;
/*     */     } 
/*     */     
/*  94 */     if (this.ssrcTable.contains(Long.valueOf(p.ssrc))) {
/*  95 */       System.out.println("ParticipantDatabase.addParticipant() SSRC already known " + 
/*  96 */           Long.toString(p.ssrc));
/*  97 */       return -2;
/*     */     } 
/*  99 */     this.ssrcTable.put(Long.valueOf(p.ssrc), p);
/* 100 */     return 0;
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
/*     */   private int addParticipantUnicast(int cameFrom, Participant p) {
/* 115 */     if (cameFrom == 0) {
/*     */       
/* 117 */       boolean bool = true;
/*     */       
/* 119 */       Enumeration<Participant> enu = this.ssrcTable.elements();
/* 120 */       while (bool && enu.hasMoreElements()) {
/* 121 */         Participant part = enu.nextElement();
/* 122 */         if (part.unexpected && (
/* 123 */           part.rtcpReceivedFromAddress.equals(part.rtcpAddress.getAddress()) || 
/* 124 */           part.rtpReceivedFromAddress.equals(part.rtpAddress.getAddress()))) {
/*     */           
/* 126 */           part.rtpAddress = p.rtpAddress;
/* 127 */           part.rtcpAddress = p.rtcpAddress;
/* 128 */           part.unexpected = false;
/*     */ 
/*     */           
/* 131 */           Participant[] partArray = { part };
/* 132 */           this.rtpSession.appIntf.userEvent(5, partArray);
/*     */           
/* 134 */           bool = false;
/* 135 */           p = part;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 140 */       this.receivers.add(p);
/* 141 */       return 0;
/*     */     } 
/*     */ 
/*     */     
/* 145 */     boolean notDone = true;
/*     */     
/* 147 */     Iterator<Participant> iter = this.receivers.iterator();
/*     */     
/* 149 */     while (notDone && iter.hasNext()) {
/* 150 */       Participant part = iter.next();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 158 */       if ((cameFrom == 1 && p.rtpReceivedFromAddress.getAddress().equals(part.rtpAddress.getAddress())) || (
/* 159 */         cameFrom == 2 && p.rtcpReceivedFromAddress.getAddress().equals(part.rtcpAddress.getAddress()))) {
/*     */         
/* 161 */         part.rtpReceivedFromAddress = p.rtpReceivedFromAddress;
/* 162 */         part.rtcpReceivedFromAddress = p.rtcpReceivedFromAddress;
/*     */ 
/*     */         
/* 165 */         part.ssrc = p.ssrc;
/* 166 */         part.cname = p.cname;
/* 167 */         part.name = p.name;
/* 168 */         part.loc = p.loc;
/* 169 */         part.phone = p.phone;
/* 170 */         part.email = p.email;
/* 171 */         part.note = p.note;
/* 172 */         part.tool = p.tool;
/* 173 */         part.priv = p.priv;
/*     */         
/* 175 */         this.ssrcTable.put(Long.valueOf(part.ssrc), part);
/*     */ 
/*     */         
/* 178 */         Participant[] partArray = { part };
/* 179 */         this.rtpSession.appIntf.userEvent(5, partArray);
/* 180 */         return 0;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 185 */     this.ssrcTable.put(Long.valueOf(p.ssrc), p);
/* 186 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void removeParticipant(Participant p) {
/* 196 */     if (!this.rtpSession.mcSession) {
/* 197 */       this.receivers.remove(p);
/*     */     }
/* 199 */     this.ssrcTable.remove(Long.valueOf(p.ssrc), p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Participant getParticipant(long ssrc) {
/* 209 */     Participant p = null;
/* 210 */     p = this.ssrcTable.get(Long.valueOf(ssrc));
/* 211 */     return p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Iterator<Participant> getUnicastReceivers() {
/* 222 */     if (!this.rtpSession.mcSession) {
/* 223 */       return this.receivers.iterator();
/*     */     }
/* 225 */     System.out.println("Request for ParticipantDatabase.getUnicastReceivers in multicast session");
/* 226 */     return null;
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
/*     */   protected Enumeration<Participant> getParticipants() {
/* 238 */     return this.ssrcTable.elements();
/*     */   }
/*     */   
/*     */   protected void debugPrint() {
/* 242 */     System.out.println("   ParticipantDatabase.debugPrint()");
/*     */     
/* 244 */     Enumeration<Participant> enu = this.ssrcTable.elements();
/* 245 */     while (enu.hasMoreElements()) {
/* 246 */       Participant p = enu.nextElement();
/* 247 */       System.out.println("           ssrcTable ssrc:" + p.ssrc + " cname:" + p.cname + 
/* 248 */           " loc:" + p.loc + " rtpAddress:" + p.rtpAddress + " rtcpAddress:" + p.rtcpAddress);
/*     */     } 
/*     */     
/* 251 */     Iterator<Participant> iter = this.receivers.iterator();
/* 252 */     while (iter.hasNext()) {
/* 253 */       Participant p = iter.next();
/* 254 */       System.out.println("           receivers: " + p.rtpAddress.toString());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\rtplib\ParticipantDatabase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */