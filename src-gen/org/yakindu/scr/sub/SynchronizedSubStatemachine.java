package org.yakindu.scr.sub;
import java.util.List;

import org.yakindu.scr.sub.SubStatemachine.State;

/**
 * Runnable wrapper of SubStatemachine. This wrapper provides a thread-safe
 * instance of the state machine.
 * 
 * Please report bugs and issues...
 */

public class SynchronizedSubStatemachine implements ISubStatemachine {
	
	/**
	 * The core state machine is simply wrapped and the event processing will be
	 * delegated to that state machine instance. This instance will be created
	 * implicitly.
	 */
	protected SubStatemachine statemachine = new SubStatemachine();
	
	/**
	 * Interface object for SCIMessage
	 */		
	protected class SynchronizedSCIMessage implements SCIMessage {
		
		public void raiseHello() {
			
			synchronized (statemachine) {
				statemachine.getSCIMessage().raiseHello();
				statemachine.runCycle();
			}
		}
		
	};
	
	protected SCIMessage sCIMessage;
	
	/**
	 * Interface object for SCINotification
	 */		
	protected class SynchronizedSCINotification implements SCINotification {
		
		public List<SCINotificationListener> getListeners() {
			synchronized(statemachine) {
				return statemachine.getSCINotification().getListeners();
			}
		}
		
		public boolean isRaisedReceived() {
			synchronized(statemachine) {
				return statemachine.getSCINotification().isRaisedReceived();
			}
		}
		
	};
	
	protected SCINotification sCINotification;
	
	public SynchronizedSubStatemachine() {
		sCIMessage = new SynchronizedSCIMessage();
		sCINotification = new SynchronizedSCINotification();
	}
	
	public synchronized SCIMessage getSCIMessage() {
		return sCIMessage;
	}
	public synchronized SCINotification getSCINotification() {
		return sCINotification;
	}
	
	/**
	 * init() will be delegated thread-safely to the wrapped state machine.
	 */
	public void init() {
		synchronized(statemachine) {
			statemachine.init();
		}
	}
	
	/**
	 * enter() will be delegated thread-safely to the wrapped state machine.
	 */
	public void enter() {
		synchronized(statemachine) {
			statemachine.enter();
		}
	}
	
	/**
	 * exit() will be delegated thread-safely to the wrapped state machine.
	 */
	public void exit() {
		synchronized(statemachine) {
			statemachine.exit();
		}
	}
	
	/**
	 * isActive() will be delegated thread-safely to the wrapped state machine.
	 */
	public boolean isActive() {
		synchronized(statemachine) {
			return statemachine.isActive();
		}
	}
	
	/**
	 * isFinal() will be delegated thread-safely to the wrapped state machine.
	 */
	public boolean isFinal() {
		synchronized(statemachine) {
			return statemachine.isFinal();
		}
	}
	
	/**
	 * isStateActive() will be delegated thread-safely to the wrapped state machine.
	 */
	public boolean isStateActive(State state) {
		synchronized(statemachine) {
			return statemachine.isStateActive(state);
		}
	}
	
	/**
	 * runCycle() will be delegated thread-safely to the wrapped state machine.
	 */ 
	@Override
	public void runCycle() {
		synchronized (statemachine) {
			statemachine.runCycle();
		}
	}
}
