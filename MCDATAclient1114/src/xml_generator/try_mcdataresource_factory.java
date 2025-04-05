package xml_generator;

import java.io.StringWriter;
import java.util.List;

import javax.sip.address.Address;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class try_mcdataresource_factory {	
	
	public String string_try_mcdataresource_factory(String request_type, String receiver) {
	    String Strresult = "";
	    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	    try {
	        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	        Document doc = docBuilder.newDocument();
  	        Element rootElement = doc.createElement("mcdataresource");
	        doc.appendChild(rootElement);
	        rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
	        Element type_request = doc.createElement("resource-lists");
	        rootElement.appendChild(type_request);
	        Element mcdata_num_payloads = doc.createElement("list");
	        type_request.appendChild(mcdata_num_payloads);
	        Element mcdata_payload = doc.createElement("entry");
	        mcdata_payload.setAttribute("uri", receiver);
	        mcdata_num_payloads.appendChild(mcdata_payload);
	      
	      
	      /////
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        DOMSource source = new DOMSource(doc);
	        StringWriter writer = new StringWriter();
	        StreamResult result = new StreamResult(writer);
	        transformer.transform(source, result);
	        Strresult = writer.toString();
	        return Strresult;
	    } catch (ParserConfigurationException e) {
	        e.printStackTrace();
	    } catch (TransformerConfigurationException e) {
	        e.printStackTrace();
	    } catch (TransformerException e) {
	        e.printStackTrace();
	    } 
	    return null; 
    }
	
}
