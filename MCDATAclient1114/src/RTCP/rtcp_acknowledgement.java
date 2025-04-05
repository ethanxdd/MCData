/*    */ package RTCP;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class rtcp_acknowledgement
/*    */   extends Rtcp
/*    */ {
/*    */   public static byte[] message;
/*    */   public byte[] Reason_code_field;
/*    */   public int Reason_code;
/*    */   
/*    */   public rtcp_acknowledgement() {}
/*    */   
/*    */   public rtcp_acknowledgement(long SSRC_ID, int reason) {
/* 18 */     message_tools tool = new message_tools();
/*    */     
/* 20 */     this.header = messageheader("acknowledgement", SSRC_ID, "MCPC");
/*    */ 
/*    */     
/* 23 */     this.Reason_code_field = new byte[4];
/* 24 */     this.Reason_code_field = set_Reason_code_field(reason);
/*    */     
/* 26 */     message = new byte[this.header.length + 
/* 27 */         this.Reason_code_field.length];
/*    */     
/* 29 */     tool.length_transf(this.header, message.length);
/*    */     
/* 31 */     writemessage();
/*    */   }
/*    */   
/*    */   public void writemessage() {
/*    */     int i;
/* 36 */     for (i = 0; i < this.header.length; i++)
/* 37 */       message[i] = this.header[i];  int j;
/* 38 */     for (i = this.header.length, j = 0; j < this.Reason_code_field.length; j++) {
/* 39 */       message[i + j] = this.Reason_code_field[j];
/*    */     }
/*    */   }
/*    */   
/*    */   public byte[] set_Reason_code_field(int reason) {
/* 44 */     int buffer = 4;
/* 45 */     byte[] Reason_Code = new byte[buffer];
/* 46 */     Reason_Code[0] = 6;
/* 47 */     Reason_Code[1] = 2;
/* 48 */     Reason_Code[2] = 0;
/* 49 */     Reason_Code[3] = (byte)reason;
/* 50 */     return Reason_Code;
/*    */   }
/*    */   
/*    */   public void get_field(byte[] M) {
/* 54 */     int nexti = 12;
/* 55 */     this.Reason_code_field = new byte[4];
/* 56 */     for (int j = 0; j < 4; j++) {
/* 57 */       this.Reason_code_field[j] = M[nexti + j];
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void get_Reason_code_field(byte[] M) {
/* 63 */     if (M[0] != 6) {
/* 64 */       System.out.println("Reason_code_field type error");
/*    */     }
/*    */     else {
/*    */       
/* 68 */       this.Reason_code = Byte.toUnsignedInt(M[3]);
/* 69 */       System.out.println("reason = " + this.Reason_code);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void parse_allfield(byte[] Data) {
/* 75 */     get_field(Data);
/* 76 */     get_header(Data);
/* 77 */     get_Reason_code_field(this.Reason_code_field);
/*    */   }
/*    */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\RTCP\rtcp_acknowledgement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */