package hu.bme.mit.gamma.impl.pubnode;

import hu.bme.mit.gamma.impl.channels.dds.*;

import java.util.List;
import org.yakindu.scr.ITimer;

import hu.bme.mit.gamma.impl.interfaces.*;
import hu.bme.mit.gamma.impl.channels.*;
import hu.bme.mit.gamma.impl.subwrapper.*;
import hu.bme.mit.gamma.impl.pubwrapper.*;

public class PubNode  {			
	// Component instances
	private PubWrapper pub = new PubWrapper();
	// Port instances
	// Inner channel instances
	// Outer channel topics
	private MessageChannelDDS messageOfPub;
	
	public PubNode() {
		init();
	}
	
	/** Enters the contained statemachines recursively. Should be used only be the container (composite system) class. */
	public void reset() {
		pub.reset();
	}
	
	/** Creates the channel mappings and enters the wrapped statemachines. */
	private void init() {
		// Registration of simple channels
		// Registration of broadcast channels
		// Instantiation of topics
		messageOfPub = new MessageChannelDDS("messageOfPub","","");
		messageOfPub.setupWriter();
		pub.getMessage().registerListener(new MessageOfPubListener());
		reset();
	}
	
	// Inner classes representing Ports
	
	// Inner classes for publishing events
	class MessageOfPubListener implements MessageInterface.Listener.Provided {
	
		public void raiseHello() {
			messageOfPub.raiseHello();
		}	
			
	
	}	
	
	// Inner classes for receiving events
	
	/** Starts the running of the asynchronous node. */
	public void start() {
		pub.start();
	}
	
	/** Setter for the timer e.g., a virtual timer. */
	public void setTimer(ITimer timer) {
		pub.setTimer(timer);
	}
	
	/**  Getter for node instances, e.g. enabling to check their states. */
	public PubWrapper getPub() {
		return pub;
	}
	
	// Method for closing the topics
	public void closeTopics(){
		messageOfPub.closeTopic();
	}
	
}
