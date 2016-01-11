package bfh.easychat.client.fx;

import bfh.easychat.client.core.ProtocolListener;
import bfh.easychat.client.serverclient.ProtocolImpl;
import java.util.Scanner;

import ch.bfh.easychat.common.EasyMessage;

public class EasyCli implements ProtocolListener {

	public static void main(String args[]) {
		(new EasyCli()).doStuff(args);
	}
	
	public void doStuff(String args[]) {
		String server;
		int port;
		Scanner scn = new Scanner(System.in);
		
		System.out.print("Enter server: ");
		server = scn.nextLine();
		System.out.print("Enter port: ");
		port = scn.nextInt();
		
		System.out.println("Connecting ...");
		ProtocolImpl protocol = new ProtocolImpl();
//		protocol.addProtocolListener(this);
		if(protocol.connect(server, port, "test-user")) {
			System.out.println("Connected");
		} else {
			System.out.println("asldjf");
		}
//		Thread t1 = new Thread(protocol);
//		t1.start();
		while(true){
			System.out.print(".");
			System.out.flush();
		}
		
//		scn.close();
		
	}
	
	public void messageRecieved(EasyMessage msg) {
		// TODO Auto-generated method stub
		System.out.println(msg.getMessage());
	}

}
