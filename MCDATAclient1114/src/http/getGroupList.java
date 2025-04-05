/*     */ package http;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

/*     */ import org.apache.http.client.methods.HttpGet;
/*     */ import org.jsoup.Jsoup;
/*     */ import org.jsoup.nodes.Document;
/*     */ import org.jsoup.nodes.Element;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class getGroupList
/*     */ {
/*     */   private static String result;
/*  22 */   private HashMap<String, String> Groupdictionary = new HashMap<>();
/*  23 */   private List<String> GroupdisplaynameList = new ArrayList<>();
/*     */   
///*     */   public void getGroupList(String serverIP) throws ParseException, IOException {
///*  26 */     HttpGet request = new HttpGet("http://" + 
///*  27 */         serverIP + 
///*  28 */         ":" + 
///*  29 */         "8080" + 
///*  30 */         "/restcomm/?cmd=GetAllGroupList");
///*     */     System.out.println(request);
///*  32 */     Exception exception1 = null, exception2 = null;
///*     */   }
/*     */ 
/*     */ 	public void getGroupList(String serverIP) throws ParseException, IOException {
			    // 创建HttpClient对象
			    HttpClient httpClient = HttpClients.createDefault();
			    // 创建HttpGet请求对象
			    HttpGet request = new HttpGet("http://" + serverIP + ":8080/restcomm/?cmd=GetAllGroupList");
			    
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
			             paserresult(responseContent.toString(),"Group-info");
			            // 解析JSON响应内容
//			            JsonElement jsonElement = JsonParser.parseString(responseContent.toString());
//			            if (jsonElement.isJsonObject()) {
//			                JsonObject jsonObject = jsonElement.getAsJsonObject();
//			                // 假设grouplist在JSON中的字段名是"groupList"
//			                if (jsonObject.has("groupList")) {
//			                    System.out.println("Group List: " + jsonObject.get("groupList").toString());
//			                } else {
//			                    System.out.println("Group list not found in the response.");
//			                }
//			            }
			        } else {
			            System.out.println("Request failed with status code: " + response.getStatusLine().getStatusCode());
			        }
			    } catch (Exception e) {
			        e.printStackTrace();
			    }
}

/*     */   private String paserresult(String dataxml, String tittle) {
/*  64 */     String token = null;
/*  65 */     Document doc = Jsoup.parseBodyFragment(dataxml);
/*  66 */     for (Element e : doc.getElementsByTag(tittle)) {
/*  67 */       String displayname = e.getElementsByTag("display-name").text();
/*  68 */       String uri = e.getElementsByTag("group-uri").text();
/*  69 */       this.Groupdictionary.put(displayname, uri);
/*  70 */       this.GroupdisplaynameList.add(displayname);
/*     */     } 
 
/*  77 */     return token;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getGroupuri(String displayname) {
/*  83 */     String GroupUri = this.Groupdictionary.get(displayname);
/*  84 */     return GroupUri;
/*     */   }
/*     */   
/*     */   public List<String> getAllGroupUri() {
/*  88 */     List<String> list = new ArrayList<>();
/*  89 */     for (String e : this.GroupdisplaynameList) {
/*  90 */       list.add(this.Groupdictionary.get(e));
/*     */     }
/*  92 */     return list;
/*     */   }
/*     */   
/*     */   public HashMap<String, String> getGroupdictionary() {
/*  96 */     return this.Groupdictionary;
/*     */   }
/*     */   
/*     */   public List<String> getAllGroupdisplayname() {
/* 100 */     return this.GroupdisplaynameList;
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\http\getGroupList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */