/*    */ package RTCP;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class rtcp_disconnect
/*    */   extends Rtcp
/*    */ {
/*    */   public byte[] message;
/*    */   public byte[] Session_id_field;
/*    */   public int Session_type;
/*    */   public String Session_id;
/*    */   
/*    */   public rtcp_disconnect() {}
/*    */   
/*    */   public rtcp_disconnect(long SSRC_ID, String session_identity) {
/* 20 */     message_tools tool = new message_tools();
/*    */ 
/*    */     
/* 23 */     this.header = messageheader("disconnect", SSRC_ID, "MCPC");
/*    */ 
/*    */     
/* 26 */     tool.set_head3_length_buffer(session_identity, this.Session_id_field);
/* 27 */     this.Session_id_field = set_Session_id_field(session_identity);
/*    */     
/* 29 */     this.message = 
/* 30 */       new byte[this.header.length + this.Session_id_field.length];
/*    */     
/* 32 */     tool.length_transf(this.header, this.message.length);
/*    */     
/* 34 */     writemessage();
/*    */   }
/*    */   
/*    */   public void writemessage() {
/*    */     int i;
/* 39 */     for (i = 0; i < this.header.length; i++)
/* 40 */       this.message[i] = this.header[i];  int j;
/* 41 */     for (i = this.header.length, j = 0; j < this.Session_id_field.length; j++)
/* 42 */       this.message[i + j] = this.Session_id_field[j]; 
/*    */   }
/*    */   
/*    */   public void get_field(byte[] M) {
/* 46 */     int nexti = 12;
/* 47 */     this.Session_id_field = new byte[M[nexti + 1] + 3];
/* 48 */     for (int j = 0; j < M[nexti + 1] + 3; j++) {
/* 49 */       this.Session_id_field[j] = M[nexti + j];
/*    */     }
/*    */   }
/*    */   
/*    */   public void get_Session_id_field(byte[] M) {
/* 54 */     if (M[0] != 1) {
/* 55 */       System.out.println("Session_id_field type error");
/*    */     } else {
/*    */       
/* 58 */       byte[] Tmp = new byte[M[1]];
/* 59 */       int nexti = 3;
/* 60 */       for (int i = 0; i < M[1]; i++) {
/* 61 */         Tmp[i] = M[nexti + i];
/*    */       }
/*    */       
/* 64 */       this.Session_type = Byte.toUnsignedInt(M[2]);
/* 65 */       this.Session_id = new String(Tmp);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void parse_all_field(byte[] data) {
/* 71 */     get_field(data);
/* 72 */     get_Session_id_field(this.Session_id_field);
/*    */   }
/*    */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\RTCP\rtcp_disconnect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */