package redirect;

public class RtpParser {
  static final int RTP_HEADER_LENGTH = 12;
  
  byte[] payload;
  
  int payloadType;
  
  public void parse(byte[] data) throws Exception {
    try {
      if (data.length > 12) {
        this.payloadType = (byte)(data[1] & 0xFF & 0x7F);
        this.payload = new byte[data.length - 12];
        for (int i = 0; i < this.payload.length; i++)
          this.payload[i] = data[i + 12]; 
      } 
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception(" invalid data for parsing ");
    } 
  }
  
  public int getPayloadType() {
    return this.payloadType;
  }
  
  public byte[] getPayload() {
    return this.payload;
  }
}
