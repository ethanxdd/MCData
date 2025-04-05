/*    */ package http;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.time.LocalDateTime;
/*    */ import java.time.format.DateTimeFormatter;
/*    */ import javax.swing.JOptionPane;

/*    */ import org.apache.http.client.methods.CloseableHttpResponse;
/*    */ import org.apache.http.client.methods.HttpGet;
/*    */ import org.apache.http.client.methods.HttpUriRequest;
/*    */ import org.apache.http.impl.client.HttpClients;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class createGroup
/*    */ {
/* 16 */   private String GroupName = "";
/*    */   
/*    */   public void createGroup(String serverIP, String userURI, String groupDisplayName) {
/* 19 */     String url = "http://" + 
/* 20 */       serverIP + 
/* 21 */       ":8080/restcomm/?cmd=CreateGroupCmd&UserURI=" + 
/* 22 */       userURI + 
/* 23 */       "&AddressParm=" + 
/* 24 */       serverIP + 
/* 25 */       ":15060&DisplayGroupName=" + 
/* 26 */       groupDisplayName;
/*    */     
/*    */     try {
/* 29 */       HttpGet get = new HttpGet(url);
/*    */       
/* 31 */       CloseableHttpResponse closeableHttpResponse = HttpClients.createDefault().execute((HttpUriRequest)get);
/* 32 */       if (closeableHttpResponse.getStatusLine().getStatusCode() != 200) {
/* 33 */         System.out.println("Failed to set group xml: " + url);
/*    */       }
/*    */       
/* 36 */       JOptionPane.showMessageDialog(null, "Group created successfully");
/* 37 */     } catch (IOException ex) {
/* 38 */       ex.printStackTrace();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private String createGroupName() {
/* 44 */     DateTimeFormatter time = DateTimeFormatter.ofPattern("yyyy_M_d_");
/*    */     
/* 46 */     int r = 0;
/*    */     
/* 48 */     r = (int)(Math.random() * 900000.0D) + 100000;
/*    */     
/* 50 */     String groupname = String.valueOf(time.format(LocalDateTime.now())) + r;
/* 51 */     this.GroupName = groupname;
/*    */ 
/*    */     
/* 54 */     return this.GroupName;

/*    */   }
/*    */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\http\createGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */