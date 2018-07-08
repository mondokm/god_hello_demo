package hu.bme.mit.gamma.impl.sub;

import hu.bme.mit.gamma.impl.interfaces.NotificationInterface;
import hu.bme.mit.gamma.impl.interfaces.MessageInterface;

public interface SubStatechartInterface {
	
	NotificationInterface.Provided getNotification();
	MessageInterface.Required getMessage();
	
	void runCycle();
	
} 
