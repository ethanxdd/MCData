package ui;

public class client_parameters {
	 public String MCPTTUserName;
	  
	  public String MCPTTServerDomain;
	  
	  public String MCPTTUserlocalIp;
	  
	  public String LMRUserId;
	  
	  public String LMRLocalIp;
	  
	  public String LMRLocalPort;
	  
	  public String EMRCardNo;
	  
	  public String EMRIp;
	  
	  public String Active;
	  
	  public client_parameters() {}
	  
	  public client_parameters(String MCPTTusername, String MCPTTServerdomain, String MCPTTuserIp, String LMRuserId, String LMRlocalIp, String LMRlocalport, String EMRcardno, String EMRip, String active) {
	    this.MCPTTUserName = MCPTTusername;
	    this.MCPTTServerDomain = MCPTTServerdomain;
	    this.MCPTTUserlocalIp = MCPTTuserIp;
	    this.LMRUserId = LMRuserId;
	    this.LMRLocalIp = LMRlocalIp;
	    this.LMRLocalPort = LMRlocalport;
	    this.EMRCardNo = EMRcardno;
	    this.EMRIp = EMRip;
	    this.Active = active;
	  }
	  
	  public void printAll() {
	    System.out.println("MCPTT User Name :" + this.MCPTTUserName);
	    System.out.println("MCPTT Server Domain :" + this.MCPTTServerDomain);
	    System.out.println("MCPTT User localIp :" + this.MCPTTUserlocalIp);
	    System.out.println("LMR User Id :" + this.LMRUserId);
	    System.out.println("LMR Local Ip :" + this.LMRLocalIp);
	    System.out.println("LMR Local port :" + this.LMRLocalPort);
	    System.out.println("EMR Card Number :" + this.EMRCardNo);
	    System.out.println("EMR Ip :" + this.EMRIp);
	    System.out.println("Active :" + this.Active);
	  }
	}