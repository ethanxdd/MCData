/*     */ package RTCP;
/*     */ 
/*     */ import java.nio.ByteBuffer;
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
/*     */ public class rtcp_granted
/*     */   extends Rtcp
/*     */ {
/*     */   public byte[] message;
/*     */   public byte[] Duration_field;
/*     */   public byte[] Floor_Priority_field;
/*     */   public byte[] Floor_Indicator_field;
/*     */   public byte[] SSRC_field;
/*     */   public short Duration_time;
/*     */   public int Priority_int;
/*     */   public short Floor_ind;
/*     */   public int SSRC_id;
/*     */   
/*     */   public rtcp_granted() {}
/*     */   
/*     */   public rtcp_granted(long SSRC_ID, long SSRC_ID2, short time, int priority, short indicator) {
/*  30 */     message_tools tool = new message_tools();
/*     */     
/*  32 */     this.header = messageheader("granted", SSRC_ID, "MCPT");
/*     */ 
/*     */     
/*  35 */     this.Duration_field = new byte[4];
/*  36 */     this.Duration_field = set_Duration_field(time);
/*     */     
/*  38 */     this.SSRC_field = new byte[4];
/*  39 */     this.SSRC_field = set_SSRC_field(SSRC_ID2);
/*     */     
/*  41 */     this.Floor_Priority_field = new byte[4];
/*  42 */     this.Floor_Priority_field = set_Floor_Priority_field(priority);
/*     */     
/*  44 */     this.Floor_Indicator_field = new byte[4];
/*  45 */     this.Floor_Indicator_field = set_Floor_Indicator_field(indicator);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  50 */     this.message = 
/*     */ 
/*     */ 
/*     */       
/*  54 */       new byte[this.header.length + this.Duration_field.length + this.SSRC_field.length + this.Floor_Priority_field.length + this.Floor_Indicator_field.length];
/*     */     
/*  56 */     tool.length_transf(this.header, this.message.length);
/*     */     
/*  58 */     writemessage();
/*     */   }
/*     */   public void writemessage() {
/*     */     int i;
/*  62 */     for (i = 0; i < this.header.length; i++)
/*  63 */       this.message[i] = this.header[i];  int j;
/*  64 */     for (i = this.header.length, j = 0; j < this.Duration_field.length; j++)
/*  65 */       this.message[i + j] = this.Duration_field[j]; 
/*  66 */     for (i = this.header.length + this.Duration_field.length, j = 0; j < this.SSRC_field.length; j++)
/*  67 */       this.message[i + j] = this.SSRC_field[j]; 
/*  68 */     for (i = this.header.length + this.Duration_field.length + this.SSRC_field.length, j = 0; j < this.Floor_Priority_field.length; j++)
/*  69 */       this.message[i + j] = this.Floor_Priority_field[j]; 
/*  70 */     for (i = this.header.length + this.Duration_field.length + this.SSRC_field.length + this.Floor_Priority_field.length, j = 0; j < this.Floor_Indicator_field.length; j++) {
/*  71 */       this.message[i + j] = this.Floor_Indicator_field[j];
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void get_field(byte[] M) {
/*  78 */     int nexti = 12, i = nexti;
/*  79 */     this.Duration_field = new byte[4]; int j;
/*  80 */     for (j = 0; j < 4; i++, j++) {
/*  81 */       this.Duration_field[j] = M[nexti + j];
/*     */     }
/*  83 */     nexti = i;
/*     */     
/*  85 */     for (; nexti % 4 != 0; nexti++);
/*     */     
/*  87 */     this.SSRC_field = new byte[M[nexti + 1] + 2];
/*  88 */     for (j = 0; j < M[nexti + 1] + 2; i++, j++) {
/*  89 */       this.SSRC_field[j] = M[nexti + j];
/*     */     }
/*     */     
/*  92 */     nexti = i;
/*  93 */     for (; nexti % 4 != 0; nexti++);
/*     */     
/*  95 */     this.Floor_Priority_field = new byte[4];
/*  96 */     for (j = 0; j < 4; i++, j++) {
/*  97 */       this.Floor_Priority_field[j] = M[nexti + j];
/*     */     }
/*     */ 
/*     */     
/* 101 */     nexti = i;
/* 102 */     for (; nexti % 4 != 0; nexti++);
/*     */     
/* 104 */     this.Floor_Indicator_field = new byte[M[nexti + 1] + 2];
/* 105 */     for (j = 0; j < M[nexti + 1] + 2; i++, j++) {
/* 106 */       this.Floor_Indicator_field[j] = M[nexti + j];
/*     */     }
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
/*     */   public void get_Duration_field(byte[] M) {
/* 119 */     if (M[0] != 1) {
/* 120 */       System.out.println("[rtcp_granted] Duration_time type error");
/*     */     } else {
/*     */       
/* 123 */       byte[] Tmp = new byte[2];
/* 124 */       int nexti = 2;
/* 125 */       for (int i = 0; i < 2; i++) {
/* 126 */         Tmp[i] = M[i + nexti];
/*     */       }
/* 128 */       ByteBuffer b = ByteBuffer.wrap(Tmp);
/* 129 */       this.Duration_time = b.getShort();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void get_Floor_Priority_field(byte[] M) {
/* 135 */     if (M[0] != 0) {
/* 136 */       System.out.println("[rtcp_granted] Floor_Priority_field type error");
/*     */     }
/*     */     else {
/*     */       
/* 140 */       this.Priority_int = Byte.toUnsignedInt(M[2]);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void get_Floor_Indicator_field(byte[] M) {
/* 146 */     if (M[0] != 13) {
/* 147 */       System.out.println("[rtcp_granted] Floor_Indicator_field type error");
/*     */     } else {
/*     */       
/* 150 */       byte[] Tmp = new byte[4];
/* 151 */       int nexti = 2;
/* 152 */       for (int i = 0; i < 2; i++) {
/* 153 */         Tmp[i] = M[i + nexti];
/*     */       }
/* 155 */       ByteBuffer b = ByteBuffer.wrap(Tmp);
/* 156 */       this.Floor_ind = b.getShort();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void get_SSRC_field(byte[] M) {
/* 162 */     if (M[0] != 14) {
/* 163 */       System.out.println("[rtcp_granted] SSRC_field type error");
/*     */     } else {
/*     */       
/* 166 */       byte[] Tmp = new byte[4];
/* 167 */       int nexti = 2;
/* 168 */       for (int i = 0; i < 4; i++) {
/* 169 */         Tmp[i] = M[i + nexti];
/*     */       }
/* 171 */       ByteBuffer b = ByteBuffer.wrap(Tmp);
/* 172 */       this.SSRC_id = b.getInt();
/*     */     } 
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
/*     */   public void parse_all_field(byte[] data) {
/* 203 */     get_field(data);
/* 204 */     get_header(data);
/* 205 */     get_Duration_field(this.Duration_field);
/* 206 */     get_SSRC_field(this.SSRC_field);
/* 207 */     get_Floor_Indicator_field(this.Floor_Indicator_field);
/* 208 */     get_Floor_Priority_field(this.Floor_Priority_field);
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\RTCP\rtcp_granted.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */