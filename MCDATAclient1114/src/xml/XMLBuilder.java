/*     */ package xml;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.util.LinkedList;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerConfigurationException;
/*     */ import javax.xml.transform.sax.SAXTransformerFactory;
/*     */ import javax.xml.transform.sax.TransformerHandler;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.AttributesImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XMLBuilder
/*     */ {
/*  23 */   private static String TAG = "XMLBuilder";
/*     */   private ByteArrayOutputStream outputStream;
/*     */   private Result result;
/*     */   private SAXTransformerFactory saxTransformerFactory;
/*     */   private TransformerHandler transformerHandle;
/*     */   private Transformer transformer;
/*     */   private LinkedList<Element> elementList;
/*     */   private boolean debugMode = false;
/*     */   
/*     */   public XMLBuilder() {
/*     */     try {
/*  34 */       initialize();
/*  35 */     } catch (TransformerConfigurationException transformerConfigurationException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void initialize() throws TransformerConfigurationException {
/*  41 */     this.outputStream = new ByteArrayOutputStream();
/*  42 */     this.result = new StreamResult(this.outputStream);
/*  43 */     this.saxTransformerFactory = (SAXTransformerFactory)SAXTransformerFactory.newInstance();
/*  44 */     this.transformerHandle = this.saxTransformerFactory.newTransformerHandler();
/*  45 */     this.transformerHandle.setResult(this.result);
/*  46 */     this.transformer = this.transformerHandle.getTransformer();
/*  47 */     this.elementList = new LinkedList<>();
/*     */   }
/*     */   
/*     */   public void setOutputProperty(String name, String value) {
/*  51 */     this.transformer.setOutputProperty(name, value);
/*     */   }
/*     */   
/*     */   public void startElement(String uri, String localName, String qName) throws SAXException {
/*  55 */     AttributesImpl attributesImpl = new AttributesImpl();
/*  56 */     Element element = new Element(uri, localName, qName, attributesImpl, 1001);
/*  57 */     this.elementList.add(element);
/*     */   }
/*     */   
/*     */   public void startElement(String uri, String localName, String qName, AttributeMap attributeMap) throws SAXException {
/*  61 */     AttributesImpl attributesImpl = new AttributesImpl();
/*  62 */     for (Attribute a : attributeMap.getAttributeList()) {
/*  63 */       attributesImpl.addAttribute(a.getUri(), a.getLocalName(), a.getqName(), a.getType(), a.getValue());
/*     */     }
/*  65 */     Element element = new Element(uri, localName, qName, attributesImpl, 1001);
/*  66 */     this.elementList.add(element);
/*     */   }
/*     */   
/*     */   public void startElement(String uri, String localName, String qName, Character character) throws SAXException {
/*  70 */     AttributesImpl attributesImpl = new AttributesImpl();
/*  71 */     Element element = new Element(uri, localName, qName, attributesImpl, 1003);
/*  72 */     element.setCharacter(character);
/*  73 */     this.elementList.add(element);
/*     */   }
/*     */   
/*     */   public void startElement(String uri, String localName, String qName, AttributeMap attributeMap, Character character) throws SAXException {
/*  77 */     AttributesImpl attributesImpl = new AttributesImpl();
/*  78 */     for (Attribute a : attributeMap.getAttributeList()) {
/*  79 */       attributesImpl.addAttribute(a.getUri(), a.getLocalName(), a.getqName(), a.getType(), a.getValue());
/*     */     }
/*  81 */     Element element = new Element(uri, localName, qName, attributesImpl, 1003);
/*  82 */     element.setCharacter(character);
/*  83 */     this.elementList.add(element);
/*     */   }
/*     */   
/*     */   public void endElement(String uri, String localName, String qName) {
/*  87 */     Element element = new Element(uri, localName, qName, null, 1002);
/*  88 */     this.elementList.add(element);

		
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteArrayOutputStream build() throws SAXException {
/*     */     try {
/*  94 */       printDebugMSG("XML Build Start");
/*  95 */       this.transformerHandle.startDocument();
/*  96 */       while (!this.elementList.isEmpty()) {
					
/*  97 */         Character c; 
					Element e = this.elementList.pop();
/*  98 */         switch (e.getElementState()) {
/*     */           case 1001:
/* 100 */             this.transformerHandle.startElement(e.getUri(), e.getLocalName(), e.getqName(), e.getAtts());
/* 101 */             printDebugMSG("Start Element : " + e.getqName());
/*     */           	break;
/*     */           case 1003:
/* 104 */             c = e.getCharacter();
						if (c != null ) {
						    this.transformerHandle.startElement(e.getUri(), e.getLocalName(), e.getqName(), e.getAtts());
						    this.transformerHandle.characters(c.getCh(), c.getStart(), c.getLength());
						    printDebugMSG("Start Element with Char : " + e.getqName() + " , " + String.valueOf(c.getCh()));
						} else {
						    // 處理空字符的情況
						    printDebugMSG("Character data is null for element: " + e.getqName());
						}
						break;
						//System.out.println("c"+c);
/* 105 */             //this.transformerHandle.startElement(e.getUri(), e.getLocalName(), e.getqName(), e.getAtts());
/* 106 */             //this.transformerHandle.characters(c.getCh(), c.getStart(), c.getLength());
/* 107 */            // printDebugMSG("Start Element with Char : " + e.getqName() + " , " + String.valueOf(c.getCh()));
/*     */           
/*     */           case 1002:
/* 110 */             this.transformerHandle.endElement(e.getUri(), e.getLocalName(), e.getqName());
/* 111 */             printDebugMSG("End Element : " + e.getqName());
						break;
/*     */         } 
/*     */       
/*     */       } 
/* 115 */       this.transformerHandle.endDocument();
/* 116 */       printDebugMSG("XML Build End");
/*     */       
/* 118 */       return this.outputStream;
/* 119 */     } catch (SAXException e) {
/* 120 */       throw new SAXException("XML build fail : " + e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setDebugMode(boolean debugMode) {
/* 125 */     this.debugMode = debugMode;
/*     */   }
/*     */   
/*     */   public boolean isDebugModeOn() {
/* 129 */     return this.debugMode;
/*     */   }
/*     */   
/*     */   private void printDebugMSG(String debugMessage) {
/* 133 */     isDebugModeOn();
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\xml\XMLBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */