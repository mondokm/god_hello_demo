package hu.bme.mit.gamma.impl.pub;

import hu.bme.mit.gamma.impl.interfaces.MessageInterface;

public interface PubStatechartInterface {
	
	MessageInterface.Provided getMessage();
	
	void runCycle();
	
} 
