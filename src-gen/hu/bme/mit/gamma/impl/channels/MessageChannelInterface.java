package hu.bme.mit.gamma.impl.channels;

import hu.bme.mit.gamma.impl.interfaces.MessageInterface;

public interface MessageChannelInterface {	    	
	
	void registerPort(MessageInterface.Provided providedPort);
	
	void registerPort(MessageInterface.Required requiredPort);

}
