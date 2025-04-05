/*    */ package RTCP;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class rtcp_ack
/*    */   extends Rtcp
/*    */ {
/*    */   public static byte[] message;
/*    */   public byte[] Source_field;
/*    */   public byte[] Message_Type_field;
/*    */   public int Source_id;
/*    */   public int Message_id;
/*    */   
/*    */   public rtcp_ack() {}
/*    */   
/*    */   public rtcp_ack(long SSRC_ID, int source) {
/* 19 */     message_tools tool = new message_tools();
/*    */     
/* 21 */     this.header = messageheader("ack", SSRC_ID, "MCPT");
/*    */ 
/*    */     
/* 24 */     this.Source_field = new byte[4];
/* 25 */     this.Source_field = set_Source_field(source);
/*    */     
/* 27 */     this.Message_Type_field = new byte[4];
/* 28 */     this.Message_Type_field = set_Message_Type_id_field();
/*    */     
/* 30 */     message = new byte[this.header.length + 
/* 31 */         this.Source_field.length + 
/* 32 */         this.Message_Type_field.length];
/*    */     
/* 34 */     tool.length_transf(this.header, message.length);
/*    */     
/* 36 */     writemessage();
/*    */   }
/*    */   
/*    */   public void writemessage() {
/*    */     int i;
/* 41 */     for (i = 0; i < this.header.length; i++)
/* 42 */       message[i] = this.header[i];  int j;
/* 43 */     for (i = this.header.length, j = 0; j < this.Source_field.length; j++)
/* 44 */       message[i + j] = this.Source_field[j]; 
/* 45 */     for (i = this.header.length + this.Source_field.length, j = 0; j < this.Message_Type_field.length; j++) {
/* 46 */       message[i + j] = this.Message_Type_field[j];
/*    */     }
/*    */   }
/*    */   
/*    */   public void get_field(byte[] M) {
/* 51 */     int nexti = 12, i = nexti;
/* 52 */     this.Source_field = new byte[4]; int j;
/* 53 */     for (j = 0; j < 4; i++, j++) {
/* 54 */       this.Source_field[j] = M[nexti + j];
/*    */     }
/* 56 */     nexti = i;
/*    */     
/* 58 */     for (; nexti % 4 != 0; nexti++);
/*    */     
/* 60 */     this.Message_Type_field = new byte[M[nexti + 1] + 2];
/* 61 */     for (j = 0; j < M[nexti + 1] + 2; i++, j++) {
/* 62 */       this.Message_Type_field[j] = M[nexti + j];
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void get_Source_field(byte[] M) {
/* 68 */     if (M[0] != 10) {
/* 69 */       System.out.println("Source_field type error");
/*    */     }
/*    */     else {
/*    */       
/* 73 */       this.Source_id = Byte.toUnsignedInt(M[3]);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void get_Message_Type_field(byte[] M) {
/* 80 */     if (M[0] != 12) {
/* 81 */       System.out.println("Message_Type_field type error");
/*    */     }
/*    */     else {
/*    */       
/* 85 */       this.Message_id = Byte.toUnsignedInt(M[2]);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void parse_allfield(byte[] Data) {
/* 92 */     get_field(Data);
/* 93 */     get_header(Data);
/* 94 */     get_Source_field(this.Source_field);
/* 95 */     get_Message_Type_field(this.Message_Type_field);
/*    */   }
/*    */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\RTCP\rtcp_ack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */