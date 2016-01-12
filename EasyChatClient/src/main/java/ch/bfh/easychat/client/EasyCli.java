package ch.bfh.easychat.client;

import java.util.Scanner;

import ch.bfh.easychat.common.EasyMessage;

public class EasyCli implements ProtocolObserver {

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
		EasyProtocol protocol = new EasyProtocol();
		protocol.addObserver(this);
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
	
	public void onMessageRecieved(EasyMessage msg) {
		// TODO Auto-generated method stub
		System.out.println(msg.getMessage());
	}

}
