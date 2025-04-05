package sip;

import RTCP.RTCPSender;
import dtmf.dtmfSender;
import gov.nist.javax.sip.SipStackExt;
import gov.nist.javax.sip.Utils;
import gov.nist.javax.sip.address.SipUri;
import gov.nist.javax.sip.clientauthutils.AccountManager;
import gov.nist.javax.sip.clientauthutils.AuthenticationHelper;
import gov.nist.javax.sip.header.Accept;
import gov.nist.javax.sip.header.Contact;
import gov.nist.javax.sip.header.ContentType;
import gov.nist.javax.sip.header.HeaderFactoryImpl;
import gov.nist.javax.sip.header.ReferTo;
import gov.nist.javax.sip.header.Require;
import gov.nist.javax.sip.header.Supported;
import gov.nist.javax.sip.header.UserAgent;
import gov.nist.javax.sip.header.ims.PAssertedIdentityHeader;
import gov.nist.javax.sip.header.ims.PPreferredIdentityHeader;
import gov.nist.javax.sip.message.Content;
import gov.nist.javax.sip.message.ContentImpl;
import gov.nist.javax.sip.message.MultipartMimeContentImpl;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;
import gov.nist.javax.sip.stack.SIPClientTransaction;
import gov.nist.javax.sip.stack.SIPClientTransactionImpl;
import gov.nist.javax.sip.stack.SIPDialog;
import gov.nist.javax.sip.stack.SIPServerTransactionImpl;
import http.createGroup;
import http.getDirectorytInfo;
import http.getGroupList;
import http.getGroupMember;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Observable;
import java.util.Properties;
import java.util.Random;
import java.util.Timer;
import java.util.TooManyListenersException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.ObjectInUseException;
import javax.sip.PeerUnavailableException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TimeoutEvent;
import javax.sip.Transaction;
import javax.sip.TransactionAlreadyExistsException;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.TransactionUnavailableException;
import javax.sip.TransportAlreadySupportedException;
import javax.sip.TransportNotSupportedException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.address.URI;
import javax.sip.header.AcceptHeader;
import javax.sip.header.AllowHeader;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.CallInfoHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentDispositionHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.EventHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.Header;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.Parameters;
import javax.sip.header.SIPIfMatchHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import list.RemoteInfo;

//import org.apache.http.ParseException;
import org.xml.sax.SAXException;

import MCDATASipMessageHandler.MessageHandler;
import MCDATASipMessageHandler.MessageHandlerFactory;
import MSRP.MSRP;
import proxy.Proxy;
import proxy.ProxyMCDATA;
import redirect.handler;
import request.GenericRequestBuilder;
import session.MediaSipSession;
import session.MySdpInfo;
import session.PeerInfo;
import sipMessageHandle.RequestObject;
import sipMessageHandle.ResponseObject;
import sipMessageHandle.SipMessageUtils;
import ui.clientUI;
import ui.client_parameters;
import xml.PIDFXmlBuilder;
import xml.Pidf;
import xml.ResourceList;
import xml.ResourceListXmlBuilder;
import xml_generator.try_mcdatainfo_factory;
import xml_generator.try_mcdatapayload_factory;
import xml_generator.try_mcdataresource_factory;
import xml_generator.try_mcdatasignalling_factory;
import xml_generator.try_mcpttinfo_factory;
import xml_generator.try_parser;
import xml_generator.try_pidf_factory;

import netmsrp.Message;
import netmsrp.Session;
import netmsrp.exceptions.InternalErrorException;

public class SipClient  implements SipListener ,PropertyChangeListener{
  public static final String ResponseEvent = null;
  
  private static SipClient instance = null;
  
  public static String localIp;
  
  public static int localPort;
  
  public static String localEndpoint;
  
  public static String transport = "tcp";
  
  public static SipStack sipStack;
  
  public static SipFactory sipFactory;
  
  public static SipProvider sipProvider;
  
  public static SipProvider EMRProvider;
  
  public static HeaderFactory headerFactory;
  
  public static AddressFactory addressFactory;
  
  public static MessageFactory messageFactory;
  
  public static ListeningPoint udpListeningPoint;
  
  public static ListeningPoint tcpListeningPoint;
  
  public static String RemoteIp = "n/a";
  
  public static int RemotePort = -1;
  
  private String StunServer;
  
  public static String RemoteEndpoint;
  
  public static String sipUserName = "n/a";
  
  public static String sipPassword;
  
  public static String mySipUri;
  
  private static AccountManager accountManager;
  
  public static String registerCallId;
  
  public static int registerCSeq;
  
  public static int messageCSeq;
  
  public static int inviteCSeq;
  
  public static int byeCSeq;
  
  public static int referCSeq;
  
  private static String registerContactAddress;
  
  public static String myTag;
  
  public static String publishCallId;
  
  public static SipPublishSender sipPublishSender;
  
  public static int publishCSeq;
  
  public static String currentCallId;
  
  public static String sessionActiveIp;
  
  public static int sessionActivePort;
  
  private ClientTransaction inviteTransaction;
  
  private ClientTransaction publishTransaction;
  
  private Map<String, SubscribeDialog> subscribeMap;
  
  public static Map<String, MediaSipSession> mediaSipSessionMap = new HashMap<>();
  
  public static Map<String, MediaSipSession> EMRmediaSipSessionMap = new HashMap<>();
  
  public static String inCallCallId;
  
  private boolean isRegister = false;
  
  private static boolean PESessionE = true;
  
  private int registerCount = 0;
  
  private int ErrorAccount = 0;
  
  private static MySdpInfo mySdpInfo;
  
  private static MySdpInfo EMRSdpInfo;
  
  private CSeqHeader stateUpgrageCseq;
  
  private CSeqHeader stateCancelCseq;
  
  public boolean MEGC = false;
  
  public boolean GroupState = false;
  
  public int kyinfoValue = 0;
  
  private DatagramSocket socket;
  
  private static long sessinID = -1L;
  
  private static Proxy proxy;
  
  private static ProxyMCDATA proxymcdata;
  
  public String GroupName = "";
  
  private List<RemoteInfo> remoteInfoList;
  
  private int RtpPort = 0;
  
  private int MsrpPort = 0;
  
  private int LMRRtpPort = 0;
  
  private getGroupList grouplist;
  
  private String E_mIP = "";
  
  private int E_mPort = 0;
  
  private String EMRinviteCallID = "";
  
  private String pIP = "";
  
  private int pRtcpPort = 0;
  
  private int pRtpPort = 0;
  
  public handler Handler;
  
  public client_parameters EMR_parameters;
  
  private static SIPResponse EMRresponse;
  
  private SIPServerTransactionImpl EMRTransaction;
  
  private PropertyChangeSupport support;
  
  private Map<String, Session> sessionmsrpMap2 = new ConcurrentHashMap<>();
  
  private Map<String, String> sessionmsrpmessageMap = new ConcurrentHashMap<>();
  
  public Session activeSession;
  
  private DownloadDecision downloadDecision;
  

  
//  private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
  
  public SipClient() {
	  System.out.println("[support]");
      support = new PropertyChangeSupport(this);  // 在构造函数中初始化 support
  }
  
  public SipClient createSipClientInstance(String userName, String sipPassword, String sipServer, String localIp, client_parameters parms) throws SocketException {
    instance = null;
    instance = new SipClient();
    //instance.setEMRparm(parms);
//    support.firePropertyChange("MCDatasipMessage", null, "1");
    System.out.println("[SipClient][createSipClientInstance()]");
    initialize(userName, sipPassword, sipServer, localIp);
    return instance;
  }
  
  public static SipClient getInstance() {
    return instance;
  }
  
  private void initialize(String userName, String password, String sipServer, String privateIp) throws SocketException {
    int privatePort = 5060;
    localIp = privateIp;
    localPort = privatePort;
    this.subscribeMap = new HashMap<>();
//    support = new PropertyChangeSupport(this);
    if (sipServer.contains(":")) {
      String[] hostPort = sipServer.split(":");
      RemoteIp = hostPort[0];
      RemotePort = Integer.parseInt(hostPort[1]);
    } else {
      RemoteIp = sipServer;
      RemotePort = 5060;
      this.StunServer = String.valueOf(RemoteIp) + ":3478";
      RemoteEndpoint = String.valueOf(RemoteIp) + ":" + RemotePort;
      sipUserName = userName;
      sipPassword = password;
      mySipUri = "sip:" + userName + "@" + sipServer;
      accountManager = new AccountManagerImpl(userName, RemoteEndpoint, sipPassword);
      sipStack = null;
      this.inviteTransaction = null;
      sipFactory = SipFactory.getInstance();
      sipFactory.setPathName("gov.nist");
      setstack(privateIp);
      
      if (sipStack != null)
        try {
          headerFactory = sipFactory.createHeaderFactory();
          addressFactory = sipFactory.createAddressFactory();
          messageFactory = sipFactory.createMessageFactory();
          setLocalEnpoint(privateIp, privatePort);
          setListeningPoint(privateIp, privatePort);
          sipProvider.setAutomaticDialogSupportEnabled(false);
//          setEMRListeningPoint(this.EMR_parameters.LMRLocalIp, Integer.parseInt(this.EMR_parameters.LMRLocalPort));
//          EMRProvider.setAutomaticDialogSupportEnabled(false);
          registerCallId = sipProvider.getNewCallId().toString().replace("Call-ID:", "").trim();
          registerCSeq = 0;
          myTag = SipMessageUtils.generateNewTag();
          publishCallId = sipProvider.getNewCallId().toString().replace("Call-ID:", "").trim();
          publishCSeq = 0;
        } catch (PeerUnavailableException e) {
          e.printStackTrace();
        } catch (Exception e) {
          e.printStackTrace();
        }  
    } 
  }
  
  public void setEMRparm(client_parameters parm) {
    this.EMR_parameters = parm;
    this.E_mIP = parm.EMRIp;
    this.E_mPort = Integer.parseInt(parm.LMRLocalPort);
  }
  
  public void setLocalEnpoint(String localIp, int localPort) {
    localEndpoint = String.valueOf(localIp) + ":" + localPort;
    registerContactAddress = localEndpoint;
  }
  
  private void setstack(String privateIp) {
    Properties properties = new Properties();
    properties.setProperty("javax.sip.IP_ADDRESS", privateIp);
    properties.setProperty("javax.sip.STACK_NAME", "IWF");
    properties.setProperty("gov.nist.javax.sip.MAX_MESSAGE_SIZE", "1048576");
    properties.setProperty("gov.nist.javax.sip.DEBUG_LOG", String.valueOf(sipUserName) + "clientbug.txt");
    properties.setProperty("gov.nist.javax.sip.SERVER_LOG", String.valueOf(sipUserName) + "clientlog.txt");
    properties.setProperty("gov.nist.javax.sip.CACHE_CLIENT_CONNECTIONS", "True");
    properties.setProperty("gov.nist.javax.sip.DELIVER_UNSOLICITED_NOTIFY", "true");
    try {
      sipStack = sipFactory.createSipStack(properties);
    } catch (PeerUnavailableException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    } 
  }
  
  public void setListeningPoint(String privateIp, int port) throws InvalidArgumentException, TransportNotSupportedException, ObjectInUseException, TooManyListenersException, TransportAlreadySupportedException {
    tcpListeningPoint = sipStack.createListeningPoint(privateIp, port, "tcp");
    sipProvider = sipStack.createSipProvider(tcpListeningPoint);
    sipProvider.addSipListener(this);
  }
  
  public void setEMRListeningPoint(String ip, int port) throws InvalidArgumentException, TransportNotSupportedException, ObjectInUseException, TooManyListenersException, TransportAlreadySupportedException {
    udpListeningPoint = sipStack.createListeningPoint(ip, port, "udp");
    EMRProvider = sipStack.createSipProvider(udpListeningPoint);
    EMRProvider.addSipListener(this);
  }
  
  public void leave() {
    try {
      if (sipProvider != null && sipStack != null && sipFactory != null && EMRProvider != null) {
        sipStack.deleteListeningPoint(tcpListeningPoint);
        sipStack.deleteListeningPoint(udpListeningPoint);
        sipProvider.removeSipListener(this);
        EMRProvider.removeSipListener(this);
        sipStack.deleteSipProvider(sipProvider);
        sipStack.deleteSipProvider(EMRProvider);
        sipFactory.resetFactory();
        sipStack = null;
        sipProvider = null;
        EMRProvider = null;
        instance = null;
        this.inviteTransaction = null;
        headerFactory = null;
        addressFactory = null;
        messageFactory = null;
        tcpListeningPoint = null;
        udpListeningPoint = null;
        System.out.println("[SipClient][leave()] clear");
      } 
    } catch (ObjectInUseException e) {
      e.printStackTrace();
    } 
  }
  
  public void sendRegister(int expires) {
    if (instance == null)
      return; 
    if (sipStack == null)
      return; 
    AddressFactory addressFactory = SipClient.addressFactory;
    SipProvider sipProvider = SipClient.sipProvider;
    HeaderFactory headerFactory = SipClient.headerFactory;
    try {
      Request request = createRegisterRequest();
      SIPRequest sipRequest = (SIPRequest)request;
      Address contactAddress = addressFactory.createAddress("sip:" + sipUserName + 
          "@" + registerContactAddress + ";transport=tcp");
      request.addHeader((Header)headerFactory.createContactHeader(contactAddress));
      addRequireHeader((Request)sipRequest, "pref");
      List<String> uAgentList = new ArrayList<>();
      uAgentList.add("IWF-Client");
      uAgentList.add("/3GPP");
      addUserAgentHeader((Request)sipRequest, uAgentList);
      ExpiresHeader eh = headerFactory.createExpiresHeader(expires);
      request.addHeader((Header)eh);
      ClientTransaction transaction = sipProvider.getNewClientTransaction(request);
      transaction.sendRequest();
      if (expires == 0)
        this.registerCount = 0; 
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  private void addUserAgentHeader(Request request, List<String> userAgentList) throws ParseException {
    UserAgent userAgent = new UserAgent();
    userAgent.setProduct(userAgentList);
    request.setHeader((Header)userAgent);
  }
  
  private void addRequireHeader(Request request, String OptionTag) throws ParseException {
    Require require = new Require();
    require.setOptionTag(OptionTag);
    request.setHeader((Header)require);
  }
  
  private ArrayList<ViaHeader> createViaHeader() {
    ArrayList<ViaHeader> viaHeaders = new ArrayList<>();
    try {
      ViaHeader myViaHeader = headerFactory.createViaHeader(registerContactAddress.split(":")[0], 
          Integer.parseInt(registerContactAddress.split(":")[1]), transport, 
          Utils.getInstance().generateBranchId());
      myViaHeader.setRPort();
      viaHeaders.add(myViaHeader);
    } catch (ParseException e) {
      e.printStackTrace();
    } catch (InvalidArgumentException e) {
      e.printStackTrace();
    } 
    return viaHeaders;
  }
  
  private Request createRegisterRequest() throws ParseException, InvalidArgumentException {
    CallIdHeader callIdHeader;
    Address fromAddress = addressFactory.createAddress("sip:" + sipUserName + "@" + RemoteIp);
    FromHeader fromHeader = headerFactory.createFromHeader(fromAddress, myTag);
    Address toAddress = addressFactory.createAddress("sip:" + sipUserName + "@" + RemoteIp);
    ToHeader toHeader = headerFactory.createToHeader(toAddress, null);
    ArrayList<ViaHeader> viaHeaders = createViaHeader();
    URI requestURI = addressFactory.createAddress("sip:" + RemoteEndpoint).getURI();
    if (registerCallId == null) {
      callIdHeader = sipProvider.getNewCallId();
      registerCallId = callIdHeader.getCallId();
    } else {
      callIdHeader = headerFactory.createCallIdHeader(registerCallId);
    } 
    registerCSeq++;
    CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(registerCSeq, "REGISTER");
    MaxForwardsHeader maxForwardsHeader = headerFactory.createMaxForwardsHeader(70);
    return messageFactory.createRequest(requestURI, "REGISTER", callIdHeader, cSeqHeader, 
        fromHeader, toHeader, viaHeaders, maxForwardsHeader);
  }
  
  public void sendInvite(String groupName, int expires, String message, String type, String receiver) throws IOException {    	
  	try {
//  		AddressFactory addressFactory = login.addressFactory;
//  		SipProvider sipProvider = login.sipProvider;
//  		MessageFactory messageFactory = login.messageFactory;
//  		HeaderFactory headerFactory = login.headerFactory;
  		String sipServer = RemoteIp;
  		CallIdHeader callIdHeader = sipProvider.getNewCallId();
  		inviteCSeq++;
  		CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(inviteCSeq, "INVITE");
  		Address fromAddress = addressFactory.createAddress("sip:" + sipUserName + "@" + RemoteIp);
  		FromHeader fromHeader = headerFactory.createFromHeader(fromAddress, myTag);
  		if ((sipServer.split(":")).length == 2)
  			sipServer = sipServer.split(":")[0];
        		URI publishURI = addressFactory.createAddress("sip:" + groupName ).getURI();
        		Address toAddress = addressFactory.createAddress("sip:" + groupName );
        		ToHeader toHeader = headerFactory.createToHeader(toAddress, null);
	        if(type=="one") {
		    	toHeader = headerFactory.createToHeader(addressFactory.createAddress("sip:"+receiver+"@" + RemoteIp), null);
		    	publishURI = addressFactory.createAddress("sip:" + receiver + "@" + RemoteIp).getURI();
		    }
	        ArrayList<ViaHeader> viaList = createInviteViaHeader();
	        MaxForwardsHeader maxForwardsHeader = headerFactory.createMaxForwardsHeader(70);
	        Request request = messageFactory.createRequest(publishURI, "INVITE", callIdHeader, cSeqHeader, 
            fromHeader, toHeader, viaList, maxForwardsHeader);
	        Address localAddress = createContactAddress();
	        ContactHeader contactHeader = headerFactory.createContactHeader(localAddress);
	        contactHeader.setParameter("g.3gpp.mcdata.sds", null);  // 添加 g.3gpp.mcdata.sds 标签
//        	contactHeader.setParameter("g.3gpp.icsi-ref", "urn:urn-7:3gpp-service.ims.icsi.mcdata.sds"); 
	        contactHeader.setParameter("g.3gpp.icsi-ref", null); 
	        contactHeader.setParameter("isfocus", null);
	        request.setHeader((Header)contactHeader);
	        ExpiresHeader eh = headerFactory.createExpiresHeader(expires);
	        request.addHeader((Header)eh);
//  		System.out.println(request);
	        SIPRequest sipRequest = (SIPRequest)request;
        
	        AllowHeader allow1 = headerFactory.createAllowHeader(Request.INVITE);
			sipRequest.addHeader(allow1);
			AllowHeader allow2 = headerFactory.createAllowHeader(Request.ACK);
			sipRequest.addHeader(allow2);
			AllowHeader allow3 = headerFactory.createAllowHeader(Request.CANCEL);
			sipRequest.addHeader(allow3);
			AllowHeader allow4 = headerFactory.createAllowHeader(Request.OPTIONS);
			sipRequest.addHeader(allow4);
			AllowHeader allow5 = headerFactory.createAllowHeader(Request.REFER);
			sipRequest.addHeader(allow5);
			AllowHeader allow6 = headerFactory.createAllowHeader(Request.MESSAGE);
			sipRequest.addHeader(allow6);
			AllowHeader allow7 = headerFactory.createAllowHeader(Request.SUBSCRIBE);
			sipRequest.addHeader(allow7);
			AllowHeader allow8 = headerFactory.createAllowHeader(Request.INFO);
			sipRequest.addHeader(allow8);
			AllowHeader allow9 = headerFactory.createAllowHeader(Request.BYE);
			sipRequest.addHeader(allow9);
        
			String acceptContactValue = "*;g.3gpp.mcdata.sds;require;explicit";
			Header acceptContactHeader = headerFactory.createHeader("Accept-Contact", acceptContactValue);
			String acceptContactValue2 = "*;g.3gpp.icsi-ref=\"urn:urn-7:3gpp-service.ims.icsi.mcdata.sds\";require;explicit"; // 替换为实际需要的值
			Header acceptContactHeader2 = headerFactory.createHeader("Accept-Contact", acceptContactValue2);
			request.addHeader(acceptContactHeader);
			request.addHeader(acceptContactHeader2);
        

			AcceptHeader acceprheader = headerFactory.createAcceptHeader("application/sdp", "vnd.3gpp.mcptt-info+xml");
			sipRequest.setHeader((Header)acceprheader);
			SipURI userURI = addressFactory.createSipURI(sipUserName, RemoteIp);
			Address userURIaddr = addressFactory.createAddress((URI)userURI);
			HeaderFactoryImpl headerfactoryImpl = new HeaderFactoryImpl();

			sipRequest.setHeader(headerFactory.createHeader("P-Preferred-Service", "urn:urn-7:3gpp-service.ims.icsi.mcdata.sds"));
			Supported supported = new Supported();
			supported.setOptionTag("timer");
			sipRequest.setHeader((Header)supported);
        
        
			SIPClientTransaction transaction = (SIPClientTransaction)sipProvider.getNewClientTransaction(sipRequest);
			Dialog dialog = sipProvider.getNewDialog(transaction);
			String target = "sip:" + groupName + "@" + sipServer;
			SipUri targetSipUri = SipMessageUtils.createSipUri(groupName, sipServer, -1, null);
			long sessionId = System.currentTimeMillis();

			Content mcdatainfoContent = InviteMcdatainfocContent(groupName, type, "sds");
			Content sdpContent = createRequestSDPContent(sessionId, this.socket, message, callIdHeader.toString());

        
			List<Content> contents = new ArrayList<>();
			contents.add(mcdatainfoContent);
        
			if(type=="one") {
				Content resourceListContent = createResourceListContent("sip:" + receiver + "@" + sipServer);
				contents.add(resourceListContent);
			}
			 // 将SDP内容添加到内容列表
			contents.add(sdpContent);

			setMutiPartContents((Request)sipRequest, contents);

			this.inviteTransaction = (ClientTransaction)transaction;
			dialog.sendRequest(transaction);
      } catch (ParseException e) {
      	e.printStackTrace();
      } catch (InvalidArgumentException e) {
      	e.printStackTrace();
      } catch (Exception p) {
      	p.printStackTrace();
      } 
      //return mediaSipSession;
  	System.out.println("fffffff");
  	InetAddress serverAddress = InetAddress.getByName("140.113.110.221");
    int serverPort = 12000;
 // 指定本地地址和端口
    InetAddress localAddress = InetAddress.getByName("192.168.0.35"); // 使用任意可用的本地地址
    int localPort = 15000; // 指定本地端口（确保端口未被占用）

    // 创建带有指定本地端口的 Socket
//    Socket socket = new Socket(serverAddress, serverPort, localAddress, localPort);
//  	OutputStream outputStream = socket.getOutputStream();
//    outputStream.write("Hello, Server!".getBytes());
//    outputStream.flush();
//
//    System.out.println("Message sent to the server.");
//
//    InputStream inputStream = socket.getInputStream();
//    byte[] buffer = new byte[1024];
//    int bytesRead = inputStream.read(buffer); // 阻塞直到有数据到达
//    if (bytesRead != -1) {
//        String response = new String(buffer, 0, bytesRead);
//        System.out.println("Message received from server: " + response);
//    }

    // 关闭连接
//    socket.close();
//  	ServerSocket serverSocket = new ServerSocket(12000);
//    Socket clientSocket = serverSocket.accept();
//    System.out.println("Connection established: " + clientSocket.getInetAddress());
  	
  	
  }
  
  private Content InviteMcdatainfocContent(String GroupURI, String type, String mcdatatype) {
  	ArrayList<String> group_list = new ArrayList<>();
	    group_list.add(GroupURI);
	    try_mcdatainfo_factory pidf = new try_mcdatainfo_factory();
	    String pidfxml="";
	    if(type=="group") {
	    	pidfxml = pidf.string_try_mcdatainfo_factory("Normal",GroupURI, String.valueOf(sipUserName) + "@" + RemoteIp, mcdatatype);
	    }else {
	    	pidfxml = pidf.string_try_mcdatainfo_factory("Normal",String.valueOf(sipUserName) + "@" + RemoteIp, mcdatatype);
	    }
	    System.out.println("////////"+pidfxml);
	    try {
	      return createContent(pidfxml, "application", "vnd.3gpp.mcdata-info+xml", null);
	    } catch (ParseException e) {
	      e.printStackTrace();
	      return null;
	    } 
    }
  
  private Content createResourceListContent(String target) throws ParseException {
      try {
        String groupName = target.substring(target.indexOf("sip:") + 4, target.indexOf("@"));
        String domain = target.substring(target.indexOf("@") + 1, target.length());
//        getGroupMember groupmember = new getGroupMember();
//        groupmember.getGroupMember(sipUserName, groupName, domain);
//        this.remoteInfoList = getGroupMember.getgroupmember();
//        Iterator<RemoteInfo> iterator = this.remoteInfoList.iterator();
//        if (iterator.hasNext()) {
//          RemoteInfo remoteInfo = iterator.next();
//          if (remoteInfo.getSipAddr() != null && !remoteInfo.getSipAddr().equals(target)) {
            String str = createResourceListString(target);
            return createContent(str, "application", "resource-lists+xml", "recipient-list");
//          } 
//          String resourceListString = createResourceListString(remoteInfo);
//          return createContent(resourceListString, "application", "resource-lists+xml", "recipient-list");
//        } 
      } catch (Exception e) {
        e.printStackTrace();
      } 
      return null;
    }
  
  private String createResourceListString(String remoteInfo) throws SAXException {
      ResourceList resourceList = new ResourceList();
      resourceList.addXmlns("urn:ietf:params:xml:ns:resource-lists");
      resourceList.addXmlnsCpList("urn:ietf:params:xml:ns:copycontrol");
//      for (String uri : remoteInfo.getUserEntries())
        resourceList.addEntry(remoteInfo, "to"); 
      ResourceListXmlBuilder listXmlBuilder = new ResourceListXmlBuilder("UTF-8", resourceList);
      byte[] bytes = listXmlBuilder.build().toByteArray();
      return new String(bytes);
    }
  
  private String createResourceListString(RemoteInfo remoteInfo) throws SAXException {
    ResourceList resourceList = new ResourceList();
    resourceList.addXmlns("urn:ietf:params:xml:ns:resource-lists");
    resourceList.addXmlnsCpList("urn:ietf:params:xml:ns:copycontrol");
    for (String uri : remoteInfo.getUserEntries())
      resourceList.addEntry(uri, "to"); 
    ResourceListXmlBuilder listXmlBuilder = new ResourceListXmlBuilder("UTF-8", resourceList);
    byte[] bytes = listXmlBuilder.build().toByteArray();
    return new String(bytes);
  }
  
  private MediaSipSession cancelMediaSession(String peerSipUserName, String peerSipDomain) {
    MediaSipSession mediaSipSessionToCancelOrBye = null;
    Map<String, MediaSipSession> mediaSessionMap = null;
    mediaSessionMap = mediaSipSessionMap;
    for (String callId : mediaSessionMap.keySet()) {
      MediaSipSession mediaSipSession = mediaSessionMap.get(callId);
      if (mediaSipSession.getPeerInfo().getPeerSipUri().getUserAtHostPort().equals(String.valueOf(peerSipUserName) + "@" + peerSipDomain)) {
        mediaSipSessionToCancelOrBye = mediaSipSession;
        break;
      } 
    } 
    if (mediaSipSessionToCancelOrBye != null)
      endSession(mediaSipSessionToCancelOrBye); 
    return mediaSipSessionToCancelOrBye;
  }
  
  private Content InviteMcpttinfocContent() {
    String clientURI = String.valueOf(sipUserName) + "@" + RemoteIp;
    try_mcpttinfo_factory mcpttinfo = new try_mcpttinfo_factory();
    String mcpttinfoxml = mcpttinfo.string_try_mcpttinfo_factory("Normal", clientURI);
    try {
      return createContent(mcpttinfoxml, "application", "vnd.3gpp.mcptt-info+xml", null);
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  private Address createContactAddress() {
    try {
      return addressFactory.createAddress("sip:" + sipUserName + "@" + registerContactAddress + ";transport=tcp");
    } catch (ParseException e) {
      return null;
    } 
  }
  
  private ArrayList<ViaHeader> createInviteViaHeader() {
    ArrayList<ViaHeader> viaHeaders = new ArrayList<>();
    try {
      ViaHeader myViaHeader = headerFactory.createViaHeader(registerContactAddress.split(":")[0], 
          localPort, transport, SipMessageUtils.generateNewBranchCode());
      myViaHeader.setRPort();
      viaHeaders.add(myViaHeader);
    } catch (ParseException e) {
      e.printStackTrace();
    } catch (InvalidArgumentException e) {
      e.printStackTrace();
    } 
    return viaHeaders;
  }
  
  private ArrayList<ViaHeader> createEMRInviteViaHeader(String localaddress, int port) {
    ArrayList<ViaHeader> viaHeaders = new ArrayList<>();
    try {
      ViaHeader myViaHeader = headerFactory.createViaHeader(localaddress, 
          port, "udp", SipMessageUtils.generateNewBranchCode());
      myViaHeader.setRPort();
      viaHeaders.add(myViaHeader);
    } catch (ParseException e) {
      e.printStackTrace();
    } catch (InvalidArgumentException e) {
      e.printStackTrace();
    } 
    return viaHeaders;
  }
  
  private Accept createAcceptContactHeader() {
    try {
      Accept accept = new Accept();
      accept.setParameter("require", null);
      accept.setParameter("explicit", null);
      accept.setHeaderName("Accept-Contact");
      return accept;
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  private UserAgent createUserAgentHeader() {
    try {
      UserAgent userAgent = new UserAgent();
      List<String> uAgentList = new ArrayList<>();
      uAgentList.add("IWF-Client");
      uAgentList.add("/3GPP");
      userAgent.setProduct(uAgentList);
      return userAgent;
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  private void setMutiPartContents(Request request, List<Content> contents) throws ParseException {
    ContentType contentType = new ContentType("multipart", "mixed");
    contentType.setParameter("boundary", "boundary1");
    MultipartMimeContentImpl multipartMimeContent = new MultipartMimeContentImpl((ContentTypeHeader)contentType);
    for (Content c : contents) {
      if (c != null)
        multipartMimeContent.addContent(c); 
    } 
    request.setContent(multipartMimeContent, (ContentTypeHeader)contentType);
  }
  
  private void setMutiPartContents2(Response response, List<Content> contents) throws ParseException {
	    ContentType contentType = new ContentType("multipart", "mixed");
	    contentType.setParameter("boundary", "boundary1");
	    MultipartMimeContentImpl multipartMimeContent = new MultipartMimeContentImpl((ContentTypeHeader)contentType);
	    for (Content c : contents) {
	      if (c != null)
	        multipartMimeContent.addContent(c); 
	    } 
	    response.setContent(multipartMimeContent, (ContentTypeHeader)contentType);
	  }
  
  private Content createContent(String contentStr, String cType, String cSubType, String contentDisPosition) throws ParseException {
    ContentTypeHeader contentType = headerFactory.createContentTypeHeader(cType, cSubType);
    ContentImpl content = new ContentImpl(contentStr);
    content.setContentTypeHeader(contentType);
    if (contentDisPosition != null) {
      ContentDispositionHeader contentDispositionHeader = headerFactory
        .createContentDispositionHeader(contentDisPosition);
      content.setContentDispositionHeader(contentDispositionHeader);
    } 
    return (Content)content;
  }
  
  private Content createRequestSDPContent(long sessionId, DatagramSocket socket, String message, String Call) throws ParseException, InternalErrorException, IOException {
	  

	
	  InetAddress myAddress = InetAddress.getByName("192.168.0.35");
//	  int localPort = 15000;
//	  ServerSocket serverSocket = new ServerSocket(localPort, 0, myAddress);
	  System.out.println(myAddress);
	  activeSession = Session.create(false, false, myAddress);
	 
	  String[] parts1 = Call.split(":");
	  java.net.URI javaNetUri = activeSession.getURI();
	  System.out.println("javaNetUri    "+javaNetUri);
      String msrpuri =javaNetUri.toString();
      String[] parts = msrpuri.split(":");
      String[] part2s = parts[2].split("/");
  	  sessionmsrpMap2.put(parts1[1].trim(), activeSession);
  	  System.out.println("---"+parts[1].replaceFirst("^//", "")+"****"+part2s[0]);
  	  String stunip = getMSRPPublicIpPort(parts[1].replaceFirst("^//", ""),part2s[0]);
//  	  String stunip = getMSRPPublicIpPort("192.168.0.35","12000");
  	  sessionmsrpmessageMap.put(parts1[1].trim(), message);
//  	  activeSession.setListener(new MSRP());  
  	  String[] stunparts = stunip.split(":");
  	  System.out.println("stun    "+stunip);
  	  System.out.println("part   "+parts[2]);
  	  System.out.println("part2s   "+part2s[0]);
      String contenttype = "message";
      String sdpData = "v=0\r\n" +
              "o=- " + sessionId + " 1 IN IP4 " + RemoteIp + "\r\n" +//?
              "s= -\r\n" +
              "c=IN IP4 " + RemoteIp + "\r\n" +
              "t=0 0\r\n" +
              "m="+contenttype+""+parts[2]+" TCP/MSRP *\r\n" + 
              "a=path:msrp://"+stunip+":"+parts[2]+"\r\n"+
//			"m="+contenttype+""+"12000"+" TCP/MSRP *\r\n" + 
//			"a=path:msrp://"+stunip+":"+"\r\n"+
              "a=sendonly\r\n"+
              "a=accept-types:application/vnd.3gpp.mcdata-signalling application/vnd.3gpp.mcdata-payload\r\n"+
              "a=setup:actpass\r\n";      // 指定音频编码格式      // 指定音频编码格式
      System.out.println(sdpData);
      
      InetAddress serverAddress = InetAddress.getByName("140.113.110.221");
      int serverPort = 12000;
   // 指定本地地址和端口
      InetAddress localAddress = InetAddress.getByName("192.168.0.35"); // 使用任意可用的本地地址
      int localPort2 = Integer.valueOf(part2s[0]); // 指定本地端口（确保端口未被占用）

      // 创建带有指定本地端口的 Socket
//      Socket socket2 = new Socket(serverAddress, serverPort, localAddress, localPort2);
      
//      if (offerSDP != null) {
//      	System.out.println("//////////////////////////////////////");
//      	System.out.println(offerSDP.encode());
        return createContent(sdpData, "application", "sdp", null); //}
      //return null;
    }
  
  private SimpleSessionDescription createOffer(long sessionId, int streamLocalPort, int rtcpPort, String localIPAddr) {
    SimpleSessionDescription offer = new SimpleSessionDescription("IWF", sessionId, localIPAddr);
//    SimpleSessionDescription.Media media = null;
//    media = offer.newMedia("audio", streamLocalPort, 1, "RTP/AVP");
//    media.setRtpPayload(0, "PCMU/8000", null);
//    media.setRtpPayload(8, "PCMA/8000", null);
//    SimpleSessionDescription.Media mediaRTCP = offer.newMedia("application", rtcpPort, 1, "udp RTCP");
//    media.setRtpPayload(127, "telephone-event/8000", "0-15");
//    offer.setAttribute("rtcp", String.valueOf(rtcpPort));
//    offer.setAttribute("fmtp", "MCPTT mc_queueing;mc_priority=4;mc_granted;mc_implicit_request");
//    offer.setAttribute("inactive", "inactive");
    return offer;
  }
  
  private SimpleSessionDescription createEMROffer(long sessionId, int streamLocalPort, int rtcpPort, String localIPAddr) {
    SimpleSessionDescription offer = new SimpleSessionDescription("IWF", sessionId, localIPAddr);
    SimpleSessionDescription.Media media = null;
//    media = offer.newMedia("audio", streamLocalPort, 1, "RTP/AVP");
//    media.setRtpPayload(0, "PCMU/8000", null);
//    media.setRtpPayload(101, "telephone-event/8000", null);
//    media.setAttribute("fmtp", "101 0-15");
//    media.setAttribute("sendrecv", "");
    return offer;
  }
  
  private void getPublicIpPort() throws SocketException {
    proxy = Proxy.getInstance();
    Random rand = new Random();
    int rtpPort = rand.nextInt(10000) + 12001;
    setRtpPort(rtpPort);
    try {
      proxy.setting(this.EMR_parameters.MCPTTUserlocalIp, getRtpPort(), this.StunServer);
    } catch (SocketException e) {
      e.printStackTrace();
    } 
    String pRtcpIP = proxy.getRTCPProxy().getPublicAddr();
    this.pRtcpPort = proxy.getRTCPProxy().getPublicPort();
    this.Handler = new handler();
    proxy.getRTCPProxy().getRTCPThread().setting(this.Handler);
    proxy.getRTCPProxy().getRTCPThread().getHandler().getPublicIpPort(this.EMR_parameters.MCPTTUserlocalIp, getRtpPort(), this.StunServer);
    String pRtpIP = proxy.getRTCPProxy().getRTCPThread().getHandler().getPublicAddr();
    this.pRtpPort = proxy.getRTCPProxy().getRTCPThread().getHandler().getPublicPort();
    if (pRtcpIP.equals(pRtpIP))
      this.pIP = pRtcpIP; 
  }
  
  private String getMSRPPublicIpPort(String localip, String localport, String serverport) throws IOException {
	    proxymcdata = ProxyMCDATA.getInstance();
//	    Random rand = new Random();
	    int msrpPort = Integer.parseInt(localport);
	    setMsrpPort(msrpPort);
	    try {
	    	proxymcdata.setting(localip, getMsrpPort(), this.StunServer, serverport);
	    } catch (SocketException e) {
	    	e.printStackTrace();
	    } 
	    
	    return proxymcdata.getMsrpProxyIp()+":"+proxymcdata.getMsrpProxyPort();
	  }
  
  private String getMSRPPublicIpPort(String ip, String port) throws IOException {
	    proxymcdata = ProxyMCDATA.getInstance();
//	    Random rand = new Random();
	    int msrpPort = Integer.parseInt(port);
	    setMsrpPort(msrpPort);
	    try {
	    	proxymcdata.setting(ip, getMsrpPort(), this.StunServer);
	    } catch (SocketException e) {
	    	e.printStackTrace();
	    } 
	    
	    return proxymcdata.getMsrpProxyIp()+":"+proxymcdata.getMsrpProxyPort();
	  }
  
  private String getMSRPPublicIpPort(String ip, String port, String username, String password) throws IOException {
	  	System.out.println("[getMSRPport]");
	  	proxymcdata = ProxyMCDATA.getInstance();
//	    Random rand = new Random();
	    int msrpPort = Integer.parseInt(port);
	    setMsrpPort(msrpPort);
	    try {
	    	proxymcdata.setting(ip, getMsrpPort(), this.StunServer, username, password);
	    } catch (SocketException e) {
	    	e.printStackTrace();
	    } 
	    
	    return proxymcdata.getMsrpProxyIp()+":"+proxymcdata.getMsrpProxyPort();
	  }
  
  public void sendPUBLISH(String groupURI, boolean affiliation) throws ParseException, InvalidArgumentException, SipException {
    AddressFactory addressFactory = SipClient.addressFactory;
    SipProvider sipProvider = SipClient.sipProvider;
    MessageFactory messageFactory = SipClient.messageFactory;
    HeaderFactory headerFactory = SipClient.headerFactory;
    URI publishURI = addressFactory.createAddress("sip:" + sipUserName + "@" + RemoteIp).getURI();
    CallIdHeader callIdheader = sipProvider.getNewCallId();
    publishCSeq++;
    CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(publishCSeq, "PUBLISH");
    Address fromAddress = addressFactory.createAddress("sip:" + sipUserName + "@" + RemoteIp);
    String tag = Utils.getInstance().generateTag();
    FromHeader fromHeader = headerFactory.createFromHeader(fromAddress, tag);
    ToHeader toHeader = headerFactory.createToHeader(fromAddress, null);
    ArrayList<ViaHeader> viaList = createPublishViaHeader();
    MaxForwardsHeader maxForwardsHeader = headerFactory.createMaxForwardsHeader(70);
    Request request = messageFactory.createRequest(publishURI, "PUBLISH", callIdheader, cSeqHeader, 
        fromHeader, toHeader, viaList, maxForwardsHeader);
    Address localAddress = addressFactory.createAddress("sip:" + sipUserName + "@" + localIp + ":" + localPort + ";transport=tcp");
    ContactHeader contactHeader = headerFactory.createContactHeader(localAddress);
    contactHeader.setParameter("isfocus", null);
    request.setHeader((Header)contactHeader);
    SIPRequest sipRequest = (SIPRequest)request;
    if (affiliation) {
      EventHeader eventHeader = headerFactory.createEventHeader("affiliation");
      sipRequest.setHeader((Header)eventHeader);
    } else {
      EventHeader eventHeader = headerFactory.createEventHeader("de-affiliation");
      sipRequest.setHeader((Header)eventHeader);
    } 
    AcceptHeader acceptheader = headerFactory.createAcceptHeader("application", "pidf+xml");
    sipRequest.setHeader((Header)acceptheader);
    SipURI userURI = addressFactory.createSipURI(sipUserName, RemoteIp);
    Address userURIaddr = addressFactory.createAddress((URI)userURI);
    HeaderFactoryImpl headerfactoryImpl = new HeaderFactoryImpl();
    PAssertedIdentityHeader PAI = headerfactoryImpl.createPAssertedIdentityHeader(userURIaddr);
    sipRequest.setHeader((Header)PAI);
    sipRequest.setHeader(headerFactory.createHeader("P-Preferred-Service", "urn:urn-7:3gpp-service.ims.icsi.mcptt"));
    if (affiliation) {
      ExpiresHeader eh = headerFactory.createExpiresHeader(2147483647);
      sipRequest.setHeader((Header)eh);
    } else {
      ExpiresHeader eh = headerFactory.createExpiresHeader(0);
      sipRequest.setHeader((Header)eh);
    } 
    Content mcpttinfoContent = PublishMcpttinfocContent();
    Content pidfContent = PublishPidfContent(groupURI);
    List<Content> contents = new ArrayList<>();
    contents.add(mcpttinfoContent);
    contents.add(pidfContent);
    setMutiPartContents((Request)sipRequest, contents);
    SIPClientTransaction transaction = (SIPClientTransaction)sipProvider.getNewClientTransaction((Request)sipRequest);
    transaction.sendRequest();
  }
  
  private Content PublishPidfContent(String GroupURI) {
    String pid = String.valueOf(sipUserName) + "0000000000001";
    String tuple_id = "sip:" + sipUserName + "@" + RemoteIp;
    ArrayList<String> group_list = new ArrayList<>();
    group_list.add(GroupURI);
    try_pidf_factory pidf = new try_pidf_factory();
    String pidfxml = pidf.string_try_pidf_factory(String.valueOf(sipUserName) + "@" + RemoteIp, tuple_id, group_list, pid);
    try {
      return createContent(pidfxml, "application", "pidf+xml", null);
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  private Content PublishMcpttinfocContent() {
    try_mcpttinfo_factory mcpttinfo = new try_mcpttinfo_factory();
    String mcpttinfoxml = mcpttinfo.string_try_mcpttinfo_factory("Normal", String.valueOf(sipUserName) + "@" + RemoteIp);
    try {
      return createContent(mcpttinfoxml, "application", "vnd.3gpp.mcptt-info+xml", null);
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    } 
  }
  
    private Content MessageMcdatainfocContent(String GroupURI, String type, String mcdatatype) {
    	//要為SDS以及FD分別生成content
//	    ArrayList<String> group_list = new ArrayList<>();
//	    group_list.add(GroupURI);
	    try_mcdatainfo_factory pidf = new try_mcdatainfo_factory();
	    String pidfxml="";
	    if(type=="group") {
	    	pidfxml = pidf.string_try_mcdatainfo_factory("Normal",GroupURI, String.valueOf(sipUserName) + "@" + RemoteIp, mcdatatype);
	    }else {
	    	pidfxml = pidf.string_try_mcdatainfo_factory("Normal",String.valueOf(sipUserName) + "@" + RemoteIp, mcdatatype);
	    }
	    try {
	        return createContent(pidfxml, "application", "vnd.3gpp.mcdata-info+xml", null);
	    } catch (ParseException e) {
	        e.printStackTrace();
	        return null;
	    } 
	}
    
    private Content MessageMcdatainfocContent(String GroupURI, String callinguser) {
    	//要為SDS以及FD分別生成content
//	    ArrayList<String> group_list = new ArrayList<>();
//	    group_list.add(GroupURI);
	    try_mcdatainfo_factory pidf = new try_mcdatainfo_factory();
	    String pidfxml="";
	    pidfxml = pidf.string_try_mcdatainfo_factory(GroupURI,  callinguser);
	    
	    try {
	        return createContent(pidfxml, "application", "vnd.3gpp.mcdata-info+xml", null);
	    } catch (ParseException e) {
	        e.printStackTrace();
	        return null;
	    } 
	}
  
    private Content MessageDataresourcecontent(String receiver) {
	    try_mcdataresource_factory mcdatainfo = new try_mcdataresource_factory();
  		String mcpdatainfoxml = mcdatainfo.string_try_mcdataresource_factory("Normal", String.valueOf("sip:"+receiver+"@" + RemoteIp));
	    try {
	        return createContent(mcpdatainfoxml, "application", "resource-lists+xml", null);
	    } catch (ParseException e) {
	        e.printStackTrace();
	        return null;
	    } 
    }
  
    private Content MessageDatasignallingcontent(String conversationid) {
	    try_mcdatasignalling_factory mcdatasignalling = new try_mcdatasignalling_factory();
	    String mcpdatasignallingxml = mcdatasignalling.string_try_mcdatasignalling_factory("Normal", String.valueOf(sipUserName) + "@" + RemoteIp, conversationid);
	    try {
	        return createContent(mcpdatasignallingxml, "application", "vnd.3gpp.mcdata-signalling", null);
	    } catch (ParseException e) {
	        e.printStackTrace();
	        return null;//application/vnd.3gpp.mcdata-payload
	    } 
	}
    
    private Content MessageDatasignallingcontent_fd(String conversationid, String fileurl, Boolean type, Boolean type2) {
	    try_mcdatasignalling_factory mcdatasignalling = new try_mcdatasignalling_factory();
	    String mcpdatasignallingxml = mcdatasignalling.string_try_mcdatasignalling_factory_fd("Normal", String.valueOf(sipUserName) + "@" + RemoteIp, conversationid, fileurl, type, type2);
	    try {
	        return createContent(mcpdatasignallingxml, "application", "vnd.3gpp.mcdata-signalling", null);
	    } catch (ParseException e) {
	        e.printStackTrace();
	        return null;//application/vnd.3gpp.mcdata-payload
	    } 
	}
    
    private Content MessageDatasignallingcontent_not(String conversationid, String notification_type, String sipUserName) {
	    try_mcdatasignalling_factory mcdatasignalling = new try_mcdatasignalling_factory();
	    String mcpdatasignallingxml = mcdatasignalling.string_try_mcdatasignalling_factory_not( conversationid, notification_type, sipUserName);
	    try {
	        return createContent(mcpdatasignallingxml, "application", "vnd.3gpp.mcdata-signalling", null);
	    } catch (ParseException e) {
	        e.printStackTrace();
	        return null;//application/vnd.3gpp.mcdata-payload
	    } 
	}
  
  
  private Content MessageDataPayloadcontent(String content, String payloadtype, Boolean type) {
	  try_mcdatapayload_factory mcdatapayload = new try_mcdatapayload_factory();
	    String mcpdatapayloadxml = mcdatapayload.string_try_mcdatapaylaod_factory("Normal", String.valueOf(sipUserName) + "@" + RemoteIp, content, payloadtype, type);
	    System.out.println(mcpdatapayloadxml);
	    try {
	      return createContent(mcpdatapayloadxml, "application", "vnd.3gpp.mcdata-payload", null);
	    } catch (ParseException e) {
	      e.printStackTrace();
	      return null;//application/vnd.3gpp.mcdata-payload
	    } 
	  }
  
  private ArrayList<ViaHeader> createPublishViaHeader() {
    ArrayList<ViaHeader> viaHeaders = new ArrayList<>();
    try {
      ViaHeader myViaHeader = headerFactory.createViaHeader(registerContactAddress.split(":")[0], 
          localPort, transport, SipMessageUtils.generateNewBranchCode());
      viaHeaders.add(myViaHeader);
    } catch (ParseException e) {
      e.printStackTrace();
    } catch (InvalidArgumentException e) {
      e.printStackTrace();
    } 
    return viaHeaders;
  }
  
  private static ArrayList<ViaHeader> createMessageViaHeader() {
	    ArrayList<ViaHeader> viaHeaders = new ArrayList<>();
	    try {
	      ViaHeader myViaHeader = headerFactory.createViaHeader(registerContactAddress.split(":")[0], 
	          localPort, transport, SipMessageUtils.generateNewBranchCode());
	      myViaHeader.setRPort();
	      viaHeaders.add(myViaHeader);
	    } catch (ParseException e) {
	      e.printStackTrace();
	    } catch (InvalidArgumentException e) {
	      e.printStackTrace();
	    } 
	    return viaHeaders;
	  }
 
    public void sendMESSAGE1(String groupURI, String content, String type, String payloadType, String receiver, 
          Boolean type2, String conversationId) throws ParseException, InvalidArgumentException, SipException {
	  // 通用的参数设置
    	String requestURI = "sip:" + (type.equals("one") ? receiver + "@" + RemoteIp : groupURI);
		List<ViaHeader> viaHeaders;
		try {
			viaHeaders = SipClient.createMessageViaHeader();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 创建通用 Request
//		GenericRequestBuilder requestBuilder = new GenericRequestBuilder(
//		"MESSAGE", requestURI, SipClient.sipProvider.getNewCallId().getCallId(),
//		Utils.getNextSequence(), SipClient.sipUserName, RemoteIp, groupURI, RemoteIp, viaHeaders, 70
//		);
//		
//		// 添加通用 Header
//		requestBuilder.setContactHeader(SipClient.sipUserName, localIp, localPort)
//		    .addHeader("Accept-Contact", "*;g.3gpp.mcdata.sds;require;explicit")
//		    .addHeader("P-Preferred-Service", "urn:urn-7:3gpp-service.ims.icsi.mcdata.sds");
//		
//		// 根据请求类型添加内容
//		if (type.equals("one")) {
//		requestBuilder.setContent(MessageDataresourcecontent(receiver).toString(), "application/json");
//		}
//		requestBuilder.setContent(MessageMcdatainfocContent(groupURI, type).toString(), "application/json");
//		requestBuilder.setContent(MessageDatasignallingcontent(conversationId).toString(), "application/json");
//		requestBuilder.setContent(MessageDataPayloadcontent(content, payloadType, type2).toString(), "application/json");
//		
//		// 发送请求
//		Request messageRequest = requestBuilder.build();
//		SipProvider sipProvider = SipClient.sipProvider;
//		SIPClientTransaction transaction = (SIPClientTransaction) sipProvider.getNewClientTransaction(messageRequest);
//		transaction.sendRequest();
    }
  
  public void sendMESSAGE(String groupURI, String content, String type, String payloadtype, String receiver, Boolean type2, String conversationid) throws ParseException, InvalidArgumentException, SipException {
	    AddressFactory addressFactory = SipClient.addressFactory;
	    SipProvider sipProvider = SipClient.sipProvider;
	    MessageFactory messageFactory = SipClient.messageFactory;
	    HeaderFactory headerFactory = SipClient.headerFactory;
	    
	    CallIdHeader callIdheader = sipProvider.getNewCallId();
	    messageCSeq++;
	    CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(messageCSeq, "MESSAGE");
	    Address fromAddress = addressFactory.createAddress("sip:" + sipUserName + "@" + RemoteIp);
	    String tag = Utils.getInstance().generateTag();
	    FromHeader fromHeader = headerFactory.createFromHeader(fromAddress, tag);
	    String sipServer="";
	    if (RemoteIp.contains(":")) {
	        // 如果 RemoteIp 包含端口號，提取出 IP 地址部分
	        sipServer = RemoteIp.split(":")[0];
	    } else {
	        sipServer = RemoteIp;
	    }	
	    Address toAddress = addressFactory.createAddress("sip:" + groupURI);
	    ToHeader toHeader = headerFactory.createToHeader(toAddress, null);
	    URI publishURI = addressFactory.createAddress("sip:" + groupURI ).getURI();
	    if(type=="one") {
	    	toHeader = headerFactory.createToHeader(addressFactory.createAddress("sip:"+receiver+"@" + RemoteIp), null);
	    	publishURI = addressFactory.createAddress("sip:" + receiver + "@" + RemoteIp).getURI();
	    }
	    ArrayList<ViaHeader> viaList = createMessageViaHeader();
	    MaxForwardsHeader maxForwardsHeader = headerFactory.createMaxForwardsHeader(70);
	    Request request = messageFactory.createRequest(publishURI, "MESSAGE", callIdheader, cSeqHeader, 
	        fromHeader, toHeader, viaList, maxForwardsHeader);
	    
	    Address localAddress = addressFactory.createAddress("sip:" + sipUserName + "@" + localIp + ":" + localPort + ";transport=tcp");
	    ContactHeader contactHeader = headerFactory.createContactHeader(localAddress);
//	    contactHeader.setParameter("isfocus", null);
	    request.setHeader((Header)contactHeader);
	    SIPRequest sipRequest = (SIPRequest)request;
	    
	    String acceptContactValue = "*;g.3gpp.mcdata.sds;require;explicit";
        Header acceptContactHeader = headerFactory.createHeader("Accept-Contact", acceptContactValue);
        String acceptContactValue2 = "*;g.3gpp.icsi-ref=\"urn:urn-7:3gpp-service.ims.icsi.mcdata.sds\";require;explicit"; // 替换为实际需要的值
        Header acceptContactHeader2 = headerFactory.createHeader("Accept-Contact", acceptContactValue2);
        request.addHeader(acceptContactHeader);
        request.addHeader(acceptContactHeader2);
	    
//        String pPreferredIdentityValue = "public-user-identity"; // 替换为实际需要的值
//        Header pPreferredIdentityHeader = headerFactory.createHeader("P-Preferred-Identity", pPreferredIdentityValue);
//        request.addHeader(pPreferredIdentityHeader);
        
	    sipRequest.setHeader(headerFactory.createHeader("P-Preferred-Service", "urn:urn-7:3gpp-service.ims.icsi.mcdata.sds"));
	    List<Content> contents = new ArrayList<>();
	    if(type == "one") {
	    	Content resourceContent = MessageDataresourcecontent(receiver);
	    	contents.add(resourceContent);
	    }
	    Content mcdatainfoContent = MessageMcdatainfocContent(groupURI, type, "sds");
	    Content signallingContent = MessageDatasignallingcontent(conversationid);
	    Content DataPayloadContent = MessageDataPayloadcontent(content, payloadtype, type2);
	    


	    
	    
	    contents.add(mcdatainfoContent);//
	    contents.add(signallingContent);
	    contents.add(DataPayloadContent);
	    setMutiPartContents((Request)sipRequest, contents);
	    SIPClientTransaction transaction = (SIPClientTransaction)sipProvider.getNewClientTransaction((Request)sipRequest);
	    transaction.sendRequest();
	    	    	    	 
	  }
  
    public void sendMESSAGE_FD(String groupURI, File file, String type, String receiver, Boolean type2, String conversationid, Boolean download, String serverstorefileurl) throws ParseException, InvalidArgumentException, SipException {
    	HTTP_fd(file);
    	System.out.println("[fd mandatory] "+download);
    	AddressFactory addressFactory = SipClient.addressFactory;
	    SipProvider sipProvider = SipClient.sipProvider;
	    MessageFactory messageFactory = SipClient.messageFactory;
	    HeaderFactory headerFactory = SipClient.headerFactory;
	    
	    CallIdHeader callIdheader = sipProvider.getNewCallId();
	    
	    messageCSeq++;
	    CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(messageCSeq, "MESSAGE");
	    Address fromAddress = addressFactory.createAddress("sip:" + sipUserName + "@" + RemoteIp);
	    String tag = Utils.getInstance().generateTag();
	    FromHeader fromHeader = headerFactory.createFromHeader(fromAddress, tag);
	    String targetSip;
	    List<Content> contents = new ArrayList<>();
	    if ("one".equals(type)) {
	        targetSip = "sip:" + receiver + "@" + RemoteIp;
	        Content resourceContent = MessageDataresourcecontent(receiver);
	    	contents.add(resourceContent);
	    } else {
	        targetSip = "sip:" + groupURI;
	    }

	    Address targetAddress = addressFactory.createAddress(targetSip);
	    ToHeader toHeader = headerFactory.createToHeader(targetAddress, null);
	    URI publishURI = targetAddress.getURI();
	    
	    
	    ArrayList<ViaHeader> viaList = createMessageViaHeader();
	    MaxForwardsHeader maxForwardsHeader = headerFactory.createMaxForwardsHeader(70);
	    Request request = messageFactory.createRequest(publishURI, "MESSAGE", callIdheader, cSeqHeader, 
	        fromHeader, toHeader, viaList, maxForwardsHeader);
	    
	    Address localAddress = addressFactory.createAddress("sip:" + sipUserName + "@" + localIp + ":" + localPort + ";transport=tcp");
	    ContactHeader contactHeader = headerFactory.createContactHeader(localAddress);
//	    contactHeader.setParameter("isfocus", null);
	    request.setHeader((Header)contactHeader);
	    SIPRequest sipRequest = (SIPRequest)request;
	    
	    
	    //6.2.4.1
	    String acceptContactValue = "*;g.3gpp.mcdata.fd;require;explicit";
        Header acceptContactHeader = headerFactory.createHeader("Accept-Contact", acceptContactValue);
        String acceptContactValue2 = "*;g.3gpp.icsi-ref=\"urn:urn-7:3gpp-service.ims.icsi.mcdata.fd\";require;explicit"; // 替换为实际需要的值
        Header acceptContactHeader2 = headerFactory.createHeader("Accept-Contact", acceptContactValue2);
        request.addHeader(acceptContactHeader);
        request.addHeader(acceptContactHeader2);
        sipRequest.setHeader(headerFactory.createHeader("P-Preferred-Service", "urn:urn-7:3gpp-service.ims.icsi.mcdata.fd"));
        //
	    Content mcdatainfoContent = MessageMcdatainfocContent(groupURI, type, "fd");
	    System.out.println("[fileurl]"+serverstorefileurl+file.getName());
	    Content signallingContent = MessageDatasignallingcontent_fd(conversationid, serverstorefileurl+file.getName(), type2, download);
	    contents.add(mcdatainfoContent);//
	    contents.add(signallingContent);
	    setMutiPartContents((Request)sipRequest, contents);
	    SIPClientTransaction transaction = (SIPClientTransaction)sipProvider.getNewClientTransaction((Request)sipRequest);
	    transaction.sendRequest();	    	    	    	 
    }
    
    public void sendMESSAGE_FD2(String groupURI, String type, String receiver, Boolean type2, String conversationid, Boolean download, String fileurl,String serverstorefileurl) throws ParseException, InvalidArgumentException, SipException, IOException {
    	String response = uploadFileWithExternalBody(
    		    "http://"+RemoteIp+":8080/restcomm/?cmd=FDfileUpload",
    		    fileurl,
    		    RemoteIp,
    		    false,  // 是否是群组传输
    		    "originating-user-id",
    		    "group-id"
    		);
    	System.out.println("[fd mandatory] "+download);
    	String[] parts = fileurl.split("/");
    	String filename = parts[parts.length - 1];
    	AddressFactory addressFactory = SipClient.addressFactory;
	    SipProvider sipProvider = SipClient.sipProvider;
	    MessageFactory messageFactory = SipClient.messageFactory;
	    HeaderFactory headerFactory = SipClient.headerFactory;
	    
	    CallIdHeader callIdheader = sipProvider.getNewCallId();
	    
	    messageCSeq++;
	    CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(messageCSeq, "MESSAGE");
	    Address fromAddress = addressFactory.createAddress("sip:" + sipUserName + "@" + RemoteIp);
	    String tag = Utils.getInstance().generateTag();
	    FromHeader fromHeader = headerFactory.createFromHeader(fromAddress, tag);
	    String targetSip;
	    List<Content> contents = new ArrayList<>();
	    if ("one".equals(type)) {
	        targetSip = "sip:" + receiver + "@" + RemoteIp;
	        Content resourceContent = MessageDataresourcecontent(receiver);
	    	contents.add(resourceContent);
	    } else {
	        targetSip = "sip:" + groupURI;
	    }

	    Address targetAddress = addressFactory.createAddress(targetSip);
	    ToHeader toHeader = headerFactory.createToHeader(targetAddress, null);
	    URI publishURI = targetAddress.getURI();
	    
	    
	    ArrayList<ViaHeader> viaList = createMessageViaHeader();
	    MaxForwardsHeader maxForwardsHeader = headerFactory.createMaxForwardsHeader(70);
	    Request request = messageFactory.createRequest(publishURI, "MESSAGE", callIdheader, cSeqHeader, 
	        fromHeader, toHeader, viaList, maxForwardsHeader);
	    
	    Address localAddress = addressFactory.createAddress("sip:" + sipUserName + "@" + localIp + ":" + localPort + ";transport=tcp");
	    ContactHeader contactHeader = headerFactory.createContactHeader(localAddress);
//	    contactHeader.setParameter("isfocus", null);
	    request.setHeader((Header)contactHeader);
	    SIPRequest sipRequest = (SIPRequest)request;
	    
	    
	    //6.2.4.1
	    String acceptContactValue = "*;g.3gpp.mcdata.fd;require;explicit";
        Header acceptContactHeader = headerFactory.createHeader("Accept-Contact", acceptContactValue);
        String acceptContactValue2 = "*;g.3gpp.icsi-ref=\"urn:urn-7:3gpp-service.ims.icsi.mcdata.fd\";require;explicit"; // 替换为实际需要的值
        Header acceptContactHeader2 = headerFactory.createHeader("Accept-Contact", acceptContactValue2);
        request.addHeader(acceptContactHeader);
        request.addHeader(acceptContactHeader2);
        sipRequest.setHeader(headerFactory.createHeader("P-Preferred-Service", "urn:urn-7:3gpp-service.ims.icsi.mcdata.fd"));
        //
	    Content mcdatainfoContent = MessageMcdatainfocContent(groupURI, type, "fd");

	    System.out.println("[fileurl]"+serverstorefileurl+filename);
	    Content signallingContent = MessageDatasignallingcontent_fd(conversationid, serverstorefileurl+filename, type2, download);
	    contents.add(mcdatainfoContent);//
	    contents.add(signallingContent);
	    setMutiPartContents((Request)sipRequest, contents);
	    SIPClientTransaction transaction = (SIPClientTransaction)sipProvider.getNewClientTransaction((Request)sipRequest);
	    transaction.sendRequest();	    	    	    	 
    }
    
    public enum DownloadDecision {
        ACCEPTED,   // 用户接受请求
        REJECTED,   // 用户拒绝请求
        DEFERRED    // 用户选择延迟处理
    }

    
    public void sendmessage_notification(String receiver, String groupURI, String conversationid, String notification_type, String callinguser) throws ParseException, InvalidArgumentException, SipException {

    	AddressFactory addressFactory = SipClient.addressFactory;
	    SipProvider sipProvider = SipClient.sipProvider;
	    MessageFactory messageFactory = SipClient.messageFactory;
	    HeaderFactory headerFactory = SipClient.headerFactory;
	    
	    CallIdHeader callIdheader = sipProvider.getNewCallId();
	    
	    messageCSeq++;
	    CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(messageCSeq, "MESSAGE");
	    Address fromAddress = addressFactory.createAddress("sip:" + sipUserName + "@" + RemoteIp);
	    String tag = Utils.getInstance().generateTag();
	    FromHeader fromHeader = headerFactory.createFromHeader(fromAddress, tag);
	    String targetSip;
	    List<Content> contents = new ArrayList<>();
        targetSip = "sip:" + receiver + "@" + RemoteIp;
        Content resourceContent = MessageDataresourcecontent(receiver);
//    	contents.add(resourceContent);

	    Address targetAddress = addressFactory.createAddress(targetSip);
	    ToHeader toHeader = headerFactory.createToHeader(targetAddress, null);
	    URI publishURI = targetAddress.getURI();
	    
	    
	    ArrayList<ViaHeader> viaList = createMessageViaHeader();
	    MaxForwardsHeader maxForwardsHeader = headerFactory.createMaxForwardsHeader(70);
	    Request request = messageFactory.createRequest(publishURI, "MESSAGE", callIdheader, cSeqHeader, 
	        fromHeader, toHeader, viaList, maxForwardsHeader);
	    
	    Address localAddress = addressFactory.createAddress("sip:" + sipUserName + "@" + localIp + ":" + localPort + ";transport=tcp");
	    ContactHeader contactHeader = headerFactory.createContactHeader(localAddress);
//	    contactHeader.setParameter("isfocus", null);
	    request.setHeader((Header)contactHeader);
	    SIPRequest sipRequest = (SIPRequest)request;
	    
	    
	    //6.2.4.1
	    String acceptContactValue = "*;g.3gpp.mcdata.fd;require;explicit";
       Header acceptContactHeader = headerFactory.createHeader("Accept-Contact", acceptContactValue);
       String acceptContactValue2 = "*;g.3gpp.icsi-ref=\"urn:urn-7:3gpp-service.ims.icsi.mcdata.fd\";require;explicit"; // 替换为实际需要的值
       Header acceptContactHeader2 = headerFactory.createHeader("Accept-Contact", acceptContactValue2);
       request.addHeader(acceptContactHeader);
       request.addHeader(acceptContactHeader2);
       sipRequest.setHeader(headerFactory.createHeader("P-Preferred-Service", "urn:urn-7:3gpp-service.ims.icsi.mcdata.fd"));
       //
	   Content mcdatainfoContent = MessageMcdatainfocContent(groupURI, callinguser);
	   contents.add(mcdatainfoContent);
	    Content signallingContent = MessageDatasignallingcontent_not(conversationid, notification_type, sipUserName);
	    
	    contents.add(signallingContent);
	    setMutiPartContents((Request)sipRequest, contents);
	    SIPClientTransaction transaction = (SIPClientTransaction)sipProvider.getNewClientTransaction((Request)sipRequest);
	    transaction.sendRequest();	    	    	    	 
   }
  
  public void sendMESSAGEtest(String groupURI, String content, String type, String payloadtype, String receiver, Boolean type2, String conversationid) throws ParseException, InvalidArgumentException, SipException {
	  	AddressFactory addressFactory = SipClient.addressFactory;
	    SipProvider sipProvider = SipClient.sipProvider;
	    MessageFactory messageFactory = SipClient.messageFactory;
	    HeaderFactory headerFactory = SipClient.headerFactory;

	    CallIdHeader callIdheader = sipProvider.getNewCallId();
	    messageCSeq++;
	    CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(messageCSeq, "MESSAGE");

	    // 创建 From 和 To 头
	    FromHeader fromHeader = createFromHeader();
	    ToHeader toHeader = createToHeader(groupURI);
	    URI publishURI = addressFactory.createAddress("sip:" + groupURI).getURI();

	    if ("one".equals(type)) {
	        toHeader = createToHeader(receiver + "@" + RemoteIp);
	        publishURI = addressFactory.createAddress("sip:" + receiver + "@" + RemoteIp).getURI();
	    }

	    ArrayList<ViaHeader> viaList = createMessageViaHeader();
	    MaxForwardsHeader maxForwardsHeader = headerFactory.createMaxForwardsHeader(70);

	    // 创建 MESSAGE 请求
	    Request request = messageFactory.createRequest(publishURI, "MESSAGE", callIdheader, cSeqHeader, fromHeader, toHeader, viaList, maxForwardsHeader);

	    // 添加 Contact 头
	    request.setHeader(createContactHeader());

	    // 添加其他公共头部
	    for (Header header : createCommonHeaders()) {
	        request.addHeader(header);
	    }

	    // 处理 Content
	    List<Content> contents = createMessageContents(groupURI, receiver, type, conversationid, content, payloadtype, type2);
	    setMutiPartContents((Request) request, contents);

	    // 发送请求
	    SIPClientTransaction transaction = (SIPClientTransaction) sipProvider.getNewClientTransaction((Request) request);
	    transaction.sendRequest();
	  }
  
  private FromHeader createFromHeader() throws ParseException {
	    Address fromAddress = addressFactory.createAddress("sip:" + sipUserName + "@" + RemoteIp);
	    String tag = Utils.getInstance().generateTag();
	    return headerFactory.createFromHeader(fromAddress, tag);
	}

	private ToHeader createToHeader(String recipient) throws ParseException {
	    Address toAddress = addressFactory.createAddress("sip:" + recipient);
	    return headerFactory.createToHeader(toAddress, null);
	}

	private ContactHeader createContactHeader() throws ParseException {
	    Address localAddress = addressFactory.createAddress("sip:" + sipUserName + "@" + localIp + ":" + localPort + ";transport=tcp");
	    return headerFactory.createContactHeader(localAddress);
	}

	private List<Header> createCommonHeaders() throws ParseException {
	    List<Header> headers = new ArrayList<>();
	    headers.add(headerFactory.createHeader("Accept-Contact", "*;g.3gpp.mcdata.sds;require;explicit"));
	    headers.add(headerFactory.createHeader("Accept-Contact", "*;g.3gpp.icsi-ref=\"urn:urn-7:3gpp-service.ims.icsi.mcdata.sds\";require;explicit"));
	    headers.add(headerFactory.createHeader("P-Preferred-Service", "urn:urn-7:3gpp-service.ims.icsi.mcdata.sds"));
	    return headers;
	}
  
	private List<Content> createMessageContents(String groupURI, String receiver, String type, String conversationid, String content, String payloadtype, Boolean type2) {
	    List<Content> contents = new ArrayList<>();

	    if ("one".equals(type)) {
	        contents.add(MessageDataresourcecontent(receiver));
	    }
	    contents.add(MessageMcdatainfocContent(groupURI, type, "fd"));//??
	    contents.add(MessageDatasignallingcontent(conversationid));
	    contents.add(MessageDataPayloadcontent(content, payloadtype, type2));

	    return contents;
	}
	
  private Content createSDSsiggnallingPayload(String target) throws ParseException {
	    try {
	      String groupName = target.substring(target.indexOf("sip:") + 4, target.indexOf("@"));
	      String domain = target.substring(target.indexOf("@") + 1, target.length());
	      getGroupMember groupmember = new getGroupMember();
	      groupmember.getGroupMember(sipUserName, groupName, domain);
	      this.remoteInfoList = getGroupMember.getgroupmember();
	      Iterator<RemoteInfo> iterator = this.remoteInfoList.iterator();
	      if (iterator.hasNext()) {
	        RemoteInfo remoteInfo = iterator.next();
	        if (remoteInfo.getSipAddr() != null && !remoteInfo.getSipAddr().equals(target)) {
	          String str = createResourceListString(remoteInfo);
	          return createContent(str, "application", "resource-list+xml", "recipient-list");
	        } 
	        String resourceListString = createResourceListString(remoteInfo);
	        return createContent(resourceListString, "application", "resource-list+xml", "recipient-list");
	      } 
	    } catch (Exception e) {
	      e.printStackTrace();
	    } 
	    return null;
	  }
  
  private Content createSDSD(String target) throws ParseException {
	    try {
	      String groupName = target.substring(target.indexOf("sip:") + 4, target.indexOf("@"));
	      String domain = target.substring(target.indexOf("@") + 1, target.length());
	      getGroupMember groupmember = new getGroupMember();
	      groupmember.getGroupMember(sipUserName, groupName, domain);
	      this.remoteInfoList = getGroupMember.getgroupmember();
	      Iterator<RemoteInfo> iterator = this.remoteInfoList.iterator();
	      if (iterator.hasNext()) {
	        RemoteInfo remoteInfo = iterator.next();
	        if (remoteInfo.getSipAddr() != null && !remoteInfo.getSipAddr().equals(target)) {
	          String str = createResourceListString(remoteInfo);
	          return createContent(str, "application", "resource-list+xml", "recipient-list");
	        } 
	        String resourceListString = createResourceListString(remoteInfo);
	        return createContent(resourceListString, "application", "resource-list+xml", "recipient-list");
	      } 
	    } catch (Exception e) {
	      e.printStackTrace();
	    } 
	    return null;
	  }
  
  public void sendReferPRG(String groupName, String sipServer) throws ParseException, InvalidArgumentException, SipException, ParserConfigurationException, TransformerException {
    AddressFactory addressFactory = SipClient.addressFactory;
    SipProvider sipProvider = SipClient.sipProvider;
    MessageFactory messageFactory = SipClient.messageFactory;
    HeaderFactory headerFactory = SipClient.headerFactory;
    String groupuri = String.valueOf(groupName) + "@" + sipServer;
    String temp = "sip:" + groupuri;
    MediaSipSession mediaSipSession = null;
    for (String key : mediaSipSessionMap.keySet()) {
      MediaSipSession session = mediaSipSessionMap.get(key);
      if (session.getPeerInfo().getPeerSipUri().toString().equals(temp))
        mediaSipSession = session; 
    } 
    SIPDialog sipDialog = (SIPDialog)mediaSipSession.getDialog();
    SIPRequest sipRequest = (SIPRequest)sipDialog.createRequest("REFER");
    sipRequest.getRequestLine().setUri((URI)mediaSipSession.getPeerInfo().getPeerSipUri());
    Address localAddress = createContactAddress();
    ContactHeader contactHeader = headerFactory.createContactHeader(localAddress);
    contactHeader.setParameter("isfocus", null);
    sipRequest.setHeader((Header)contactHeader);
    String referToTarget = MediaSipSession.getSipTarget();
    Address referToAddr = addressFactory.createAddress(referToTarget);
    ReferTo referTo = (ReferTo)headerFactory.createReferToHeader(referToAddr);
    referTo.setParameter("session", "prearranged");
    sipRequest.setHeader((Header)referTo);
    SipURI userURI = addressFactory.createSipURI(sipUserName, localIp);
    Address userURIaddr = addressFactory.createAddress((URI)userURI);
    HeaderFactoryImpl headerfactoryImpl = new HeaderFactoryImpl();
    PPreferredIdentityHeader PPI = headerfactoryImpl.createPPreferredIdentityHeader(userURIaddr);
    sipRequest.setHeader((Header)PPI);
    sipRequest.setHeader(headerFactory.createHeader("P-Preferred-Service", "urn:urn-7:3gpp-service.ims.icsi.mcptt"));
    Content mcpttinfoContent = ReferMcpttinfocContent(groupName);
    Content sdpContent = createReferSDPContent(sessinID, this.socket, null);
    List<Content> contents = new ArrayList<>();
    contents.add(mcpttinfoContent);
    contents.add(sdpContent);
    setMutiPartContents((Request)sipRequest, contents);
    sipRequest.getTopmostViaHeader().setRPort();
    SIPClientTransaction transaction = (SIPClientTransaction)sipProvider.getNewClientTransaction((Request)sipRequest);
    mediaSipSession.getDialog().sendRequest((ClientTransaction)transaction);
    if (!getPESessionE()) {
      proxy.getRTCPProxy().getRTCPThread().getHandler().setSession(mediaSipSession);
      proxy.start();
      instance = this;
      proxy.getRTCPProxy().getRTCPThread().getHandler().setTwoModeHandler(1, mySdpInfo.getIp(), mySdpInfo.getPort(), 
          this.EMR_parameters.MCPTTUserlocalIp, getRtpPort(), this.EMR_parameters.LMRLocalIp, 12001, EMRSdpInfo.getIp(), EMRSdpInfo.getPort());
    } else {
      proxy.getRTCPProxy().getRTCPThread().getHandler().setAllParm(1024, mySdpInfo.getIp(), mySdpInfo.getPort(), 
          this.EMR_parameters.MCPTTUserlocalIp, getRtpPort(), this.EMR_parameters.LMRLocalIp, 12001, "255.255.255.255", 30000);
      proxy.getRTCPProxy().getRTCPThread().getHandler().setSession(mediaSipSession);
      proxy.start();
    } 
  }
  
  private Content ReferMcpttinfocContent(String groupName) throws ParserConfigurationException, TransformerException {
    String clientURI = String.valueOf(sipUserName) + "@" + RemoteIp;
    String groupURI = String.valueOf(groupName) + "@" + RemoteIp;
    try_mcpttinfo_factory mcpttinfo = new try_mcpttinfo_factory();
    String mcpttinfoxml = mcpttinfo.string_try_mcpttinfo_factory("Normal", clientURI, groupURI);
    try {
      return createContent(mcpttinfoxml, "application", "vnd.3gpp.mcptt-info+xml", null);
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  private Content createReferSDPContent(long sessionId, DatagramSocket socket, MediaSipSession mediaSipSession) throws ParseException {
    SimpleSessionDescription offerSDP = null;
    offerSDP = createOffer(sessionId, this.pRtpPort, this.pRtcpPort, this.pIP);
    if (offerSDP != null)
      return createContent(offerSDP.encode(), "application", "sdp", null); 
    return null;
  }
  
  public void sendConferenceSubscribe(String sipUri) {}
  
  private void sendSubscribe(String sipUri, int expires, String accept, String event, boolean isXcapDiff) {
    try {
      CSeqHeader cSeqHeader;
      CallIdHeader callIdHeader;
      Address toAddress;
      ToHeader toHeader;
      FromHeader fromHeader;
      SipUri sipUri1;
      ArrayList<ViaHeader> viaList = createViaHeader();
      if (isXcapDiff) {
        toAddress = addressFactory.createAddress("sip:" + 
            sipUserName + "@" + RemoteIp);
      } else {
        toAddress = addressFactory.createAddress(sipUri);
      } 
      Address fromAddress = addressFactory.createAddress("sip:" + 
          sipUserName + "@" + RemoteIp);
      if (this.subscribeMap.containsKey(sipUri)) {
        SubscribeDialog subscribeDialog = this.subscribeMap.get(sipUri);
        cSeqHeader = headerFactory.createCSeqHeader(Long.valueOf(subscribeDialog.getCSeq()).longValue() + 1L, "SUBSCRIBE");
        fromHeader = headerFactory.createFromHeader(fromAddress, subscribeDialog.getMyTag());
        callIdHeader = headerFactory.createCallIdHeader(subscribeDialog.getCallId());
        toHeader = headerFactory.createToHeader(toAddress, subscribeDialog.getPeerTag());
      } else {
        cSeqHeader = headerFactory.createCSeqHeader(1L, "SUBSCRIBE");
        viaList = createViaHeader();
        fromHeader = headerFactory.createFromHeader(fromAddress, SipMessageUtils.generateNewTag());
        callIdHeader = SipClient.sipProvider.getNewCallId();
        toHeader = headerFactory.createToHeader(toAddress, null);
      } 
      SipProvider sipProvider = SipClient.sipProvider;
      if (isXcapDiff) {
        URI requestUri = fromAddress.getURI();
        //??
        String[] usernameHost = sipUri.split("@");
        String userName = usernameHost[0].replace("sip:", "");
        String host = usernameHost[1];
        sipUri1 = SipMessageUtils.createSipUri(userName, host, RemotePort, null);
        //??
      } else {
        String[] usernameHost = sipUri.split("@");
        String userName = usernameHost[0].replace("sip:", "");
        String host = usernameHost[1];
        sipUri1 = SipMessageUtils.createSipUri(userName, host, RemotePort, null);
      } 
      Address localAddress = createContactAddress();
      ContactHeader contactHeader = headerFactory.createContactHeader(localAddress);
      MaxForwardsHeader maxForwardsHeader = headerFactory.createMaxForwardsHeader(70);
      Request request = messageFactory.createRequest((URI)sipUri1, "SUBSCRIBE", callIdHeader, cSeqHeader, fromHeader, toHeader, viaList, maxForwardsHeader);
      AcceptHeader acceptHeader = headerFactory.createAcceptHeader("application", accept);
      request.setHeader((Header)contactHeader);
      request.setHeader((Header)acceptHeader);
      EventHeader eventHeader = headerFactory.createEventHeader(event);
      ExpiresHeader expiresHeader = headerFactory.createExpiresHeader(expires);
      request.setHeader((Header)eventHeader);
      request.setHeader((Header)expiresHeader);
      ClientTransaction clientTransaction = sipProvider.getNewClientTransaction(request);
      clientTransaction.sendRequest();
    } catch (ParseException e) {
      e.printStackTrace();
    } catch (InvalidArgumentException e) {
      e.printStackTrace();
    } catch (TransactionUnavailableException e) {
      e.printStackTrace();
    } catch (SipException e) {
      e.printStackTrace();
    } 
  }
  
  public void EmergencyStateUpgrade(String groupName, String sipServer) throws SipException, ParseException, ParserConfigurationException, TransformerException {
    if (!getEmergencyState()) {
      setEmergencyState(true);
      AddressFactory addressFactory = SipClient.addressFactory;
      SipProvider sipProvider = SipClient.sipProvider;
      MessageFactory messageFactory = SipClient.messageFactory;
      HeaderFactory headerFactory = SipClient.headerFactory;
      String groupuri = String.valueOf(groupName) + "@" + sipServer;
      String temp = "sip:" + groupuri;
      MediaSipSession mediaSipSession = null;
      for (String key : mediaSipSessionMap.keySet()) {
        MediaSipSession session = mediaSipSessionMap.get(key);
        if (session.getPeerInfo().getPeerSipUri().toString().equals(temp))
          mediaSipSession = session; 
      } 
      SIPDialog sipDialog = (SIPDialog)mediaSipSession.getDialog();
      SIPRequest sipRequest = (SIPRequest)sipDialog.createRequest("INVITE");
      String conferenceFactoryUri = groupName;
      conferenceFactoryUri = String.valueOf(conferenceFactoryUri) + "_preestablished";
      SipUri sipUri = SipMessageUtils.createSipUri(conferenceFactoryUri, RemoteIp, RemotePort, null);
      ((Parameters)sipUri).setParameter("session", "prearranged");
      sipRequest.getRequestLine().setUri((URI)sipUri);
      Address localAddress = createContactAddress();
      ContactHeader contactHeader = headerFactory.createContactHeader(localAddress);
      contactHeader.setParameter("isfocus", null);
      sipRequest.setHeader((Header)contactHeader);
      sipRequest.setHeader((Header)createAcceptContactHeader());
      SipURI userURI = addressFactory.createSipURI(sipUserName, RemoteIp);
      Address userURIaddr = addressFactory.createAddress((URI)userURI);
      HeaderFactoryImpl headerfactoryImpl = new HeaderFactoryImpl();
      PAssertedIdentityHeader PAI = headerfactoryImpl.createPAssertedIdentityHeader(userURIaddr);
      sipRequest.setHeader((Header)PAI);
      sipRequest.setHeader(headerFactory.createHeader("P-Preferred-Service", "urn:urn-7:3gpp-service.ims.icsi.mcptt"));
      EventHeader eventHeader = headerFactory.createEventHeader("MEGC");
      sipRequest.setHeader((Header)eventHeader);
      Content mcpttinfoContent = StateUpgradeMcpttinfo(groupName);
      List<Content> contents = new ArrayList<>();
      contents.add(mcpttinfoContent);
      setMutiPartContents((Request)sipRequest, contents);
      SIPClientTransaction transaction = (SIPClientTransaction)sipProvider.getNewClientTransaction((Request)sipRequest);
      mediaSipSession.getDialog().sendRequest((ClientTransaction)transaction);
      this.stateUpgrageCseq = sipRequest.getCSeqHeader();
      try {
        Thread.sleep(500L);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } 
    } 
  }
  
  private Content StateUpgradeMcpttinfo(String groupName) throws ParserConfigurationException, TransformerException {
    String clientURI = String.valueOf(sipUserName) + "@" + RemoteIp;
    String groupURI = String.valueOf(groupName) + "@" + RemoteIp;
    try_mcpttinfo_factory mcpttinfo = new try_mcpttinfo_factory();
    String mcpttinfoxml = mcpttinfo.string_try_mcpttinfo_factory("Normal", clientURI, groupURI, true, true);
    try {
      return createContent(mcpttinfoxml, "application", "vnd.3gpp.mcptt-info+xml", null);
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public void EmergencyStateCancel(String groupName, String sipServer) throws ParseException, SipException, ParserConfigurationException, TransformerException {
    if (getEmergencyState()) {
      setEmergencyState(false);
      AddressFactory addressFactory = SipClient.addressFactory;
      SipProvider sipProvider = SipClient.sipProvider;
      MessageFactory messageFactory = SipClient.messageFactory;
      HeaderFactory headerFactory = SipClient.headerFactory;
      String groupuri = String.valueOf(groupName) + "@" + sipServer;
      String temp = "sip:" + groupuri;
      MediaSipSession mediaSipSession = null;
      for (String key : mediaSipSessionMap.keySet()) {
        MediaSipSession session = mediaSipSessionMap.get(key);
        if (session.getPeerInfo().getPeerSipUri().toString().equals(temp))
          mediaSipSession = session; 
      } 
      SIPDialog sipDialog = (SIPDialog)mediaSipSession.getDialog();
      SIPRequest sipRequest = (SIPRequest)sipDialog.createRequest("INVITE");
      String conferenceFactoryUri = groupName;
      conferenceFactoryUri = String.valueOf(conferenceFactoryUri) + "_preestablished";
      SipUri sipUri = SipMessageUtils.createSipUri(conferenceFactoryUri, RemoteIp, RemotePort, null);
      ((Parameters)sipUri).setParameter("session", "prearranged");
      sipRequest.getRequestLine().setUri((URI)sipUri);
      Address localAddress = createContactAddress();
      ContactHeader contactHeader = headerFactory.createContactHeader(localAddress);
      contactHeader.setParameter("isfocus", null);
      sipRequest.setHeader((Header)contactHeader);
      sipRequest.setHeader((Header)createAcceptContactHeader());
      SipURI userURI = addressFactory.createSipURI(sipUserName, RemoteIp);
      Address userURIaddr = addressFactory.createAddress((URI)userURI);
      HeaderFactoryImpl headerfactoryImpl = new HeaderFactoryImpl();
      PAssertedIdentityHeader PAI = headerfactoryImpl.createPAssertedIdentityHeader(userURIaddr);
      sipRequest.setHeader((Header)PAI);
      sipRequest.setHeader(headerFactory.createHeader("P-Preferred-Service", "urn:urn-7:3gpp-service.ims.icsi.mcptt"));
      EventHeader eventHeader = headerFactory.createEventHeader("MEGC");
      sipRequest.setHeader((Header)eventHeader);
      Content mcpttinfoContent = StateCancelMcpttinfo(groupName);
      List<Content> contents = new ArrayList<>();
      contents.add(mcpttinfoContent);
      setMutiPartContents((Request)sipRequest, contents);
      SIPClientTransaction transaction = (SIPClientTransaction)sipProvider.getNewClientTransaction((Request)sipRequest);
      mediaSipSession.getDialog().sendRequest((ClientTransaction)transaction);
      this.stateCancelCseq = sipRequest.getCSeqHeader();
      try {
        Thread.sleep(500L);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } 
    } 
  }
  
  private Content StateCancelMcpttinfo(String groupName) throws ParserConfigurationException, TransformerException {
    String clientURI = String.valueOf(sipUserName) + "@" + RemoteIp;
    String groupURI = String.valueOf(groupName) + "@" + RemoteIp;
    try_mcpttinfo_factory mcpttinfo = new try_mcpttinfo_factory();
    String mcpttinfoxml = mcpttinfo.string_try_mcpttinfo_factory("Normal", clientURI, groupURI, false, false);
    try {
      return createContent(mcpttinfoxml, "application", "vnd.3gpp.mcptt-info+xml", null);
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public void InitialEmergencyCall(String groupName, String sipServer) throws SipException, ParseException, InvalidArgumentException, ParserConfigurationException, TransformerException {
    if (!this.GroupState) {
      AddressFactory addressFactory = SipClient.addressFactory;
      SipProvider sipProvider = SipClient.sipProvider;
      MessageFactory messageFactory = SipClient.messageFactory;
      HeaderFactory headerFactory = SipClient.headerFactory;
      String groupuri = String.valueOf(groupName) + "@" + sipServer;
      String temp = "sip:" + groupuri;
      MediaSipSession mediaSipSession = null;
      for (String key : mediaSipSessionMap.keySet()) {
        MediaSipSession session = mediaSipSessionMap.get(key);
        if (session.getPeerInfo().getPeerSipUri().toString().equals(temp))
          mediaSipSession = session; 
      } 
      SIPDialog sipDialog = (SIPDialog)mediaSipSession.getDialog();
      SIPRequest sipRequest = (SIPRequest)sipDialog.createRequest("REFER");
      SipUri sipUri = SipMessageUtils.createSipUri(groupName, RemoteIp, RemotePort, null);
      sipRequest.getRequestLine().setUri((URI)sipUri);
      Address localAddress = createContactAddress();
      ContactHeader contactHeader = headerFactory.createContactHeader(localAddress);
      contactHeader.setParameter("isfocus", null);
      sipRequest.setHeader((Header)contactHeader);
      String referToTarget = MediaSipSession.getSipTarget();
      Address referToAddr = addressFactory.createAddress(referToTarget);
      ReferTo referTo = (ReferTo)headerFactory.createReferToHeader(referToAddr);
      referTo.setParameter("session", "prearranged");
      sipRequest.setHeader((Header)referTo);
      SipURI userURI = addressFactory.createSipURI(sipUserName, localIp);
      Address userURIaddr = addressFactory.createAddress((URI)userURI);
      HeaderFactoryImpl headerfactoryImpl = new HeaderFactoryImpl();
      PPreferredIdentityHeader PPI = headerfactoryImpl.createPPreferredIdentityHeader(userURIaddr);
      sipRequest.setHeader((Header)PPI);
      sipRequest.setHeader(headerFactory.createHeader("P-Preferred-Service", "urn:urn-7:3gpp-service.ims.icsi.mcptt"));
      EventHeader eventHeader = headerFactory.createEventHeader("MEGC");
      sipRequest.setHeader((Header)eventHeader);
      sipRequest.getTopmostViaHeader().setRPort();
      Content mcpttinfoContent = InitialEmergencyMcpttinfocContent(groupName);
      Content sdpContent = createReferSDPContent(sessinID, this.socket, null);
      List<Content> contents = new ArrayList<>();
      contents.add(mcpttinfoContent);
      contents.add(sdpContent);
      setMutiPartContents((Request)sipRequest, contents);
      SIPClientTransaction transaction = (SIPClientTransaction)sipProvider.getNewClientTransaction((Request)sipRequest);
      mediaSipSession.getDialog().sendRequest((ClientTransaction)transaction);
      proxy.getRTCPProxy().getRTCPThread().getHandler().setAllParm(1024, mySdpInfo.getIp(), mySdpInfo.getPort(), 
          this.EMR_parameters.MCPTTUserlocalIp, getRtpPort(), this.EMR_parameters.LMRLocalIp, 12001, "127.0.0.1", 30000);
      proxy.getRTCPProxy().getRTCPThread().getHandler().setSession(mediaSipSession);
      proxy.start();
    } 
  }
  
  private Content InitialEmergencyMcpttinfocContent(String groupName) throws ParserConfigurationException, TransformerException {
    String clientURI = String.valueOf(sipUserName) + "@" + RemoteIp;
    String groupURI = String.valueOf(groupName) + "@" + RemoteIp;
    try_mcpttinfo_factory mcpttinfo = new try_mcpttinfo_factory();
    String mcpttinfoxml = mcpttinfo.string_try_mcpttinfo_factory("Normal", clientURI, groupURI, true, true);
    try {
      return createContent(mcpttinfoxml, "application", "vnd.3gpp.mcptt-info+xml", null);
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public void closeSession(MediaSipSession mediaSipSession) {
    if (mediaSipSession == null)
      return; 
    sendReferBye(mediaSipSession, "prearranged");
    try {
      Thread.sleep(800L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } 
    sendBye(mediaSipSession);
  }
  
  public void sendReferBye(MediaSipSession mediaSipSession, String sessionType) {
    try {
      SIPDialog sipDialog = (SIPDialog)mediaSipSession.getDialog();
      SIPRequest sipRequest = (SIPRequest)sipDialog.createRequest("REFER");
      String target = "sip:" + this.GroupName + "@" + RemoteIp;
      Address referToAddr = addressFactory.createAddress(target);
      ReferTo referTo = (ReferTo)headerFactory.createReferToHeader(referToAddr);
      referTo.setParameter("session", sessionType);
      referTo.setParameter("method", "BYE");
      sipRequest.setHeader((Header)referTo);
      URI requestURI = addressFactory.createAddress("sip:" + RemoteEndpoint + ";transport=tcp").getURI();
      sipRequest.getRequestLine().setUri(requestURI);
      SIPClientTransactionImpl sipClientTransaction = (SIPClientTransactionImpl)sipProvider.getNewClientTransaction((Request)sipRequest);
      sipDialog.sendRequest((ClientTransaction)sipClientTransaction);
    } catch (SipException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    } 
  }
  
  public void sendBye(MediaSipSession mediaSipSession) {
    try {
      SIPDialog sipDialog = (SIPDialog)mediaSipSession.getDialog();
      SIPRequest request = (SIPRequest)sipDialog.createRequest("BYE");
      request.getTopmostViaHeader().setRPort();
      URI requestURI = addressFactory.createAddress("sip:" + RemoteEndpoint + ";transport=tcp").getURI();
      request.getRequestLine().setUri(requestURI);
      SIPClientTransactionImpl sipClientTransaction = (SIPClientTransactionImpl)sipProvider.getNewClientTransaction((Request)request);
      sipDialog.sendRequest((ClientTransaction)sipClientTransaction);
      endSession(mediaSipSession);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  private void endSession(MediaSipSession mediaSipSession) {
    if (mediaSipSession.getMeaningSender() != null || mediaSipSession.getMediaSessionState() != null) {
      mediaSipSession.getMeaningSender().stop();
      mediaSipSession.getMediaSessionState().stop();
    } 
    if (proxy != null)
      proxy.stopProxy(); 
    mediaSipSessionMap.remove(MediaSipSession.getCallId());
    if (!mediaSipSessionMap.isEmpty())
      mediaSipSessionMap.clear(); 
    this.GroupName = "";
  }
  
  public void processDialogTerminated(DialogTerminatedEvent arg0) {}
  
  public void processIOException(IOExceptionEvent arg0) {}
  
  public void processRequest(RequestEvent requestEvent) {
	  System.out.println("processrequest");
	  RequestObject requestObj = parseRequestObj(requestEvent);
	  String domain = requestObj.getCallIdString();
	  domain = domain.substring(domain.indexOf("@") + 1, domain.length());
	  SIPServerTransactionImpl sipServerTransaction = null;

	  SIPDialog sipDialog = null;
	  System.out.println("requestObj"+requestObj);
	  System.out.println("sipServerTransaction"+sipServerTransaction);
	  if (requestObj == null)
		  return; 
//	  if (sipServerTransaction == null)
//	      return; 
	  String requestMethod = requestObj.getMethod();
	  System.out.println("requestMethos:"+requestMethod);
	    if (requestMethod.equals("INVITE")) {
	    	System.out.println("requestMethos1:"+requestMethod);
	      try {
	          sipServerTransaction = getSipServerTransaction(requestEvent);
	        onInviteRequest(requestObj, sipServerTransaction, sipDialog);
	      } catch (PeerUnavailableException peerUnavailableException) {
	      
	      } catch (ParseException e) {
	        e.printStackTrace();
	      } catch (IOException e) {
	        e.printStackTrace();
	      } catch (TransformerException e) {
	        e.printStackTrace();
	      } catch (InternalErrorException e) {
			e.printStackTrace();
		} 
	    } else if (requestMethod.equals("BYE")) {
	    	sipServerTransaction = getSipServerTransaction(requestEvent);
	    	System.out.println("requestMethos2:"+requestMethod);
	      onByeRequest(requestObj, sipServerTransaction);
	    } else if (requestMethod.equals("NOTIFY")) {
	    	System.out.println("requestMethos3:"+requestMethod);
	      try {
	          sipServerTransaction = getSipServerTransaction(requestEvent);
	    	  System.out.println("requestMethos33:"+requestMethod);
	        onNotifyRequest(requestObj, sipServerTransaction);
	      } catch (PeerUnavailableException e) {
	        e.printStackTrace();
	      } catch (ParseException e) {
	        e.printStackTrace();
	      } catch (IOException e) {
	        e.printStackTrace();
	      } 
	    } else if (requestMethod.equals("MESSAGE")) {
	    	System.out.println("requestMethos4:"+requestMethod);
	      try {
	          sipServerTransaction = getSipServerTransaction(requestEvent);
	        onMessageRequest(requestObj, sipServerTransaction);
	      } catch (ParseException e) {
	        e.printStackTrace();
	      } catch (InvalidArgumentException e) {
			// 
			e.printStackTrace();
		} catch (SipException e) {
			// 
			e.printStackTrace();
		} 
	    } else if (!requestMethod.equals("REGISTER")) {
	    	System.out.println("requestMethos5:"+requestMethod);
	      if (!requestMethod.equals("ACK"))
	        if (requestMethod.equals("CANCEL")) {
	            sipServerTransaction = getSipServerTransaction(requestEvent);
	          onCancel(requestObj, sipServerTransaction);
	        } else if (requestMethod.equals("INFO")) {
	          onInfo(requestObj, sipServerTransaction);
	        }  
	    } 
  }
  
  private void onInfo(RequestObject requestObj, SIPServerTransactionImpl sipServerTransaction) {
    sendOKFromRequest((Request)requestObj.getSipRequest(), sipServerTransaction);
    SIPRequest sIPRequest = requestObj.getSipRequest();
    String content = new String(sIPRequest.getRawContent());
    String value = content.substring(content.indexOf("signal=") + 7, content.length());
    if (value.equals("emer_on")) {
      if (!getEmergencyState() || !getGroupState())
        try {
          EmergencyStateUpgrade(this.GroupName, RemoteIp);
        } catch (SipException e) {
          e.printStackTrace();
        } catch (ParseException e) {
          e.printStackTrace();
        } catch (ParserConfigurationException e) {
          e.printStackTrace();
        } catch (TransformerException e) {
          e.printStackTrace();
        }  
    } else {
      value.equals("ptt_gaurd");
    } 
  }
  
  private void send481Response(SIPServerTransactionImpl sipServerTransaction, Request inviteRequest) {
    try {
      SIPResponse response = (SIPResponse)messageFactory.createResponse(481, inviteRequest);
      UserAgent userAgent = new UserAgent();
      List<String> uAgentList = new ArrayList<>();
      uAgentList.add("IWF-Client");
      uAgentList.add("/3GPP");
      userAgent.setProduct(uAgentList);
      response.setHeader((Header)userAgent);
      Require r = new Require();
      r.setOptionTag("timer");
      response.setHeader((Header)r);
      response.getTo().setTag(Utils.getInstance().generateTag());
      sipServerTransaction.sendResponse((Response)response);
    } catch (ParseException e) {
      e.printStackTrace();
    } catch (SipException e) {
      e.printStackTrace();
    } 
  }
  
  private void sendTrying(SIPServerTransactionImpl sipServerTransaction, Request inviteRequest) {
    try {
      SIPResponse response = (SIPResponse)messageFactory.createResponse(100, inviteRequest);
      UserAgent userAgent = new UserAgent();
      List<String> uAgentList = new ArrayList<>();
      uAgentList.add("IWF-Client");
      uAgentList.add("/3GPP");
      userAgent.setProduct(uAgentList);
      response.setHeader((Header)userAgent);
      Require r = new Require();
      r.setOptionTag("timer");
      response.setHeader((Header)r);
      response.getTo().setTag(Utils.getInstance().generateTag());
      sipServerTransaction.sendResponse((Response)response);
    } catch (ParseException e) {
      e.printStackTrace();
    } catch (SipException e) {
      e.printStackTrace();
    } 
  }
  
  private void sendRing(SIPServerTransactionImpl sipServerTransaction, Request inviteRequest) {
    try {
      SIPResponse response = (SIPResponse)messageFactory.createResponse(180, inviteRequest);
      UserAgent userAgent = new UserAgent();
      List<String> uAgentList = new ArrayList<>();
      uAgentList.add("IWF-Client");
      uAgentList.add("/3GPP");
      userAgent.setProduct(uAgentList);
      response.setHeader((Header)userAgent);
      Require r = new Require();
      r.setOptionTag("timer");
      response.setHeader((Header)r);
      response.getTo().setTag(Utils.getInstance().generateTag());
      sipServerTransaction.sendResponse((Response)response);
    } catch (ParseException|SipException e) {
      e.printStackTrace();
    } 
  }
  
  private void onCancel(RequestObject requestObject, SIPServerTransactionImpl sipServerTransaction) {
    String CallID = requestObject.getCallIdString();
    CallID = CallID.substring(CallID.indexOf("@") + 1, CallID.length());
    if (CallID.equals(this.EMRinviteCallID)) {
      MediaSipSession EMRmediaSipSession = EMRmediaSipSessionMap.get(CallID);
      endEMRSession(EMRmediaSipSession);
      sendOKFromRequest((Request)requestObject.getSipRequest(), sipServerTransaction);
    } 
  }
  
  private void onEMRAck(RequestObject requestObject) {}
  
  private void onInviteRequest(RequestObject requestObj, SIPServerTransactionImpl sipServerTransaction, SIPDialog sipDialog) throws PeerUnavailableException, ParseException, IOException, TransformerException, InternalErrorException {
  	  	String domain = requestObj.getCallIdString();
        domain = domain.substring(domain.indexOf("@") + 1, domain.length());
        SIPRequest siprequest = requestObj.getSipRequest();
        Response response = null;
        Response response1 = null;
        try {
      	  response1 = messageFactory.createResponse(100, (Request)siprequest);
      	  sipServerTransaction.sendResponse(response1);
      	  response = messageFactory.createResponse(200, (Request)siprequest);
      	  ToHeader toHeader = (ToHeader) response.getHeader(ToHeader.NAME);
      	  toHeader.setTag(myTag);
      	  System.out.println(response);
      	  response.addHeader(headerFactory.createRequireHeader("timer"));
  	      ExpiresHeader sessionExpiresHeader = headerFactory.createExpiresHeader(1800); // 1800 表示 30 分钟的计时器，可根据需求调整
//  	       ExpiresHeader.setRefresher(ExpiresHeader.REFRESHER_UAC);
  	      response.addHeader(sessionExpiresHeader);	
  	      Address contactAddress = addressFactory.createAddress("sip:" + sipUserName + 
  	              "@" + registerContactAddress + ";transport=tcp");
  	      response.addHeader((Header)headerFactory.createContactHeader(contactAddress));
//  	      System.out.println("[invite]stun public address"+registerContactAddress);
  	      ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader("application", "sdp");

  	      String xmlString = requestObj.getContentString();
  	      try_parser get1 = new try_parser();
  	      String sdp = get1.string_try_mcdata_sdp(xmlString);
  	      InetAddress myAddress = InetAddress.getByName("192.168.0.35");
  	      System.out.println("[invite] local address"+myAddress);
  	      System.out.println("[invite] server sdp "+sdp);
  	      
  	      String[] parts1 = sdp.split(":");
	      String[] part2s1 = parts1[2].split("/");
  	      
  	      long sessionId = System.currentTimeMillis();
//  	      Session passiveSession;
  		
  	      String sdp1 = get1.string_try_mcdata_info_groupuri(xmlString);
//  	      passiveSession = Session.create(false, false,new java.net.URI(sdp), myAddress, Integer.valueOf(part2s1[0]) );
  	      Session passiveSession = Session.create(false, true,new java.net.URI(sdp), myAddress);
//  	    Session passiveSession = Session.create(false, true, myAddress);
//  	      System.out.println("[invite]stunparts"+sdp+"-----"+myAddress);
  	      MSRP msrp = new MSRP("group");
  	      if(sdp1==null) {
  	    	 msrp = new MSRP("one");
  	      }
//  	    System.out.println("[invite]msrpuri test//////////////////////////////");
//  	      ArrayList<java.net.URI> targetUris = new ArrayList<>();
//  	    java.net.URI ur = new java.net.URI(sdp);
//  	    	targetUris.add(ur);
//  	      passiveSession.setToPath(targetUris);
//
//    	    System.out.println("[invite]msrpuri test//////////////////////////////");
  	      msrp.addPropertyChangeListener(this);
  	      passiveSession.setListener(msrp);  	
	      java.net.URI javaNetUri = passiveSession.getURI();
	      String msrpuri =javaNetUri.toString();
	      System.out.println("[invite]msrpuri "+msrpuri);
	      String[] parts = msrpuri.split(":");
	      String[] part2s = parts[2].split("/");
	      System.out.println("[invite]parts "+parts[1].replaceFirst("^//", ""));
	      System.out.println("[invite]part2s "+part2s[0]);
	      System.out.println("[invite]part2s1 "+part2s1[0]);
//	      String stunip = getMSRPPublicIpPort(parts[1].replaceFirst("^//", ""),part2s[0],part2s1[0]);
	      
	      //turn server
//	      String stunip = getMSRPPublicIpPort(parts[1].replaceFirst("^//", ""),part2s[0],"user", "123456");
//	      String[] stunparts = stunip.split(":");
//	      System.out.println("[invite]stun ip "+stunip);
	      String contenttype = "message";
	      
	      String sdpData = "v=0\r\n" +
  		                "o=- " + sessionId + " 1 IN IP4 " + RemoteIp + "\r\n" +//?
  		                "s= -\r\n" +
  		                "c=IN IP4 " + RemoteIp + "\r\n" +
  		                "t=0 0\r\n" +
  		                "m="+contenttype+""+parts[2]+" TCP/MSRP *\r\n" + //需修改 用msrp port
  		                "a=path:"+msrpuri+"\r\n"+
//  		                "a=path:msrp://"+stunip+"/"+part2s[1]+"\r\n"+//111.
  		                "a=sendonly\r\n"+
  		                "a=accept-types:application/vnd.3gpp.mcdata-signalling application/vnd.3gpp.mcdata-payload\r\n"+
  		                "a=setup:actpass\r\n";      // 指定音频编码格式      // 指定音频编码格式

	      Content sdpContent = createContent(sdpData, "application", "sdp", null);
	      System.out.println("[invite]sdpdata "+sdpData);	
  		    
	      List<Content> contents = new ArrayList<>();
	      contents.add(sdpContent);
	      setMutiPartContents2((Response)response, contents);
  	      
  	      System.out.println("////////////////////////////response"+response);
  	      Dialog dialog = sipServerTransaction.getDialog();

  	      sipServerTransaction.sendResponse(response);
//  	      onByeRequest(requestObj,sipServerTransaction);
        } catch (ParseException e) {
          e.printStackTrace();
        } catch (SipException e) {
          e.printStackTrace();
        } catch (InvalidArgumentException e) {
  			e.printStackTrace();
        } catch (InternalErrorException | URISyntaxException e) {
  			e.printStackTrace();
  		}
      }
  
  private void createresponse(RequestObject requestObj, SIPServerTransactionImpl sipServerTransaction, SIPDialog sipDialog) {
    try {
      SIPRequest sipRequest = requestObj.getSipRequest();
      SIPRequest sIPRequest1 = sipRequest;
      SIPResponse response = (SIPResponse)messageFactory.createResponse(200, (Request)sIPRequest1);
      AllowHeader allow1 = headerFactory.createAllowHeader("INVITE");
      response.addHeader((Header)allow1);
      AllowHeader allow2 = headerFactory.createAllowHeader("ACK");
      response.addHeader((Header)allow2);
      AllowHeader allow3 = headerFactory.createAllowHeader("CANCEL");
      response.addHeader((Header)allow3);
      AllowHeader allow4 = headerFactory.createAllowHeader("OPTIONS");
      response.addHeader((Header)allow4);
      AllowHeader allow5 = headerFactory.createAllowHeader("REFER");
      response.addHeader((Header)allow5);
      AllowHeader allow6 = headerFactory.createAllowHeader("MESSAGE");
      response.addHeader((Header)allow6);
      AllowHeader allow7 = headerFactory.createAllowHeader("SUBSCRIBE");
      response.addHeader((Header)allow7);
      AllowHeader allow8 = headerFactory.createAllowHeader("INFO");
      response.addHeader((Header)allow8);
      Address contactaddress = addressFactory.createAddress("sip:" + this.EMR_parameters.EMRCardNo + "@" + this.EMR_parameters.LMRLocalIp + ";transport=udp");
      ContactHeader contactheader = headerFactory.createContactHeader(contactaddress);
      response.setHeader((Header)contactheader);
      List<String> uAgentList = new ArrayList<>();
      uAgentList.add("IWF");
      UserAgent userAgent = new UserAgent();
      userAgent.setProduct(uAgentList);
      response.setHeader((Header)userAgent);
      response.getTo().setTag(Utils.getInstance().generateTag());
      String OfferSDP = createEMRoffer(this.EMR_parameters.LMRLocalIp, 12001);
      ContentType contentType = new ContentType("application", "sdp");
      response.setContent(OfferSDP, (ContentTypeHeader)contentType);
      sipDialog = (SIPDialog)EMRProvider.getNewDialog((Transaction)sipServerTransaction);
      setEMRInviteResponse(response);
      setEMRTransaction(sipServerTransaction);
      try {
        getEMRSDPInfo(requestObj.getSdp());
      } catch (Exception e1) {
        e1.printStackTrace();
      } 
      String targeturi = String.valueOf(this.EMR_parameters.EMRCardNo) + "@" + this.EMR_parameters.LMRLocalIp;
      String target = "sip:" + targeturi;
      SipUri targetSipUri = SipMessageUtils.createSipUri(targeturi.split("@")[0], targeturi.split("@")[1], -1, null);
      MediaSipSession EMRmediaSipSession = new MediaSipSession(requestObj.getCallIdString(), myTag, (Dialog)sipDialog, target, 
          targetSipUri, false);
      EMRmediaSipSession.getUserActiveMap().put(requestObj.getFrom().toString(), "in");
      EMRmediaSipSessionMap.put(MediaSipSession.getCallId(), EMRmediaSipSession);
    } catch (SipException|ParseException|SocketException e) {
      e.printStackTrace();
    } 
  }
  
  private void pasercallinfo(SIPRequest sipRequest) {
    String CallInfo = sipRequest.toString().split("Call-Info:")[1].split("Content-Length:")[0];
    String kyinfo = CallInfo.substring(CallInfo.indexOf("KyInfo_F=") + 9, CallInfo.length());
    setCallInfoValue(Integer.parseInt(kyinfo.replaceAll("[^\\d]", "")));
  }
  
  public void UpgradeCancel() {
    if (getCallInfoValue() == 99) {
      try {
        EmergencyStateUpgrade(this.GroupName, RemoteIp);
      } catch (SipException e) {
        e.printStackTrace();
      } catch (ParseException e) {
        e.printStackTrace();
      } catch (ParserConfigurationException e) {
        e.printStackTrace();
      } catch (TransformerException e) {
        e.printStackTrace();
      } 
    } else {
      try {
        EmergencyStateCancel(this.GroupName, RemoteIp);
      } catch (ParseException e) {
        e.printStackTrace();
      } catch (SipException e) {
        e.printStackTrace();
      } catch (ParserConfigurationException e) {
        e.printStackTrace();
      } catch (TransformerException e) {
        e.printStackTrace();
      } 
    } 
  }
  
  private void sendinvite() {
    try {
      setPESessionE(false);
      List<String> GetDirectInfo = getAffiliatedGroup(sipUserName, RemoteIp);
//      sendInvite(GetDirectInfo.get(0), RemoteIp, 100);
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
  
  private void setEMRInviteResponse(SIPResponse response) {
    EMRresponse = response;
  }
  
  private SIPResponse getEMRInviteResponse() {
    return EMRresponse;
  }
  
  private void setEMRTransaction(SIPServerTransactionImpl Transaction) {
    this.EMRTransaction = Transaction;
  }
  
  private SIPServerTransactionImpl getEMRTransaction() {
    return this.EMRTransaction;
  }
  
  public void sendEmrInviteOKResponse() {
    try {
      getEMRTransaction().sendResponse((Response)getEMRInviteResponse());
    } catch (SipException e) {
      e.printStackTrace();
    } 
  }
  
  private SIPServerTransactionImpl getSipServerTransaction(RequestEvent requestEvent) {
    SIPServerTransactionImpl sipServerTransaction = (SIPServerTransactionImpl)requestEvent.getServerTransaction();
    if (sipServerTransaction == null)
      try {
        sipServerTransaction = (SIPServerTransactionImpl)sipProvider.getNewServerTransaction(requestEvent.getRequest());
      } catch (TransactionAlreadyExistsException e) {
        e.printStackTrace();
      } catch (TransactionUnavailableException e) {
        e.printStackTrace();
      }  
    return sipServerTransaction;
  }
  
  private SIPServerTransactionImpl getEMRSipServerTransaction(RequestEvent requestEvent) {
    SIPServerTransactionImpl sipServerTransaction = (SIPServerTransactionImpl)requestEvent.getServerTransaction();
    if (sipServerTransaction == null)
      try {
        sipServerTransaction = (SIPServerTransactionImpl)EMRProvider.getNewServerTransaction(requestEvent.getRequest());
      } catch (TransactionAlreadyExistsException e) {
        e.printStackTrace();
      } catch (TransactionUnavailableException e) {
        e.printStackTrace();
      }  
    return sipServerTransaction;
  }
  
  private RequestObject parseRequestObj(RequestEvent requestEvent) {
    RequestObject requestObj = null;
    try {
      requestObj = RequestObject.parse(requestEvent.getRequest());
    } catch (PeerUnavailableException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    } 
    return requestObj;
  }
  
    private void onMessageRequest(RequestObject requestObj, SIPServerTransactionImpl sipServerTransaction) throws ParseException, InvalidArgumentException, SipException {
//
//        // 获取消息类型
//        String messageType = sipMessage.getHeader("Type"); // 可能是 "group" 或 "one"
//
//        // 获取对应的处理器并处理
//        MessageHandler handler = MessageHandlerFactory.getHandler(requestObj);
//        handler.handle(sipMessage);
    	try {
    		SIPRequest sIPRequest = requestObj.getSipRequest();
    		
    		Header preferredServiceHeader = sIPRequest.getHeader("P-Preferred-Service");
    		String extractedValue = "";
            // 检查是否存在该头部
            if (preferredServiceHeader != null) {
                String preferredServiceValue = preferredServiceHeader.toString();
                System.out.println("P-Preferred-Service: " + preferredServiceValue);
                Pattern pattern = Pattern.compile("mcdata\\.(\\S+)");
                Matcher matcher = pattern.matcher(preferredServiceValue);

                if (matcher.find()) {
                    extractedValue = matcher.group(1);
                    System.out.println("Extracted value: " + extractedValue);
                } else {
                    System.out.println("No match found.");
                }
            } else {
                System.out.println("P-Preferred-Service header not found.");
            }
    		
            if(extractedValue.equals("sds")) {
	    		String eventType = getMessageEventType(requestObj);
	    		String xmlString = requestObj.getContentString();
	    		try_parser get1 = new try_parser();
	    		String emergencyind1 = get1.string_try_mcdata_payload(xmlString);
	    		String emergencyind2 = get1.string_try_mcdata_payload_content_type(xmlString);
	    		String emergencyind3 = get1.string_try_mcdata_signalling_senderid(xmlString);
	    		String emergencyind4 = get1.string_try_mcdata_signalling_conversationid(xmlString);
	    		String emergencyind5 = get1.string_try_mcdata_info_requesttype(xmlString);
	    		String emergencyind6 = "";
	    		if(emergencyind5.equals("one-to-one-sds")) {
	//    	  		emergencyind6 = get1.string_try_mcdata_info_senderid(xmlString);
	    		}else {
	    			emergencyind6 = get1.string_try_mcdata_info_groupuri(xmlString);
	    		}
	//      System.out.println("66"+emergencyind6);
	    		String[] parts = emergencyind1.split(",");
	    		String contenttype="";
	    		switch(emergencyind2) {
	    			case "00000001":
			      		contenttype="TEXT";
			      		break;
			      	case "00000010":
			      		contenttype="BINARY";
			      		break;
			      	case "00000011":
			      		contenttype="HYPERLINKS";
			      		break;
			      	case "00000100":
			      		contenttype="FILEURL";
			      		break;
		        }
	    		String[] emergencyData = {parts[0], parts[1], contenttype, emergencyind3,emergencyind4,emergencyind5,emergencyind6};
	    		support.firePropertyChange("MCDatasipMessage", null, emergencyData);

		        Response response = null;
		        try {
		        	response = messageFactory.createResponse(200, (Request)sIPRequest);
		        	System.out.println("response"+response);
		        	sipServerTransaction.sendResponse(response);
		        } catch (ParseException e) {
		        	e.printStackTrace();
		        } catch (SipException e) {
		        	e.printStackTrace();
		        } 
		        System.out.println("[message]: 1");

            }else {
            	
            	sendOKFromRequest((Request)requestObj.getSipRequest(), sipServerTransaction);
            	String xmlString = requestObj.getContentString();
	    		try_parser get1 = new try_parser();
	    		if(get1.string_try_mcdata_notification(xmlString)=="false") {
		    		String emergencyind = get1.string_try_mcdata_signalling_mandatory(xmlString);
		    		if(emergencyind=="true") {
		    			String content =get1.string_try_mcdata_payload_fd_payload_content(xmlString);
		    			String[] parts = content.split(", ", 2);
		    			String path = parts.length > 1 ? parts[1] : "";
		    			String callinguser = get1.string_try_mcdata_info_callinguser(xmlString);
		    			// 取得 "FDfile/" 之後的值
		    			String[] pathParts = path.split("FDfile/", 2);
		    			String fileName = pathParts.length > 1 ? pathParts[1] : "";
		    			String conversationid = get1.string_try_mcdata_signalling_conversationid(xmlString);
		    			String emergencyind5 = get1.string_try_mcdata_signalling_applicationid(xmlString);
		    			String useruri = get1.string_try_mcdata_payload_fd_useruri(xmlString);
		    			if(emergencyind5=="true") {
		    				//todo
			    		}else {
	//		    			String emergencyind6 = get1.string_try_mcdata_signalling_payloadcontent(xmlString);
			    			//notify usere autodownload
			    		}
	//	    			generate an FD NOTIFICATION indicating
	//	    			shall attempt to download the file
		    			String emergencyind6 = get1.string_try_mcdata_info_requesttype(xmlString);
		    			String emergencyind1 = get1.string_try_mcdata_payload(xmlString);
		    			String[] emergencyData = {emergencyind1, emergencyind6, "mandatory download", content, useruri};
		    			support.firePropertyChange("MCDatasipMessageFD", null, emergencyData);
		    			String groupuri = null;
		    			if(emergencyind6.equals("group")){
		    				groupuri=get1.string_try_mcdata_info_groupuri(xmlString);
		    			}
		    			startDownload(path, fileName, groupuri, conversationid,callinguser);
		    		}else {
		    			String content =get1.string_try_mcdata_payload_fd_payload_content(xmlString);
	//	    			System.out.println("[test1]"+content);
		    			String[] parts = content.split(", ", 2);
		    			String path = parts.length > 1 ? parts[1] : "";
	
		    			// 取得 "FDfile/" 之後的值
		    			String[] pathParts = path.split("FDfile/", 2);
		    			String fileName = pathParts.length > 1 ? pathParts[1] : "";
	
		    			System.out.println("完整路徑: " + path);
		    			System.out.println("文件名稱: " + fileName);
		    			String useruri = get1.string_try_mcdata_payload_fd_useruri(xmlString);
	//	    			System.out.println("[test2]"+useruri);
		    			String conversationid = get1.string_try_mcdata_signalling_conversationid(xmlString);
		    			String apporuser = get1.string_try_mcdata_signalling_applicationid(xmlString);
		    			if(apporuser=="true") {
		    				//todo
			    		}else {		    			
			    			String emergencyind6 = get1.string_try_mcdata_signalling_payloadcontent(xmlString);//detect for user or not	    			
			    		}
	
		    			String emergencyind6 = get1.string_try_mcdata_info_requesttype(xmlString);//null
		    			String callinguser = get1.string_try_mcdata_info_callinguser(xmlString);
		    			String emergencyind1 = get1.string_try_mcdata_payload(xmlString);
		    			System.out.println("[emergencyind1]"+emergencyind1);
		    			String[] emergencyData = {emergencyind1, emergencyind6, "not mandatory download", content, useruri};
		    			support.firePropertyChange("MCDatasipMessageFD", null, emergencyData);
		    			String groupuri = null;
		    			if(emergencyind6.equals("group")){
		    				groupuri=get1.string_try_mcdata_info_groupuri(xmlString);//
		    			}
		    			
		    			//start  timer
		
		            	clientUI.showDownloadDialog(fileName, instance);
		            	System.out.println("[start download]");
		            	System.out.println("[downloadDecision2]"+instance.downloadDecision);
		            	DownloadDecision  down = getDownloadDecision();
		            	System.out.println("[start download]"+down);
		            	switch (down) {
		            		
			                case ACCEPTED:
			                    // 用户接受请求，执行下载
			                	System.out.println("[getDownloadDecision] ACCEPTED");
			                    startDownload(path, fileName, groupuri, conversationid,callinguser);
			                    break;
			                case REJECTED:
			                    // 用户拒绝请求，取消操作
			                	System.out.println("[getDownloadDecision] REJECTED");
			                    cancelDownload(groupuri, conversationid,callinguser);
			                    break;
			                case DEFERRED:
			                    // 用户选择稍后下载，可能存储状态或者提醒用户
			                	System.out.println("[getDownloadDecision] DEFERRED");
			                    deferDownload(groupuri, conversationid,callinguser);
			                    break;
			                default:
			                    throw new IllegalStateException("Unexpected value: " + down);
			            }
	//	            	HTTP_fd_download(path,fileName);
	//	    			sendmessage_notification("mcdataserver", groupuri, conversationid);
		    		}
		    		
	            }else {
	    			String notificationtype = get1.string_try_mcdata_notification(xmlString);
	    			String senderuser = get1.string_try_mcdata_sender(xmlString);
	    			System.out.println("[senderuser] "+senderuser);
	    			if(notificationtype.equals("00000001")) {
	    				System.out.println("[getDownloadDecision] ACCEPTED"+senderuser);
	    				String[] emergencyData = {"ACCEPTED",senderuser};
	    				support.firePropertyChange("MCDatasipMessageFDnotification", null, emergencyData);
//	    				accepted
	    			}else if(notificationtype.equals("00000010")) {
	    				System.out.println("[getDownloadDecision] REJECTED"+senderuser);

	    				String[] emergencyData = {"REJECTED",senderuser};
	    				support.firePropertyChange("MCDatasipMessageFDnotification", null, emergencyData);
//	    				rejected
	    			}else if(notificationtype.equals("00000011")) {
	    				System.out.println("[getDownloadDecision] complete"+senderuser);

	    				String[] emergencyData = {"complete",senderuser};
	    				support.firePropertyChange("MCDatasipMessageFDnotification", null, emergencyData);
//	    				completed
	    			}else if(notificationtype.equals("00000100")) {
	    				System.out.println("[getDownloadDecision] DEFERRED"+senderuser);
	    				String[] emergencyData = {"DEFERRED",senderuser};
	    				support.firePropertyChange("MCDatasipMessageFDnotification", null, emergencyData);
//	    				deffered
	    			}
	    		}
    		}
	    } catch (ParseException e) {
	        e.printStackTrace();
	    } 
    }
    
    private void startDownload(String path, String fileName, String groupuri, String conversationid, String callinguser) throws ParseException, InvalidArgumentException, SipException {
        System.out.println("开始下载...");
        sendmessage_notification("mcdataserver", groupuri, conversationid, "accept", callinguser);
        HTTP_fd_download(path, fileName);
        sendmessage_notification("mcdataserver", groupuri, conversationid, "download", callinguser);
    }

    private void cancelDownload(String groupuri, String conversationid, String callinguser) throws ParseException, InvalidArgumentException, SipException {
        System.out.println("下载已取消。");
        sendmessage_notification("mcdataserver", groupuri, conversationid, "rejected", callinguser);
    }

    private void deferDownload(String groupuri, String conversationid, String callinguser) throws ParseException, InvalidArgumentException, SipException {
        System.out.println("下载已延迟，将稍后提醒用户。");
        sendmessage_notification("mcdataserver", groupuri, conversationid, "deferred", callinguser);
    }
    
    private void startFileDownload() {
        System.out.println("正在下載文件...");
    }
    
    public void setDownloadDecision(DownloadDecision decision) {
    	instance.downloadDecision = decision;
        System.out.println("[setDownloadDecision]"+instance.downloadDecision);
    }

    public synchronized DownloadDecision getDownloadDecision() {
//        while (this.downloadDecision == null) {
//            try {
//                wait();
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        }
        return instance.downloadDecision;
    }
  

  

  
  
    private String getMessageEventType(RequestObject requestObj) {
        if (!requestObj.getSipRequest().hasHeader("Event"))
            return null; 
        return ((EventHeader)requestObj.getSipRequest().getHeader("Event")).getEventType().replaceAll("\\s+", "");
    }
  
  private void onByeRequest(RequestObject requestObj, SIPServerTransactionImpl sipServerTransaction) {
	  SIPRequest siprequest = requestObj.getSipRequest();
      Response response = null;
      try {
      	System.out.println("1");
    	  response = messageFactory.createResponse(200, (Request)siprequest);
    	System.out.println("1");
    	  ContactHeader contactHeader = (ContactHeader) siprequest.getHeader(ContactHeader.NAME);
    	System.out.println("1");
//    	  response.addHeader(contactHeader);
    	System.out.println("1");
    	  System.out.println(response.toString());
//    	  response.addHeader(headerFactory.createRequireHeader("timer"));
	      sipServerTransaction.sendResponse(response);
      } catch (ParseException e) {
        e.printStackTrace();
      } catch (SipException e) {
        e.printStackTrace();
      }
//    SIPRequest sIPRequest = requestObj.getSipRequest();
//    boolean EMRrequest = false;
//    if (requestObj.getCallIdString().equals(this.EMRinviteCallID))
//      EMRrequest = true; 
//    if (!EMRrequest) {
//      for (String callIdKey : mediaSipSessionMap.keySet()) {
//        if (callIdKey.equals(requestObj.getCallIdString())) {
//          MediaSipSession mediaSipSession = mediaSipSessionMap.get(callIdKey);
//          endSession(mediaSipSession);
//          Response response = null;
//          try {
//            response = messageFactory.createResponse(200, (Request)sIPRequest);
//            sipServerTransaction.sendResponse(response);
//          } catch (ParseException e) {
//            e.printStackTrace();
//          } catch (SipException e) {
//            e.printStackTrace();
//          } 
//          setEmergencyState(false);
//          setGroupState(false);
//          if (!EMRmediaSipSessionMap.isEmpty()) {
//            MediaSipSession EMRmediaSipSession = null;
//            for (String key : EMRmediaSipSessionMap.keySet()) {
//              EMRmediaSipSession = EMRmediaSipSessionMap.get(key);
//              sendEMRBye(EMRmediaSipSession);
//            } 
//          } 
//          clientUI.Session_Refresh.doClick();
////          try {
////            Thread.sleep(800L);
////            sendInvite(requestObj.getFromHeaderUserName(), RemoteIp, 100);
////            System.out.println("[SipClient][onByeRequest()] resendInvite");
////          } catch (IOException e1) {
////            e1.printStackTrace();
////          } catch (Exception e1) {
////            e1.printStackTrace();
////          } 
//          return;
//        } 
//      } 
//    } else {
//      for (String callIdKey : EMRmediaSipSessionMap.keySet()) {
//        if (callIdKey.equals(requestObj.getCallIdString())) {
//          MediaSipSession EMRmediaSipSession = EMRmediaSipSessionMap.get(callIdKey);
//          endEMRSession(EMRmediaSipSession);
//          Response response = null;
//          try {
//            response = messageFactory.createResponse(200, (Request)sIPRequest);
//            sipServerTransaction.sendResponse(response);
//          } catch (ParseException e) {
//            e.printStackTrace();
//          } catch (SipException e) {
//            e.printStackTrace();
//          } 
//          String groupuri = String.valueOf(this.GroupName) + "@" + RemoteIp;
//          String temp = "sip:" + groupuri;
//          MediaSipSession mediaSipSession = null;
//          for (String key : mediaSipSessionMap.keySet()) {
//            MediaSipSession session = mediaSipSessionMap.get(key);
//            if (session.getPeerInfo().getPeerSipUri().toString().equals(temp))
//              mediaSipSession = session; 
//          } 
//          if (!mediaSipSession.getMediaSessionState().getstate()) {
//            DatagramSocket datagramSocket = proxy.getRTCPSocket();
//            FloorRelease(datagramSocket);
//          } 
//          return;
//        } 
//      } 
//    } 
  }
  
  private void onNotifyRequest(RequestObject requestObj, SIPServerTransactionImpl sipServerTransaction) throws PeerUnavailableException, ParseException, IOException {
	  sendOKFromRequest((Request)requestObj.getSipRequest(), sipServerTransaction);
    String eventType = getNotifyEventType(requestObj);
    if (eventType != null && 
      !eventType.equals("presence"))
      if (!eventType.equals("conference"))
        if (!eventType.contains("xcap-diff"))
          if (eventType.equals("affiliation") || eventType.equals("de-affiliation"))
            ParserResult(requestObj.getSipRequest());    
  }
  
  private String getNotifyEventType(RequestObject requestObj) {
    if (!requestObj.getSipRequest().hasHeader("Event"))
      return null; 
    return ((EventHeader)requestObj.getSipRequest().getHeader("Event")).getEventType();
  }
  
  private void sendOKFromRequest(Request request, SIPServerTransactionImpl transaction) {
    try {
    	 System.out.println("sendokfromrequest");
      Response response = messageFactory.createResponse(200, request);
      SIPResponse response1 = (SIPResponse)response;
      transaction.sendResponse(response);
//      showResponseInfo(response);
      System.out.println(response);
    } catch (ParseException e) {
      e.printStackTrace();
    } catch (SipException e) {
      e.printStackTrace();
    } 
  }
  
  private void ParserResult(SIPRequest sipRequest) throws PeerUnavailableException, ParseException, IOException {
    String EventHeader = ((EventHeader)sipRequest.getHeader("Event")).getEventType();
    RequestObject read = RequestObject.parse((Request)sipRequest);
    read.onMutiPartContent(sipRequest);
    String pidf = RequestObject.get_pidf();
    try_parser get = new try_parser();
    String pidf_pid = get.string_try_pidf_pid_get(pidf);
    List<String> pidf_grouplist = get.string_try_pidf_get(pidf);
    String groupURI = pidf_grouplist.get(0);
    String[] groupName = groupURI.split("@");
    String status = pidf_grouplist.get(1);
    status.equals("");
    HashMap<String, String> Groupdictionary = getGroupdictionary();
    List<String> Keys = getKeyList(Groupdictionary, groupURI);
    String result = null;
    if (status.equals("1")) {
      result = "The result of affiliation is successful.";
    } else if (status.equals("2")) {
      result = "The result of de-affiliation is successful.";
    } else if (status.equals("3")) {
      result = "The number of MCPTT group [" + (String)Keys.get(0) + "] " + "exceeds the upper limit.";
    } else if (status.equals("4")) {
      result = "The selected MCPTT group [" + (String)Keys.get(0) + "] " + "has been affiliated before.";
    } else if (status.equals("5")) {
      result = "The selected MCPTT group [" + (String)Keys.get(0) + "] " + "has not been affiliated yet.";
      result = "<" + sipUserName + "> " + "has been de-affiliated with the MCPTT group" + " [" + (String)Keys.get(0) + "] " + ".";
    } else if (status.equals("6")) {
      result = "The selected MCPTT group [" + (String)Keys.get(0) + "] " + "dose not exist.";
    } else if (status.equals("7")) {
      result = "The number of MCPTT group [" + (String)Keys.get(0) + "] " + "members cannot be less than two.";
    } 
    if (EventHeader.equals("affiliation")) {
      if (status.equals("3") || status.equals("4") || status.equals("6"))
        JOptionPane.showMessageDialog(null, result, "Warning: <" + sipUserName + "> ", 2); 
      
    } else {
      clientUI.Deaff_Refresh.doClick();
      if (status.equals("5") || status.equals("6") || status.equals("7"))
        JOptionPane.showMessageDialog(null, result, "Warning: <" + sipUserName + "> ", 2); 
    } 
  }
  
  public void processResponse(ResponseEvent responseEvent) {
    ClientTransaction tid = null;
    if (responseEvent.getClientTransaction() != null) {
      tid = responseEvent.getClientTransaction();
    } else {
      tid = null;
    } 
    ResponseObject responseObj = ResponseObject.parse(responseEvent.getResponse());
    if (responseObj.getStatusCode() == 200 || responseObj.getStatusCode() == 202) {
    	try {
			on2xxResponse(responseObj, responseEvent);
		} catch (InternalErrorException e) {
			// 
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// 
			e.printStackTrace();
		} catch (ParseException e) {
			// 
			e.printStackTrace();
		}
    } else if (responseObj.getStatusCode() == 180) {
      onRingingResponse(responseObj);
    } else if (responseObj.getStatusCode() == 100) {
      onTryingResponse(responseObj);
    } else if (responseObj.getStatusCode() == 404) {
      on404Response(responseObj, responseEvent);
    } else if (responseObj.getStatusCode() == 412) {
      on412Response(responseObj, responseEvent);
    } else if (responseObj.getStatusCode() == 401 || 
      responseObj.getStatusCode() == 407) {
      on401or407Response(responseObj, tid);
    } else if (responseObj.getStatusCode() == 487) {
      on487Response(responseObj, responseEvent);
    } else if (responseObj.getStatusCode() == 480) {
      on480Response(responseObj, responseEvent);
    } else if (responseObj.getStatusCode() == 403) {
      on403Response(responseObj, responseEvent);
    } else if (responseObj.getStatusCode() == 486) {
      on486Response(responseObj, responseEvent);
    } else if (responseObj.getStatusCode() == 503) {
      on503Response(responseObj, responseEvent);
    } else if (responseObj.getStatusCode() == 481) {
      on481Response(responseObj, responseEvent);
    } 
  }
  
  private void showResponseInfo(Response response) {
    ListIterator<String> headerNames = response.getHeaderNames();
    while (headerNames.hasNext())
//      String str = headerNames.next(); 
    	;
  }
  
  private void on503Response(ResponseObject responseObj, ResponseEvent responseEvent) {
    String domain = responseObj.getCallIdString();
    domain = domain.substring(domain.indexOf("@") + 1, domain.length());
    if (domain.equals(this.EMR_parameters.LMRLocalIp) || domain.equals(this.EMR_parameters.EMRIp)) {
      SIPDialog sipDialog = (SIPDialog)responseEvent.getDialog();
      sendEmrACK(responseObj, (Dialog)sipDialog);
      String callId = responseObj.getSipResponse().getCallId().getCallId();
      MediaSipSession emrmediaSipSession = EMRmediaSipSessionMap.get(callId);
      if (emrmediaSipSession != null)
        endEMRSession(emrmediaSipSession); 
    } else {
      clientUI.Log_Out.setVisible(false);
      setisRegister(false);
      clientUI.sipRegisterSender.stopSend();
      JOptionPane.showMessageDialog(null, "<" + sipUserName + "> " + "is loggining at other device. Please checking it out.", "Error: <" + sipUserName + "> ", 0);
    } 
  }
  
  private void on481Response(ResponseObject responseObj, ResponseEvent responseEvent) {
    SIPDialog sipDialog = (SIPDialog)responseEvent.getDialog();
    sendEmrACK(responseObj, (Dialog)sipDialog);
    String callId = responseObj.getSipResponse().getCallId().getCallId();
    MediaSipSession emrmediaSipSession = EMRmediaSipSessionMap.get(callId);
    if (emrmediaSipSession != null)
      endEMRSession(emrmediaSipSession); 
  }
  
  private void on486Response(ResponseObject responseObj, ResponseEvent responseEvent) {
    SIPDialog sipDialog = (SIPDialog)responseEvent.getDialog();
    sendACK(responseObj, (Dialog)sipDialog);
    this.inviteTransaction = null;
    String callId = responseObj.getSipResponse().getCallId().getCallId();
    MediaSipSession mediaSipSession = mediaSipSessionMap.get(callId);
    if (mediaSipSession != null)
      endSession(mediaSipSession); 
    clientUI.Session_Refresh.doClick();
  }
  
  private void on403Response(ResponseObject responseObj, ResponseEvent responseEvent) {
    SIPDialog sipDialog = (SIPDialog)responseEvent.getDialog();
    sendACK(responseObj, (Dialog)sipDialog);
    this.inviteTransaction = null;
    String callId = responseObj.getSipResponse().getCallId().getCallId();
    MediaSipSession mediaSipSession = mediaSipSessionMap.get(callId);
    if (mediaSipSession != null)
      endSession(mediaSipSession); 
    clientUI.Session_Refresh.doClick();
    String reasonPhase = null;
    reasonPhase = responseEvent.getResponse().getReasonPhrase();
  }
  
  private void on480Response(ResponseObject responseObj, ResponseEvent responseEvent) {
    SIPDialog sipDialog = (SIPDialog)responseEvent.getDialog();
    sendACK(responseObj, (Dialog)sipDialog);
    this.inviteTransaction = null;
    String callId = responseObj.getSipResponse().getCallId().getCallId();
    MediaSipSession mediaSipSession = mediaSipSessionMap.get(callId);
    if (mediaSipSession != null)
      endSession(mediaSipSession); 
    clientUI.Session_Refresh.doClick();
  }
  
  private void on487Response(ResponseObject responseObj, ResponseEvent responseEvent) {
    SIPDialog sipDialog = (SIPDialog)responseEvent.getDialog();
    sendACK(responseObj, (Dialog)sipDialog);
    this.inviteTransaction = null;
    String callId = responseObj.getSipResponse().getCallId().getCallId();
    MediaSipSession mediaSipSession = mediaSipSessionMap.get(callId);
    if (mediaSipSession != null)
      endSession(mediaSipSession); 
    clientUI.Session_Refresh.doClick();
  }
  
  private void on401or407Response(ResponseObject responseObj, ClientTransaction tid) {
    ViaHeader viaHeader = responseObj.getSipResponse().getTopmostViaHeader();
    String viaReceivedRPort = String.valueOf(viaHeader.getReceived()) + ":" + viaHeader.getRPort();
    if (registerContactAddress.equals(viaReceivedRPort)) {
      this.ErrorAccount++;
      if (this.ErrorAccount >= 3) {
        clientUI.Log_Out.setVisible(false);
        setisRegister(false);
        clientUI.sipRegisterSender.stopSend();
        JOptionPane.showMessageDialog(null, "MCPTT user name is not correct. Please check the config.txt.", "Error: " + sipUserName, 0);
      } else {
        doAuthentication(responseObj, tid);
      } 
    } else {
      registerContactAddress = viaReceivedRPort;
      localIp = registerContactAddress.split(":")[0];
      localPort = Integer.parseInt(registerContactAddress.split(":")[1]);
      sendRegister(SipRegisterSender.getExpires());
    } 
  }
  
  private void doAuthentication(ResponseObject responseObj, ClientTransaction tid) {
    AuthenticationHelper authenticationHelper = ((SipStackExt)sipStack).getAuthenticationHelper(accountManager, 
        headerFactory);
    try {
      if (tid != null) {
        ClientTransaction inviteTid = authenticationHelper.handleChallenge((Response)responseObj.getSipResponse(), tid, sipProvider, 0, true);
        inviteTid.getRequest();
        inviteTid.sendRequest();
      } 
    } catch (SipException e) {
      e.printStackTrace();
    } 
  }
  
  private void on412Response(ResponseObject responseObj, ResponseEvent responseEvent) {
    String cSeqMethod = responseObj.getCSeq().getMethod();
    if (cSeqMethod.equals("PUBLISH")) {
      sipPublishSender.setETag((String)null);
    } else if (cSeqMethod.equals("INVITE")) {
      getEmergencyState();
    } 
  }
  
  private void on404Response(ResponseObject responseObj, ResponseEvent responseEvent) {
    SIPDialog sipDialog = (SIPDialog)responseEvent.getDialog();
    sendACK(responseObj, (Dialog)sipDialog);
    this.inviteTransaction = null;
    String callId = responseObj.getSipResponse().getCallId().getCallId();
    MediaSipSession mediaSipSession = mediaSipSessionMap.get(callId);
    if (mediaSipSession != null)
      endSession(mediaSipSession); 
    clientUI.Session_Refresh.doClick();
  }
  
  private void onTryingResponse(ResponseObject responseObj) {}
  
  private void onRingingResponse(ResponseObject responseObj) {}
  
  private void on2xxResponse(ResponseObject responseObj, ResponseEvent responseEvent) throws InternalErrorException, UnknownHostException, ParseException {
    String cSeqMethod = responseObj.getCSeq().getMethod();
    if (cSeqMethod.equals("REGISTER")) {
      onRegisterOK(responseObj);
    } else if (cSeqMethod.equals("INVITE")) {
      onInviteOK(responseObj, responseEvent);
    } else if (cSeqMethod.equals("MESSAGE")) {
    	  System.out.println("202");
    } else if (!cSeqMethod.equals("SUBSCRIBE")) {
      if (cSeqMethod.equals("PUBLISH")) {
    	  System.out.println("publish11");
        onPublishOK(responseObj);
      } else if (!cSeqMethod.equals("CANCEL")) {
        if (!cSeqMethod.equals("MESSAGE"))
          if (cSeqMethod.equals("BYE")) {
            onByeok(responseObj);
          } else if (!cSeqMethod.equals("INFO")) {
            cSeqMethod.equals("REFER");
          }  
      } 
    } 
  }
  
  private void onByeok(ResponseObject responseObj) {
    String CallID = responseObj.getCallIdString();
    String domain = CallID.substring(CallID.indexOf("@") + 1, CallID.length());
    if (!domain.equals(this.EMR_parameters.LMRLocalIp) && !domain.equals(this.EMR_parameters.EMRIp))
      clientUI.Session_Refresh.doClick(); 
  }
  
  private void onRegisterOK(ResponseObject responseObj) {
	  setisRegister(true);//??
    System.gc();
    this.ErrorAccount = 0;
    if (getisRegister()) {
      this.registerCount++;
      if (this.registerCount == 1) {
        sipPublishSender.start();
        System.out.println("[SipClient][onRegisterOK()]");
      } 
      setisRegister(true);
      clientUI.Log_Out.setVisible(true);
    } else {
      setisRegister(false);
    } 
  }
  
  public void publishsender() {
    sipPublishSender = new SipPublishSender(getInstance(), "open", 120);
    sipPublishSender.setName("publishSender");
  }
  
  public void stoppublishsender() {
    sipPublishSender.stopSend();
  }
  
  public void sendPresencePublish(String onlineStatus, int expires, String eTag) {
    try {
      CallIdHeader callIdHeader;
      SipProvider sipProvider = SipClient.sipProvider;
      if (publishCallId == null) {
        callIdHeader = sipProvider.getNewCallId();
        publishCallId = callIdHeader.getCallId();
      } else {
        callIdHeader = headerFactory.createCallIdHeader(publishCallId);
      } 
      SipUri sipUri = SipMessageUtils.createSipUri(sipUserName, RemoteIp, RemotePort, null);
      publishCSeq++;
      CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(publishCSeq, "PUBLISH");
      Address fromAddress = addressFactory.createAddress("sip:" + sipUserName + "@" + RemoteIp);
      FromHeader fromHeader = headerFactory.createFromHeader(fromAddress, myTag);
      ToHeader toHeader = headerFactory.createToHeader(fromAddress, null);
      ArrayList<ViaHeader> viaList = createViaHeader();
      MaxForwardsHeader maxForwardsHeader = headerFactory.createMaxForwardsHeader(70);
      Address localAddress = createContactAddress();
      ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader("application", "pidf+xml");
      Pidf pidf = new Pidf();
      pidf.setTupleId(sipUserName);
      pidf.setNote("My PIDF");
      pidf.setStatus(onlineStatus);
      pidf.setContactPriority("0.8");
      pidf.setEntityName(String.valueOf(sipUserName) + "@" + RemoteIp);
      pidf.setContact(localAddress.toString());
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date curDate = new Date(System.currentTimeMillis());
      String dateTime = formatter.format(curDate);
      pidf.setTimeStamp(dateTime);
      pidf.addXmlns("urn:ietf:params:xml:ns:pidf");
      PIDFXmlBuilder pidfXmlBuilder = new PIDFXmlBuilder(pidf, "UTF-8");
      pidfXmlBuilder.setDebugMode(true);
      InputStream inputStream = new ByteArrayInputStream(pidfXmlBuilder.build().toByteArray());
      byte[] xmlRaw = inputStreamToBytes(inputStream);
      Request request = messageFactory.createRequest((URI)sipUri, "PUBLISH", callIdHeader, cSeqHeader, 
          fromHeader, toHeader, viaList, maxForwardsHeader, contentTypeHeader, xmlRaw);
      ContactHeader contactHeader = headerFactory.createContactHeader(localAddress);
      request.setHeader((Header)contactHeader);
      EventHeader eventHeader = headerFactory.createEventHeader("presence");
      request.setHeader((Header)eventHeader);
      ExpiresHeader expiresHeader = headerFactory.createExpiresHeader(expires);
      request.setHeader((Header)expiresHeader);
      if (eTag != null) {
        SIPIfMatchHeader sipIfMatchHeader = headerFactory.createSIPIfMatchHeader(eTag);
        request.setHeader((Header)sipIfMatchHeader);
      } 
      ClientTransaction clientTransaction = sipProvider.getNewClientTransaction(request);
      clientTransaction.sendRequest();
    } catch (ParseException e) {
      e.printStackTrace();
    } catch (InvalidArgumentException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (TransactionUnavailableException e) {
      e.printStackTrace();
    } catch (SipException e) {
      e.printStackTrace();
    } 
  }
  
  private byte[] inputStreamToBytes(InputStream inputStream) throws IOException {
    byte[] data = new byte[16384];
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    int nRead;
    while ((nRead = inputStream.read(data, 0, data.length)) != -1)
      buffer.write(data, 0, nRead); 
    buffer.flush();
    return buffer.toByteArray();
  }
  
  private void onPublishOK(ResponseObject responseObj) {
    String callid = responseObj.getSipResponse().getCallId().getCallId();
    if (callid.equals(publishCallId))
      if (getisRegister()) {
    	  System.out.println("11");
        String et = responseObj.getSIPETagString();
        sipPublishSender.setETag(et);
      } else {
        sendRegister(0);
        System.out.println("121");
      }  
  }
  
  private void HTTP_fd(File file) {
	  try {
		  String url = "http://"+RemoteIp+":8080/restcomm/?cmd=FDfileUpload";
          String host = RemoteIp;
          String mcdataId = "originating-user-id";
          String groupId = "group-id";

          // false = 一對一傳輸, true = 群組傳輸
          boolean isGroup = false;

          String response = uploadFile(url, file, host, isGroup, mcdataId, groupId);
          System.out.println("[FDhttpupload]"+response);


      } catch (Exception e) {
          e.printStackTrace();
      }
  }
  
  private void HTTP_fd_download(String fileurl, String filename) {
	  try {
          // 2. 測試下載文件
//          String downloadResponse = downloadFile("http://"+RemoteIp+":8080/restcomm/?cmd=GetFDFile&filePath=/home/mcpttserver/Desktop/FDfile/uploaded_file.txt", "downloaded_test.txt");
          String downloadResponse = downloadFile("http://"+RemoteIp+":8080/restcomm/?cmd=GetFDFile&filePath="+fileurl, filename);
          System.out.println("Download Response: " + downloadResponse);
      } catch (Exception e) {
          e.printStackTrace();
      }
  }
  
  public static String uploadFile(String urlStr, File file, String host, boolean isGroup, String mcdataId, String groupId) throws IOException {

      String boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW";
      HttpURLConnection connection = (HttpURLConnection) new URL(urlStr).openConnection();
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Host", host);
      connection.setRequestProperty("Content-Type", "multipart/mixed; boundary=" + boundary);
//      connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
      connection.setDoOutput(true);

      try (OutputStream outputStream = connection.getOutputStream();
           FileInputStream fileInputStream = new FileInputStream(file)) {

          // 插入 MCData info XML
          String mcDataInfoXml = generateMcDataInfo(isGroup, mcdataId, groupId);
          String xmlPart = "--" + boundary + "\r\n"
                  + "Content-Type: application/vnd.3gpp.mcdata-info+xml\r\n\r\n"
                  + mcDataInfoXml + "\r\n";
//          outputStream.write(xmlPart.getBytes());

          // 插入文件內容
          String fileHeader = "--" + boundary + "\r\n"
                  + "Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"\r\n"
                  + "Content-Type: application/octet-stream\r\n"
//					+ "Content-Type: application/form-data\r\n"
                  + "Content-Length: " + file.length() + "\r\n\r\n";
			outputStream.write(fileHeader.getBytes());

			
			outputStream.write(xmlPart.getBytes());

          byte[] buffer = new byte[1024];
          int bytesRead;
          while ((bytesRead = fileInputStream.read(buffer)) != -1) {
              outputStream.write(buffer, 0, bytesRead);
          }

          outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
          outputStream.flush();
      }

      return readResponse(connection);
  }

  public static String uploadFileWithExternalBody(String urlStr, String fileUrl, String host, boolean isGroup, String mcdataId, String groupId) throws IOException {
	    String boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW";
	    HttpURLConnection connection = (HttpURLConnection) new URL(urlStr).openConnection();
	    connection.setRequestMethod("POST");
	    connection.setRequestProperty("Host", host);
	    connection.setRequestProperty("Content-Type", "multipart/mixed; boundary=" + boundary);
	    connection.setDoOutput(true);

	    try (OutputStream outputStream = connection.getOutputStream()) {

	        // 插入 MCData info XML
	        String mcDataInfoXml = generateMcDataInfo(isGroup, mcdataId, groupId);
	        String xmlPart = "--" + boundary + "\r\n"
	                + "Content-Type: application/vnd.3gpp.mcdata-info+xml\r\n\r\n"
	                + mcDataInfoXml + "\r\n";
	        outputStream.write(xmlPart.getBytes());

	        // 插入 `message/external-body`，让服务器自己下载文件
	        String externalBodyPart = "--" + boundary + "\r\n"
	                + "Content-Type: message/external-body; access-type=\"URL\"; URL=\"" + fileUrl + "\"\r\n\r\n";
	        outputStream.write(externalBodyPart.getBytes());

	        outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
	        outputStream.flush();
	    }

	    return readResponse(connection);
	}

  
  private static String generateMcDataInfo(boolean isGroup, String mcdataId, String groupId) {
      if (isGroup) {
          return "<mcdata-info>\n" +
                  "    <request-type>group-fd</request-type>\n" +
                  "    <mcdata-request-uri>" + groupId + "</mcdata-request-uri>\n" +
                  "    <mcdata-calling-user-id>" + mcdataId + "</mcdata-calling-user-id>\n" +
                  "</mcdata-info>";
      } else {
          return "<mcdata-info>\n" +
                  "    <request-type>one-to-one-fd</request-type>\n" +
                  "    <mcdata-calling-user-id>" + mcdataId + "</mcdata-calling-user-id>\n" +
                  "</mcdata-info>";
      }
  }

  private static String readResponse(HttpURLConnection connection) throws IOException {
      int responseCode = connection.getResponseCode();
      InputStream inputStream = (responseCode >= 200 && responseCode < 300) ?
              connection.getInputStream() : connection.getErrorStream();

      try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
          StringBuilder response = new StringBuilder();
          String line;
          while ((line = reader.readLine()) != null) {
              response.append(line).append("\n");
          }
          return "Response Code: " + responseCode + "\n" + response.toString();
      }
  }

  // 下載文件 (GET)
  public static String downloadFile(String urlStr, String savePath) throws IOException {
      HttpURLConnection connection = (HttpURLConnection) new URL(urlStr).openConnection();
      connection.setRequestMethod("GET");

      if (connection.getResponseCode() == 404) {
          return "File not found on server.";
      }

      try (InputStream inputStream = connection.getInputStream();
           FileOutputStream fileOutputStream = new FileOutputStream(savePath)) {
          
          byte[] buffer = new byte[1024];
          int bytesRead;
          while ((bytesRead = inputStream.read(buffer)) != -1) {
              fileOutputStream.write(buffer, 0, bytesRead);
          }
      }

      return "File downloaded successfully: " + savePath;
  }


  
  private void onInviteOK(ResponseObject responseObj, ResponseEvent responseEvent) throws InternalErrorException, UnknownHostException, ParseException {
	 
	  System.out.println("oninviteok");
  		try {
			Response response = responseEvent.getResponse();
			ToHeader toHeader = (ToHeader) response.getHeader(ToHeader.NAME);
			String[] parts = toHeader.toString().split(":|@");
			
			String xmlString = response.toString();
			try_parser get1 = new try_parser();
			System.out.println("/////////////////");
			System.out.println(xmlString);
			String sdp = get1.string_try_mcdata_sdp(xmlString);
//			String sdp2 = get1.string_try_mcdata_sdp2(xmlString);
			System.out.println("/////////////////");
//			System.out.println(sdp2);
			java.net.URI remoteUri = new java.net.URI(sdp);
//			java.net.URI remoteUri2 = new java.net.URI(sdp2);
	    	ArrayList<java.net.URI> toList = new ArrayList<java.net.URI>();
//	    	System.out.println("receive2uri::"+remoteUri);
	    	toList.add(remoteUri);
	    	CallIdHeader callIdHeader = (CallIdHeader) response.getHeader(CallIdHeader.NAME);
	    	String callId = callIdHeader.getCallId();
	    	Session activeSession = sessionmsrpMap2.get(callId);
	    	
	    	System.out.println("---"+callId+"****"+activeSession);
	    	try {
				activeSession.setToPath(toList);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	
	    	String message = sessionmsrpmessageMap.get(callId);
	    	Message sendMsg = activeSession.sendMessage("text/plain", (message+","+callId+"\n").getBytes());
			/////////////////////
			
//	    	String xmlString = responseObj.getContentString();
//	  	      try_parser get1 = new try_parser();
//	  	      String sdp = get1.string_try_mcdata_sdp(xmlString);
	  	      InetAddress myAddress = InetAddress.getByName("192.168.0.35");
//	  	      System.out.println("///+"+myAddress);
//	  	      System.out.println("sdp///+"+sdp);
//	  	      
//	  	      String[] parts1 = sdp.split(":");
//		      String[] part2s1 = parts1[2].split("/");
//	  	      System.out.println("提取的端口号是: " + part2s1[0]);
//	  	      
//	  	      long sessionId = System.currentTimeMillis();
//	  	      Session passiveSession;
//	  		
//	  	      String sdp1 = get1.string_try_mcdata_info_groupuri(xmlString);
	  	    Session passiveSession;
	  	    passiveSession = Session.create(false, false,new java.net.URI(sdp), myAddress);
	  	    MSRP msrp = new MSRP("group");
	  	    passiveSession.setListener(msrp); 
	  	    java.net.URI javaNetUri = passiveSession.getURI();
	        String msrpuri1 =javaNetUri.toString();
//	    	
	    	///////////////////
	    	
			ClientTransaction clientTransaction = responseEvent.getClientTransaction();
	        Dialog dialog = clientTransaction.getDialog();
			SIPRequest ack = (SIPRequest)dialog.createAck(responseObj.getCSeq().getSeqNumber());
			ack.setRequestURI((URI)SipMessageUtils.createSipUri(parts[2], RemoteIp, RemotePort, null));
			
			dialog.sendAck((Request)ack);
			System.out.println(ack.toString());
			try {
			    // 创建 BYE 请求
			    Request byeRequest = dialog.createRequest(Request.BYE);

			    // 创建一个新的 ClientTransaction 用于 BYE 请求
			    ClientTransaction byeTransaction = sipProvider.getNewClientTransaction(byeRequest);

			    // 发送 BYE 请求
			    dialog.sendRequest(byeTransaction);
			    
			    System.out.println("BYE request sent successfully.");
			} catch (Exception e) {
			    e.printStackTrace();
			    System.out.println("Failed to send BYE request.");
			}

	    } catch (InvalidArgumentException e) {
	    	e.printStackTrace();
	    } catch (SipException e) {
	    	e.printStackTrace();
	    } 
//  	InetAddress myAddress = InetAddress.getByName("myHostnameThatDoesMsrp");
//  	Session activeSession = Session.create(false, false, myAddress);
//  	SipURI  myMsrpUri = (SipURI) activeSession.getURI();
//      getInstance();
//      String domain = responseObj.getCallIdString();
//      domain = domain.substring(domain.indexOf("@") + 1, domain.length());
//      try {
//        if (domain.equals(this.EMR_parameters.LMRLocalIp)) {
//          String CallID = responseObj.getCallIdString();
//          MediaSipSession EMRmediaSipSession = EMRmediaSipSessionMap.get(CallID);
//          if (EMRmediaSipSession == null)
//            return; 
//          if (EMRmediaSipSession.isActive())
//            return; 
//          SIPDialog sipDialog = (SIPDialog)responseEvent.getDialog();
//          getEMRSDPInfo(new String(responseObj.getRawContent()));
//          sendEmrACK(responseObj, (Dialog)sipDialog);
//          dtmfSender dtmf1sender = new dtmfSender(1, EMRSdpInfo.getIp(), EMRSdpInfo.getPort());
//          dtmf1sender.setName("dtmf 1 senfer");
//          dtmf1sender.createDataSocket(this.EMR_parameters.LMRLocalIp, 22001);
//          dtmf1sender.start();
//          try {
//            Thread.sleep(160L);
//          } catch (InterruptedException e) {
//            e.printStackTrace();
//          } 
//          dtmf1sender.stopSend();
//          proxy.getRTCPProxy().getRTCPThread().getHandler().setTwoModeHandler(0, mySdpInfo.getIp(), mySdpInfo.getPort(), 
//              this.EMR_parameters.MCPTTUserlocalIp, getRtpPort(), this.EMR_parameters.LMRLocalIp, 12001, EMRSdpInfo.getIp(), EMRSdpInfo.getPort());
//        } else if (responseObj.getEventHeader() != null) {
//          String EventHeader = responseObj.getEventHeader();
//          EventHeader = EventHeader.replaceAll("\\s+", "");
//          if (EventHeader.equals("MEGC")) {
//            MediaSipSession mediaSipSession = mediaSipSessionMap.get(responseObj.getCallIdString());
//            if (mediaSipSession == null)
//              return; 
//            SIPDialog sipDialog = (SIPDialog)mediaSipSession.getDialog();
//            sendACK(responseObj, (Dialog)sipDialog);
//          } 
//        } else {
//          getSDPInfo(new String(responseObj.getRawContent()));
//          Map<String, MediaSipSession> sessionMap = mediaSipSessionMap;
//          MediaSipSession mediaSipSession = mediaSipSessionMap.get(responseObj.getCallIdString());
//          if (mediaSipSession == null)
//            return; 
//          if (mediaSipSession.isActive())
//            return; 
//          String GroupURI = mediaSipSession.getPeerInfo().getPeerSipUri().toString().split("sip:")[1];
//          this.GroupName = GroupURI.split("@")[0];
//          SIPDialog sipDialog = (SIPDialog)responseEvent.getDialog();
//          sendACK(responseObj, (Dialog)sipDialog);
//          setPeerInfoFromResponse(responseObj, mediaSipSession);
//          activeMediaSipSession(mediaSipSession);
//          String GroupName = getTarget(MediaSipSession.getSipTarget()).split("@")[0];
//          String ServerIP = getTarget(MediaSipSession.getSipTarget()).split("@")[1];
//          try {
//            Thread.sleep(200L);
//          } catch (InterruptedException e1) {
//            e1.printStackTrace();
//          } 
//          sendReferPRG(GroupName, ServerIP);
//        } 
//      } catch (Exception e) {
//        e.printStackTrace();
//      } 
		catch (URISyntaxException e) {
					e.printStackTrace();
		}
    }
  
  private String getTarget(String target) {
    return target.split("sip:")[1];
  }
  
  private void sendInfo(Dialog dialog, String contentType, String contentText) {
    try {
      SIPRequest request = (SIPRequest)dialog.createRequest("INFO");
      ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader("application", contentType);
      request.setContent(contentText.getBytes(), contentTypeHeader);
      request.getTopmostViaHeader().setRPort();
      SIPClientTransactionImpl sipClientTransaction = (SIPClientTransactionImpl)EMRProvider.getNewClientTransaction((Request)request);
      dialog.sendRequest((ClientTransaction)sipClientTransaction);
    } catch (ParseException e) {
      e.printStackTrace();
    } catch (SipException e) {
      e.printStackTrace();
    } catch (InvalidArgumentException e) {
      e.printStackTrace();
    } 
  }
  
  private MySdpInfo getSDPInfo(String sessionDescription) throws Exception {
    String[] lines = sessionDescription.trim().replaceAll(" +", " ").split("[\r\n]+");
    String mediaType = "";
    String peerIP = "n/a";
    int peerPort = -1;
    int peerRtcpPort = -1;
    byte b;
    int i;
    String[] arrayOfString1;
    for (i = (arrayOfString1 = lines).length, b = 0; b < i; ) {
      String line = arrayOfString1[b];
      if (line.charAt(0) == 'c') {
        String linec = line;
        List<String> matched = getMatched(linec, "IP[0-9]* [0-9.]*");
        if (matched.size() == 1) {
          peerIP = ((String)matched.get(0)).replace("IP4", "").replace("IP6", "").trim();
        } else {
          throw new Exception("content/IP error!!");
        } 
      } else if (line.charAt(0) == 'm') {
        String linem = line;
        List<String> matched = getMatched(linem, "(m=audio [0-9]* RTP/AVP)|(m=video [0-9]* RTP/AVP)");
        if (matched.size() == 1) {
          mediaType = getMatched(matched.get(0), "audio|video").get(0);
          peerPort = Integer.parseInt(((String)matched.get(0)).replace("m=audio", "").replace("m=video", "").replace("RTP/AVP", "").trim());
        } 
      } else if (line.charAt(0) == 'a') {
        String lineRtcp = line;
        List<String> matched = getMatched(lineRtcp, "a=rtcp:[0-9]*");
        if (matched.size() == 1)
          peerRtcpPort = Integer.parseInt(((String)matched.get(0)).replace("a=rtcp:", "").trim()); 
      } 
      b++;
    } 
    mySdpInfo = new MySdpInfo();
    mySdpInfo.setIp(peerIP);
    mySdpInfo.setPort(peerPort);
    mySdpInfo.setRtcpPort(peerRtcpPort);
    mySdpInfo.setMediaType(mediaType);
    return mySdpInfo;
  }
  
  private MySdpInfo getEMRSDPInfo(String sessionDescription) throws Exception {
    String[] lines = sessionDescription.trim().replaceAll(" +", " ").split("[\r\n]+");
    String mediaType = "";
    String peerIP = "n/a";
    int peerPort = -1;
    int peerRtcpPort = -1;
    byte b;
    int i;
    String[] arrayOfString1;
    for (i = (arrayOfString1 = lines).length, b = 0; b < i; ) {
      String line = arrayOfString1[b];
      if (line.charAt(0) == 'c') {
        String linec = line;
        List<String> matched = getMatched(linec, "IP[0-9]* [0-9.]*");
        if (matched.size() == 1) {
          peerIP = ((String)matched.get(0)).replace("IP4", "").replace("IP6", "").trim();
        } else {
          throw new Exception("content/IP error!!");
        } 
      } else if (line.charAt(0) == 'm') {
        String linem = line;
        List<String> matched = getMatched(linem, "(m=audio [0-9]* RTP/AVP)|(m=video [0-9]* RTP/AVP)");
        if (matched.size() == 1) {
          mediaType = getMatched(matched.get(0), "audio|video").get(0);
          peerPort = Integer.parseInt(((String)matched.get(0)).replace("m=audio", "").replace("m=video", "").replace("RTP/AVP", "").trim());
        } 
      } else if (line.charAt(0) == 'a') {
        String lineRtcp = line;
        List<String> matched = getMatched(lineRtcp, "a=rtcp:[0-9]*");
        if (matched.size() == 1)
          peerRtcpPort = Integer.parseInt(((String)matched.get(0)).replace("a=rtcp:", "").trim()); 
      } 
      b++;
    } 
    EMRSdpInfo = new MySdpInfo();
    EMRSdpInfo.setIp(peerIP);
    EMRSdpInfo.setPort(peerPort);
    EMRSdpInfo.setRtcpPort(peerRtcpPort);
    EMRSdpInfo.setMediaType(mediaType);
    return EMRSdpInfo;
  }
  
  private void setPeerInfoFromResponse(ResponseObject responseObj, MediaSipSession mediaSipSession) throws Exception {
    MySdpInfo mySdpInfo = getSDPInfo(new String(responseObj.getRawContent()));
    PeerInfo peerInfo = mediaSipSession.getPeerInfo();
    peerInfo.setSDPInfo(mySdpInfo);
    peerInfo.setPeerUserName(responseObj.getToHeaderUserName());
    peerInfo.setPeerTag(responseObj.getToTag());
  }
  
  private void activeMediaSipSession(MediaSipSession mediaSipSession) {
    try {
      PeerInfo peerInfo = mediaSipSession.getPeerInfo();
      if (!peerInfo.getPeerIp().equals("n/a") && peerInfo.getPeerRtpPort() != -1) {
        String peerIp = peerInfo.getPeerIp();
        int peerPort = peerInfo.getPeerRtcpPort();
        mediaSipSession.getMediaSessionState().setRtpport(getRtpPort());
        mediaSipSession.getMediaSessionState().setrtpSocket(proxy.getRTCPProxy().getRTCPThread().getHandler().getRtpSocket());
        mediaSipSession.getMediaSessionState().changeState(2, "", this.EMR_parameters.MCPTTUserlocalIp);
        mediaSipSession.setIsActive(true);
      } 
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  private List<String> getMatched(String s, String regex1) {
    List<String> matchList = new ArrayList<>();
    Pattern pattern1 = Pattern.compile(regex1);
    Matcher matcher = pattern1.matcher(s);
    while (matcher.find())
      matchList.add(matcher.group()); 
    return matchList;
  }
  
  public void sendRefer(MediaSipSession mediaSipSession, String sessionType) {
    String target = MediaSipSession.getSipTarget();
    sendRefer(mediaSipSession, sessionType, target);
  }
  
  public void sendRefer(MediaSipSession mediaSipSession, String sessionType, String referToTarget) {
    try {
      SIPDialog sipDialog = (SIPDialog)mediaSipSession.getDialog();
      String target = referToTarget;
      SIPRequest sipRequest = (SIPRequest)sipDialog.createRequest("REFER");
      Address referToAddr = addressFactory.createAddress(target);
      ReferTo referTo = (ReferTo)headerFactory.createReferToHeader(referToAddr);
      referTo.setParameter("session", sessionType);
      sipRequest.setHeader((Header)referTo);
      sipRequest.getTopmostViaHeader().setRPort();
      sipRequest.getRequestLine().setUri((URI)mediaSipSession.getPeerInfo().getPeerSipUri());
      Contact c = new Contact();
      c.setAddress(createContactAddress());
      sipRequest.setHeader((Header)c);
      SIPClientTransactionImpl sipClientTransaction = (SIPClientTransactionImpl)sipProvider.getNewClientTransaction((Request)sipRequest);
      mediaSipSession.getDialog().sendRequest((ClientTransaction)sipClientTransaction);
    } catch (SipException e) {
      e.printStackTrace();
      sendReferTx(mediaSipSession, sessionType, referToTarget);
    } catch (ParseException e) {
      e.printStackTrace();
    } catch (InvalidArgumentException e) {
      e.printStackTrace();
    } 
  }
  
  public void sendReferTx(MediaSipSession mediaSipSession, String sessionType, String referToTarget) {
    try {
      String groupName = mediaSipSession.getPeerInfo().getPeerUserName();
      String peerSipDomain = mediaSipSession.getPeerInfo().getPeerSipDomain();
      SipUri sipUri = SipMessageUtils.createSipUri(groupName, peerSipDomain, RemotePort, null);
      CallIdHeader callIdHeader = sipProvider.getNewCallId();
      CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(1L, "REFER");
      Address fromAddress = addressFactory.createAddress("sip:" + 
          sipUserName + "@" + RemoteIp);
      String tag = Utils.getInstance().generateTag();
      FromHeader fromHeader = headerFactory.createFromHeader(fromAddress, tag);
      Address toAddress = addressFactory.createAddress("sip:" + 
          groupName + "@" + peerSipDomain);
      ToHeader toHeader = headerFactory.createToHeader(toAddress, null);
      Address localAddress = createContactAddress();
      ContactHeader contactHeader = headerFactory.createContactHeader(localAddress);
      List<ViaHeader> viaList = createInviteViaHeader();
      MaxForwardsHeader maxForwardsHeader = headerFactory.createMaxForwardsHeader(70);
      SIPRequest sipRequest = (SIPRequest)messageFactory.createRequest((URI)sipUri, "REFER", callIdHeader, cSeqHeader, fromHeader, toHeader, viaList, maxForwardsHeader);
      sipRequest.setHeader((Header)contactHeader);
      Address referToAddr = addressFactory.createAddress(referToTarget);
      ReferTo referTo = (ReferTo)headerFactory.createReferToHeader(referToAddr);
      referTo.setParameter("session", sessionType);
      sipRequest.setHeader((Header)referTo);
      sendTxRequest(sipProvider, (Request)sipRequest);
    } catch (ParseException e) {
      e.printStackTrace();
    } catch (InvalidArgumentException e) {
      e.printStackTrace();
    } 
  }
  
  private void sendACK(ResponseObject responseObject, Dialog dialog) {
    try {
      SIPRequest ack = (SIPRequest)dialog.createAck(responseObject.getCSeq().getSeqNumber());
      ack.setRequestURI((URI)SipMessageUtils.createSipUri(sipUserName, RemoteIp, RemotePort, null));
      dialog.sendAck((Request)ack);
    } catch (InvalidArgumentException e) {
      e.printStackTrace();
    } catch (SipException e) {
      e.printStackTrace();
    } 
  }
  
  private void sendEmrACK(ResponseObject responseObject, Dialog dialog) {
    try {
      SIPRequest ack = (SIPRequest)dialog.createAck(responseObject.getCSeq().getSeqNumber());
      ack.setRequestURI((URI)SipMessageUtils.createSipUri(responseObject.getToHeaderUserName(), this.EMR_parameters.EMRIp, Integer.valueOf(this.EMR_parameters.LMRLocalPort).intValue(), null));
      dialog.sendAck((Request)ack);
    } catch (InvalidArgumentException e) {
      e.printStackTrace();
    } catch (SipException e) {
      e.printStackTrace();
    } catch (NumberFormatException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  private static void sendTxRequest(final SipProvider sipProvider, final Request request) {
    Thread thread = new Thread() {
        public void run() {
          try {
            ClientTransaction clientTransaction = sipProvider.getNewClientTransaction(request);
            clientTransaction.sendRequest();
          } catch (SipException e) {
            e.printStackTrace();
          } finally {
            Thread.currentThread().interrupt();
          } 
        }
      };
    thread.start();
  }
  
  public void processTimeout(TimeoutEvent timeout) {
    Request request = timeout.getClientTransaction().getRequest();
    String requestMethod = request.getMethod();
    if (requestMethod.equals("INVITE")) {
      String[] CallID = request.getHeader("Call-ID").toString().split("Call-ID:");
      String callID = CallID[1].replaceAll("\\s+", "");
      MediaSipSession mediaSipSession = mediaSipSessionMap.get(callID);
      MediaSipSession EMRmediaSipSession = mediaSipSessionMap.get(callID);
      if (mediaSipSession != null)
        endSession(mediaSipSession); 
      if (EMRmediaSipSession != null)
        endEMRSession(EMRmediaSipSession); 
    } else if (!requestMethod.equals("REGISTER")) {
      if (!requestMethod.equals("BYE"))
        if (!requestMethod.equals("REFER"))
          requestMethod.equals("PUBLISH");  
    } 
    JOptionPane.showMessageDialog(null, "Timeout: SIP " + requestMethod + " request", "Error: <" + sipUserName + "> ", 0);
  }
  
  public void processTransactionTerminated(TransactionTerminatedEvent arg0) {}
  
  public Proxy getProxy() {
    return proxy;
  }
  
  public void sendrtcp(DatagramSocket datagramSocket) {
    int priority;
    String peerIp = mySdpInfo.getIp();
    int peerPort = mySdpInfo.getRtcpPort();
    String sessionId = String.valueOf(this.GroupName) + "@" + RemoteIp;
    RTCPSender connectSender = new RTCPSender(peerIp, peerPort, datagramSocket, 0, null);
    if (getEmergencyState() || (getGroupState() && getCallInfoValue() == 99)) {
      priority = 5;
      connectSender.setInd((short)4096);
    } else {
      priority = 4;
      connectSender.setInd((short)-32768);//??
    } 
    connectSender.set(mySipUri, sessionId, priority);
    connectSender.setName("[SipClient][sendrtcp()] RTCPSender floor request");
    connectSender.start();
  }
  
  public void FloorRequest(DatagramSocket datagramSocket) {
    int priority;
    String peerIp = mySdpInfo.getIp();
    int peerPort = mySdpInfo.getRtcpPort();
    String sessionId = String.valueOf(this.GroupName) + "@" + RemoteIp;
    RTCPSender Sender = new RTCPSender(peerIp, peerPort, datagramSocket, 0, null);
    if (getEmergencyState() || (getGroupState() && getCallInfoValue() == 99)) {
      priority = 5;
      Sender.setInd((short)4096);
    } else {
      priority = 4;
      Sender.setInd((short)-32768);//??
    } 
    Sender.set(mySipUri, sessionId, priority);
    Sender.setName("[SipClient][FloorRequest()] RTCPSender floor requset");
    Sender.start();
  }
  
  public void FloorRelease(DatagramSocket datagramSocket) {
    int priority;
    String peerIp = mySdpInfo.getIp();
    int peerPort = mySdpInfo.getRtcpPort();
    String sessionId = String.valueOf(this.GroupName) + "@" + RemoteIp;
    RTCPSender Sender = new RTCPSender(peerIp, peerPort, datagramSocket, 4, null);
    if (getEmergencyState() || (getGroupState() && getCallInfoValue() == 99)) {
      priority = 4;
      Sender.setInd((short)4096);
    } else {
      priority = 3;
      Sender.setInd((short)-32768);
    } 
    Sender.set(mySipUri, sessionId, priority);
    Sender.setName("[SipClient][FloorRelease()] RTCPSender floor release");
    Sender.start();
  }
  
  public List<String> getAllGroupDisplayName(String serverIP) throws ParseException, IOException {
    this.grouplist = new getGroupList();
    this.grouplist.getGroupList(serverIP);
    return this.grouplist.getAllGroupdisplayname();
  }
  
  public HashMap<String, String> getGroupdictionary() {
    return this.grouplist.getGroupdictionary();
  }
  
  public List<String> getKeyList(HashMap<String, String> map, String value) {
    List<String> keyList = new ArrayList<>();
    for (String getKey : map.keySet()) {
      if (((String)map.get(getKey)).equals(value))
        keyList.add(getKey); 
    } 
    return keyList;
  }
  
  public void CreatGroup(String serverIP, String userURI, String groupDisplayName) {
    createGroup newGroup = new createGroup();
    newGroup.createGroup(serverIP, userURI, groupDisplayName);
  }
  
  public List<String> getAffiliatedGroup(String username, String serverip) throws IOException {
    getDirectorytInfo directinfo = new getDirectorytInfo();
    directinfo.getDirectorytInfo(username, serverip);
    return directinfo.AffiliatedGroup();
  }
  
  public boolean setEmergencyState(boolean R) {
    return this.MEGC = R;
  }
  
  public boolean getEmergencyState() {
    return this.MEGC;
  }
  
  public boolean setGroupState(boolean R) {
    return this.GroupState = R;
  }
  
  public boolean getGroupState() {
    return this.GroupState;
  }
  
  public int setCallInfoValue(int R) {
    return this.kyinfoValue = R;
  }
  
  public int getCallInfoValue() {
    return this.kyinfoValue;
  }
  
  public boolean getisRegister() {
    return this.isRegister;
  }
  
  public void setisRegister(boolean R) {
    this.isRegister = R;
  }
  
  public void sendEMRInvite(String targeturi, boolean Emergency) {
    try {
      AddressFactory addressFactory = SipClient.addressFactory;
      MessageFactory messageFactory = SipClient.messageFactory;
      HeaderFactory headerFactory = SipClient.headerFactory;
      SipProvider sipProvider = EMRProvider;
      URI inviteURI = addressFactory.createURI("sip:" + targeturi);
      long sessionId = System.currentTimeMillis();
      CallIdHeader callIdHeader = sipProvider.getNewCallId();
      this.EMRinviteCallID = callIdHeader.getCallId();
      long seq = (long)(Math.random() * 3000.0D);
      CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(seq, "INVITE");
      String LMRuseruri = String.valueOf(this.EMR_parameters.LMRUserId) + "@" + this.EMR_parameters.LMRLocalIp;
      Address fromAddress = addressFactory.createAddress("sip:" + LMRuseruri);
      Random rand = new Random();
      String tag = SipMessageUtils.generateNewTag();
      FromHeader fromHeader = headerFactory.createFromHeader(fromAddress, tag);
      Address toAddress = addressFactory.createAddress("sip:" + targeturi);
      ToHeader toHeader = headerFactory.createToHeader(toAddress, null);
      ArrayList<ViaHeader> viaList = createEMRInviteViaHeader(this.EMR_parameters.LMRLocalIp, Integer.parseInt(this.EMR_parameters.LMRLocalPort));
      MaxForwardsHeader maxForwardsHeader = headerFactory.createMaxForwardsHeader(70);
      Request request = messageFactory.createRequest(inviteURI, "INVITE", callIdHeader, cSeqHeader, 
          fromHeader, toHeader, viaList, maxForwardsHeader);
      Address localAddress = SipClient.addressFactory.createAddress("sip:" + LMRuseruri + ";transport=udp");
      ContactHeader contactHeader = headerFactory.createContactHeader(localAddress);
      request.setHeader((Header)contactHeader);
      SIPRequest sipRequest = (SIPRequest)request;
      sipRequest.setHeader((Header)createUserAgentHeader());
      Supported supported = new Supported();
      supported.setOptionTag("timer");
      sipRequest.setHeader((Header)supported);
      sipRequest.setHeader(headerFactory.createHeader("Answer-Mode", "Auto"));
      int expires = 600;
      ExpiresHeader eh = headerFactory.createExpiresHeader(expires);
      sipRequest.setHeader((Header)eh);
      CallInfoHeader callinfo = headerFactory.createCallInfoHeader(inviteURI);
      if (Emergency) {
        callinfo.setParameter("KyInfo_F", "99");
      } else {
        callinfo.setParameter("KyInfo_F", "00");
      } 
      sipRequest.addHeader((Header)callinfo);
      AllowHeader allow1 = headerFactory.createAllowHeader("INVITE");
      sipRequest.addHeader((Header)allow1);
      AllowHeader allow2 = headerFactory.createAllowHeader("ACK");
      sipRequest.addHeader((Header)allow2);
      AllowHeader allow3 = headerFactory.createAllowHeader("CANCEL");
      sipRequest.addHeader((Header)allow3);
      AllowHeader allow4 = headerFactory.createAllowHeader("OPTIONS");
      sipRequest.addHeader((Header)allow4);
      AllowHeader allow5 = headerFactory.createAllowHeader("REFER");
      sipRequest.addHeader((Header)allow5);
      AllowHeader allow6 = headerFactory.createAllowHeader("MESSAGE");
      sipRequest.addHeader((Header)allow6);
      AllowHeader allow7 = headerFactory.createAllowHeader("SUBSCRIBE");
      sipRequest.addHeader((Header)allow7);
      AllowHeader allow8 = headerFactory.createAllowHeader("INFO");
      sipRequest.addHeader((Header)allow8);
      AllowHeader allow9 = headerFactory.createAllowHeader("BYE");
      sipRequest.addHeader((Header)allow9);
      String OfferSDP = createEMRoffer(this.EMR_parameters.LMRLocalIp, 12001);
      ContentType contentType = new ContentType("application", "sdp");
      sipRequest.setContent(OfferSDP, (ContentTypeHeader)contentType);
      SIPClientTransaction transaction = (SIPClientTransaction)sipProvider.getNewClientTransaction((Request)sipRequest);
      Dialog dialog = sipProvider.getNewDialog((Transaction)transaction);
      String target = "sip:" + targeturi;
      SipUri targetSipUri = SipMessageUtils.createSipUri(targeturi.split("@")[0], targeturi.split("@")[1], -1, null);
      MediaSipSession EMRmediaSipSession = new MediaSipSession(callIdHeader.getCallId(), myTag, dialog, target, 
          targetSipUri, false);
      EMRmediaSipSession.getUserActiveMap().put(fromAddress.getURI().toString(), "in");
      EMRmediaSipSessionMap.put(MediaSipSession.getCallId(), EMRmediaSipSession);
      instance = this;
      try {
        Thread.sleep(100L);
      } catch (InterruptedException e1) {
        e1.printStackTrace();
      } 
      dialog.sendRequest((ClientTransaction)transaction);
    } catch (ParseException e) {
      e.printStackTrace();
    } catch (InvalidArgumentException e) {
      e.printStackTrace();
    } catch (Exception p) {
      p.printStackTrace();
    } 
  }
  
  public void sendEMRBye(MediaSipSession EMRmediaSipSession) {
    try {
      SIPDialog sipDialog = (SIPDialog)EMRmediaSipSession.getDialog();
      if (sipDialog == null)
        return; 
      sipDialog.setState(1);
      SIPRequest request = (SIPRequest)sipDialog.createRequest("BYE");
      request.getTopmostViaHeader().setRPort();
      SIPClientTransactionImpl sipClientTransaction = (SIPClientTransactionImpl)EMRProvider.getNewClientTransaction((Request)request);
      sipDialog.sendRequest((ClientTransaction)sipClientTransaction);
      endEMRSession(EMRmediaSipSession);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public void sendEMRInfo(String targeturi, boolean signal) {
    try {
      AddressFactory addressFactory = SipClient.addressFactory;
      MessageFactory messageFactory = SipClient.messageFactory;
      HeaderFactory headerFactory = SipClient.headerFactory;
      SipProvider sipProvider = EMRProvider;
      URI infoURI = addressFactory.createURI("sip:" + targeturi);
      CallIdHeader callIdHeader = sipProvider.getNewCallId();
      this.EMRinviteCallID = callIdHeader.getCallId();
      long seq = (long)(Math.random() * 3000.0D);
      CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(seq, "INFO");
      String LMRuseruri = String.valueOf(this.EMR_parameters.LMRUserId) + "@" + this.EMR_parameters.LMRLocalIp;
      Address fromAddress = addressFactory.createAddress("sip:" + LMRuseruri);
      Random rand = new Random();
      String tag = SipMessageUtils.generateNewTag();
      FromHeader fromHeader = headerFactory.createFromHeader(fromAddress, tag);
      Address toAddress = addressFactory.createAddress("sip:" + targeturi);
      ToHeader toHeader = headerFactory.createToHeader(toAddress, null);
      ArrayList<ViaHeader> viaList = createEMRInviteViaHeader(this.EMR_parameters.LMRLocalIp, Integer.parseInt(this.EMR_parameters.LMRLocalPort));
      MaxForwardsHeader maxForwardsHeader = headerFactory.createMaxForwardsHeader(70);
      Request request = messageFactory.createRequest(infoURI, "INFO", callIdHeader, cSeqHeader, 
          fromHeader, toHeader, viaList, maxForwardsHeader);
      Address localAddress = SipClient.addressFactory.createAddress("sip:" + LMRuseruri + ";transport=udp");
      ContactHeader contactHeader = headerFactory.createContactHeader(localAddress);
      request.setHeader((Header)contactHeader);
      SIPRequest sipRequest = (SIPRequest)request;
      sipRequest.setHeader((Header)createUserAgentHeader());
      AllowHeader allow1 = headerFactory.createAllowHeader("INVITE");
      sipRequest.addHeader((Header)allow1);
      AllowHeader allow2 = headerFactory.createAllowHeader("ACK");
      sipRequest.addHeader((Header)allow2);
      AllowHeader allow3 = headerFactory.createAllowHeader("CANCEL");
      sipRequest.addHeader((Header)allow3);
      AllowHeader allow4 = headerFactory.createAllowHeader("OPTIONS");
      sipRequest.addHeader((Header)allow4);
      AllowHeader allow5 = headerFactory.createAllowHeader("REFER");
      sipRequest.addHeader((Header)allow5);
      AllowHeader allow6 = headerFactory.createAllowHeader("MESSAGE");
      sipRequest.addHeader((Header)allow6);
      AllowHeader allow7 = headerFactory.createAllowHeader("SUBSCRIBE");
      sipRequest.addHeader((Header)allow7);
      AllowHeader allow8 = headerFactory.createAllowHeader("INFO");
      sipRequest.addHeader((Header)allow8);
      AllowHeader allow9 = headerFactory.createAllowHeader("BYE");
      sipRequest.addHeader((Header)allow9);
      createInfoContent(signal);
      ContentType contentType = new ContentType("application", "dtmf-relay");
      sipRequest.setContent(createInfoContent(signal), (ContentTypeHeader)contentType);
      SIPClientTransaction transaction = (SIPClientTransaction)sipProvider.getNewClientTransaction((Request)sipRequest);
      transaction.sendRequest();
    } catch (ParseException e) {
      e.printStackTrace();
    } catch (InvalidArgumentException e) {
      e.printStackTrace();
    } catch (Exception p) {
      p.printStackTrace();
    } 
  }
  
  private String createEMRoffer(String localIP, int streamPort) {
    long sessionId = System.currentTimeMillis();
    String content = "";
    content = String.valueOf(content) + "v=0" + "\r\n";
    content = String.valueOf(content) + "o=IWF " + Long.toString(sessionId) + " " + Long.toString(sessionId) + 
      Long.toString(System.currentTimeMillis()) + " IN IP4 " + localIP + "\r\n";
    content = String.valueOf(content) + "s=-" + "\r\n";
    content = String.valueOf(content) + "c=IN IP4 " + localIP + "\r\n";
    content = String.valueOf(content) + "t=0 0" + "\r\n";
    content = String.valueOf(content) + "m=audio " + streamPort + " " + "RTP/AVP " + "0 8 101" + "\r\n";
    content = String.valueOf(content) + "a=rtpmap:0 PCMU/8000" + "\r\n";
    content = String.valueOf(content) + "a=rtpmap:8 PCMA/8000" + "\r\n";
    content = String.valueOf(content) + "a=rtpmap:101 telephone-event/8000" + "\r\n";
    content = String.valueOf(content) + "a=fmtp:101 0-15" + "\r\n";
    content = String.valueOf(content) + "a=sendrecv" + "\r\n";
    return content;
  }
  
  private String createInfoContent(boolean signal) {
    String content = "";
    if (signal) {
      content = "signal=emer_on";
    } else {
      content = "signal=ptt_gaurd";
    } 
    return content;
  }
  
  private void endEMRSession(MediaSipSession EMRmediaSipSession) {
    this.EMRTransaction = null;
    EMRresponse = null;
    EMRmediaSipSessionMap.remove(MediaSipSession.getCallId());
    if (!EMRmediaSipSessionMap.isEmpty())
      EMRmediaSipSessionMap.clear(); 
  }
  
  public String getEMRinviteCallID() {
    return this.EMRinviteCallID;
  }
  
  public int getRtpPort() {
    return this.RtpPort;
  }
  
  public void setRtpPort(int port) {
    this.RtpPort = port;
  }
  
  public int getMsrpPort() {
	  return this.MsrpPort;
  }
	  
  public void setMsrpPort(int port) {
	  this.MsrpPort = port;
  }
  
  public boolean getPESessionE() {
    return PESessionE;
  }
  
  public boolean setPESessionE(boolean pESessionE) {
    PESessionE = pESessionE;
    return pESessionE;
  }
  
  public void receiveSipMessage(String newSipMessage) {
      //String oldSipMessage = this.sipMessage;
      //this.sipMessage = newSipMessage;
      support.firePropertyChange("sipMessage", null, newSipMessage);
  }
  
  public void addPropertyChangeListener(PropertyChangeListener listener) {
      support.addPropertyChangeListener(listener);
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
      support.removePropertyChangeListener(listener);
  }
  
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
      // 在这里处理属性更改事件
      if ("MCDataSDSMessage".equals(evt.getPropertyName())) {
          String receivedText = (String) evt.getNewValue();
//          System.out.println("接收到的消息: " + receivedText);
          support.firePropertyChange("MCDataSDSMessage", null, receivedText);
      }
  }
}
