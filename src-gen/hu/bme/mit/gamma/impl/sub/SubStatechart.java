package hu.bme.mit.gamma.impl.sub;

import java.util.Queue;
import java.util.List;
import java.util.LinkedList;

import hu.bme.mit.gamma.impl.event.*;
import hu.bme.mit.gamma.impl.interfaces.*;
import org.yakindu.scr.sub.ISubStatemachine.SCINotificationListener;
import org.yakindu.scr.sub.SubStatemachine;
import org.yakindu.scr.sub.SubStatemachine.State;

public class SubStatechart implements SubStatechartInterface {
	// The wrapped Yakindu statemachine
	private SubStatemachine subStatemachine = new SubStatemachine();
	// Port instances
	private Message message = new Message();
	private Notification notification = new Notification();
	// Indicates which queues are active in this cycle
	private boolean insertQueue = true;
	private boolean processQueue = false;
	// Event queues for the synchronization of statecharts
	private Queue<Event> eventQueue1 = new LinkedList<Event>();
	private Queue<Event> eventQueue2 = new LinkedList<Event>();
	
	public SubStatechart() {
		// Initializing and entering the wrapped statemachine
		subStatemachine.init();
		subStatemachine.enter();
	}
	
	/** Resets the statemachine. Should be used only be the container (composite system) class. */
	public void reset() {
		subStatemachine.enter();
	}
	
	/** Changes the event queues of the component instance. Should be used only be the container (composite system) class. */
	public void changeEventQueues() {
		insertQueue = !insertQueue;
		processQueue = !processQueue;
	}
	
	/** Changes the event queues to which the events are put. Should be used only be a cascade container (composite system) class. */
	public void changeInsertQueue() {
	    insertQueue = !insertQueue;
	}
	
	/** Returns whether the eventQueue containing incoming messages is empty. Should be used only be the container (composite system) class. */
	public boolean isEventQueueEmpty() {
		return getInsertQueue().isEmpty();
	}
	
	/** Returns the event queue into which events should be put in the particular cycle. */
	private Queue<Event> getInsertQueue() {
		if (insertQueue) {
			return eventQueue1;
		}
		return eventQueue2;
	}
	
	/** Returns the event queue from which events should be inspected in the particular cycle. */
	private Queue<Event> getProcessQueue() {
		if (processQueue) {
			return eventQueue1;
		}
		return eventQueue2;
	}
	
	/** Changes event queues and initiating a cycle run. */
	@Override
	public void runCycle() {
		changeEventQueues();
		runComponent();
	}
	
	/** Changes the insert queue and initiates a run. */
	public void runAndRechangeInsertQueue() {
		// First the insert queue is changed back, so self-event sending can work
	    changeInsertQueue();
	    runComponent();
	}
	
	/** Initiates a cycle run without changing the event queues. It is needed if this component is contained (wrapped) by another component.
	Should be used only be the container (composite system) class. */
	public void runComponent() {
		Queue<Event> eventQueue = getProcessQueue();
		while (!eventQueue.isEmpty()) {
				Event event = eventQueue.remove();
				switch (event.getEvent()) {
					case "Message.Hello": 
						subStatemachine.getSCIMessage().raiseHello();
					break;
					default:
						throw new IllegalArgumentException("No such event!");
				}
		}
		subStatemachine.runCycle();
	}    		
	
	// Inner classes representing Ports
	public class Message implements MessageInterface.Required {
		private List<MessageInterface.Listener.Required> registeredListeners = new LinkedList<MessageInterface.Listener.Required>();

		@Override
		public void raiseHello() {
			getInsertQueue().add(new Event("Message.Hello", null));
		}

		@Override
		public void registerListener(MessageInterface.Listener.Required listener) {
			registeredListeners.add(listener);
		}
		
		@Override
		public List<MessageInterface.Listener.Required> getRegisteredListeners() {
			return registeredListeners;
		}

	}
	
	@Override
	public Message getMessage() {
		return message;
	}
	
	public class Notification implements NotificationInterface.Provided {
		private List<NotificationInterface.Listener.Provided> registeredListeners = new LinkedList<NotificationInterface.Listener.Provided>();


		@Override
		public boolean isRaisedReceived() {
			return subStatemachine.getSCINotification().isRaisedReceived();
		}
		@Override
		public void registerListener(NotificationInterface.Listener.Provided listener) {
			registeredListeners.add(listener);
			subStatemachine.getSCINotification().getListeners().add(new SCINotificationListener() {
				@Override
				public void onReceivedRaised() {
					listener.raiseReceived();
				}
			});
		}
		
		@Override
		public List<NotificationInterface.Listener.Provided> getRegisteredListeners() {
			return registeredListeners;
		}

	}
	
	@Override
	public Notification getNotification() {
		return notification;
	}
	
	/** Checks whether the wrapped statemachine is in the given state. */
	public boolean isStateActive(State state) {
		return subStatemachine.isStateActive(state);
	}
	
	
}
