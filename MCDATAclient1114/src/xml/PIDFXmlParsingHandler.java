/*    */ package xml;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Stack;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PIDFXmlParsingHandler
/*    */   extends SimpleXMLParsingHandler
/*    */ {
/* 15 */   private static String TAG = "PIDFXmlParsingHandler";
/*    */ 
/*    */   
/*    */   private Pidf pidfItem;
/*    */ 
/*    */   
/*    */   private Stack<Pidf> pidfItemList;
/*    */ 
/*    */   
/*    */   public Object getParsedData() {
/* 25 */     Pidf[] pidfArray = (Pidf[])this.pidfItemList.toArray((Object[])new Pidf[this.pidfItemList.size()]);
/*    */     
/* 27 */     return new Object[] { pidfArray };
/*    */   }
/*    */ 
/*    */   
/*    */   public void startDocument() {
/* 32 */     super.startDocument();
/* 33 */     this.pidfItemList = new Stack<>();
/*    */   }
/*    */ 
/*    */   
/*    */   public void endDocument() throws SAXException {
/* 38 */     if (this.pidfItemList.size() == 0) {
/* 39 */       throw new SAXException("Bad presence xml format !");
/*    */     }
/* 41 */     super.endDocument();
/*    */   }
/*    */ 
/*    */   
/*    */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/* 46 */     if (getInNode().size() >= 1 && ((String)getInNode().get(getInNode().size() - 1)).equals("presence")) {
/* 47 */       this.pidfItem = new Pidf();
/*    */     }
/* 49 */     super.startElement(namespaceURI, localName, qName, atts);
/*    */   }
/*    */ 
/*    */   
/*    */   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
/* 54 */     if (getInNode().size() >= 1 && ((String)getInNode().get(getInNode().size() - 1)).equals("presence")) {
/* 55 */       this.pidfItemList.add(this.pidfItem);
/* 56 */       this.pidfItem = null;
/*    */     } 
/* 58 */     super.endElement(namespaceURI, localName, qName);
/*    */   }
/*    */ 
/*    */   
/*    */   public void characters(String fetchStr) {
/* 63 */     if (getInNode().size() >= 1) {
/*    */       
/* 65 */       if (this.pidfItem == null) {
/*    */         return;
/*    */       }
/*    */       
/* 69 */       if (((String)getInNode().lastElement()).equals("tuple")) {
/* 70 */         if (getAttsMap().get("tuple") != null) {
/* 71 */           this.pidfItem.setTupleId((String)((Map)getAttsMap().get("tuple")).get("id"));
/*    */         }
/* 73 */       } else if (((String)getInNode().lastElement()).equals("timestamp")) {
/* 74 */         this.pidfItem.setTimeStamp(fetchStr);
/* 75 */       } else if (((String)getInNode().lastElement()).equals("basic")) {
/* 76 */         this.pidfItem.setStatus(fetchStr);
/* 77 */       } else if (((String)getInNode().lastElement()).equals("presence")) {
/* 78 */         if (((Map)getAttsMap().get("presence")).get("entity") != null) {
/* 79 */           this.pidfItem.setEntityName((String)((Map)getAttsMap().get("presence")).get("entity"));
/*    */         }
/* 81 */       } else if (((String)getInNode().lastElement()).equals("note")) {
/* 82 */         this.pidfItem.setNote(fetchStr);
/* 83 */       } else if (((String)getInNode().lastElement()).equals("contact")) {
/* 84 */         this.pidfItem.setContact(fetchStr);
/* 85 */         if (getAttsMap().get("contact") != null)
/* 86 */           this.pidfItem.setContactPriority((String)((Map)getAttsMap().get("contact")).get("priority")); 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\xml\PIDFXmlParsingHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */