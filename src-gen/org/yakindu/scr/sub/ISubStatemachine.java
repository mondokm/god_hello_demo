package org.yakindu.scr.sub;

import java.util.List;
import org.yakindu.scr.IStatemachine;

public interface ISubStatemachine extends IStatemachine {

	public interface SCIMessage {
	
		public void raiseHello();
		
	}
	
	public SCIMessage getSCIMessage();
	
	public interface SCINotification {
	
		public boolean isRaisedReceived();
		
	public List<SCINotificationListener> getListeners();
	}
	
	public interface SCINotificationListener {
	
		public void onReceivedRaised();
		}
	
	public SCINotification getSCINotification();
	
}
