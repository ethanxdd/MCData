package netmsrp.testutils;

/*
 * Copyright ? Jo?o Antunes 2008 This file is part of MSRP Java Stack.
 * 
 * MSRP Java Stack is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * MSRP Java Stack is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with MSRP Java Stack. If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.ArrayList;

import netmsrp.*;
import netmsrp.events.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class used to test the callbacks of the Stack
 * 
 * @author Jo?o Andr? Pereira Antunes 2008
 */
public class MockSessionListener
    implements SessionListener
{
    /** The logger associated with this class */
    private static final Logger logger =
        LoggerFactory.getLogger(MockSessionListener.class);

    private String name;

    /* The field results of the calls: */
    private Session acceptHookSession;

    private Message acceptHookMessage;

    private Session receiveMessageSession;

    private Message receiveMessage;

    private Session receivedReportSession;

    private Transaction receivedReportTransaction;

    private Transaction receivedNicknameTransaction;

    private Session updateSendStatusSession;

    private Message updateSendStatusMessage;

    private Session abortedMessageSession;

    private Message abortedMessage;

    private Boolean acceptHookResult;

    private String nickname;

    /**
     * For threading lock-step.
     */
    private Object	stopAndWait = new Object();

    public Object  messageComplete = new Object();

    /**
     * The external data container object to be set or not, that is going to be
     * used by the acceptHook if it exists when called.
     */
    private DataContainer externalDataContainer;

    /**
     * Counter for the send status update, the number of elements reflect the
     * number of calls to the updateSendStatus method and its values the number
     * of bytes that the function was called with
     * 
     * @see SessionListener#updateSendStatus(Session, Message, long)
     */
    public ArrayList<Long> updateSendStatusCounter = new ArrayList<Long>();

    /**
     * A counter for the number of success reports received
     */
    public ArrayList<Long> successReportCounter = new ArrayList<Long>();

    /**
     * A counter for the number of message aborts received. the size of this
     * array counts how many message abortions and its values the bytes that
     * were received before the message was aborted
     */
    public ArrayList<Long> abortMessageCounter = new ArrayList<Long>();

    public ArrayList<MessageAbortedEvent> messageAbortEvents =
    new ArrayList<MessageAbortedEvent>();

    /**
     * Constructor of the mock session listener
     * 
     * @param name the String that names this constructor, used for debug
     *            purposes
     */
    public MockSessionListener(String name)
    {
        this.name = name;
    }

    private void dbg(String msg)
    {
        logger.debug(String.format("{%s} %s", name, msg));
    }

    private void warn(String msg)
    {
        logger.warn(String.format("{%s} %s", name, msg));
    }

    @Override
	public boolean acceptHook(Session session, IncomingMessage message)
    {
        dbg("AcceptHook called");
        synchronized (stopAndWait)
        {
            while (acceptHookResult == null)
            {
                try
                {
                    stopAndWait.wait();
                }
                catch (InterruptedException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }
        boolean toReturn = acceptHookResult.booleanValue();

        dbg("AcceptHook will return: " + toReturn);

        acceptHookMessage = message;
        acceptHookSession = session;
        if (externalDataContainer == null)
        {
            MemoryDataContainer dataContainer =
                new MemoryDataContainer((int) message.getSize());
            message.setDataContainer(dataContainer);
        }
        else
        {
            message.setDataContainer(externalDataContainer);
        }
        acceptHookResult = null;
        dbg("AcceptHook returns: " + toReturn);
        return toReturn;
    }

    /**
     * To be called by the receiver once it is ready to receive data.
     */
    public void triggerReception()
    {
    	synchronized(stopAndWait)
    	{
    		stopAndWait.notifyAll();
    		try {
				stopAndWait.wait();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
    	}
    }

    @Override
    public void receivedMessage(Session session, IncomingMessage message)
    {
        dbg("receiveMessage(id=[" + message.getMessageID() + "])");
        if (message instanceof IncomingAliveMessage)
        	return;
        receiveMessage = message;
        receiveMessageSession = session;
        /*
         * Signal that everything has been received.
         */
        synchronized (stopAndWait)
        {
            stopAndWait.notifyAll();
        }
        if (message.isComplete())
        {
            synchronized (messageComplete)
            {
                messageComplete.notifyAll();
            }
        }
    }

    @Override
    public void receivedReport(Session session, Transaction tReport)
    {
        if (tReport.getStatusHeader().getStatusCode() != ResponseCode.RC200)
        {
            dbg("Received report with code different from 200"
                + ", with code: " + tReport.getStatusHeader().getStatusCode()
                + " returning");
            return;
        }
        dbg("Received report, confirming "
            + tReport.getByteRange()[1] + " bytes were sent(== "
            + (tReport.getByteRange()[1] * 100)
            / tReport.getTotalMessageBytes() + "%) Tx-"
            + tReport.getTransactionType() + ", status:"
            + tReport.getStatusHeader().getStatusCode());
        receivedReportSession = session;
        receivedReportTransaction = tReport;
        synchronized (successReportCounter)
        {
            successReportCounter.add(tReport.getByteRange()[1]);
            successReportCounter.notifyAll();
        }
    }

    @Override
    public void updateSendStatus(Session session, Message message,
        long numberBytesSent)
    {
    	final long size = message.getSize();
    	final long sent = ((OutgoingMessage) message).getSentBytes();
    	dbg("updateSendStatus(bytes sent[" + numberBytesSent
            + "], msgtotal[" + sent + "]) == " + (size == 0 ? 100 : (sent * 100) / size) + "%");
        updateSendStatusSession = session;
        updateSendStatusMessage = message;
        synchronized (updateSendStatusCounter)
        {
            updateSendStatusCounter.add(numberBytesSent);
            updateSendStatusCounter.notifyAll();
        }
    }

    @Override
    public void abortedMessageEvent(MessageAbortedEvent abortEvent)
    {
    	final long size;
        messageAbortEvents.add(abortEvent);
        boolean incoming = false;
        if (abortEvent.getMessage().getDirection() == Direction.IN)
        {
            incoming = true;

            IncomingMessage message = (IncomingMessage) abortEvent.getMessage();
        	size = message.getSize();
        	dbg("abortedMessageEvent() bytes received: "
                + message.getReceivedBytes() + " == "
                + (size == 0 ? 100 : (message.getReceivedBytes() * 100) / size) + "%");
        }
        else if (abortEvent.getMessage().getDirection() == Direction.OUT)
        {
            OutgoingMessage message = (OutgoingMessage) abortEvent.getMessage();
        	size = message.getSize();
        	dbg("abortedMessageEvent() bytes sent: "
                + message.getSentBytes() + " == "
                + (size == 0 ? 100 : (message.getSentBytes() * 100) / size) + "%");
        }
        abortedMessage = abortEvent.getMessage();
        abortedMessageSession = abortEvent.getSession();
        synchronized (abortMessageCounter)
        {
            if (incoming)
                abortMessageCounter.add(((IncomingMessage) abortedMessage)
                    .getReceivedBytes());
            else
                abortMessageCounter.add(((OutgoingMessage) abortedMessage)
                    .getSentBytes());
            abortMessageCounter.notifyAll();
        }
    }

    /**
     * @return the acceptHookSession
     */
    public Session getAcceptHookSession()
    {
        return acceptHookSession;
    }

    /**
     * @return the acceptHookMessage
     */
    public Message getAcceptHookMessage()
    {
        return acceptHookMessage;
    }

    /**
     * @return the receiveMessageSession
     */
    public Session getReceiveMessageSession()
    {
        return receiveMessageSession;
    }

    /**
     * @return the receiveMessage
     */
    public Message getReceiveMessage()
    {
        return receiveMessage;
    }

    /**
     * @return the receivedReportSession
     */
    public Session getReceivedReportSession()
    {
        return receivedReportSession;
    }

    /**
     * @return the receivedReportTransaction
     */
    public Transaction getReceivedReportTransaction()
    {
        return receivedReportTransaction;
    }

    /**
     * @return the receivedNicknameTransaction
     */
    public Transaction getReceivedNicknameTransaction()
    {
        return receivedNicknameTransaction;
    }

    /**
     * @return the updateSendStatusSession
     */
    public Session getUpdateSendStatusSession()
    {
        return updateSendStatusSession;
    }

    /**
     * @return the updateSendStatusMessage
     */
    public Message getUpdateSendStatusMessage()
    {
        return updateSendStatusMessage;
    }

    /**
     * @return the abortedMessageSession
     */
    public Session getAbortedMessageSession()
    {
        return abortedMessageSession;
    }

    /**
     * @return the abortedMessage
     */
    public Message getAbortedMessage()
    {
        return abortedMessage;
    }

    /**
     * @param acceptHookResult the acceptHookResult to set
     */
    public void setAcceptHookResult(Boolean acceptHookResult)
    {
        this.acceptHookResult = acceptHookResult;
    }

    public void setDataContainer(DataContainer dc)
    {
        externalDataContainer = dc;
    }

	@Override
	public void connectionLost(Session session, Throwable cause) {
		warn("Connection broke, reason: " + cause.getMessage());
		cause.printStackTrace();
		session.tearDown();
	}

	@Override
	public void receivedNickname(Session session, Transaction request) {
		receivedNicknameTransaction = request;
		nickname = request.getNickname();
        synchronized (this)
        {
            this.notifyAll();
        }
	}

	@Override
	public void receivedNickNameResult(Session session, TransactionResponse result) {
		int code = result.getResponseCode(); 
		if (code != ResponseCode.RC200)
		{
			warn("Bad nickname result: " + ResponseCode.toString(code));
		}
	}

	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}
}
