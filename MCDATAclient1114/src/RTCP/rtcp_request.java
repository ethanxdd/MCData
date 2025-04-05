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
/*     */ public class rtcp_request
/*     */   extends Rtcp
/*     */ {
/*     */   public byte[] message;
/*     */   public byte[] Floor_Priority_field;
/*     */   public byte[] User_ID_field;
/*     */   public byte[] Floor_Indicator_field;
/*     */   public int Priority_int;
/*     */   public String User_id;
/*     */   public short Floor_ind;
/*     */   
/*     */   public rtcp_request() {}
/*     */   
/*     */   public rtcp_request(long SSRC_ID, int priority, String user_id, short indicator) {
/*  26 */     message_tools tool = new message_tools();
/*     */     
/*  28 */     this.header = messageheader("request", SSRC_ID, "MCPT");
/*     */ 
/*     */     
/*  31 */     this.Floor_Priority_field = new byte[4];
/*  32 */     this.Floor_Priority_field = set_Floor_Priority_field(priority);
/*     */     
/*  34 */     tool.set_head2_length_buffer(user_id, this.User_ID_field);
/*  35 */     this.User_ID_field = set_User_ID_field(user_id);
/*     */     
/*  37 */     this.Floor_Indicator_field = new byte[4];
/*  38 */     this.Floor_Indicator_field = set_Floor_Indicator_field(indicator);
/*     */ 
/*     */     
/*  41 */     this.message = 
/*     */ 
/*     */       
/*  44 */       new byte[this.header.length + this.Floor_Priority_field.length + this.User_ID_field.length + this.Floor_Indicator_field.length];
/*     */     
/*  46 */     tool.length_transf(this.header, this.message.length);
/*     */     
/*  48 */     writemessage();
/*     */   }
/*     */   public void writemessage() {
/*     */     int i;
/*  52 */     for (i = 0; i < this.header.length; i++)
/*  53 */       this.message[i] = this.header[i];  int j;
/*  54 */     for (i = this.header.length, j = 0; j < this.Floor_Priority_field.length; j++)
/*  55 */       this.message[i + j] = this.Floor_Priority_field[j]; 
/*  56 */     for (i = this.header.length + this.Floor_Priority_field.length, j = 0; j < this.User_ID_field.length; j++)
/*  57 */       this.message[i + j] = this.User_ID_field[j]; 
/*  58 */     for (i = this.header.length + this.Floor_Priority_field.length + this.User_ID_field.length, j = 0; j < this.Floor_Indicator_field.length; j++)
/*  59 */       this.message[i + j] = this.Floor_Indicator_field[j]; 
/*     */   }
/*     */   public byte[] set_User_ID_field(String id) {
/*  62 */     int buffer = id.length() + 2;
/*  63 */     for (; buffer % 4 != 0; buffer++);
/*  64 */     byte[] user = new byte[buffer];
/*  65 */     byte[] byteArr = id.getBytes();
/*  66 */     user[0] = 6;
/*  67 */     user[1] = (byte)id.length();
/*  68 */     for (int i = 0; i < id.length(); i++) {
/*  69 */       user[i + 2] = byteArr[i];
/*     */     }
/*  71 */     return user;
/*     */   }
/*     */   
/*     */   public void get_field(byte[] M) {
/*  75 */     int nexti = 12, i = nexti;
/*     */     
/*  77 */     this.Floor_Priority_field = new byte[4]; int j;
/*  78 */     for (j = 0; j < 4; i++, j++) {
/*  79 */       this.Floor_Priority_field[j] = M[nexti + j];
/*     */     }
/*     */     
/*  82 */     nexti = i;
/*  83 */     for (; nexti % 4 != 0; nexti++);
/*     */     
/*  85 */     this.User_ID_field = new byte[M[nexti + 1] + 2];
/*  86 */     for (j = 0; j < M[nexti + 1] + 2; i++, j++) {
/*  87 */       this.User_ID_field[j] = M[nexti + j];
/*     */     }
/*  89 */     nexti = i;
/*     */     
/*  91 */     for (; nexti % 4 != 0; nexti++);
/*     */     
/*  93 */     this.Floor_Indicator_field = new byte[M[nexti + 1] + 2];
/*  94 */     for (j = 0; j < M[nexti + 1] + 2; i++, j++) {
/*  95 */       this.Floor_Indicator_field[j] = M[nexti + j];
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void get_Floor_Priority_field(byte[] M) {
/* 101 */     if (M[0] != 0) {
/* 102 */       System.out.println("Floor_Priority_field type error");
/*     */     }
/*     */     else {
/*     */       
/* 106 */       this.Priority_int = Byte.toUnsignedInt(M[2]);
/* 107 */       System.out.println("floor priority = " + this.Priority_int);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void get_User_ID_field(byte[] M) {
/* 113 */     if (M[0] != 6) {
/* 114 */       System.out.println("User_ID_field type error");
/*     */     } else {
/*     */       
/* 117 */       byte[] Tmp = new byte[M[1]];
/* 118 */       int nexti = 2;
/* 119 */       for (int i = 0; i < M[1]; i++) {
/* 120 */         Tmp[i] = M[nexti + i];
/*     */       }
/* 122 */       this.User_id = new String(Tmp);
/* 123 */       System.out.println("user uri = " + this.User_id);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void get_Floor_Indicator_field(byte[] M) {
/* 129 */     if (M[0] != 13) {
/* 130 */       System.out.println("Floor_Indicator_field type error");
/*     */     } else {
/*     */       
/* 133 */       byte[] Tmp = new byte[4];
/* 134 */       int nexti = 2;
/* 135 */       for (int i = 0; i < 2; i++) {
/* 136 */         Tmp[i] = M[i + nexti];
/*     */       }
/* 138 */       ByteBuffer b = ByteBuffer.wrap(Tmp);
/* 139 */       this.Floor_ind = b.getShort();
/* 140 */       System.out.println("floor indaicator = " + this.Floor_ind);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void parse_allfield(byte[] Data) {
/* 146 */     get_field(Data);
/* 147 */     get_header(Data);
/* 148 */     get_User_ID_field(this.User_ID_field);
/* 149 */     get_Floor_Priority_field(this.Floor_Priority_field);
/* 150 */     get_Floor_Indicator_field(this.Floor_Indicator_field);
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\RTCP\rtcp_request.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */