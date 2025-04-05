/*    */ package sip;
/*    */ 
/*    */ import gov.nist.javax.sip.clientauthutils.AccountManager;
/*    */ import gov.nist.javax.sip.clientauthutils.UserCredentials;
/*    */ import javax.sip.ClientTransaction;
/*    */ 
/*    */ 
/*    */ public class AccountManagerImpl
/*    */   implements AccountManager
/*    */ {
/*    */   private String username;
/*    */   private String sipDomain;
/*    */   private String password;
/*    */   
/*    */   public AccountManagerImpl(String username, String sipDomain, String password) {
/* 16 */     this.username = username;
/* 17 */     this.sipDomain = sipDomain;
/* 18 */     this.password = password;
/*    */   }
/*    */   
/*    */   public UserCredentials getCredentials(ClientTransaction challengedTransaction, String realm) {
/* 22 */     return new UserCredentialsImpl(this.username, this.sipDomain, this.password);
/*    */   }
/*    */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\sip\AccountManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */