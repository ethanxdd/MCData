/*     */ package media.rtplib;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StaticProcs
/*     */ {
/*     */   public static byte[] uIntIntToByteWord(int i) {
/*  36 */     byte[] byteWord = new byte[2];
/*  37 */     byteWord[0] = (byte)(i >> 8 & 0xFF);
/*  38 */     byteWord[1] = (byte)(i & 0xFF);
/*  39 */     return byteWord;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] uIntLongToByteWord(long j) {
/*  49 */     int i = (int)j;
/*  50 */     byte[] byteWord = new byte[4];
/*  51 */     byteWord[0] = (byte)(i >>> 24 & 0xFF);
/*  52 */     byteWord[1] = (byte)(i >> 16 & 0xFF);
/*  53 */     byteWord[2] = (byte)(i >> 8 & 0xFF);
/*  54 */     byteWord[3] = (byte)(i & 0xFF);
/*  55 */     return byteWord;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int bytesToUIntInt(byte[] bytes, int index) {
/*  65 */     int accum = 0;
/*  66 */     int i = 1;
/*  67 */     for (int shiftBy = 0; shiftBy < 16; shiftBy += 8) {
/*  68 */       accum = (int)(accum | (bytes[index + i] & 0xFF) << shiftBy);
/*  69 */       i--;
/*     */     } 
/*  71 */     return accum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long bytesToUIntLong(byte[] bytes, int index) {
/*  82 */     long accum = 0L;
/*  83 */     int i = 3;
/*  84 */     for (int shiftBy = 0; shiftBy < 32; shiftBy += 8) {
/*  85 */       accum |= (bytes[index + i] & 0xFF) << shiftBy;
/*  86 */       i--;
/*     */     } 
/*  88 */     return accum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long undoNtpMess(long ntpTs1, long ntpTs2) {
/* 140 */     long timeVal = (ntpTs1 - 2208988800L) * 1000L;
/*     */     
/* 142 */     double tmp = 1000.0D * ntpTs2 / 4.294967295E9D;
/* 143 */     long ms = (long)tmp;
/*     */     
/* 145 */     timeVal += ms;
/*     */     
/* 147 */     return timeVal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String bitsOfByte(byte aByte) {
/* 160 */     String out = "";
/* 161 */     for (int i = 7; i >= 0; i--) {
/* 162 */       int temp = aByte >>> i;
/* 163 */       temp &= 0x1;
/* 164 */       out = String.valueOf(out) + temp;
/*     */     } 
/* 166 */     return out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String hexOfByte(byte aByte) {
/* 176 */     String out = "";
/*     */     
/* 178 */     for (int i = 0; i < 2; i++) {
/* 179 */       int temp = aByte;
/* 180 */       if (temp < 0) {
/* 181 */         temp += 256;
/*     */       }
/* 183 */       if (i == 0) {
/* 184 */         temp /= 16;
/*     */       } else {
/* 186 */         temp %= 16;
/*     */       } 
/*     */       
/* 189 */       if (temp > 9) {
/* 190 */         switch (temp) { case 10:
/* 191 */             out = String.valueOf(out) + "A"; break;
/* 192 */           case 11: out = String.valueOf(out) + "B"; break;
/* 193 */           case 12: out = String.valueOf(out) + "C"; break;
/* 194 */           case 13: out = String.valueOf(out) + "D"; break;
/* 195 */           case 14: out = String.valueOf(out) + "E"; break;
/* 196 */           case 15: out = String.valueOf(out) + "F"; break; }
/*     */       
/*     */       } else {
/* 199 */         out = String.valueOf(out) + Integer.toString(temp);
/*     */       } 
/*     */     } 
/* 202 */     return out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte byteOfHex(byte[] hex) {
/* 212 */     byte retByte = 0;
/*     */     
/* 214 */     int val = 0;
/*     */ 
/*     */     
/* 217 */     Byte tmp = Byte.valueOf(hex[0]);
/* 218 */     val = tmp.intValue();
/* 219 */     if (val > 64) {
/*     */       
/* 221 */       val -= 55;
/*     */     } else {
/*     */       
/* 224 */       val -= 48;
/*     */     } 
/* 226 */     retByte = (byte)(val << 4);
/*     */ 
/*     */     
/* 229 */     tmp = Byte.valueOf(hex[1]);
/* 230 */     val = tmp.intValue();
/* 231 */     if (val > 64) {
/*     */       
/* 233 */       val -= 55;
/*     */     } else {
/*     */       
/* 236 */       val -= 48;
/*     */     } 
/* 238 */     retByte = (byte)(retByte | (byte)val);
/*     */     
/* 240 */     return retByte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void printBits(byte aByte) {
/* 251 */     for (int i = 7; i >= 0; i--) {
/* 252 */       int temp = aByte >>> i;
/* 253 */       temp &= 0x1;
/* 254 */       System.out.print(temp);
/*     */     } 
/* 256 */     System.out.println();
/*     */   }
/*     */   
/*     */   public static String bitsOfBytes(byte[] bytes) {
/* 260 */     String str = "";
/*     */     
/* 262 */     for (int i = 0; i < bytes.length; i++) {
/* 263 */       str = String.valueOf(str) + bitsOfByte(bytes[i]) + " ";
/* 264 */       if ((i + 1) % 4 == 0) {
/* 265 */         str = String.valueOf(str) + "\n";
/*     */       }
/*     */     } 
/* 268 */     return str;
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\rtplib\StaticProcs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */