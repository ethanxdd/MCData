/*     */ package xml;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Stack;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SimpleXMLParsingHandler
/*     */   extends DefaultHandler
/*     */ {
/*  18 */   private static String TAG = "XMLParsingHandler";
/*     */   private Stack<String> in_node;
/*     */   private Map<String, Map<String, String>> attsMap;
/*     */   private boolean debugMode = false;
/*     */   
/*     */   public Stack<String> getInNode() {
/*  24 */     return this.in_node;
/*     */   }
/*     */   
/*     */   public Map<String, Map<String, String>> getAttsMap() {
/*  28 */     return this.attsMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract Object getParsedData();
/*     */   
/*     */   public void startDocument() {
/*  35 */     this.in_node = new Stack<>();
/*  36 */     this.attsMap = new HashMap<>();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void endDocument() throws SAXException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/*  46 */     if (isDebugModeOn())
/*     */     {
/*     */       
/*  49 */       for (int i = 0; i < atts.getLength(); i++) {
/*     */ 
/*     */ 
/*     */         
/*  53 */         Map<String, String> map = new HashMap<>();
/*  54 */         map.put(atts.getQName(i), atts.getValue(i));
/*  55 */         this.attsMap.put(qName, map);
/*     */       } 
/*     */     }
/*  58 */     this.in_node.push(qName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
/*  65 */     if (isDebugModeOn())
/*     */     {
/*  67 */       this.in_node.pop();
/*     */     }
/*     */   }
/*     */   
/*     */   public void characters(char[] ch, int start, int length) {
/*  72 */     String fetchStr = (new String(ch)).substring(start, start + length);
/*     */     
/*  74 */     isDebugModeOn();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     characters(fetchStr);
/*     */   }
/*     */ 
/*     */   
/*     */   public void characters(String fetchStr) {}
/*     */ 
/*     */   
/*     */   public String printNodePosition() {
/*  87 */     StringBuffer sb = new StringBuffer();
/*  88 */     for (int i = 0; i < this.in_node.size(); i++) {
/*  89 */       if (i > 0) {
/*  90 */         sb.append(" --> " + (String)this.in_node.get(i));
/*     */       }
/*     */     } 
/*     */     
/*  94 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String findAttr(Attributes atts, String findStr) {
/*     */     int i;
/* 100 */     for (i = 0; i < atts.getLength(); i++) {
/*     */       
/* 102 */       if (atts.getQName(i).compareToIgnoreCase(findStr) == 0) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/* 107 */     return atts.getValue(i);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDebugMode(boolean flag) {
/* 112 */     this.debugMode = flag;
/*     */   }
/*     */   
/*     */   private boolean isDebugModeOn() {
/* 116 */     return this.debugMode;
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\xml\SimpleXMLParsingHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */