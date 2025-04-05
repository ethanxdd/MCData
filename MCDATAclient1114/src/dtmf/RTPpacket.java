package dtmf;

import java.util.Random;

public class RTPpacket {
  private static Random sRandom = new Random();
  
  private byte[] packet;
  
  private int packetLength;
  
  public RTPpacket(byte[] buffer, int event, boolean marker, boolean end, int duration) {
    this.packet = buffer;
    setVersion(2);
    if (marker) {
      setPayloadType(229, marker);
    } else {
      setPayloadType(101, marker);
    } 
    setSequenceNumber(sRandom.nextInt());
    setSscr(sRandom.nextLong());
    if (end) {
      setDTMFevent(event, 1, 0, 10, duration);
    } else {
      setDTMFevent(event, 0, 0, 10, duration);
    } 
  }
  
  public byte[] getRawPacket() {
    return this.packet;
  }
  
  public int getPacketLength() {
    return this.packetLength;
  }
  
  public int getHeaderLength() {
    return 12 + 4 * getCscrCount();
  }
  
  public int getPayloadLength() {
    return this.packetLength - getHeaderLength();
  }
  
  public void setPayloadLength(int length) {
    this.packetLength = getHeaderLength() + length;
  }
  
  public int getVersion() {
    return this.packet[0] >> 6 & 0x3;
  }
  
  public void setVersion(int v) {
    if (v > 3)
      throw new RuntimeException("illegal version: " + v); 
    this.packet[0] = (byte)(this.packet[0] & 0x3F | (v & 0x3) << 6);
  }
  
  int getCscrCount() {
    return this.packet[0] & 0xF;
  }
  
  public int getPayloadType() {
    return this.packet[1] & Byte.MAX_VALUE;
  }
  
  public void setPayloadType(int pt, boolean marker) {
    if (!marker) {
      this.packet[1] = (byte)(this.packet[1] & 0x80 | pt & 0x7F);
    } else {
      this.packet[1] = (byte)pt;
    } 
  }
  
  public int getSequenceNumber() {
    return (int)get(2, 2);
  }
  
  public void setSequenceNumber(int sn) {
    set(sn, 2, 2);
  }
  
  public long getTimestamp() {
    return get(4, 4);
  }
  
  public void setTimestamp(long timestamp) {
    set(timestamp, 4, 4);
  }
  
  void setSscr(long ssrc) {
    set(ssrc, 8, 4);
  }
  
  private long get(int begin, int length) {
    long n = 0L;
    for (int i = begin, end = i + length; i < end; i++)
      n = n << 8L | this.packet[i] & 0xFFL; 
    return n;
  }
  
  private void set(long n, int begin, int length) {
    for (int i = begin + length - 1; i >= begin; i--) {
      this.packet[i] = (byte)(int)(n & 0xFFL);
      n >>= 8L;
    } 
  }
  
  public void setDTMFevent(int event, int E, int R, int volume, int duration) {
    set(event, 12, 1);
    if (E != 0) {
      this.packet[13] = -118;
    } else {
      this.packet[13] = 10;
    } 
    set(duration, 14, 2);
  }
}
