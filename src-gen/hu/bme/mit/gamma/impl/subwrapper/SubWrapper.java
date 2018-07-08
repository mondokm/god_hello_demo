package hu.bme.mit.gamma.impl.subwrapper;

import java.util.Collections;
import java.util.List;

import lbmq.*; 
import org.yakindu.scr.*; 
import hu.bme.mit.gamma.impl.event.*;
import hu.bme.mit.gamma.impl.interfaces.*;

import hu.bme.mit.gamma.impl.sub.*;

public class SubWrapper implements Runnable, SubWrapperInterface {			
	// Thread running this wrapper instance
	private Thread thread;
	// Wrapped synchronous instance
	private SubStatechart subStatechart = new SubStatechart();
	// Control port instances
	// Wrapped port instances
	private Message message = new Message();
	private Notification notification = new Notification();
	// Clocks
	private ITimer timerService;
	private final int clockSignal = 0;
	// Main queue
	private LinkedBlockingMultiQueue<String, Event> __asyncQueue = new LinkedBlockingMultiQueue<String, Event>();
	// Subqueues
	private LinkedBlockingMultiQueue<String, Event>.SubQueue executionMessages;
	private LinkedBlockingMultiQueue<String, Event>.SubQueue pingMessages;
	
	public SubWrapper(ITimer timer) {
		setTimer(timer);
		// Init is done in setTimer
	}
	
	public SubWrapper() {
		timerService = new TimerService();
		init();
	}
	
	/** Enters the wrapped component. Should be used only be the container (composite system) class. */
	public void reset() {
		subStatechart.reset();
	}
	
	/** Creates the subqueues, clocks and enters the wrapped synchronous component. */
	private void init() {
		// Creating subqueues: the negative conversion regarding prioirities is needed,
		// beacase the lbmq defines higher priority with lower integer values
		__asyncQueue.addSubQueue("executionMessages", -(2), 8);
		executionMessages = __asyncQueue.getSubQueue("executionMessages");
		__asyncQueue.addSubQueue("pingMessages", -(1), 8);
		pingMessages = __asyncQueue.getSubQueue("pingMessages");
		// Creating clock callbacks for the single timer service
		timerService.setTimer(createTimerCallback(), clockSignal, 300, true);
		// Entering the statecharts
		reset();
		// The thread has to be started manually
	}
	
	private ITimerCallback createTimerCallback() {
		return new ITimerCallback() {
			@Override
			public void timeElapsed(int eventId) {
				switch (eventId) {
					case clockSignal:
						executionMessages.offer(new Event("clockSignal", null));
					break;
					default:
						throw new IllegalArgumentException("No such event id: " + eventId);
				}
			}
		};
	}
	
	// Inner classes representing control ports
	
	// Inner classes representing wrapped ports
	public class Message implements MessageInterface.Required {
		
		@Override
		public void raiseHello() {
			pingMessages.offer(new Event("message.hello", null));
		}
		
		
		@Override
		public void registerListener(MessageInterface.Listener.Required listener) {
			subStatechart.getMessage().registerListener(listener);
		}
		
		@Override
		public List<MessageInterface.Listener.Required> getRegisteredListeners() {
			return subStatechart.getMessage().getRegisteredListeners();
		}
		
	}
	
	@Override
	public Message getMessage() {
		return message;
	}
	
	public class Notification implements NotificationInterface.Provided {
		
		
		@Override
		public boolean isRaisedReceived() {
			return subStatechart.getNotification().isRaisedReceived();
		}
		
		@Override
		public void registerListener(NotificationInterface.Listener.Provided listener) {
			subStatechart.getNotification().registerListener(listener);
		}
		
		@Override
		public List<NotificationInterface.Listener.Provided> getRegisteredListeners() {
			return subStatechart.getNotification().getRegisteredListeners();
		}
		
	}
	
	@Override
	public Notification getNotification() {
		return notification;
	}
	
	/** Operation. */
	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			try {
				Event event = __asyncQueue.take();		
				if (!isControlEvent(event)) {
					// Event is forwarded to the wrapped component
					forwardEvent(event);
				}
				performControlActions(event);		
			} catch (InterruptedException e) {
				thread.interrupt();
				System.out.println("Interrupted");
			}
		}
	}
	
	private boolean isControlEvent(Event event) {
		String portName = event.getEvent().split("\\.")[0];
		return portName.equals("clockSignal");
	}
	
	private void forwardEvent(Event event) {
		switch (event.getEvent()) {
			case "message.hello":
				subStatechart.getMessage().raiseHello();
			break;
			default:
				throw new IllegalArgumentException("No such event!");
		}
	}
	
	private void performControlActions(Event event) {
		String[] eventName = event.getEvent().split("\\.");
		// Clock trigger
		if (eventName.length == 1 && eventName[0].equals("clockSignal")) {
			subStatechart.runCycle();
			return;
		}
	}
	
	/** Starts this wrapper instance on a thread. */
	public void start() {
		thread = new Thread(this);
		thread.start();
	}
	
	public boolean isWaiting() {
		return thread.getState() == Thread.State.WAITING;
	}
	
	/** Stops the thread running this wrapper instance. */
	public void interrupt() {
		thread.interrupt();
	}
	
	public SubStatechart getSubStatechart() {
		return subStatechart;
	}
	
	public void setTimer(ITimer timer) {
		timerService = timer;
		init(); // To set the service into functioning state wih clocks
	}
	
	
}
