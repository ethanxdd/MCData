/*    */ package RTCP;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class message_tools
/*    */ {
/*    */   public void bits_to_bytes(byte[] header, int id, byte b) {
/* 11 */     header[id] = (byte)(header[id] + b);
/* 12 */     header[id] = (byte)(header[id] << 1);
/*    */   }
/*    */   
/*    */   public void length_transf(byte[] header, int len) {
/* 16 */     byte[] bytes = new byte[16]; int i, k;
/* 17 */     for (i = 15, k = 0; i >= 0; i--, k++) {
/* 18 */       if ((len >> i & 0x1) == 1) { bytes[k] = 1; }
/* 19 */       else { bytes[k] = 0; }
/*    */     
/* 21 */     }  for (i = 0; i < 8; i++)
/* 22 */       bits_to_bytes(header, 2, bytes[i]); 
/* 23 */     for (i = 8; i < 16; i++)
/* 24 */       bits_to_bytes(header, 3, bytes[i]); 
/* 25 */     header[2] = (byte)(header[2] >> 1);
/* 26 */     header[3] = (byte)(header[3] >> 1);
/* 27 */     header[2] = (byte)(header[2] & 0x7E);
/* 28 */     header[3] = (byte)(header[3] & 0x7E);
/*    */   }
/*    */   
/*    */   public void set_head2_length_buffer(String id, byte[] msg_field) {
/* 32 */     int length = id.length();
/* 33 */     int buffer = 0;
/* 34 */     buffer = length + 2;
/* 35 */     for (; buffer % 4 != 0; buffer++);
/* 36 */     msg_field = new byte[buffer];
/*    */   }
/*    */ 
/*    */   
/*    */   public void set_head3_length_buffer(String id, byte[] msg_field) {
/* 41 */     int length = id.length();
/* 42 */     int buffer = 0;
/* 43 */     buffer = length + 3;
/* 44 */     for (; buffer % 4 != 0; buffer++);
/* 45 */     msg_field = new byte[buffer];
/*    */   }
/*    */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\RTCP\message_tools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */