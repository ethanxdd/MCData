package ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.SystemColor;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.swing.DropMode;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
//import org.apache.http.ParseException;
import session.MediaSipSession;
import sip.SipClient;
import sip.SipRegisterSender;
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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TooManyListenersException;
import java.util.UUID;
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
import proxy.Proxy;
import redirect.handler;
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
import xml_generator.try_mcpttinfo_factory;
import xml_generator.try_parser;
import xml_generator.try_pidf_factory;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import netmsrp.Session;
import netmsrp.exceptions.InternalErrorException;

public class clientUI implements PropertyChangeListener{
	private static JFrame frmIwfUserInterface;
	  
	  private JPanel panel1;
	  
	  private JLabel txtMcpttGroup;
	  
	  private JLabel txtMcpttGroupfd;
	  
	  private JComboBox<String> Existing_Groups;
	  
	  private JComboBox<String> Existing_Groups2;
	  
	  private JComboBox<String> Existing_Groups3;
	  
	  private JComboBox<String> Existing_Groupsfd;
	  
	  private JComboBox<String> Existing_Groupsfd2;
	  
	  private JComboBox<String> Existing_Groupsfd3;
	  
	  private JComboBox<String> ComboBox;
	  
	  private JComboBox<String> ComboBoxfd;
	  
	  private JButton Log_In;
	  
	  public static JButton Log_Out;
	  
	  private JButton Affiliate;
	  
	  private JScrollPane srollpane;
	  
	  private JScrollPane srollpane1;
	  
	  private JComboBox<String> Session_Affiliated_Groups;
	  
	  private JLabel lblNewLabel_1;
	  
	  private JButton Set_Up_Session;
	  
	  private JButton Release_Session;
	  
	  private static JTabbedPane tabbedPane;
	  
	  private static JPanel panel2;
	  
	  private static JPanel panel4;
	  
	  private static JPanel panel5;
	  
	  private static JPanel panel6;
	  
	  private static JPanel panel7;
	  
	  private static JPanel panel8;
	  
	  private static SipClient user;
	  
	  private static String ServerIP = "";
	  
	  private static String localIp = "";
	  
	  public static SipRegisterSender sipRegisterSender;
	  
	  private static String username = "";
	  
	  private static String userURI = String.valueOf(username) + "@" + ServerIP;
	  
	  private static String LMRUserID = "";
	  
	  private static String LMRLocalIP = "";
	  
	  private static String LMRLocalPort = "";
	  
	  private static String EMRCardNo = "";
	  
	  private static String EMRIP = "";
	  
	  private JTextField txtExampleGroup;
	  
	  private static String Title = "";
	  
	  public static JButton Aff_Refresh;
	  
	  public static JButton Session_Refresh;
	  
	  private static client_parameters parm;
	  
	  private JPanel panel3;
	  
	  private getGroupList grouplist;///
	  
	  private JTextArea txtrSelectAnMcptt;
	  
	  private JTextArea sendSDS;
	  
	  private JTextArea sendFDone;
	  
	  private JTextArea sendFDgroup;
	  
	  private JTextArea sendSDS1;
	  
	  private JLabel txtMcpttGroup_1;
	  
	  private JLabel txtMcpttGroup_fd1;
	  
	  private JLabel txtMcpttGroup_2;
	  
	  private JLabel txtMcpttGroup_fd2;
	  
	  private JLabel txtMcpttGroup_3;
	  
	  private JLabel payloadtypelabel;
	  
	  private JLabel payloadcontentlabel;
	  
	  private JLabel payloadcontentfilelabelfd;
	  
	  private JLabel payloadcontentfilelabelfdgroup;
	  
	  private JLabel payloadcontentlabel1;
	  
	  private JLabel payloadtypelabelfd;
	  
	  private JLabel payloadcontentlabelfd;
	  
	  private JLabel payloadcontentlabelfdgroup;
	  
	  private JLabel payloadcontentlabelfd1;
	  
	  private JLabel receiverlabel;
	  
	  private JLabel receiverlabel1;
	  
	  private JLabel checkboxlabel;
	  
	  private JLabel receiverlabelfd;
	  
	  private JTextArea networkfilearea;
	  
	  private JTextArea networkfileareagroup;
	  
	  private JLabel receiverlabelfd1;
	  
	  private JLabel checkboxlabelfd;
	  
	  private JLabel checkboxlabelfd2;
	  
	  private JLabel checkboxlabelfdgroup;
	  
	  private JLabel checkboxlabelfdgroup2;
	  
	  private JComboBox<String> Affiliated_Groups;
	  
	  private JCheckBox checkbox;
	  
	  private JCheckBox checkboxfd;
	  
	  private JCheckBox checkboxfd2;
	  
	  private JCheckBox checkboxfdgroup;
	  
	  private JCheckBox checkboxfdgroup2;
	  
	  private JButton De_affiliate;
	  
	  private JButton SDS_group;
	  
	  private JButton SDS_group1;
	  
	  private JButton SDS_one_to_one;
	  
	  private JButton SDS_one_to_one1;
	  
	  private JButton FD_group;
	  
	  private JButton FD_group1;
	  
	  private JButton FD_group2;
	  
	  private JButton FD_one_to_one;
	  
	  private JButton FD_one_to_one1;
	  
	  private JButton FD_one_to_one2;

	  public static JButton Deaff_Refresh;
	  
	  public static JButton SDS_Refresh;
	  
	  public static JButton FD_Refresh;
	  
	  public static JButton SDS_Refresh1;
	  
	  private JTextArea Affiliated_Groups_List;
	  
	  private JTextArea textArea;
	  
	  private JTextArea textAreafd;
	  
	  private JTextArea textArea1;
	  
	  private JTextArea textAreafd1;
	  
	  private JTextArea receiverarea;
	  
	  private JTextArea receiverareafd;
	  
	  private JTextArea receiverarea1;
	  
	  private JTextArea receiverareafd1;
	  
	  private JTextArea chatarea;
	  
	  private JTextArea chatarea1;
	  
	  private JTextArea chatarea2;
	  
	  private JTextArea chatarea3;
	  
	  private JTextArea chatareafd;
	  
	  private JTextArea chatareafd1;
	  
	  private JTextField textfield;
	  
	  public String selectAffURI;
	  
	  public String serverstorefile = "/home/mcpttserver/Desktop/FDfile/";
	  
	  private File selectedFile;
	  
	  private File selectedFilegroup;
	  
	  HashMap<String, String> conversationmap = new HashMap<>();
	  
	  HashMap<String, String> conversationmapfd = new HashMap<>();
	  
	  private static Timer timer;
	    private static boolean userMadeChoice = false;
	    private static final int TIMEOUT = 6000; // 60 秒（可修改）
	  
	  public static void main(final String[] args) {
	    EventQueue.invokeLater(new Runnable() {
	    	
	          public void run() {
	            try {
	            	clientUI.user = new SipClient();
		            clientUI window = new clientUI();
		            clientUI.user.addPropertyChangeListener(window);
		            //String tep = args[0];
		            clientUI.parameterSet(clientUI.SplitParm());
		            window.setTitle((clientUI.SplitParm()).MCPTTUserName);
		            window.initialize();
		            clientUI.frmIwfUserInterface.setBounds(clientUI.getXbound(), clientUI.getYbound(), 745, 350);
		            clientUI.frmIwfUserInterface.setVisible(true);
		            //clientUI.user.setEMRparm(clientUI.SplitParm());
		            System.out.println(clientUI.ServerIP);
		            clientUI.user.createSipClientInstance(clientUI.username, "123456", clientUI.ServerIP, clientUI.localIp, clientUI.SplitParm());
		            clientUI.sipRegisterSender = new SipRegisterSender(clientUI.user, 120);
		            clientUI.sipRegisterSender.setName("registerSender");
		            clientUI.sipRegisterSender.start();
		            clientUI.user.publishsender();
	            } catch (Exception e) {
	              e.printStackTrace();
	            } 
	          }
	        });
	  }
	  
	  private static client_parameters SplitParm() {
	    parm = new client_parameters();
	    parm.MCPTTUserName = "test2";
//	    parm.MCPTTServerDomain = "140.113.110.221";
	    parm.MCPTTServerDomain = "140.113.110.221";
	    parm.MCPTTUserlocalIp = "192.168.0.35";
	    parm.LMRUserId = "1";
	    parm.LMRLocalIp = "1";
	    parm.LMRLocalPort = "1";
	    parm.EMRCardNo = "1";
	    parm.EMRIp = "1";
	    parm.Active = "true";
//	    String[] parms = temp.split(",");
//	    parm.MCPTTUserName = parms[0];
//	    parm.MCPTTServerDomain = parms[1];
//	    parm.MCPTTUserlocalIp = parms[2];
//	    parm.LMRUserId = parms[3];
//	    parm.LMRLocalIp = parms[4];
//	    parm.LMRLocalPort = parms[5];
//	    parm.EMRCardNo = parms[6];
//	    parm.EMRIp = parms[7];
//	    parm.Active = parms[8];
	    return parm;
	  }
	  
	  public static void parameterSet(client_parameters parm) {
	    ServerIP = parm.MCPTTServerDomain;
	    username = parm.MCPTTUserName;
	    userURI = String.valueOf(username) + "@" + ServerIP;
	    localIp = parm.MCPTTUserlocalIp;
	    LMRUserID = parm.LMRUserId;
	    LMRLocalIP = parm.LMRLocalIp;
	    LMRLocalPort = parm.LMRLocalPort;
	    EMRCardNo = parm.EMRCardNo;
	    EMRIP = parm.EMRIp;
	  }
	  
	  public clientUI() {
	    initialize();
	  }
	  
	  private void initialize() {
	    frmIwfUserInterface = new JFrame();
	    frmIwfUserInterface.addWindowListener(new WindowAdapter() {	    	
	          public void windowClosing(WindowEvent e) {//j frame關閉後執行
	            if (clientUI.user.getisRegister()) {
	            	clientUI.Log_Out.doClick();
	              try {
	                Thread.sleep(300L);
	              } catch (InterruptedException e1) {
	                e1.printStackTrace();
	              } 
	              int selectedOption = JOptionPane.showConfirmDialog(null, "Do you wanna close the IWF:<" + clientUI.username + ">" + " Interface?", "Warring: <" + clientUI.username + "> ", 0, 2);
	              if (selectedOption == 0) {
	                if (!clientUI.user.GroupName.equals("")) {
	                	//clientUI.this.releasesession();
	                  try {
	                    Thread.sleep(500L);
	                  } catch (InterruptedException e1) {
	                    e1.printStackTrace();
	                  } 
	                } 
	                if (clientUI.user.getisRegister()) {
	                	clientUI.user.setisRegister(false);
	                	clientUI.user.stoppublishsender();
	                	clientUI.sipRegisterSender.stopSend();
	                	clientUI.this.Log_In.setVisible(true);
	                	clientUI.Log_Out.setVisible(false);
	                  try {
	                    Thread.sleep(300L);
	                  } catch (InterruptedException e1) {
	                    e1.printStackTrace();
	                  } 
	                } 
	                clientUI.user.leave();
	                clientUI.frmIwfUserInterface.setDefaultCloseOperation(3);
	              } else if (selectedOption == 1) {
	            	  clientUI.frmIwfUserInterface.setDefaultCloseOperation(0);
	              } 
	            } else {
	              int selectedOption = JOptionPane.showConfirmDialog(null, "Do you wanna close the IWF:<" + clientUI.username + ">" + " Interface?", "Warring: <" + clientUI.username + "> ", 0, 2);
	              if (selectedOption == 0) {
	            	  clientUI.user.leave();
	            	  clientUI.frmIwfUserInterface.setDefaultCloseOperation(3);
	              } else if (selectedOption == 1) {
	            	  clientUI.frmIwfUserInterface.setDefaultCloseOperation(0);
	              } 
	            } 
	          }
	        });

//	    sclientUI.user.addPropertyChangeListener(window);
	    frmIwfUserInterface.setResizable(false);
	    frmIwfUserInterface.getContentPane().setForeground(UIManager.getColor("Button.background"));
	    frmIwfUserInterface.getContentPane().setFont(new Font("Segoe UI", 1, 14));
	    frmIwfUserInterface.setTitle("Client: <> " + "Interface");
	   
	    frmIwfUserInterface.setDefaultCloseOperation(3);
	    frmIwfUserInterface.getContentPane().setLayout((LayoutManager)null);
	    
	    tabbedPane = new JTabbedPane(2);
	    tabbedPane.setBackground(Color.LIGHT_GRAY);
	    tabbedPane.setForeground(Color.BLACK);
	    tabbedPane.setFont(new Font("Segoe UI", 1, 14));
	    tabbedPane.setBounds(10, 10, 710, 295);
	    frmIwfUserInterface.getContentPane().add(tabbedPane);
	   
	    this.panel1 = new JPanel();
	    tabbedPane.addTab("Log In/Log Out", (Icon)null, this.panel1, (String)null);
	    this.panel1.setLayout((LayoutManager)null);
	    this.Log_In = new JButton("Log In");
	    this.Log_In.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent arg0) {
	        	  clientUI.user.setisRegister(true);
	        	  System.out.println(clientUI.user.getisRegister());
	        	  clientUI.sipRegisterSender = new SipRegisterSender(clientUI.user, 120);
	        	  clientUI.sipRegisterSender.setName("registerSender");
	        	  clientUI.sipRegisterSender.start();
	        	  clientUI.user.publishsender();
	        	  clientUI.this.Log_In.setVisible(false);
	          }
	        });
	    this.Log_In.setFont(new Font("Segoe UI", 1, 12));
	    this.Log_In.setBounds(105, 124, 300, 40);
	    this.panel1.add(this.Log_In);
	    this.Log_In.setVisible(false);
	    
	    
	    Log_Out = new JButton("Log Out");
	    Log_Out.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent arg0) {
	            if (!clientUI.user.GroupName.equals("")) {
	              int selectedOption = JOptionPane.showConfirmDialog(null, "Do you wanna release session and log out?", "Warring: <" + clientUI.username + "> ", 0, 2);
	              if (selectedOption == 0) {
	            	  //clientUI.this.releasesession();
	                try {
	                  Thread.sleep(500L);                
	                  
	                } catch (InterruptedException e1) {
	                  e1.printStackTrace();
	                } 
	                clientUI.user.setisRegister(false);
	                System.out.println(clientUI.user.getisRegister());
	                clientUI.user.stoppublishsender();
	                clientUI.sipRegisterSender.stopSend();
	                clientUI.this.Log_In.setVisible(true);
	                clientUI.Log_Out.setVisible(false);
	              } 
	            } else {
	              int selectedOption = JOptionPane.showConfirmDialog(null, "Do you wanna log out?", "Warring: <" + clientUI.username + "> ", 0, 2);
	              if (selectedOption == 0) {
	            	  clientUI.user.setisRegister(false);
	            	  System.out.println(clientUI.user.getisRegister()+"11");
	            	  clientUI.user.stoppublishsender();
	            	  clientUI.sipRegisterSender.stopSend();
	            	  clientUI.this.Log_In.setVisible(true);
	            	  clientUI.Log_Out.setVisible(false);
	              } 
	            } 
	            
	            clientUI.this.Affiliated_Groups_List.setText((String)null);
	            clientUI.this.Affiliated_Groups.removeAllItems();
	            clientUI.this.Existing_Groups.removeAllItems();
	            clientUI.this.Session_Affiliated_Groups.removeAllItems();
	            clientUI.Aff_Refresh.setEnabled(clientUI.this.getenable());
	            clientUI.this.Affiliate.setEnabled(clientUI.this.getenable());
	            clientUI.this.Existing_Groups.setEnabled(clientUI.this.getenable());
	            clientUI.this.srollpane.setEnabled(clientUI.this.getenable());
	            clientUI.this.De_affiliate.setEnabled(clientUI.this.getenable());
	            clientUI.Deaff_Refresh.setEnabled(clientUI.this.getenable());
	            clientUI.this.Affiliated_Groups.setEnabled(clientUI.this.getenable());
	            clientUI.this.Set_Up_Session.setEnabled(clientUI.this.getenable());
	            clientUI.this.Release_Session.setEnabled(clientUI.this.getenable());
	            clientUI.Session_Refresh.setEnabled(clientUI.this.getenable());
	            clientUI.this.Session_Affiliated_Groups.setEnabled(clientUI.this.getenable());
	          }
	        });
	    Log_Out.setFont(new Font("Segoe UI", 1, 12));
	    Log_Out.setBounds(105, 124, 300, 40);
	    this.panel1.add(Log_Out);
	    Log_Out.setVisible(true);
	    
	    JTextArea lblNewLabel_3 = new JTextArea("");
	    
	    lblNewLabel_3.setForeground(Color.BLACK);
	    lblNewLabel_3.setWrapStyleWord(true);
	    lblNewLabel_3.setEditable(false);
	    lblNewLabel_3.setBackground(UIManager.getColor("Button.background"));
	    lblNewLabel_3.setLineWrap(true);
	    lblNewLabel_3.setFont(new Font("Segoe UI", 1, 20));
	    lblNewLabel_3.setBounds(30, 10, 424, 80);
	    this.panel1.add(lblNewLabel_3);
	    
	    
	    
	    panel2 = new JPanel();
	    tabbedPane.addTab("Affiliate Group", (Icon)null, panel2, (String)null);
	    panel2.setLayout((LayoutManager)null);
	    
	    JTextArea txtrSelectTheUser = new JTextArea();
	    txtrSelectTheUser.setDropMode(DropMode.INSERT);
	    txtrSelectTheUser.setWrapStyleWord(true);
	    txtrSelectTheUser.setLineWrap(true);
	    txtrSelectTheUser.setEditable(false);
	    txtrSelectTheUser.setBackground(UIManager.getColor("Button.background"));
	    txtrSelectTheUser.setForeground(Color.BLACK);
	    txtrSelectTheUser.setFont(new Font("Segoe UI", 1, 20));
	    txtrSelectTheUser.setText("Select an Group to Affiliate.");
	    txtrSelectTheUser.setBounds(30, 10, 424, 80);
	    panel2.add(txtrSelectTheUser);
	    
	    this.txtMcpttGroup = new JLabel();
	    this.txtMcpttGroup.setBackground(UIManager.getColor("Button.background"));
	    this.txtMcpttGroup.setFont(new Font("Segoe UI", 1, 12));
	    this.txtMcpttGroup.setText("Existing Group(s)");
	    this.txtMcpttGroup.setBounds(30, 84, 200, 21);
	    this.txtMcpttGroup.setBorder((Border)null);
	    panel2.add(this.txtMcpttGroup);
	    
	    
	    this.Existing_Groups = new JComboBox<>();
	    this.Existing_Groups.setBounds(30, 105, 200, 23);
	    panel2.add(this.Existing_Groups);
	    
	    
	    this.Affiliate = new JButton("Affiliate");
	    this.Affiliate.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent arg0) {
	            String GroupURI = (String)clientUI.user.getGroupdictionary().get(clientUI.this.Existing_Groups.getSelectedItem());
	            try {
	            	System.out.println(GroupURI);
	              clientUI.user.sendPUBLISH(GroupURI, true);
	            } catch (ParseException e) {
	              e.printStackTrace();
	            } catch (InvalidArgumentException e) {
	              e.printStackTrace();
	            } catch (SipException e) {
	              e.printStackTrace();
	            } 
	          }
	    	});
	    

	    this.Affiliate.setFont(new Font("Segoe UI", 1, 12));
	    this.Affiliate.setBounds(274, 94, 180, 40);
	    panel2.add(this.Affiliate);
	    this.Affiliate.setEnabled(getenable());
	    
	    
	    this.Affiliated_Groups_List = new JTextArea();
	    this.Affiliated_Groups_List.setFont(new Font("Segoe UI", 0, 13));
	    this.Affiliated_Groups_List.setEditable(false);
	    this.Affiliated_Groups_List.setLineWrap(true);
	    this.Affiliated_Groups_List.setBounds(389, 113, 144, 72);
	    panel2.add(this.Affiliated_Groups_List);
	    
	    this.srollpane = new JScrollPane(this.Affiliated_Groups_List, 20, 30);
	    this.srollpane.setSize(200, 78);
	    this.srollpane.setLocation(30, 197);
	    panel2.add(this.srollpane);
	    
	    JLabel lblAffiliatedGroups = new JLabel();
	    lblAffiliatedGroups.setText("Affiliated Group(s)");
	    lblAffiliatedGroups.setFont(new Font("Segoe UI", 1, 12));
	    lblAffiliatedGroups.setBorder((Border)null);
	    lblAffiliatedGroups.setBackground(SystemColor.menu);
	    lblAffiliatedGroups.setBounds(30, 174, 200, 21);
	    panel2.add(lblAffiliatedGroups);
	    
	    Aff_Refresh = new JButton();
	    Aff_Refresh.setForeground(new Color(0, 0, 0));
	    Aff_Refresh.setFont(new Font("Segoe UI", 1, 12));
	    Aff_Refresh.setText("Refresh");
	    Aff_Refresh.setBackground(UIManager.getColor("Button.background"));
	    //Aff_Refresh.setIcon(new ImageIcon(getRefreshIcon()));
	    Aff_Refresh.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent arg0) {
	            try {
	              List<String> grouplist = clientUI.user.getAllGroupDisplayName(clientUI.ServerIP);
	              //System.out.println("aff"+grouplist);
	              clientUI.this.Existing_Groups.removeAllItems();
	              for (int i = 1; i <= grouplist.size(); i++)
	            	  clientUI.this.Existing_Groups.addItem(grouplist.get(i - 1)); 
	              List<String> GetDirectInnfo = clientUI.user.getAffiliatedGroup(clientUI.username, clientUI.ServerIP);
	              clientUI.this.Affiliated_Groups_List.setText((String)null);
	              if (GetDirectInnfo.size() != 0) {
	                try {
	                  Thread.sleep(500L);
	                } catch (InterruptedException e1) {
	                  e1.printStackTrace();
	                } 
	                HashMap<String, String> Groupdictionary = clientUI.user.getGroupdictionary();
	                
	                for (int j = 1; j <= GetDirectInnfo.size(); j++) {
	                  String AffGroupURI = String.valueOf(GetDirectInnfo.get(j - 1)) + "@" + clientUI.ServerIP;
	                  List<String> Keys = clientUI.getKeyList(Groupdictionary, AffGroupURI);
	                  clientUI.this.Affiliated_Groups_List.append(" " + (String)Keys.get(0) + "\n");
	                } 
	                clientUI.this.Affiliated_Groups_List.setCaretPosition(clientUI.this.Affiliated_Groups_List.getDocument().getLength());
	              } 
	            } catch (ParseException|IOException e1) {
	              e1.printStackTrace();
	            } 
	          }
	        });
	    Aff_Refresh.setBounds(334, 252, 120, 23);
	    panel2.add(Aff_Refresh);
	    Aff_Refresh.setEnabled(getenable());
	    
	    
	    this.panel3 = new JPanel();
	    tabbedPane.addTab("De-affiliate Group", (Icon)null, this.panel3, (String)null);
	    this.panel3.setLayout((LayoutManager)null);
	    this.txtrSelectAnMcptt = new JTextArea();
	    this.txtrSelectAnMcptt.setWrapStyleWord(true);
	    this.txtrSelectAnMcptt.setText("Select an Group to De-affiliate.");
	    this.txtrSelectAnMcptt.setLineWrap(true);
	    this.txtrSelectAnMcptt.setForeground(Color.BLACK);
	    this.txtrSelectAnMcptt.setFont(new Font("Segoe UI", 1, 20));
	    this.txtrSelectAnMcptt.setEditable(false);
	    this.txtrSelectAnMcptt.setDropMode(DropMode.INSERT);
	    this.txtrSelectAnMcptt.setBackground(UIManager.getColor("Button.background"));
	    this.txtrSelectAnMcptt.setBounds(30, 10, 424, 80);
	    this.panel3.add(this.txtrSelectAnMcptt);
	    
	    this.txtMcpttGroup_1 = new JLabel();
	    this.txtMcpttGroup_1.setText("Affiliated Group(s)");
	    this.txtMcpttGroup_1.setFont(new Font("Segoe UI", 1, 12));
	    this.txtMcpttGroup_1.setBorder((Border)null);
	    this.txtMcpttGroup_1.setBackground(SystemColor.menu);
	    this.txtMcpttGroup_1.setBounds(30, 84, 200, 21);
	    this.panel3.add(this.txtMcpttGroup_1);
	    
	    this.Affiliated_Groups = new JComboBox<>();
	    this.Affiliated_Groups.setBounds(30, 105, 200, 23);
	    this.panel3.add(this.Affiliated_Groups);
	    
	    this.De_affiliate = new JButton("De-affiliate");
	    this.De_affiliate.setFont(new Font("Segoe UI", 1, 12));
	    this.De_affiliate.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent arg0) {
	            try {
	              String GroupURI = (String)clientUI.user.getGroupdictionary().get(clientUI.this.Affiliated_Groups.getSelectedItem());
	              int selectedOption = JOptionPane.showConfirmDialog(null, "Do you want to de-affiliate to the MCPTT group [" + clientUI.this.Affiliated_Groups.getSelectedItem() + "]?", 
	                  "Warring: <" + clientUI.username + "> ", 0, 2);
	              if (selectedOption == 0) {
	                if (clientUI.user.GroupName != "")
	                	//clientUI.this.releasesession(); 
	                try {
	                  Thread.sleep(500L);
	                } catch (InterruptedException e1) {
	                  e1.printStackTrace();
	                } 
	                clientUI.user.sendPUBLISH(GroupURI, false);
	              } 
	            } catch (ParseException e) {
	              e.printStackTrace();
//	            } catch (ParseException e) {
//	              e.printStackTrace();
	            } catch (InvalidArgumentException e) {
	              e.printStackTrace();
	            } catch (SipException e) {
	              e.printStackTrace();
	            } 
	          }
	        });
	    this.De_affiliate.setBounds(274, 94, 180, 40);
	    this.panel3.add(this.De_affiliate);
	    this.De_affiliate.setEnabled(getenable());
	    
	    Deaff_Refresh = new JButton();
	    //Deaff_Refresh.setIcon(new ImageIcon(getRefreshIcon()));
	    Deaff_Refresh.setText("Refresh");
	    Deaff_Refresh.setForeground(Color.BLACK);
	    Deaff_Refresh.setFont(new Font("Segoe UI", 1, 12));
	    Deaff_Refresh.setBackground(UIManager.getColor("Button.background"));
	    Deaff_Refresh.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent arg0) {
	            try {
	              List<String> grouplist = clientUI.user.getAllGroupDisplayName(clientUI.ServerIP);
	              clientUI.this.Affiliated_Groups.removeAllItems();
	              List<String> GetDirectInnfo = clientUI.user.getAffiliatedGroup(clientUI.username, clientUI.ServerIP);
	              if (GetDirectInnfo.size() != 0) {
	                try {
	                  Thread.sleep(500L);
	                } catch (InterruptedException e1) {
	                  e1.printStackTrace();
	                } 
	                HashMap<String, String> Groupdictionary = clientUI.user.getGroupdictionary();
	                for (int i = 1; i <= GetDirectInnfo.size(); i++) {
	                  String AffGroupURI = String.valueOf(GetDirectInnfo.get(i - 1)) + "@" + clientUI.ServerIP;
	                  List<String> Keys = clientUI.getKeyList(Groupdictionary, AffGroupURI);
	                  clientUI.this.Affiliated_Groups.addItem(Keys.get(0));
	                } 
	              } else if (GetDirectInnfo.size() == 0) {
	                JOptionPane.showMessageDialog(null, " < " + clientUI.username + " >" + " doesn't affiliate to any MCPTT groups.", "Waring: <" + clientUI.username + "> ", 2);
	              } 
	            } catch (ParseException e) {
	              e.printStackTrace();
	            } catch (IOException e) {
	              e.printStackTrace();
	            } 
	          }
	        });
	    Deaff_Refresh.setBounds(334, 252, 120, 23);
	    this.panel3.add(Deaff_Refresh);
	    Deaff_Refresh.setEnabled(getenable());
	    
	    
	    
	    panel4 = new JPanel();
//	    tabbedPane.addTab("Set Up/Release Session", (Icon)null, panel4, (String)null);
//	    panel4.setLayout((LayoutManager)null);
	    
	    
	    final JTextArea txtrSelectAMcptt = new JTextArea();
	    txtrSelectAMcptt.setEditable(false);
	    txtrSelectAMcptt.setWrapStyleWord(true);
	    txtrSelectAMcptt.setText("Select an MCPTT Group to Set Up Session.");
	    txtrSelectAMcptt.setLineWrap(true);
	    txtrSelectAMcptt.setForeground(Color.BLACK);
	    txtrSelectAMcptt.setFont(new Font("Segoe UI", 1, 20));
	    txtrSelectAMcptt.setBackground(UIManager.getColor("Button.background"));
	    txtrSelectAMcptt.setBounds(30, 10, 424, 80);
	    panel4.add(txtrSelectAMcptt);
	    
	    this.lblNewLabel_1 = new JLabel("Affiliated MCPTT Group(s)");
	    this.lblNewLabel_1.setBackground(UIManager.getColor("Button.background"));
	    this.lblNewLabel_1.setFont(new Font("Segoe UI", 1, 12));
	    this.lblNewLabel_1.setBounds(30, 84, 200, 21);
	    panel4.add(this.lblNewLabel_1);
	   
	    this.Session_Affiliated_Groups = new JComboBox<>();
	    this.Session_Affiliated_Groups.setBounds(30, 225, 200, 23);
	    panel4.add(this.Session_Affiliated_Groups);
	    
	    JButton btnNewButton_15_2 = new JButton("###");
	    btnNewButton_15_2.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent arg0) {
	            System.out.println("[clientUI] getEmergencyState = " + clientUI.user.getEmergencyState());
	            System.out.println("[clientUI] getGroupState = " + clientUI.user.getGroupState());
	            System.out.println("[clientUI] getCallInfoValue = " + clientUI.user.getCallInfoValue());
	            System.out.println("[clientUI] mediaSipSessionMap = " + SipClient.mediaSipSessionMap);
	            System.out.println("[clientUI] EMRmediaSipSessionMap = " + SipClient.EMRmediaSipSessionMap);
	            System.out.println("[clientUI] SetUpSessionWithGroup = " + clientUI.user.GroupName);
	          }
	        });
	    btnNewButton_15_2.setBounds(387, 204, 65, 23);
	    panel4.add(btnNewButton_15_2);
	    btnNewButton_15_2.setVisible(false);
	    
	    this.Set_Up_Session = new JButton("Set Up Session");
	    this.Set_Up_Session.setFont(new Font("Segoe UI", 1, 12));
	    this.Set_Up_Session.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent arg0) {
	            String GroupName = ((String)clientUI.user.getGroupdictionary().get(clientUI.this.Session_Affiliated_Groups.getSelectedItem())).split("@")[0];
//	            try {
//	            	clientUI.user.sendInvite(GroupName, clientUI.ServerIP, 100);
//	            } catch (IOException e) {
//	              e.printStackTrace();
//	            } 
	          }
	        });
	    this.Set_Up_Session.setBounds(274, 94, 180, 40);
	    panel4.add(this.Set_Up_Session);
	    this.Set_Up_Session.setVisible(true);
	    this.Set_Up_Session.setEnabled(getenable());
	    
	    
	    this.Release_Session = new JButton("");
	    this.Release_Session.setFont(new Font("Segoe UI", 1, 12));
	    this.Release_Session.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent arg0) {
//	        	  clientUI.this.releasesession();
	          }
	        });
	    this.Release_Session.setBounds(105, 124, 300, 40);
	    panel4.add(this.Release_Session);
	    this.Release_Session.setVisible(false);
	    this.Release_Session.setEnabled(getenable());
	    Session_Refresh = new JButton();
	    Session_Refresh.setBackground(UIManager.getColor("Button.background"));
	    Session_Refresh.setFont(new Font("Segoe UI", 1, 12));
	    Session_Refresh.setForeground(new Color(0, 0, 0));
	    Session_Refresh.setText("Refresh");
	    //Session_Refresh.setIcon(new ImageIcon(getRefreshIcon()));
	    Session_Refresh.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent arg0) {
	            try {
	              if (clientUI.user.GroupName != "") {
//	            	  clientUI.user.getAllGroupDisplayName(clientUI.ServerIP);
	                HashMap<String, String> Groupdictionary = clientUI.user.getGroupdictionary();
	                String GroupURI = String.valueOf(clientUI.user.GroupName) + "@" + clientUI.ServerIP;
	                List<String> Keys = clientUI.getKeyList(Groupdictionary, GroupURI);
	                clientUI.frmIwfUserInterface.setTitle("IWF: <" + clientUI.getTitle() + "> " + "Interface" + " <---> [" + (String)Keys.get(0) + "] ");
	                txtrSelectAMcptt.setText("Do You Want to Release Session of MCPTT Group [" + (String)Keys.get(0) + "]?");
	                clientUI.this.Release_Session.setText("Confirm");
	                clientUI.Session_Refresh.setVisible(false);
	                clientUI.this.Release_Session.setVisible(true);
	                clientUI.this.Set_Up_Session.setVisible(false);
	                clientUI.this.Session_Affiliated_Groups.setVisible(false);
	                clientUI.this.lblNewLabel_1.setVisible(false);
	              } else {
	            	  clientUI.frmIwfUserInterface.setTitle("IWF: <" + clientUI.getTitle() + "> " + "Interface");
	                txtrSelectAMcptt.setText("Select an MCPTT Group to Set Up Session.");
	                clientUI.Session_Refresh.setVisible(true);
	                clientUI.this.Release_Session.setVisible(false);
	                clientUI.this.Set_Up_Session.setVisible(true);
	                clientUI.this.Session_Affiliated_Groups.setVisible(true);
	                clientUI.this.lblNewLabel_1.setVisible(true);
	              } 
	              //List<String> grouplist = clientUI.user.getAllGroupDisplayName(clientUI.ServerIP);
	              clientUI.this.Session_Affiliated_Groups.removeAllItems();
	              List<String> GetDirectInnfo = clientUI.user.getAffiliatedGroup(clientUI.username, clientUI.ServerIP);
	              if (GetDirectInnfo.size() != 0) {
	                HashMap<String, String> Groupdictionary = clientUI.user.getGroupdictionary();
	                for (int i = 1; i <= GetDirectInnfo.size(); i++) {
	                  String AffGroupURI = String.valueOf(GetDirectInnfo.get(i - 1)) + "@" + clientUI.ServerIP;
	                  List<String> Keys = clientUI.getKeyList(Groupdictionary, AffGroupURI);
	                  clientUI.this.Session_Affiliated_Groups.addItem(Keys.get(0));
	                } 
	              } else if (GetDirectInnfo.size() == 0) {
	                JOptionPane.showMessageDialog(null, " <" + clientUI.username + "> " + "doesn't affiliate to any MCPTT groups.", "Waring: <" + clientUI.username + "> ", 2);
	              } 
	            } catch (IOException e1) {
	              e1.printStackTrace();
	            } 
	          }
	        });
	  
	    Session_Refresh.setBounds(334, 252, 120, 23);
	    panel4.add(Session_Refresh);
	    Session_Refresh.setEnabled(getenable());
	    
	    //////////////////////
	    
	    this.panel5 = new JPanel();
	    tabbedPane.addTab("SDS", (Icon)null, this.panel5, (String)null);
	    this.panel5.setLayout((LayoutManager)null);
	    this.sendSDS = new JTextArea();
	    this.sendSDS.setWrapStyleWord(true);
	    this.sendSDS.setText("send SDS.");
	    this.sendSDS.setLineWrap(true);
	    this.sendSDS.setForeground(Color.BLACK);
	    this.sendSDS.setFont(new Font("Segoe UI", 1, 20));
	    this.sendSDS.setEditable(false);
	    this.sendSDS.setDropMode(DropMode.INSERT);
	    this.sendSDS.setBackground(UIManager.getColor("Button.background"));
	    this.sendSDS.setBounds(0, 10, 150, 30);
	    this.panel5.add(this.sendSDS);
	    
	    String[] options = {"TEXT", "BINARY", "HYPERLINKS", "FILEURL"};
	    
        this.ComboBox = new JComboBox<>(options);
        this.panel5.add(this.ComboBox);

        this.ComboBox.setBounds(260, 45, 200, 23);
	    
        this.payloadtypelabel = new JLabel();
	    this.payloadtypelabel.setText("Payload type");
	    this.payloadtypelabel.setFont(new Font("Segoe UI", 1, 12));
	    this.payloadtypelabel.setBorder((Border)null);
	    this.payloadtypelabel.setBackground(SystemColor.menu);
	    this.payloadtypelabel.setBounds(260, 24, 200, 21);
	    this.panel5.add(this.payloadtypelabel);
        
	    this.payloadcontentlabel = new JLabel();
	    this.payloadcontentlabel.setText("payload content");
	    this.payloadcontentlabel.setFont(new Font("Segoe UI", 1, 12));
	    this.payloadcontentlabel.setBorder((Border)null);
	    this.payloadcontentlabel.setBackground(SystemColor.menu);
	    this.payloadcontentlabel.setBounds(40, 210, 200, 21);
	    this.panel5.add(this.payloadcontentlabel);
	    
	    this.checkbox = new JCheckBox();
	    this.checkbox.setBounds(260, 110, 20, 21);
	    this.checkboxlabel = new JLabel("app(payload destination type)");
        this.checkboxlabel.setBounds(290, 110, 200, 21);
        // 添加到 JFrame 中
        this.panel5.add(this.checkbox);
        this.panel5.add(this.checkboxlabel);

        // 设置窗口可见
	    
	    this.receiverlabel = new JLabel();
	    this.receiverlabel.setText("receiver");
	    this.receiverlabel.setFont(new Font("Segoe UI", 1, 12));
	    this.receiverlabel.setBorder((Border)null);
	    this.receiverlabel.setBackground(SystemColor.menu);
	    this.receiverlabel.setBounds(260, 70, 200, 21);
	    this.panel5.add(this.receiverlabel);
	    
	    this.textArea = new JTextArea(5, 5); // 5 行，20 列//payload content
	    JScrollPane scrollPane = new JScrollPane(this.textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 添加滾動條
	    this.textArea.setVisible(true);
	    this.textArea.setWrapStyleWord(true);  // 在单词边界处换行
	    this.textArea.setLineWrap(true);
	    scrollPane.setBounds(40, 240, 160, 40);
	    this.panel5.add(scrollPane);
	    
	    this.receiverarea = new JTextArea(2, 20); // 5 行，20 列 receiver
	    this.receiverarea.setVisible(true);
//	    this.receiverarea.setEditable(false);
	    this.receiverarea.setBounds(260, 90, 160, 20);
	    this.panel5.add(this.receiverarea);
	    
	    this.chatarea = new JTextArea(10, 20); // 5 行，20 列 payload content
	    JScrollPane scrollPane2 = new JScrollPane(this.chatarea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 添加滾動條
	    this.chatarea.setVisible(true);
	    this.chatarea.setLineWrap(true);  // 启用自动换行
	    this.chatarea.setWrapStyleWord(true);  // 在单词边界处换行
	    this.chatarea.setEditable(false);
	    scrollPane2.setBounds(20, 120, 160, 90);
	    this.panel5.add(scrollPane2);
	    
	    this.chatarea1 = new JTextArea(10, 20); // 5 行，20 列 payload content
	    JScrollPane scrollPane3 = new JScrollPane(this.chatarea1, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 添加滾動條
	    this.chatarea1.setVisible(true);
	    this.chatarea1.setLineWrap(true);  // 启用自动换行
	    this.chatarea1.setWrapStyleWord(true);  // 在单词边界处换行
	    this.chatarea1.setEditable(false);
	    scrollPane3.setBounds(190, 137, 160, 90);
	    this.panel5.add(scrollPane3);
	    
	    this.txtMcpttGroup_2 = new JLabel();
	    this.txtMcpttGroup_2.setText("Affiliated Group(s)");
	    this.txtMcpttGroup_2.setFont(new Font("Segoe UI", 1, 12));
	    this.txtMcpttGroup_2.setBorder((Border)null);
	    this.txtMcpttGroup_2.setBackground(SystemColor.menu);
	    this.txtMcpttGroup_2.setBounds(30, 44, 200, 21);
	    this.panel5.add(this.txtMcpttGroup_2);
	    
	    this.Existing_Groups2 = new JComboBox<>();
	    this.Existing_Groups2.setBounds(30, 65, 200, 23);
	    this.panel5.add(this.Existing_Groups2);
	    
	    this.SDS_group = new JButton("SDS group");
	    this.SDS_group.setFont(new Font("Segoe UI", 1, 12));
	    this.SDS_group.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	            try {
	              String GroupURI = (String)clientUI.user.getGroupdictionary().get(clientUI.this.Existing_Groups2.getSelectedItem());
	              String payloadtypelabel = (String)clientUI.this.ComboBox.getSelectedItem();
	              String SDSmessage = textArea.getText();
//	              System.out.println("111"+SDSmessage);
	              //if(SDSmessage.size()>000)
	              Boolean box;
	              if (!clientUI.this.checkbox.isSelected()) {
	                    box=true;
	                } else {
	                    box=false;
	                }
	              String conversationID_uuidAsString="";
	              if(true) {
	            	  UUID conversationID_uuid = UUID.randomUUID();
	        	      conversationID_uuidAsString = conversationID_uuid.toString();
	        	      conversationmap.put(conversationID_uuidAsString, "2");
	              }else {
	            	  conversationID_uuidAsString="";
	              }
	              clientUI.user.sendMESSAGE(GroupURI, SDSmessage, "group", payloadtypelabel, "", box, conversationID_uuidAsString);
	               chatarea.append(SDSmessage+"\n");
	               chatarea.append("---group sds---\n");
	            } catch (ParseException e) {
	              e.printStackTrace();
//	            } catch (ParseException e) {
//	              e.printStackTrace();
	            } catch (InvalidArgumentException e) {
	              e.printStackTrace();
	            } catch (SipException e) {
	              e.printStackTrace();
	            } 
	          }
	        });
	    
	    this.SDS_group.setBounds(364, 200, 180, 40);
	    this.panel5.add(this.SDS_group);
	    this.SDS_group.setEnabled(getenable());

	    this.SDS_one_to_one = new JButton("SDS one");
	    this.SDS_one_to_one.setFont(new Font("Segoe UI", 1, 12));
	    this.SDS_one_to_one.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	            try {
	              String GroupURI = (String)clientUI.user.getGroupdictionary().get(clientUI.this.Existing_Groups2.getSelectedItem());
	              String payloadtypelabel = (String)clientUI.this.ComboBox.getSelectedItem();
	              String SDSmessage = textArea.getText();
	              String receiver = receiverarea.getText();
	              //if(SDSmessage.size()>000)
	              Boolean box;
	              if (!clientUI.this.checkbox.isSelected()) {
	                    box=true;
	                } else {
	                    box=false;
	                }
	              String conversationID_uuidAsString="";
	              if(true) {
	            	  UUID conversationID_uuid = UUID.randomUUID();
	        	      conversationID_uuidAsString = conversationID_uuid.toString();
	        	      conversationmap.put(conversationID_uuidAsString, "1");
	              }else {
	            	  conversationID_uuidAsString="";
	              }
	              clientUI.user.sendMESSAGE(GroupURI, SDSmessage, "one", payloadtypelabel, receiver, box, conversationID_uuidAsString);
	               chatarea1.append(SDSmessage+"\n");
	               chatarea1.append("---one-to-one sds---\n");
	               
	            } catch (ParseException e) {
	              e.printStackTrace();
//	            } catch (ParseException e) {
//	              e.printStackTrace();
	            } catch (InvalidArgumentException e) {
	              e.printStackTrace();
	            } catch (SipException e) {
	              e.printStackTrace();
	            } 
	          }
	        });
	    this.SDS_one_to_one.setBounds(364, 140, 180, 40);
	    this.panel5.add(this.SDS_one_to_one);
	    this.SDS_one_to_one.setEnabled(getenable());
	    
	    SDS_Refresh = new JButton();
	    //Deaff_Refresh.setIcon(new ImageIcon(getRefreshIcon()));
	    SDS_Refresh.setText("Refresh");
	    SDS_Refresh.setForeground(Color.BLACK);
	    SDS_Refresh.setFont(new Font("Segoe UI", 1, 12));
	    SDS_Refresh.setBackground(UIManager.getColor("Button.background"));
	    SDS_Refresh.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	            try {
//	              List<String> grouplist = clientUI.user.getAllGroupDisplayName(clientUI.ServerIP);
//	              clientUI.this.Existing_Groups2.removeAllItems();
//	              for (int i = 1; i <= grouplist.size(); i++)
//	            	  clientUI.this.Existing_Groups2.addItem(grouplist.get(i - 1)); 
	              
	              
	              clientUI.this.Existing_Groups2.removeAllItems();
	              List<String> GetDirectInnfo = clientUI.user.getAffiliatedGroup(clientUI.username, clientUI.ServerIP);
	              if (GetDirectInnfo.size() != 0) {
	                try {
	                  Thread.sleep(500L);
	                } catch (InterruptedException e1) {
	                  e1.printStackTrace();
	                } 
	                HashMap<String, String> Groupdictionary = clientUI.user.getGroupdictionary();
	                for (int i = 1; i <= GetDirectInnfo.size(); i++) {
	                  String AffGroupURI = String.valueOf(GetDirectInnfo.get(i - 1)) + "@" + clientUI.ServerIP;
	                  List<String> Keys = clientUI.getKeyList(Groupdictionary, AffGroupURI);
	                  clientUI.this.Existing_Groups2.addItem(Keys.get(0));
	                } 
	              } else if (GetDirectInnfo.size() == 0) {
//	                JOptionPane.showMessageDialog(null, " < " + clientUI.username + " >" + " doesn't affiliate to any MCPTT groups.", "Waring: <" + clientUI.username + "> ", 2);
	              } 
//	              List<String> GetDirectInnfo = clientUI.user.getAffiliatedGroup(clientUI.username, clientUI.ServerIP);
	              
//	              if (GetDirectInnfo.size() != 0) {
//	                try {
//	                  Thread.sleep(500L);
//	                } catch (InterruptedException e1) {
//	                  e1.printStackTrace();
//	                } 
//	                HashMap<String, String> Groupdictionary = clientUI.user.getGroupdictionary();
//	                for (int j = 1; j <= GetDirectInnfo.size(); j++) {
//	                  String AffGroupURI = String.valueOf(GetDirectInnfo.get(j - 1)) + "@" + clientUI.ServerIP;
//	                  List<String> Keys = clientUI.getKeyList(Groupdictionary, AffGroupURI);
//	                 
//	                } 
//	              } 
	            } catch (IOException e1) {
	              e1.printStackTrace();
	            } 
	          }
		        });
	    SDS_Refresh.setBounds(364, 252, 120, 23);
	    this.panel5.add(SDS_Refresh);
	    SDS_Refresh.setEnabled(true);
	  
	    
	    this.panel6 = new JPanel();
	    tabbedPane.addTab("converstaion", (Icon)null, this.panel6, (String)null);
	    this.panel6.setLayout((LayoutManager)null);
	    this.sendSDS1 = new JTextArea();
	    this.sendSDS1.setWrapStyleWord(true);
	    this.sendSDS1.setText("SDS media plane.");
	    this.sendSDS1.setLineWrap(true);
	    this.sendSDS1.setForeground(Color.BLACK);
	    this.sendSDS1.setFont(new Font("Segoe UI", 1, 20));
	    this.sendSDS1.setEditable(false);
	    this.sendSDS1.setDropMode(DropMode.INSERT);
	    this.sendSDS1.setBackground(UIManager.getColor("Button.background"));
	    this.sendSDS1.setBounds(30, 10, 150, 30);
	    this.panel6.add(this.sendSDS1);
	    
        
	    this.payloadcontentlabel1 = new JLabel();
	    this.payloadcontentlabel1.setText("payload content");
	    this.payloadcontentlabel1.setFont(new Font("Segoe UI", 1, 12));
	    this.payloadcontentlabel1.setBorder((Border)null);
	    this.payloadcontentlabel1.setBackground(SystemColor.menu);
	    this.payloadcontentlabel1.setBounds(40, 210, 200, 21);
	    this.panel6.add(this.payloadcontentlabel1);
	    
	    

        // 设置窗口可见
	    
	    this.receiverlabel1 = new JLabel();
	    this.receiverlabel1.setText("receiver");
	    this.receiverlabel1.setFont(new Font("Segoe UI", 1, 12));
	    this.receiverlabel1.setBorder((Border)null);
	    this.receiverlabel1.setBackground(SystemColor.menu);
	    this.receiverlabel1.setBounds(260, 70, 200, 21);
	    this.panel6.add(this.receiverlabel1);
	    
	    this.textArea1 = new JTextArea(5, 5); // 5 行，20 列//payload content
	    JScrollPane scrollPane4 = new JScrollPane(this.textArea1, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 添加滾動條
	    this.textArea1.setVisible(true);
	    this.textArea1.setWrapStyleWord(true);  // 在单词边界处换行
	    this.textArea1.setLineWrap(true);
	    scrollPane4.setBounds(40, 240, 160, 40);
	    this.panel6.add(scrollPane4);
	    
	    this.receiverarea1 = new JTextArea(2, 20); // 5 行，20 列 receiver
	    this.receiverarea1.setVisible(true);
//	    this.receiverarea.setEditable(false);
	    this.receiverarea1.setBounds(260, 90, 160, 20);
	    this.panel6.add(this.receiverarea1);
	    
	    this.chatarea2 = new JTextArea(10, 20); // 5 行，20 列 payload content
	    JScrollPane scrollPane5 = new JScrollPane(this.chatarea2, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 添加滾動條
	    this.chatarea2.setVisible(true);
	    this.chatarea2.setLineWrap(true);  // 启用自动换行
	    this.chatarea2.setWrapStyleWord(true);  // 在单词边界处换行
	    this.chatarea2.setEditable(false);
	    scrollPane5.setBounds(20, 120, 160, 90);
	    this.panel6.add(scrollPane5);
	    
	    this.chatarea3 = new JTextArea(10, 20); // 5 行，20 列 payload content
	    JScrollPane scrollPane6 = new JScrollPane(this.chatarea3, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 添加滾動條
	    this.chatarea3.setVisible(true);
	    this.chatarea3.setLineWrap(true);  // 启用自动换行
	    this.chatarea3.setWrapStyleWord(true);  // 在单词边界处换行
	    this.chatarea3.setEditable(false);
	    scrollPane6.setBounds(190, 137, 160, 90);
	    this.panel6.add(scrollPane6);
	    
	    this.txtMcpttGroup_3 = new JLabel();
	    this.txtMcpttGroup_3.setText("Affiliated Group(s)");
	    this.txtMcpttGroup_3.setFont(new Font("Segoe UI", 1, 12));
	    this.txtMcpttGroup_3.setBorder((Border)null);
	    this.txtMcpttGroup_3.setBackground(SystemColor.menu);
	    this.txtMcpttGroup_3.setBounds(30, 44, 200, 21);
	    this.panel6.add(this.txtMcpttGroup_3);
	    
	    this.Existing_Groups3 = new JComboBox<>();
	    this.Existing_Groups3.setBounds(30, 65, 200, 23);
	    this.panel6.add(this.Existing_Groups3);
	    
	    this.SDS_group1 = new JButton("SDS group");
	    this.SDS_group1.setFont(new Font("Segoe UI", 1, 12));
	    this.SDS_group1.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent arg0) {
	            try {
	            	
	              String GroupURI = (String)clientUI.user.getGroupdictionary().get(clientUI.this.Existing_Groups3.getSelectedItem());
	              String SDSmessage = textArea1.getText();
//	              
	              clientUI.user.sendInvite(GroupURI, 70, SDSmessage, "group", "");
	               chatarea2.append(SDSmessage+"\n");
	               chatarea2.append("---group sds---\n");
	            } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
	          }
	        });
	    
	    this.SDS_group1.setBounds(364, 270, 180, 40);
	    this.panel6.add(this.SDS_group1);
	    this.SDS_group1.setEnabled(getenable());

	    this.SDS_one_to_one1 = new JButton("SDS one");
	    this.SDS_one_to_one1.setFont(new Font("Segoe UI", 1, 12));
	    this.SDS_one_to_one1.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent arg0) {
	            try {
//	            	InetAddress myAddress = InetAddress.getByName(localIp);
//		          	  System.out.println(myAddress.toString());
//		          	  Session activeSession = Session.create(false, false, myAddress);
//		          	  System.out.println(activeSession.toString());
		          	  String GroupURI = (String)clientUI.user.getGroupdictionary().get(clientUI.this.Existing_Groups3.getSelectedItem());
	              String SDSmessage = textArea1.getText();
	              String receiver = receiverarea1.getText();
	              
	              clientUI.user.sendInvite(GroupURI, 70, SDSmessage, "one", receiver);
	               chatarea3.append(SDSmessage+"\n");
	               chatarea3.append("---one-to-one sds---\n");
//	               netmsrp.exceptions.InternalErrorException
	            } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
	          }
	        });
	    this.SDS_one_to_one1.setBounds(364, 140, 180, 40);
	    this.panel6.add(this.SDS_one_to_one1);
	    this.SDS_one_to_one1.setEnabled(getenable());
	    
	    SDS_Refresh1 = new JButton();
	    //Deaff_Refresh.setIcon(new ImageIcon(getRefreshIcon()));
	    SDS_Refresh1.setText("Refresh");
	    SDS_Refresh1.setForeground(Color.BLACK);
	    SDS_Refresh1.setFont(new Font("Segoe UI", 1, 12));
	    SDS_Refresh1.setBackground(UIManager.getColor("Button.background"));
	    SDS_Refresh1.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	            try {
//	              List<String> grouplist = clientUI.user.getAllGroupDisplayName(clientUI.ServerIP);
//	              clientUI.this.Existing_Groups2.removeAllItems();
//	              for (int i = 1; i <= grouplist.size(); i++)
//	            	  clientUI.this.Existing_Groups2.addItem(grouplist.get(i - 1)); 
	              
	              
	              clientUI.this.Existing_Groups3.removeAllItems();
	              List<String> GetDirectInnfo = clientUI.user.getAffiliatedGroup(clientUI.username, clientUI.ServerIP);
	              if (GetDirectInnfo.size() != 0) {
	                try {
	                  Thread.sleep(500L);
	                } catch (InterruptedException e1) {
	                  e1.printStackTrace();
	                } 
	                HashMap<String, String> Groupdictionary = clientUI.user.getGroupdictionary();
	                for (int i = 1; i <= GetDirectInnfo.size(); i++) {
	                  String AffGroupURI = String.valueOf(GetDirectInnfo.get(i - 1)) + "@" + clientUI.ServerIP;
	                  List<String> Keys = clientUI.getKeyList(Groupdictionary, AffGroupURI);
	                  clientUI.this.Existing_Groups3.addItem(Keys.get(0));
	                } 
	              } else if (GetDirectInnfo.size() == 0) {
//	                JOptionPane.showMessageDialog(null, " < " + clientUI.username + " >" + " doesn't affiliate to any MCPTT groups.", "Waring: <" + clientUI.username + "> ", 2);
	              } 
//	              List<String> GetDirectInnfo = clientUI.user.getAffiliatedGroup(clientUI.username, clientUI.ServerIP);
	              
//	              if (GetDirectInnfo.size() != 0) {
//	                try {
//	                  Thread.sleep(500L);
//	                } catch (InterruptedException e1) {
//	                  e1.printStackTrace();
//	                } 
//	                HashMap<String, String> Groupdictionary = clientUI.user.getGroupdictionary();
//	                for (int j = 1; j <= GetDirectInnfo.size(); j++) {
//	                  String AffGroupURI = String.valueOf(GetDirectInnfo.get(j - 1)) + "@" + clientUI.ServerIP;
//	                  List<String> Keys = clientUI.getKeyList(Groupdictionary, AffGroupURI);
//	                 
//	                } 
//	              } 
	            } catch (IOException e1) {
	              e1.printStackTrace();
	            } 
	          }
		        });
	    SDS_Refresh1.setBounds(364, 252, 120, 23);
	    this.panel6.add(SDS_Refresh1);
	    SDS_Refresh1.setEnabled(true);
	    
	    this.panel7 = new JPanel();
	    tabbedPane.addTab("FD one", (Icon)null, this.panel7, (String)null);
	    this.panel7.setLayout((LayoutManager)null);
	    this.sendFDone = new JTextArea();
	    this.sendFDone.setWrapStyleWord(true);
	    this.sendFDone.setText("FD one to one.");
	    this.sendFDone.setLineWrap(true);
	    this.sendFDone.setForeground(Color.BLACK);
	    this.sendFDone.setFont(new Font("Segoe UI", 1, 20));
	    this.sendFDone.setEditable(false);
	    this.sendFDone.setDropMode(DropMode.INSERT);
	    this.sendFDone.setBackground(UIManager.getColor("Button.background"));
	    this.sendFDone.setBounds(0, 10, 150, 30);
	    this.panel7.add(this.sendFDone);
        
	    this.payloadcontentlabelfd = new JLabel();
	    this.payloadcontentlabelfd.setText("file URL");
	    this.payloadcontentlabelfd.setFont(new Font("Segoe UI", 1, 12));
	    this.payloadcontentlabelfd.setBorder((Border)null);
	    this.payloadcontentlabelfd.setBackground(SystemColor.menu);
	    this.payloadcontentlabelfd.setBounds(20, 180, 200, 21);
	    this.panel7.add(this.payloadcontentlabelfd);
	    
	    this.payloadcontentfilelabelfd = new JLabel();
	    this.payloadcontentfilelabelfd.setText("network file URL");
	    this.payloadcontentfilelabelfd.setFont(new Font("Segoe UI", 1, 12));
	    this.payloadcontentfilelabelfd.setBorder((Border)null);
	    this.payloadcontentfilelabelfd.setBackground(SystemColor.menu);
	    this.payloadcontentfilelabelfd.setBounds(20, 215, 200, 21);
	    this.panel7.add(this.payloadcontentfilelabelfd);
	    
	    this.checkboxfd = new JCheckBox();
	    this.checkboxfd.setBounds(260, 70, 20, 21);
	    this.checkboxlabelfd = new JLabel("app(payload destination type)");
        this.checkboxlabelfd.setBounds(290, 70, 200, 21);
        // 添加到 JFrame 中
        this.panel7.add(this.checkboxfd);
        this.panel7.add(this.checkboxlabelfd);
        
        this.checkboxfd2 = new JCheckBox();
	    this.checkboxfd2.setBounds(260, 90, 20, 21);
	    this.checkboxlabelfd2 = new JLabel("mandatory download");
        this.checkboxlabelfd2.setBounds(290, 90, 200, 21);
        // 添加到 JFrame 中
        this.panel7.add(this.checkboxfd2);
        this.panel7.add(this.checkboxlabelfd2);

        // 设置窗口可见
	    
	    this.receiverlabelfd = new JLabel();
	    this.receiverlabelfd.setText("receiver");
	    this.receiverlabelfd.setFont(new Font("Segoe UI", 1, 12));
	    this.receiverlabelfd.setBorder((Border)null);
	    this.receiverlabelfd.setBackground(SystemColor.menu);
	    this.receiverlabelfd.setBounds(30, 40, 200, 21);
	    this.panel7.add(this.receiverlabelfd);
	    
	    this.networkfilearea = new JTextArea(2, 20); 
	    this.networkfilearea.setVisible(true);
//	    this.receiverarea.setEditable(false);
	    this.networkfilearea.setBounds(30, 240, 160, 20);
	    this.panel7.add(this.networkfilearea);
	    
	    JButton selectFileButton = new JButton("選擇文件");
	    JTextField filePathField = new JTextField(20);
	    filePathField.setEditable(false);  // 让用户无法手动修改路径

	    selectFileButton.addActionListener(e -> {
	        JFileChooser fileChooser = new JFileChooser();
	        int returnValue = fileChooser.showOpenDialog(null);
	        if (returnValue == JFileChooser.APPROVE_OPTION) {
	            selectedFile = fileChooser.getSelectedFile();
	            filePathField.setText(selectedFile.getAbsolutePath());  // 显示文件路径
	        }
	    });
	    
	    JPanel filePanel = new JPanel();
	    filePanel.add(filePathField);
	    filePanel.add(selectFileButton);

	    filePanel.setBounds(20, 195, 300, 40);
	    this.panel7.add(filePanel);
	    
	    this.receiverareafd = new JTextArea(2, 20); // 5 行，20 列 receiver
	    this.receiverareafd.setVisible(true);
//	    this.receiverarea.setEditable(false);
	    this.receiverareafd.setBounds(30, 60, 160, 20);
	    this.panel7.add(this.receiverareafd);
	    
	    this.chatareafd1 = new JTextArea(10, 20); // 5 行，20 列 payload content
	    JScrollPane scrollPanefd3 = new JScrollPane(this.chatareafd1, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 添加滾動條
	    this.chatareafd1.setVisible(true);
	    this.chatareafd1.setLineWrap(true);  // 启用自动换行
	    this.chatareafd1.setWrapStyleWord(true);  // 在单词边界处换行
	    this.chatareafd1.setEditable(false);
	    scrollPanefd3.setBounds(30, 90, 160, 90);
	    this.panel7.add(scrollPanefd3);
	    


	    this.FD_one_to_one = new JButton("FD one");
	    this.FD_one_to_one.setFont(new Font("Segoe UI", 1, 12));
	    this.FD_one_to_one.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	            try {
//	              String GroupURI = (String)clientUI.user.getGroupdictionary().get(clientUI.this.Existing_Groupsfd2.getSelectedItem());
	              String receiver = receiverareafd.getText();
	              //if(SDSmessage.size()>000)
	              Boolean box;
	              if (!clientUI.this.checkboxfd.isSelected()) {
	                    box=true;
	                } else {
	                    box=false;
	                }
	              Boolean box2;
	              if (!clientUI.this.checkboxfd2.isSelected()) {
	                    box2=false;
	                } else {
	                    box2=true;
	                }
	              String conversationID_uuidAsString="";
	              if(true) {
	            	  UUID conversationID_uuid = UUID.randomUUID();
	        	      conversationID_uuidAsString = conversationID_uuid.toString();
	        	      conversationmapfd.put(conversationID_uuidAsString, "1");
	              }else {
	            	  conversationID_uuidAsString="";
	              }
	              clientUI.user.sendMESSAGE_FD("", selectedFile, "one", receiver, box, conversationID_uuidAsString, box2, serverstorefile);
	               chatareafd1.append(selectedFile.getName()+"\n");
	               chatareafd1.append("---one-to-one fd---\n");
	               
	            } catch (ParseException e) {
	              e.printStackTrace();
//	            } catch (ParseException e) {
//	              e.printStackTrace();
	            } catch (InvalidArgumentException e) {
	              e.printStackTrace();
	            } catch (SipException e) {
	              e.printStackTrace();
	            } 
	          }
	        });
	    this.FD_one_to_one.setBounds(330, 200, 100, 40);
	    this.panel7.add(this.FD_one_to_one);
	    this.FD_one_to_one.setEnabled(getenable());
	    
	    this.FD_one_to_one2 = new JButton("FD one");
	    this.FD_one_to_one2.setFont(new Font("Segoe UI", 1, 12));
	    this.FD_one_to_one2.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	            try {
//	              String GroupURI = (String)clientUI.user.getGroupdictionary().get(clientUI.this.Existing_Groupsfd2.getSelectedItem());
	              String receiver = receiverareafd.getText();
	              String fileurl = networkfilearea.getText();
	              //if(SDSmessage.size()>000)
	              Boolean box;
	              if (!clientUI.this.checkboxfd.isSelected()) {
	                    box=true;
	                } else {
	                    box=false;
	                }
	              Boolean box2;
	              if (!clientUI.this.checkboxfd2.isSelected()) {
	                    box2=false;
	                } else {
	                    box2=true;
	                }
	              String conversationID_uuidAsString="";
	              if(true) {
	            	  UUID conversationID_uuid = UUID.randomUUID();
	        	      conversationID_uuidAsString = conversationID_uuid.toString();
	        	      conversationmapfd.put(conversationID_uuidAsString, "1");
	              }else {
	            	  conversationID_uuidAsString="";
	              }
	              clientUI.user.sendMESSAGE_FD2("", "one", receiver, box, conversationID_uuidAsString, box2, fileurl, serverstorefile);
	               chatareafd1.append(selectedFile.getName()+"\n");
	               chatareafd1.append("---one-to-one fd---\n");
	               
	            } catch (ParseException e) {
	              e.printStackTrace();
//	            } catch (ParseException e) {
//	              e.printStackTrace();
	            } catch (InvalidArgumentException e) {
	              e.printStackTrace();
	            } catch (SipException e) {
	              e.printStackTrace();
	            } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
	          }
	        });
	    this.FD_one_to_one2.setBounds(210, 230, 100, 40);
	    this.panel7.add(this.FD_one_to_one2);
	    this.FD_one_to_one2.setEnabled(getenable());
	    
	    
	    this.panel8 = new JPanel();
	    tabbedPane.addTab("FD group", (Icon)null, this.panel8, (String)null);
	    this.panel8.setLayout((LayoutManager)null);
	    this.sendFDgroup = new JTextArea();
	    this.sendFDgroup.setWrapStyleWord(true);
	    this.sendFDgroup.setText("FD group");
	    this.sendFDgroup.setLineWrap(true);
	    this.sendFDgroup.setForeground(Color.BLACK);
	    this.sendFDgroup.setFont(new Font("Segoe UI", 1, 20));
	    this.sendFDgroup.setEditable(false);
	    this.sendFDgroup.setDropMode(DropMode.INSERT);
	    this.sendFDgroup.setBackground(UIManager.getColor("Button.background"));
	    this.sendFDgroup.setBounds(0, 10, 150, 30);
	    this.panel8.add(this.sendFDgroup);
        
	    this.payloadcontentlabelfdgroup = new JLabel();
	    this.payloadcontentlabelfdgroup.setText("file URL");
	    this.payloadcontentlabelfdgroup.setFont(new Font("Segoe UI", 1, 12));
	    this.payloadcontentlabelfdgroup.setBorder((Border)null);
	    this.payloadcontentlabelfdgroup.setBackground(SystemColor.menu);
	    this.payloadcontentlabelfdgroup.setBounds(20, 180, 200, 21);
	    this.panel8.add(this.payloadcontentlabelfdgroup);
	    
	    this.payloadcontentfilelabelfdgroup = new JLabel();
	    this.payloadcontentfilelabelfdgroup.setText("network file URL");
	    this.payloadcontentfilelabelfdgroup.setFont(new Font("Segoe UI", 1, 12));
	    this.payloadcontentfilelabelfdgroup.setBorder((Border)null);
	    this.payloadcontentfilelabelfdgroup.setBackground(SystemColor.menu);
	    this.payloadcontentfilelabelfdgroup.setBounds(20, 215, 200, 21);
	    this.panel8.add(this.payloadcontentfilelabelfdgroup);
	    
	    this.checkboxfdgroup = new JCheckBox();
	    this.checkboxfdgroup.setBounds(260, 70, 20, 21);
	    this.checkboxlabelfdgroup = new JLabel("app(payload destination type)");
        this.checkboxlabelfdgroup.setBounds(290, 70, 200, 21);
        // 添加到 JFrame 中
        this.panel8.add(this.checkboxfdgroup);
        this.panel8.add(this.checkboxlabelfdgroup);
        
        this.checkboxfdgroup2 = new JCheckBox();
	    this.checkboxfdgroup2.setBounds(260, 90, 20, 21);
	    this.checkboxlabelfdgroup2 = new JLabel("mandatory download");
        this.checkboxlabelfdgroup2.setBounds(290, 90, 200, 21);
        // 添加到 JFrame 中
        this.panel8.add(this.checkboxfdgroup2);
        this.panel8.add(this.checkboxlabelfdgroup2);
        
        this.networkfileareagroup = new JTextArea(2, 20); 
	    this.networkfileareagroup.setVisible(true);
//	    this.receiverarea.setEditable(false);
	    this.networkfileareagroup.setBounds(30, 240, 160, 20);
	    this.panel8.add(this.networkfileareagroup);
	    
	    JButton selectFileButton1 = new JButton("選擇文件");
	    JTextField filePathField1 = new JTextField(20);
	    filePathField1.setEditable(false);  // 让用户无法手动修改路径

	    selectFileButton1.addActionListener(e -> {
	        JFileChooser fileChooser1 = new JFileChooser();
	        int returnValue = fileChooser1.showOpenDialog(null);
	        if (returnValue == JFileChooser.APPROVE_OPTION) {
	            selectedFilegroup = fileChooser1.getSelectedFile();
	            filePathField1.setText(selectedFilegroup.getAbsolutePath());  // 显示文件路径
	        }
	    });
	    
	    JPanel filePanel1 = new JPanel();
	    filePanel1.add(filePathField1);
	    filePanel1.add(selectFileButton1);

	    filePanel1.setBounds(20, 195, 300, 40);
	    this.panel8.add(filePanel1);

	    this.chatareafd = new JTextArea(10, 20); // 5 行，20 列 payload content
	    JScrollPane scrollPanefd2 = new JScrollPane(this.chatareafd, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 添加滾動條
	    this.chatareafd.setVisible(true);
	    this.chatareafd.setLineWrap(true);  // 启用自动换行
	    this.chatareafd.setWrapStyleWord(true);  // 在单词边界处换行
	    this.chatareafd.setEditable(false);
	    scrollPanefd2.setBounds(30, 90, 160, 90);
	    this.panel8.add(scrollPanefd2);

	    this.txtMcpttGroup_fd2 = new JLabel();
	    this.txtMcpttGroup_fd2.setText("Affiliated Group(s)");
	    this.txtMcpttGroup_fd2.setFont(new Font("Segoe UI", 1, 12));
	    this.txtMcpttGroup_fd2.setBorder((Border)null);
	    this.txtMcpttGroup_fd2.setBackground(SystemColor.menu);
	    this.txtMcpttGroup_fd2.setBounds(30, 40, 200, 21);
	    this.panel8.add(this.txtMcpttGroup_fd2);
	    
	    this.Existing_Groupsfd2 = new JComboBox<>();
	    this.Existing_Groupsfd2.setBounds(30, 65, 200, 23);
	    this.panel8.add(this.Existing_Groupsfd2);
	    
	    this.FD_group = new JButton("FD group");
	    this.FD_group.setFont(new Font("Segoe UI", 1, 12));
	    this.FD_group.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	            try {
	              String GroupURI = (String)clientUI.user.getGroupdictionary().get(clientUI.this.Existing_Groupsfd2.getSelectedItem());

	              Boolean box;
	              if (!clientUI.this.checkboxfdgroup.isSelected()) {
	                    box=true;
	                } else {
	                    box=false;
	                }
	              Boolean box2;
	              if (!clientUI.this.checkboxfdgroup2.isSelected()) {
	                    box2=false;
	                } else {
	                    box2=true;
	                }
	              String conversationID_uuidAsString="";
	              if(true) {
	            	  UUID conversationID_uuid = UUID.randomUUID();
	        	      conversationID_uuidAsString = conversationID_uuid.toString();
	        	      conversationmapfd.put(conversationID_uuidAsString, "2");
	              }else {
	            	  conversationID_uuidAsString="";
	              }
	              clientUI.user.sendMESSAGE_FD(GroupURI, selectedFilegroup, "group", "", box, conversationID_uuidAsString, box2, serverstorefile);
	               chatareafd.append(selectedFilegroup.getName()+"\n");
	               chatareafd.append("---group fd---\n");
	            } catch (ParseException e) {
	              e.printStackTrace();
	            } catch (InvalidArgumentException e) {
	              e.printStackTrace();
	            } catch (SipException e) {
	              e.printStackTrace();
	            } 
	          }
	        });
	    
	    this.FD_group.setBounds(330, 200, 100, 40);
	    this.panel8.add(this.FD_group);
	    this.FD_group.setEnabled(getenable());   
	    
	    this.FD_group2 = new JButton("FD group");
	    this.FD_group2.setFont(new Font("Segoe UI", 1, 12));
	    this.FD_group2.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	            try {
	              String GroupURI = (String)clientUI.user.getGroupdictionary().get(clientUI.this.Existing_Groupsfd2.getSelectedItem());
	              String fileurl = networkfilearea.getText();
	              Boolean box;
	              if (!clientUI.this.checkboxfdgroup.isSelected()) {
	                    box=true;
	                } else {
	                    box=false;
	                }
	              Boolean box2;
	              if (!clientUI.this.checkboxfdgroup2.isSelected()) {
	                    box2=false;
	                } else {
	                    box2=true;
	                }
	              String conversationID_uuidAsString="";
	              if(true) {
	            	  UUID conversationID_uuid = UUID.randomUUID();
	        	      conversationID_uuidAsString = conversationID_uuid.toString();
	        	      conversationmapfd.put(conversationID_uuidAsString, "2");
	              }else {
	            	  conversationID_uuidAsString="";
	              }
	              clientUI.user.sendMESSAGE_FD2(GroupURI,  "group", "", box, conversationID_uuidAsString, box2, fileurl, serverstorefile);
	               chatareafd.append(selectedFilegroup.getName()+"\n");
	               chatareafd.append("---group fd---\n");
	            } catch (ParseException e) {
	              e.printStackTrace();
	            } catch (InvalidArgumentException e) {
	              e.printStackTrace();
	            } catch (SipException e) {
	              e.printStackTrace();
	            } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
	          }
	        });
	    
	    this.FD_group2.setBounds(210, 230, 100, 40);
	    this.panel8.add(this.FD_group2);
	    this.FD_group2.setEnabled(getenable());
	    
	    FD_Refresh = new JButton();
	    FD_Refresh.setText("Refresh");
	    FD_Refresh.setForeground(Color.BLACK);
	    FD_Refresh.setFont(new Font("Segoe UI", 1, 12));
	    FD_Refresh.setBackground(UIManager.getColor("Button.background"));
	    FD_Refresh.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	            try {

	              clientUI.this.Existing_Groupsfd2.removeAllItems();
	              List<String> GetDirectInnfo = clientUI.user.getAffiliatedGroup(clientUI.username, clientUI.ServerIP);
	              if (GetDirectInnfo.size() != 0) {
	                try {
	                  Thread.sleep(500L);
	                } catch (InterruptedException e1) {
	                  e1.printStackTrace();
	                } 
	                HashMap<String, String> Groupdictionary = clientUI.user.getGroupdictionary();
	                for (int i = 1; i <= GetDirectInnfo.size(); i++) {
	                  String AffGroupURI = String.valueOf(GetDirectInnfo.get(i - 1)) + "@" + clientUI.ServerIP;
	                  List<String> Keys = clientUI.getKeyList(Groupdictionary, AffGroupURI);
	                  clientUI.this.Existing_Groupsfd2.addItem(Keys.get(0));
	                } 
	              } else if (GetDirectInnfo.size() == 0) {
//	                JOptionPane.showMessageDialog(null, " < " + clientUI.username + " >" + " doesn't affiliate to any MCPTT groups.", "Waring: <" + clientUI.username + "> ", 2);
	              } 
	            } catch (IOException e1) {
	              e1.printStackTrace();
	            } 
	          }
		        });
	    FD_Refresh.setBounds(364, 252, 120, 23);
	    this.panel8.add(FD_Refresh);
	    FD_Refresh.setEnabled(true);
	    
	    
//	    panel5 = new JPanel();
//	    panel5.setLayout((LayoutManager)null);
//	    JTextArea txtrEnterGroupDisplay_1 = new JTextArea();
//	    txtrEnterGroupDisplay_1.setWrapStyleWord(true);
//	    txtrEnterGroupDisplay_1.setText("Enter MCPTT Group Name and Press \"Create\" Button.");
//	    txtrEnterGroupDisplay_1.setLineWrap(true);
//	    txtrEnterGroupDisplay_1.setForeground(Color.BLACK);
//	    txtrEnterGroupDisplay_1.setFont(new Font("Segoe UI", 1, 20));
//	    txtrEnterGroupDisplay_1.setEditable(false);
//	    txtrEnterGroupDisplay_1.setBackground(UIManager.getColor("Button.background"));
//	    txtrEnterGroupDisplay_1.setBounds(30, 10, 424, 80);
//	    panel5.add(txtrEnterGroupDisplay_1);
//	    this.txtExampleGroup = new JTextField("");
//	    this.txtExampleGroup.setFont(new Font("Segoe UI", 0, 12));
//	    this.txtExampleGroup.setHorizontalAlignment(2);
//	    this.txtExampleGroup.setForeground(Color.black);
//	    this.txtExampleGroup.setBounds(30, 114, 360, 40);
//	    panel5.add(this.txtExampleGroup);
//	    JButton btnNewButton_16 = new JButton("Create");
//	    btnNewButton_16.setFont(new Font("Segoe UI", 1, 14));
//	    btnNewButton_16.addActionListener(new ActionListener() {
//	          public void actionPerformed(ActionEvent arg0) {
//	            boolean correct = false;
//	            String str = clientUI.this.txtExampleGroup.getText();
//	            Pattern p = Pattern.compile("[^A-Za-z0-9]", 2);
//	            Matcher m = p.matcher(str);
//	            boolean c = m.find();
//	            Pattern pattern = Pattern.compile("\\s");
//	            Matcher matcher = pattern.matcher(str);
//	            boolean w = matcher.find();
//	            if (!c && !w) {
//	              if (clientUI.this.txtExampleGroup.getText().length() <= 20) {
//	                correct = true;
//	              } else {
//	                correct = false;
//	              } 
//	            } else {
//	              correct = false;
//	            } 
//	            if (correct) {
//	              List<String> grouplist = null;
//	              try {
//	                grouplist = clientUI.user.getAllGroupDisplayName(clientUI.ServerIP);
//	              } catch (ParseException e) {
//	                e.printStackTrace();
//	              } catch (IOException e) {
//	                e.printStackTrace();
//	              } 
//	              if (!grouplist.contains(clientUI.this.txtExampleGroup.getText())) {
//	                clientUI.user.CreatGroup(clientUI.ServerIP, clientUI.userURI, clientUI.this.txtExampleGroup.getText());
//	              } else {
//	                JOptionPane.showMessageDialog(null, "MCPTT group [" + clientUI.this.txtExampleGroup.getText() + "] " + 
//	                    "already exist. Please enter other MCPTT group name.", "Error: <" + clientUI.username + "> ", 0);
//	              } 
//	            } else {
//	              JOptionPane.showMessageDialog(null, "The length of MCPTT group name must be no more than 20 characters. Only a-z, A-Z, and 0-9 are acceptable.", "Error: <" + clientUI.username + "> ", 0);
//	            } 
//	          }
//	        });
//	    btnNewButton_16.setBounds(210, 188, 180, 40);
//	    panel5.add(btnNewButton_16);
//	    JLabel lblNewLabel = new JLabel("# No more than 20 characters. Only a-z, A-Z, and 0-9 are acceptable.");
//	    lblNewLabel.setForeground(new Color(0, 0, 0));
//	    lblNewLabel.setFont(new Font("PMingLiU", 0, 10));
//	    lblNewLabel.setBounds(30, 88, 424, 27);
//	    panel5.add(lblNewLabel);
	    panel2.addAncestorListener(new AncestorListener() {
	          public void ancestorAdded(AncestorEvent arg0) {
	            if (clientUI.user.getisRegister()) {
	            	clientUI.Aff_Refresh.setEnabled(clientUI.this.getenable());
	            	clientUI.this.Affiliate.setEnabled(clientUI.this.getenable());
	            	clientUI.this.Existing_Groups.setEnabled(clientUI.this.getenable());
	            	clientUI.this.srollpane.setEnabled(clientUI.this.getenable());
	            	clientUI.Aff_Refresh.doClick();
	            } else {
	            	clientUI.this.Affiliate.setEnabled(clientUI.this.getenable());
	            	clientUI.Aff_Refresh.setEnabled(clientUI.this.getenable());
	            	clientUI.this.Existing_Groups.setEnabled(clientUI.this.getenable());
	            	clientUI.this.srollpane.setEnabled(clientUI.this.getenable());
	              JOptionPane.showMessageDialog(null, "You haven't logged in yet.", "Error", 0);
	            } 
	          }
	          
	          public void ancestorMoved(AncestorEvent arg0) {}
	          
	          public void ancestorRemoved(AncestorEvent arg0) {}
	        });
	    
	    this.panel3.addAncestorListener(new AncestorListener() {
	          public void ancestorAdded(AncestorEvent arg0) {
	            if (clientUI.user.getisRegister()) {
	              clientUI.this.De_affiliate.setEnabled(clientUI.this.getenable());
	              clientUI.Deaff_Refresh.setEnabled(clientUI.this.getenable());
	              clientUI.this.Affiliated_Groups.setEnabled(clientUI.this.getenable());
	              clientUI.Deaff_Refresh.doClick();
	            } else {
	              clientUI.this.De_affiliate.setEnabled(clientUI.this.getenable());
	              clientUI.Deaff_Refresh.setEnabled(clientUI.this.getenable());
	              clientUI.this.Affiliated_Groups.setEnabled(clientUI.this.getenable());
	              JOptionPane.showMessageDialog(null, "You haven't logged in yet.", "Error", 0);
	            } 
	          }
	          
	          public void ancestorMoved(AncestorEvent arg0) {}
	          
	          public void ancestorRemoved(AncestorEvent arg0) {}
	        });
	    panel4.addAncestorListener(new AncestorListener() {
	          public void ancestorAdded(AncestorEvent arg0) {
	            if (clientUI.user.getisRegister()) {
	              clientUI.Session_Refresh.setEnabled(clientUI.this.getenable());
	              clientUI.this.Set_Up_Session.setEnabled(clientUI.this.getenable());
	              clientUI.this.Release_Session.setEnabled(clientUI.this.getenable());
	              clientUI.this.Session_Affiliated_Groups.setEnabled(clientUI.this.getenable());
	              clientUI.Session_Refresh.doClick();
	            } else {
	              clientUI.this.Set_Up_Session.setEnabled(clientUI.this.getenable());
	              clientUI.this.Release_Session.setEnabled(clientUI.this.getenable());
	              clientUI.Session_Refresh.setEnabled(clientUI.this.getenable());
	              clientUI.this.Session_Affiliated_Groups.setEnabled(clientUI.this.getenable());
	              JOptionPane.showMessageDialog(null, "You haven't logged in yet.", "Error", 0);
	            } 
	          }
	          
	          public void ancestorMoved(AncestorEvent arg0) {}
	          
	          public void ancestorRemoved(AncestorEvent arg0) {}
	        });
	    
	    
	    panel5.addAncestorListener(new AncestorListener() {
	          public void ancestorAdded(AncestorEvent arg0) {
	            if (clientUI.user.getisRegister()) {
	            	clientUI.SDS_Refresh.setEnabled(clientUI.this.getenable());
	            	clientUI.this.SDS_group.setEnabled(clientUI.this.getenable());
	            	clientUI.this.SDS_one_to_one.setEnabled(clientUI.this.getenable());
	            	clientUI.this.Existing_Groups2.setEnabled(clientUI.this.getenable());
	            	clientUI.this.srollpane.setEnabled(clientUI.this.getenable());
	            	clientUI.SDS_Refresh.doClick();
	            } else {
	            	clientUI.this.SDS_group.setEnabled(clientUI.this.getenable());
	            	clientUI.this.SDS_one_to_one.setEnabled(clientUI.this.getenable());
	            	clientUI.SDS_Refresh.setEnabled(clientUI.this.getenable());
	            	clientUI.this.Existing_Groups2.setEnabled(clientUI.this.getenable());
	            	clientUI.this.srollpane.setEnabled(clientUI.this.getenable());
	              JOptionPane.showMessageDialog(null, "You haven't logged in yet.", "Error", 0);
	            } 
	          }
	          
	          public void ancestorMoved(AncestorEvent arg0) {}
	          
	          public void ancestorRemoved(AncestorEvent arg0) {}
	        });
	    panel6.addAncestorListener(new AncestorListener() {
	          public void ancestorAdded(AncestorEvent arg0) {
	            if (clientUI.user.getisRegister()) {
	            	clientUI.SDS_Refresh1.setEnabled(clientUI.this.getenable());
	            	clientUI.this.SDS_group1.setEnabled(clientUI.this.getenable());
	            	clientUI.this.SDS_one_to_one1.setEnabled(clientUI.this.getenable());
	            	clientUI.this.Existing_Groups3.setEnabled(clientUI.this.getenable());
//	            	clientUI.this.srollpane1.setEnabled(clientUI.this.getenable());
	            	clientUI.SDS_Refresh1.doClick();
	            } else {
	            	clientUI.this.SDS_group1.setEnabled(clientUI.this.getenable());
	            	clientUI.this.SDS_one_to_one1.setEnabled(clientUI.this.getenable());
	            	clientUI.SDS_Refresh1.setEnabled(clientUI.this.getenable());
	            	clientUI.this.Existing_Groups3.setEnabled(clientUI.this.getenable());
//	            	clientUI.this.srollpane1.setEnabled(clientUI.this.getenable());
	              JOptionPane.showMessageDialog(null, "You haven't logged in yet.", "Error", 0);
	            } 
	          }
	          
	          public void ancestorMoved(AncestorEvent arg0) {}
	          
	          public void ancestorRemoved(AncestorEvent arg0) {}
	        });
	    
	    panel7.addAncestorListener(new AncestorListener() {
	          public void ancestorAdded(AncestorEvent arg0) {
	            if (clientUI.user.getisRegister()) {
//	            	clientUI.FD_Refresh.setEnabled(clientUI.this.getenable());
//	            	clientUI.this.FD_group.setEnabled(clientUI.this.getenable());
	            	clientUI.this.FD_one_to_one.setEnabled(clientUI.this.getenable());
	            	clientUI.this.FD_one_to_one2.setEnabled(clientUI.this.getenable());
//	            	clientUI.this.Existing_Groupsfd2.setEnabled(clientUI.this.getenable());
//	            	clientUI.this.srollpane1.setEnabled(clientUI.this.getenable());
//	            	clientUI.FD_Refresh.doClick();
	            } else {
//	            	clientUI.this.FD_group.setEnabled(clientUI.this.getenable());
	            	clientUI.this.FD_one_to_one.setEnabled(clientUI.this.getenable());
	            	clientUI.this.FD_one_to_one2.setEnabled(clientUI.this.getenable());
//	            	clientUI.FD_Refresh.setEnabled(clientUI.this.getenable());
//	            	clientUI.this.Existing_Groupsfd2.setEnabled(clientUI.this.getenable());
//	            	clientUI.this.srollpane1.setEnabled(clientUI.this.getenable());
	              JOptionPane.showMessageDialog(null, "You haven't logged in yet.", "Error", 0);
	            } 
	          }
	          
	          public void ancestorMoved(AncestorEvent arg0) {}
	          
	          public void ancestorRemoved(AncestorEvent arg0) {}
	        });
	    
	    panel8.addAncestorListener(new AncestorListener() {
	          public void ancestorAdded(AncestorEvent arg0) {
	            if (clientUI.user.getisRegister()) {
	            	clientUI.FD_Refresh.setEnabled(clientUI.this.getenable());
	            	clientUI.this.FD_group.setEnabled(clientUI.this.getenable());
	            	clientUI.this.FD_group2.setEnabled(clientUI.this.getenable());
//	            	clientUI.this.FD_one_to_one.setEnabled(clientUI.this.getenable());
	            	clientUI.this.Existing_Groupsfd2.setEnabled(clientUI.this.getenable());
//	            	clientUI.this.srollpane1.setEnabled(clientUI.this.getenable());
	            	clientUI.FD_Refresh.doClick();
	            } else {
	            	clientUI.this.FD_group.setEnabled(clientUI.this.getenable());
	            	clientUI.this.FD_group2.setEnabled(clientUI.this.getenable());
//	            	clientUI.this.FD_one_to_one.setEnabled(clientUI.this.getenable());
	            	clientUI.FD_Refresh.setEnabled(clientUI.this.getenable());
	            	clientUI.this.Existing_Groupsfd2.setEnabled(clientUI.this.getenable());
//	            	clientUI.this.srollpane1.setEnabled(clientUI.this.getenable());
	              JOptionPane.showMessageDialog(null, "You haven't logged in yet.", "Error", 0);
	            } 
	          }
	          
	          public void ancestorMoved(AncestorEvent arg0) {}
	          
	          public void ancestorRemoved(AncestorEvent arg0) {}
	        });
	  }
	  
	  public boolean getenable() {
	    return user.getisRegister();
	  }
//	  
//	  public void releasesession() {
//	    try {
//	      user.EmergencyStateCancel(user.GroupName, ServerIP);
//	      for (String key : SipClient.mediaSipSessionMap.keySet()) {
//	        MediaSipSession session = (MediaSipSession)SipClient.mediaSipSessionMap.get(key);
//	        user.closeSession(session);
//	      } 
//	      if (!SipClient.EMRmediaSipSessionMap.isEmpty()) {
//	        MediaSipSession EMRmediaSipSession = null;
//	        for (String key : SipClient.EMRmediaSipSessionMap.keySet()) {
//	          EMRmediaSipSession = (MediaSipSession)SipClient.EMRmediaSipSessionMap.get(key);
//	          user.sendEMRBye(EMRmediaSipSession);
//	        } 
//	      } 
//	    } catch (ParseException e) {
//	      e.printStackTrace();
//	    } catch (SipException e) {
//	      e.printStackTrace();
//	    } catch (ParserConfigurationException e) {
//	      e.printStackTrace();
//	    } catch (TransformerException e) {
//	      e.printStackTrace();
//	    } 
//	  }
//	  
	  public static int getXbound() {
	    int x = 0;
	    if (username.contains("EMCard")) {
	      String ind = username.split("EMCard")[1];
	      if (ind.equals("10")) {
	        x = 100;
	      } else if (ind.equals("11")) {
	        x = 150;
	      } else if (ind.equals("20")) {
	        x = 200;
	      } else if (ind.equals("21")) {
	        x = 250;
	      } else if (ind.equals("30")) {
	        x = 300;
	      } else if (ind.equals("31")) {
	        x = 350;
	      } else if (ind.equals("40")) {
	        x = 400;
	      } else if (ind.equals("41")) {
	        x = 450;
	      } else if (ind.equals("50")) {
	        x = 500;
	      } else if (ind.equals("51")) {
	        x = 550;
	      } 
	    } else {
	      x = 500;
	    } 
	    return x;
	  }
	  
	  public static int getYbound() {
	    int y = 0;
	    if (username.contains("EMCard")) {
	      String ind = username.split("EMCard")[1];
	      if (ind.equals("10")) {
	        y = 100;
	      } else if (ind.equals("11")) {
	        y = 150;
	      } else if (ind.equals("20")) {
	        y = 200;
	      } else if (ind.equals("21")) {
	        y = 250;
	      } else if (ind.equals("30")) {
	        y = 300;
	      } else if (ind.equals("31")) {
	        y = 350;
	      } else if (ind.equals("40")) {
	        y = 400;
	      } else if (ind.equals("41")) {
	        y = 450;
	      } else if (ind.equals("50")) {
	        y = 500;
	      } else if (ind.equals("51")) {
	        y = 550;
	      } 
	    } else {
	      y = 300;
	    } 
	    return y;
	  }
	  
	  public static List<String> getKeyList(HashMap<String, String> map, String value) {
	    List<String> keyList = new ArrayList<>();
	    for (String getKey : map.keySet()) {
	      if (((String)map.get(getKey)).equals(value))
	        keyList.add(getKey); 
	    } 
	    return keyList;
	  }
	  
	  public void setTitle(String title) {
	    Title = title;
	  }
	  
	  public static String getTitle() {
	    return Title;
	  }
	  
//	  private Image getRefreshIcon() {
//	    Image img = null;
//	    try {
//	      img = ImageIO.read(getClass().getResource("/res/refresh_4.png"));
//	    } catch (IOException e2) {
//	      e2.printStackTrace();
//	    } 
//	    return img;
//	  }
//	  
	  public static void settitle() {
	    if (user.GroupName != "") {
	      try {
	        user.getAllGroupDisplayName(ServerIP);
	      } catch (ParseException e) {
	        e.printStackTrace();
	      } catch (IOException e) {
	        e.printStackTrace();
	      } 
	      HashMap<String, String> Groupdictionary = user.getGroupdictionary();
	      String GroupURI = String.valueOf(user.GroupName) + "@" + ServerIP;
	      //List<String> Keys = getKeyList(Groupdictionary, GroupURI);
	      frmIwfUserInterface.setTitle("IWF: <> " + "Interface" + " <---> [] ");
	    } else {
	      frmIwfUserInterface.setTitle("IWF: < > " + "Interface");
	    } 
	  }
	  
	  public void propertyChange(PropertyChangeEvent evt) {
		  System.out.println("propertychange");
			  if ("MCDatasipMessage".equals(evt.getPropertyName())) {
				 
	            String[] newSipMessage = (String[]) evt.getNewValue();
	            String[] parts = newSipMessage[3].split("@");
//	            System.out.println("type "+newSipMessage[5]=="one-to-one-sds");
	            if(newSipMessage[5].equals("one-to-one-sds")) {
	            	chatarea1.append(newSipMessage[1]+"\n");
 	                chatarea1.append("<--sender : "+parts[0]+", type : "+newSipMessage[2]+", "+newSipMessage[0]+"-->\n");
	            }else {
	            	chatarea.append(newSipMessage[1]+"\n");
	 	            chatarea.append("<--sender : "+parts[0]+", type : "+newSipMessage[2]+", "+newSipMessage[0]+", "+newSipMessage[6]+"-->\n");
	            }        
	        }else if("MCDataSDSMessage".equals(evt.getPropertyName())){
	        	System.out.println("DataContainer 未设置，无法读取数据。");
	        	String[] parts = ((String)evt.getNewValue()).split(",");
	        	if(parts[1].equals("group")) {
	        		chatarea2.append(parts[0]);
	        	}else {
	        		chatarea3.append(parts[0]);
	        	}
	        }
	        if ("sipMessage".equals(evt.getPropertyName())) {
	            String newSipMessage = (String) evt.getNewValue();
	            this.receiverarea.setText(newSipMessage);  // 將接收到的SIP消息顯示到textArea
	        }
	        if ("MCDatasipMessageFD".equals(evt.getPropertyName())) {
	        	String[] newSipMessage = (String[]) evt.getNewValue();
	        	String[] parts = newSipMessage[3].split(",");

	            // 提取 "user"
	            String userPart = parts[0].replaceAll(".*?(user|application)", "$1").trim();

	            // 提取 "123"
	            String numberPart = parts[1].trim();
	            String fileName = numberPart.split("/")[numberPart.split("/").length - 1];
	            if(newSipMessage[1].equals("one-to-one-fd")) {
	            	chatareafd1.append(fileName+"\n");
	            	chatareafd1.append("<--"+userPart+","+newSipMessage[2]+"-->\n");
	            }else {
	            	chatareafd.append(fileName+"\n");//content
	            	chatareafd.append("<--"+userPart+","+newSipMessage[2]+"-->\n");//sender appor user
	            }  
	        }
	        if ("MCDatasipMessageFDnotification".equals(evt.getPropertyName())) {
	        	String[] newSipMessage = (String[]) evt.getNewValue();
	        	System.out.println("newSipMessage"+newSipMessage[0]);
	        	System.out.println("newSipMessage"+newSipMessage[1]);
	            // 提取 "123"
//	            String numberPart = parts[1].trim();
//	            if(newSipMessage[1].equals("one-to-one-fd")) {
//	            	chatareafd1.append(newSipMessage[1]+" "+newSipMessage[0]+"\n");
	            	chatareafd.append(newSipMessage[1]+" "+newSipMessage[0]+"\n");
//	            }else {
//	            	chatareafd.append(numberPart+"\n");//content
//	            	chatareafd.append("<--"+userPart+","+newSipMessage[2]+"-->\n");//sender appor user
//	            }  
	        }
	    }
	  
	  public synchronized static void showDownloadDialog(String fileName, SipClient sipClient) {
		    userMadeChoice = false;
		    startTimer(fileName, sipClient);

		    SwingUtilities.invokeLater(() -> {
		        String[] options = {"接受", "拒絕", "延後"};
		        int result = JOptionPane.showOptionDialog(
		                null,
		                "是否下載文件：" + fileName + "？",
		                "下載確認",
		                JOptionPane.DEFAULT_OPTION,
		                JOptionPane.QUESTION_MESSAGE,
		                null,
		                options,
		                options[0]
		        );

		        synchronized (sipClient) { // 使用 sipClient 作为锁对象
		        	System.out.println("[synchronized] "+result);
		            userMadeChoice = true;
		            if (timer != null) {
		                timer.cancel();
		            }

		            if (result == 0) {
		            	System.out.println("[ACCEPTED] "+SipClient.DownloadDecision.ACCEPTED);
		                sipClient.setDownloadDecision(SipClient.DownloadDecision.ACCEPTED);
		                System.out.println("【After setDownloadDecision】" + sipClient.getDownloadDecision());
		            } else if (result == 1) {
		            	System.out.println("[REJECTED]");
		                sipClient.setDownloadDecision(SipClient.DownloadDecision.REJECTED);
		            } else {
		            	System.out.println("[DEFERRED]");
		                sipClient.setDownloadDecision(SipClient.DownloadDecision.DEFERRED);
		            }
		            sipClient.notify(); // 唤醒等待的线程
		        }
		    });

		    synchronized (sipClient) {
		        while (!userMadeChoice) {
		            try {
		                sipClient.wait(); // 等待用户做出选择
		            } catch (InterruptedException e) {
		                Thread.currentThread().interrupt();
		            }
		        }
		    }
		}

	  private static void startTimer(String fileName, SipClient sipClient) {
	        timer = new Timer();
	        timer.schedule(new TimerTask() {
	            @Override
	            public void run() {
	                if (!userMadeChoice) {
	                    SwingUtilities.invokeLater(() -> {
	                        // **尝试关闭旧对话框**
	                        for (Window window : Window.getWindows()) {
	                            if (window instanceof JDialog) {
	                                window.dispose();  // 关闭旧的对话框
	                            }
	                        }

	                        // 超时后，只提供“接受”和“拒绝”选项
	                        String[] newOptions = {"接受", "拒絕"};

	                        int result = JOptionPane.showOptionDialog(
	                                null,
	                                "時間已到！請選擇下載或拒絕文件：" + fileName,
	                                "時間到期",
	                                JOptionPane.DEFAULT_OPTION,
	                                JOptionPane.WARNING_MESSAGE,
	                                null,
	                                newOptions,
	                                newOptions[0]
	                        );

	                        // 设置用户的选择
	                        if (result == 0) {
	                            sipClient.setDownloadDecision(SipClient.DownloadDecision.ACCEPTED);
	                        } else {
	                            sipClient.setDownloadDecision(SipClient.DownloadDecision.REJECTED);
	                        }
	                    });
	                }
	            }
	        }, TIMEOUT);
	    }

	  
//	  public interface DownloadCallback {
//	        void onUserDecision(boolean download);
//	    }
//
//	    public void showDownloadDialog(DownloadCallback callback) {
//	        int result = JOptionPane.showConfirmDialog(
//	            null, 
//	            "是否要下載文件？", 
//	            "文件下載", 
//	            JOptionPane.YES_NO_OPTION
//	        );
//	        callback.onUserDecision(result == JOptionPane.YES_OPTION);
//	    }
	}
