package media.rtplib;

public interface RTCPAVPFIntf {
  void PSFBPktPictureLossReceived(long paramLong);
  
  void PSFBPktSliceLossIndic(long paramLong, int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3);
  
  void PSFBPktRefPictureSelIndic(long paramLong, int paramInt1, byte[] paramArrayOfbyte, int paramInt2);
  
  void PSFBPktAppLayerFBReceived(long paramLong, byte[] paramArrayOfbyte);
  
  void RTPFBPktReceived(long paramLong, int paramInt, int[] paramArrayOfint1, int[] paramArrayOfint2);
}


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\rtplib\RTCPAVPFIntf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */