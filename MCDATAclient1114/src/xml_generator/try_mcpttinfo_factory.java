/*     */ package xml_generator;
/*     */ 
/*     */ import java.io.StringWriter;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerConfigurationException;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
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
/*     */ public class try_mcpttinfo_factory
/*     */ {
/*     */   public String string_try_mcpttinfo_factory(String request_type, String client_id) {
/*  31 */     String Strresult = "";
/*  32 */     DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
/*     */     
/*     */     try {
/*  35 */       DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
/*     */ 
/*     */       
/*  38 */       Document doc = docBuilder.newDocument();
/*  39 */       Element rootElement = doc.createElement("mcpttinfo");
/*  40 */       Element URI_type = doc.createElement("mcpttURI");
/*  41 */       Element Boolean_type = doc.createElement("mcpttBoolean");
/*  42 */       doc.appendChild(rootElement);
/*  43 */       rootElement.setAttribute("xmlns", "urn:3gpp:ns:mcpttInfo:1.0");
/*  44 */       rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
/*  45 */       Element mcptt_params = doc.createElement("mcptt-Params");
/*  46 */       rootElement.appendChild(mcptt_params);
/*  47 */       Element mcptt_req_uri = doc.createElement("mcptt-request-uri");
/*  48 */       mcptt_req_uri.setAttribute("type", request_type);
/*  49 */       mcptt_params.appendChild(mcptt_req_uri);
/*  50 */       Element mcptt_uri_1 = doc.createElement("mcpttURI");
/*  51 */       mcptt_req_uri.appendChild(mcptt_uri_1);
/*  52 */       mcptt_uri_1.setTextContent(client_id);
/*     */       
/*  54 */       TransformerFactory transformerFactory = TransformerFactory.newInstance();
/*     */ 
/*     */       
/*  57 */       Transformer transformer = transformerFactory.newTransformer();
/*  58 */       DOMSource source = new DOMSource(doc);
/*  59 */       StringWriter writer = new StringWriter();
/*  60 */       StreamResult result = new StreamResult(writer);
/*  61 */       transformer.transform(source, result);
/*  62 */       Strresult = writer.toString();
/*     */       
/*  64 */       return Strresult;
/*  65 */     } catch (ParserConfigurationException e) {
/*     */       
/*  67 */       e.printStackTrace();
/*  68 */     } catch (TransformerConfigurationException e) {
/*     */       
/*  70 */       e.printStackTrace();
/*  71 */     } catch (TransformerException e) {
/*     */       
/*  73 */       e.printStackTrace();
/*     */     } 
/*  75 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String string_try_mcpttinfo_factory(String request_type, String client_id, String groupID) {
/*  80 */     String Strresult = "";
/*  81 */     DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
/*     */     
/*     */     try {
/*  84 */       DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
/*     */ 
/*     */       
/*  87 */       Document doc = docBuilder.newDocument();
/*  88 */       Element rootElement = doc.createElement("mcpttinfo");
/*  89 */       Element URI_type = doc.createElement("mcpttURI");
/*  90 */       Element Boolean_type = doc.createElement("mcpttBoolean");
/*  91 */       doc.appendChild(rootElement);
/*  92 */       rootElement.setAttribute("xmlns", "urn:3gpp:ns:mcpttInfo:1.0");
/*  93 */       rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
/*  94 */       Element mcptt_params = doc.createElement("mcptt-Params");
/*  95 */       rootElement.appendChild(mcptt_params);
/*  96 */       Element mcptt_req_uri = doc.createElement("mcptt-request-uri");
/*  97 */       mcptt_req_uri.setAttribute("type", request_type);
/*  98 */       mcptt_params.appendChild(mcptt_req_uri);
/*  99 */       Element mcptt_uri_1 = doc.createElement("mcpttURI");
/* 100 */       mcptt_req_uri.appendChild(mcptt_uri_1);
/* 101 */       mcptt_uri_1.setTextContent(groupID);
/* 102 */       Element mcptt_client_id = doc.createElement("mcptt-client-id");
/* 103 */       mcptt_params.appendChild(mcptt_client_id);
/* 104 */       mcptt_client_id.setTextContent(client_id);
/*     */       
/* 106 */       TransformerFactory transformerFactory = TransformerFactory.newInstance();
/*     */ 
/*     */       
/* 109 */       Transformer transformer = transformerFactory.newTransformer();
/* 110 */       DOMSource source = new DOMSource(doc);
/* 111 */       StringWriter writer = new StringWriter();
/* 112 */       StreamResult result = new StreamResult(writer);
/* 113 */       transformer.transform(source, result);
/* 114 */       Strresult = writer.toString();
/*     */       
/* 116 */       return Strresult;
/* 117 */     } catch (ParserConfigurationException e) {
/*     */       
/* 119 */       e.printStackTrace();
/* 120 */     } catch (TransformerConfigurationException e) {
/*     */       
/* 122 */       e.printStackTrace();
/* 123 */     } catch (TransformerException e) {
/*     */       
/* 125 */       e.printStackTrace();
/*     */     } 
/* 127 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String string_try_mcpttinfo_factory(String request_type, String client_id, String group_id, boolean emergency, boolean alert) throws ParserConfigurationException, TransformerException {
/* 133 */     String Strresult = "";
/*     */     
/* 135 */     DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
/* 136 */     DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
/*     */ 
/*     */ 
/*     */     
/* 140 */     Document doc = docBuilder.newDocument();
/* 141 */     Element rootElement = doc.createElement("mcpttinfo");
/* 142 */     Element URI_type = doc.createElement("mcpttURI");
/* 143 */     Element Boolean_type = doc.createElement("mcpttBoolean");
/* 144 */     doc.appendChild(rootElement);
/* 145 */     rootElement.setAttribute("xmlns", "urn:3gpp:ns:mcpttInfo:1.0");
/* 146 */     rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
/* 147 */     Element mcptt_params = doc.createElement("mcptt-Params");
/* 148 */     rootElement.appendChild(mcptt_params);
/* 149 */     Element mcptt_req_uri = doc.createElement("mcptt-request-uri");
/* 150 */     mcptt_req_uri.setAttribute("type", request_type);
/* 151 */     mcptt_params.appendChild(mcptt_req_uri);
/* 152 */     Element mcptt_uri_1 = doc.createElement("mcpttURI");
/* 153 */     mcptt_req_uri.appendChild(mcptt_uri_1);
/* 154 */     mcptt_uri_1.setTextContent(client_id);
/* 155 */     Element mcptt_client_id = doc.createElement("mcptt-client-id");
/* 156 */     mcptt_params.appendChild(mcptt_client_id);
/* 157 */     mcptt_client_id.setTextContent(client_id);
/* 158 */     Element mcptt_calling_id = doc.createElement("mcptt-calling-user-id");
/* 159 */     mcptt_params.appendChild(mcptt_calling_id);
/* 160 */     Element mcptt_uri_2 = doc.createElement("mcpttURI");
/* 161 */     mcptt_calling_id.appendChild(mcptt_uri_2);
/* 162 */     mcptt_uri_2.setTextContent(client_id);
/*     */     
/* 164 */     Element mcptt_callinggroup_id = doc.createElement("mcptt-calling-group-id");
/* 165 */     mcptt_params.appendChild(mcptt_callinggroup_id);
/* 166 */     Element mcptt_uri_3 = doc.createElement("mcpttURI");
/* 167 */     mcptt_callinggroup_id.appendChild(mcptt_uri_3);
/* 168 */     mcptt_uri_3.setTextContent(group_id);
/*     */     
/* 170 */     Element mcptt_emergency_ind = doc.createElement("emergency-ind");
/* 171 */     Element mcptt_boolean = doc.createElement("mcpttBoolean");
/* 172 */     mcptt_params.appendChild(mcptt_emergency_ind);
/* 173 */     mcptt_emergency_ind.appendChild(mcptt_boolean);
/* 174 */     if (emergency) {
/*     */       
/* 176 */       mcptt_boolean.setTextContent("true");
/*     */     }
/*     */     else {
/*     */       
/* 180 */       mcptt_boolean.setTextContent("false");
/*     */     } 
/*     */     
/* 183 */     Element mcptt_emergency_alert_ind = doc.createElement("alert-ind");
/* 184 */     Element mcptt_boolean_1 = doc.createElement("mcpttBoolean");
/* 185 */     mcptt_params.appendChild(mcptt_emergency_alert_ind);
/* 186 */     mcptt_emergency_alert_ind.appendChild(mcptt_boolean_1);
/* 187 */     if (alert) {
/*     */       
/* 189 */       mcptt_boolean_1.setTextContent("true");
/*     */     }
/*     */     else {
/*     */       
/* 193 */       mcptt_boolean_1.setTextContent("false");
/*     */     } 
/*     */ 
/*     */     
/* 197 */     TransformerFactory transformerFactory = TransformerFactory.newInstance();
/* 198 */     Transformer transformer = transformerFactory.newTransformer();
/* 199 */     DOMSource source = new DOMSource(doc);
/* 200 */     StringWriter writer = new StringWriter();
/* 201 */     StreamResult result = new StreamResult(writer);
/* 202 */     transformer.transform(source, result);
/* 203 */     Strresult = writer.toString();
/* 204 */     return Strresult;
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\xml_generator\try_mcpttinfo_factory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */