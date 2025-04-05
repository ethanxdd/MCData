package media.rtplib;

public interface RTPAppIntf {
  void receiveData(DataFrame paramDataFrame, Participant paramParticipant);
  
  void userEvent(int paramInt, Participant[] paramArrayOfParticipant);
  
  int frameSize(int paramInt);
}


/* Location:              C:\Users\ethan\Downloads\User.jar!\media\rtplib\RTPAppIntf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */