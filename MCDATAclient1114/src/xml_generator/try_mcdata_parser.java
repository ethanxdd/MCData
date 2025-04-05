package xml_generator;

import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class try_mcdata_parser {
  public String string_try_mcdata_signalling_get(String xmlcontext, String title) {
    InputSource is = new InputSource(new StringReader(xmlcontext));
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    String token = null;
    try {
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(is);
      doc.getDocumentElement().normalize();
      token = doc.getDocumentElement().getElementsByTagName(title).item(0).getTextContent();
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return token;
  }
  
  public String string_try_mcdata_info_get(String xmlcontext, String title) {
    InputSource is = new InputSource(new StringReader(xmlcontext));
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    String token = null;
    try {
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(is);
      doc.getDocumentElement().normalize();
      token = doc.getDocumentElement().getElementsByTagName(title).item(0).getTextContent();
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return token;
  }
  
  public String string_try_mcdata_info_get_groupURI(String xmlcontext) {
    InputSource is = new InputSource(new StringReader(xmlcontext));
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    String token = null;
    try {
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(is);
      doc.getDocumentElement().normalize();
      token = doc.getDocumentElement().getElementsByTagName("mcdataURI").item(0).getTextContent();
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return token;
  }
  
  public String string_try_mcdata_payload_get(String xmlcontext, String title) {
    InputSource is = new InputSource(new StringReader(xmlcontext));
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    String token = null;
    try {
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(is);
      doc.getDocumentElement().normalize();
      token = doc.getDocumentElement().getElementsByTagName(title).item(0).getTextContent();
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return token;
  }
}
