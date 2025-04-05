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
/*    */ public class ResourceListXmlBuilder
/*    */ {
/*    */   private ResourceList resourceList;
/*    */   private XMLBuilder xmlBuilder;
/*    */   private String encode;
/*    */   
/*    */   public ResourceListXmlBuilder(String encode, ResourceList resourceList) {
/* 19 */     this.encode = encode;
/* 20 */     this.xmlBuilder = new XMLBuilder();
/* 21 */     this.resourceList = resourceList;
/*    */   }
/*    */   
/*    */   public ByteArrayOutputStream build() throws SAXException {
/* 25 */     this.xmlBuilder.setOutputProperty("encoding", this.encode);
/* 26 */     AttributeMap resourceListAttributeMap = new AttributeMap();
/*    */     
/* 28 */     for (String str : this.resourceList.getXmlnsCpList()) {
/* 29 */       resourceListAttributeMap.addAttribute("", "", "xmlns:cp", "String", str);
/*    */     }
/*    */     
/* 32 */     for (String str : this.resourceList.getXmlnsList()) {
/* 33 */       resourceListAttributeMap.addAttribute("", "", "xmlns", "String", str);
/*    */     }
/*    */     
/* 36 */     this.xmlBuilder.startElement("", "", "resource-lists", resourceListAttributeMap);
/* 37 */     this.xmlBuilder.startElement("", "", "list");
/*    */ 
/*    */     
/* 40 */     for (String uri : this.resourceList.getEntryList()) {
/* 41 */       AttributeMap entryAttr = new AttributeMap();
/* 42 */       entryAttr.addAttribute("", "", "uri", "String", uri);
/* 43 */       entryAttr.addAttribute("", "", "cp:copyControl", "String", this.resourceList.getCpMap().get(uri));
/* 44 */       this.xmlBuilder.startElement("", "", "entry", entryAttr);
/* 45 */       this.xmlBuilder.endElement("", "", "entry");
/*    */     } 
/*    */     
/* 48 */     this.xmlBuilder.endElement("", "", "list");
/* 49 */     this.xmlBuilder.endElement("", "", "resource-lists");
/*    */ 
/*    */     
/* 52 */     return this.xmlBuilder.build();
/*    */   }
/*    */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\xml\ResourceListXmlBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */