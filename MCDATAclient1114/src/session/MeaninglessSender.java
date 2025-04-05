/*    */ package session;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.DatagramPacket;
/*    */ import java.net.DatagramSocket;
/*    */ import java.net.InetAddress;
/*    */ import java.net.UnknownHostException;
/*    */ import java.util.Timer;
/*    */ import java.util.TimerTask;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MeaninglessSender
/*    */ {
/*    */   private Timer timer;
/*    */   private long timePeriod;
/*    */   private String remoteIp;
/*    */   private int remotePort;
/*    */   private DatagramSocket udpSocket;
/*    */   private String tag;
/*    */   private boolean isStarted;
/*    */   
/*    */   public MeaninglessSender(DatagramSocket udpSocket, String remoteIp, int remotePort) {
/* 25 */     this.udpSocket = udpSocket;
/* 26 */     this.remoteIp = remoteIp;
/* 27 */     this.remotePort = remotePort;
/* 28 */     init();
/*    */   }
/*    */   
/*    */   private void init() {
/* 32 */     this.isStarted = false;
/* 33 */     this.timePeriod = 20000L;
/* 34 */     this.tag = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 39 */     if (!this.isStarted) {
/* 40 */       this.timer = new Timer();
///* 41 */       this.timer.schedule(new SenderTimerTask(null), 0L, this.timePeriod);
/* 42 */       this.isStarted = true;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void stop() {
/* 49 */     if (this.isStarted) {
/* 50 */       this.isStarted = false;
/* 51 */       this.timer.cancel();
/* 52 */       this.timer = null;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void setTag(String tag) {
/* 58 */     this.tag = tag;
/*    */   }
/*    */   
/*    */   public long getTimePeriod() {
/* 62 */     return this.timePeriod;
/*    */   }
/*    */   
/*    */   public void setTimePeriod(long timePeriod) {
/* 66 */     this.timePeriod = timePeriod;
/*    */   }
/*    */   
/*    */   private class SenderTimerTask
/*    */     extends TimerTask {
/*    */     public void run() {
/* 72 */       DatagramPacket packet = new DatagramPacket(new byte[0], 0);
/*    */       
/*    */       try {
/* 75 */         packet.setAddress(InetAddress.getByName(MeaninglessSender.this.remoteIp));
/* 76 */         packet.setPort(MeaninglessSender.this.remotePort);
/* 77 */         MeaninglessSender.this.udpSocket.send(packet);
/* 78 */       } catch (UnknownHostException e) {
/* 79 */         e.printStackTrace();
/* 80 */       } catch (IOException e) {
/* 81 */         e.printStackTrace();
/* 82 */         MeaninglessSender.this.stop();
/*    */       } 
/*    */     }
/*    */     
/*    */     private SenderTimerTask() {}
/*    */   }
/*    */   
/*    */   public void printInfo() {}
/*    */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\session\MeaninglessSender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */