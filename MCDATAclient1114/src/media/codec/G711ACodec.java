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
/*    */ public class G711ACodec
/*    */   implements Encoder, Decoder
/*    */ {
/* 16 */   private static byte[] table12to8 = new byte[4096];
/* 17 */   private static short[] table8to16 = new short[256];
/*    */ 
/*    */   
/*    */   static {
/* 21 */     for (int i = 0; i < 16; i++) {
/* 22 */       int v = i ^ 0x55;
/* 23 */       table12to8[i] = (byte)v;
/* 24 */       table12to8[4095 - i] = (byte)(v + 128);
/*    */     } 
/* 26 */     for (int p = 1, j = 16; p <= 64; p <<= 1, j += 16) {
/* 27 */       for (int k = 0, n = p << 4; k < 16; k++, n += p) {
/* 28 */         int v = k + j ^ 0x55;
/* 29 */         byte value1 = (byte)v;
/* 30 */         byte value2 = (byte)(v + 128);
/* 31 */         for (int i1 = n, e = n + p; i1 < e; i1++) {
/* 32 */           table12to8[i1] = value1;
/* 33 */           table12to8[4095 - i1] = value2;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 39 */     for (int m = 0; m < 16; m++) {
/* 40 */       int v = m << 4;
/* 41 */       table8to16[m ^ 0x55] = (short)v;
/* 42 */       table8to16[m + 128 ^ 0x55] = (short)(65536 - v);
/*    */     } 
/* 44 */     for (int q = 1; q <= 7; q++) {
/* 45 */       for (int k = 0, n = q << 4; k < 16; k++, n++) {
/* 46 */         int v = k + 16 << q + 3;
/* 47 */         table8to16[n ^ 0x55] = (short)v;
/* 48 */         table8to16[n + 128 ^ 0x55] = (short)(65536 - v);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public int decode(short[] b16, byte[] b8, int count, int offset) {
/* 54 */     for (int i = 0, j = offset; i < count; i++, j++) {
/* 55 */       b16[i] = table8to16[b8[j] & 0xFF];
/*    */     }
/* 57 */     return count;
/*    */   }
/*    */ 
/*    */   
/*    */   public int encode(short[] b16, int count, byte[] b8, int offset) {
/* 62 */     for (int i = 0, j = offset; i < count; i++, j++) {
/* 63 */       b8[j] = table12to8[(b16[i] & 0xFFFF) >> 4];
/*    */     }
/* 65 */     return count;
/*    */   }
/*    */   
/*    */   public int getSampleCount(int frameSize) {
/* 69 */     return frameSize;
/*    */   }
/*    */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\codec\G711ACodec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */