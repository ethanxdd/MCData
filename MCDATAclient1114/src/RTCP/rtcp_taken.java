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
/*     */ public class rtcp_taken
/*     */   extends Rtcp
/*     */ {
/*     */   public byte[] message;
/*     */   public byte[] Granted_Party_id_field;
/*     */   public byte[] Permission_to_Request_field;
/*     */   public byte[] Message_Sequence_Number_field;
/*     */   public byte[] Floor_Indicator_field;
/*     */   public byte[] SSRC_field;
/*     */   public String Granted_id;
/*     */   public int permission_int;
/*     */   public short Floor_ind;
/*     */   public int SSRC_id;
/*     */   public short Message_Sequence_Number;
/*     */   
/*     */   public rtcp_taken() {}
/*     */   
/*     */   public rtcp_taken(long SSRC_ID, long SSRC_ID2, String granted_id, int permission, short MSN, short Floor) {
/*  31 */     message_tools tool = new message_tools();
/*     */     
/*  33 */     this.header = messageheader("taken", SSRC_ID, "MCPT");
/*     */     
/*  35 */     tool.set_head2_length_buffer(granted_id, this.Granted_Party_id_field);
/*  36 */     this.Granted_Party_id_field = set_Granted_Party_id_field(granted_id);
/*     */     
/*  38 */     this.Permission_to_Request_field = new byte[4];
/*  39 */     this.Permission_to_Request_field = set_Permission_to_Request_field(permission);
/*     */     
/*  41 */     this.Message_Sequence_Number_field = new byte[4];
/*  42 */     this.Message_Sequence_Number_field = set_Message_Sequence_Number_field(MSN);
/*     */     
/*  44 */     this.Floor_Indicator_field = new byte[4];
/*  45 */     this.Floor_Indicator_field = set_Floor_Indicator_field(Floor);
/*     */     
/*  47 */     this.SSRC_field = new byte[4];
/*  48 */     this.SSRC_field = set_SSRC_field(SSRC_ID2);
/*     */     
/*  50 */     this.message = 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  55 */       new byte[this.header.length + this.Granted_Party_id_field.length + this.Permission_to_Request_field.length + this.Message_Sequence_Number_field.length + this.Floor_Indicator_field.length + this.SSRC_field.length];
/*     */     
/*  57 */     tool.length_transf(this.header, this.message.length);
/*     */     
/*  59 */     writemessage();
/*     */   }
/*     */   
/*     */   public void writemessage() {
/*     */     int i;
/*  64 */     for (i = 0; i < this.header.length; i++)
/*  65 */       this.message[i] = this.header[i];  int j;
/*  66 */     for (i = this.header.length, j = 0; j < this.Granted_Party_id_field.length; j++)
/*  67 */       this.message[i + j] = this.Granted_Party_id_field[j]; 
/*  68 */     for (i = this.header.length + this.Granted_Party_id_field.length, j = 0; j < this.Permission_to_Request_field.length; j++)
/*  69 */       this.message[i + j] = this.Permission_to_Request_field[j]; 
/*  70 */     for (i = this.header.length + this.Granted_Party_id_field.length + this.Permission_to_Request_field.length, j = 0; j < this.Message_Sequence_Number_field.length; j++)
/*  71 */       this.message[i + j] = this.Message_Sequence_Number_field[j]; 
/*  72 */     for (i = this.header.length + this.Granted_Party_id_field.length + this.Permission_to_Request_field.length + this.Message_Sequence_Number_field.length, j = 0; j < this.Floor_Indicator_field.length; j++)
/*  73 */       this.message[i + j] = this.Floor_Indicator_field[j]; 
/*  74 */     for (i = this.header.length + this.Granted_Party_id_field.length + this.Permission_to_Request_field.length + this.Message_Sequence_Number_field.length + this.Floor_Indicator_field.length, j = 0; j < this.SSRC_field.length; j++) {
/*  75 */       this.message[i + j] = this.SSRC_field[j];
/*     */     }
/*     */   }
/*     */   
/*     */   public void get_field(byte[] M) {
/*  80 */     int nexti = 12, i = nexti;
/*  81 */     this.Granted_Party_id_field = new byte[M[nexti + 1] + 2]; int j;
/*  82 */     for (j = 0; j < M[nexti + 1] + 2; i++, j++) {
/*  83 */       this.Granted_Party_id_field[j] = M[nexti + j];
/*     */     }
/*     */     
/*  86 */     nexti = i;
/*     */     
/*  88 */     for (; nexti % 4 != 0; nexti++);
/*  89 */     this.Permission_to_Request_field = new byte[4];
/*  90 */     for (j = 0; j < 4; i++, j++) {
/*  91 */       this.Permission_to_Request_field[j] = M[nexti + j];
/*     */     }
/*     */     
/*  94 */     nexti = i;
/*  95 */     for (; nexti % 4 != 0; nexti++);
/*  96 */     this.Message_Sequence_Number_field = new byte[4];
/*  97 */     for (j = 0; j < 4; i++, j++) {
/*  98 */       this.Message_Sequence_Number_field[j] = M[nexti + j];
/*     */     }
/*     */ 
/*     */     
/* 102 */     nexti = i;
/* 103 */     for (; nexti % 4 != 0; nexti++);
/* 104 */     this.Floor_Indicator_field = new byte[M[nexti + 1] + 2];
/* 105 */     for (j = 0; j < M[nexti + 1] + 2; i++, j++) {
/* 106 */       this.Floor_Indicator_field[j] = M[nexti + j];
/*     */     }
/*     */     
/* 109 */     nexti = i;
/* 110 */     for (; nexti % 4 != 0; nexti++);
/*     */     
/* 112 */     this.SSRC_field = new byte[M[nexti + 1] + 2];
/* 113 */     for (j = 0; j < M[nexti + 1] + 2; i++, j++) {
/* 114 */       this.SSRC_field[j] = M[nexti + j];
/*     */     }
/*     */   }
/*     */   
/*     */   public String get_Granted_Party_id_field(byte[] M) {
/* 119 */     if (M[0] != 4) {
/* 120 */       System.out.println("[rtcp_taken] Granted_Party_id_field type error");
/*     */     } else {
/*     */       
/* 123 */       byte[] Tmp = new byte[M[1]];
/* 124 */       int nexti = 2;
/* 125 */       for (int i = 0; i < M[1]; i++) {
/* 126 */         Tmp[i] = M[nexti + i];
/*     */       }
/* 128 */       this.Granted_id = new String(Tmp);
/*     */     } 
/*     */     
/* 131 */     String granted_id = this.Granted_id;
/* 132 */     return granted_id;
/*     */   }
/*     */   
/*     */   public void get_Permission_to_Request_field(byte[] M) {
/* 136 */     if (M[0] != 5) {
/* 137 */       System.out.println("[rtcp_taken] Permission_to_Request_field type error");
/*     */     }
/*     */     else {
/*     */       
/* 141 */       this.permission_int = Byte.toUnsignedInt(M[3]);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void get_Message_Sequence_Number_field(byte[] M) {
/* 147 */     if (M[0] != 8) {
/* 148 */       System.out.println("[rtcp_taken] Message_Sequence_Number_field type error");
/*     */     } else {
/*     */       
/* 151 */       byte[] Tmp = new byte[2];
/* 152 */       int nexti = 2;
/* 153 */       for (int i = 0; i < 2; i++) {
/* 154 */         Tmp[i] = M[i + nexti];
/*     */       }
/* 156 */       ByteBuffer b = ByteBuffer.wrap(Tmp);
/* 157 */       this.Message_Sequence_Number = b.getShort();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public short get_Floor_Indicator_field(byte[] M) {
/* 164 */     if (M[0] != 13) {
/* 165 */       System.out.println("[rtcp_taken] Floor_Indicator_field type error");
/*     */     } else {
/*     */       
/* 168 */       byte[] Tmp = new byte[4];
/* 169 */       int nexti = 2;
/* 170 */       for (int i = 0; i < 2; i++) {
/* 171 */         Tmp[i] = M[i + nexti];
/*     */       }
/* 173 */       ByteBuffer b = ByteBuffer.wrap(Tmp);
/* 174 */       this.Floor_ind = b.getShort();
/*     */     } 
/*     */     
/* 177 */     return this.Floor_ind;
/*     */   }
/*     */   
/*     */   public void get_SSRC_field(byte[] M) {
/* 181 */     if (M[0] != 14) {
/* 182 */       System.out.println("[rtcp_taken] SSRC_field type error");
/*     */     } else {
/*     */       
/* 185 */       byte[] Tmp = new byte[4];
/* 186 */       int nexti = 2;
/* 187 */       for (int i = 0; i < 4; i++) {
/* 188 */         Tmp[i] = M[i + nexti];
/*     */       }
/* 190 */       ByteBuffer b = ByteBuffer.wrap(Tmp);
/* 191 */       this.SSRC_id = b.getInt();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void parse_all_field(byte[] data) {
/* 197 */     get_field(data);
/* 198 */     get_Floor_Indicator_field(this.Floor_Indicator_field);
/* 199 */     get_Granted_Party_id_field(this.Granted_Party_id_field);
/* 200 */     get_Message_Sequence_Number_field(this.Message_Sequence_Number_field);
/* 201 */     get_Permission_to_Request_field(this.Permission_to_Request_field);
/* 202 */     get_SSRC_field(this.SSRC_field);
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\RTCP\rtcp_taken.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */