/*    */ package xml;
/*    */ 
/*    */ import org.xml.sax.Attributes;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Element
/*    */ {
/*    */   public static final int START = 1001;
/*    */   public static final int END = 1002;
/*    */   public static final int START_WITH_CHARACTOR = 1003;
/*    */   private String uri;
/*    */   private String localName;
/*    */   private String qName;
/*    */   private Attributes atts;
/*    */   private int elementState;
/*    */   private Character character;
/*    */   
/*    */   Element(String uri, String localName, String qName, Attributes atts, int elementState) {
/* 22 */     this.uri = uri;
/* 23 */     this.localName = localName;
/* 24 */     this.qName = qName;
/* 25 */     this.atts = atts;
/* 26 */     this.elementState = elementState;
/*    */   }
/*    */   
/*    */   public String getUri() {
/* 30 */     return this.uri;
/*    */   }
/*    */   
/*    */   public String getLocalName() {
/* 34 */     return this.localName;
/*    */   }
/*    */   
/*    */   public String getqName() {
/* 38 */     return this.qName;
/*    */   }
/*    */   
/*    */   public Attributes getAtts() {
/* 42 */     return this.atts;
/*    */   }
/*    */   
/*    */   public int getElementState() {
/* 46 */     return this.elementState;
/*    */   }
/*    */   
/*    */   public Character getCharacter() {
/* 50 */     return this.character;
/*    */   }
/*    */   
/*    */   public void setCharacter(Character character) {
/* 54 */     this.character = character;
/*    */   }
/*    */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\xml\Element.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */