package media.rtplib;

public interface RTCPAppIntf {
  void SRPktReceived(long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5, long paramLong6, long[] paramArrayOflong1, int[] paramArrayOfint1, int[] paramArrayOfint2, long[] paramArrayOflong2, long[] paramArrayOflong3, long[] paramArrayOflong4, long[] paramArrayOflong5);
  
  void RRPktReceived(long paramLong, long[] paramArrayOflong1, int[] paramArrayOfint1, int[] paramArrayOfint2, long[] paramArrayOflong2, long[] paramArrayOflong3, long[] paramArrayOflong4, long[] paramArrayOflong5);
  
  void SDESPktReceived(Participant[] paramArrayOfParticipant);
  
  void BYEPktReceived(Participant[] paramArrayOfParticipant, String paramString);
  
  void APPPktReceived(Participant paramParticipant, int paramInt, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
}


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\rtplib\RTCPAppIntf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */