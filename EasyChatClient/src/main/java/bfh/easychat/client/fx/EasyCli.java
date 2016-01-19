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
		protocol.setProtocolListener(this);
		if(protocol.connect(server, port, "test-user")) {
//			Thread t1 = new Thread(protocol);
//			t1.start();
			System.out.println("Connected");
		} else {
			System.out.println("asldjf");
		}
		
		while(true){
			String msg = scn.nextLine();
			if(msg.equals("exit")) {
				break;
			}
			protocol.sendMessage(msg);
		}
		
		scn.close();
		return;
	}
	
	public void messageRecieved(EasyMessage msg) {
		// TODO Auto-generated method stub
		System.out.println(msg.getMessage());
	}

}
