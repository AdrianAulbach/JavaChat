package ch.bfh.easychat.client;

import java.util.AbstractList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import ch.bfh.easychat.common.EasyMessage;
//import ch.bfh.easychat.server.InputBuffer;

public class EasyProtocol implements Protocol, Runnable {
	private Socket socket = null;
	private InputStream in = null;
	BufferedReader buffer = null;
	private OutputStream out = null;
	private ProtocolObserver observer = null;
	
	
	public boolean sendMessage(EasyMessage msg) {
		try {
			out.write(msg.toJson().getBytes());
			out.write(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean connect(String host, int port, String user) {
		try {
			socket = new Socket(host, port);
			in = socket.getInputStream();
			buffer = new BufferedReader(new InputStreamReader(in));
			out = socket.getOutputStream();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		Connection connection = new Connection();
		connection.start();
		return true;
	}

	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

	private class Connection extends Thread {
		//public Connection()
		
		public synchronized void run() {
			// TODO Auto-generated method stub
			while(true) {
				int c = 0;
				String json = "";
				try {
					while((c = buffer.read()) > 0) {
						json += (char) c;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(c <= 0) {
					EasyMessage msg = EasyMessage.load(json);
					observer.onMessageRecieved(msg);
				}
			}
		}
	}

	public void addObserver(ProtocolObserver mainApp) {
		this.observer = mainApp;
	}

	public void run() {
		// TODO Auto-generated method stub
		while(socket.isConnected()) {
			int c = 0;
			String json = "";
			try {
				while((c = buffer.read()) > 0) {
					json += (char) c;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(c <= 0) {
				EasyMessage msg = EasyMessage.load(json);
			}
		}
	}
}
