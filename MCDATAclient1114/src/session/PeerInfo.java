/*    */ package session;
/*    */ 
/*    */ import gov.nist.javax.sip.address.SipUri;
/*    */ 
/*    */ 
/*    */ public class PeerInfo
/*    */ {
/*    */   private int peerRtpPort;
/*    */   private int peerRtcpPort;
/*    */   private String peerIp;
/*    */   private String peerUserName;
/*    */   private String peerSipDomain;
/*    */   private String peerTag;
/*    */   private SipUri peerSipUri;
/*    */   private int peerSipPort;
/*    */   
/*    */   public PeerInfo(SipUri sipUri) {
/* 18 */     SipUri sipUriClone = (SipUri)sipUri.clone();
/* 19 */     sipUriClone.clearUriParms();
/*    */     
/* 21 */     this.peerSipUri = sipUriClone;
/*    */   }
/*    */   
/*    */   public int getPeerRtpPort() {
/* 25 */     return this.peerRtpPort;
/*    */   }
/*    */   
/*    */   public void setPeerRtpPort(int peerRtpPort) {
/* 29 */     this.peerRtpPort = peerRtpPort;
/*    */   }
/*    */   
/*    */   public int getPeerRtcpPort() {
/* 33 */     return this.peerRtcpPort;
/*    */   }
/*    */   
/*    */   public void setPeerRtcpPort(int peerRtcpPort) {
/* 37 */     this.peerRtcpPort = peerRtcpPort;
/*    */   }
/*    */   
/*    */   public String getPeerIp() {
/* 41 */     return this.peerIp;
/*    */   }
/*    */   
/*    */   public void setPeerIp(String peerIp) {
/* 45 */     this.peerIp = peerIp;
/*    */   }
/*    */   
/*    */   public String getPeerUserName() {
/* 49 */     return this.peerUserName;
/*    */   }
/*    */   
/*    */   public void setPeerUserName(String peerUserName) {
/* 53 */     this.peerUserName = peerUserName;
/*    */   }
/*    */   
/*    */   public String getPeerSipDomain() {
/* 57 */     return this.peerSipDomain;
/*    */   }
/*    */   
/*    */   public void setPeerSipDomain(String peerSipDomain) {
/* 61 */     this.peerSipDomain = peerSipDomain;
/*    */   }
/*    */   
/*    */   public String getPeerTag() {
/* 65 */     return this.peerTag;
/*    */   }
/*    */   
/*    */   public void setPeerTag(String peerTag) {
/* 69 */     this.peerTag = peerTag;
/*    */   }
/*    */   
/*    */   public SipUri getPeerSipUri() {
/* 73 */     return this.peerSipUri;
/*    */   }
/*    */   
/*    */   public void setPeerSipUri(SipUri peerSipUri) {
/* 77 */     peerSipUri.clearUriParms();
/* 78 */     this.peerSipUri = peerSipUri;
/*    */   }
/*    */   
/*    */   public int getPeerSipPort() {
/* 82 */     return this.peerSipPort;
/*    */   }
/*    */   
/*    */   public void setPeerSipPort(int peerSipPort) {
/* 86 */     this.peerSipPort = peerSipPort;
/*    */   }
/*    */   
/*    */   public void setSDPInfo(MySdpInfo sdpInfo) {
/* 90 */     this.peerIp = sdpInfo.getIp();
/* 91 */     this.peerRtpPort = sdpInfo.getPort();
/* 92 */     this.peerRtcpPort = sdpInfo.getRtcpPort();
/*    */   }
/*    */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\session\PeerInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */