package ch.bfh.easychat.client;

import java.util.AbstractList;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import ch.bfh.easychat.common.EasyMessage;

public class EasyProtocol implements Protocol {
	private Socket socket = null;
	
	public boolean sendMessage(EasyMessage msg) {
		// TODO Auto-generated method stubE	
		return false;
	}

	public boolean connect(String host, int port, String user) {
		try {
			socket = new Socket(host, port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void disconnect() {
		// TODO Auto-generated method stub

	}

	private class Connection extends Thread {
		
		
		public synchronized void run() {
			
		}
	}

	public void addObserver(ProtocolObserver mainApp) {
		// TODO Auto-generated method stub
		
	}
}
