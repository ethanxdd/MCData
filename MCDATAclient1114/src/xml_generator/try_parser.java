/*     */ package xml_generator;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.InputSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class try_parser
/*     */ {
/*     */   public List<String> try_pidf_get() {
/*  25 */     List<String> grouptoken = new ArrayList<>();
/*     */     try {
/*  27 */       File inputFile = new File("pidf.xml");
/*  28 */       DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
/*  29 */       DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
/*  30 */       Document doc = dBuilder.parse(inputFile);
/*  31 */       doc.getDocumentElement().normalize();
/*  32 */       NodeList nList = doc.getElementsByTagName("mcpttPI10:affiliation");
/*  33 */       for (int temp = 0; temp < nList.getLength(); temp++) {
/*  34 */         Node nNode = nList.item(temp);
/*     */         
/*  36 */         if (nNode.getNodeType() == 1) {
/*  37 */           Element eElement = (Element)nNode;
/*  38 */           System.out.println("group = " + eElement.getAttribute("group") + "status = " + eElement.getAttribute("status"));
/*  39 */           grouptoken.add(eElement.getAttribute("group").split("sip:")[1]);
/*     */         } 
/*     */       } 
/*  42 */     } catch (Exception e) {
/*  43 */       e.printStackTrace();
/*     */     } 
/*  45 */     return grouptoken;
/*     */   }

/*     */   public String try_pidf_pid_get() {
/*  48 */     File inputFile = new File("pidf.xml");
/*  49 */     DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
/*     */     
/*  51 */     String token = null;
/*     */     try {
/*  53 */       DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
/*  54 */       Document doc = dBuilder.parse(inputFile);
/*  55 */       doc.getDocumentElement().normalize();
/*  56 */       token = doc.getDocumentElement().getElementsByTagName("mcpttPI10:p-id").item(0).getTextContent();
/*     */     }
/*  58 */     catch (Exception e) {
/*     */       
/*  60 */       e.printStackTrace();
/*     */     } 
/*  62 */     return token;
/*     */   }

/*     */   public String try_mcptt_info_get() {
/*  65 */     File inputFile = new File("mcpttinfo.xml");
/*  66 */     DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
/*     */     
/*  68 */     String token = null;
/*     */     try {
/*  70 */       DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
/*  71 */       Document doc = dBuilder.parse(inputFile);
/*  72 */       doc.getDocumentElement().normalize();
/*  73 */       token = doc.getDocumentElement().getElementsByTagName("mcpttURI").item(0).getTextContent();
/*     */     }
/*  75 */     catch (Exception e) {
/*     */       
/*  77 */       e.printStackTrace();
/*     */     } 
/*  79 */     return token.split("sip:")[1];
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> string_try_pidf_get(String xmlcontext) {
/*  84 */     List<String> grouptoken = new ArrayList<>();
/*     */     try {
/*  86 */       InputSource is = new InputSource(new StringReader(xmlcontext));
/*  87 */       DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
/*  88 */       DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
/*  89 */       Document doc = dBuilder.parse(is);
/*  90 */       doc.getDocumentElement().normalize();
/*  91 */       NodeList nList = doc.getElementsByTagName("mcpttPI10:affiliation");
/*  92 */       for (int temp = 0; temp < nList.getLength(); temp++) {
/*  93 */         Node nNode = nList.item(temp);
/*     */         
/*  95 */         if (nNode.getNodeType() == 1) {
/*  96 */           Element eElement = (Element)nNode;
/*     */           
/*  98 */           grouptoken.add(eElement.getAttribute("group").split("sip:")[1]);
/*  99 */           grouptoken.add(eElement.getAttribute("status"));
/*     */         } 
/*     */       } 
/* 102 */     } catch (Exception e) {
/* 103 */       e.printStackTrace();
/*     */     } 
/* 105 */     return grouptoken;
/*     */   }

/*     */   public String string_try_pidf_pid_get(String xmlcontext) {
/* 108 */     InputSource is = new InputSource(new StringReader(xmlcontext));
/* 109 */     DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
/*     */     
/* 111 */     String token = null;
/*     */     try {
/* 113 */       DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
/* 114 */       Document doc = dBuilder.parse(is);
/* 115 */       doc.getDocumentElement().normalize();
/* 116 */       token = doc.getDocumentElement().getElementsByTagName("mcpttPI10:p-id").item(0).getTextContent();
/*     */     }
/* 118 */     catch (Exception e) {
/*     */       
/* 120 */       e.printStackTrace();
/*     */     } 
/* 122 */     return token;
/*     */   }
/*     */   public String string_try_mcptt_info_get(String xmlcontext) {
/* 125 */     InputSource is = new InputSource(new StringReader(xmlcontext));
/* 126 */     DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
/*     */     
/* 128 */     String token = null;
/*     */     try {
/* 130 */       DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
/* 131 */       Document doc = dBuilder.parse(is);
/* 132 */       doc.getDocumentElement().normalize();
/* 133 */       token = doc.getDocumentElement().getElementsByTagName("mcpttURI").item(0).getTextContent();
/*     */     }
/* 135 */     catch (Exception e) {
/*     */       
/* 137 */       e.printStackTrace();
/*     */     } 
/* 139 */     if (token.contains("sip:"))
/*     */     {
/* 141 */       return token.split("sip:")[1];
/*     */     }
/* 143 */     return token;
/*     */   }
/*     */   
/*     */   public String string_try_mcptt_info_get_each_element(String xmlcontext, String title) {
/* 147 */     InputSource is = new InputSource(new StringReader(xmlcontext));
/* 148 */     DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
/*     */     
/* 150 */     String token = null;
/*     */     try {
/* 152 */       DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
/* 153 */       Document doc = dBuilder.parse(is);
/* 154 */       doc.getDocumentElement().normalize();
/* 155 */       token = doc.getDocumentElement().getElementsByTagName(title).item(0).getTextContent();
/*     */     }
/* 157 */     catch (Exception e) {
/*     */       
/* 159 */       e.printStackTrace();
/*     */     } 
/* 161 */     return token;
/*     */   }

		public String string_try_mcdata_payload(String xmlcontext) {
/* 147 */     DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
/*     */     String boundary = "--boundary1"; // 边界标识符
				String[] parts = xmlcontext.split(boundary);
				String token = null;
				// 遍历每个部分
				for (String part : parts) {
				    if (part.contains("Content-Type: application/vnd.3gpp.mcdata-payload")) {
				    	int xmlStart = part.indexOf("<?xml");
				        if (xmlStart != -1) {
				        	part=part.substring(xmlStart).trim();
				        }
					    try {
					    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
							Document doc = dBuilder.parse(new InputSource(new StringReader(part)));
					        NodeList dataNodes = doc.getElementsByTagName("data");
						        if (dataNodes.getLength() > 0) {
						            Element dataElement = (Element) dataNodes.item(0);
						            token=dataElement.getTextContent(); 
						            
						        } else {
						            throw new Exception("No <data> element found in XML.");
						        }
					    }	
				    catch (Exception e) {
		      
				    		e.printStackTrace();
				    	} 
				    }
			    }
				System.out.println("[payload]"+token);
/* 161 */     return token;
		}
		
	public String string_try_mcdata_payload_content_type(String xmlcontext) {
/* 147 */     DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
/*     */     String boundary = "--boundary1"; // 边界标识符
				String[] parts = xmlcontext.split(boundary);
				String token = null;
				// 遍历每个部分
				for (String part : parts) {
				    if (part.contains("Content-Type: application/vnd.3gpp.mcdata-payload")) {
				    	int xmlStart = part.indexOf("<?xml");
				        if (xmlStart != -1) {
				        	part=part.substring(xmlStart).trim();
				        }
					    try {
					    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
							Document doc = dBuilder.parse(new InputSource(new StringReader(part)));
					        NodeList dataNodes = doc.getElementsByTagName("content-type");
						        if (dataNodes.getLength() > 0) {
						            Element dataElement = (Element) dataNodes.item(0);
						            token=dataElement.getTextContent(); 
						            
						        } else {
						            throw new Exception("No <data> element found in XML.");
						        }
					    }	
				    catch (Exception e) {
		      
				    		e.printStackTrace();
				    	} 
				    }
			    }
				System.out.println("[content_type]"+token);
/* 161 */     return token;
/*     */ }
	
	public String string_try_mcdata_signalling_senderid(String xmlcontext) {
		/* 147 */     DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		/*     */     String boundary = "--boundary1"; // 边界标识符
						String[] parts = xmlcontext.split(boundary);
						String token = null;
						// 遍历每个部分
						for (String part : parts) {
						    if (part.contains("Content-Type: application/vnd.3gpp.mcdata-signalling")) {
						    	int xmlStart = part.indexOf("<?xml");
						        if (xmlStart != -1) {
						        	part=part.substring(xmlStart).trim();
						        }
							    try {
							    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
									Document doc = dBuilder.parse(new InputSource(new StringReader(part)));
							        NodeList dataNodes = doc.getElementsByTagName("sendermcdatauserID");
								        if (dataNodes.getLength() > 0) {
								            Element dataElement = (Element) dataNodes.item(0);
								            token=dataElement.getTextContent(); 
								            
								        } else {
								            throw new Exception("No <data> element found in XML.");
								        }
							    }	
						    catch (Exception e) {
				      
						    		e.printStackTrace();
						    	} 
						    }
					    }
						System.out.println("[senderid]"+token);
		/* 161 */     return token;
		/*     */ }
	
	public String string_try_mcdata_signalling_conversationid(String xmlcontext) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		String boundary = "--boundary1"; // 边界标识符
		String[] parts = xmlcontext.split(boundary);
		String token = null;
		// 遍历每个部分
		for (String part : parts) {
		    if (part.contains("Content-Type: application/vnd.3gpp.mcdata-signalling")) {
		    	int xmlStart = part.indexOf("<?xml");
		        if (xmlStart != -1) {
		        	part=part.substring(xmlStart).trim();
		        }
			    try {
			    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(new InputSource(new StringReader(part)));
					NodeList dataNodes = doc.getElementsByTagName("conversationID");
					if (dataNodes.getLength() > 0) {
					    Element dataElement = (Element) dataNodes.item(0);
						token=dataElement.getTextContent(); 
				    } else {
				        throw new Exception("No <data> element found in XML.");
				    }
				}	
				catch (Exception e) {
					e.printStackTrace();
				} 
		    }
	    }
		System.out.println("[conversationid]"+token);
		return token;
    }
	
	public String string_try_mcdata_signalling_mandatory(String xmlcontext) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		String boundary = "--boundary1"; // 边界标识符
		String[] parts = xmlcontext.split(boundary);
		String token = "false";
		// 遍历每个部分
		for (String part : parts) {
		    if (part.contains("Content-Type: application/vnd.3gpp.mcdata-signalling")) {
		    	System.out.println("[sigfd]"+part);
		    	int xmlStart = part.indexOf("<?xml");
		        if (xmlStart != -1) {
		        	part=part.substring(xmlStart).trim();
		        }
			    try {
			    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(new InputSource(new StringReader(part)));
					NodeList dataNodes = doc.getElementsByTagName("mandatory-download");
					if (dataNodes.getLength() > 0) {
//					    Element dataElement = (Element) dataNodes.item(0);
//						token=dataElement.getTextContent(); 
						token = "true";
				    } else {
				    	token = "false";
				    }
				}	
				catch (Exception e) {
					e.printStackTrace();
				} 
		    }
	    }
		System.out.println("[conversationid]"+token);
		return token;
    }
	
	public String string_try_mcdata_notification(String xmlcontext) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		String boundary = "--boundary1"; // 边界标识符
		String[] parts = xmlcontext.split(boundary);
		String token = "false";
		// 遍历每个部分
		for (String part : parts) {
		    if (part.contains("Content-Type: application/vnd.3gpp.mcdata-signalling")) {
		    	int xmlStart = part.indexOf("<?xml");
		        if (xmlStart != -1) {
		        	part=part.substring(xmlStart).trim();
		        }
			    try {
			    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(new InputSource(new StringReader(part)));
					NodeList dataNodes = doc.getElementsByTagName("disposition-notification-type");
					if (dataNodes.getLength() > 0) {
					    Element dataElement = (Element) dataNodes.item(0);
						token=dataElement.getTextContent(); 
				    } else {
				    	token = "false";
				    }
				}	
				catch (Exception e) {
					e.printStackTrace();
				} 
		    }
	    }
		return token;
    }
	
	public String string_try_mcdata_sender(String xmlcontext) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		String boundary = "--boundary1"; // 边界标识符
		String[] parts = xmlcontext.split(boundary);
		String token = "false";
		// 遍历每个部分
		for (String part : parts) {
		    if (part.contains("Content-Type: application/vnd.3gpp.mcdata-signalling")) {
		    	System.out.println("[sigfd]"+part);
		    	int xmlStart = part.indexOf("<?xml");
		        if (xmlStart != -1) {
		        	part=part.substring(xmlStart).trim();
		        }
			    try {
			    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(new InputSource(new StringReader(part)));
					NodeList dataNodes = doc.getElementsByTagName("sender-user");
					if (dataNodes.getLength() > 0) {
					    Element dataElement = (Element) dataNodes.item(0);
						token=dataElement.getTextContent(); 
				    } else {
				    	token = "false";
				    }
				}	
				catch (Exception e) {
					e.printStackTrace();
				} 
		    }
	    }
		System.out.println("[sender]"+token);
		return token;
    }
	
	public String string_try_mcdata_signalling_applicationid(String xmlcontext) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		String boundary = "--boundary1"; // 边界标识符
		String[] parts = xmlcontext.split(boundary);
		String token = "false";
		// 遍历每个部分
		for (String part : parts) {
		    if (part.contains("Content-Type: application/vnd.3gpp.mcdata-signalling")) {
		    	int xmlStart = part.indexOf("<?xml");
		        if (xmlStart != -1) {
		        	part=part.substring(xmlStart).trim();
		        }
			    try {
			    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(new InputSource(new StringReader(part)));
					NodeList dataNodes = doc.getElementsByTagName("applicationid");
					if (dataNodes.getLength() > 0) {
//					    Element dataElement = (Element) dataNodes.item(0);
//						token=dataElement.getTextContent(); 
						token = "true";
				    } else {
//				        throw new Exception("No <data> element found in XML.");
				        token = "false";
				    }
				}	
				catch (Exception e) {
					e.printStackTrace();
				} 
		    }
	    }
		System.out.println("[conversationid]"+token);
		return token;
    }
	
	public String string_try_mcdata_signalling_payloadcontent(String xmlcontext) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		String boundary = "--boundary1"; // 边界标识符
		String[] parts = xmlcontext.split(boundary);
		String token = null;
		// 遍历每个部分
		for (String part : parts) {
		    if (part.contains("Content-Type: application/vnd.3gpp.mcdata-signalling")) {
		    	int xmlStart = part.indexOf("<?xml");
		        if (xmlStart != -1) {
		        	part=part.substring(xmlStart).trim();
		        }
		        try {
			    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(new InputSource(new StringReader(part)));
			        NodeList dataNodes = doc.getElementsByTagName("payload");
			        if (dataNodes.getLength() < 2) {
			            Element dataElement = (Element) dataNodes.item(0);
			            token=dataElement.getTextContent(); 
			            System.out.println("[payloadcontent]"+token);
			        } else {
			            throw new Exception("No <paylaod> more than one in XML.");
			        }
			    }	
				catch (Exception e) {
					e.printStackTrace();
				} 
		    }
	    }
		System.out.println("[conversationid]"+token);
		return token;
    }
	
	public String string_try_mcdata_info_requesttype(String xmlcontext) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		String boundary = "--boundary1"; // 边界标识符
		String[] parts = xmlcontext.split(boundary);
		String token = null;
		for (String part : parts) {
		    if (part.contains("Content-Type: application/vnd.3gpp.mcdata-info+xml")) {
		    	int xmlStart = part.indexOf("<?xml");
		        if (xmlStart != -1) {
		        	part=part.substring(xmlStart).trim();
		        }
			    try {
			    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(new InputSource(new StringReader(part)));
			        NodeList dataNodes = doc.getElementsByTagName("request-type");
			        if (dataNodes.getLength() > 0) {
			            Element dataElement = (Element) dataNodes.item(0);
			            token=dataElement.getTextContent(); 
			            
			        } else {
			            throw new Exception("No <data> element found in XML.");
			        }
			    }catch (Exception e) {
		    		e.printStackTrace();
		    	} 
		    }
	    }
		return token;
	}
	
	public String string_try_mcdata_info_callinguser(String xmlcontext) {
		/* 147 */     DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		/*     */     String boundary = "--boundary1"; // 边界标识符
						String[] parts = xmlcontext.split(boundary);
						String token = null;
						// 遍历每个部分
						for (String part : parts) {
						    if (part.contains("Content-Type: application/vnd.3gpp.mcdata-info+xml")) {
						    	int xmlStart = part.indexOf("<?xml");
						        if (xmlStart != -1) {
						        	part=part.substring(xmlStart).trim();
						        }
							    try {
							    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
									Document doc = dBuilder.parse(new InputSource(new StringReader(part)));
							        NodeList dataNodes = doc.getElementsByTagName("mcdata-calling-user-id");
								        if (dataNodes.getLength() > 0) {
								            Element dataElement = (Element) dataNodes.item(0);
								            token=dataElement.getTextContent(); 
								            
								        } else {
								            throw new Exception("No <data> element found in XML.");
								        }
							    }	
						    catch (Exception e) {
				      
						    		e.printStackTrace();
						    	} 
						    }
					    }
						System.out.println("[requesttype]"+token);
		/* 161 */     return token;
		/*     */ }
	
	public String string_try_mcdata_info_groupuri(String xmlcontext) {
/* 147 */     DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
/*     */     String boundary = "--boundary1"; // 边界标识符
				String[] parts = xmlcontext.split(boundary);
				String token = "null";
				// 遍历每个部分
				for (String part : parts) {
				    if (part.contains("Content-Type: application/vnd.3gpp.mcdata-info+xml")) {
				    	int xmlStart = part.indexOf("<?xml");
				        if (xmlStart != -1) {
				        	part=part.substring(xmlStart).trim();
				        }
					    try {
					    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
							Document doc = dBuilder.parse(new InputSource(new StringReader(part)));
							NodeList dataNodes = doc.getElementsByTagName("mcdataURI");
							if (dataNodes.getLength() > 0) {
					            Element dataElement = (Element) dataNodes.item(0);
					            token=dataElement.getTextContent(); 
					            
					        } else {
					            return token;
					        }
//							if (dataNodes.getLength() > 0) {
//							    Element dataElement = (Element) dataNodes.item(0);
//							    String token1 = dataElement.getTextContent(); // 获取 mcdataURI 的值
//						        System.out.println("mcdataURI value: " + token1);
//							    // 获取 mcdata-request-uri 元素下的 mcdataURI 子元素
//							    NodeList mcdataURINodes = dataElement.getElementsByTagName("mcdataURI");
//							    
//							    if (mcdataURINodes.getLength() > 0) {
//							        Element mcdataURIElement = (Element) mcdataURINodes.item(0);
//							        token = mcdataURIElement.getTextContent(); // 获取 mcdataURI 的值
//							        System.out.println("mcdataURI value: " + token);
//							    } else {
//							        throw new Exception("No <mcdataURI> element found in <mcdata-request-uri>.");
//							    }
//							    
//							} else {
//							    throw new Exception("No <mcdata-request-uri> element found in XML.");
//							}
					    }	
				    catch (Exception e) {
		      
				    		e.printStackTrace();
				    	} 
				    }
			    }
				System.out.println("[groupuri]"+token);
/* 161 */     return token;
/*     */ }

	public String string_try_mcdata_sdp(String xmlcontext) {
//		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		String boundary = "--boundary1"; // 边界标识符
			String[] parts = xmlcontext.split(boundary);
			String token = null;
			for (String part : parts) {
				String[] lines = part.split("\n");

		        // 遍历每一行，寻找包含 "a=path:" 的行
		        for (String line : lines) {
		            if (line.startsWith("a=path:")) {
		                // 提取 "a=path:" 之后的部分并返回
		                return line.substring(7).trim();
		            }
		        }
			}
		return token;
	}
	
	public String string_try_mcdata_sdp2(String xmlcontext) {
//		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		String boundary = "--boundary1"; // 边界标识符
			String[] parts = xmlcontext.split(boundary);
			String token = null;
			for (String part : parts) {
				String[] lines = part.split("\n");

		        // 遍历每一行，寻找包含 "a=path:" 的行
		        for (String line : lines) {
		            if (line.startsWith("a=path2:")) {
		                // 提取 "a=path:" 之后的部分并返回
		                return line.substring(8).trim();
		            }
		        }
			}
		return token;
	}
	
	public String string_try_mcdata_payload_fd(String xmlcontext) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		String boundary = "--boundary1"; // 边界标识符
		String[] parts = xmlcontext.split(boundary);
		String token = null;
		for (String part : parts) {
		    if (part.contains("Content-Type: application/vnd.3gpp.mcdata-payload")) {
		    	int xmlStart = part.indexOf("<?xml");
		        if (xmlStart != -1) {
		        	part=part.substring(xmlStart).trim();
		        }
			    try {
			    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(new InputSource(new StringReader(part)));
			        NodeList dataNodes = doc.getElementsByTagName("payload");
			        if (dataNodes.getLength() < 2) {
			            Element dataElement = (Element) dataNodes.item(0);
			            token=dataElement.getTextContent(); 
			        } else {
			            throw new Exception("No <paylaod> more than one in XML.");
			        }
			    }	
			    catch (Exception e) {
		    		e.printStackTrace();
		    	} 
		    }
	    }
		System.out.println("[payload]"+token);
	    return token;
	}
	
	public String string_try_mcdata_payload_fd_payload_content(String xmlcontext) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		String boundary = "--boundary1"; // 边界标识符
		String[] parts = xmlcontext.split(boundary);
		String token = null;
		for (String part : parts) {
		    if (part.contains("Content-Type: application/vnd.3gpp.mcdata-signalling")) {
		    	System.out.println("[fdpayload]"+part);
		    	int xmlStart = part.indexOf("<?xml");
		        if (xmlStart != -1) {
		        	part=part.substring(xmlStart).trim();
		        }
			    try {
			    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(new InputSource(new StringReader(part)));
			        NodeList dataNodes = doc.getElementsByTagName("payload");
			        if (dataNodes.getLength() < 2) {
			            Element dataElement = (Element) dataNodes.item(0);
			            token=dataElement.getTextContent(); 
			            //test	Payload content type=FILEURL
			        } else {
			           
			        }
			    }	
			    catch (Exception e) {
		    		e.printStackTrace();
		    	} 
		    }
	    }
//		System.out.println("[fdpayload]"+token);
	    return token;
	}
	
	public String string_try_mcdata_payload_fd_mandatory_download(String xmlcontext) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		String boundary = "--boundary1"; // 边界标识符
		String[] parts = xmlcontext.split(boundary);
		String token = null;
		for (String part : parts) {
		    if (part.contains("Content-Type: application/vnd.3gpp.mcdata-payload")) {
		    	
		    	int xmlStart = part.indexOf("<?xml");
		        if (xmlStart != -1) {
		        	part=part.substring(xmlStart).trim();
		        }
			    try {
			    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(new InputSource(new StringReader(part)));
			        NodeList dataNodes = doc.getElementsByTagName("mandatory-download");
			        if (dataNodes.getLength() > 0) {
			            return "true";
			        } else {
			            throw new Exception("No <paylaod> more than one in XML.");
			        }
			    }	
			    catch (Exception e) {
		    		e.printStackTrace();
		    	} 
		    }
	    }
//		System.out.println("[payload]"+token);
	    return "false";
	}
	
	public String string_try_mcdata_payload_fd_useruri(String xmlcontext) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		String boundary = "--boundary1"; // 边界标识符
		String[] parts = xmlcontext.split(boundary);
		String token = null;
		for (String part : parts) {
		    if (part.contains("Content-Type: application/vnd.3gpp.mcdata-info+xml")) {
		    	System.out.println("[fduri]"+part);
		    	int xmlStart = part.indexOf("<?xml");
		        if (xmlStart != -1) {
		        	part=part.substring(xmlStart).trim();
		        }
			    try {
			    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(new InputSource(new StringReader(part)));
			        NodeList dataNodes = doc.getElementsByTagName("mcdata-request-uri");
			        if (dataNodes.getLength() > 0) {
			            Element dataElement = (Element) dataNodes.item(0);
			            String requestUri = dataElement.getTextContent();
			            return requestUri; // 直接回傳標籤的內容
			        } else {
			            return "false"; // 如果沒找到標籤，回傳 false
			        }
			    }	
			    catch (Exception e) {
		    		e.printStackTrace();
		    	} 
		    }
	    }
	    return "false";
	}
	
	public static Object extractXmlElement(String xmlContext, String contentType, String tagName, boolean allowMultiple) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		List<String> result = new ArrayList<>();
        String boundary = "--boundary1"; // 解析 SIP 消息的边界
        String[] parts = xmlContext.split(boundary);

        for (String part : parts) {
            if (part.contains("Content-Type: " + contentType)) {
                int xmlStart = part.indexOf("<?xml");
                if (xmlStart != -1) {
                    part = part.substring(xmlStart).trim();
                }
                try {
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(new InputSource(new StringReader(part)));

                    NodeList dataNodes = doc.getElementsByTagName(tagName);
                    for (int i = 0; i < dataNodes.getLength(); i++) {
                        result.add(dataNodes.item(i).getTextContent().trim());
                    }

                    // 处理 allowMultiple 参数
                    if (!allowMultiple) {
                        if (result.size() > 1) {
                            return "ERROR: " + tagName + " 只能有 1 个，但找到了 " + result.size() + " 个"; // 返回错误信息
                        } else if (result.size() == 1) {
                            return result.get(0); // 返回唯一的值
                        } else {
                            return null; // 没找到
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return allowMultiple ? result : null; // 如果允许多个，返回 List，否则返回 null
    }
}

