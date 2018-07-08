package hu.bme.mit.gamma.impl.channels;

import hu.bme.mit.gamma.impl.interfaces.NotificationInterface;

public interface NotificationChannelInterface {	    	
	
	void registerPort(NotificationInterface.Provided providedPort);
	
	void registerPort(NotificationInterface.Required requiredPort);

}
