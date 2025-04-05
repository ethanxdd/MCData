/*     */ package http;
import java.io.BufferedReader;
/*     */ 
/*     */ import java.io.IOException;
import java.io.InputStreamReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
/*     */ import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
/*     */ import org.jsoup.Jsoup;
/*     */ import org.jsoup.nodes.Document;
/*     */ import org.jsoup.nodes.Element;
/*     */ import org.jsoup.select.Elements;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class getDirectorytInfo
/*     */ {
/*     */   private String result;
/*     */   private String UserURI;
/*  21 */   private List<String> AffiliatedGroupList = new ArrayList<>();
/*     */ 
/*     */   
/*     */   public void getDirectorytInfo(String username, String serverIP) throws IOException {
/*  25 */     	HttpClient httpClient = HttpClients.createDefault();
				HttpGet request = new HttpGet("http://" + 
/*  26 */         serverIP + 
/*  27 */         ":" + 
/*  28 */         "8080" + 
/*  29 */         "/restcomm/?cmd=GetData&Parm=" + 
/*  30 */         serverIP + 
/*  31 */         ":" + 
/*  32 */         "15060/xcap-root/org.openmobilealliance.xcap-directory/" + 
/*  33 */         "users/sip:" + 
/*  34 */         username + 
/*  35 */         "@" + 
/*  36 */         serverIP + 
/*  37 */         "/" + 
/*  38 */         "directory.xml");
/*     */     
/*  40 */     this.UserURI = String.valueOf(username) + "@" + serverIP;
///*     */     
				try {
				    // 执行请求，获取响应
				    HttpResponse response = httpClient.execute(request);
				    HttpEntity entity = response.getEntity();
				
				    // 检查响应的状态码是否为200 (HTTP OK)
				    if (response.getStatusLine().getStatusCode() == 200 && entity != null) {
				        // 读取响应内容
				        BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
				        StringBuilder responseContent = new StringBuilder();
				        String line;
				        while ((line = reader.readLine()) != null) {
				            responseContent.append(line);
				        }
				
				        // 打印原始响应内容（可选）
				        paserresult(responseContent.toString(),"folder");
				        // 解析JSON响应内容
				//        JsonElement jsonElement = JsonParser.parseString(responseContent.toString());
				//        if (jsonElement.isJsonObject()) {
				//            JsonObject jsonObject = jsonElement.getAsJsonObject();
				//            // 假设grouplist在JSON中的字段名是"groupList"
				//            if (jsonObject.has("groupList")) {
				//                System.out.println("Group List: " + jsonObject.get("groupList").toString());
				//            } else {
				//                System.out.println("Group list not found in the response.");
				//            }
				//        }
				    } else {
				        System.out.println("Request failed with status code: " + response.getStatusLine().getStatusCode());
				    }
				} catch (Exception e) {
				    e.printStackTrace();
				}
/*  42 */     Exception exception1 = null, exception2 = null;
				//return this.UserURI;
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
/*     */   private void paserresult(String dataxml, String tittle) {
/*  81 */     String token = null;
/*  82 */     String GroupName = null;
/*  83 */     Document doc = Jsoup.parseBodyFragment(dataxml);
				for (Element folder : doc.getElementsByTag(tittle)) {
				    Elements elements = folder.getElementsByAttributeValueMatching("auid", "org.openmobilealliance.poc-groups");
				    Elements entryElements = elements.select("entry");
				    
				    if (!entryElements.isEmpty()) {
				        for (Element entry : entryElements) {
				            token = entry.toString();
				            String g = token.substring(0, token.indexOf(".xml"));
				            GroupName = g.substring(g.indexOf(this.UserURI) + this.UserURI.length() + 1);
				            this.AffiliatedGroupList.add(GroupName);
				        }
				    }
				}
///*  85 */     for (Element e : doc.getElementsByTag(tittle)) {
///*     */       
///*  87 */       Elements elements = e.getElementsByAttributeValueMatching("auid", "org.openmobilealliance.poc-groups");
///*  88 */       Elements elementss = elements.select("entry");
///*     */       System.out.println("Responsedee: " + elementss.toString());
///*  90 */       if (!elementss.toString().isEmpty()) {
///*  91 */         for (Element d : elementss) {
///*  92 */           token = d.toString();
//					System.out.println("Responsed: " + token);
///*  93 */           String g = token.substring(0, token.indexOf(".xml"));
///*  94 */           System.out.println("Responseg: " + g);
//					GroupName = g.substring(g.indexOf(this.UserURI) + this.UserURI.length() + 1, g.length());
///*  95 */           this.AffiliatedGroupList.add(GroupName);
///*     */         } 
///*     */       }
///*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> AffiliatedGroup() {
/* 104 */     return this.AffiliatedGroupList;
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\http\getDirectorytInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */