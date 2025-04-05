package list;

import java.util.ArrayList;
import java.util.List;

public class RemoteInfo {
  public static final String SIP_ADDR = "sipAddr";
  
  public static final String DISPLAYNAME = "displayName";
  
  private String sipAddr;
  
  private String displayName;
  
  public ArrayList<String> usersInGroup = new ArrayList<>();
  
  public String getSipAddr() {
    return this.sipAddr;
  }
  
  public void setSipAddr(String sipAddr) {
    this.sipAddr = sipAddr;
  }
  
  public String getDisplayName() {
    return this.displayName;
  }
  
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }
  
  public List<String> getUserEntries() {
    return this.usersInGroup;
  }
  
  public String toString() {
    return String.valueOf(this.displayName) + this.sipAddr;
  }
}
