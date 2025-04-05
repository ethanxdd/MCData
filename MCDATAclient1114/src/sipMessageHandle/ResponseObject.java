/*     */ package sipMessageHandle;
/*     */ 
/*     */ import gov.nist.javax.sip.address.GenericURI;
/*     */ import gov.nist.javax.sip.address.SipUri;
/*     */ import gov.nist.javax.sip.header.CSeq;
/*     */ import gov.nist.javax.sip.header.From;
/*     */ import gov.nist.javax.sip.header.SIPETag;
/*     */ import gov.nist.javax.sip.header.To;
/*     */ import gov.nist.javax.sip.header.Via;
/*     */ import gov.nist.javax.sip.message.SIPResponse;
/*     */ import javax.sip.message.Response;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResponseObject
/*     */ {
/*     */   private SIPResponse sipResponse;
/*     */   private static final String TAG = "ResponseObject";
/*     */   
/*     */   private ResponseObject(Response response) {
/*  24 */     this.sipResponse = (SIPResponse)response;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ResponseObject parse(Response response) {
/*  29 */     return new ResponseObject(response);
/*     */   }
/*     */   
/*     */   public String getTopmostViaReceiveAddress() {
/*  33 */     Via via = this.sipResponse.getTopmostVia();
/*  34 */     return String.valueOf(via.getReceived()) + ":" + via.getRPort();
/*     */   }
/*     */   public SIPResponse getSipResponse() {
/*  37 */     return this.sipResponse;
/*     */   }
/*     */   
/*     */   public String getFromHeaderUserAtHostPort() {
/*  41 */     return getFrom().getUserAtHostPort();
/*     */   }
/*     */   
/*     */   public String getCallIdString() {
/*  45 */     return this.sipResponse.getCallId().getCallId();
/*     */   }
/*     */   
/*     */   public String getFromHeaderUserName() throws Exception {
/*  49 */     String user = null;
/*  50 */     GenericURI address = (GenericURI)getFrom().getAddress().getURI();
/*     */     
/*  52 */     if (address instanceof SipUri) {
/*     */       
/*  54 */       SipUri uri = (SipUri)address;
/*  55 */       user = uri.getAuthority().getUserInfo().getUser();
/*     */     } 
/*  57 */     if (user != null) {
/*  58 */       return user;
/*     */     }
/*  60 */     throw new Exception("Bad From Header or User name not found");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEventHeader() {
/*  66 */     String event = null;
/*  67 */     if (this.sipResponse.getHeader("Event") != null) {
/*     */ 
/*     */       
/*  70 */       event = this.sipResponse.getHeader("Event").toString();
/*  71 */       if (event.contains(" "))
/*     */       {
/*  73 */         event = event.substring(event.indexOf(" "), event.length());
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  80 */     return event;
/*     */   }
/*     */   
/*     */   public To getTo() {
/*  84 */     return (To)this.sipResponse.getTo();
/*     */   }
/*     */   
/*     */   public From getFrom() {
/*  88 */     return (From)this.sipResponse.getFrom();
/*     */   }
/*     */   
/*     */   public CSeq getCSeq() {
/*  92 */     return (CSeq)this.sipResponse.getCSeq();
/*     */   }
/*     */   
/*     */   public int getStatusCode() {
/*  96 */     return this.sipResponse.getStatusCode();
/*     */   }
/*     */   
/*     */   public int getContactExpires() {
/* 100 */     if (this.sipResponse.getContactHeader() == null) {
/* 101 */       return -1;
/*     */     }
/* 103 */     return this.sipResponse.getContactHeader().getExpires();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getRawContent() {
/* 109 */     return this.sipResponse.getRawContent();
/*     */   }
/*     */   
/*     */   public String getToHeaderUserName() throws Exception {
/* 113 */     String user = null;
/* 114 */     GenericURI address = (GenericURI)getTo().getAddress().getURI();
/*     */     
/* 116 */     if (address instanceof SipUri) {
/*     */       
/* 118 */       SipUri uri = (SipUri)address;
/* 119 */       user = uri.getAuthority().getUserInfo().getUser();
/*     */     } 
/* 121 */     if (user != null) {
/* 122 */       return user;
/*     */     }
/* 124 */     throw new Exception("Bad To Header or User name not found");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getToTag() {
/* 129 */     return this.sipResponse.getToTag();
/*     */   }
/*     */   
/*     */   public SIPETag getSIPETag() {
/* 133 */     if (this.sipResponse.hasHeader("SIP-ETag")) {
/* 134 */       return (SIPETag)this.sipResponse.getHeader("SIP-ETag");
/*     */     }
/* 136 */     return null;
/*     */   }
/*     */   
/*     */   public String getSIPETagString() {
/* 140 */     String et = "";
/* 141 */     if (getSIPETag() != null) {
/* 142 */       et = getSIPETag().getETag();
/*     */     }
/* 144 */     return et;
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\sipMessageHandle\ResponseObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */