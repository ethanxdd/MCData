/*    */ package xml;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Character
/*    */ {
/*    */   private char[] ch;
/*    */   private int start;
/*    */   private int length;
/*    */   
/*    */   public Character(char[] ch, int start, int length) {
/* 12 */     this.ch = ch;
/* 13 */     this.start = start;
/* 14 */     this.length = length;
/*    */   }
/*    */   
/*    */   public char[] getCh() {
/* 18 */     return this.ch;
/*    */   }
/*    */   
/*    */   public int getStart() {
/* 22 */     return this.start;
/*    */   }
/*    */   
/*    */   public int getLength() {
/* 26 */     return this.length;
/*    */   }
/*    */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\xml\Character.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */