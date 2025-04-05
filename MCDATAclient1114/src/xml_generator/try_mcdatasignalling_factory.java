package xml_generator;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.UUID;

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

public class try_mcdatasignalling_factory {
	public String string_try_mcdatasignalling_factory(String request_type, String client_id, String conversationid) {
		String Strresult = "";
	    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	    try {
	      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	      Document doc = docBuilder.newDocument();
	      Element rootElement = doc.createElement("mcdatasignalling");
	      doc.appendChild(rootElement);
	      rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
	      Element mcdata_params = doc.createElement("mcdata-Params");
	      rootElement.appendChild(mcdata_params);
	      Element type_request = doc.createElement("message-type");
	      mcdata_params.appendChild(type_request);
	      type_request.setTextContent("00000001");
	      Element mcdata_req_uri = doc.createElement("time");
	      mcdata_params.appendChild(mcdata_req_uri);
	      long time1 = Calendar.getInstance().getTimeInMillis();
	      String time = Long.toString(time1);
	      mcdata_req_uri.setTextContent(time);
	      
	      Element id_request = doc.createElement("mcdata-request-id");
	      mcdata_params.appendChild(id_request);
	      id_request.setAttribute("type", "normal");
//	      mcdata_params.appendChild(type_request);
	      Element messageID = doc.createElement("messageID");
	      UUID messageID_uuid = UUID.randomUUID();
	      String messageID_uuidAsString = messageID_uuid.toString();
	      messageID.setTextContent(messageID_uuidAsString);
	      id_request.appendChild(messageID);
	      Element conversationID = doc.createElement("conversationID");
//	      UUID conversationID_uuid = UUID.randomUUID();
//	      String conversationID_uuidAsString = conversationID_uuid.toString();
//	      conversationID.setTextContent(conversationID_uuidAsString);
	      conversationID.setTextContent(conversationid);
	      id_request.appendChild(conversationID);
	      Element senderID = doc.createElement("sendermcdatauserID");
	      senderID.setTextContent(client_id);
	      id_request.appendChild(senderID);
	      /////2021/07/15 04:27:37 
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
	//FD:data, conversation, message id, inreply id?, app id?, extended app id?,  FD disposition request type? mandatory download? payload:FILEURL, metadata? app metadata?
	//2 8 9 10/ 11 7 4 16 13 17 24 25 15 28
	public String string_try_mcdatasignalling_factory_fd(String request_type, String client_id, String conversationid, String fileurl, Boolean type, Boolean type2) {
		String Strresult = "";
	    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	    try {
	      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	      Document doc = docBuilder.newDocument();
	      Element rootElement = doc.createElement("mcdatasignalling");
	      doc.appendChild(rootElement);
	      rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
	      Element mcdata_params = doc.createElement("mcdata-Params");
	      rootElement.appendChild(mcdata_params);
	      Element type_request = doc.createElement("message-type");//
	      mcdata_params.appendChild(type_request);
	      type_request.setTextContent("00000010");
	      Element mcdata_req_uri = doc.createElement("time");//
	      mcdata_params.appendChild(mcdata_req_uri);
	      long time1 = Calendar.getInstance().getTimeInMillis();
	      String time = Long.toString(time1);
	      mcdata_req_uri.setTextContent(time);
	      
	      if(type2) {
		      Element mandatory = doc.createElement("mandatory-download");
		      mcdata_params.appendChild(mandatory);
		      mandatory.setTextContent("0001");
	      }
	      
//	      Element disposition_request_type = doc.createElement("fd-disposition-request-type");
//	      mcdata_params.appendChild(disposition_request_type);
//	      disposition_request_type.setTextContent("0001");
	      
//	      Element applicationid = doc.createElement("ApplicationID");
//	      mcdata_params.appendChild(applicationid);
//	      applicationid.setTextContent("00000001");
//	      Element inreplyid = doc.createElement("InReplyTo message ID");
//	      mcdata_params.appendChild(inreplyid);
//	      inreplyid.setTextContent("00000010");
	      Element mcdata_payload = doc.createElement("payload");
	      mcdata_params.appendChild(mcdata_payload);
	      Element mcdata_content_type = doc.createElement("content-type");
	      mcdata_payload.appendChild(mcdata_content_type);
	      String pt="00000100";
	      
	      mcdata_content_type.setTextContent(pt);
	      Element mcdata_data = doc.createElement("data");
	      mcdata_payload.appendChild(mcdata_data);
	      if(type) {
	    	  fileurl="user, "+fileurl;
	      }else {
	    	  fileurl="application, "+fileurl;
	      }
	      mcdata_data.setTextContent(fileurl);//??the Payload content type set to "FILEURL
//	      Element user_id = doc.createElement("MCData-userID");
//	      mcdata_params.appendChild(user_id);
//	      user_id.setTextContent("00000010");
//	      Element Mandatory = doc.createElement("Mandatory");
//	      mcdata_params.appendChild(Mandatory);
//	      Mandatory.setTextContent("0001");
//	      Element Metadata = doc.createElement("Metadata");
//	      mcdata_params.appendChild(Metadata);
//	      Metadata.setTextContent("00000010");//??
//	      Element Extended_applicationID = doc.createElement("Extended application ID");
//	      mcdata_params.appendChild(Extended_applicationID);
//	      Extended_applicationID.setTextContent("00000010");//??
//	      Element location = doc.createElement("User location");
//	      mcdata_params.appendChild(location);
//	      location.setTextContent("00000010");
//	      Element app_metadata = doc.createElement("app_metadata");
//	      mcdata_params.appendChild(app_metadata);
//	      app_metadata.setTextContent("00000010");
	      
	      
	      Element id_request = doc.createElement("mcdata-request-id");
	      mcdata_params.appendChild(id_request);
	      id_request.setAttribute("type", "normal");
	      mcdata_params.appendChild(type_request);
	      Element messageID = doc.createElement("messageID");//
	      UUID messageID_uuid = UUID.randomUUID();
	      String messageID_uuidAsString = messageID_uuid.toString();
	      messageID.setTextContent(messageID_uuidAsString);
	      id_request.appendChild(messageID);
	      Element conversationID = doc.createElement("conversationID");//
//	      UUID conversationID_uuid = UUID.randomUUID();
//	      String conversationID_uuidAsString = conversationID_uuid.toString();
//	      conversationID.setTextContent(conversationID_uuidAsString);
	      conversationID.setTextContent(conversationid);
	      id_request.appendChild(conversationID);
	      /////2021/07/15 04:27:37 
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
	
	//FD NOTIFICATION message: disposition notification type, Conversation ID, Date and time IE,message type
	//iffile download completed notif: disposition notification type :FILE DOWNLOAD COMPLETED, Message ID, 
	public String string_try_mcdatasignalling_factory_not(String conversationid, String notification_type, String sipUserName) {
		String Strresult = "";
	    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	    try {
	      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	      Document doc = docBuilder.newDocument();
	      Element rootElement = doc.createElement("mcdatasignalling");
	      doc.appendChild(rootElement);
	      rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
	      Element mcdata_params = doc.createElement("mcdata-Params");
	      rootElement.appendChild(mcdata_params);
	      Element type_request = doc.createElement("message-type");//
	      mcdata_params.appendChild(type_request);
	      type_request.setTextContent("00000110");
	      Element senderuser = doc.createElement("sender-user");//
	      mcdata_params.appendChild(senderuser);
	      senderuser.setTextContent(sipUserName);
	      Element mcdata_req_uri = doc.createElement("time");//
	      mcdata_params.appendChild(mcdata_req_uri);
	      long time1 = Calendar.getInstance().getTimeInMillis();
	      String time = Long.toString(time1);
	      mcdata_req_uri.setTextContent(time);
	      Element type_notification = doc.createElement("disposition-notification-type");//
	      mcdata_params.appendChild(type_notification);
	      switch (notification_type) {
	          case "accept":
	        	  type_notification.setTextContent("00000001");
	              break;
	          case "download":
	        	  type_notification.setTextContent("00000011");
	              break;
	          case "rejected":
	        	  type_notification.setTextContent("00000010");
	              break;
	          case "deferred":
	        	  type_notification.setTextContent("00000100");
	              break;
	          default:
	              throw new IllegalStateException("Unexpected value: " + notification_type);
	      }
	      

	      Element id_request = doc.createElement("mcdata-request-id");
	      mcdata_params.appendChild(id_request);
	      id_request.setAttribute("type", "normal");
	      mcdata_params.appendChild(type_request);
	      Element messageID = doc.createElement("messageID");//2
	      UUID messageID_uuid = UUID.randomUUID();
	      String messageID_uuidAsString = messageID_uuid.toString();
	      messageID.setTextContent(messageID_uuidAsString);
	      id_request.appendChild(messageID);
	      Element conversationID = doc.createElement("conversationID");//

	      conversationID.setTextContent(conversationid);
	      id_request.appendChild(conversationID);
	      /////2021/07/15 04:27:37 
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
