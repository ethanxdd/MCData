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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class pidfPlusxml
/*     */ {
/*     */   public void pidf_Plus_xml_tuple_builder_without_mcpttPI(String xml, String title, String tuple_id) throws SAXException, IOException, ParserConfigurationException, TransformerException {
/*  34 */     File xmlFile = new File(xml);
/*  35 */     DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
/*  36 */     DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
/*  37 */     Document document = documentBuilder.parse(xmlFile);
/*     */     
/*  39 */     NodeList nList = document.getElementsByTagName(title);
/*  40 */     Node node = nList.item(0);
/*  41 */     Element Title = (Element)node;
/*  42 */     Title.setAttribute("id", tuple_id);
/*     */     
/*  44 */     TransformerFactory factory = TransformerFactory.newInstance();
/*  45 */     Transformer transformer = factory.newTransformer();
/*  46 */     DOMSource domSource = new DOMSource(document);
/*  47 */     StreamResult streamResult = new StreamResult(new File(xml));
/*  48 */     transformer.transform(domSource, streamResult);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String string_pidf_Plus_xml_tuple_builder_without_mcpttPI(String xmlcontext, String title, String tuple_id) throws SAXException, IOException, ParserConfigurationException, TransformerException {
/*  54 */     String Strresult = "";
/*  55 */     InputSource is = new InputSource(new StringReader(xmlcontext));
/*  56 */     DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
/*  57 */     DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
/*  58 */     Document document = documentBuilder.parse(is);
/*     */     
/*  60 */     NodeList nList = document.getElementsByTagName(title);
/*  61 */     Node node = nList.item(0);
/*  62 */     Element Title = (Element)node;
/*  63 */     Title.setAttribute("id", tuple_id);
/*     */     
/*  65 */     TransformerFactory factory = TransformerFactory.newInstance();
/*  66 */     Transformer transformer = factory.newTransformer();
/*  67 */     DOMSource domSource = new DOMSource(document);
/*  68 */     StringWriter writer = new StringWriter();
/*  69 */     StreamResult result = new StreamResult(writer);
/*  70 */     transformer.transform(domSource, result);
/*  71 */     Strresult = writer.toString();
/*  72 */     return Strresult;
/*     */   }
/*     */ 
/*     */   
/*     */   public void pidf_Plus_xml_group_builder(String xml, String title, String target, String sip_group, String stateid) throws SAXException, IOException, ParserConfigurationException, TransformerException {
/*  77 */     File xmlFile = new File(xml);
/*  78 */     DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
/*  79 */     DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
/*  80 */     Document document = documentBuilder.parse(xmlFile);
/*     */     
/*  82 */     NodeList nList = document.getElementsByTagName(title);
/*  83 */     Node node = nList.item(0);
/*  84 */     Element Title = (Element)node;
/*  85 */     Element mcpttPI = document.createElement(target);
/*  86 */     Title.appendChild(mcpttPI);
/*  87 */     mcpttPI.setAttribute("group", sip_group);
/*  88 */     mcpttPI.setAttribute("status", stateid);
/*     */     
/*  90 */     TransformerFactory factory = TransformerFactory.newInstance();
/*  91 */     Transformer transformer = factory.newTransformer();
/*  92 */     DOMSource domSource = new DOMSource(document);
/*  93 */     StreamResult streamResult = new StreamResult(new File(xml));
/*  94 */     transformer.transform(domSource, streamResult);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String string_pidf_Plus_xml_group_builder(String xmlcontext, String title, String target, String sip_group, String stateid) throws SAXException, IOException, ParserConfigurationException, TransformerException {
/* 100 */     String Strresult = "";
/* 101 */     InputSource is = new InputSource(new StringReader(xmlcontext));
/* 102 */     DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
/* 103 */     DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
/* 104 */     Document document = docBuilder.parse(is);
/*     */     
/* 106 */     NodeList nList = document.getElementsByTagName(title);
/* 107 */     Node node = nList.item(0);
/* 108 */     Element Title = (Element)node;
/* 109 */     Element mcpttPI = document.createElement(target);
/* 110 */     Title.appendChild(mcpttPI);
/* 111 */     mcpttPI.setAttribute("group", sip_group);
/* 112 */     mcpttPI.setAttribute("status", stateid);
/*     */     
/* 114 */     TransformerFactory factory = TransformerFactory.newInstance();
/* 115 */     Transformer transformer = factory.newTransformer();
/* 116 */     DOMSource domSource = new DOMSource(document);
/* 117 */     StringWriter writer = new StringWriter();
/* 118 */     StreamResult result = new StreamResult(writer);
/* 119 */     transformer.transform(domSource, result);
/* 120 */     Strresult = writer.toString();
/* 121 */     return Strresult;
/*     */   }
/*     */ 
/*     */   
/*     */   public void pidf_Plus_xml_group_builder(String xml, String title, String target, String sip_group) throws SAXException, IOException, ParserConfigurationException, TransformerException {
/* 126 */     File xmlFile = new File(xml);
/* 127 */     DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
/* 128 */     DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
/* 129 */     Document document = documentBuilder.parse(xmlFile);
/*     */     
/* 131 */     NodeList nList = document.getElementsByTagName(title);
/* 132 */     Node node = nList.item(0);
/* 133 */     Element Title = (Element)node;
/* 134 */     Element mcpttPI = document.createElement(target);
/* 135 */     Title.appendChild(mcpttPI);
/* 136 */     mcpttPI.setAttribute("group", sip_group);
/*     */     
/* 138 */     TransformerFactory factory = TransformerFactory.newInstance();
/* 139 */     Transformer transformer = factory.newTransformer();
/* 140 */     DOMSource domSource = new DOMSource(document);
/* 141 */     StreamResult streamResult = new StreamResult(new File(xml));
/* 142 */     transformer.transform(domSource, streamResult);
/*     */   }
/*     */ 
/*     */   
/*     */   public String string_pidf_Plus_xml_group_builder(String xmlcontext, String title, String target, String sip_group) throws SAXException, IOException, ParserConfigurationException, TransformerException {
/* 147 */     String Strresult = "";
/* 148 */     InputSource is = new InputSource(new StringReader(xmlcontext));
/* 149 */     DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
/* 150 */     DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
/* 151 */     Document document = documentBuilder.parse(is);
/*     */     
/* 153 */     NodeList nList = document.getElementsByTagName(title);
/* 154 */     Node node = nList.item(0);
/* 155 */     Element Title = (Element)node;
/* 156 */     Element mcpttPI = document.createElement(target);
/* 157 */     Title.appendChild(mcpttPI);
/* 158 */     mcpttPI.setAttribute("group", sip_group);
/*     */     
/* 160 */     TransformerFactory factory = TransformerFactory.newInstance();
/* 161 */     Transformer transformer = factory.newTransformer();
/* 162 */     DOMSource domSource = new DOMSource(document);
/* 163 */     StringWriter writer = new StringWriter();
/* 164 */     StreamResult result = new StreamResult(writer);
/* 165 */     transformer.transform(domSource, result);
/* 166 */     Strresult = writer.toString();
/* 167 */     return Strresult;
/*     */   }
/*     */   
/*     */   public void mcptt_adder(String xml, String pid, String title) throws SAXException, IOException, ParserConfigurationException, TransformerException {
/* 171 */     File xmlFile = new File(xml);
/* 172 */     DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
/* 173 */     DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
/* 174 */     Document document = documentBuilder.parse(xmlFile);
/*     */     
/* 176 */     NodeList nList = document.getElementsByTagName(title);
/* 177 */     Node node = nList.item(0);
/* 178 */     Element Title = (Element)node;
/* 179 */     Title.setTextContent(pid);
/*     */     
/* 181 */     TransformerFactory factory = TransformerFactory.newInstance();
/* 182 */     Transformer transformer = factory.newTransformer();
/* 183 */     DOMSource domSource = new DOMSource(document);
/* 184 */     StreamResult streamResult = new StreamResult(new File(xml));
/* 185 */     transformer.transform(domSource, streamResult);
/*     */   }
/*     */ 
/*     */   
/*     */   public String string_mcptt_adder(String xmlcontext, String pid, String title) throws SAXException, IOException, ParserConfigurationException, TransformerException {
/* 190 */     String Strresult = "";
/* 191 */     InputSource is = new InputSource(new StringReader(xmlcontext));
/* 192 */     DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
/* 193 */     DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
/* 194 */     Document document = documentBuilder.parse(is);
/*     */     
/* 196 */     NodeList nList = document.getElementsByTagName(title);
/* 197 */     Node node = nList.item(0);
/* 198 */     Element Title = (Element)node;
/* 199 */     Title.setTextContent(pid);
/*     */     
/* 201 */     TransformerFactory factory = TransformerFactory.newInstance();
/* 202 */     Transformer transformer = factory.newTransformer();
/* 203 */     DOMSource domSource = new DOMSource(document);
/* 204 */     StringWriter writer = new StringWriter();
/* 205 */     StreamResult result = new StreamResult(writer);
/* 206 */     transformer.transform(domSource, result);
/* 207 */     Strresult = writer.toString();
/* 208 */     return Strresult;
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\xml_generator\pidfPlusxml.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */