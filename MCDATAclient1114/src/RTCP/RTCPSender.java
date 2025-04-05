/*     */ package RTCP;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.InetAddress;
/*     */ import java.net.SocketException;
/*     */ import java.net.UnknownHostException;
/*     */ import session.PermissionState;
/*     */ import sip.SipClient;
/*     */ 
/*     */ 
/*     */ public class RTCPSender
/*     */   extends Thread
/*     */ {
/*     */   private String peerAddress;
/*     */   private String clienturi;
/*     */   private String sessionid;
/*     */   private int peerRTCPPort;
/*  20 */   private int count = 0;
/*     */   private int localPort;
/*  22 */   DatagramSocket socket = null;
/*     */   int packetState;
/*     */   private PermissionState permissionState;
/*     */   private int tbPriority;
/*     */   private int lastSeq;
/*  27 */   private short ind = Short.MIN_VALUE;
/*     */ 
/*     */   
/*     */   private SipClient sipclient;
/*     */ 
/*     */   
/*     */   public RTCPSender(String peerAddress, int peerRTCPPort, DatagramSocket socket, int packetState, PermissionState permissionState) {
/*  34 */     this.peerAddress = peerAddress;
/*  35 */     this.peerRTCPPort = peerRTCPPort;
/*  36 */     this.socket = socket;
/*  37 */     this.packetState = packetState;
/*  38 */     this.permissionState = permissionState;
/*     */   }
/*     */   
/*     */   public void set(String clientUri, String sessionId, int priority) {
/*  42 */     this.clienturi = clientUri;
/*  43 */     this.sessionid = sessionId;
/*  44 */     this.tbPriority = priority; } public void run() { 
/*     */     try { rtcp_request requestmsg; int ignoreSeqNum; rtcp_release releaseMsg; rtcp_acknowledgement mcpcAck; rtcp_queue_position_request queuereqMsg; rtcp_ack mcptAck; rtcp_connect mcpcConnect; rtcp_disconnect disconnectMsg; rtcp_idle idleMsg; rtcp_deny denyMsg;
/*     */       rtcp_granted grantMsg;
/*     */       rtcp_queue_position_info queueInfoMsg;
/*     */       rtcp_revoke revokeMsg;
/*  49 */       InetAddress peerIP = InetAddress.getByName(this.peerAddress);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  55 */       Rtcp rtcp = new Rtcp();
/*  56 */       TimeStamp ts = TimeStamp.getCurrentTime();
/*     */ 
/*     */       
/*  59 */       switch (this.packetState) {
/*     */         
/*     */         case 0:
/*  62 */           requestmsg = new rtcp_request(rtcp.gen_ssrc().longValue(), getTBPriority(), getclienturi(), this.ind);
/*  63 */           sendRtcp(rtcp, requestmsg.message, peerIP);
/*  64 */           System.out.println("===== sent RTCP-MCPT-Floor Request =====>");
/*     */           break;
/*     */         
/*     */         case 4:
/*  68 */           ignoreSeqNum = 0;
/*  69 */           if (this.lastSeq == -1) {
/*  70 */             ignoreSeqNum = 1;
/*     */           }
/*  72 */           releaseMsg = new rtcp_release(rtcp.gen_ssrc().longValue(), getclienturi(), this.ind);
/*  73 */           sendRtcp(rtcp, releaseMsg.message, peerIP);
/*  74 */           System.out.println("===== sent RTCP-MCPT-Floor-Release Message=====>");
/*     */           break;
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
/*     */         case 2:
/*  88 */           mcpcAck = new rtcp_acknowledgement(rtcp.gen_ssrc().longValue(), 0);
/*  89 */           sendRtcp(rtcp, rtcp_acknowledgement.message, peerIP);
/*  90 */           System.out.println("===== sent RTCP-MCPC-Acknowledgement =====>");
/*     */           break;
/*     */         case 8:
/*  93 */           queuereqMsg = new rtcp_queue_position_request(rtcp.gen_ssrc().longValue(), getclienturi());
/*  94 */           sendRtcp(rtcp, queuereqMsg.message, peerIP);
/*  95 */           System.out.println("===== sent RTCP Floor Queue Position Request =====>");
/*     */           break;
/*     */         
/*     */         case 10:
/*  99 */           mcptAck = new rtcp_ack(rtcp.gen_ssrc().longValue(), 0);
/* 100 */           sendRtcp(rtcp, rtcp_ack.message, peerIP);
/* 101 */           System.out.println("===== sent RTCP Floor Ack =====>");
/*     */           break;
/*     */         case 16:
/* 104 */           mcpcConnect = new rtcp_connect(rtcp.gen_ssrc().longValue(), getclienturi(), getsessionId(), 1, 2, 0);
/* 105 */           sendRtcp(rtcp, rtcp_connect.message, peerIP);
/* 106 */           System.out.println("===== sent MCPC Connect Message =====>");
/*     */           break;
/*     */ 
/*     */         
/*     */         case 17:
/* 111 */           disconnectMsg = new rtcp_disconnect(rtcp.gen_ssrc().longValue(), getsessionId());
/* 112 */           sendRtcp(rtcp, disconnectMsg.message, peerIP);
/* 113 */           System.out.println("===== sent RTCP-MCPC-disconnect Request =====>");
/*     */           break;
/*     */         case 5:
/* 116 */           idleMsg = new rtcp_idle(rtcp.gen_ssrc().longValue(), (short)10, this.ind);
/* 117 */           sendRtcp(rtcp, idleMsg.message, peerIP);
/* 118 */           System.out.println("===== sent RTCP-MCPT-IDLE Request =====>");
/*     */           break;
/*     */         case 3:
/* 121 */           denyMsg = new rtcp_deny(rtcp.gen_ssrc().longValue(), 4, getclienturi(), this.ind);
/* 122 */           sendRtcp(rtcp, denyMsg.message, peerIP);
/* 123 */           System.out.println("===== sent RTCP-MCPT-DENY Request =====>");
/*     */           break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 1:
/* 132 */           grantMsg = new rtcp_granted(rtcp.gen_ssrc().longValue(), rtcp.gen_ssrc().longValue(), (short)60, 4, this.ind);
/* 133 */           sendRtcp(rtcp, grantMsg.message, peerIP);
/* 134 */           System.out.println("===== sent RTCP-MCPT-Granted Request =====>");
/*     */           break;
/*     */         case 9:
/* 137 */           queueInfoMsg = new rtcp_queue_position_info(rtcp.gen_ssrc().longValue(), getclienturi(), 2, 4, this.ind);
/* 138 */           sendRtcp(rtcp, queueInfoMsg.message, peerIP);
/* 139 */           System.out.println("===== sent RTCP-MCPT-QUEUE Position Info Request =====>");
/*     */           break;
/*     */         case 6:
/* 142 */           revokeMsg = new rtcp_revoke(rtcp.gen_ssrc().longValue(), (short)255, this.ind);
/* 143 */           sendRtcp(rtcp, revokeMsg.message, peerIP);
/* 144 */           System.out.println("===== sent RTCP-MCPT-Revoke Request =====>");
/*     */           break;
/*     */       } 
/*     */        }
/* 148 */     catch (SocketException e)
/* 149 */     { e.printStackTrace(); }
/* 150 */     catch (UnknownHostException e)
/* 151 */     { e.printStackTrace(); }
/* 152 */     catch (IOException e)
/* 153 */     { e.printStackTrace(); }
/* 154 */     catch (Exception exception) {  }
/*     */     finally
/* 156 */     { Thread.currentThread().interrupt(); }
/*     */      }
/*     */ 
/*     */   
/*     */   public void setLastSeq(int lastSeq) {
/* 161 */     this.lastSeq = lastSeq;
/*     */   }
/*     */   
/*     */   private void sendRtcp(Rtcp rtcp, byte[] rtcpMsg, InetAddress peerIP) throws IOException {
/* 165 */     DatagramPacket packet = new DatagramPacket(rtcpMsg, rtcpMsg.length, peerIP, this.peerRTCPPort);
/*     */     
/* 167 */     this.socket.send(packet);
/* 168 */     this.count++;
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
/*     */   public void setclienturi(String client) {
/* 191 */     this.clienturi = client;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getclienturi() {
/* 201 */     return this.clienturi;
/*     */   }
/*     */   
/*     */   public void setsessionId(String sessionId) {
/* 205 */     this.sessionid = sessionId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getsessionId() {
/* 210 */     if (this.sessionid == null)
/*     */     {
/*     */       
/* 213 */       this.sessionid = "setsessionId@domain";
/*     */     }
/* 215 */     return this.sessionid;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTBPriority(int tbPriority) {
/* 220 */     this.tbPriority = tbPriority;
/*     */   }
/*     */   
/*     */   public int getTBPriority() {
/* 224 */     if (this.tbPriority == 0)
/* 225 */       this.tbPriority = 4; 
/* 226 */     return this.tbPriority;
/*     */   }
/*     */   
/*     */   public boolean isFRRequest() {
/* 230 */     return (this.packetState == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInd(short Ind) {
/* 235 */     this.ind = Ind;
/*     */   }
/*     */ 
/*     */   
/*     */   public short getInd() {
/* 240 */     return this.ind;
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\RTCP\RTCPSender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */