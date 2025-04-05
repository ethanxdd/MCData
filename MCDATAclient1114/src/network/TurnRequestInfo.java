package network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.zip.CRC32;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.ice4j.StunException;
import org.ice4j.StunMessageEvent;
import org.ice4j.StunResponseEvent;
import org.ice4j.Transport;
import org.ice4j.TransportAddress;
import org.ice4j.attribute.Attribute;
import org.ice4j.attribute.AttributeFactory;
import org.ice4j.attribute.ErrorCodeAttribute;
import org.ice4j.attribute.MessageIntegrityAttribute;
import org.ice4j.attribute.NonceAttribute;
import org.ice4j.attribute.RealmAttribute;
import org.ice4j.attribute.RequestedTransportAttribute;
import org.ice4j.attribute.XorMappedAddressAttribute;
import org.ice4j.attribute.XorRelayedAddressAttribute;
import org.ice4j.ice.Agent;
//import org.ice4j.ice.Agent;
import org.ice4j.ice.harvest.CandidateHarvester;
import org.ice4j.ice.harvest.StunCandidateHarvester;
import org.ice4j.ice.harvest.TurnCandidateHarvester;
import org.ice4j.message.Message;
import org.ice4j.message.MessageFactory;
import org.ice4j.message.Request;
import org.ice4j.socket.IceSocketWrapper;
import org.ice4j.socket.IceTcpSocketWrapper;
import org.ice4j.stack.MessageEventHandler;
import org.ice4j.stack.StunStack;
import org.ice4j.stack.TransactionID;
import org.ice4j.stunclient.BlockingRequestSender;

import turnserver.collectors.AllocationResponseCollector;
import turnserver.stack.TurnStack;

public class TurnRequestInfo {
  private DatagramSocket tcpSocket;
  
  private TransactionID transactionID;
  
  private String turnServerAddr;
  
  private String turnServerHost;
  
  private int turnServerPort;
  
  private String mappedHost;
  
  private int mappedPort;
  
  private String responseHost;
  
  private int responsePort;
  
  private int retryCount;
  
  private boolean isEstablishmentSuccess;
  
  private String turnUsername;  // TURN 用户名
  
  private String turnPassword;  // TURN 密码
  
  
  private static BlockingRequestSender requestSender;

  private static IceTcpSocketWrapper sock;

  private static TurnStack turnStack;

  private static TransportAddress localAddress;

  private static TransportAddress serverAddress;

  private static boolean started;

  private static Socket tcpSocketToServer = null;
  
  public TurnRequestInfo(DatagramSocket socket, String turnServerAddr, String username, String password) {
    this.tcpSocket = socket;
    this.turnServerAddr = turnServerAddr;
    this.turnUsername = username;
    this.turnPassword = password;
    this.isEstablishmentSuccess = false;
    this.retryCount = 0;
    initTransactionId();
    initStunServer();
  }
  
  private void initStunServer() {
    String host;
    int port;
    String[] pair = this.turnServerAddr.split(":");
    if (pair.length == 2) {
      host = pair[0];
      port = Integer.parseInt(pair[1]);
    } else {
      host = this.turnServerAddr;
      port = 3478;
    } 
    this.turnServerHost = host;
    this.turnServerPort = port;
  }
  
  private void initTransactionId() {
    this.transactionID = TransactionID.createNewTransactionID();
  }
  
  
  public void establish2() {
	    System.out.println("begin");
	    this.isEstablishmentSuccess = false;
	    
	    try {
	        String host = this.turnServerHost;
	        int port = this.turnServerPort;
//	        String username = this.turnUsername;
	        String username = "mcdata";
	        String password = this.turnPassword;
	        TransactionID id = this.transactionID;
	        DatagramSocket socket = this.tcpSocket;
	        System.out.println("username: "+username);
	        System.out.println("password: "+password);

	        Agent agent = new Agent();
	        System.out.println("step0.5");

	        // 添加 TURN CandidateHarvester，支持用户名
	        agent.addCandidateHarvester(new TurnCandidateHarvester(
	            new TransportAddress(host, port, Transport.UDP), 
	            username
	        ));

	        System.out.println("step1");

	     // 构造 Binding Request 消息
	        ByteBuffer bindingBuffer = ByteBuffer.allocate(512);
	        bindingBuffer.putShort((short) 0x0001); // Binding Request 类型
	        bindingBuffer.putShort((short) 0);      // 消息长度先占位（后续计算填充）
	        bindingBuffer.putInt(0x2112A442);         // Magic Cookie

	        // 生成一个新的 Transaction ID（或你可以重用现有的，但建议单独生成一个，用于区分不同请求）
	        TransactionID bindingTransactionID = TransactionID.createNewTransactionID();  // 假设 TransactionID 类生成随机12字节数据
	        bindingBuffer.put(bindingTransactionID.getBytes());

	        // 如果有其他属性，可在此添加（例如 Fingerprint、NONCE 等），这里暂略

	        // 计算实际消息长度并回填到消息头
	        int bindingMessageLength = bindingBuffer.position() - 20; // 除去 STUN 头部 20 字节
	        bindingBuffer.putShort(2, (short) bindingMessageLength);

	        // 将 ByteBuffer 中的数据转为字节数组，实际发送的消息就是此数组
	        byte[] bindingRequest = Arrays.copyOf(bindingBuffer.array(), bindingBuffer.position());
	        
	        
	     // 假设 host、port 已定义，socket 是已初始化的 DatagramSocket 对象
	        InetAddress serverAddress = InetAddress.getByName(host);
	        DatagramPacket bindingPacket = new DatagramPacket(bindingRequest, bindingRequest.length, serverAddress, port);

	        // 发送 Binding Request
	        socket.send(bindingPacket);
	        System.out.println("Binding Request 已发送");

	        // 准备接收响应
	        byte[] recvBuffer = new byte[512];
	        DatagramPacket responsePacket1 = new DatagramPacket(recvBuffer, recvBuffer.length);

	        // 设置接收超时，例如 3000 毫秒
	        socket.setSoTimeout(3000);
	        try {
	            socket.receive(responsePacket1);
	            System.out.println("收到 Binding Response");
	            
	            // 解析响应：通常你可以检查响应的 Transaction ID 是否与发送的匹配，
	            // 并从响应中提取 XOR-MAPPED-ADDRESS 等属性。
	            ByteBuffer respBuffer = ByteBuffer.wrap(responsePacket1.getData(), 0, responsePacket1.getLength());
	            
	            short respType = respBuffer.getShort();
	            short respLength = respBuffer.getShort();
	            int magicCookie = respBuffer.getInt();
	            byte[] respTransactionID = new byte[12];
	            respBuffer.get(respTransactionID);
	            
	            // 检查 Transaction ID 是否匹配
	            if (Arrays.equals(respTransactionID, bindingTransactionID.getBytes())) {
	                System.out.println("Transaction ID 匹配");
	                // 可继续解析属性，例如 XOR-MAPPED-ADDRESS（属性类型 0x0020）
	                // 这里可以根据需要解析更多属性
	            } else {
	                System.out.println("Transaction ID 不匹配，可能是其他响应");
	            }
	            
	        } catch (SocketTimeoutException e) {
	            System.out.println("等待 Binding Response 超时");
	        }
	        //===========================
	        // 创建初次 Allocate 请求（不带认证）
	        ByteBuffer buffer = ByteBuffer.allocate(512);
	        buffer.putShort((short) 0x0003); // TURN Allocate Request
	        buffer.putShort((short) 0); // 先占位长度
	        buffer.putInt(0x2112A442); // Magic Cookie
	        buffer.put(id.getBytes()); // Transaction ID

	        // 添加 Requested Transport 属性
	        buffer.putShort((short) 0x0019); // Requested Transport Attribute Type
	        buffer.putShort((short) 4); // Attribute Length
	        buffer.put((byte) 0x11); // Transport protocol (UDP)
	        buffer.put((byte) 0x00); // Padding (must be 3 bytes of zero)
	        buffer.put((byte) 0x00);
	        buffer.put((byte) 0x00);

	        // 计算请求长度并填充
	        int messageLength = buffer.position() - 20;
	        buffer.putShort(2, (short) messageLength);

//	        int fingerprintPos = buffer.position();
//	        buffer.putShort((short) 0x8028); // FINGERPRINT Attribute Type
//	        buffer.putShort((short) 4); // Length: 4 bytes
//	        buffer.putInt(0); // 先占位
	        
	        // 发送请求
	        byte[] stunRequest = Arrays.copyOf(buffer.array(), buffer.position());
//	        int crcValue = computeCRC32(stunRequest) ^ 0x5354554E; // STUN XOR

	        // 填充 FINGERPRINT 值
//	        buffer.putInt(fingerprintPos + 4, crcValue);

	        // 更新总长度
//	        messageLength = buffer.position() - 20;
//	        buffer.putShort(2, (short) messageLength);
	        
	        DatagramPacket udpPacket = new DatagramPacket(stunRequest, stunRequest.length, InetAddress.getByName(host), port);
	        socket.send(udpPacket);
	        System.out.println("step3");

	        // 接收响应
	        byte[] receivedData = new byte[500];
	        DatagramPacket responsePacket = new DatagramPacket(receivedData, receivedData.length);
	        socket.setSoTimeout(3000);
	        socket.receive(responsePacket);
	        System.out.println("step4");

	        // 解析 401 (Unauthorized) 响应
	        Message responseMessage = Message.decode(receivedData, (char) 0, (char) responsePacket.getLength());
	        RealmAttribute realmAttribute = (RealmAttribute) responseMessage.getAttribute(Attribute.REALM);
	        NonceAttribute nonceAttribute = (NonceAttribute) responseMessage.getAttribute(Attribute.NONCE);
	        if (realmAttribute != null && nonceAttribute != null) {
	            String realm = new String(realmAttribute.getRealm(), StandardCharsets.UTF_8);
	            String nonce = new String(nonceAttribute.getNonce(), StandardCharsets.UTF_8);
	            System.out.println("Received realm: " + realm);
	            System.out.println("Received nonce: " + nonce);

	            // 重新构造带认证信息的 Allocate 请求
	            buffer.clear();
	            buffer.putShort((short) 0x0003); // Allocate Request
	            buffer.putShort((short) 0);
	            buffer.putInt(0x2112A442);
	            buffer.put(id.getBytes());

	            // 添加 Requested Transport
	            buffer.putShort((short) 0x0019);
	            buffer.putShort((short) 4);
	            buffer.put((byte) 0x11); // Transport protocol (UDP)
	            buffer.put((byte) 0x00); // Padding (must be 3 bytes of zero)
	            buffer.put((byte) 0x00);
	            buffer.put((byte) 0x00);

	            // 添加 Username
	            byte[] usernameBytes = username.getBytes(StandardCharsets.UTF_8);
	            buffer.putShort((short) 0x0006); // Username Attribute
	            buffer.putShort((short) usernameBytes.length);
	            buffer.put(usernameBytes);

	            // 计算 Padding（补 0 直到 4 字节对齐）
	            int usernamePadding = (4 - (usernameBytes.length % 4)) % 4;
	            for (int i = 0; i < usernamePadding; i++) {
	                buffer.put((byte) 0x00);
	            }
	            
	            // 添加 Realm
	            byte[] realmBytes = realm.getBytes(StandardCharsets.UTF_8);
	            buffer.putShort((short) 0x0014);
	            buffer.putShort((short) realmBytes.length);
	            buffer.put(realmBytes);
	            
	            int realmPadding = (4 - (realmBytes.length % 4)) % 4;
	            for (int i = 0; i < realmPadding; i++) {
	                buffer.put((byte) 0x00); // 填充 0x00
	            }

	            byte[] nonceBytes = nonce.getBytes(StandardCharsets.UTF_8);
	            buffer.putShort((short) 0x0015); // Nonce Attribute Type (0x0015)
	            buffer.putShort((short) nonceBytes.length); // Attribute Length
	            buffer.put(nonceBytes); // Nonce Valu
	            
	            int noncePadding = (4 - (nonceBytes.length % 4)) % 4;
	            for (int i = 0; i < noncePadding; i++) {
	                buffer.put((byte) 0x00); // 填充 0x00
	            }
	            
	            MessageDigest md = MessageDigest.getInstance("MD5");
//	            byte[] hmacKey = md.digest((username + ":" + realm + ":" + password).getBytes(StandardCharsets.UTF_8));
	            String keyInput = "mcdata:140.113.110.221:12345";
	            byte[] hmacKey = md.digest(keyInput.getBytes(StandardCharsets.UTF_8));
	            System.out.println("Generated MD5 Key (Hex): " + bytesToHex(hmacKey));

	            messageLength = buffer.position() - 20;
	            buffer.putShort(2, (short) messageLength);
	            
	            int messageIntegrityStart = buffer.position(); // 记录 Message-Integrity 开始的位置
	            byte[] hmacInput = Arrays.copyOf(buffer.array(), messageIntegrityStart);
	            System.out.println("HMAC Input Bytes (Hex): " + bytesToHex(hmacInput));

	            // 打印 HMAC 计算用的 STUN Message (HEX)
	            StringBuilder hmacHex = new StringBuilder();
	            for (byte b : hmacInput) {
	                hmacHex.append(String.format("%02X ", b)); // 轉換為 HEX 並加上空格
	            }
	            System.out.println("HMAC Input (HEX, before Message-Integrity): \n" + hmacHex.toString().trim());
	            
	            // 计算 Message-Integrity（HMAC-SHA1）
	            byte[] hmac = hmacSHA1(hmacKey, buffer.array(), buffer.position());
	            System.out.println("Computed HMAC (Hex): " + bytesToHex(hmac));
	            buffer.putShort((short) 0x0008);
	            buffer.putShort((short) hmac.length);
	            buffer.put(hmac);

//	            // 更新请求长度
	            messageLength = buffer.position() - 20;
	            buffer.putShort(2, (short) messageLength);

	            // 发送第二次请求
	            stunRequest = Arrays.copyOf(buffer.array(), buffer.position());
	            
	            StringBuilder stunHex = new StringBuilder();
	            for (byte b : stunRequest) {
	                stunHex.append(String.format("%02X ", b)); // 轉換為 HEX 並加上空格
	            }
	            System.out.println("STUN Message (HEX): \n" + stunHex.toString().trim());
	            
	            System.out.println("STUN Request: " + Arrays.toString(stunRequest));
	            udpPacket = new DatagramPacket(stunRequest, stunRequest.length, InetAddress.getByName(host), port);
	            socket.send(udpPacket);
	            System.out.println("Sent authenticated Allocate request.");
	        }

	    } catch (Exception e) {
	        System.err.println("❌ 发生错误: " + e.getMessage());
	        e.printStackTrace();
	        this.isEstablishmentSuccess = false;
	    }
	}
  
  public void establish() {
	    System.out.println("begin");
	    this.isEstablishmentSuccess = false;
	    
	    try {
	        // 參數設定
	        String host = this.turnServerHost;
	        int port = this.turnServerPort;
	        String staticAuthSecret = "123456";
	        long timestamp = System.currentTimeMillis() / 1000; // 取得 UNIX 時間戳（秒）
	        String username = String.valueOf(timestamp);
	        // 在 static auth secret 模式下，password（共享密鑰衍生出的值）通常由 HMAC(username, staticAuthSecret) 得到
	        String password = generateTurnPassword(staticAuthSecret, username);
	        System.out.println("username: " + username);
	        System.out.println("password: " + password);

	        // 使用已建立的 DatagramSocket
	        DatagramSocket socket = this.tcpSocket;

	        // ---------------------------
	        // 第一步：發送基本的 Allocate Request (無認證屬性)
	        ByteBuffer buffer1 = ByteBuffer.allocate(512);
	        
	        TransactionID bindingTransactionID = TransactionID.createNewTransactionID();  // 假设 TransactionID 类生成随机12字节数据
//	        buffer1.put(bindingTransactionID.getBytes());
	        
	        // 寫入 STUN/TURN 頭部
	        buffer1.putShort((short) 0x0003);      // Message Type：Allocate Request
	        buffer1.putShort((short) 0);           // Message Length 先暫留 0（稍後填入）
	        buffer1.putInt(0x2112A442);             // Magic Cookie
	        buffer1.put(bindingTransactionID.getBytes()); // Transaction ID

	        // 添加 Requested Transport 屬性（要求使用 UDP，值為 0x11）
	        buffer1.putShort((short) 0x0019);  // Attribute Type：Requested Transport
	        buffer1.putShort((short) 4);       // Attribute Length：4
	        buffer1.put((byte) 0x11);          // UDP 協議
	        buffer1.put((byte) 0x00);          // 補 3 個 0x00
	        buffer1.put((byte) 0x00);
	        buffer1.put((byte) 0x00);

	        // 更新 Message Length（屬性總長度 = 當前 buffer 位置 - 20 個字節（頭部））
	        int messageLength1 = buffer1.position() - 20;
	        buffer1.putShort(2, (short) messageLength1);

	        byte[] stunRequest1 = Arrays.copyOf(buffer1.array(), buffer1.position());
	        DatagramPacket packet1 = new DatagramPacket(stunRequest1, stunRequest1.length,
	                                                    InetAddress.getByName(host), port);
	        socket.send(packet1);
	        System.out.println("已發送第一個 Allocate Request (不含認證屬性)。");

	        // 接收第一個響應（預期為 401 Unauthorized，並包含 realm 與 nonce）
	        byte[] responseData1 = new byte[500];
	        DatagramPacket responsePacket1 = new DatagramPacket(responseData1, responseData1.length);
	        socket.setSoTimeout(3000);
	        socket.receive(responsePacket1);
	        System.out.println("收到第一個響應。");

	        // 解析響應，並取得 realm 與 nonce 屬性
	        Message responseMessage1 = Message.decode(responseData1, (char) 0, (char) responsePacket1.getLength());
	        RealmAttribute realmAttribute = (RealmAttribute) responseMessage1.getAttribute(Attribute.REALM);
	        NonceAttribute nonceAttribute = (NonceAttribute) responseMessage1.getAttribute(Attribute.NONCE);
	        if (realmAttribute == null || nonceAttribute == null) {
	            throw new Exception("401 響應中缺少 realm 或 nonce 屬性");
	        }
	        String realm = new String(realmAttribute.getRealm(), StandardCharsets.UTF_8);
            String nonce = new String(nonceAttribute.getNonce(), StandardCharsets.UTF_8);
	        System.out.println("取得 realm: " + realm);
	        System.out.println("取得 nonce: " + nonce);

	        // ---------------------------
	        // 第二步：發送包含認證屬性的 Allocate Request
	        TransactionID bindingTransactionID1 = TransactionID.createNewTransactionID(); 
	        ByteBuffer buffer2 = ByteBuffer.allocate(512);

	        // 寫入 STUN/TURN 頭部
	        buffer2.putShort((short) 0x0003);      // Allocate Request
	        buffer2.putShort((short) 0);           // 長度先暫留
	        buffer2.putInt(0x2112A442);             // Magic Cookie
	        buffer2.put(bindingTransactionID1.getBytes()); // 新的 Transaction ID

	        // 添加 Requested Transport 屬性（UDP）
	        buffer2.putShort((short) 0x0019);
	        buffer2.putShort((short) 4);
	        buffer2.put((byte) 0x11);
	        buffer2.put((byte) 0x00);
	        buffer2.put((byte) 0x00);
	        buffer2.put((byte) 0x00);

	        // 添加 Username 屬性
	        byte[] usernameBytes = username.getBytes(StandardCharsets.UTF_8);
	        buffer2.putShort((short) 0x0006);             // Username 屬性型別
	        buffer2.putShort((short) usernameBytes.length);
	        buffer2.put(usernameBytes);
	        // 補齊 4 字節對齊
	        int usernamePadding = (4 - (usernameBytes.length % 4)) % 4;
	        for (int i = 0; i < usernamePadding; i++) {
	            buffer2.put((byte) 0x00);
	        }

	        // 添加 Realm 屬性（使用從 401 響應取得的 realm）
	        byte[] realmBytes = realm.getBytes(StandardCharsets.UTF_8);
	        buffer2.putShort((short) 0x0014);             // Realm 屬性型別
	        buffer2.putShort((short) realmBytes.length);
	        buffer2.put(realmBytes);
	        int realmPadding = (4 - (realmBytes.length % 4)) % 4;
	        for (int i = 0; i < realmPadding; i++) {
	            buffer2.put((byte) 0x00);
	        }

	        // 添加 Nonce 屬性（使用從 401 響應取得的 nonce）
	        byte[] nonceBytes = nonce.getBytes(StandardCharsets.UTF_8);
	        buffer2.putShort((short) 0x0015);             // Nonce 屬性型別
	        buffer2.putShort((short) nonceBytes.length);
	        buffer2.put(nonceBytes);
	        int noncePadding = (4 - (nonceBytes.length % 4)) % 4;
	        for (int i = 0; i < noncePadding; i++) {
	            buffer2.put((byte) 0x00);
	        }

	        // 添加 MESSAGE-INTEGRITY 屬性佔位（20 字節）
	        int miAttrPos = buffer2.position();  // 紀錄該屬性起始位置
	        buffer2.putShort((short) 0x0008);      // MESSAGE-INTEGRITY 屬性型別
	        buffer2.putShort((short) 20);          // 長度固定為 20
	        int miValuePos = buffer2.position();
	        // 先填入 20 個 0x00，稍後用計算出的 HMAC 替換
	        for (int i = 0; i < 20; i++) {
	            buffer2.put((byte) 0x00);
	        }

	        // 更新第二個請求的 Message Length（所有屬性長度）
	        int messageLength2 = buffer2.position() - 20;
	        buffer2.putShort(2, (short) messageLength2);

	        // 計算 MESSAGE-INTEGRITY 的 HMAC-SHA1
	        // 注意：計算 HMAC 時，MESSAGE-INTEGRITY 屬性值須保持為 0（剛好填入的佔位值）
	        byte[] messageForHMAC = Arrays.copyOf(buffer2.array(), buffer2.position());
	        byte[] hmac = generateMessageIntegrity(messageForHMAC, password);
	        // 將計算出的 HMAC 填入到之前保留的位置
	        System.arraycopy(hmac, 0, buffer2.array(), miValuePos, 20);

	        byte[] stunRequest2 = Arrays.copyOf(buffer2.array(), buffer2.position());
	        DatagramPacket packet2 = new DatagramPacket(stunRequest2, stunRequest2.length,
	                                                    InetAddress.getByName(host), port);
	        socket.send(packet2);
	        System.out.println("已發送第二個 Allocate Request (包含認證屬性)。");

	        // ---------------------------
	        // 接收第二次 Allocate 響應（預期狀態為 201 Created）
	        byte[] responseData2 = new byte[500];
	        DatagramPacket responsePacket2 = new DatagramPacket(responseData2, responseData2.length);
	        socket.setSoTimeout(3000);
	        socket.receive(responsePacket2);
	        System.out.println("收到第二次響應。");

	        Message responseMessage2 = Message.decode(responseData2, (char) 0, (char) responsePacket2.getLength());
	        // 假設 Message 類別中有定義成功的 Message Type (例如 0x0101 代表 201 Created)
//	        if (responseMessage2.getMessageType() == Message.ALLOCATE_SUCCESS) {
//	            System.out.println("Allocation 成功！");
//	        } else {
//	            System.out.println("Allocation 失敗，訊息型別: " + responseMessage2.getMessageType());
//	        }
	    } catch (Exception e) {
	        System.err.println("❌ 發生錯誤: " + e.getMessage());
	        e.printStackTrace();
	        this.isEstablishmentSuccess = false;
	    }
	}
  
  public String generateTurnPassword(String secret, String username) {
	    try {
	        Mac mac = Mac.getInstance("HmacSHA1");
	        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA1");
	        mac.init(keySpec);
	        byte[] hmac = mac.doFinal(username.getBytes());
	        return Base64.getEncoder().encodeToString(hmac); // 轉為 Base64
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
  
  byte[] hmacSHA1(byte[] key, byte[] data, int length) throws NoSuchAlgorithmException, InvalidKeyException {
	    SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");
	    Mac mac = Mac.getInstance("HmacSHA1");
	    mac.init(signingKey);
	    
	    byte[] hmacInput = Arrays.copyOfRange(data, 0, length);
	    
	    // 打印 HMAC 计算输入的 HEX
	    StringBuilder hmacInputHex = new StringBuilder();
	    for (byte b : hmacInput) {
	        hmacInputHex.append(String.format("%02X ", b)); // 转换为 HEX 并加空格
	    }
	    System.out.println("HMAC Input (HEX, actually used in HMAC-SHA1 calculation): \n" + hmacInputHex.toString().trim());
	    
	    return mac.doFinal(Arrays.copyOfRange(data, 0, length)); // ✅ 确保计算范围
	}
  
  public static byte[] generateMessageIntegrity(byte[] message, String password) throws Exception {
	    // 取得 HMAC-SHA1 的 Mac 實例
	    Mac mac = Mac.getInstance("HmacSHA1");
	    // 使用 password 作為金鑰 (需以 UTF-8 編碼轉為 byte array)
	    SecretKeySpec keySpec = new SecretKeySpec(password.getBytes(StandardCharsets.UTF_8), "HmacSHA1");
	    mac.init(keySpec);
	    // 計算 HMAC，結果為 20 字節
	    return mac.doFinal(message);
	}
  
  public static byte[] hmacSHA1(String key, byte[] data, int length) throws Exception {
	    SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA1");
	    Mac mac = Mac.getInstance("HmacSHA1");
	    mac.init(signingKey);
	    return mac.doFinal(Arrays.copyOf(data, length));
	}
  
  private static String bytesToHex(byte[] bytes) {
	    StringBuilder hexString = new StringBuilder();
	    for (byte b : bytes) {
	        hexString.append(String.format("%02X", b));
	    }
	    return hexString.toString();
	}
  
  private static int computeCRC32(byte[] data) {
      CRC32 crc32 = new CRC32();
      crc32.update(data);
      return (int) crc32.getValue();
  }


  
//  public void establish() {
//	  this.isEstablishmentSuccess = false;
//	    XorMappedAddressAttribute attribute = null;
//	    try {
//	        String host = this.turnServerHost;  // 修改为 TURN 服务器的地址
//	        int port = this.turnServerPort;    // 修改为 TURN 服务器的端口
//	        String username = this.turnUsername; // TURN 服务器的用户名
//	        String password = this.turnPassword; // TURN 服务器的密码
//	        TransactionID id = this.transactionID;
//	        DatagramSocket socket = this.tcpSocket;
//
//	        Agent agent = new Agent();
//	        // 添加 TURN CandidateHarvester，支持用户名和密码
//	        agent.addCandidateHarvester(new TurnCandidateHarvester(
//	            new TransportAddress(host, port, Transport.UDP), 
//	            username
//	        ));
//	        
//	        
//	        
//	        // 创建 TURN Allocation 请求
//	        Request turnRequest = MessageFactory.createAllocateRequest();
//	        
////	        byte transportProtocol = 0x11; // 0x11 代表 UDP
//	        byte transportProtocol = 0x06; // 0x06 表示 TCP 协议
//	        Attribute transportAttribute = AttributeFactory.createRequestedTransportAttribute(transportProtocol);
//
//	        // 將 Transport 屬性添加到 TURN Allocation Request
//	        turnRequest.putAttribute(transportAttribute);
//	        
////	        if (username != null && password != null) {
////	            Attribute usernameAttribute = AttributeFactory.createUsernameAttribute(username);
////	            turnRequest.putAttribute(usernameAttribute);
////	            
////	            // 发送初始 Allocate Request 以获取 REALM 和 NONCE
////	            // 后续代码会获取服务器返回的 REALM 和 NONCE
////	        }
//	        
//	        byte[] traId = id.getBytes();
//	        turnRequest.setTransactionID(traId);
//
//	        StunStack stunStack = agent.getStunStack();
//	        agent.startConnectivityEstablishment();
//	        byte[] bytes = turnRequest.encode(stunStack);
//
//	        InetAddress turnServerAddress = InetAddress.getByName(host);
//	        DatagramPacket udpPacket = new DatagramPacket(bytes, bytes.length);
//	        udpPacket.setAddress(turnServerAddress);
//	        udpPacket.setPort(port);
//
//	        // 发送请求到 TURN 服务器
//	        try {
//	            Thread.sleep(400L);
//	        } catch (InterruptedException e) {
//	            e.printStackTrace();
//	        }
//	        socket.send(udpPacket);
//
//	        // 接收响应
//	        byte[] receivedData = new byte[500];
//	        DatagramPacket responsePacket = new DatagramPacket(receivedData, receivedData.length);
//	        socket.setSoTimeout(3000);
//	        try {
//	            socket.receive(responsePacket);
//	        } catch (SocketTimeoutException e) {
//	            System.out.println("TURN Server no response");
//	            e.printStackTrace();
//	            this.retryCount++;
//	            stunStack.shutDown();
//	            this.isEstablishmentSuccess = false;
//	            socket.setSoTimeout(0);
//	            return;
//	        }
//	        socket.setSoTimeout(0);
//
//	        // 解码 TURN 响应
//	        Message responseMessage = Message.decode(responsePacket.getData(), (char) 0, (char) responsePacket.getLength());
//	        attribute = (XorMappedAddressAttribute) responseMessage.getAttribute(Attribute.XOR_MAPPED_ADDRESS);
//	        System.out.println("[TURN Server] decode");
//	        
//	        // 提取 XOR-RELAYED-ADDRESS 和 XOR-MAPPED-ADDRESS
//	        XorRelayedAddressAttribute relayedAttribute = 
//	            (XorRelayedAddressAttribute) responseMessage.getAttribute(Attribute.XOR_RELAYED_ADDRESS);
//	        XorMappedAddressAttribute mappedAttribute = 
//	            (XorMappedAddressAttribute) responseMessage.getAttribute(Attribute.XOR_MAPPED_ADDRESS);
//
//	        if (relayedAttribute != null && mappedAttribute != null) {
//	        	
//	            this.responseHost = relayedAttribute.getAddress(id.getBytes()).getHostAddress();
//	            this.responsePort = relayedAttribute.getAddress(id.getBytes()).getPort();
//	            System.out.println("TURN Relayed Address: " + this.responseHost + ":" + this.responsePort);
//	            
//	            this.mappedHost = mappedAttribute.getAddress(id.getBytes()).getHostAddress();
//	            this.mappedPort = mappedAttribute.getAddress(id.getBytes()).getPort();
//	            System.out.println("TURN Mapped Address: " + this.mappedHost + ":" + this.mappedPort);
//	            this.isEstablishmentSuccess = true;
//	        } else {
//	            System.out.println("TURN Allocate Response missing attributes");
//	            this.isEstablishmentSuccess = false;
//	        }
//	        
////	        if (attribute != null) {
////	            stunStack.shutDown();
////	            this.responseHost = attribute.getAddress(id.getBytes()).getHostAddress();
////	            this.responsePort = attribute.getAddress(id.getBytes()).getPort();
////	            System.out.println("TURN Server response ip"+this.responseHost);
////	            System.out.println("TURN Server response port"+this.responsePort);
////	            this.isEstablishmentSuccess = true;
////	        } else {
////	            System.out.println("TURN Server response error");
////	            this.isEstablishmentSuccess = false;
////	        }
//	        this.retryCount = 0;
//
//	    } catch (StunException e) {
//	        e.printStackTrace();
//	        this.isEstablishmentSuccess = false;
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	        this.isEstablishmentSuccess = false;
//	    }
//	}
//  
//  public void establish() {
//	    this.isEstablishmentSuccess = false;
//	    Socket socket = null;
//
//	    try {
//	    	System.out.println("[establish]-1");
//	        String host = this.turnServerHost;  // TURN 服务器地址
//	        int port = this.turnServerPort;    // TURN 服务器端口
////	        String username = this.turnUsername; // TURN 用户名
////	        String password = this.turnPassword; // TURN 密码
//	        String username = "mcdata";
//	        String password = "123456";
//	        TransactionID id = this.transactionID;
//
//	        Agent agent = new Agent();
//	        agent.addCandidateHarvester(new TurnCandidateHarvester(
//	            new TransportAddress(host, port, Transport.TCP), 
//	            username
//	        ));
//
//	        // 初始化 TCP Socket
//	        socket = new Socket(host, port);
//	        OutputStream outputStream = socket.getOutputStream();
//	        InputStream inputStream = socket.getInputStream();
//
//	        StunStack stunStack = agent.getStunStack();
//	        agent.startConnectivityEstablishment();
//
//	        // 第一次 Allocate 请求
//	        Request turnRequest = MessageFactory.createAllocateRequest();
//	        byte transportProtocol = 0x11; // 0x11 代表 UDP
////	        byte transportProtocol = 0x06; // 0x06 表示 TCP 协议
//	        Attribute transportAttribute = AttributeFactory.createRequestedTransportAttribute(transportProtocol);
//	        turnRequest.putAttribute(transportAttribute);
//
//	        // 设置 TransactionID
//	        turnRequest.setTransactionID(id.getBytes());
//	        for (Attribute attribute : turnRequest.getAttributes()) {
//              System.out.println("[attribute]"+attribute.toString());
//          }
//	        // 编码并发送请求
//	        outputStream.write(turnRequest.encode(stunStack));
//	        outputStream.flush();
//	        
//	        // 接收响应
//	        byte[] receivedData = new byte[500];
//	        int bytesRead = inputStream.read(receivedData);
//	        if (bytesRead > 0) {
//	            // 解码响应
//	            Message responseMessage = Message.decode(receivedData, (char) 0, (char) bytesRead);
//	            RealmAttribute realmAttribute = (RealmAttribute) responseMessage.getAttribute(Attribute.REALM);
//	            NonceAttribute nonceAttribute = (NonceAttribute) responseMessage.getAttribute(Attribute.NONCE);
//	            if (realmAttribute != null && nonceAttribute != null) {
//	            	String realm = new String(realmAttribute.getRealm(), StandardCharsets.UTF_8);
//			        String nonce = new String(nonceAttribute.getNonce(), StandardCharsets.UTF_8);
//			        
//
//			        
//	                // 第二次 Allocate 请求，带认证信息
//			        Request secondTurnRequest = MessageFactory.createAllocateRequest();
//			        secondTurnRequest.putAttribute(transportAttribute);
//	                System.out.println("[establish]-5");
//	                // 添加用户名属性
//	                Attribute usernameAttribute = AttributeFactory.createUsernameAttribute(username);
//	                secondTurnRequest.putAttribute(usernameAttribute);
//	                
//	                // 添加 REALM 和 NONCE 属性
//	                Attribute realmAttr = AttributeFactory.createRealmAttribute(realm.getBytes(StandardCharsets.UTF_8));
//	                Attribute nonceAttr = AttributeFactory.createNonceAttribute(nonce.getBytes(StandardCharsets.UTF_8));
//	                secondTurnRequest.putAttribute(realmAttr);
//	                secondTurnRequest.putAttribute(nonceAttr);
//	                
//	                System.out.println("[establish]-6");
//	                secondTurnRequest.setTransactionID(TransactionID.createNewTransactionID().getBytes());
//
//	                System.out.println("[establish]-----------------");
//	                for (Attribute attribute : secondTurnRequest.getAttributes()) {
//	                    System.out.println("[attribute]"+attribute.toString());
//	                }
//	                System.out.println("[establish]-6-2");
//	                Attribute messageIntegrityAttr = AttributeFactory.createMessageIntegrityAttribute(username);
//	                secondTurnRequest.putAttribute(messageIntegrityAttr);
//	                for (Attribute attribute : secondTurnRequest.getAttributes()) {
//	                    System.out.println(attribute.toString());
//	                }               
//	                
//	                byte[] finalEncodedData = secondTurnRequest.encode(stunStack); // 编码包含所有属性的最终请求
//	                for (Attribute attribute : secondTurnRequest.getAttributes()) {
//	                    System.out.println(attribute.toString());
//	                }
////	                System.out.println("[Final Encoded Data]: " + Arrays.toString(finalEncodedData));
//	                System.out.println("[establish]-7");
////	                secondTurnRequest.setTransactionID(TransactionID.createNewTransactionID().getBytes());
//	                
//	                // 编码并发送请求
//	                outputStream.write(finalEncodedData);
//	                outputStream.flush();
//	                
//	                System.out.println("[establish]-8");
//	                // 接收响应
//	                bytesRead = inputStream.read(receivedData);
//	                responseMessage = Message.decode(receivedData, (char) 0, (char) bytesRead);
//
//	                System.out.println("[establish]-9");
//	                // 验证服务器响应中的 MESSAGE-INTEGRITY
//	                MessageIntegrityAttribute serverMessageIntegrity = 
//	                    (MessageIntegrityAttribute) responseMessage.getAttribute(Attribute.MESSAGE_INTEGRITY);
////	                byte[] expectedIntegrity = mac.doFinal(responseMessage.encode(stunStack));
////	                if (!Arrays.equals(expectedIntegrity, serverMessageIntegrity.getMessageIntegrity())) {
////	                    throw new SecurityException("MESSAGE-INTEGRITY validation failed");
////	                }
//	                System.out.println("[establish]-10");
//	                // 提取 XOR-RELAYED-ADDRESS 和 XOR-MAPPED-ADDRESS
//	                XorRelayedAddressAttribute relayedAttribute = 
//	                    (XorRelayedAddressAttribute) responseMessage.getAttribute(Attribute.XOR_RELAYED_ADDRESS);
//	                XorMappedAddressAttribute mappedAttribute = 
//	                    (XorMappedAddressAttribute) responseMessage.getAttribute(Attribute.XOR_MAPPED_ADDRESS);
//	                System.out.println("[establish]-11");
//	                if (relayedAttribute != null && mappedAttribute != null) {
//	                	System.out.println("[establish]-12");
//	                    this.responseHost = relayedAttribute.getAddress(id.getBytes()).getHostAddress();
//	                    this.responsePort = relayedAttribute.getAddress(id.getBytes()).getPort();
//	                    System.out.println("TURN Relayed Address: " + this.responseHost + ":" + this.responsePort);
//
//	                    this.mappedHost = mappedAttribute.getAddress(id.getBytes()).getHostAddress();
//	                    this.mappedPort = mappedAttribute.getAddress(id.getBytes()).getPort();
//	                    System.out.println("TURN Mapped Address: " + this.mappedHost + ":" + this.mappedPort);
//
//	                    this.isEstablishmentSuccess = true;
//	                } else {
//	                    System.out.println("TURN Allocate Response missing attributes");
//	                    this.isEstablishmentSuccess = false;
//	                }
//	            }
//	        } else {
//	            System.out.println("No response from TURN server.");
//	            this.isEstablishmentSuccess = false;
//	        }
//
//	        this.retryCount = 0;
//	    } catch (StunException | IOException e) {
////	    } catch (StunException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
//	        e.printStackTrace();
//	        this.isEstablishmentSuccess = false;
//	    } finally {
//	        // 关闭 TCP Socket
////	        if (socket != null) {
////	            try {
////	                socket.close();
////	            } catch (IOException e) {
////	                e.printStackTrace();
////	            }
////	        }
//	    }
//	}
  
  
//  public void establish() {
//	    this.isEstablishmentSuccess = false;
//	    Socket socket = null;
//
//	    try {
//	    	System.out.println("[establish]-1");
//	        String host = this.turnServerHost;  // TURN 服务器地址
//	        int port = this.turnServerPort;    // TURN 服务器端口
////	        String username = this.turnUsername; // TURN 用户名
////	        String password = this.turnPassword; // TURN 密码
//	        String username = "mcdata";
//	        String password = "123456";
//	        TransactionID id = this.transactionID;
//
//	        Agent agent = new Agent();
//	        agent.addCandidateHarvester(new TurnCandidateHarvester(
//	            new TransportAddress(host, port, Transport.TCP), 
//	            username
//	        ));
//
//	        // 初始化 TCP Socket
//	        socket = new Socket(host, port);
//	        OutputStream outputStream = socket.getOutputStream();
//	        InputStream inputStream = socket.getInputStream();
//
//	        StunStack stunStack = agent.getStunStack();
//	        agent.startConnectivityEstablishment();
//
//	        // 第一次 Allocate 请求
//	        Request turnRequest = MessageFactory.createAllocateRequest();
//	        byte transportProtocol = 0x11; // 0x11 代表 UDP
////	        byte transportProtocol = 0x06; // 0x06 表示 TCP 协议
//	        Attribute transportAttribute = AttributeFactory.createRequestedTransportAttribute(transportProtocol);
//	        turnRequest.putAttribute(transportAttribute);
//
//	        // 设置 TransactionID
//	        turnRequest.setTransactionID(id.getBytes());
//	        for (Attribute attribute : turnRequest.getAttributes()) {
//                System.out.println("[attribute]"+attribute.toString());
//            }
//	        // 编码并发送请求
//	        outputStream.write(turnRequest.encode(stunStack));
//	        outputStream.flush();
//	        
//	        // 接收响应
//	        byte[] receivedData = new byte[500];
//	        int bytesRead = inputStream.read(receivedData);
//	        if (bytesRead > 0) {
//	            // 解码响应
//	            Message responseMessage = Message.decode(receivedData, (char) 0, (char) bytesRead);
//	            RealmAttribute realmAttribute = (RealmAttribute) responseMessage.getAttribute(Attribute.REALM);
//	            NonceAttribute nonceAttribute = (NonceAttribute) responseMessage.getAttribute(Attribute.NONCE);
//	            if (realmAttribute != null && nonceAttribute != null) {
//	            	String realm = new String(realmAttribute.getRealm(), StandardCharsets.UTF_8);
//			        String nonce = new String(nonceAttribute.getNonce(), StandardCharsets.UTF_8);
//			        
//
//			        
//	                // 第二次 Allocate 请求，带认证信息
//			        Request secondTurnRequest = MessageFactory.createAllocateRequest();
//			        secondTurnRequest.putAttribute(transportAttribute);
//	                System.out.println("[establish]-5");
//	                // 添加用户名属性
//	                Attribute usernameAttribute = AttributeFactory.createUsernameAttribute(username);
//	                secondTurnRequest.putAttribute(usernameAttribute);
//	                
//	                // 添加 REALM 和 NONCE 属性
//	                Attribute realmAttr = AttributeFactory.createRealmAttribute(realm.getBytes(StandardCharsets.UTF_8));
//	                Attribute nonceAttr = AttributeFactory.createNonceAttribute(nonce.getBytes(StandardCharsets.UTF_8));
//	                secondTurnRequest.putAttribute(realmAttr);
//	                secondTurnRequest.putAttribute(nonceAttr);
//	                
//	                System.out.println("[establish]-6");
//	                secondTurnRequest.setTransactionID(TransactionID.createNewTransactionID().getBytes());
//
//	                
////	                byte[] encodedData = turnRequest.encode(stunStack);
//	                // 计算 MESSAGE-INTEGRITY
////	                String keySource = username + ":" + realm + ":" + password;
////	                byte[] key = MessageDigest.getInstance("MD5").digest(keySource.getBytes(StandardCharsets.UTF_8));
////	                Mac mac = Mac.getInstance("HmacSHA1");
////	                SecretKeySpec keySpec = new SecretKeySpec(key, "HmacSHA1");
////	                mac.init(keySpec);
////	                System.out.println("[establish]-6-1");
////	                for (Attribute attribute : secondTurnRequest.getAttributes()) {
////	                    System.out.println("[attribute]"+attribute.toString());
////	                }
////	                byte[] messageIntegrity = mac.doFinal(secondTurnRequest.encode(stunStack));
////	                secondTurnRequest.encode(stunStack);
//	         
//	                System.out.println("[establish]-----------------");
////	                Attribute softAttr = secondTurnRequest.getAttribute(Attribute.SOFTWARE);
////	                Attribute fingerAttr = secondTurnRequest.getAttribute(Attribute.FINGERPRINT);
////	                secondTurnRequest.removeAttribute(Attribute.SOFTWARE);
////	                secondTurnRequest.removeAttribute(Attribute.FINGERPRINT);
//	                for (Attribute attribute : secondTurnRequest.getAttributes()) {
//	                    System.out.println("[attribute]"+attribute.toString());
//	                }
//	                System.out.println("[establish]-6-2");
//	                Attribute messageIntegrityAttr = AttributeFactory.createMessageIntegrityAttribute(username);
//	                secondTurnRequest.putAttribute(messageIntegrityAttr);
//	                for (Attribute attribute : secondTurnRequest.getAttributes()) {
//	                    System.out.println(attribute.toString());
//	                }
////	                Attribute messageIntegrityAttr = AttributeFactory.createMessageIntegrityAttribute(new String(messageIntegrity, StandardCharsets.UTF_8));
////	                secondTurnRequest.putAttribute(messageIntegrityAttr);
////	                secondTurnRequest.putAttribute(softAttr);
////	                secondTurnRequest.putAttribute(fingerAttr);
////	                System.out.println("[establish]-7 "+messageIntegrityAttr.toString());
//	                System.out.println(secondTurnRequest.toString());
//	                
//	                
//	                
//	                byte[] finalEncodedData = secondTurnRequest.encode(stunStack); // 编码包含所有属性的最终请求
//	                for (Attribute attribute : secondTurnRequest.getAttributes()) {
//	                    System.out.println(attribute.toString());
//	                }
////	                System.out.println("[Final Encoded Data]: " + Arrays.toString(finalEncodedData));
//	                System.out.println("[establish]-7");
////	                secondTurnRequest.setTransactionID(TransactionID.createNewTransactionID().getBytes());
//	                
//	                // 编码并发送请求
//	                outputStream.write(finalEncodedData);
//	                outputStream.flush();
//	                
//	                System.out.println("[establish]-8");
//	                // 接收响应
//	                bytesRead = inputStream.read(receivedData);
//	                responseMessage = Message.decode(receivedData, (char) 0, (char) bytesRead);
//
//	                System.out.println("[establish]-9");
//	                // 验证服务器响应中的 MESSAGE-INTEGRITY
//	                MessageIntegrityAttribute serverMessageIntegrity = 
//	                    (MessageIntegrityAttribute) responseMessage.getAttribute(Attribute.MESSAGE_INTEGRITY);
////	                byte[] expectedIntegrity = mac.doFinal(responseMessage.encode(stunStack));
////	                if (!Arrays.equals(expectedIntegrity, serverMessageIntegrity.getMessageIntegrity())) {
////	                    throw new SecurityException("MESSAGE-INTEGRITY validation failed");
////	                }
//	                System.out.println("[establish]-10");
//	                // 提取 XOR-RELAYED-ADDRESS 和 XOR-MAPPED-ADDRESS
//	                XorRelayedAddressAttribute relayedAttribute = 
//	                    (XorRelayedAddressAttribute) responseMessage.getAttribute(Attribute.XOR_RELAYED_ADDRESS);
//	                XorMappedAddressAttribute mappedAttribute = 
//	                    (XorMappedAddressAttribute) responseMessage.getAttribute(Attribute.XOR_MAPPED_ADDRESS);
//	                System.out.println("[establish]-11");
//	                if (relayedAttribute != null && mappedAttribute != null) {
//	                	System.out.println("[establish]-12");
//	                    this.responseHost = relayedAttribute.getAddress(id.getBytes()).getHostAddress();
//	                    this.responsePort = relayedAttribute.getAddress(id.getBytes()).getPort();
//	                    System.out.println("TURN Relayed Address: " + this.responseHost + ":" + this.responsePort);
//
//	                    this.mappedHost = mappedAttribute.getAddress(id.getBytes()).getHostAddress();
//	                    this.mappedPort = mappedAttribute.getAddress(id.getBytes()).getPort();
//	                    System.out.println("TURN Mapped Address: " + this.mappedHost + ":" + this.mappedPort);
//
//	                    this.isEstablishmentSuccess = true;
//	                } else {
//	                    System.out.println("TURN Allocate Response missing attributes");
//	                    this.isEstablishmentSuccess = false;
//	                }
//	            }
//	        } else {
//	            System.out.println("No response from TURN server.");
//	            this.isEstablishmentSuccess = false;
//	        }
//
//	        this.retryCount = 0;
//	    } catch (StunException | IOException e) {
////	    } catch (StunException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
//	        e.printStackTrace();
//	        this.isEstablishmentSuccess = false;
//	    } finally {
//	        // 关闭 TCP Socket
////	        if (socket != null) {
////	            try {
////	                socket.close();
////	            } catch (IOException e) {
////	                e.printStackTrace();
////	            }
////	        }
//	    }
//	}

//  public void establish() {
//	    this.isEstablishmentSuccess = false;
//	    Socket socket = null;

//	    try {
//	    	System.out.println("[establish]-1");
//	        String host = this.turnServerHost;  // TURN 服务器地址
//	        int port = this.turnServerPort;    // TURN 服务器端口
//	        String username = "mcdata";
//	        String password = "123456";
//	        TransactionID id = this.transactionID;

//	        Agent agent = new Agent();
//	        agent.addCandidateHarvester(new TurnCandidateHarvester(
//	            new TransportAddress(host, port, Transport.TCP), 
//	            username
//	        ));
//
//	        // 初始化 TCP Socket
//	        socket = new Socket(host, port);
//
//
//	        StunStack stunStack = agent.getStunStack();
//	        agent.startConnectivityEstablishment();
//	        Transport protocol = Transport.TCP;
	        
//	        serverAddress =
//	                new TransportAddress(InetAddress.getLocalHost(), Integer.valueOf(
//	                    args[1]).intValue(), protocol);
//
//	        tcpSocketToServer = new Socket(serverAddress.getHostAddress(), 3478);
//	        System.out.println("Local port chosen : " + tcpSocketToServer.getLocalPort());
//	        
//	        sock = new IceTcpSocketWrapper(tcpSocketToServer);
//	        System.out.println("Adding an new TCP connection to : "
//	            + serverAddress.getHostAddress());
//
//	        localAddress =
//	            new TransportAddress(InetAddress.getLocalHost(),
//	                tcpSocketToServer.getLocalPort(), protocol);
//	        System.out.println("Client adress : " + localAddress);
//	        System.out.println("Server adress : " + serverAddress);
//
//	        ClientChannelDataEventHandler channelDataHandler =
//	            new ClientChannelDataEventHandler();
//	        turnStack = new TurnStack(null, channelDataHandler);
//	        channelDataHandler.setTurnStack(turnStack);
//	        
//	        turnStack.addSocket(sock);
//	        
//	        requestSender = new BlockingRequestSender(turnStack, localAddress);
//	        
//	        // 第一次 Allocate 请求
//	        Request turnRequest = MessageFactory.createAllocateRequest();
//	        RequestedTransportAttribute requestedTransportAttribute =
//	                AttributeFactory
//	                    .createRequestedTransportAttribute(RequestedTransportAttribute.TCP);
//	        turnRequest.putAttribute(requestedTransportAttribute);
//	        System.out.println("Message type : " + (int) turnRequest.getMessageType());
//
//	        StunMessageEvent evt = null;
//	        try
//	        {
//	            AllocationResponseCollector allocResCollec =
//	                new AllocationResponseCollector(turnStack);
//	            /*
//	             * turnStack.sendRequest(request, serverAddress, localAddress,
//	             * allocResCollec);
//	             */
//	            evt = requestSender.sendRequestAndWaitForResponse(
//	            		turnRequest, serverAddress);
//	            allocResCollec.processResponse((StunResponseEvent) evt);
//	        }
//	        catch (Exception ex)
//	        {
//	            // this shouldn't happen since we are the ones that created the
//	            // request
//	            ex.printStackTrace();
//	            System.out.println("Internal Error. Failed to encode a message");
//	            return ;
//	        }
	        
	        
//        }

	        
	        
	        
//	        // 接收响应
//	        byte[] receivedData = new byte[500];
//	        int bytesRead = inputStream.read(receivedData);
//	        if (bytesRead > 0) {
//	            // 解码响应
//	            Message responseMessage = Message.decode(receivedData, (char) 0, (char) bytesRead);
//	            RealmAttribute realmAttribute = (RealmAttribute) responseMessage.getAttribute(Attribute.REALM);
//	            NonceAttribute nonceAttribute = (NonceAttribute) responseMessage.getAttribute(Attribute.NONCE);
//	            if (realmAttribute != null && nonceAttribute != null) {
//	            	String realm = new String(realmAttribute.getRealm(), StandardCharsets.UTF_8);
//			        String nonce = new String(nonceAttribute.getNonce(), StandardCharsets.UTF_8);
//			        
//
//			        
//	                // 第二次 Allocate 请求，带认证信息
//			        Request secondTurnRequest = MessageFactory.createAllocateRequest();
//			        secondTurnRequest.putAttribute(transportAttribute);
//	                System.out.println("[establish]-5");
//	                // 添加用户名属性
//	                Attribute usernameAttribute = AttributeFactory.createUsernameAttribute(username);
//	                secondTurnRequest.putAttribute(usernameAttribute);
//	                
//	                // 添加 REALM 和 NONCE 属性
//	                Attribute realmAttr = AttributeFactory.createRealmAttribute(realm.getBytes(StandardCharsets.UTF_8));
//	                Attribute nonceAttr = AttributeFactory.createNonceAttribute(nonce.getBytes(StandardCharsets.UTF_8));
//	                secondTurnRequest.putAttribute(realmAttr);
//	                secondTurnRequest.putAttribute(nonceAttr);
//	                
//	                System.out.println("[establish]-6");
//	                secondTurnRequest.setTransactionID(TransactionID.createNewTransactionID().getBytes());
//
//	                System.out.println("[establish]-----------------");
//	                for (Attribute attribute : secondTurnRequest.getAttributes()) {
//	                    System.out.println("[attribute]"+attribute.toString());
//	                }
//	                System.out.println("[establish]-6-2");
//	                Attribute messageIntegrityAttr = AttributeFactory.createMessageIntegrityAttribute(username);
//	                secondTurnRequest.putAttribute(messageIntegrityAttr);
//	                for (Attribute attribute : secondTurnRequest.getAttributes()) {
//	                    System.out.println(attribute.toString());
//	                }               
//	                
//	                byte[] finalEncodedData = secondTurnRequest.encode(stunStack); // 编码包含所有属性的最终请求
//	                for (Attribute attribute : secondTurnRequest.getAttributes()) {
//	                    System.out.println(attribute.toString());
//	                }
////	                System.out.println("[Final Encoded Data]: " + Arrays.toString(finalEncodedData));
//	                System.out.println("[establish]-7");
////	                secondTurnRequest.setTransactionID(TransactionID.createNewTransactionID().getBytes());
//	                
//	                // 编码并发送请求
//	                outputStream.write(finalEncodedData);
//	                outputStream.flush();
//	                
//	                System.out.println("[establish]-8");
//	                // 接收响应
//	                bytesRead = inputStream.read(receivedData);
//	                responseMessage = Message.decode(receivedData, (char) 0, (char) bytesRead);
//
//	                System.out.println("[establish]-9");
//	                // 验证服务器响应中的 MESSAGE-INTEGRITY
//	                MessageIntegrityAttribute serverMessageIntegrity = 
//	                    (MessageIntegrityAttribute) responseMessage.getAttribute(Attribute.MESSAGE_INTEGRITY);
////	                byte[] expectedIntegrity = mac.doFinal(responseMessage.encode(stunStack));
////	                if (!Arrays.equals(expectedIntegrity, serverMessageIntegrity.getMessageIntegrity())) {
////	                    throw new SecurityException("MESSAGE-INTEGRITY validation failed");
////	                }
//	                System.out.println("[establish]-10");
//	                // 提取 XOR-RELAYED-ADDRESS 和 XOR-MAPPED-ADDRESS
//	                XorRelayedAddressAttribute relayedAttribute = 
//	                    (XorRelayedAddressAttribute) responseMessage.getAttribute(Attribute.XOR_RELAYED_ADDRESS);
//	                XorMappedAddressAttribute mappedAttribute = 
//	                    (XorMappedAddressAttribute) responseMessage.getAttribute(Attribute.XOR_MAPPED_ADDRESS);
//	                System.out.println("[establish]-11");
//	                if (relayedAttribute != null && mappedAttribute != null) {
//	                	System.out.println("[establish]-12");
//	                    this.responseHost = relayedAttribute.getAddress(id.getBytes()).getHostAddress();
//	                    this.responsePort = relayedAttribute.getAddress(id.getBytes()).getPort();
//	                    System.out.println("TURN Relayed Address: " + this.responseHost + ":" + this.responsePort);
//
//	                    this.mappedHost = mappedAttribute.getAddress(id.getBytes()).getHostAddress();
//	                    this.mappedPort = mappedAttribute.getAddress(id.getBytes()).getPort();
//	                    System.out.println("TURN Mapped Address: " + this.mappedHost + ":" + this.mappedPort);
//
//	                    this.isEstablishmentSuccess = true;
//	                } else {
//	                    System.out.println("TURN Allocate Response missing attributes");
//	                    this.isEstablishmentSuccess = false;
//	                }
//	            }
//	        } else {
//	            System.out.println("No response from TURN server.");
//	            this.isEstablishmentSuccess = false;
//	        }
//
//	        this.retryCount = 0;
//	    } catch (IOException e) {
////	    } catch (StunException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
//	        e.printStackTrace();
//	        this.isEstablishmentSuccess = false;
//	    } finally {
//	        // 关闭 TCP Socket
////	        if (socket != null) {
////	            try {
////	                socket.close();
////	            } catch (IOException e) {
////	                e.printStackTrace();
////	            }
////	        }
//	    }
//	}
  
  public boolean isEstablishmentSuccess() {
    return this.isEstablishmentSuccess;
  }
  
  public String getResponseHost() {
    return this.responseHost;
  }
  
  public int getResponsePort() {
    return this.responsePort;
  }
  
  public DatagramSocket getUdpSocket() {
    return this.tcpSocket;
  }
  
  public TransactionID getTransactionID() {
    return this.transactionID;
  }
  
  public String getStunServerHost() {
    return this.turnServerHost;
  }
  
  public int getStunServerPort() {
    return this.turnServerPort;
  }
}
