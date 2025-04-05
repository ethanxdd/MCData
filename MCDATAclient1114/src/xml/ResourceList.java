/*    */ package xml;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ResourceList
/*    */ {
/*    */   public static final String RESOURCE_LISTS = "resource-lists";
/*    */   public static final String LIST = "list";
/*    */   public static final String ENTRY = "entry";
/*    */   public static final String UTF8 = "UTF-8";
/*    */   public static final String URI = "uri";
/*    */   public static final String COPY_CONTROL = "cp:copyControl";
/*    */   public static final String XMLNS = "xmlns";
/*    */   public static final String XMLNS_CP = "xmlns:cp";
/* 25 */   private List<String> xmlnsList = new ArrayList<>();
/* 26 */   private List<String> xmlnsCpList = new ArrayList<>();
/* 27 */   private List<String> entryList = new ArrayList<>();
/* 28 */   private Map<String, String> cpMap = new HashMap<>();
/*    */ 
/*    */   
/*    */   public List<String> getXmlnsCpList() {
/* 32 */     return this.xmlnsCpList;
/*    */   }
/*    */   public List<String> getXmlnsList() {
/* 35 */     return this.xmlnsList;
/*    */   }
/*    */   
/*    */   public List<String> getEntryList() {
/* 39 */     return this.entryList;
/*    */   }
/*    */   
/*    */   public void addXmlns(String xmlns) {
/* 43 */     this.xmlnsList.add(xmlns);
/*    */   }
/*    */   
/*    */   public void addXmlnsCpList(String xmlnsCp) {
/* 47 */     this.xmlnsCpList.add(xmlnsCp);
/*    */   }
/*    */   
/*    */   public void addEntry(String entry, String cp) {
/* 51 */     this.entryList.add(entry);
/* 52 */     this.cpMap.put(entry, cp);
/*    */   }
/*    */   
/*    */   public Map<String, String> getCpMap() {
/* 56 */     return this.cpMap;
/*    */   }
/*    */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\xml\ResourceList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */