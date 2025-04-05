/*    */ package media.codec;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class G711UCodec
/*    */   implements Encoder, Decoder
/*    */ {
/* 16 */   private static byte[] table13to8 = new byte[8192];
/* 17 */   private static short[] table8to16 = new short[256];
/*    */ 
/*    */   
/*    */   static {
/* 21 */     for (int p = 1, i = 0; p <= 128; p <<= 1, i += 16) {
/* 22 */       for (int k = 0, j = (p << 4) - 16; k < 16; k++, j += p) {
/* 23 */         int v = k + i ^ 0x7F;
/* 24 */         byte value1 = (byte)v;
/* 25 */         byte value2 = (byte)(v + 128);
/* 26 */         for (int m = j, e = j + p; m < e; m++) {
/* 27 */           table13to8[m] = value1;
/* 28 */           table13to8[8191 - m] = value2;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 34 */     for (int q = 0; q <= 7; q++) {
/* 35 */       for (int j = 0, m = q << 4; j < 16; j++, m++) {
/* 36 */         int v = (j + 16 << q) - 16 << 3;
/* 37 */         table8to16[m ^ 0x7F] = (short)v;
/* 38 */         table8to16[(m ^ 0x7F) + 128] = (short)(65536 - v);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public int decode(short[] b16, byte[] b8, int count, int offset) {
/* 44 */     for (int i = 0, j = offset; i < count; i++, j++) {
/* 45 */       b16[i] = table8to16[b8[j] & 0xFF];
/*    */     }
/* 47 */     return count;
/*    */   }
/*    */ 
/*    */   
/*    */   public int encode(short[] b16, int count, byte[] b8, int offset) {
/* 52 */     for (int i = 0, j = offset; i < count; i++, j++) {
/* 53 */       b8[j] = table13to8[b16[i] >> 4 & 0x1FFF];
/*    */     }
/* 55 */     return count;
/*    */   }
/*    */   
/*    */   public int getSampleCount(int frameSize) {
/* 59 */     return frameSize;
/*    */   }
/*    */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\codec\G711UCodec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */