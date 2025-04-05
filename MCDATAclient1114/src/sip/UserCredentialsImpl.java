/*    */ package sip;
/*    */ 
/*    */ import gov.nist.javax.sip.clientauthutils.UserCredentials;
/*    */ 
/*    */ public class UserCredentialsImpl implements UserCredentials {
/*    */   private String userName;
/*    */   private String sipDomain;
/*    */   private String password;
/*    */   
/*    */   public UserCredentialsImpl(String userName, String sipDomain, String password) {
/* 11 */     this.userName = userName;
/* 12 */     this.sipDomain = sipDomain;
/* 13 */     this.password = password;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getPassword() {
/* 18 */     return this.password;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSipDomain() {
/* 23 */     return this.sipDomain;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getUserName() {
/* 29 */     return this.userName;
/*    */   }
/*    */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\sip\UserCredentialsImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */