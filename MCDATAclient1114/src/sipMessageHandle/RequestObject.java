/*     */ package sipMessageHandle;
/*     */ 
/*     */ import gov.nist.javax.sip.address.GenericURI;
/*     */ import gov.nist.javax.sip.address.SipUri;
/*     */ import gov.nist.javax.sip.header.CSeq;
/*     */ import gov.nist.javax.sip.header.CallID;
/*     */ import gov.nist.javax.sip.header.Contact;
/*     */ import gov.nist.javax.sip.header.ContentType;
/*     */ import gov.nist.javax.sip.header.From;
/*     */ import gov.nist.javax.sip.header.RequestLine;
/*     */ import gov.nist.javax.sip.header.SubscriptionState;
/*     */ import gov.nist.javax.sip.header.To;
/*     */ import gov.nist.javax.sip.message.Content;
/*     */ import gov.nist.javax.sip.message.ContentImpl;
/*     */ import gov.nist.javax.sip.message.MultipartMimeContentImpl;
/*     */ import gov.nist.javax.sip.message.SIPRequest;
/*     */ import java.text.ParseException;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.sip.PeerUnavailableException;
/*     */ import javax.sip.SipFactory;
/*     */ import javax.sip.address.URI;
/*     */ import javax.sip.header.HeaderFactory;
/*     */ import javax.sip.message.Request;
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
/*     */ public class RequestObject
/*     */ {
/*     */   private static final String TAG = "RequestObject";
/*     */   SIPRequest sipRequest;
/*     */   SipFactory sipFactory;
/*     */   HeaderFactory headerFactory;
/*     */   private SIPRequest request;
/*     */   private URI requestUri;
/*     */   private CallID callId;
/*     */   private Contact contact;
/*     */   private CSeq cSeq;
/*     */   private String event;
/*     */   private String contentText;
/*     */   private String contentSubType;
/*     */   private String contentType;
/*     */   private String sdpText;
/*     */   private String resourceListText;
/*     */   private String groupXml;
/*     */   private static String mcptt_info;
/*     */   private static String pidf;
/*     */   private Integer sdpRtpPort;
/*     */   private Integer sdpRtcpPort;
/*     */   private String sdpRtpIp;
/*     */   private String acceptContact;
/*     */   private String requestUriString;
/*     */   private List<Integer> sdpRtpMediaFormats;
/*     */   private boolean isInvitationForVideo;
/*     */   
/*     */   private RequestObject(Request request) throws PeerUnavailableException, ParseException {
/*  67 */     this.sipRequest = (SIPRequest)request;
/*  68 */     this.sipFactory = SipFactory.getInstance();
/*  69 */     this.headerFactory = this.sipFactory.createHeaderFactory();
/*  70 */     this.sipRequest.checkHeaders();
/*     */   }
/*     */   
/*     */   public static RequestObject parse(Request request) throws PeerUnavailableException, ParseException {
/*  74 */     return new RequestObject(request);
/*     */   }
/*     */ 
/*     */   
/*     */   public SIPRequest getSipRequest() {
/*  79 */     return this.sipRequest;
/*     */   }
/*     */   
/*     */   public String getMethod() {
/*  83 */     return this.sipRequest.getMethod();
/*     */   }
/*     */   
/*     */   public String getCallIdString() {
/*  87 */     return this.sipRequest.getCallId().getCallId();
/*     */   }
/*     */ 
/*     */   
/*     */   public From getFrom() {
/*  92 */     return (From)this.sipRequest.getFrom();
/*     */   }
/*     */   
/*     */   public String getFromHeaderUserName() throws Exception {
/*  96 */     String user = null;
/*  97 */     GenericURI address = (GenericURI)getFrom().getAddress().getURI();
/*     */ 
/*     */     
/* 100 */     if (address instanceof SipUri) {
/*     */       
/* 102 */       SipUri uri = (SipUri)address;
/* 103 */       user = uri.getUser();
/*     */     } 
/*     */     
/* 106 */     if (user != null) {
/* 107 */       return user;
/*     */     }
/* 109 */     throw new Exception("Bad From Header or User name not found");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFromTag() {
/* 115 */     return getFrom().getTag();
/*     */   }
/*     */   
/*     */   public String getFromDisplayName() {
/* 119 */     return getFrom().getDisplayName();
/*     */   }
/*     */   
/*     */   public To getTo() {
/* 123 */     return (To)this.sipRequest.getTo();
/*     */   }
/*     */   
/*     */   public String getToTag() {
/* 127 */     return getTo().getTag();
/*     */   }
/*     */   
/*     */   public String getToDisplayName() {
/* 131 */     return getTo().getDisplayName();
/*     */   }
/*     */   
/*     */   public CSeq getCSeqHeader() {
/* 135 */     return (CSeq)this.sipRequest.getCSeq();
/*     */   }
/*     */   
/*     */   public long getCSeqNumber() {
/* 139 */     return getCSeqHeader().getSeqNumber();
/*     */   }
/*     */   
/*     */   public RequestLine getRequestLine() {
/* 143 */     return this.sipRequest.getRequestLine();
/*     */   }
/*     */   
/*     */   public GenericURI getRequestURI() {
/* 147 */     return getRequestLine().getUri();
/*     */   }
/*     */   
/*     */   public Contact getContact() {
/* 151 */     return this.sipRequest.getContactHeader();
/*     */   }
/*     */   
/*     */   public SubscriptionState getSubscriptionState() throws Exception {
/* 155 */     if (this.sipRequest.hasHeader("Subscription-State")) {
/* 156 */       return (SubscriptionState)this.sipRequest.getHeader("Subscription-State");
/*     */     }
/* 158 */     throw new Exception("Subscription header not found");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContentString() throws ParseException {
/* 164 */     if (this.sipRequest.getRawContent() != null)
/*     */     {
/* 166 */       return new String(this.sipRequest.getRawContent());
/*     */     }
/* 168 */     return null;
/*     */   }
/*     */   
/*     */   public void onMutiPartContent(SIPRequest sipRequest) throws ParseException {
/* 172 */     MultipartMimeContentImpl multipartMimeContentImpl = (MultipartMimeContentImpl)sipRequest.getMultipartMimeContent();
/* 173 */     Iterator<Content> contants = multipartMimeContentImpl.getContents();
/* 174 */     while (contants.hasNext()) {
/* 175 */       ContentImpl partContent = (ContentImpl)contants.next();
/* 176 */       onPartContent((Content)partContent);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String onPartContent(Content content) {
/* 181 */     ContentType contentType = (ContentType)content.getContentTypeHeader();
/* 182 */     if (contentType != null) {
/* 183 */       String subType = contentType.getContentSubType();
/* 184 */       if (subType.equals("sdp")) {
/* 185 */         this.sdpText = content.getContent().toString();
/* 186 */       } else if (subType.equals("resource-list+xml")) {
/* 187 */         this.resourceListText = content.getContent().toString();
/* 188 */       } else if (subType.equals("group+xml")) {
/* 189 */         this.groupXml = content.getContent().toString();
/* 190 */       } else if (subType.equals("vnd.3gpp.mcptt-info+xml")) {
/* 191 */         mcptt_info = content.getContent().toString();
/* 192 */       } else if (subType.equals("pidf+xml")) {
/* 193 */         pidf = content.getContent().toString();
/*     */       } 
/*     */     } 
/* 196 */     return null;
/*     */   }
/*     */   
/*     */   public String getGroupXml() {
/* 200 */     if (this.sipRequest.getContentTypeHeader() == null) {
/* 201 */       return null;
/*     */     }
/*     */     try {
/* 204 */       if (this.sipRequest.getContentTypeHeader().getContentType().contains("multipart")) {
/* 205 */         Iterator<Content> it = this.sipRequest.getMultipartMimeContent().getContents();
/* 206 */         while (it.hasNext()) {
/* 207 */           Content content = it.next();
/* 208 */           if ((content.getContentTypeHeader() == null || content.getContentTypeHeader().getContentSubType() == null) && 
/* 209 */             !content.getContent().toString().contains("group+xml")) {
/*     */             continue;
/*     */           }
/* 212 */           if (content.getContent().toString().contains("group+xml") || 
/* 213 */             content.getContentTypeHeader().getContentSubType().contains("group+xml")) {
/* 214 */             return content.getContent().toString();
/*     */           }
/*     */         } 
/* 217 */       } else if (this.sipRequest.getContentTypeHeader().getContentSubType().contains("group+xml") || 
/* 218 */         this.sipRequest.getContent().toString().contains("group+xml")) {
/* 219 */         return this.sipRequest.getContent().toString();
/*     */       }
/*     */     
/* 222 */     } catch (ParseException e) {
/* 223 */       e.printStackTrace();
/*     */     } 
/* 225 */     return null;
/*     */   }
/*     */   
/*     */   public String getSdp() {
/*     */     try {
/* 230 */       if (this.sipRequest.getContentTypeHeader() == null)
/*     */       {
/* 232 */         return null;
/*     */       }
/*     */       
/* 235 */       if (this.sipRequest.getContentTypeHeader().getContentType().contains("multipart")) {
/* 236 */         Iterator<Content> it = this.sipRequest.getMultipartMimeContent().getContents();
/* 237 */         while (it.hasNext()) {
/* 238 */           Content content = it.next();
/* 239 */           if ((content.getContentTypeHeader() == null || content.getContentTypeHeader().getContentSubType() == null) && 
/* 240 */             !content.getContent().toString().contains("sdp")) {
/*     */             continue;
/*     */           }
/* 243 */           if (content.getContent().toString().contains("sdp") || 
/* 244 */             content.getContentTypeHeader().getContentSubType().contains("sdp")) {
/* 245 */             return content.getContent().toString();
/*     */           }
/*     */         } 
/* 248 */       } else if (this.sipRequest.getContentTypeHeader().getContentType().contains("sdp") || 
/* 249 */         this.sipRequest.getContentTypeHeader().getContentSubType().contains("sdp")) {
/* 250 */         return new String(this.sipRequest.getRawContent());
/*     */       } 
/* 252 */     } catch (ParseException e) {
/* 253 */       e.printStackTrace();
/*     */     } 
/* 255 */     return null;
/*     */   }
/*     */   
/*     */   public static String get_mcptt_info() {
/* 259 */     return mcptt_info;
/*     */   }
/*     */   
/*     */   public static String get_pidf() {
/* 263 */     return pidf;
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\sipMessageHandle\RequestObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */