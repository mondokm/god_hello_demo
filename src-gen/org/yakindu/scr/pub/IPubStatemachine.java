package org.yakindu.scr.pub;

import java.util.List;
import org.yakindu.scr.IStatemachine;
import org.yakindu.scr.ITimerCallback;

public interface IPubStatemachine extends ITimerCallback,IStatemachine {

	public interface SCIMessage {
	
		public boolean isRaisedHello();
		
	public List<SCIMessageListener> getListeners();
	}
	
	public interface SCIMessageListener {
	
		public void onHelloRaised();
		}
	
	public SCIMessage getSCIMessage();
	
}
