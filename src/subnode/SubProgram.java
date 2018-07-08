package subnode;

import hu.bme.mit.gamma.impl.subnode.SubNode;

public class SubProgram {

	public static void main(String[] args) {
		SubNode sub=new SubNode();
		sub.getNotification().registerListener(()->{System.out.println("received");});
		sub.start();
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
