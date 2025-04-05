/*     */ package RTCP;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
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
/*     */ public class TimeStamp
/*     */   implements Serializable, Comparable<TimeStamp>
/*     */ {
/*     */   private static final long serialVersionUID = 8139806907588338737L;
/*     */   protected static final long msb0baseTime = 2085978496000L;
/*     */   protected static final long msb1baseTime = -2208988800000L;
/*     */   public static final String NTP_DATE_FORMAT = "EEE, MMM dd yyyy HH:mm:ss.SSS";
/*     */   private final long ntpTime;
/*     */   private DateFormat simpleFormatter;
/*     */   private DateFormat utcFormatter;
/*     */   
/*     */   public TimeStamp(long ntpTime) {
/*  94 */     this.ntpTime = ntpTime;
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
/*     */   public TimeStamp(String hexStamp) throws NumberFormatException {
/* 107 */     this.ntpTime = decodeNtpHexString(hexStamp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeStamp(Date d) {
/* 118 */     this.ntpTime = (d == null) ? 0L : toNtpTime(d.getTime());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long ntpValue() {
/* 128 */     return this.ntpTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getSeconds() {
/* 138 */     return this.ntpTime >>> 32L & 0xFFFFFFFFL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getFraction() {
/* 148 */     return this.ntpTime & 0xFFFFFFFFL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getTime() {
/* 158 */     return getTime(this.ntpTime);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getDate() {
/* 168 */     long time = getTime(this.ntpTime);
/* 169 */     return new Date(time);
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
/*     */   public static long getTime(long ntpTimeValue) {
/* 187 */     long seconds = ntpTimeValue >>> 32L & 0xFFFFFFFFL;
/* 188 */     long fraction = ntpTimeValue & 0xFFFFFFFFL;
/*     */ 
/*     */     
/* 191 */     fraction = Math.round(1000.0D * fraction / 4.294967296E9D);
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
/* 202 */     long msb = seconds & 0x80000000L;
/* 203 */     if (msb == 0L)
/*     */     {
/* 205 */       return 2085978496000L + seconds * 1000L + fraction;
/*     */     }
/*     */     
/* 208 */     return -2208988800000L + seconds * 1000L + fraction;
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
/*     */   public static TimeStamp getNtpTime(long date) {
/* 224 */     return new TimeStamp(toNtpTime(date));
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
/*     */   public static TimeStamp getCurrentTime() {
/* 236 */     return getNtpTime(System.currentTimeMillis());
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
/*     */   protected static long decodeNtpHexString(String hexString) throws NumberFormatException {
/* 250 */     if (hexString == null) {
/* 251 */       throw new NumberFormatException("null");
/*     */     }
/* 253 */     int ind = hexString.indexOf('.');
/* 254 */     if (ind == -1) {
/* 255 */       if (hexString.length() == 0) {
/* 256 */         return 0L;
/*     */       }
/* 258 */       return Long.parseLong(hexString, 16) << 32L;
/*     */     } 
/*     */     
/* 261 */     return Long.parseLong(hexString.substring(0, ind), 16) << 32L | 
/* 262 */       Long.parseLong(hexString.substring(ind + 1), 16);
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
/*     */   public static TimeStamp parseNtpString(String s) throws NumberFormatException {
/* 276 */     return new TimeStamp(decodeNtpHexString(s));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static long toNtpTime(long t) {
/*     */     long baseTime;
/* 287 */     boolean useBase1 = (t < 2085978496000L);
/*     */     
/* 289 */     if (useBase1) {
/* 290 */       baseTime = t - -2208988800000L;
/*     */     } else {
/*     */       
/* 293 */       baseTime = t - 2085978496000L;
/*     */     } 
/*     */     
/* 296 */     long seconds = baseTime / 1000L;
/* 297 */     long fraction = baseTime % 1000L * 4294967296L / 1000L;
/*     */     
/* 299 */     if (useBase1) {
/* 300 */       seconds |= 0x80000000L;
/*     */     }
/*     */     
/* 303 */     long time = seconds << 32L | fraction;
/* 304 */     return time;
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
/*     */   public int hashCode() {
/* 321 */     return (int)(this.ntpTime ^ this.ntpTime >>> 32L);
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
/*     */   public boolean equals(Object obj) {
/* 337 */     if (obj instanceof TimeStamp) {
/* 338 */       return (this.ntpTime == ((TimeStamp)obj).ntpValue());
/*     */     }
/* 340 */     return false;
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
/*     */   public String toString() {
/* 355 */     return toString(this.ntpTime);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void appendHexString(StringBuilder buf, long l) {
/* 366 */     String s = Long.toHexString(l);
/* 367 */     for (int i = s.length(); i < 8; i++) {
/* 368 */       buf.append('0');
/*     */     }
/* 370 */     buf.append(s);
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
/*     */   public static String toString(long ntpTime) {
/* 385 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 387 */     appendHexString(buf, ntpTime >>> 32L & 0xFFFFFFFFL);
/*     */ 
/*     */     
/* 390 */     buf.append('.');
/* 391 */     appendHexString(buf, ntpTime & 0xFFFFFFFFL);
/*     */     
/* 393 */     return buf.toString();
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
/*     */   public String toDateString() {
/* 407 */     if (this.simpleFormatter == null) {
/* 408 */       this.simpleFormatter = new SimpleDateFormat("EEE, MMM dd yyyy HH:mm:ss.SSS", Locale.US);
/* 409 */       this.simpleFormatter.setTimeZone(TimeZone.getDefault());
/*     */     } 
/* 411 */     Date ntpDate = getDate();
/* 412 */     return this.simpleFormatter.format(ntpDate);
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
/*     */   public String toUTCString() {
/* 426 */     if (this.utcFormatter == null) {
/* 427 */       this.utcFormatter = new SimpleDateFormat("EEE, MMM dd yyyy HH:mm:ss.SSS 'UTC'", 
/* 428 */           Locale.US);
/* 429 */       this.utcFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
/*     */     } 
/* 431 */     Date ntpDate = getDate();
/* 432 */     return this.utcFormatter.format(ntpDate);
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
/*     */   public int compareTo(TimeStamp anotherTimeStamp) {
/* 449 */     long thisVal = this.ntpTime;
/* 450 */     long anotherVal = anotherTimeStamp.ntpTime;
/* 451 */     return (thisVal < anotherVal) ? -1 : ((thisVal == anotherVal) ? 0 : 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\RTCP\TimeStamp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */