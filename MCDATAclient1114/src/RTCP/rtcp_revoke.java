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
/*     */ public class rtcp_revoke
/*     */   extends Rtcp
/*     */ {
/*     */   public byte[] message;
/*     */   public byte[] Reject_Cause_field;
/*     */   public byte[] Floor_Indicator_field;
/*     */   public int Source_id;
/*     */   public int Message_id;
/*     */   public int Reject_Cause_number;
/*     */   public String Reject_Phrase;
/*     */   public short Floor_Indicator_number;
/*     */   
/*     */   public rtcp_revoke() {}
/*     */   
/*     */   public rtcp_revoke(long SSRC_ID, short reject_cause, short indicator) {
/*  27 */     message_tools tool = new message_tools();
/*     */     
/*  29 */     this.header = messageheader("revoke", SSRC_ID, "MCPT");
/*     */     
/*  31 */     String phrase = get_reject_phrase(reject_cause);
/*  32 */     set_Reject_Cause_field_buffer(phrase, this.Reject_Cause_field);
/*  33 */     this.Reject_Cause_field = set_Reject_Cause_field(reject_cause, phrase);
/*     */     
/*  35 */     this.Floor_Indicator_field = new byte[4];
/*  36 */     this.Floor_Indicator_field = set_Floor_Indicator_field(indicator);
/*     */     
/*  38 */     this.message = 
/*     */       
/*  40 */       new byte[this.header.length + this.Reject_Cause_field.length + this.Floor_Indicator_field.length];
/*     */     
/*  42 */     tool.length_transf(this.header, this.message.length);
/*     */     
/*  44 */     writemessage();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] set_Reject_Cause_field(short reject, String phrase) {
/*  50 */     int buffer = phrase.length() + 4;
/*  51 */     for (; buffer % 4 != 0; buffer++);
/*  52 */     byte[] reject_cause = new byte[buffer];
/*  53 */     ByteBuffer b = ByteBuffer.allocate(2);
/*  54 */     b.putShort(reject);
/*  55 */     byte[] byteArr = b.array();
/*  56 */     reject_cause[0] = 2;
/*  57 */     reject_cause[1] = (byte)(phrase.length() + 2);
/*  58 */     reject_cause[2] = byteArr[0];
/*  59 */     reject_cause[3] = byteArr[1];
/*  60 */     for (int i = 0; i < phrase.length(); i++) {
/*  61 */       reject_cause[4 + i] = (byte)phrase.charAt(i);
/*     */     }
/*  63 */     return reject_cause;
/*     */   }
/*     */ 
/*     */   
/*     */   public String get_reject_phrase(short reject) {
/*  68 */     switch (reject) {
/*     */       case 1:
/*  70 */         return " Only one MCPTT client";
/*     */       case 2:
/*  72 */         return "Media burst too long";
/*     */       case 3:
/*  74 */         return "No permission to send a Media Burst";
/*     */       case 4:
/*  76 */         return "Media Burst pre-empted";
/*     */       case 6:
/*  78 */         return "No resources available";
/*     */       case 255:
/*  80 */         return "Other reason";
/*     */     } 
/*     */     
/*  83 */     return "cannot find this reject phrase";
/*     */   }
/*     */ 
/*     */   
/*     */   public void set_Reject_Cause_field_buffer(String phrase, byte[] reject_cause_field) {
/*  88 */     int length = phrase.length();
/*  89 */     int buffer = 0;
/*  90 */     buffer = length + 4;
/*  91 */     for (; buffer % 4 != 0; buffer++);
/*  92 */     reject_cause_field = new byte[buffer];
/*     */   }
/*     */ 
/*     */   
/*     */   public void writemessage() {
/*     */     int i;
/*  98 */     for (i = 0; i < this.header.length; i++)
/*  99 */       this.message[i] = this.header[i];  int j;
/* 100 */     for (i = this.header.length, j = 0; j < this.Reject_Cause_field.length; j++)
/* 101 */       this.message[i + j] = this.Reject_Cause_field[j]; 
/* 102 */     for (i = this.header.length + this.Reject_Cause_field.length, j = 0; j < this.Floor_Indicator_field.length; j++) {
/* 103 */       this.message[i + j] = this.Floor_Indicator_field[j];
/*     */     }
/*     */   }
/*     */   
/*     */   public void get_field(byte[] M) {
/* 108 */     int nexti = 12, i = nexti;
/* 109 */     this.Reject_Cause_field = new byte[M[nexti + 1] + 2]; int j;
/* 110 */     for (j = 0; j < M[nexti + 1] + 2; i++, j++) {
/* 111 */       this.Reject_Cause_field[j] = M[nexti + j];
/*     */     }
/*     */     
/* 114 */     nexti = i;
/*     */     
/* 116 */     for (; nexti % 4 != 0; nexti++);
/*     */     
/* 118 */     this.Floor_Indicator_field = new byte[M[nexti + 1] + 2];
/* 119 */     for (j = 0; j < M[nexti + 1] + 2; i++, j++) {
/* 120 */       this.Floor_Indicator_field[j] = M[nexti + j];
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void get_Reject_Cause_field(byte[] M) {
/* 126 */     if (M[0] != 2) {
/* 127 */       System.out.println("Reject_Cause_field type error");
/*     */     } else {
/*     */       
/* 130 */       byte[] Tmp = new byte[4];
/* 131 */       int nexti = 2;
/* 132 */       for (int i = 0; i < 2; i++) {
/* 133 */         Tmp[i] = M[i + nexti];
/*     */       }
/* 135 */       ByteBuffer b = ByteBuffer.wrap(Tmp);
/* 136 */       this.Reject_Cause_number = b.getShort();
/*     */       
/* 138 */       nexti = 4;
/* 139 */       Tmp = new byte[M[1] - 2];
/* 140 */       for (int j = 0; j < M[1] - 2; j++) {
/* 141 */         Tmp[j] = M[nexti + j];
/*     */       }
/* 143 */       this.Reject_Phrase = new String(Tmp);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void get_Floor_Indicator_field(byte[] M) {
/* 149 */     if (M[0] != 13) {
/* 150 */       System.out.println("Floor_Indicator_field type error");
/*     */     } else {
/*     */       
/* 153 */       byte[] Tmp = new byte[4];
/* 154 */       int nexti = 2;
/* 155 */       for (int i = 0; i < 2; i++) {
/* 156 */         Tmp[i] = M[i + nexti];
/*     */       }
/* 158 */       ByteBuffer b = ByteBuffer.wrap(Tmp);
/* 159 */       this.Floor_Indicator_number = b.getShort();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void parse_all_field(byte[] data) {
/* 165 */     get_field(data);
/* 166 */     get_Floor_Indicator_field(this.Floor_Indicator_field);
/* 167 */     get_Reject_Cause_field(this.Reject_Cause_field);
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\RTCP\rtcp_revoke.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */