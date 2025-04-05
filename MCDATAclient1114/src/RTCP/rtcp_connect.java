/*     */ package RTCP;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class rtcp_connect
/*     */   extends Rtcp
/*     */ {
/*     */   public static byte[] message;
/*     */   public byte[] Session_id_field;
/*     */   public byte[] Media_Streams_field;
/*     */   public byte[] Answer_state_field;
/*     */   public byte[] Invite_MCU_field;
/*     */   public String MCS_ID;
/*     */   public String Session_id;
/*     */   public int Media_stream;
/*     */   public int control_channel;
/*     */   public int Session_type;
/*     */   public int answer_state;
/*     */   public String Invite_MCU;
/*     */   
/*     */   public rtcp_connect() {}
/*     */   
/*     */   public rtcp_connect(long SSRC_ID, String MCU_ID, String session_id, int media, int channel, int answer) {
/*  28 */     message_tools tool = new message_tools();
/*     */     
/*  30 */     this.header = messageheader("connect", SSRC_ID, "MCPC");
/*     */     
/*  32 */     tool.set_head3_length_buffer(session_id, this.Session_id_field);
/*  33 */     this.Session_id_field = set_Session_id_field(session_id);
/*     */     
/*  35 */     this.Media_Streams_field = new byte[4];
/*  36 */     this.Media_Streams_field = set_Media_Streams_field(media, channel);
/*  37 */     this.Answer_state_field = new byte[4];
/*  38 */     this.Answer_state_field = set_Answer_state_field(answer);
/*  39 */     tool.set_head2_length_buffer(MCU_ID, this.Invite_MCU_field);
/*  40 */     this.Invite_MCU_field = set_Invite_MCU_field(MCU_ID);
/*     */     
/*  42 */     message = new byte[this.header.length + 
/*  43 */         this.Session_id_field.length + 
/*  44 */         this.Media_Streams_field.length + 
/*  45 */         this.Answer_state_field.length + 
/*  46 */         this.Invite_MCU_field.length];
/*     */     
/*  48 */     tool.length_transf(this.header, message.length);
/*     */     
/*  50 */     writemessage();
/*     */   }
/*     */   
/*     */   public void writemessage() {
/*     */     int i;
/*  55 */     for (i = 0; i < this.header.length; i++)
/*  56 */       message[i] = this.header[i];  int j;
/*  57 */     for (i = this.header.length, j = 0; j < this.Session_id_field.length; j++)
/*  58 */       message[i + j] = this.Session_id_field[j]; 
/*  59 */     for (i = this.header.length + this.Session_id_field.length, j = 0; j < this.Media_Streams_field.length; j++)
/*  60 */       message[i + j] = this.Media_Streams_field[j]; 
/*  61 */     for (i = this.header.length + this.Session_id_field.length + this.Media_Streams_field.length, j = 0; j < this.Answer_state_field.length; j++)
/*  62 */       message[i + j] = this.Answer_state_field[j]; 
/*  63 */     for (i = this.header.length + this.Session_id_field.length + this.Media_Streams_field.length + this.Answer_state_field.length, j = 0; j < this.Invite_MCU_field.length; j++) {
/*  64 */       message[i + j] = this.Invite_MCU_field[j];
/*     */     }
/*     */   }
/*     */   
/*     */   public void get_field(byte[] M) {
/*  69 */     int nexti = 12, i = nexti;
/*  70 */     this.Session_id_field = new byte[M[nexti + 1] + 3]; int j;
/*  71 */     for (j = 0; j < M[nexti + 1] + 3; i++, j++) {
/*  72 */       this.Session_id_field[j] = M[nexti + j];
/*     */     }
/*  74 */     nexti = i;
/*     */     
/*  76 */     for (; nexti % 4 != 0; nexti++);
/*     */     
/*  78 */     this.Media_Streams_field = new byte[4];
/*  79 */     for (j = 0; j < 4; i++, j++) {
/*  80 */       this.Media_Streams_field[j] = M[nexti + j];
/*     */     }
/*     */     
/*  83 */     nexti = i;
/*  84 */     for (; nexti % 4 != 0; nexti++);
/*     */     
/*  86 */     this.Answer_state_field = new byte[4];
/*  87 */     for (j = 0; j < 4; i++, j++) {
/*  88 */       this.Answer_state_field[j] = M[nexti + j];
/*     */     }
/*     */ 
/*     */     
/*  92 */     nexti = i;
/*  93 */     for (; nexti % 4 != 0; nexti++);
/*     */     
/*  95 */     this.Invite_MCU_field = new byte[M[nexti + 1] + 2];
/*  96 */     for (j = 0; j < M[nexti + 1] + 2; i++, j++) {
/*  97 */       this.Invite_MCU_field[j] = M[nexti + j];
/*     */     }
/*     */   }
/*     */   
/*     */   public void get_Session_id_field(byte[] M) {
/* 102 */     if (M[0] != 1) {
/* 103 */       System.out.println("Session_id_field type error");
/*     */     } else {
/*     */       
/* 106 */       byte[] Tmp = new byte[M[1]];
/* 107 */       int nexti = 3;
/* 108 */       for (int i = 0; i < M[1]; i++) {
/* 109 */         Tmp[i] = M[nexti + i];
/*     */       }
/*     */       
/* 112 */       this.Session_id = new String(Tmp);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void get_Media_Streams_field(byte[] M) {
/* 118 */     if (M[0] != 0) {
/* 119 */       System.out.println("Media_Streams_field type error");
/*     */     }
/*     */     else {
/*     */       
/* 123 */       this.Media_stream = Byte.toUnsignedInt(M[2]);
/*     */       
/* 125 */       this.control_channel = Byte.toUnsignedInt(M[3]);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void get_Answer_state_field(byte[] M) {
/* 130 */     if (M[0] != 4) {
/* 131 */       System.out.println("Answer_state_field type error");
/*     */     }
/*     */     else {
/*     */       
/* 135 */       this.answer_state = Byte.toUnsignedInt(M[3]);
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
/*     */   public void get_Invite_MCU_field(byte[] M) {
/* 147 */     if (M[0] != 5) {
/* 148 */       System.out.println("Invite_MCU_field type error");
/*     */     } else {
/*     */       
/* 151 */       byte[] Tmp = new byte[M[1]];
/* 152 */       int nexti = 2;
/* 153 */       for (int i = 0; i < M[1]; i++) {
/* 154 */         Tmp[i] = M[nexti + i];
/*     */       }
/* 156 */       this.Invite_MCU = new String(Tmp);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void parse_all_field(byte[] data) {
/* 162 */     get_field(data);
/* 163 */     get_Answer_state_field(this.Answer_state_field);
/* 164 */     get_Invite_MCU_field(this.Invite_MCU_field);
/* 165 */     get_Session_id_field(this.Session_id_field);
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\RTCP\rtcp_connect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */