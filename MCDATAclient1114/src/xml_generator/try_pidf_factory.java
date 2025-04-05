/*     */ package xml_generator;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class try_pidf_factory
/*     */ {
/*     */   public try_pidf_factory() {}
/*     */   
/*     */   public try_pidf_factory(String clientURI, String tuple_id, List<String> sip_group, List<String> status, String pid) {
/*  19 */     XMLbase message = new XMLbase();
/*  20 */     message.base_pidfPlusxml_XML(clientURI);
/*     */     try {
/*  22 */       pidfPlusxml rewrite = new pidfPlusxml();
/*  23 */       rewrite.pidf_Plus_xml_tuple_builder_without_mcpttPI("test_pidf.xml", "tuple", tuple_id);
/*     */ 
/*     */       
/*  26 */       for (int i = 0; i < sip_group.size(); i++) {
/*  27 */         rewrite.pidf_Plus_xml_group_builder("test_pidf.xml", "status", "mcpttPI10:affiliation", "sip:" + (String)sip_group.get(i), status.get(i));
/*     */       }
/*  29 */       rewrite.mcptt_adder("test_pidf.xml", pid, "mcpttPI10:p-id");
/*  30 */     } catch (SAXException e) {
/*     */       
/*  32 */       e.printStackTrace();
/*  33 */     } catch (IOException e) {
/*     */       
/*  35 */       e.printStackTrace();
/*  36 */     } catch (ParserConfigurationException e) {
/*     */       
/*  38 */       e.printStackTrace();
/*  39 */     } catch (TransformerException e) {
/*     */       
/*  41 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   public try_pidf_factory(String clientURI, String tuple_id, List<String> sip_group, String pid) {
/*  45 */     XMLbase message = new XMLbase();
/*  46 */     message.base_pidfPlusxml_XML(clientURI);
/*     */     try {
/*  48 */       pidfPlusxml rewrite = new pidfPlusxml();
/*  49 */       rewrite.pidf_Plus_xml_tuple_builder_without_mcpttPI("test_pidf.xml", "tuple", tuple_id);
/*     */ 
/*     */       
/*  52 */       for (int i = 0; i < sip_group.size(); i++) {
/*  53 */         rewrite.pidf_Plus_xml_group_builder("test_pidf.xml", "status", "mcpttPI10:affiliation", "sip:" + (String)sip_group.get(i));
/*     */       }
/*  55 */       rewrite.mcptt_adder("test_pidf.xml", pid, "mcpttPI10:p-id");
/*  56 */     } catch (SAXException e) {
/*     */       
/*  58 */       e.printStackTrace();
/*  59 */     } catch (IOException e) {
/*     */       
/*  61 */       e.printStackTrace();
/*  62 */     } catch (ParserConfigurationException e) {
/*     */       
/*  64 */       e.printStackTrace();
/*  65 */     } catch (TransformerException e) {
/*     */       
/*  67 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String string_try_pidf_factory(String clientURI, String tuple_id, List<String> sip_group, List<String> status, String pid) {
/*  73 */     XMLbase message = new XMLbase();
/*  74 */     String xmlstring = message.string_base_pidfPlusxml_XML(clientURI);
/*     */ 
/*     */     
/*     */     try {
/*  78 */       pidfPlusxml rewrite = new pidfPlusxml();
/*  79 */       xmlstring = rewrite.string_pidf_Plus_xml_tuple_builder_without_mcpttPI(xmlstring, "tuple", tuple_id);
/*     */ 
/*     */ 
/*     */       
/*  83 */       for (int i = 0; i < sip_group.size(); i++)
/*     */       {
/*  85 */         xmlstring = rewrite.string_pidf_Plus_xml_group_builder(xmlstring, "status", "mcpttPI10:affiliation", "sip:" + (String)sip_group.get(i), status.get(i));
/*     */       }
/*     */       
/*  88 */       xmlstring = rewrite.string_mcptt_adder(xmlstring, pid, "mcpttPI10:p-id");
/*  89 */     } catch (SAXException e) {
/*     */       
/*  91 */       e.printStackTrace();
/*  92 */     } catch (IOException e) {
/*     */       
/*  94 */       e.printStackTrace();
/*  95 */     } catch (ParserConfigurationException e) {
/*     */       
/*  97 */       e.printStackTrace();
/*  98 */     } catch (TransformerException e) {
/*     */       
/* 100 */       e.printStackTrace();
/*     */     } 
/* 102 */     return xmlstring;
/*     */   }
/*     */   
/*     */   public String string_try_pidf_factory(String clientURI, String tuple_id, List<String> sip_group, String pid) {
/* 106 */     XMLbase message = new XMLbase();
/* 107 */     String xmlbody = message.string_base_pidfPlusxml_XML(clientURI);
/*     */     try {
/* 109 */       pidfPlusxml rewrite = new pidfPlusxml();
/* 110 */       xmlbody = rewrite.string_pidf_Plus_xml_tuple_builder_without_mcpttPI(xmlbody, "tuple", tuple_id);
/*     */ 
/*     */       
/* 113 */       for (int i = 0; i < sip_group.size(); i++) {
/* 114 */         xmlbody = rewrite.string_pidf_Plus_xml_group_builder(xmlbody, "status", "mcpttPI10:affiliation", "sip:" + (String)sip_group.get(i));
/*     */       }
/* 116 */       xmlbody = rewrite.string_mcptt_adder(xmlbody, pid, "mcpttPI10:p-id");
/* 117 */     } catch (SAXException e) {
/*     */       
/* 119 */       e.printStackTrace();
/* 120 */     } catch (IOException e) {
/*     */       
/* 122 */       e.printStackTrace();
/* 123 */     } catch (ParserConfigurationException e) {
/*     */       
/* 125 */       e.printStackTrace();
/* 126 */     } catch (TransformerException e) {
/*     */       
/* 128 */       e.printStackTrace();
/*     */     } 
/* 130 */     return xmlbody;
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\xml_generator\try_pidf_factory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */