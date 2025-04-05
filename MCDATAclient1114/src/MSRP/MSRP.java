package MSRP;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;

import netmsrp.IncomingMessage;
import netmsrp.MemoryDataContainer;
import netmsrp.Message;
import netmsrp.Session;
import netmsrp.SessionListener;
import netmsrp.Transaction;
import netmsrp.TransactionResponse;
import netmsrp.events.MessageAbortedEvent;

public class MSRP implements SessionListener {
	
	private PropertyChangeSupport support;
	 
	private String tt ="";
	public MSRP() {
	      support = new PropertyChangeSupport(this);  // 在构造函数中初始化 support
	  }
	public MSRP(String type) {
	      support = new PropertyChangeSupport(this);  // 在构造函数中初始化 support
	      tt=type;
	  }
	 
	@Override
	public void abortedMessageEvent(MessageAbortedEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean acceptHook(Session session, IncomingMessage message) {
		// TODO Auto-generated method stub
		System.out.println("收到新消息!");
//		System.out.println(tt);
        // Step 4: 创建 MemoryDataContainer 并设置到 IncomingMessage
        MemoryDataContainer mdc = new MemoryDataContainer((int) message.getSize());
        message.setDataContainer(mdc);

        // Step 5: 返回 true 表示接受该消息
        return true;
//		return false;
	}

	@Override
	public void connectionLost(Session session, Throwable cause) {
		// TODO Auto-generated method stub
		System.out.println("Connection lost: " + cause.getMessage());
	}

	@Override
	public void receivedMessage(Session session, IncomingMessage message) {
	    try {
	        MemoryDataContainer mdc = (MemoryDataContainer) message.getDataContainer();
	        if (mdc != null) {
	        	ByteBuffer buffer = mdc.get(0, 0);
	        	byte[] data = new byte[buffer.remaining()];
	            buffer.get(data); // 将数据填充到字节数组中
	            String receivedText = new String(data, "UTF-8");
	            System.out.println("接收到的消息内容: " + receivedText);
//	            support = new PropertyChangeSupport(this);
		        support.firePropertyChange("MCDataSDSMessage", null, receivedText+","+tt);
	        } else {
	            System.out.println("DataContainer 未设置，无法读取数据。");
	        }
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    if (latch != null) {
            latch.countDown();
        }
	}
	
	private CountDownLatch latch;

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }
	
	@Override
	public void receivedNickNameResult(Session arg0, TransactionResponse arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receivedNickname(Session arg0, Transaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receivedReport(Session arg0, Transaction arg1) {
		// TODO Auto-generated method stub
		System.out.println("????");
	}

	@Override
	public void updateSendStatus(Session arg0, Message arg1, long arg2) {
		// TODO Auto-generated method stub
		
	}
        
	 public void receiveSipMessage(String receivedText) {
	      //String oldSipMessage = this.sipMessage;
	      //this.sipMessage = newSipMessage;
		 support.firePropertyChange("MCDataSDSMessage", null, receivedText);
	  }
	  
	  public void addPropertyChangeListener(PropertyChangeListener listener) {
	      support.addPropertyChangeListener(listener);
	  }

	  public void removePropertyChangeListener(PropertyChangeListener listener) {
	      support.removePropertyChangeListener(listener);
	  }    
}
