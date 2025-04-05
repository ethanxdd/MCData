/*     */ package sip;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Locale;
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
/*     */ public class SimpleSessionDescription
/*     */ {
/*  42 */   private final Fields mFields = new Fields("voscbtka");
/*  43 */   private final ArrayList<Media> mMedia = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleSessionDescription(String name, long sessionId, String address) {
/*  52 */     address = String.valueOf((address.indexOf(':') < 0) ? "IN IP4 " : "IN IP6 ") + address;
/*  53 */     this.mFields.parse("v=0");
/*  54 */     this.mFields.parse(String.format(Locale.US, "o=%s %d %d %s", new Object[] { name, Long.valueOf(sessionId), 
/*  55 */             Long.valueOf(System.currentTimeMillis()), address }));
/*  56 */     this.mFields.parse("s=-");
/*  57 */     this.mFields.parse("t=0 0");
/*  58 */     this.mFields.parse("c=" + address);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleSessionDescription(String message) {
/*  67 */     String[] lines = message.trim().replaceAll(" +", " ").split("[\r\n]+");
/*  68 */     Fields fields = this.mFields; byte b; int i;
/*     */     String[] arrayOfString1;
/*  70 */     for (i = (arrayOfString1 = lines).length, b = 0; b < i; ) { String line = arrayOfString1[b];
///*     */       try {
///*  72 */         if (line.charAt(1) != '=') {
///*  73 */           throw new IllegalArgumentException();
///*     */         }
///*  75 */         if (line.charAt(0) == 'm') {
///*  76 */           String[] parts = line.substring(2).split(" ", 4);
///*  77 */           String[] ports = parts[1].split("/", 2);
///*  78 */           Media media = newMedia(parts[0], Integer.parseInt(ports[0]), 
///*  79 */               (ports.length < 2) ? 1 : Integer.parseInt(ports[1]), 
///*  80 */               parts[2]); byte b1; int j; String[] arrayOfString2;
///*  81 */           for (j = (arrayOfString2 = parts[3].split(" ")).length, b1 = 0; b1 < j; ) { String format = arrayOfString2[b1];
///*  82 */             media.setFormat(format, null); b1++; }
///*     */           
///*  84 */           fields = media;
///*     */         } else {
///*  86 */           fields.parse(line);
///*     */         } 
///*  88 */       } catch (Exception e) {
///*  89 */         throw new IllegalArgumentException("Invalid SDP: " + line);
///*     */       } 
/*     */       b++; }
/*     */   
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
///*     */   public Media newMedia(String type, int port, int portCount, String protocol) {
///* 104 */     Media media = new Media(type, port, portCount, protocol, null);
///* 105 */     this.mMedia.add(media);
///* 106 */     return media;
///*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Media[] getMedia() {
/* 113 */     return this.mMedia.<Media>toArray(new Media[this.mMedia.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String encode() {
/* 122 */     StringBuilder buffer = new StringBuilder();
/* 123 */     this.mFields.write(buffer);
/* 124 */     for (Media media : this.mMedia) {
/* 125 */       media.write(buffer);
/*     */     }
/* 127 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAddress() {
/* 134 */     return this.mFields.getAddress();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAddress(String address) {
/* 142 */     this.mFields.setAddress(address);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEncryptionMethod() {
/* 149 */     return this.mFields.getEncryptionMethod();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEncryptionKey() {
/* 156 */     return this.mFields.getEncryptionKey();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncryption(String method, String key) {
/* 164 */     this.mFields.setEncryption(method, key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getBandwidthTypes() {
/* 171 */     return this.mFields.getBandwidthTypes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBandwidth(String type) {
/* 179 */     return this.mFields.getBandwidth(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBandwidth(String type, int value) {
/* 187 */     this.mFields.setBandwidth(type, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getAttributeNames() {
/* 194 */     return this.mFields.getAttributeNames();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAttribute(String name) {
/* 202 */     return this.mFields.getAttribute(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttribute(String name, String value) {
/* 211 */     this.mFields.setAttribute(name, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Media
/*     */     extends Fields
/*     */   {
/*     */     private final String mType;
/*     */     
/*     */     private final int mPort;
/*     */     
/*     */     private final int mPortCount;
/*     */     
/*     */     private final String mProtocol;
/*     */     
/* 226 */     private ArrayList<String> mFormats = new ArrayList<>();
/*     */     
/*     */     private Media(String type, int port, int portCount, String protocol) {
/* 229 */       super("icbka");
/* 230 */       this.mType = type;
/* 231 */       this.mPort = port;
/* 232 */       this.mPortCount = portCount;
/* 233 */       this.mProtocol = protocol;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getType() {
/* 240 */       return this.mType;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getPort() {
/* 247 */       return this.mPort;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getPortCount() {
/* 254 */       return this.mPortCount;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getProtocol() {
/* 261 */       return this.mProtocol;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getFormats() {
/* 268 */       return this.mFormats.<String>toArray(new String[this.mFormats.size()]);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getFmtp(String format) {
/* 276 */       return "";//get("a=fmtp:" + format, ' ');
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setFormat(String format, String fmtp) {
/* 284 */       this.mFormats.remove(format);
/* 285 */       this.mFormats.add(format);
///* 286 */       set("a=rtpmap:" + format, ' ', null);
///* 287 */       set("a=fmtp:" + format, ' ', fmtp);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void removeFormat(String format) {
/* 294 */       this.mFormats.remove(format);
///* 295 */       set("a=rtpmap:" + format, ' ', null);
///* 296 */       set("a=fmtp:" + format, ' ', null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int[] getRtpPayloadTypes() {
/* 303 */       int[] types = new int[this.mFormats.size()];
/* 304 */       int length = 0;
/* 305 */       for (String format : this.mFormats) {
/*     */         try {
/* 307 */           types[length] = Integer.parseInt(format);
/* 308 */           length++;
/* 309 */         } catch (NumberFormatException numberFormatException) {}
/*     */       } 
/* 311 */       return Arrays.copyOf(types, length);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getRtpmap(int type) {
///* 319 */       return get("a=rtpmap:" + type, ' ');
	return "";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getFmtp(int type) {
///* 327 */       return get("a=fmtp:" + type, ' ');
	return "";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setRtpPayload(int type, String rtpmap, String fmtp) {
/* 337 */       String format = String.valueOf(type);
/* 338 */       this.mFormats.remove(format);
/* 339 */       this.mFormats.add(format);
///* 340 */       set("a=rtpmap:" + format, ' ', rtpmap);
///* 341 */       set("a=fmtp:" + format, ' ', fmtp);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void removeRtpPayload(int type) {
/* 349 */       removeFormat(String.valueOf(type));
/*     */     }
/*     */     
/*     */     private void write(StringBuilder buffer) {
/* 353 */       buffer.append("m=").append(this.mType).append(' ').append(this.mPort);
/* 354 */       if (this.mPortCount != 1) {
/* 355 */         buffer.append('/').append(this.mPortCount);
/*     */       }
/* 357 */       buffer.append(' ').append(this.mProtocol);
/* 358 */       for (String format : this.mFormats) {
/* 359 */         buffer.append(' ').append(format);
/*     */       }
/* 361 */       buffer.append("\r\n");
/* 362 */       write(buffer);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class Fields
/*     */   {
/*     */     private final String mOrder;
/*     */ 
/*     */ 
/*     */     
/* 376 */     private final ArrayList<String> mLines = new ArrayList<>();
/*     */     
/*     */     Fields(String order) {
/* 379 */       this.mOrder = order;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getAddress() {
/* 386 */       String address = get("c", '=');
/* 387 */       if (address == null) {
/* 388 */         return null;
/*     */       }
/* 390 */       String[] parts = address.split(" ");
/* 391 */       if (parts.length != 3) {
/* 392 */         return null;
/*     */       }
/* 394 */       int slash = parts[2].indexOf('/');
/* 395 */       return (slash < 0) ? parts[2] : parts[2].substring(0, slash);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setAddress(String address) {
/* 403 */       if (address != null) {
/* 404 */         address = String.valueOf((address.indexOf(':') < 0) ? "IN IP4 " : "IN IP6 ") + 
/* 405 */           address;
/*     */       }
/* 407 */       set("c", '=', address);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getEncryptionMethod() {
/* 414 */       String encryption = get("k", '=');
/* 415 */       if (encryption == null) {
/* 416 */         return null;
/*     */       }
/* 418 */       int colon = encryption.indexOf(':');
/* 419 */       return (colon == -1) ? encryption : encryption.substring(0, colon);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getEncryptionKey() {
/* 426 */       String encryption = get("k", '=');
/* 427 */       if (encryption == null) {
/* 428 */         return null;
/*     */       }
/* 430 */       int colon = encryption.indexOf(':');
/* 431 */       return (colon == -1) ? null : encryption.substring(0, colon + 1);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setEncryption(String method, String key) {
/* 439 */       set("k", '=', (method == null || key == null) ? 
/* 440 */           method : (String.valueOf(method) + ':' + key));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getBandwidthTypes() {
/* 447 */       return cut("b=", ':');
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getBandwidth(String type) {
/* 455 */       String value = get("b=" + type, ':');
/* 456 */       if (value != null)
/*     */         try {
/* 458 */           return Integer.parseInt(value);
/* 459 */         } catch (NumberFormatException numberFormatException) {
/* 460 */           setBandwidth(type, -1);
/*     */         }  
/* 462 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setBandwidth(String type, int value) {
/* 470 */       set("b=" + type, ':', (value < 0) ? null : String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getAttributeNames() {
/* 477 */       return cut("a=", ':');
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getAttribute(String name) {
/* 485 */       return get("a=" + name, ':');
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setAttribute(String name, String value) {
/* 494 */       set("a=" + name, ':', value);
/*     */     }
/*     */     
/*     */     private void write(StringBuilder buffer) {
/* 498 */       for (int i = 0; i < this.mOrder.length(); i++) {
/* 499 */         char type = this.mOrder.charAt(i);
/* 500 */         for (String line : this.mLines) {
/* 501 */           if (line.charAt(0) == type) {
/* 502 */             buffer.append(line).append("\r\n");
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void parse(String line) {
/* 512 */       char type = line.charAt(0);
/* 513 */       if (this.mOrder.indexOf(type) == -1) {
/*     */         return;
/*     */       }
/* 516 */       char delimiter = '=';
/* 517 */       if (line.startsWith("a=rtpmap:") || line.startsWith("a=fmtp:")) {
/* 518 */         delimiter = ' ';
/* 519 */       } else if (type == 'b' || type == 'a') {
/* 520 */         delimiter = ':';
/*     */       } 
/* 522 */       int i = line.indexOf(delimiter);
/* 523 */       if (i == -1) {
/* 524 */         set(line, delimiter, "");
/*     */       } else {
/* 526 */         set(line.substring(0, i), delimiter, line.substring(i + 1));
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private String[] cut(String prefix, char delimiter) {
/* 534 */       String[] names = new String[this.mLines.size()];
/* 535 */       int length = 0;
/* 536 */       for (String line : this.mLines) {
/* 537 */         if (line.startsWith(prefix)) {
/* 538 */           int i = line.indexOf(delimiter);
/* 539 */           if (i == -1) {
/* 540 */             i = line.length();
/*     */           }
/* 542 */           names[length] = line.substring(prefix.length(), i);
/* 543 */           length++;
/*     */         } 
/*     */       } 
/* 546 */       return Arrays.<String>copyOf(names, length);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int find(String key, char delimiter) {
/* 553 */       int length = key.length();
/* 554 */       for (int i = this.mLines.size() - 1; i >= 0; i--) {
/* 555 */         String line = this.mLines.get(i);
/* 556 */         if (line.startsWith(key) && (line.length() == length || 
/* 557 */           line.charAt(length) == delimiter)) {
/* 558 */           return i;
/*     */         }
/*     */       } 
/* 561 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void set(String key, char delimiter, String value) {
/* 569 */       int index = find(key, delimiter);
/* 570 */       if (value != null) {
/* 571 */         if (value.equals("inactive")) {
/* 572 */           this.mLines.add(key);
/*     */           return;
/*     */         } 
/* 575 */         if (value.length() != 0) {
/* 576 */           key = String.valueOf(key) + delimiter + value;
/*     */         }
/* 578 */         if (index == -1) {
/* 579 */           this.mLines.add(key);
/*     */         } else {
/* 581 */           this.mLines.set(index, key);
/*     */         } 
/* 583 */       } else if (index != -1) {
/* 584 */         this.mLines.remove(index);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private String get(String key, char delimiter) {
/* 593 */       int index = find(key, delimiter);
/* 594 */       if (index == -1) {
/* 595 */         return null;
/*     */       }
/* 597 */       String line = this.mLines.get(index);
/* 598 */       int length = key.length();
/* 599 */       return (line.length() == length) ? "" : line.substring(length + 1);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\sip\SimpleSessionDescription.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */