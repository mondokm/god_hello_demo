package hu.bme.mit.gamma.impl.subwrapper;

import hu.bme.mit.gamma.impl.interfaces.NotificationInterface;
import hu.bme.mit.gamma.impl.interfaces.MessageInterface;

public interface SubWrapperInterface {
	
	NotificationInterface.Provided getNotification();
	MessageInterface.Required getMessage();
	
	
} 
