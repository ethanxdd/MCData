/*     */ package xml_generator;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
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
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class mcpttinfo
/*     */ {
/*     */   public void mcpttinfo_builder(String xml, String title, String request_type, String title_two, String client_id) throws SAXException, IOException, ParserConfigurationException, TransformerException {
/*  31 */     File xmlFile = new File(xml);
/*  32 */     DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
/*  33 */     DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
/*  34 */     Document document = documentBuilder.parse(xmlFile);
/*     */     
/*  36 */     NodeList nList = document.getElementsByTagName(title);
/*  37 */     Node node = nList.item(0);
/*  38 */     Element Title = (Element)node;
/*  39 */     Title.setAttribute("type", request_type);
/*     */     
/*  41 */     NodeList nList_two = document.getElementsByTagName(title_two);
/*  42 */     Node node_two = nList_two.item(0);
/*  43 */     Element Title_two = (Element)node_two;
/*  44 */     Title_two.setTextContent(client_id);
/*     */     
/*  46 */     TransformerFactory factory = TransformerFactory.newInstance();
/*  47 */     Transformer transformer = factory.newTransformer();
/*  48 */     DOMSource domSource = new DOMSource(document);
/*  49 */     StreamResult streamResult = new StreamResult(new File(xml));
/*  50 */     transformer.transform(domSource, streamResult);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void mcpttinfo_builder(String xml, String title, String request_type, String title_two, String title_three, String client_id, String group_id) throws SAXException, IOException, ParserConfigurationException, TransformerException {
/*  56 */     File xmlFile = new File(xml);
/*  57 */     DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
/*  58 */     DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
/*  59 */     Document document = documentBuilder.parse(xmlFile);
/*     */     
/*  61 */     NodeList nList = document.getElementsByTagName(title);
/*  62 */     Node node = nList.item(0);
/*  63 */     Element Title = (Element)node;
/*  64 */     Title.setAttribute("type", request_type);
/*     */     
/*  66 */     NodeList nList_two = document.getElementsByTagName(title_two);
/*  67 */     Node node_two = nList_two.item(0);
/*  68 */     Element Title_two = (Element)node_two;
/*  69 */     Title_two.setTextContent(group_id);
/*     */     
/*  71 */     NodeList nList_three = document.getElementsByTagName(title_three);
/*  72 */     Node node_three = nList_three.item(0);
/*  73 */     Element Title_three = (Element)node_three;
/*  74 */     Title_three.setTextContent(client_id);
/*     */     
/*  76 */     TransformerFactory factory = TransformerFactory.newInstance();
/*  77 */     Transformer transformer = factory.newTransformer();
/*  78 */     DOMSource domSource = new DOMSource(document);
/*  79 */     StreamResult streamResult = new StreamResult(new File(xml));
/*  80 */     transformer.transform(domSource, streamResult);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String string_mcpttinfo_builder(String xmlcontext, String title, String request_type, String title_two, String client_id) throws SAXException, IOException, ParserConfigurationException, TransformerException {
/*  87 */     String Strresult = "";
/*  88 */     InputSource is = new InputSource(new StringReader(xmlcontext));
/*     */     
/*  90 */     DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
/*  91 */     DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
/*  92 */     Document document = documentBuilder.parse(is);
/*     */     
/*  94 */     NodeList nList = document.getElementsByTagName(title);
/*  95 */     Node node = nList.item(0);
/*  96 */     Element Title = (Element)node;
/*  97 */     Title.setAttribute("type", request_type);
/*     */     
/*  99 */     NodeList nList_two = document.getElementsByTagName(title_two);
/* 100 */     Node node_two = nList_two.item(0);
/* 101 */     Element Title_two = (Element)node_two;
/* 102 */     Title_two.setTextContent(client_id);
/*     */     
/* 104 */     TransformerFactory factory = TransformerFactory.newInstance();
/* 105 */     Transformer transformer = factory.newTransformer();
/* 106 */     DOMSource domSource = new DOMSource(document);
/* 107 */     StringWriter writer = new StringWriter();
/* 108 */     StreamResult result = new StreamResult(writer);
/* 109 */     transformer.transform(domSource, result);
/* 110 */     Strresult = writer.toString();
/* 111 */     return Strresult;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String string_mcpttinfo_builder(String xmlcontext, String title, String request_type, String title_two, String title_three, String client_id, String group_id) throws SAXException, IOException, ParserConfigurationException, TransformerException {
/* 117 */     String Strresult = "";
/* 118 */     InputSource is = new InputSource(new StringReader(xmlcontext));
/* 119 */     DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
/* 120 */     DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
/* 121 */     Document document = documentBuilder.parse(is);
/*     */     
/* 123 */     NodeList nList = document.getElementsByTagName(title);
/* 124 */     Node node = nList.item(0);
/* 125 */     Element Title = (Element)node;
/* 126 */     Title.setAttribute("type", request_type);
/*     */     
/* 128 */     NodeList nList_two = document.getElementsByTagName(title_two);
/* 129 */     Node node_two = nList_two.item(0);
/* 130 */     Element Title_two = (Element)node_two;
/* 131 */     Title_two.setTextContent(group_id);
/*     */     
/* 133 */     NodeList nList_three = document.getElementsByTagName(title_three);
/* 134 */     Node node_three = nList_three.item(0);
/* 135 */     Element Title_three = (Element)node_three;
/* 136 */     Title_three.setTextContent(client_id);
/*     */     
/* 138 */     TransformerFactory factory = TransformerFactory.newInstance();
/* 139 */     Transformer transformer = factory.newTransformer();
/* 140 */     DOMSource domSource = new DOMSource(document);
/* 141 */     StringWriter writer = new StringWriter();
/* 142 */     StreamResult result = new StreamResult(writer);
/* 143 */     transformer.transform(domSource, result);
/* 144 */     Strresult = writer.toString();
/* 145 */     return Strresult;
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\xml_generator\mcpttinfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */