package hu.bme.mit.gamma.impl.hello;

import java.util.List;
import org.yakindu.scr.ITimer;

import hu.bme.mit.gamma.impl.interfaces.*;
import hu.bme.mit.gamma.impl.channels.*;
import hu.bme.mit.gamma.impl.pubwrapper.*;
import hu.bme.mit.gamma.impl.subwrapper.*;

public class Hello implements HelloInterface {			
	// Component instances
	private PubWrapper pub = new PubWrapper();
	private SubWrapper sub = new SubWrapper();
	// Port instances
	private Notification notification = new Notification();
	// Channel instances
	private MessageChannelInterface channelMessageOfPub;
	
	public Hello(ITimer timer) {
		setTimer(timer);
		init();
	}
	
	public Hello() {
		init();
	}
	
	/** Resets the contained statemachines recursively. Should be used only be the container (composite system) class. */
	public void reset() {
		pub.reset();
		sub.reset();
	}
	
	/** Creates the channel mappings and enters the wrapped statemachines. */
	private void init() {
		// Registration of simple channels
		channelMessageOfPub = new MessageChannel(pub.getMessage());
		channelMessageOfPub.registerPort(sub.getMessage());
		// Registration of broadcast channels
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
	
	@Override
	public Notification getNotification() {
		return notification;
	}
	
	/** Starts the running of the asynchronous component. */
	public void start() {
		pub.start();
		sub.start();
	}
	
	public boolean isWaiting() {
		return pub.isWaiting() && sub.isWaiting();
	}
	
	/** Setter for the timer e.g., a virtual timer. */
	public void setTimer(ITimer timer) {
		pub.setTimer(timer);
	}
	
	/**  Getter for component instances, e.g. enabling to check their states. */
	public PubWrapper getPub() {
		return pub;
	}
	
	public SubWrapper getSub() {
		return sub;
	}
	
	
}
