/*    */ package http;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import list.RemoteInfo;
/*    */ import org.apache.http.client.methods.HttpGet;
/*    */ import org.jsoup.Jsoup;
/*    */ import org.jsoup.nodes.Document;
/*    */ import org.jsoup.nodes.Element;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class getGroupMember
/*    */ {
/*    */   private String result;
/*    */   private static List<String> member_list;
/*    */   private static List<RemoteInfo> remoteInfoList;
/*    */   
/*    */   public void getGroupMember(String username, String groupname, String serverIP) throws IOException {
/* 24 */     HttpGet request = new HttpGet("http://" + 
/* 25 */         serverIP + 
/* 26 */         ":" + 
/* 27 */         "15060" + 
/* 28 */         "/xcap-root/org.openmobilealliance.poc-groups/" + 
/* 29 */         "users/sip:" + 
/* 30 */         username + 
/* 31 */         "@" + 
/* 32 */         serverIP + 
/* 33 */         "/" + 
/* 34 */         groupname + 
/* 35 */         ".xml");
/*    */     
/* 37 */     Exception exception1 = null, exception2 = null;
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
/*    */ 
/*    */   
/*    */   private static void paserresult(String groupmemberxml) {
/* 58 */     member_list = new ArrayList<>();
/* 59 */     Document doc = Jsoup.parseBodyFragment(groupmemberxml);
/*    */     
/* 61 */     for (Element e : doc.getElementsByTag("entry")) {
/* 62 */       String friendUri = e.attr("uri");
/* 63 */       friendUri = friendUri.substring(friendUri.indexOf("sip:") + 4, friendUri.length());
/* 64 */       member_list.add(friendUri);
/*    */     } 
/*    */ 
/*    */     
/* 68 */     setremoteinfo(member_list);
/*    */   }
/*    */   
/*    */   private static void setremoteinfo(List<String> member_list) {
/* 72 */     remoteInfoList = new ArrayList<>();
/* 73 */     RemoteInfo member = new RemoteInfo();
/* 74 */     for (int i = 1; i <= member_list.size(); i++) {
/* 75 */       member.setDisplayName(((String)member_list.get(i - 1)).split("@")[0]);
/* 76 */       member.setSipAddr(((String)member_list.get(i - 1)).split("@")[1]);
/* 77 */       member.usersInGroup.add(member_list.get(i - 1));
/*    */       
/* 79 */       remoteInfoList.add(member);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static List<RemoteInfo> getgroupmember() {
/* 85 */     return remoteInfoList;
/*    */   }
/*    */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\http\getGroupMember.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */