package hu.bme.mit.gamma.impl.subnode;

import hu.bme.mit.gamma.impl.channels.dds.*;

import java.util.List;
import org.yakindu.scr.ITimer;

import hu.bme.mit.gamma.impl.interfaces.*;
import hu.bme.mit.gamma.impl.channels.*;
import hu.bme.mit.gamma.impl.pubwrapper.*;
import hu.bme.mit.gamma.impl.subwrapper.*;

public class SubNode  {			
	// Component instances
	private SubWrapper sub = new SubWrapper();
	// Port instances
	private Notification notification = new Notification();
	// Inner channel instances
	// Outer channel topics
	private MessageChannelDDS messageOfPub;
	
	public SubNode() {
		init();
	}
	
	/** Enters the contained statemachines recursively. Should be used only be the container (composite system) class. */
	public void reset() {
		sub.reset();
	}
	
	/** Creates the channel mappings and enters the wrapped statemachines. */
	private void init() {
		// Registration of simple channels
		// Registration of broadcast channels
		// Instantiation of topics
		messageOfPub = new MessageChannelDDS("messageOfPub");
		messageOfPub.setupReader();
		messageOfPub.addSubscriptionListener(new MessageOfPubListener());
		reset();
	}
	
	// Inner classes representing Ports
	public class Notification implements NotificationInterface.Provided {
	
		
		@Override
		public boolean isRaisedReceived() {
			return sub.getNotification().isRaisedReceived();
		}
		
		@Override
		public void registerListener(NotificationInterface.Listener.Provided listener) {
			sub.getNotification().registerListener(listener);
		}
		
		@Override
		public List<NotificationInterface.Listener.Provided> getRegisteredListeners() {
			return sub.getNotification().getRegisteredListeners();
		}
		
	}
	
	public Notification getNotification() {
		return notification;
	}
	
	// Inner classes for publishing events
	
	// Inner classes for receiving events
	class MessageOfPubListener implements MessageChannelDDS.MessageListener{
		public void raisedHello() {
			sub.getMessage().raiseHello();
		}
	}	
	
	/** Starts the running of the asynchronous node. */
	public void start() {
		sub.start();
	}
	
	/** Setter for the timer e.g., a virtual timer. */
	public void setTimer(ITimer timer) {
	}
	
	/**  Getter for node instances, e.g. enabling to check their states. */
	public SubWrapper getSub() {
		return sub;
	}
	
	// Method for closing the topics
	public void closeTopics(){
		messageOfPub.closeTopic();
	}
	
}
