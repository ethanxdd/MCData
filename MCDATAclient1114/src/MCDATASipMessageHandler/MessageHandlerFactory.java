package MCDATASipMessageHandler;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sip.header.Header;

import gov.nist.javax.sip.message.SIPRequest;
import sipMessageHandle.RequestObject;
import xml_generator.try_parser;

public class MessageHandlerFactory {
	public static MessageHandler getHandler(RequestObject requestObj) throws ParseException {
	    SIPRequest sipRequest = requestObj.getSipRequest();
	    try_parser get1 = new try_parser();
	    // 提取 P-Preferred-Service 的值
	    String xmlString = requestObj.getContentString();
	    String extractedValue = extractPreferredService(sipRequest);
	    String requesttype = get1.string_try_mcdata_info_requesttype(xmlString);//one-to-one-sds fd group
	    
	    switch (extractedValue) {
	        case "group":
	            return new SDSGroupMessageHandler();
	        case "one":
	            return new SDSOneToOneMessageHandler();
	        default:
	            throw new IllegalArgumentException("未知的 MESSAGE 类型: " + extractedValue);
	    }
	    

	    
	    
//			emergencyind6 = get1.string_try_mcdata_info_groupuri(xmlString);

		
//		get1.string_try_mcdata_notification(xmlString)=="false"

//		String emergencyind = get1.string_try_mcdata_signalling_mandatory(xmlString);
//		string_try_mcdata_signalling_applicationid
	}

	private static String extractPreferredService(SIPRequest sipRequest) {
	    Header preferredServiceHeader = sipRequest.getHeader("P-Preferred-Service");
	    
	    if (preferredServiceHeader == null) {
	        return ""; // 这里可以改成 null 或者抛异常，看你的业务需求
	    }

	    String preferredServiceValue = preferredServiceHeader.toString();
	    Pattern pattern = Pattern.compile("mcdata\\.(\\S+)");
	    Matcher matcher = pattern.matcher(preferredServiceValue);

	    return matcher.find() ? matcher.group(1) : "";
	}
    
    
}
