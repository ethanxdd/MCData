/*    */ package sipMessageHandle;
/*    */ 
/*    */ import gov.nist.core.Host;
/*    */ import gov.nist.javax.sip.Utils;
/*    */ import gov.nist.javax.sip.address.Authority;
/*    */ import gov.nist.javax.sip.address.SipUri;
/*    */ import java.util.Random;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SipMessageUtils
/*    */ {
/*    */   public static String generateNewTag() {
/* 15 */     Random random = new Random();
/* 16 */     String tag = "";
/* 17 */     for (int i = 0; i < 8; i++) {
/* 18 */       if (random.nextInt(2) == 0) {
/* 19 */         tag = String.valueOf(tag) + random.nextInt(10);
/*    */       } else {
/* 21 */         int num = random.nextInt(2);
/* 22 */         if (num == 1) {
/* 23 */           char[] character = { (char)(int)(Math.random() * 26.0D + 65.0D) };
/* 24 */           tag = String.valueOf(tag) + new String(character);
/*    */         } else {
/* 26 */           char[] character = { (char)(int)(Math.random() * 26.0D + 97.0D) };
/* 27 */           tag = String.valueOf(tag) + new String(character);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 34 */     return tag;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String generateNewBranchCode() {
/* 53 */     String result = Utils.getInstance().generateBranchId();
/*    */     
/* 55 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static SipUri createSipUri(String sipUserName, String sipDomain, int port, String password) {
/* 66 */     port = -1;
/* 67 */     SipUri sipUri = new SipUri();
/* 68 */     Host host = new Host(sipDomain);
/* 69 */     Authority a = createAuthority(sipUserName, host, port, password);
/* 70 */     sipUri.setAuthority(a);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 77 */     return sipUri;
/*    */   }
/*    */   
/*    */   public static Authority createAuthority(String sipUserName, Host host, int port, String password) {
/* 81 */     Authority authority = new Authority();
/* 82 */     if (sipUserName != null) {
/* 83 */       authority.setUser(sipUserName);
/*    */     }
/* 85 */     authority.setHost(host);
/* 86 */     if (password != null) {
/* 87 */       authority.setPassword(password);
/*    */     }
/* 89 */     if (port != -1) {
/* 90 */       authority.setPort(port);
/*    */     }
/* 92 */     return authority;
/*    */   }
/*    */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\sipMessageHandle\SipMessageUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */