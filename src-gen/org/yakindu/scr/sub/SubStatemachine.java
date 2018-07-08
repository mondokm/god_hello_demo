package org.yakindu.scr.sub;
import java.util.LinkedList;
import java.util.List;

public class SubStatemachine implements ISubStatemachine {

	protected class SCIMessageImpl implements SCIMessage {
	
		private boolean hello;
		
		public void raiseHello() {
			hello = true;
		}
		
		protected void clearEvents() {
			hello = false;
		}
	}
	
	protected SCIMessageImpl sCIMessage;
	
	protected class SCINotificationImpl implements SCINotification {
	
		private List<SCINotificationListener> listeners = new LinkedList<SCINotificationListener>();
		
		public List<SCINotificationListener> getListeners() {
			return listeners;
		}
		private boolean received;
		
		public boolean isRaisedReceived() {
			return received;
		}
		
		protected void raiseReceived() {
			received = true;
			for (SCINotificationListener listener : listeners) {
				listener.onReceivedRaised();
			}
		}
		
		protected void clearEvents() {
		}
		protected void clearOutEvents() {
		
		received = false;
		}
		
	}
	
	protected SCINotificationImpl sCINotification;
	
	private boolean initialized = false;
	
	public enum State {
		main_region_StateA,
		$NullState$
	};
	
	private final State[] stateVector = new State[1];
	
	private int nextStateIndex;
	
	
	
	public SubStatemachine() {
		sCIMessage = new SCIMessageImpl();
		sCINotification = new SCINotificationImpl();
	}
	
	public void init() {
		this.initialized = true;
		
		for (int i = 0; i < 1; i++) {
			stateVector[i] = State.$NullState$;
		}
		clearEvents();
		clearOutEvents();
	}
	
	public void enter() {
		if (!initialized) {
			throw new IllegalStateException(
					"The state machine needs to be initialized first by calling the init() function.");
		}
	
		enterSequence_main_region_default();
	}
	
	public void exit() {
		exitSequence_main_region();
	}
	
	/**
	 * @see IStatemachine#isActive()
	 */
	public boolean isActive() {
		return stateVector[0] != State.$NullState$;
	}
	
	/** 
	* Always returns 'false' since this state machine can never become final.
	*
	* @see IStatemachine#isFinal()
	*/
	public boolean isFinal() {
		return false;
	}
	/**
	* This method resets the incoming events (time events included).
	*/
	protected void clearEvents() {
		sCIMessage.clearEvents();
		sCINotification.clearEvents();
	}
	
	/**
	* This method resets the outgoing events.
	*/
	protected void clearOutEvents() {
		sCINotification.clearOutEvents();
	}
	
	/**
	* Returns true if the given state is currently active otherwise false.
	*/
	public boolean isStateActive(State state) {
	
		switch (state) {
		case main_region_StateA:
			return stateVector[0] == State.main_region_StateA;
		default:
			return false;
		}
	}
	
	public SCIMessage getSCIMessage() {
		return sCIMessage;
	}
	
	public SCINotification getSCINotification() {
		return sCINotification;
	}
	
	private boolean check_main_region_StateA_tr0_tr0() {
		return sCIMessage.hello;
	}
	
	private void effect_main_region_StateA_tr0() {
		exitSequence_main_region_StateA();
		sCINotification.raiseReceived();
		
		enterSequence_main_region_StateA_default();
	}
	
	/* 'default' enter sequence for state StateA */
	private void enterSequence_main_region_StateA_default() {
		nextStateIndex = 0;
		stateVector[0] = State.main_region_StateA;
	}
	
	/* 'default' enter sequence for region main region */
	private void enterSequence_main_region_default() {
		react_main_region__entry_Default();
	}
	
	/* Default exit sequence for state StateA */
	private void exitSequence_main_region_StateA() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for region main region */
	private void exitSequence_main_region() {
		switch (stateVector[0]) {
		case main_region_StateA:
			exitSequence_main_region_StateA();
			break;
		default:
			break;
		}
	}
	
	/* The reactions of state StateA. */
	private void react_main_region_StateA() {
		if (check_main_region_StateA_tr0_tr0()) {
			effect_main_region_StateA_tr0();
		}
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region__entry_Default() {
		enterSequence_main_region_StateA_default();
	}
	
	public void runCycle() {
		if (!initialized)
			throw new IllegalStateException(
					"The state machine needs to be initialized first by calling the init() function.");
		clearOutEvents();
		for (nextStateIndex = 0; nextStateIndex < stateVector.length; nextStateIndex++) {
			switch (stateVector[nextStateIndex]) {
			case main_region_StateA:
				react_main_region_StateA();
				break;
			default:
				// $NullState$
			}
		}
		clearEvents();
	}
}
