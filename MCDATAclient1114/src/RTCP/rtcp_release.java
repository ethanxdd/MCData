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
/*     */ public class rtcp_release
/*     */   extends Rtcp
/*     */ {
/*     */   public byte[] message;
/*     */   public byte[] header;
/*     */   public byte[] User_ID_field;
/*     */   public byte[] Floor_Indicator_field;
/*     */   public String User_id;
/*     */   public short Floor_ind;
/*     */   
/*     */   public rtcp_release() {}
/*     */   
/*     */   public rtcp_release(long SSRC_ID, String user_id, short indicator) {
/*  25 */     message_tools tool = new message_tools();
/*     */     
/*  27 */     this.header = messageheader("release", SSRC_ID, "MCPT");
/*     */     
/*  29 */     tool.set_head2_length_buffer(user_id, this.User_ID_field);
/*  30 */     this.User_ID_field = set_User_ID_field(user_id);
/*     */     
/*  32 */     this.Floor_Indicator_field = new byte[4];
/*  33 */     this.Floor_Indicator_field = set_Floor_Indicator_field(indicator);
/*     */ 
/*     */     
/*  36 */     this.message = 
/*     */       
/*  38 */       new byte[this.header.length + this.User_ID_field.length + this.Floor_Indicator_field.length];
/*     */     
/*  40 */     tool.length_transf(this.header, this.message.length);
/*     */     
/*  42 */     writemessage();
/*     */   }
/*     */   
/*     */   public void writemessage() {
/*     */     int i;
/*  47 */     for (i = 0; i < this.header.length; i++)
/*  48 */       this.message[i] = this.header[i];  int j;
/*  49 */     for (i = this.header.length, j = 0; j < this.User_ID_field.length; j++)
/*  50 */       this.message[i + j] = this.User_ID_field[j]; 
/*  51 */     for (i = this.header.length + this.User_ID_field.length, j = 0; j < this.Floor_Indicator_field.length; j++)
/*  52 */       this.message[i + j] = this.Floor_Indicator_field[j]; 
/*     */   }
/*     */   
/*     */   public byte[] set_User_ID_field(String id) {
/*  56 */     int buffer = id.length() + 2;
/*  57 */     for (; buffer % 4 != 0; buffer++);
/*  58 */     byte[] user = new byte[buffer];
/*  59 */     byte[] byteArr = id.getBytes();
/*  60 */     user[0] = 6;
/*  61 */     user[1] = (byte)id.length();
/*  62 */     for (int i = 0; i < id.length(); i++) {
/*  63 */       user[i + 2] = byteArr[i];
/*     */     }
/*  65 */     return user;
/*     */   }
/*     */   
/*     */   public void get_field(byte[] M) {
/*  69 */     int nexti = 12, i = nexti;
/*     */     
/*  71 */     this.User_ID_field = new byte[M[nexti + 1] + 2]; int j;
/*  72 */     for (j = 0; j < M[nexti + 1] + 2; i++, j++) {
/*  73 */       this.User_ID_field[j] = M[nexti + j];
/*     */     }
/*  75 */     nexti = i;
/*     */     
/*  77 */     for (; nexti % 4 != 0; nexti++);
/*     */     
/*  79 */     this.Floor_Indicator_field = new byte[M[nexti + 1] + 2];
/*  80 */     for (j = 0; j < M[nexti + 1] + 2; i++, j++) {
/*  81 */       this.Floor_Indicator_field[j] = M[nexti + j];
/*     */     }
/*     */   }
/*     */   
/*     */   public void get_User_ID_field(byte[] M) {
/*  86 */     if (M[0] != 6) {
/*  87 */       System.out.println("User_ID_field type error");
/*     */     } else {
/*     */       
/*  90 */       byte[] Tmp = new byte[M[1]];
/*  91 */       int nexti = 2;
/*  92 */       for (int i = 0; i < M[1]; i++) {
/*  93 */         Tmp[i] = M[nexti + i];
/*     */       }
/*  95 */       this.User_id = new String(Tmp);
/*  96 */       System.out.println("user uri = " + this.User_id);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void get_Floor_Indicator_field(byte[] M) {
/* 101 */     if (M[0] != 13) {
/* 102 */       System.out.println("Floor_Indicator_field type error");
/*     */     } else {
/*     */       
/* 105 */       byte[] Tmp = new byte[4];
/* 106 */       int nexti = 2;
/* 107 */       for (int i = 0; i < 2; i++) {
/* 108 */         Tmp[i] = M[i + nexti];
/*     */       }
/* 110 */       ByteBuffer b = ByteBuffer.wrap(Tmp);
/* 111 */       this.Floor_ind = b.getShort();
/* 112 */       System.out.println("floor indicator = " + this.Floor_ind);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void parse_allfield(byte[] Data) {
/* 117 */     get_field(Data);
/* 118 */     get_header(Data);
/* 119 */     get_User_ID_field(this.User_ID_field);
/* 120 */     get_Floor_Indicator_field(this.Floor_Indicator_field);
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\RTCP\rtcp_release.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */