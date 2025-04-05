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
/*     */ public class rtcp_queue_position_info
/*     */   extends Rtcp
/*     */ {
/*     */   public byte[] message;
/*     */   public byte[] User_ID_field;
/*     */   public byte[] Queue_Info_field;
/*     */   public byte[] Floor_Indicator_field;
/*     */   public int Position_info;
/*     */   public int Priority_int;
/*     */   public short Floor_Indicator_number;
/*     */   public String User_id;
/*     */   
/*     */   public rtcp_queue_position_info() {}
/*     */   
/*     */   public rtcp_queue_position_info(long SSRC_ID, String user_id, int queue_position_info, int queue_priority, short indicator) {
/*  27 */     message_tools tool = new message_tools();
/*     */     
/*  29 */     this.header = messageheader("queue_position_info", SSRC_ID, "MCPT");
/*     */ 
/*     */     
/*  32 */     tool.set_head2_length_buffer(user_id, this.User_ID_field);
/*  33 */     this.User_ID_field = set_User_ID_field(user_id);
/*     */     
/*  35 */     this.Queue_Info_field = new byte[4];
/*  36 */     this.Queue_Info_field = set_Queue_Info_field(queue_position_info, queue_priority);
/*     */     
/*  38 */     this.Floor_Indicator_field = new byte[4];
/*  39 */     this.Floor_Indicator_field = set_Floor_Indicator_field(indicator);
/*     */     
/*  41 */     this.message = 
/*     */ 
/*     */       
/*  44 */       new byte[this.header.length + this.User_ID_field.length + this.Queue_Info_field.length + this.Floor_Indicator_field.length];
/*     */     
/*  46 */     tool.length_transf(this.header, this.message.length);
/*     */     
/*  48 */     writemessage();
/*     */   }
/*     */   
/*     */   public void writemessage() {
/*     */     int i;
/*  53 */     for (i = 0; i < this.header.length; i++)
/*  54 */       this.message[i] = this.header[i];  int j;
/*  55 */     for (i = this.header.length, j = 0; j < this.User_ID_field.length; j++)
/*  56 */       this.message[i + j] = this.User_ID_field[j]; 
/*  57 */     for (i = this.header.length + this.User_ID_field.length, j = 0; j < this.Queue_Info_field.length; j++)
/*  58 */       this.message[i + j] = this.Queue_Info_field[j]; 
/*  59 */     for (i = this.header.length + this.User_ID_field.length + this.Queue_Info_field.length, j = 0; j < this.Floor_Indicator_field.length; j++)
/*  60 */       this.message[i + j] = this.Floor_Indicator_field[j]; 
/*     */   }
/*     */   
/*     */   public byte[] set_Queue_Info_field(int position_info, int priority) {
/*  64 */     int buffer = 4;
/*  65 */     byte[] FP = new byte[buffer];
/*  66 */     FP[0] = 3;
/*  67 */     FP[1] = 2;
/*  68 */     FP[2] = (byte)position_info;
/*  69 */     FP[3] = (byte)priority;
/*  70 */     return FP;
/*     */   }
/*     */   
/*     */   public byte[] set_User_ID_field(String id) {
/*  74 */     int buffer = id.length() + 2;
/*  75 */     for (; buffer % 4 != 0; buffer++);
/*  76 */     byte[] user = new byte[buffer];
/*  77 */     byte[] byteArr = id.getBytes();
/*  78 */     user[0] = 6;
/*  79 */     user[1] = (byte)id.length();
/*  80 */     for (int i = 0; i < id.length(); i++) {
/*  81 */       user[i + 2] = byteArr[i];
/*     */     }
/*  83 */     return user;
/*     */   }
/*     */   
/*     */   public void get_field(byte[] M) {
/*  87 */     int nexti = 12, i = nexti;
/*  88 */     this.User_ID_field = new byte[M[nexti + 1] + 2]; int j;
/*  89 */     for (j = 0; j < M[nexti + 1] + 2; i++, j++) {
/*  90 */       this.User_ID_field[j] = M[nexti + j];
/*     */     }
/*  92 */     nexti = i;
/*     */     
/*  94 */     for (; nexti % 4 != 0; nexti++);
/*     */     
/*  96 */     this.Queue_Info_field = new byte[4];
/*  97 */     for (j = 0; j < 4; i++, j++) {
/*  98 */       this.Queue_Info_field[j] = M[nexti + j];
/*     */     }
/* 100 */     nexti = i;
/* 101 */     for (; nexti % 4 != 0; nexti++);
/* 102 */     this.Floor_Indicator_field = new byte[4];
/* 103 */     for (j = 0; j < 4; i++, j++) {
/* 104 */       this.Floor_Indicator_field[j] = M[nexti + j];
/*     */     }
/*     */   }
/*     */   
/*     */   public void get_User_ID_field(byte[] M) {
/* 109 */     if (M[0] != 6) {
/* 110 */       System.out.println("User_id_field type error");
/*     */     } else {
/*     */       
/* 113 */       byte[] Tmp = new byte[M[1]];
/* 114 */       int nexti = 2;
/* 115 */       for (int i = 0; i < M[1]; i++) {
/* 116 */         Tmp[i] = M[nexti + i];
/*     */       }
/* 118 */       this.User_id = new String(Tmp);
/* 119 */       System.out.println("user uri = " + this.User_id);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void get_Queue_info_field(byte[] M) {
/* 124 */     int check = Byte.toUnsignedInt(M[0]);
/* 125 */     if (check != 3) {
/* 126 */       System.out.println("Floor_Priority_field type error");
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 131 */       this.Position_info = Byte.toUnsignedInt(M[2]);
/*     */       
/* 133 */       this.Priority_int = Byte.toUnsignedInt(M[3]);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 138 */     this.Position_info = Byte.toUnsignedInt(M[2]);
/*     */     
/* 140 */     this.Priority_int = Byte.toUnsignedInt(M[3]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void get_Floor_Indicator_field(byte[] M) {
/* 146 */     if (M[0] != 13) {
/* 147 */       System.out.println("Floor_Indicator_field type error");
/*     */     } else {
/*     */       
/* 150 */       byte[] Tmp = new byte[4];
/* 151 */       int nexti = 2;
/* 152 */       for (int i = 0; i < 2; i++) {
/* 153 */         Tmp[i] = M[i + nexti];
/*     */       }
/* 155 */       ByteBuffer b = ByteBuffer.wrap(Tmp);
/* 156 */       this.Floor_Indicator_number = b.getShort();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse_all_field(byte[] data) {
/* 163 */     get_field(data);
/* 164 */     get_User_ID_field(this.User_ID_field);
/* 165 */     get_Queue_info_field(this.Queue_Info_field);
/* 166 */     get_Floor_Indicator_field(this.Floor_Indicator_field);
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\RTCP\rtcp_queue_position_info.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */