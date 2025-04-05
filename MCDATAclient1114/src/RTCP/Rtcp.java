/*     */ package RTCP;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ 
/*     */ public class Rtcp
/*     */ {
/*     */   public byte[] header;
/*     */   public byte[] databuffer;
/*     */   
/*     */   enum rtcp_types {
/*  13 */     connect, disconnect, acknowledgement, granted, taken, ack, deny, request, release, idle, revoke, queue_position_request, queue_position_info;
/*     */   }
/*     */   
/*  16 */   public byte connect = -112;
/*  17 */   public byte acknowledgement = -126;
/*  18 */   public byte disconnect = -111;
/*  19 */   public byte granted = -127;
/*  20 */   public byte taken = -126;
/*  21 */   public byte ack = -118;
/*  22 */   public byte deny = -125;
/*  23 */   public byte request = Byte.MIN_VALUE;
/*  24 */   public byte release = -124;
/*  25 */   public byte idle = -123;
/*  26 */   public byte revoke = -122;
/*  27 */   public byte queue_position_request = -120;
/*  28 */   public byte queue_position_info = -119;
/*  29 */   public byte unknown = 0;
/*     */   
/*     */   public static final int RTCP_UNKNOWN = -100;
/*     */   
/*     */   public static final int RTCP_MCPC_CONNECT = 16;
/*     */   
/*     */   public static final int RTCP_MCPC_DISCONNECT = 17;
/*     */   
/*     */   public static final int RTCP_MCPC_ACK = 2;
/*     */   public static final int RTCP_MCPT_REQUEST = 0;
/*     */   public static final int RTCP_MCPT_GRANTED = 1;
/*     */   public static final int RTCP_MCPT_TAKEN = 2;
/*     */   public static final int RTCP_MCPT_DENY = 3;
/*     */   public static final int RTCP_MCPT_RELEASE = 4;
/*     */   public static final int RTCP_MCPT_IDLE = 5;
/*     */   public static final int RTCP_MCPT_REVOKE = 6;
/*     */   public static final int RTCP_MCPT_QUEUE_POSITION_REQUEST = 8;
/*     */   public static final int RTCP_MCPT_QUEUE_POSITION_INFO = 9;
/*     */   public static final int RTCP_MCPT_ACK = 10;
/*  48 */   public int ptt_state = -100;
/*     */   
/*     */   public static final int DENY_REASON_ANOTHER_HAS_PERMISSION = 1;
/*     */   
/*     */   public static final int DENY_REASON_INTERNAL_ERROR = 2;
/*     */   
/*     */   public static final int DENY_REASON_ONLY_ONE_PARTICIPANT = 3;
/*     */   
/*     */   public static final int DENY_REASON_RETRY_AFTER_TIMER_HAS_NOT_EXPIRED = 4;
/*     */   public static final int DENY_REASON_RECEIVE_ONLY = 5;
/*     */   public static final int DENY_REASON_NO_RESOURCE_AVAILABLE = 6;
/*     */   public static final int DENY_REASON_QUEUE_FULL = 7;
/*     */   public static final int REVOKE_REASON_ONLY_ONE_CLIENT = 1;
/*     */   public static final int REVOKE_REASON_MEDIA_BURST_TOO_LONG = 2;
/*     */   public static final int REVOKE_REASON_NO_PERMISSION = 3;
/*     */   public static final int REVOKE_REASON_PREEMPTED = 4;
/*     */   public static final int REVOKE_REASON_NO_RESOURCE_AVAILABLE = 6;
/*     */   public static final int REVOKE_REASON_OTHER_REASON = 255;
/*     */   public byte V2;
/*     */   public byte P;
/*     */   public byte subtype;
/*     */   public short pt_app;
/*     */   public byte[] length_field;
/*     */   public short Message_length;
/*     */   public long SSRC;
/*     */   public String name;
/*     */   public byte[] msgbuf;
/*  75 */   private static short rtcp_sequence_num = 1;
/*     */ 
/*     */ 
/*     */   
/*     */   public Rtcp() {
/*  80 */     init();
/*     */   }
/*     */   
/*     */   private void init() {
/*  84 */     this.V2 = 0;
/*  85 */     this.P = 0;
/*  86 */     this.pt_app = 0;
/*  87 */     this.SSRC = gen_ssrc().longValue();
/*  88 */     this.ptt_state = this.unknown;
/*     */   }
/*     */   
/*     */   public Long gen_ssrc() {
/*  92 */     long ssrc = (long)Math.random();
/*  93 */     Random rand = new Random();
/*  94 */     ssrc = rand.nextLong();
/*  95 */     if (ssrc < 0L)
/*  96 */       ssrc *= -1L; 
/*  97 */     return Long.valueOf(ssrc);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] messageheader(String ms_type, long SSRC_uid, String name) {
/* 103 */     this.header = new byte[12];
/* 104 */     set_typeinfo(ms_type);
/* 105 */     this.header[1] = -52;
/*     */     
/* 107 */     set_header_SSRC_field(SSRC_uid);
/*     */     
/* 109 */     set_name(name);
/* 110 */     return this.header;
/*     */   }
/*     */   
/*     */   public void set_header_SSRC_field(long id) {
/* 114 */     byte[] SSRC = set_SSRC(id);
/*     */     
/* 116 */     for (int i = 0; i < 4; ) { this.header[4 + i] = SSRC[i]; i++; }
/*     */   
/*     */   }
/*     */   public byte[] set_SSRC(long id) {
/* 120 */     byte[] SSRC = new byte[4];
/* 121 */     SSRC[0] = (byte)(int)(id >> 24L & 0xFFL);
/* 122 */     SSRC[1] = (byte)(int)(id >> 16L & 0xFFL);
/* 123 */     SSRC[2] = (byte)(int)(id >> 8L & 0xFFL);
/* 124 */     SSRC[3] = (byte)(int)(id >> 0L & 0xFFL);
/*     */ 
/*     */ 
/*     */     
/* 128 */     return SSRC;
/*     */   }
/*     */   
/*     */   public void set_name(String str) {
/* 132 */     byte[] info = str.getBytes();
/* 133 */     for (int i = 0; i < 4; ) { this.header[8 + i] = info[i]; i++; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set_typeinfo(String str) {
/* 141 */     rtcp_types type = rtcp_types.valueOf(str);
/* 142 */     switch (type) {
/*     */       case connect:
/* 144 */         this.header[0] = this.connect;
/*     */         break;
/*     */       case granted:
/* 147 */         this.header[0] = this.granted;
/*     */         break;
/*     */       case taken:
/* 150 */         this.header[0] = this.taken;
/*     */         break;
///*     */       case null:
///* 153 */         this.header[0] = this.ack;
///*     */         break;
/*     */       case acknowledgement:
/* 156 */         this.header[0] = this.acknowledgement;
/*     */         break;
/*     */       case disconnect:
/* 159 */         this.header[0] = this.disconnect;
/*     */         break;
/*     */       case deny:
/* 162 */         this.header[0] = this.deny;
/*     */         break;
/*     */       case request:
/* 165 */         this.header[0] = this.request;
/*     */         break;
/*     */       case release:
/* 168 */         this.header[0] = this.release;
/*     */         break;
/*     */       case idle:
/* 171 */         this.header[0] = this.idle;
/*     */         break;
/*     */       case revoke:
/* 174 */         this.header[0] = this.revoke;
/*     */         break;
/*     */       case queue_position_request:
/* 177 */         this.header[0] = this.queue_position_request;
/*     */         break;
/*     */       case queue_position_info:
/* 180 */         this.header[0] = this.queue_position_info;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void Rtcp_parse(byte[] Data) {
/* 186 */     this.msgbuf = new byte[2048];
/*     */     
/* 188 */     System.arraycopy(Data, 0, this.msgbuf, 0, Math.min(Data.length, this.msgbuf.length));
/* 189 */     get_header(Data);
/* 190 */     if (this.name.equals("MCPC")) {
/*     */       
/* 192 */       switch (this.subtype) {
/*     */         
/*     */         case 16:
/* 195 */           this.ptt_state = 16;
/*     */           return;
/*     */         
/*     */         case 17:
/* 199 */           this.ptt_state = 17;
/*     */           return;
/*     */         
/*     */         case 2:
/* 203 */           this.ptt_state = 2;
/*     */           return;
/*     */       } 
/*     */       
/* 207 */       System.out.println("\nV: ERROR state.\n");
/*     */ 
/*     */     
/*     */     }
/* 211 */     else if (this.name.equals("MCPT")) {
/*     */       
/* 213 */       switch (this.subtype) {
/*     */         
/*     */         case 0:
/* 216 */           this.ptt_state = 0;
/*     */           return;
/*     */         
/*     */         case 1:
/* 220 */           this.ptt_state = 1;
/*     */           return;
/*     */         
/*     */         case 2:
/* 224 */           this.ptt_state = 2;
/*     */           return;
/*     */         
/*     */         case 3:
/* 228 */           this.ptt_state = 3;
/*     */           return;
/*     */         
/*     */         case 4:
/* 232 */           this.ptt_state = 4;
/*     */           return;
/*     */         
/*     */         case 5:
/* 236 */           this.ptt_state = 5;
/*     */           return;
/*     */         
/*     */         case 6:
/* 240 */           this.ptt_state = 6;
/*     */           return;
/*     */         
/*     */         case 8:
/* 244 */           this.ptt_state = 8;
/*     */           return;
/*     */         
/*     */         case 9:
/* 248 */           this.ptt_state = 9;
/*     */           return;
/*     */         
/*     */         case 10:
/* 252 */           this.ptt_state = 10;
/*     */           return;
/*     */       } 
/*     */       
/* 256 */       System.out.println("\nV: ERROR state.\n");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void get_header(byte[] message) {
/* 266 */     byte t = message[0];
/*     */     
/* 268 */     byte[] Tmp = new byte[4];
/* 269 */     byte tmp = (byte)(t >> 6);
/* 270 */     tmp = (byte)(tmp & 0x3);
/* 271 */     this.V2 = tmp;
/*     */     
/* 273 */     tmp = (byte)(t >> 5);
/* 274 */     tmp = (byte)(tmp & 0x1);
/* 275 */     this.P = tmp;
/* 276 */     t = (byte)(t & 0x1F);
/* 277 */     this.subtype = t;
/* 278 */     this.pt_app = byteArrayToShort((byte)0, message[1]);
/*     */     
/* 280 */     this.Message_length = byteArrayToShort(message[2], message[3]);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 285 */     for (int i = 0; i < 4; i++) {
/* 286 */       Tmp[i] = message[4 + i];
/*     */     }
/*     */ 
/*     */     
/* 290 */     byte[] body = new byte[8];
/* 291 */     byte[] SSRC_body = new byte[4];
/* 292 */     byte[] name_body = new byte[4];
/* 293 */     System.arraycopy(message, 4, body, 0, 8);
/* 294 */     System.arraycopy(body, 4, name_body, 0, 4);
/* 295 */     this.SSRC = (body[0] & 0xFF);
/* 296 */     this.SSRC |= (body[1] << 8 & 0xFF00);
/* 297 */     this.SSRC |= (body[2] << 16 & 0xFF0000);
/* 298 */     this.SSRC |= (body[3] << 24 & 0xFF000000);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 304 */     this.name = new String(name_body);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static short byteArrayToShort(byte l1, byte l2) {
/* 310 */     return (short)(l2 & 0xFF | (l1 & 0xFF) << 8);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] set_Invite_MCU_field(String id) {
/* 316 */     int buffer = id.length() + 2;
/* 317 */     for (; buffer % 4 != 0; buffer++);
/* 318 */     byte[] MCU = new byte[buffer];
/* 319 */     byte[] byteArr = id.getBytes();
/* 320 */     MCU[0] = 5;
/* 321 */     MCU[1] = (byte)id.length();
/* 322 */     for (int i = 0; i < id.length(); i++) {
/* 323 */       MCU[i + 2] = byteArr[i];
/*     */     }
/* 325 */     return MCU;
/*     */   }
/*     */   
/*     */   public byte[] set_Session_id_field(String id) {
/* 329 */     int buffer = id.length() + 3;
/* 330 */     for (; buffer % 4 != 0; buffer++);
/* 331 */     byte[] Session_id = new byte[buffer];
/* 332 */     byte[] byteArr = id.getBytes();
/* 333 */     Session_id[0] = 1;
/* 334 */     Session_id[1] = (byte)id.length();
/* 335 */     Session_id[2] = 3;
/* 336 */     for (int i = 0; i < id.length(); i++) {
/* 337 */       Session_id[i + 3] = byteArr[i];
/*     */     }
/* 339 */     return Session_id;
/*     */   }
/*     */   
/*     */   public byte[] set_Media_Streams_field(int media, int channel) {
/* 343 */     int buffer = 4;
/* 344 */     byte[] Media_Streams = new byte[buffer];
/* 345 */     Media_Streams[0] = 0;
/* 346 */     Media_Streams[1] = 2;
/* 347 */     Media_Streams[2] = (byte)media;
/* 348 */     Media_Streams[3] = (byte)channel;
/* 349 */     return Media_Streams;
/*     */   }
/*     */   
/*     */   public byte[] set_Answer_state_field(int answer) {
/* 353 */     int buffer = 4;
/* 354 */     byte[] Answer_state = new byte[buffer];
/* 355 */     Answer_state[0] = 4;
/* 356 */     Answer_state[1] = 2;
/* 357 */     Answer_state[2] = 0;
/* 358 */     Answer_state[3] = (byte)answer;
/* 359 */     return Answer_state;
/*     */   }
/*     */   
/*     */   public byte[] set_Source_field(int source) {
/* 363 */     int buffer = 4;
/* 364 */     byte[] Source = new byte[buffer];
/* 365 */     Source[0] = 10;
/* 366 */     Source[1] = 2;
/* 367 */     Source[2] = 0;
/* 368 */     Source[3] = (byte)source;
/* 369 */     return Source;
/*     */   }
/*     */   
/*     */   public byte[] set_Message_Type_id_field() {
/* 373 */     int buffer = 4;
/* 374 */     byte[] MsT = new byte[buffer];
/* 375 */     MsT[0] = 12;
/* 376 */     MsT[1] = 2;
/* 377 */     MsT[2] = 10;
/* 378 */     MsT[3] = 0;
/* 379 */     return MsT;
/*     */   }
/*     */   
/*     */   public byte[] set_Granted_Party_id_field(String id) {
/* 383 */     int buffer = id.length() + 2;
/* 384 */     for (; buffer % 4 != 0; buffer++);
/* 385 */     byte[] grant = new byte[buffer];
/* 386 */     byte[] byteArr = id.getBytes();
/* 387 */     grant[0] = 4;
/* 388 */     grant[1] = (byte)id.length();
/* 389 */     for (int i = 0; i < id.length(); i++) {
/* 390 */       grant[i + 2] = byteArr[i];
/*     */     }
/* 392 */     return grant;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] set_Permission_to_Request_field(int permission) {
/* 397 */     int buffer = 4;
/* 398 */     byte[] P2r = new byte[buffer];
/* 399 */     P2r[0] = 5;
/* 400 */     P2r[1] = 2;
/* 401 */     P2r[2] = 0;
/* 402 */     P2r[3] = (byte)permission;
/* 403 */     return P2r;
/*     */   }
/*     */   
/*     */   public byte[] set_Message_Sequence_Number_field(short num) {
/* 407 */     int buffer = 4;
/* 408 */     ByteBuffer b = ByteBuffer.allocate(2);
/* 409 */     b.putShort(num);
/* 410 */     byte[] byteArr = b.array();
/* 411 */     byte[] MSn = new byte[buffer];
/* 412 */     MSn[0] = 8;
/* 413 */     MSn[1] = 2;
/* 414 */     MSn[2] = byteArr[0];
/* 415 */     MSn[3] = byteArr[1];
/* 416 */     return MSn;
/*     */   }
/*     */   
/*     */   public byte[] set_Floor_Indicator_field(short type) {
/* 420 */     int buffer = 4;
/* 421 */     ByteBuffer b = ByteBuffer.allocate(2);
/* 422 */     b.putShort(type);
/* 423 */     byte[] byteArr = b.array();
/* 424 */     byte[] MSn = new byte[buffer];
/* 425 */     MSn[0] = 13;
/* 426 */     MSn[1] = 2;
/* 427 */     MSn[2] = byteArr[0];
/* 428 */     MSn[3] = byteArr[1];
/* 429 */     return MSn;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] set_SSRC_field(long id) {
/* 434 */     int buffer = 8;
/* 435 */     byte[] SSRC = new byte[buffer];
/* 436 */     SSRC[0] = 14;
/* 437 */     SSRC[1] = 4;
/*     */     
/* 439 */     SSRC[2] = (byte)(int)(id >> 24L & 0xFFL);
/* 440 */     SSRC[3] = (byte)(int)(id >> 16L & 0xFFL);
/* 441 */     SSRC[4] = (byte)(int)(id >> 8L & 0xFFL);
/* 442 */     SSRC[5] = (byte)(int)(id >> 0L & 0xFFL);
/*     */ 
/*     */     
/* 445 */     return SSRC;
/*     */   }
/*     */   public byte[] longToBytes(long x) {
/* 448 */     ByteBuffer buffer = ByteBuffer.allocate(64);
/* 449 */     buffer.putLong(x);
/* 450 */     System.out.println(buffer);
/* 451 */     return buffer.array();
/*     */   }
/*     */   public static byte[] getBitsArray(byte b) {
/* 454 */     byte[] array = new byte[8];
/* 455 */     for (int i = 7; i >= 0; i--) {
/* 456 */       array[i] = (byte)(b & 0x1);
/* 457 */       b = (byte)(b >> 1);
/*     */     } 
/* 459 */     return array;
/*     */   }
/*     */   
/*     */   public byte[] set_Floor_Priority_field(int priority) {
/* 463 */     int buffer = 4;
/* 464 */     byte[] FP = new byte[buffer];
/* 465 */     FP[0] = 0;
/* 466 */     FP[1] = 2;
/* 467 */     FP[2] = (byte)priority;
/* 468 */     FP[3] = 0;
/* 469 */     return FP;
/*     */   }
/*     */   
/*     */   public byte[] set_Duration_field(short time) {
/* 473 */     int buffer = 4;
/* 474 */     byte[] Df = new byte[buffer];
/* 475 */     ByteBuffer b = ByteBuffer.allocate(2);
/* 476 */     b.putShort(time);
/* 477 */     byte[] byteArr = b.array();
/* 478 */     Df[0] = 1;
/* 479 */     Df[1] = 2;
/* 480 */     for (int i = 0; i < 2; i++) {
/* 481 */       Df[i + 2] = byteArr[i];
/*     */     }
/* 483 */     return Df;
/*     */   }
/*     */   
/*     */   public void set_Queued_User_id_buffer(List<String> send_uri, byte[] Queued_User_id_field) {
/* 487 */     int total = 0;
/* 488 */     int length = 0;
/* 489 */     int buffer = 0;
/* 490 */     for (String str : send_uri) {
/* 491 */       total++;
/* 492 */       length += str.length();
/*     */     } 
/* 494 */     buffer = length + total + 3;
/* 495 */     for (; buffer % 4 != 0; buffer++);
/* 496 */     Queued_User_id_field = new byte[buffer];
/*     */   }
/*     */   
/*     */   public byte[] set_Queued_User_id_field(List<String> send_uri) {
/* 500 */     int total = 0;
/* 501 */     int leng = 0;
/* 502 */     int buffer = 0;
/* 503 */     for (String str : send_uri) {
/* 504 */       total++;
/* 505 */       leng += str.length();
/*     */     } 
/* 507 */     buffer = leng + total + 3;
/* 508 */     for (; buffer % 4 != 0; buffer++);
/* 509 */     byte[] Queued_id = new byte[buffer];
/* 510 */     Queued_id[0] = 2;
/* 511 */     Queued_id[1] = (byte)buffer;
/* 512 */     Queued_id[2] = (byte)total;
/* 513 */     int i = 3;
/* 514 */     for (String str : send_uri) {
/* 515 */       int limit = i + str.length();
/* 516 */       byte[] byteArr = str.getBytes();
/* 517 */       for (; i < limit; i = i + str.length() + 1) {
/* 518 */         Queued_id[i] = (byte)str.length();
/* 519 */         for (int j = 0; j < str.length(); j++) {
/* 520 */           Queued_id[i + j + 1] = byteArr[j];
/*     */         }
/*     */       } 
/*     */     } 
/* 524 */     return Queued_id;
/*     */   }
/*     */   public static void increaseSequenceNum() {
/* 527 */     rtcp_sequence_num = (short)(rtcp_sequence_num + 1);
/* 528 */     if (rtcp_sequence_num == 0)
/* 529 */       rtcp_sequence_num = 1; 
/*     */   }
/*     */   
/*     */   public static short get_sequence_num() {
/* 533 */     return rtcp_sequence_num;
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\RTCP\Rtcp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */