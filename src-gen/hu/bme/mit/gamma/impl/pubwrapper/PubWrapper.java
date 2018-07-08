package hu.bme.mit.gamma.impl.pubwrapper;

import java.util.Collections;
import java.util.List;

import lbmq.*; 
import org.yakindu.scr.*; 
import hu.bme.mit.gamma.impl.event.*;
import hu.bme.mit.gamma.impl.interfaces.*;

import hu.bme.mit.gamma.impl.pub.*;

public class PubWrapper implements Runnable, PubWrapperInterface {			
	// Thread running this wrapper instance
	private Thread thread;
	// Wrapped synchronous instance
	private PubStatechart pubStatechart = new PubStatechart();
	// Control port instances
	// Wrapped port instances
	private Message message = new Message();
	// Clocks
	private ITimer timerService;
	private final int clockSignal = 0;
	// Main queue
	private LinkedBlockingMultiQueue<String, Event> __asyncQueue = new LinkedBlockingMultiQueue<String, Event>();
	// Subqueues
	private LinkedBlockingMultiQueue<String, Event>.SubQueue executionMessages;
	
	public PubWrapper(ITimer timer) {
		setTimer(timer);
		// Init is done in setTimer
	}
	
	public PubWrapper() {
		timerService = new TimerService();
		init();
	}
	
	/** Enters the wrapped component. Should be used only be the container (composite system) class. */
	public void reset() {
		pubStatechart.reset();
	}
	
	/** Creates the subqueues, clocks and enters the wrapped synchronous component. */
	private void init() {
		// Creating subqueues: the negative conversion regarding prioirities is needed,
		// beacase the lbmq defines higher priority with lower integer values
		__asyncQueue.addSubQueue("executionMessages", -(2), 8);
		executionMessages = __asyncQueue.getSubQueue("executionMessages");
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
	public class Message implements MessageInterface.Provided {
		
		
		@Override
		public boolean isRaisedHello() {
			return pubStatechart.getMessage().isRaisedHello();
		}
		
		@Override
		public void registerListener(MessageInterface.Listener.Provided listener) {
			pubStatechart.getMessage().registerListener(listener);
		}
		
		@Override
		public List<MessageInterface.Listener.Provided> getRegisteredListeners() {
			return pubStatechart.getMessage().getRegisteredListeners();
		}
		
	}
	
	@Override
	public Message getMessage() {
		return message;
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
			default:
				throw new IllegalArgumentException("No such event!");
		}
	}
	
	private void performControlActions(Event event) {
		String[] eventName = event.getEvent().split("\\.");
		// Clock trigger
		if (eventName.length == 1 && eventName[0].equals("clockSignal")) {
			pubStatechart.runCycle();
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
	
	public PubStatechart getPubStatechart() {
		return pubStatechart;
	}
	
	public void setTimer(ITimer timer) {
		timerService = timer;
		pubStatechart.setTimer(timer);
		init(); // To set the service into functioning state wih clocks
	}
	
	
}
