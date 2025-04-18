package netmsrp.wrap.cpim;

import java.nio.ByteBuffer;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import netmsrp.WrappedMessage;
import netmsrp.utils.TextUtils;
import netmsrp.wrap.Headers;

/**
 * CPIM message
 * 
 * @author jexa7410
 */
public class Message implements WrappedMessage {

	public static final String WRAP_TYPE = Header.CPIM_TYPE;

	/**
	 * Message content
	 */
	private byte[] msgContent = null;

	/**
	 * MIME headers
	 */
	private ArrayList<Header> headers = new ArrayList<Header>();

	/**
	 * MIME content headers
	 */
	private ArrayList<Header> contentHeaders = new ArrayList<Header>();

	/**
	 * Constructor
	 */
	public Message() { }

    /**
     * Returns content type
     * 
     * @return Content type
     */
	private static final Header ContentType = new Header(Headers.CONTENT_TYPE, null);

	@Override
	public String getContentType() {
    	return contentHeaders.get(contentHeaders.indexOf(ContentType)).getValue();
    }

    /**
     * Returns MIME header
     * 
     * @param name Header name
     * @return Header value
     */
    @Override
	public String getHeader(String name) {
		return headers.get(headers.indexOf(new Header(name, null))).getValue();
	}

    /**
     * Returns MIME content header
     * 
     * @param name Header name
     * @return Header value
     */
    @Override
	public String getContentHeader(String name) {
		return contentHeaders.get(contentHeaders.indexOf(new Header(name, null))).getValue();
	}

    /**
     * Returns message content
     * 
     * @return Content
     */
    @Override
	public byte[] getMessageContent() {
		return msgContent;
	}

	private static final String CRLF = "\r\n";
	private static final String EMPTY_LINE = CRLF+CRLF;
    /**
     * Parse message/CPIM document
     * 
     * @param buffer Input data
     */
	@Override
	public void parse(ByteBuffer buffer) {
		/* CPIM sample:
	    From: MR SANDERS <im:piglet@100akerwood.com>
	    To: Depressed Donkey <im:eeyore@100akerwood.com>
	    DateTime: 2000-12-13T13:40:00-08:00
	    Subject: the weather will be fine today

	    Content-type: text/plain
	    Content-ID: <1234567890@foo.com>

	    Here is the text of my message.
	    */
		String data = new String(buffer.array(), TextUtils.utf8);
		int start = 0;
		int end = data.indexOf(EMPTY_LINE, start);
		String[] lines;
		// Read message headers
		lines = data.substring(start, end).split(CRLF);
		for (String token : lines) {
			Header hd = Header.parseHeader(token);
			headers.add(hd);
		}
		// Read the MIME-encapsulated content headers
		start = end + EMPTY_LINE.length();
		end = data.indexOf(EMPTY_LINE, start);
		lines = data.substring(start, end).split(CRLF);
		for (String token : lines) {
			Header hd = Header.parseHeader(token);
			contentHeaders.add(hd);
		}
		// Create the CPIM message
		start = end + EMPTY_LINE.length();
		msgContent = new byte[data.length()-start];		 //data.substring(start);
		buffer.position(start);
		buffer.get(msgContent);
	}

    private String getTimeStamp() {
        Format sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

        return sdf.format(Long.valueOf(System.currentTimeMillis()))
            .replaceAll("(\\d\\d)(\\d\\d)$", "$1:$2");
    }

	@Override
	public byte[] wrap(String from, String to, String contentType, byte[] content) {
		headers.add(new Header(Headers.FROM, from));
		headers.add(new Header(Headers.TO, to));
		headers.add(new Header(Headers.DATETIME, getTimeStamp()));
		contentHeaders.add(new Header(Headers.CONTENT_TYPE, contentType));
		msgContent = content;
		return this.toString().getBytes(TextUtils.utf8);
	}

    @Override
	public String toString() {
		StringBuilder sb = new StringBuilder(msgContent.length + (headers.size() + contentHeaders.size()) * 20);
		for (Header h : headers) {
			sb.append(h).append(CRLF);
		}
		sb.append(CRLF);
		for (Header h : contentHeaders) {
			sb.append(h).append(CRLF);		}
		sb.append(CRLF).append(new String(msgContent, TextUtils.utf8));
		return sb.toString();
	}
}