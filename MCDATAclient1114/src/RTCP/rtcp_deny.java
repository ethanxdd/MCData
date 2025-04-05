/*     */ package RTCP;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class rtcp_deny
/*     */   extends Rtcp
/*     */ {
/*     */   public byte[] message;
/*     */   public byte[] header;
/*     */   public byte[] User_ID_field;
/*     */   public byte[] Floor_Indicator_field;
/*     */   public byte[] Reject_Cause_field;
/*     */   public String User_id;
/*     */   public int Floor_Indicator;
/*     */   public int Reject_Cause;
/*     */   public String Reject_Phrase;
/*     */   
/*     */   public rtcp_deny() {}
/*     */   
/*     */   public rtcp_deny(long SSRC_ID, int reject, String user_id, short Floor) {
/*  26 */     message_tools tool = new message_tools();
/*     */     
/*  28 */     this.header = messageheader("deny", SSRC_ID, "MCPT");
/*     */     
/*  30 */     String phrase = get_reject_phrase(reject);
/*  31 */     this.Reject_Cause_field = set_Reject_Cause_field(reject, phrase);
/*  32 */     set_Reject_Cause_field_buffer(phrase, this.Reject_Cause_field);
/*     */ 
/*     */     
/*  35 */     tool.set_head2_length_buffer(user_id, this.User_ID_field);
/*  36 */     this.User_ID_field = set_User_ID_field(user_id);
/*     */     
/*  38 */     this.Floor_Indicator_field = new byte[4];
/*  39 */     this.Floor_Indicator_field = set_Floor_Indicator_field(Floor);
/*  40 */     this.message = 
/*     */ 
/*     */       
/*  43 */       new byte[this.header.length + this.Reject_Cause_field.length + this.User_ID_field.length + this.Floor_Indicator_field.length];
/*     */     
/*  45 */     tool.length_transf(this.header, this.message.length);
/*     */     
/*  47 */     writemessage();
/*     */   }
/*     */   
/*     */   public void writemessage() {
/*     */     int i;
/*  52 */     for (i = 0; i < this.header.length; i++)
/*  53 */       this.message[i] = this.header[i];  int j;
/*  54 */     for (i = this.header.length, j = 0; j < this.Reject_Cause_field.length; j++)
/*  55 */       this.message[i + j] = this.Reject_Cause_field[j]; 
/*  56 */     for (i = this.header.length + this.Reject_Cause_field.length, j = 0; j < this.User_ID_field.length; j++)
/*  57 */       this.message[i + j] = this.User_ID_field[j]; 
/*  58 */     for (i = this.header.length + this.Reject_Cause_field.length + this.User_ID_field.length, j = 0; j < this.Floor_Indicator_field.length; j++) {
/*  59 */       this.message[i + j] = this.Floor_Indicator_field[j];
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] set_Reject_Cause_field(int reject, String phrase) {
/*  66 */     int buffer = phrase.length() + 4;
/*  67 */     for (; buffer % 4 != 0; buffer++);
/*  68 */     byte[] reject_cause = new byte[buffer];
/*  69 */     ByteBuffer b = ByteBuffer.allocate(4);
/*  70 */     b.putInt(reject);
/*  71 */     byte[] byteArr = b.array();
/*  72 */     reject_cause[0] = 2;
/*  73 */     reject_cause[1] = (byte)(phrase.length() + 2);
/*  74 */     reject_cause[2] = byteArr[2];
/*  75 */     reject_cause[3] = byteArr[3];
/*  76 */     for (int i = 0; i < phrase.length(); i++) {
/*  77 */       reject_cause[4 + i] = (byte)phrase.charAt(i);
/*     */     }
/*  79 */     return reject_cause;
/*     */   }
/*     */ 
/*     */   
/*     */   public String get_reject_phrase(int reject) {
/*  84 */     switch (reject) {
/*     */       case 1:
/*  86 */         return "Another MCPTT client has permission";
/*     */       case 2:
/*  88 */         return "Internal floor control server error";
/*     */       case 3:
/*  90 */         return "Only one participant";
/*     */       case 4:
/*  92 */         return "Retry-after timer has not expired";
/*     */       case 5:
/*  94 */         return "Receive only";
/*     */       case 6:
/*  96 */         return "No resources available";
/*     */       case 7:
/*  98 */         return "Queue full";
/*     */       case 255:
/* 100 */         return "Other reason";
/*     */     } 
/*     */     
/* 103 */     return "cannot find this reject phrase";
/*     */   }
/*     */ 
/*     */   
/*     */   public void set_Reject_Cause_field_buffer(String phrase, byte[] reject_cause_field) {
/* 108 */     int length = phrase.length();
/* 109 */     int buffer = 0;
/* 110 */     buffer = length + 4;
/* 111 */     for (; buffer % 4 != 0; buffer++);
/* 112 */     reject_cause_field = new byte[buffer];
/*     */   }
/*     */   
/*     */   public byte[] set_User_ID_field(String id) {
/* 116 */     int buffer = id.length() + 2;
/* 117 */     for (; buffer % 4 != 0; buffer++);
/* 118 */     byte[] user = new byte[buffer];
/* 119 */     byte[] byteArr = id.getBytes();
/* 120 */     user[0] = 6;
/* 121 */     user[1] = (byte)id.length();
/* 122 */     for (int i = 0; i < id.length(); i++) {
/* 123 */       user[i + 2] = byteArr[i];
/*     */     }
/* 125 */     return user;
/*     */   }
/*     */ 
/*     */   
/*     */   public void get_field(byte[] M) {
/* 130 */     int nexti = 12, i = nexti;
/* 131 */     this.Reject_Cause_field = new byte[M[nexti + 1] + 2]; int j;
/* 132 */     for (j = 0; j < M[nexti + 1] + 2; i++, j++) {
/* 133 */       this.Reject_Cause_field[j] = M[nexti + j];
/*     */     }
/*     */     
/* 136 */     nexti = i;
/*     */     
/* 138 */     for (; nexti % 4 != 0; nexti++);
/*     */     
/* 140 */     this.User_ID_field = new byte[M[nexti + 1] + 2];
/* 141 */     for (j = 0; j < M[nexti + 1] + 2; i++, j++) {
/* 142 */       this.User_ID_field[j] = M[nexti + j];
/*     */     }
/*     */ 
/*     */     
/* 146 */     nexti = i;
/*     */ 
/*     */     
/* 149 */     for (; nexti % 4 != 0; nexti++);
/*     */     
/* 151 */     this.Floor_Indicator_field = new byte[M[nexti + 1] + 4];
/*     */     
/* 153 */     for (j = 0; j < M[nexti + 1] + 4; i++, j++) {
/* 154 */       this.Floor_Indicator_field[j] = M[nexti + j + 4];
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void get_Reject_Cause_field(byte[] M) {
/* 163 */     if (M[0] != 2) {
/* 164 */       System.out.println("Reject_Cause_field type error");
/*     */     } else {
/*     */       
/* 167 */       byte[] Tmp = new byte[4];
/* 168 */       int nexti = 2;
/* 169 */       for (int i = 0; i < 2; i++) {
/* 170 */         Tmp[i] = M[i + nexti];
/*     */       }
/* 172 */       ByteBuffer b = ByteBuffer.wrap(Tmp);
/* 173 */       this.Reject_Cause = b.getShort();
/*     */       
/* 175 */       nexti = 4;
/* 176 */       Tmp = new byte[M[1] - 2];
/* 177 */       for (int j = 0; j < M[1] - 2; j++) {
/* 178 */         Tmp[j] = M[nexti + j];
/*     */       }
/* 180 */       this.Reject_Phrase = new String(Tmp);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void get_User_ID_field(byte[] M) {
/* 186 */     if (M[0] != 6) {
/* 187 */       System.out.println("User_id_field type error");
/*     */     } else {
/*     */       
/* 190 */       byte[] Tmp = new byte[M[1]];
/* 191 */       int nexti = 2;
/* 192 */       for (int i = 0; i < M[1]; i++) {
/* 193 */         Tmp[i] = M[nexti + i];
/*     */       }
/* 195 */       this.User_id = new String(Tmp);
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
/*     */   public void get_Floor_Indicator_field(byte[] M) {
/* 208 */     if (M[0] != 13) {
/* 209 */       System.out.println("Floor_Indicator_field type error");
/*     */     } else {
/*     */       
/* 212 */       byte[] Tmp = new byte[4];
/* 213 */       int nexti = 2;
/* 214 */       for (int i = 0; i < 2; i++) {
/* 215 */         Tmp[i] = M[i + nexti];
/*     */       }
/* 217 */       ByteBuffer b = ByteBuffer.wrap(Tmp);
/* 218 */       this.Floor_Indicator = b.getShort();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse_all_field(byte[] data) {
/* 225 */     get_field(data);
/* 226 */     get_Reject_Cause_field(this.Reject_Cause_field);
/* 227 */     get_User_ID_field(this.User_ID_field);
/* 228 */     get_Floor_Indicator_field(this.Floor_Indicator_field);
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\RTCP\rtcp_deny.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */