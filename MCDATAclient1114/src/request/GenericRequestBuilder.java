package request;

import java.text.ParseException;
import java.util.List;

import javax.sip.InvalidArgumentException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.URI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.Header;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;


import gov.nist.javax.sip.Utils;
import sip.SipClient;

public class GenericRequestBuilder {
    private final Request request;
    private final HeaderFactory headerFactory = SipClient.headerFactory;
    private final AddressFactory addressFactory = SipClient.addressFactory;
    private final MessageFactory messageFactory = SipClient.messageFactory;

    public GenericRequestBuilder(String method, String requestURI, String callId, long sequenceNumber, 
                                 String fromUser, String fromHost, String toUser, String toHost, 
                                 List<ViaHeader> viaHeaders, int maxForwards) throws ParseException, InvalidArgumentException {
        // 创建核心 Request
        URI uri = addressFactory.createURI(requestURI);
        CallIdHeader callIdHeader = headerFactory.createCallIdHeader(callId);
        CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(sequenceNumber, method);
        FromHeader fromHeader = createFromHeader(fromUser, fromHost);
        ToHeader toHeader = createToHeader(toUser, toHost);
        MaxForwardsHeader maxForwardsHeader = headerFactory.createMaxForwardsHeader(maxForwards);

        this.request = messageFactory.createRequest(uri, method, callIdHeader, cSeqHeader, 
                                                     fromHeader, toHeader, viaHeaders, maxForwardsHeader);
    }

    // 创建 FromHeader
    private FromHeader createFromHeader(String user, String host) throws ParseException {
        Address fromAddress = addressFactory.createAddress("sip:" + user + "@" + host);
        String tag = Utils.getInstance().generateTag();
        return headerFactory.createFromHeader(fromAddress, tag);
    }

    // 创建 ToHeader
    private ToHeader createToHeader(String user, String host) throws ParseException {
        Address toAddress = addressFactory.createAddress("sip:" + user + "@" + host);
        return headerFactory.createToHeader(toAddress, null);
    }

    // 添加通用 Header
    public GenericRequestBuilder addHeader(String name, String value) throws ParseException {
        Header header = headerFactory.createHeader(name, value);
        this.request.addHeader(header);
        return this;
    }

    // 设置 Contact Header
    public GenericRequestBuilder setContactHeader(String user, String host, int port) throws ParseException {
        Address contactAddress = addressFactory.createAddress("sip:" + user + "@" + host + ":" + port + ";transport=tcp");
        ContactHeader contactHeader = headerFactory.createContactHeader(contactAddress);
        this.request.setHeader(contactHeader);
        return this;
    }

    // 设置内容
    public GenericRequestBuilder setContent(String content, String contentType) throws ParseException {
        ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader(contentType, null);
        this.request.setContent(content, contentTypeHeader);
        return this;
    }

    // 获取最终构建的 Request
    public Request build() {
        return this.request;
    }
}