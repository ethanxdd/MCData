/*    */ package RTCP;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class rtcp_queue_position_request
/*    */   extends Rtcp
/*    */ {
/*    */   public byte[] message;
/*    */   public byte[] User_ID_field;
/*    */   public String User_id;
/*    */   
/*    */   public rtcp_queue_position_request() {}
/*    */   
/*    */   public rtcp_queue_position_request(long SSRC_ID, String user_id) {
/* 19 */     message_tools tool = new message_tools();
/*    */     
/* 21 */     this.header = messageheader("queue_position_request", SSRC_ID, "MCPT");
/*    */ 
/*    */     
/* 24 */     tool.set_head2_length_buffer(user_id, this.User_ID_field);
/* 25 */     this.User_ID_field = set_User_ID_field(user_id);
/*    */     
/* 27 */     this.message = 
/* 28 */       new byte[this.header.length + this.User_ID_field.length];
/*    */     
/* 30 */     tool.length_transf(this.header, this.message.length);
/*    */     
/* 32 */     writemessage();
/*    */   }
/*    */   public void writemessage() {
/*    */     int i;
/* 36 */     for (i = 0; i < this.header.length; i++)
/* 37 */       this.message[i] = this.header[i];  int j;
/* 38 */     for (i = this.header.length, j = 0; j < this.User_ID_field.length; j++) {
/* 39 */       this.message[i + j] = this.User_ID_field[j];
/*    */     }
/*    */   }
/*    */   
/*    */   public byte[] set_User_ID_field(String id) {
/* 44 */     int buffer = id.length() + 2;
/* 45 */     for (; buffer % 4 != 0; buffer++);
/* 46 */     byte[] user = new byte[buffer];
/* 47 */     byte[] byteArr = id.getBytes();
/* 48 */     user[0] = 6;
/* 49 */     user[1] = (byte)id.length();
/* 50 */     for (int i = 0; i < id.length(); i++) {
/* 51 */       user[i + 2] = byteArr[i];
/*    */     }
/* 53 */     return user;
/*    */   }
/*    */   
/*    */   public void get_field(byte[] M) {
/* 57 */     int nexti = 12;
/* 58 */     this.User_ID_field = new byte[M[nexti + 1] + 2];
/* 59 */     for (int j = 0; j < M[nexti + 1] + 2; j++) {
/* 60 */       this.User_ID_field[j] = M[nexti + j];
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void get_User_ID_field(byte[] M) {
/* 68 */     if (M[0] != 6) {
/* 69 */       System.out.println("User_id_field type error");
/*    */     } else {
/*    */       
/* 72 */       byte[] Tmp = new byte[M[1]];
/* 73 */       int nexti = 2;
/* 74 */       for (int i = 0; i < M[1]; i++) {
/* 75 */         Tmp[i] = M[nexti + i];
/*    */       }
/* 77 */       this.User_id = new String(Tmp);
/* 78 */       System.out.println("user uri = " + this.User_id);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void parse_allfield(byte[] Data) {
/* 83 */     get_field(Data);
/* 84 */     get_header(Data);
/* 85 */     get_User_ID_field(this.User_ID_field);
/*    */   }
/*    */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\RTCP\rtcp_queue_position_request.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */