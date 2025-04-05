/*     */ package xml_generator;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.StringWriter;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.transform.Transformer;
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
/*     */ public class XMLbase
/*     */ {
/*     */   public void base_pidfPlusxml_XML(String clientURI) {
/*     */     try {
/*  26 */       DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
/*  27 */       DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
/*     */ 
/*     */       
/*  30 */       Document doc = docBuilder.newDocument();
/*  31 */       Element rootElement = doc.createElement("presence");
/*  32 */       doc.appendChild(rootElement);
/*  33 */       rootElement.setAttribute("xmlns", "urn:ietf:params:xml:ns:pidf");
/*     */       
/*  35 */       rootElement.setAttribute("entity", "sip:" + clientURI);
/*  36 */       rootElement.setAttribute("xmlns:mcpttPI10", "urn:3gpp:ns:mcpttPresInfo:1.0");
/*  37 */       Element affliType = doc.createElement("tuple");
/*  38 */       rootElement.appendChild(affliType);
/*  39 */       Element statusType = doc.createElement("status");
/*  40 */       affliType.appendChild(statusType);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  49 */       Element pid = doc.createElement("mcpttPI10:p-id");
/*  50 */       rootElement.appendChild(pid);
/*     */       
/*  52 */       TransformerFactory transformerFactory = TransformerFactory.newInstance();
/*  53 */       Transformer transformer = transformerFactory.newTransformer();
/*  54 */       DOMSource source = new DOMSource(doc);
/*  55 */       StreamResult result = new StreamResult(new File("test_pidf.xml"));
/*     */       
/*  57 */       transformer.transform(source, result);
/*     */       
/*  59 */       System.out.println("File saved!");
/*     */     }
/*  61 */     catch (ParserConfigurationException pce) {
/*  62 */       pce.printStackTrace();
/*  63 */     } catch (TransformerException tfe) {
/*  64 */       tfe.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String string_base_pidfPlusxml_XML(String clientURI) {
/*  70 */     String Strresult = "";
/*     */     try {
/*  72 */       DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
/*  73 */       DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
/*     */       
/*  75 */       Document doc = docBuilder.newDocument();
/*  76 */       Element rootElement = doc.createElement("presence");
/*  77 */       doc.appendChild(rootElement);
/*  78 */       rootElement.setAttribute("xmlns", "urn:ietf:params:xml:ns:pidf");
/*     */       
/*  80 */       rootElement.setAttribute("entity", "sip:" + clientURI);
/*  81 */       rootElement.setAttribute("xmlns:mcpttPI10", "urn:3gpp:ns:mcpttPresInfo:1.0");
/*  82 */       Element affliType = doc.createElement("tuple");
/*  83 */       rootElement.appendChild(affliType);
/*  84 */       Element statusType = doc.createElement("status");
/*  85 */       affliType.appendChild(statusType);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  94 */       Element pid = doc.createElement("mcpttPI10:p-id");
/*  95 */       rootElement.appendChild(pid);
/*     */       
/*  97 */       TransformerFactory transformerFactory = TransformerFactory.newInstance();
/*  98 */       Transformer transformer = transformerFactory.newTransformer();
/*  99 */       DOMSource source = new DOMSource(doc);
/* 100 */       StringWriter writer = new StringWriter();
/* 101 */       StreamResult result = new StreamResult(writer);
/* 102 */       transformer.transform(source, result);
/* 103 */       Strresult = writer.toString();
/*     */     }
/* 105 */     catch (ParserConfigurationException pce) {
/* 106 */       pce.printStackTrace();
/* 107 */     } catch (TransformerException tfe) {
/* 108 */       tfe.printStackTrace();
/*     */     } 
/* 110 */     return Strresult;
/*     */   }
/*     */   
/*     */   public void base_mcpttinfo_XML() {
/*     */     try {
/* 115 */       DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
/* 116 */       DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
/*     */ 
/*     */       
/* 119 */       Document doc = docBuilder.newDocument();
/* 120 */       Element rootElement = doc.createElement("mcpttinfo");
/* 121 */       doc.appendChild(rootElement);
/* 122 */       rootElement.setAttribute("xmlns", "urn:3gpp:ns:mcpttInfo:1.0");
/* 123 */       rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
/* 124 */       Element mcptt_params = doc.createElement("mcptt-Params");
/* 125 */       rootElement.appendChild(mcptt_params);
/* 126 */       Element mcptt_req_uri = doc.createElement("mcptt-request-uri");
/* 127 */       mcptt_params.appendChild(mcptt_req_uri);
/* 128 */       Element mcptt_uri = doc.createElement("mcpttURI");
/* 129 */       mcptt_req_uri.appendChild(mcptt_uri);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 135 */       TransformerFactory transformerFactory = TransformerFactory.newInstance();
/* 136 */       Transformer transformer = transformerFactory.newTransformer();
/* 137 */       DOMSource source = new DOMSource(doc);
/* 138 */       StreamResult result1 = new StreamResult(new File("test_mcpttinfo.xml"));
/* 139 */       StreamResult result2 = new StreamResult(new File("mcpttinfo.xml"));
/*     */       
/* 141 */       transformer.transform(source, result1);
/* 142 */       transformer.transform(source, result2);
/* 143 */       System.out.println("File saved!");
/*     */     }
/* 145 */     catch (ParserConfigurationException pce) {
/* 146 */       pce.printStackTrace();
/* 147 */     } catch (TransformerException tfe) {
/* 148 */       tfe.printStackTrace();
/*     */     } 
/*     */   }
/*     */   public String string_base_mcpttinfo_XML() {
/* 152 */     String Strresult = "";
/*     */     try {
/* 154 */       DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
/* 155 */       DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
/*     */       
/* 157 */       Document doc = docBuilder.newDocument();
/* 158 */       Element rootElement = doc.createElement("mcpttinfo");
/* 159 */       doc.appendChild(rootElement);
/* 160 */       rootElement.setAttribute("xmlns", "urn:3gpp:ns:mcpttInfo:1.0");
/* 161 */       rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
/* 162 */       Element mcptt_params = doc.createElement("mcptt-Params");
/* 163 */       rootElement.appendChild(mcptt_params);
/* 164 */       Element mcptt_req_uri = doc.createElement("mcptt-request-uri");
/* 165 */       mcptt_params.appendChild(mcptt_req_uri);
/* 166 */       Element mcptt_uri = doc.createElement("mcpttURI");
/* 167 */       mcptt_req_uri.appendChild(mcptt_uri);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 173 */       TransformerFactory transformerFactory = TransformerFactory.newInstance();
/* 174 */       Transformer transformer = transformerFactory.newTransformer();
/* 175 */       DOMSource source = new DOMSource(doc);
/* 176 */       StringWriter writer = new StringWriter();
/* 177 */       StreamResult result = new StreamResult(writer);
/* 178 */       transformer.transform(source, result);
/* 179 */       Strresult = writer.toString();
/*     */     }
/* 181 */     catch (ParserConfigurationException pce) {
/* 182 */       pce.printStackTrace();
/* 183 */     } catch (TransformerException tfe) {
/* 184 */       tfe.printStackTrace();
/*     */     } 
/* 186 */     return Strresult;
/*     */   }
/*     */   public void refer_mcpttinfo_XML() {
/*     */     try {
/* 190 */       DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
/* 191 */       DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
/*     */ 
/*     */       
/* 194 */       Document doc = docBuilder.newDocument();
/* 195 */       Element rootElement = doc.createElement("mcpttinfo");
/* 196 */       doc.appendChild(rootElement);
/* 197 */       rootElement.setAttribute("xmlns", "urn:3gpp:ns:mcpttInfo:1.0");
/* 198 */       rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
/* 199 */       Element mcptt_params = doc.createElement("mcptt-Params");
/* 200 */       rootElement.appendChild(mcptt_params);
/* 201 */       Element mcptt_req_uri = doc.createElement("mcptt-request-uri");
/* 202 */       mcptt_params.appendChild(mcptt_req_uri);
/* 203 */       Element mcptt_uri = doc.createElement("mcpttURI");
/* 204 */       mcptt_req_uri.appendChild(mcptt_uri);
/* 205 */       Element mcptt_client_id = doc.createElement("mcptt-client-id");
/* 206 */       mcptt_params.appendChild(mcptt_client_id);
/*     */       
/* 208 */       TransformerFactory transformerFactory = TransformerFactory.newInstance();
/* 209 */       Transformer transformer = transformerFactory.newTransformer();
/* 210 */       DOMSource source = new DOMSource(doc);
/* 211 */       StreamResult result = new StreamResult(new File("test_mcpttinfo.xml"));
/*     */       
/* 213 */       transformer.transform(source, result);
/* 214 */       System.out.println("File saved!");
/*     */     }
/* 216 */     catch (ParserConfigurationException pce) {
/* 217 */       pce.printStackTrace();
/* 218 */     } catch (TransformerException tfe) {
/* 219 */       tfe.printStackTrace();
/*     */     } 
/*     */   }
/*     */   public String string_refer_mcpttinfo_XML() {
/* 223 */     String Strresult = "";
/*     */     try {
/* 225 */       DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
/* 226 */       DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
/*     */ 
/*     */       
/* 229 */       Document doc = docBuilder.newDocument();
/* 230 */       Element rootElement = doc.createElement("mcpttinfo");
/* 231 */       doc.appendChild(rootElement);
/* 232 */       rootElement.setAttribute("xmlns", "urn:3gpp:ns:mcpttInfo:1.0");
/* 233 */       rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
/* 234 */       Element mcptt_params = doc.createElement("mcptt-Params");
/* 235 */       rootElement.appendChild(mcptt_params);
/* 236 */       Element mcptt_req_uri = doc.createElement("mcptt-request-uri");
/* 237 */       mcptt_params.appendChild(mcptt_req_uri);
/* 238 */       Element mcptt_uri = doc.createElement("mcpttURI");
/* 239 */       mcptt_req_uri.appendChild(mcptt_uri);
/* 240 */       Element mcptt_client_id = doc.createElement("mcptt-client-id");
/* 241 */       mcptt_params.appendChild(mcptt_client_id);
/*     */ 
/*     */       
/* 244 */       TransformerFactory transformerFactory = TransformerFactory.newInstance();
/* 245 */       Transformer transformer = transformerFactory.newTransformer();
/* 246 */       DOMSource source = new DOMSource(doc);
/* 247 */       StringWriter writer = new StringWriter();
/* 248 */       StreamResult result = new StreamResult(writer);
/* 249 */       transformer.transform(source, result);
/* 250 */       Strresult = writer.toString();
/*     */     }
/* 252 */     catch (ParserConfigurationException pce) {
/* 253 */       pce.printStackTrace();
/* 254 */     } catch (TransformerException tfe) {
/* 255 */       tfe.printStackTrace();
/*     */     } 
/* 257 */     return Strresult;
/*     */   }
/*     */   
/*     */   public String string_refer_mcpttinfo_EMGC_XML() {
/* 261 */     String Strresult = "";
/*     */     try {
/* 263 */       DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
/* 264 */       DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
/*     */ 
/*     */ 
/*     */       
/* 268 */       Document doc = docBuilder.newDocument();
/* 269 */       Element rootElement = doc.createElement("mcpttinfo");
/* 270 */       Element URI_type = doc.createElement("mcpttURI");
/* 271 */       Element Boolean_type = doc.createElement("mcpttBoolean");
/* 272 */       doc.appendChild(rootElement);
/* 273 */       rootElement.setAttribute("xmlns", "urn:3gpp:ns:mcpttInfo:1.0");
/* 274 */       rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
/* 275 */       Element mcptt_params = doc.createElement("mcptt-Params");
/* 276 */       rootElement.appendChild(mcptt_params);
/* 277 */       Element mcptt_req_uri = doc.createElement("mcptt-request-uri");
/* 278 */       mcptt_params.appendChild(mcptt_req_uri);
/*     */ 
/*     */       
/* 281 */       Element mcptt_client_id = doc.createElement("mcptt-client-id");
/* 282 */       mcptt_params.appendChild(mcptt_client_id);
/* 283 */       Element mcptt_calling_id = doc.createElement("mcptt-calling-user-id");
/* 284 */       mcptt_params.appendChild(mcptt_calling_id);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 289 */       Element mcptt_callinggroup_id = doc.createElement("mcptt-calling-group-id");
/* 290 */       mcptt_params.appendChild(mcptt_callinggroup_id);
/*     */ 
/*     */ 
/*     */       
/* 294 */       Element mcptt_emergency_ind = doc.createElement("emergency-ind");
/*     */       
/* 296 */       mcptt_params.appendChild(mcptt_emergency_ind);
/*     */       
/* 298 */       Element mcptt_emergency_alert_ind = doc.createElement("alert-ind");
/*     */       
/* 300 */       mcptt_params.appendChild(mcptt_emergency_alert_ind);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 305 */       TransformerFactory transformerFactory = TransformerFactory.newInstance();
/* 306 */       Transformer transformer = transformerFactory.newTransformer();
/* 307 */       DOMSource source = new DOMSource(doc);
/* 308 */       StringWriter writer = new StringWriter();
/* 309 */       StreamResult result = new StreamResult(writer);
/* 310 */       transformer.transform(source, result);
/* 311 */       Strresult = writer.toString();
/*     */     }
/* 313 */     catch (ParserConfigurationException pce) {
/* 314 */       pce.printStackTrace();
/* 315 */     } catch (TransformerException tfe) {
/* 316 */       tfe.printStackTrace();
/*     */     } 
/* 318 */     return Strresult;
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\xml_generator\XMLbase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */