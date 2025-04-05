/*     */ package sip;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.DatagramSocket;
/*     */ import proxy.Proxy;
/*     */ 
/*     */ public class rtpdetect
/*     */   extends Thread
/*     */ {
/*  11 */   private int bufferSize = 1024;
/*     */   private boolean runThread = false;
/*     */   private DatagramSocket NIC1Socket;
/*     */   private DatagramSocket NIC2Socket;
/*  15 */   private Proxy proxy = Proxy.getInstance();
/*  16 */   private int EMC_IWF = 0;
/*  17 */   private int SERVER_IWF = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNIC1Socket(DatagramSocket NIC1) {
/*  24 */     this.NIC1Socket = NIC1;
/*  25 */     System.out.println("[rtpdetect] NIC1Socket = " + this.NIC1Socket);
/*     */   }
/*     */   
/*     */   public void setNIC2Socket(DatagramSocket NIC2) {
/*  29 */     this.NIC2Socket = NIC2;
/*  30 */     System.out.println("[rtpdetect] NIC2Socket = " + this.NIC2Socket);
/*     */   }
/*     */   
/*     */   public synchronized void start() {
/*  34 */     setThreadState(true);
/*  35 */     Thread();
/*     */   }
/*     */   
/*     */   private void Thread() {
/*  39 */     if (this.runThread) {
/*  40 */       Thread thread = new Thread(new Runnable()
/*     */           {
/*     */             public void run()
/*     */             {
/*  44 */               byte[] data1 = new byte[rtpdetect.this.bufferSize];
/*  45 */               DatagramPacket packet1 = new DatagramPacket(data1, data1.length);
/*     */               
/*  47 */               byte[] data2 = new byte[rtpdetect.this.bufferSize];
/*  48 */               DatagramPacket packet2 = new DatagramPacket(data2, data2.length);
/*     */               try {
/*  50 */                 int i = 0;
/*  51 */                 while (rtpdetect.this.runThread) {
/*  52 */                   i++;
/*  53 */                   System.out.println("[rtpdetect] i = " + i);
/*  54 */                   rtpdetect.this.NIC1Socket.receive(packet1);
/*  55 */                   if (packet1.getLength() != 0) {
/*  56 */                     int direction = (rtpdetect.this.proxy.getRTCPProxy().getRTCPThread().getHandler()).direction;
/*  57 */                     if (direction != 0) {
/*  58 */                       rtpdetect.this.SERVER_IWF = rtpdetect.this.SERVER_IWF + 1;
/*     */                     } else {
/*  60 */                       System.out.println("[rtpdetect] NIC1Socket direction is correct");
/*     */                     } 
/*     */                   } else {
/*  63 */                     System.out.println("[rtpdetect] NIC1Socket doesn't receive data");
/*     */                   } 
/*     */                   
/*  66 */                   rtpdetect.this.NIC2Socket.receive(packet2);
/*  67 */                   if (packet2.getLength() != 0) {
/*  68 */                     int direction = (rtpdetect.this.proxy.getRTCPProxy().getRTCPThread().getHandler()).direction;
/*  69 */                     if (direction != 1) {
/*  70 */                       rtpdetect.this.EMC_IWF = rtpdetect.this.EMC_IWF + 1;
/*     */                     } else {
/*  72 */                       System.out.println("[rtpdetect] NIC2Socket direction is correct");
/*     */                     } 
/*     */                   } else {
/*  75 */                     System.out.println("[rtpdetect] NIC2Socket doesn't receive data");
/*     */                   } 
/*     */                   
/*  78 */                   System.out.println("[rtpdetect] EMC_IWF = " + rtpdetect.this.EMC_IWF);
/*  79 */                   System.out.println("[rtpdetect] SERVER_IWF = " + rtpdetect.this.SERVER_IWF);
/*     */                 } 
/*  81 */                 System.out.println("[rtpdetect] EMC_IWF = " + rtpdetect.this.EMC_IWF);
/*  82 */                 System.out.println("[rtpdetect] SERVER_IWF = " + rtpdetect.this.SERVER_IWF);
/*  83 */               } catch (IOException e) {
/*  84 */                 e.printStackTrace();
/*     */               } 
/*     */             }
/*     */           });
/*  88 */       thread.start();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void stopThread() {
/*  93 */     this.runThread = false;
/*  94 */     if (!isInterrupted()) {
/*  95 */       interrupt();
/*  96 */       System.out.println("[rtpdetect][stopThread()] interrupt......");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getThreadState() {
/* 102 */     return this.runThread;
/*     */   }
/*     */   
/*     */   public void setThreadState(boolean state) {
/* 106 */     this.runThread = state;
/*     */   }
/*     */ }


/* Location:              C:\Users\ethan\Downloads\User.jar!\sip\rtpdetect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */