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
/*     */ public class rtcp_idle
/*     */   extends Rtcp
/*     */ {
/*     */   public byte[] message;
/*     */   public byte[] Message_Sequence_Number_field;
/*     */   public byte[] Floor_Indicator_field;
/*     */   public short Message_Sequence_id;
/*     */   public short Floor_ind;
/*     */   
/*     */   public rtcp_idle() {}
/*     */   
/*     */   public rtcp_idle(long SSRC_ID, short message_sequence_id, short indicator) {
/*  25 */     message_tools tool = new message_tools();
/*     */     
/*  27 */     this.header = messageheader("idle", SSRC_ID, "MCPT");
/*     */     
/*  29 */     this.Message_Sequence_Number_field = new byte[4];
/*  30 */     this.Message_Sequence_Number_field = set_Message_Sequence_Number_field(message_sequence_id);
/*     */     
/*  32 */     this.Floor_Indicator_field = new byte[4];
/*  33 */     this.Floor_Indicator_field = set_Floor_Indicator_field(indicator);
/*     */ 
/*     */     
/*  36 */     this.message = 
/*     */       
/*  38 */       new byte[this.header.length + this.Message_Sequence_Number_field.length + this.Floor_Indicator_field.length];
/*     */     
/*  40 */     tool.length_transf(this.header, this.message.length);
/*     */     
/*  42 */     writemessage();
/*     */   }
/*     */ 
/*     */   
/*     */   public void writemessage() {
/*     */     int i;
/*  48 */     for (i = 0; i < this.header.length; i++)
/*  49 */       this.message[i] = this.header[i];  int j;
/*  50 */     for (i = this.header.length, j = 0; j < this.Message_Sequence_Number_field.length; j++)
/*  51 */       this.message[i + j] = this.Message_Sequence_Number_field[j]; 
/*  52 */     for (i = this.header.length + this.Message_Sequence_Number_field.length, j = 0; j < this.Floor_Indicator_field.length; j++)
/*  53 */       this.message[i + j] = this.Floor_Indicator_field[j]; 
/*     */   }
/*     */   
/*     */   public void get_field(byte[] M) {
/*  57 */     int nexti = 12, i = nexti;
/*     */     
/*  59 */     this.Message_Sequence_Number_field = new byte[M[nexti + 1] + 2]; int j;
/*  60 */     for (j = 0; j < M[nexti + 1] + 2; i++, j++) {
/*  61 */       this.Message_Sequence_Number_field[j] = M[nexti + j];
/*     */     }
/*  63 */     nexti = i;
/*     */     
/*  65 */     for (; nexti % 4 != 0; nexti++);
/*     */     
/*  67 */     this.Floor_Indicator_field = new byte[M[nexti + 1] + 2];
/*  68 */     for (j = 0; j < M[nexti + 1] + 2; i++, j++) {
/*  69 */       this.Floor_Indicator_field[j] = M[nexti + j];
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void get_Message_Sequence_Number_field(byte[] M) {
/*  75 */     if (M[0] != 8) {
/*  76 */       System.out.println("Message_Sequence_Number_field type error");
/*     */     } else {
/*     */       
/*  79 */       byte[] Tmp = new byte[4];
/*  80 */       int nexti = 2;
/*  81 */       for (int i = 0; i < M[1]; i++) {
/*  82 */         Tmp[i] = M[nexti + i];
/*     */       }
/*  84 */       ByteBuffer b = ByteBuffer.wrap(Tmp);
/*  85 */       this.Message_Sequence_id = b.getShort();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void get_Floor_Indicator_field(byte[] M) {
/*  93 */     if (M[0] != 13) {
/*  94 */       System.out.println("Floor_Indicator_field type error");
/*     */     } else {
/*     */       
/*  97 */       byte[] Tmp = new byte[4];
/*  98 */       int nexti = 2;
/*  99 */       for (int i = 0; i < 2; i++) {
/* 100 */         Tmp[i] = M[i + nexti];
/*     */       }
/* 102 */       ByteBuffer b = ByteBuffer.wrap(Tmp);
/* 103 */       this.Floor_ind = b.getShort();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void parse_all_field(byte[] data) {
/* 109 */     get_field(data);
/* 110 */     get_Floor_Indicator_field(this.Floor_Indicator_field);
/* 111 */     get_Message_Sequence_Number_field(this.Message_Sequence_Number_field);
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\RTCP\rtcp_idle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */