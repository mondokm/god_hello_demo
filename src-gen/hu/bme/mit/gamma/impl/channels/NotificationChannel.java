package hu.bme.mit.gamma.impl.channels;

import hu.bme.mit.gamma.impl.interfaces.NotificationInterface;
import java.util.List;
import java.util.LinkedList;

public class NotificationChannel implements NotificationChannelInterface {
	
	private NotificationInterface.Provided providedPort;
	private List<NotificationInterface.Required> requiredPorts = new LinkedList<NotificationInterface.Required>();
	
	public NotificationChannel() {}
	
	public NotificationChannel(NotificationInterface.Provided providedPort) {
		this.providedPort = providedPort;
	}
	
	public void registerPort(NotificationInterface.Provided providedPort) {
		// Former port is forgotten
		this.providedPort = providedPort;
		// Registering the listeners
		for (NotificationInterface.Required requiredPort : requiredPorts) {
			providedPort.registerListener(requiredPort);
			requiredPort.registerListener(providedPort);
		}
	}
	
	public void registerPort(NotificationInterface.Required requiredPort) {
		requiredPorts.add(requiredPort);
		// Checking whether a provided port is already given
		if (providedPort != null) {
			providedPort.registerListener(requiredPort);
			requiredPort.registerListener(providedPort);
		}
	}

}
