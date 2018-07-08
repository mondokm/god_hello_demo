package hu.bme.mit.gamma.impl.interfaces;

import java.util.List;

public interface NotificationInterface {
	
	interface Provided extends Listener.Required {
		
		public boolean isRaisedReceived();
		
		void registerListener(Listener.Provided listener);
		List<Listener.Provided> getRegisteredListeners();
	}
	
	interface Required extends Listener.Provided {
		
		
		void registerListener(Listener.Required listener);
		List<Listener.Required> getRegisteredListeners();
	}
	
	interface Listener {
		
		interface Provided  {
			void raiseReceived();
		}
		
		interface Required   {
		}
		
	}
} 
