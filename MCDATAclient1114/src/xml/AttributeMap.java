/*    */ package xml;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AttributeMap
/*    */ {
/* 14 */   private Map<String, Attribute> attrMap = new HashMap<>();
/*    */ 
/*    */ 
/*    */   
/*    */   public void addAttribute(String uri, String localName, String qName, String type, String value) {
/* 19 */     Attribute attribute = new Attribute();
/* 20 */     attribute.setUri(uri);
/* 21 */     attribute.setLocalName(localName);
/* 22 */     attribute.setqName(qName);
/* 23 */     attribute.setType(type);
/* 24 */     attribute.setValue(value);
/* 25 */     this.attrMap.put(qName, attribute);
/*    */   }
/*    */   
/*    */   public void removeAttribute(String qName) {
/* 29 */     this.attrMap.remove(qName);
/*    */   }
/*    */   
/*    */   public Map<String, Attribute> getAttrMap() {
/* 33 */     return this.attrMap;
/*    */   }
/*    */ 
/*    */   
/*    */   public Attribute getAttribute(String qName) throws NullPointerException {
/* 38 */     if (this.attrMap.containsKey(qName)) {
/* 39 */       return this.attrMap.get(qName);
/*    */     }
/* 41 */     throw new NullPointerException("Attribute " + qName + " not exist");
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<Attribute> getAttributeList() {
/* 46 */     return this.attrMap.values();
/*    */   }
/*    */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\xml\AttributeMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */