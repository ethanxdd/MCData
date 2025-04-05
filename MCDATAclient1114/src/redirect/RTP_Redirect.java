package redirect;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class RTP_Redirect extends Thread {
  public DatagramSocket receiveSocket;
  
  public DatagramSocket sendSocket;
  
  private int bufferSize;
  
  private boolean runThread = false;
  
  private int SocketPort;
  
  private String DstIp;
  
  private int DstPort;
  
  public networkcard IWFtoServer;
  
  public networkcard IWFtoEMR;
  
  public networkcard send;
  
  public networkcard recv;
  
  public static short SHRT_MAX = 32512;
  
  public static short SHRT_MIN = -32512;
  
  private AudioFormat format;
  
  private SourceDataLine speaker;
  
  public RTP_Redirect(int buffer_size, int port, String DstIp, int DstPort, networkcard card1, networkcard card2) {
    setBufferSize(buffer_size);
    setSocketPort(port);
    setDstIp(DstIp);
    setDstPort(DstPort);
    this.IWFtoServer = card1;
    this.IWFtoEMR = card2;
  }
  
  public boolean createDataSocket(networkcard recv, networkcard send, DatagramSocket rtpSocket, boolean b, DatagramSocket EMRRtpSocket) throws SocketException {
    this.send = send;
    this.recv = recv;
    if (b) {
      if (this.receiveSocket == null)
        this.receiveSocket = rtpSocket; 
      if (this.sendSocket == null)
        this.sendSocket = EMRRtpSocket; 
      return true;
    } 
    if (this.receiveSocket == null)
      this.receiveSocket = EMRRtpSocket; 
    if (this.sendSocket == null)
      this.sendSocket = rtpSocket; 
    return true;
  }
  
  public synchronized void start() {
    setThreadState(true);
    Thread();
  }
  
  private void Thread() {
    if (this.runThread) {
      Thread thread = new Thread(new Runnable() {
            public void run() {
              byte[] data = new byte[RTP_Redirect.this.bufferSize];
              DatagramPacket packet = new DatagramPacket(data, data.length);
              try {
                while (RTP_Redirect.this.runThread) {
                  RTP_Redirect.this.receiveSocket.receive(packet);
                  if (packet.getLength() == 0)
                    continue; 
                  RTP_Redirect.this.forward(packet, RTP_Redirect.this.sendSocket);
                } 
              } catch (IOException e) {
                e.printStackTrace();
              } 
            }
          });
      thread.start();
    } 
  }
  
  public void forward(DatagramPacket packet, DatagramSocket deliver) throws IOException {
    packet.setAddress(InetAddress.getByName(getDstIp()));
    packet.setPort(getDstPort());
    if (deliver != null)
      deliver.send(packet); 
  }
  
  public void clearsocket() {
    if (!this.receiveSocket.isClosed())
      this.receiveSocket.close(); 
    if (!this.sendSocket.isClosed())
      this.sendSocket.close(); 
  }
  
  public void stopThread() {
    this.runThread = false;
    if (!isInterrupted())
      interrupt(); 
  }
  
  public void setAudioFormat() {
    AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
    float rate = 8000.0F;
    int channels = 1;
    int sampleSize = 16;
    boolean bigEndian = false;
    this.format = new AudioFormat(encoding, rate, sampleSize, channels, sampleSize / 8 * channels, rate, bigEndian);
  }
  
  public void initPlayer() {
    try {
      this.speaker = AudioSystem.getSourceDataLine(this.format);
      this.speaker.open();
      this.speaker.start();
    } catch (LineUnavailableException e) {
      e.printStackTrace();
      System.exit(-1);
    } 
  }
  
  public void playsound(DatagramPacket packet) {
    try {
      byte[] buf = Arrays.copyOf(packet.getData(), 172);
      RtpParser parse = new RtpParser();
      parse.parse(buf);
      byte[] g711buffer = Arrays.copyOf(parse.getPayload(), (parse.getPayload()).length);
      g711buffer = parse.getPayload();
      byte[] pcmbuffer = null;
      pcmbuffer = convertG711ToPCM(g711buffer, g711buffer.length, pcmbuffer);
      this.speaker.write(pcmbuffer, 0, pcmbuffer.length);
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }
  
  private static String byteArrayToHexStr(byte[] byteArray) {
    if (byteArray == null)
      return null; 
    StringBuilder hex = new StringBuilder(byteArray.length * 2);
    byte b;
    int i;
    byte[] arrayOfByte;
    for (i = (arrayOfByte = byteArray).length, b = 0; b < i; ) {
      byte aData = arrayOfByte[b];
      hex.append(String.format("%02X", new Object[] { Byte.valueOf(aData) }));
      b++;
    } 
    String gethex = hex.toString();
    return gethex;
  }
  
  public static byte[] convertPCMtoG711(byte[] pcmbuffer, int length, byte[] g711Buffer) {
    length /= 2;
    if (pcmbuffer == null)
      g711Buffer = new byte[length]; 
    for (int i = 0; i < length; i++) {
      short pcm = getShort(pcmbuffer, i * 2);
      int sign = (pcm & 0x8000) >> 8;
      if (sign != 0)
        pcm = (short)-pcm; 
      if (pcm > SHRT_MAX)
        pcm = SHRT_MAX; 
      int exponent = 7;
      for (int expMask = 16384; (pcm & expMask) == 0 && exponent > 0; ) {
        exponent--;
        expMask >>= 1;
      } 
      int mantissa = pcm >> ((exponent == 0) ? 4 : (exponent + 3)) & 0xF;
      byte alaw = (byte)(sign | exponent << 4 | mantissa);
      g711Buffer[i] = (byte)(alaw ^ 0xD5);
    } 
    return g711Buffer;
  }
  
  public static byte[] convertG711ToPCM(byte[] g711Buffer, int length, byte[] pcmBuffer) {
    if (pcmBuffer == null)
      pcmBuffer = new byte[length * 2]; 
    for (int i = 0; i < length; i++) {
      byte alaw = g711Buffer[i];
      alaw = (byte)(int)(alaw ^ 0xFFL);
      int sign = alaw & 0x80;
      int exponent = (alaw & 0x70) >> 4;
      int value = (alaw & 0xF) >> 12;
      if (exponent != 0)
        value += 256; 
      if (exponent > 1)
        value <<= exponent - 1; 
      value = (char)(((sign == 0) ? value : -value) & Character.MAX_VALUE);
      pcmBuffer[i * 2 + 0] = (byte)(value & 0xFF);
      pcmBuffer[i * 2 + 1] = (byte)(value >> 8 & 0xFF);
    } 
    return pcmBuffer;
  }
  
  private static short getShort(byte[] src, int start) {
    return (short)(src[start] & 0xFF | src[start + 1] << 8);
  }
  
  private static byte[] amplifyPCMData(byte[] src, int nLen, byte[] dest, int nBitsPerSample, float multiple) {
    int nCur = 0;
    if (16 == nBitsPerSample)
      while (nCur < nLen) {
        short volum = getShort(src, nCur);
        volum = (short)(int)(volum * multiple);
        if (volum < SHRT_MIN) {
          volum = SHRT_MIN;
        } else if (volum > SHRT_MAX) {
          volum = SHRT_MAX;
        } 
        dest[nCur] = (byte)(volum & 0xFF);
        dest[nCur + 1] = (byte)(volum >> 8 & 0xFF);
        nCur += 2;
      }  
    return dest;
  }
  
  public int getSocketPort() {
    return this.SocketPort;
  }
  
  public void setSocketPort(int socketPort) {
    this.SocketPort = socketPort;
  }
  
  public String getDstIp() {
    return this.DstIp;
  }
  
  public void setDstIp(String dstIp) {
    this.DstIp = dstIp;
  }
  
  public int getDstPort() {
    return this.DstPort;
  }
  
  public void setDstPort(int dstPort) {
    this.DstPort = dstPort;
  }
  
  public int getBufferSize() {
    return this.bufferSize;
  }
  
  public void setBufferSize(int Size) {
    this.bufferSize = Size;
  }
  
  public boolean getThreadState() {
    return this.runThread;
  }
  
  public void setThreadState(boolean state) {
    this.runThread = state;
  }
  
  public RTP_Redirect() {}
}
