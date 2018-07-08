package hu.bme.mit.gamma.impl.channels;

import hu.bme.mit.gamma.impl.interfaces.MessageInterface;
import java.util.List;
import java.util.LinkedList;

public class MessageChannel implements MessageChannelInterface {
	
	private MessageInterface.Provided providedPort;
	private List<MessageInterface.Required> requiredPorts = new LinkedList<MessageInterface.Required>();
	
	public MessageChannel() {}
	
	public MessageChannel(MessageInterface.Provided providedPort) {
		this.providedPort = providedPort;
	}
	
	public void registerPort(MessageInterface.Provided providedPort) {
		// Former port is forgotten
		this.providedPort = providedPort;
		// Registering the listeners
		for (MessageInterface.Required requiredPort : requiredPorts) {
			providedPort.registerListener(requiredPort);
			requiredPort.registerListener(providedPort);
		}
	}
	
	public void registerPort(MessageInterface.Required requiredPort) {
		requiredPorts.add(requiredPort);
		// Checking whether a provided port is already given
		if (providedPort != null) {
			providedPort.registerListener(requiredPort);
			requiredPort.registerListener(providedPort);
		}
	}

}
