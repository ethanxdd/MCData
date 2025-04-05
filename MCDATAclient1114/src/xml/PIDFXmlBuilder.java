/*    */ package xml;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PIDFXmlBuilder
/*    */ {
/*    */   private Pidf pidf;
/*    */   private XMLBuilder xmlBuilder;
/*    */   private String encoding;
/*    */   
/*    */   public PIDFXmlBuilder(Pidf pidf, String encoding) {
/* 19 */     this.pidf = pidf;
/* 20 */     this.xmlBuilder = new XMLBuilder();
/* 21 */     this.encoding = encoding;
/*    */   }
/*    */ 
/*    */   
/*    */   public ByteArrayOutputStream build() throws SAXException {
/* 26 */     this.xmlBuilder.setOutputProperty("encoding", this.encoding);
/* 27 */     this.xmlBuilder.setOutputProperty("indent", "yes");
/* 28 */     AttributeMap presenceAttributeMap = new AttributeMap();
///* 29 */     for (String str : this.pidf.getXmlnsList()) {
///* 30 */       presenceAttributeMap.addAttribute("", "", "xmlns", "String", str);
///*    */     }
/* 32 */     presenceAttributeMap.addAttribute("", "", "entity", "String", this.pidf.getEntityName());
/* 33 */     this.xmlBuilder.startElement("", "", "presence", presenceAttributeMap);//1001
/* 34 */     AttributeMap tupleAttributeMap = new AttributeMap();
/* 35 */     tupleAttributeMap.addAttribute("", "", "id", "String", this.pidf.getTupleId());
/* 36 */     this.xmlBuilder.startElement("", "", "tuple", tupleAttributeMap);
/* 37 */     this.xmlBuilder.startElement("", "", "status");
/* 38 */     Character characterBasic = new Character(this.pidf.getStatus().toCharArray(), 0, (this.pidf.getStatus().toCharArray()).length);
/* 39 */     this.xmlBuilder.startElement("", "", "basic", characterBasic);
/* 40 */     this.xmlBuilder.endElement("", "", "basic");

/* 41 */     this.xmlBuilder.endElement("", "", "status");

/* 42 */     AttributeMap contactAttributeMap = new AttributeMap();
/* 43 */     contactAttributeMap.addAttribute("", "", "priority", "String", this.pidf.getContactPriority());
/* 44 */     Character characterContact = new Character(this.pidf.getContact().toCharArray(), 0, (this.pidf.getContact().toCharArray()).length);
/* 45 */     this.xmlBuilder.startElement("", "", "contact", contactAttributeMap, characterContact);
/* 46 */     this.xmlBuilder.endElement("", "", "contact");

/* 47 */     Character characterTime = new Character(this.pidf.getTimeStamp().toCharArray(), 0, (this.pidf.getTimeStamp().toCharArray()).length);
/* 48 */     this.xmlBuilder.startElement("", "", "timestamp", characterTime);
/* 49 */     this.xmlBuilder.endElement("", "", "timestamp");
/* 50 */     this.xmlBuilder.endElement("", "", "tuple");
/* 51 */     this.xmlBuilder.endElement("", "", "presence");
/* 52 */     return this.xmlBuilder.build();
/*    */   }
/*    */   
/*    */   public void setDebugMode(boolean debugMode) {
/* 56 */     this.xmlBuilder.setDebugMode(debugMode);
/*    */   }
/*    */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\xml\PIDFXmlBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */