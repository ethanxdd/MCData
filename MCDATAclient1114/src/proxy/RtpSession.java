package proxy;

import java.net.InetAddress;

public interface RtpSession {
  int getCodecId();
  
  String getName();
  
  int getSampleRate();
  
  void startSending();
  
  void startReceiving();
  
  void setAdjustMicGainFactor(int paramInt);
  
  void setRemoteTarget(InetAddress paramInetAddress, int paramInt);
  
  void stop();
  
  int toggleMute();
  
  boolean isMuted();
  
  void sendDtmf();
}
