package pubnode;

import hu.bme.mit.gamma.impl.pubnode.PubNode;

public class PubProgram {

	public static void main(String[] args) {
		PubNode pub=new PubNode();
		pub.start();
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
