/*     */ package xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Pidf
/*     */ {
/*     */   public static final String STATUS_OPEN = "open";
/*     */   public static final String STATUS_CLOSED = "closed";
/*     */   public static final String PRESENCE = "presence";
/*     */   public static final String UTF8 = "UTF-8";
/*     */   public static final String TUPLE = "tuple";
/*     */   public static final String STATUS = "status";
/*     */   public static final String BASIC = "basic";
/*     */   public static final String CONTACT = "contact";
/*     */   public static final String TIMESTAMP = "timestamp";
/*     */   public static final String XMLNS = "xmlns";
/*     */   public static final String ENTITY = "entity";
/*     */   public static final String ID = "id";
/*     */   public static final String PRIORITY = "priority";
/*     */   public static final String NOTE = "note";
/*  26 */   private List<String> xmlnsList = new ArrayList<>();
/*     */   private String entityName;
/*     */   private String tupleId;
/*     */   private String status;
/*     */   private String contact;
/*     */   private String contactPriority;
/*     */   private String note;
/*     */   private String timeStamp;
/*     */   
/*     */   public String getEntityName() {
/*  36 */     return this.entityName;
/*     */   }
/*     */   
/*     */   public void setEntityName(String entityName) {
/*  40 */     this.entityName = entityName;
/*     */   }
/*     */   
/*     */   public String getTupleId() {
/*  44 */     return this.tupleId;
/*     */   }
/*     */   
/*     */   public void setTupleId(String tupleId) {
/*  48 */     this.tupleId = tupleId;
/*     */   }
/*     */   
/*     */   public String getStatus() {
/*  52 */     return this.status;
/*     */   }
/*     */   
/*     */   public void setStatus(String status) {
/*  56 */     this.status = status;
/*     */   }
/*     */   
/*     */   public String getContactPriority() {
/*  60 */     return this.contactPriority;
/*     */   }
/*     */   
/*     */   public void setContactPriority(String contactPriority) {
/*  64 */     this.contactPriority = contactPriority;
/*     */   }
/*     */   
/*     */   public String getNote() {
/*  68 */     return this.note;
/*     */   }
/*     */   
/*     */   public void setNote(String note) {
/*  72 */     this.note = note;
/*     */   }
/*     */   
/*     */   public String getTimeStamp() {
/*  76 */     return this.timeStamp;
/*     */   }
/*     */   
/*     */   public void setTimeStamp(String timeStamp) {
/*  80 */     this.timeStamp = timeStamp;
/*     */   }
/*     */   
/*     */   public String getContact() {
/*  84 */     return this.contact;
/*     */   }
/*     */   
/*     */   public void setContact(String contact) {
/*  88 */     this.contact = contact;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  92 */     return "PIDF : " + this.entityName + "\n TupleId : " + this.tupleId + 
/*  93 */       "\n Status : " + this.status + "\n Contact : " + this.contact + 
/*  94 */       " ContactPriority : " + this.contactPriority + "\n Note : " + this.note + 
/*  95 */       "\n TimeStamp : " + this.timeStamp;
/*     */   }
/*     */   
/*     */   public void addXmlns(String xmlns) {
/*  99 */     this.xmlnsList.add(xmlns);
/*     */   }
/*     */   
/*     */   public List<String> getXmlnsList() {
/* 103 */     return this.xmlnsList;
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\xml\Pidf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */