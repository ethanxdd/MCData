package xml_generator;

import java.io.StringWriter;

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

public class try_mcdatainfo_factory {
	public String string_try_mcdatainfo_factory(String request_type, String client_id, String mcdatatype) {//one to one
		String Strresult = "";
	    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	    try {
	    	DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	        Document doc = docBuilder.newDocument();
	        Element rootElement = doc.createElement("mcdatainfo");
	        doc.appendChild(rootElement);
	        rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
	        Element mcdata_params = doc.createElement("mcdata-Params");
	        rootElement.appendChild(mcdata_params);
	        Element type_request = doc.createElement("request-type");
	        mcdata_params.appendChild(type_request);
	        type_request.setTextContent("one-to-one-"+mcdatatype);//fd
	        Element calling_user = doc.createElement("mcdata-calling-user-id");
	        mcdata_params.appendChild(calling_user);
	        calling_user.setTextContent(client_id);//fd
	        //amyExt element if alias or app priority
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
	
	public String string_try_mcdatainfo_factory(String request_type, String groupID, String initiater, String mcdatatype) {
	    String Strresult = "";
	    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	    try {
	        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	        Document doc = docBuilder.newDocument();
		    Element rootElement = doc.createElement("mcdatainfo");
		    doc.appendChild(rootElement);
		    rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		    Element mcdata_params = doc.createElement("mcdata-Params");
		    rootElement.appendChild(mcdata_params);
		    Element type_request = doc.createElement("request-type");
		    mcdata_params.appendChild(type_request);
		    type_request.setTextContent("group-"+mcdatatype);//fd
		    Element mcdata_req_uri = doc.createElement("mcdata-request-uri");
		    mcdata_req_uri.setAttribute("type", request_type);
		    mcdata_params.appendChild(mcdata_req_uri);
		    Element mcdata_uri_2 = doc.createElement("mcdataURI");//24.282 D.1.3
		    mcdata_req_uri.appendChild(mcdata_uri_2);
		    mcdata_uri_2.setTextContent(groupID);
		    Element mcdata_client_id = doc.createElement("mcdata-client-id");//mcdata-calling-user-id
		    mcdata_client_id.setAttribute("type", request_type);
		    mcdata_params.appendChild(mcdata_client_id);
		    Element calling_user = doc.createElement("mcdata-calling-user-id");
	        mcdata_params.appendChild(calling_user);
	        calling_user.setTextContent(initiater);//fd
		    Element mcdata_uri_3 = doc.createElement("mcdataURI");
		    mcdata_client_id.appendChild(mcdata_uri_3);
		    mcdata_uri_3.setTextContent(initiater);
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
	
	public String string_try_mcdatainfo_factory(String groupID, String callinguser) {
	    String Strresult = "";
	    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	    try {
	        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	        Document doc = docBuilder.newDocument();
		    Element rootElement = doc.createElement("mcdatainfo");
		    doc.appendChild(rootElement);
		    rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		    Element mcdata_params = doc.createElement("mcdata-Params");
		    rootElement.appendChild(mcdata_params);
		    if(groupID== "null") {
			    Element mcdata_client_id = doc.createElement("mcdata-calling-group-id");//mcdata-calling-user-id
			    mcdata_params.appendChild(mcdata_client_id);
			    Element mcdata_uri_3 = doc.createElement("mcdataURI");
			    mcdata_client_id.appendChild(mcdata_uri_3);
			    mcdata_uri_3.setTextContent(groupID);
		    }
		    Element callinguserid = doc.createElement("mcdata-calling-user-id");//mcdata-calling-user-id
		    mcdata_params.appendChild(callinguserid);
		    callinguserid.setTextContent(callinguser);
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