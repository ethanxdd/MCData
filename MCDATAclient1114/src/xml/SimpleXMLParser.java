/*    */ package xml;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import javax.xml.parsers.ParserConfigurationException;
/*    */ import javax.xml.parsers.SAXParser;
/*    */ import javax.xml.parsers.SAXParserFactory;
/*    */ import org.xml.sax.InputSource;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.XMLReader;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpleXMLParser
/*    */ {
/*    */   protected SimpleXMLParsingHandler xmlParsingHandler;
/*    */   
/*    */   public SimpleXMLParser(SimpleXMLParsingHandler simpleXMLParsingHandler) {
/* 21 */     this.xmlParsingHandler = simpleXMLParsingHandler;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object[] getData(InputStream inputStream) throws ParserConfigurationException, SAXException, IOException {
/* 27 */     SAXParserFactory spf = SAXParserFactory.newInstance();
/* 28 */     SAXParser sp = spf.newSAXParser();
/* 29 */     XMLReader xmlReader = sp.getXMLReader();
/*    */     
/* 31 */     if (this.xmlParsingHandler == null) {
/* 32 */       throw new NullPointerException("xmlParsingHandler is null");
/*    */     }
/* 34 */     xmlReader.setContentHandler(this.xmlParsingHandler);
/* 35 */     xmlReader.parse(new InputSource(inputStream));
/* 36 */     Object[] data = (Object[])this.xmlParsingHandler.getParsedData();
/*    */     
/* 38 */     inputStream.close();
/* 39 */     return data;
/*    */   }
/*    */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\xml\SimpleXMLParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */