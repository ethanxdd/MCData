package xml_generator;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.OutputStreamWriter;

public class try_mcdatapayload_factory {
	public String string_try_mcdatapaylaod_factory(String request_type, String client_id, String content, String payloadtype, Boolean type) {
		String Strresult = "";
	    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	    try {
	      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	      Document doc = docBuilder.newDocument();
	      Element rootElement = doc.createElement("mcdatapayload");
	      doc.appendChild(rootElement);
	      rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
	      Element type_request = doc.createElement("message-type");
	      rootElement.appendChild(type_request);
	      type_request.setTextContent("00000011");
	      Element mcdata_num_payloads = doc.createElement("number-of-payloads");
	      rootElement.appendChild(mcdata_num_payloads);
	      mcdata_num_payloads.setTextContent("1");
	      Element mcdata_payload = doc.createElement("payload");
	      rootElement.appendChild(mcdata_payload);
	      Element mcdata_content_type = doc.createElement("content-type");
	      mcdata_payload.appendChild(mcdata_content_type);
	      String pt="";
	      switch(payloadtype) {
	      	case "TEXT":
	      		pt="00000001";
	      		break;
	      	case "BINARY":
	      		pt="00000010";
	      		break;
	      	case "HYPERLINKS":
	      		pt="00000011";
	      		break;
	      	case "FILEURL":
	      		pt="00000100";
	      		break;
	      }
	      mcdata_content_type.setTextContent(pt);
	      Element mcdata_data = doc.createElement("data");
	      mcdata_payload.appendChild(mcdata_data);
	      if(type) {
	    	  content="user, "+content;
	      }else {
	    	  content="application, "+content;
	      }
	      mcdata_data.setTextContent(content);
	      System.out.println("777"+content);
	      /////
	      TransformerFactory transformerFactory = TransformerFactory.newInstance();
	      Transformer transformer = transformerFactory.newTransformer();
	      
	      transformer.setOutputProperty("encoding", "UTF-8");
          transformer.setOutputProperty("indent", "yes");
          
          ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
          StreamResult result = new StreamResult(outputStream);
          DOMSource source = new DOMSource(doc);

          transformer.transform(source, result);

          // 将 ByteArrayOutputStream 转换为字符串
          try {
              Strresult = outputStream.toString("UTF-8");
              Strresult = new String(Strresult.getBytes("UTF-8"), "UTF-8");
          } catch (UnsupportedEncodingException e) {
              e.printStackTrace();  // 捕获异常并输出
          }
          
          return Strresult;
//	      DOMSource source = new DOMSource(doc);
//	      StringWriter writer = new StringWriter();
//	      StreamResult result = new StreamResult(writer);
//	      
//	      transformer.transform(source, result);
//	      Strresult = writer.toString();
//	      return Strresult;
	    } catch (ParserConfigurationException e) {
	      e.printStackTrace();
	    } catch (TransformerConfigurationException e) {
	      e.printStackTrace();
	    } catch (TransformerException e) {
	      e.printStackTrace();
	    } 
	    return null;
	   }
	
	
//	public String string_try_mcdatapaylaod_factory(String request_type, String receiver, String groupID, String initiater) {
//	    String Strresult = "";
//	    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//	    try {
//	      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//	      Document doc = docBuilder.newDocument();
//	      Element rootElement = doc.createElement("mcdatapayload");
//	      doc.appendChild(rootElement);
//	      rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
//	      Element type_request = doc.createElement("message-type");
//	      rootElement.appendChild(type_request);
//	      type_request.setTextContent("DATA PAYLOAD");
//	      Element mcdata_num_payloads = doc.createElement("number-of-payloads");
//	      rootElement.appendChild(mcdata_num_payloads);
//	      mcdata_num_payloads.setTextContent("1");
//	      Element mcdata_payload = doc.createElement("payload");
//	      rootElement.appendChild(mcdata_payload);
//	      Element mcdata_content_type = doc.createElement("content-type");
//	      mcdata_payload.appendChild(mcdata_content_type);
//	      mcdata_content_type.setTextContent("TEXT");
//	      Element mcdata_data = doc.createElement("data");
//	      mcdata_payload.appendChild(mcdata_data);
//	      mcdata_data.setTextContent("Hello my friend");
//	      
//	      /////
//	      TransformerFactory transformerFactory = TransformerFactory.newInstance();
//	      Transformer transformer = transformerFactory.newTransformer();
//	      DOMSource source = new DOMSource(doc);
//	      StringWriter writer = new StringWriter();
//	      StreamResult result = new StreamResult(writer);
//	      transformer.transform(source, result);
//	      Strresult = writer.toString();
//	      return Strresult;
//	    } catch (ParserConfigurationException e) {
//	      e.printStackTrace();
//	    } catch (TransformerConfigurationException e) {
//	      e.printStackTrace();
//	    } catch (TransformerException e) {
//	      e.printStackTrace();
//	    } 
//	    return null;
//	  }
}
