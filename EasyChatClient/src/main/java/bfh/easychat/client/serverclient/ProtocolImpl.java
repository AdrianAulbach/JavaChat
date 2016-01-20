package bfh.easychat.client.serverclient;

import bfh.easychat.client.core.Protocol;
import bfh.easychat.client.core.ProtocolListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import ch.bfh.easychat.common.EasyMessage;
import ch.bfh.easychat.common.EasyRequestUntil;
import ch.bfh.easychat.common.EasyRequestTop;
import ch.bfh.easychat.common.InputBuffer;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProtocolImpl implements Protocol, Runnable {

    private final static String STREAM_ENCODING = "UTF-8";
    private static final Logger LOGGER = Logger.getLogger(ProtocolImpl.class.getName());

    private Socket socket = null;
    private ProtocolListener listener = null;
    private String user = "";
    
    private long lastMessageID = -1;
    
    private boolean shutdown = false;
    private Thread thread;
    
    private boolean sendData(byte[] data) {
        if (socket.isConnected() && !socket.isClosed()) {
            try {
            	OutputStream out = new BufferedOutputStream(socket.getOutputStream());
                //copying data to longer Array with 0 at the end
                byte[] output = Arrays.copyOf(data, data.length + 1);
                out.write(output);
                out.flush();
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Sends a message to the server
     * @param EasyMessage message The message to send
     * @return true on success
     */
    public boolean sendMessage(EasyMessage message) {
    	try {
			return sendData(message.toJson().getBytes(STREAM_ENCODING));
		} catch (UnsupportedEncodingException e) {
			LOGGER.log(Level.SEVERE, "Unsupported encoding", e);
		}
    	return false;
    }
    
    /**
     * Sends a message to the server
     * @param String message the message to send
     * @return true on success
     */
    public boolean sendMessage(String message) {
        EasyMessage easyMessage = new EasyMessage(message, user);
        return sendMessage(easyMessage);
    }

    /**
     * Connects to the server and starts the Thread that listens for new messages
     * @param String host Server's host (IP, URL, localhost)
     * @param int port Port on which the server is listening
     * @param String user The username that will be added as sender
     */
    public boolean connect(String host, int port, String user) {
        try {
            shutdown = false;
            this.user = user;
            socket = new Socket(host, port);
            thread = new Thread(this);
            thread.start();
            
            //Ask server for last messages ...
            String request;
            if(lastMessageID > 0) {
            	//... since disconnect
            	EasyRequestUntil requestUntil = new EasyRequestUntil(lastMessageID);
            	request = requestUntil.toJson();
            } else {
            	//... no previos connection, fetch last 10 messages
            	EasyRequestTop requestTop = new EasyRequestTop(10);
            	request = requestTop.toJson();
            }
            sendData(request.getBytes(STREAM_ENCODING));
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * Disconnects from the server
     */
    public void disconnect() {
        shutdown = true;
    	if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Sets the listener that gets notified when a new message arrives
     * @param ProtocolListener listener The listener to notify
     */
    public void setProtocolListener(ProtocolListener listener) {
        this.listener = listener;
    }

    /**
     * Do not use this method
     * Only to be used by class Thread
     */
    public void run() {
        InputStream in;
        try {
            in = socket.getInputStream();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return;
        }

        InputBuffer buffer = new InputBuffer();
        while (!shutdown) {
            try {
                while (true) {
                    byte data = (byte) in.read();
                    if(data == 0) {
                    	break;
                    }
                    buffer.buffer(data);
                    if (in.available() == 0) {
                        break;
                    }
                }

                if (!buffer.isEmpty()) {
                    String line = buffer.asString(STREAM_ENCODING);
                    List<EasyMessage> messages= EasyMessage.loadFromArray(line);
                    if(this.listener != null) {
	                    for(EasyMessage msg : messages) {
	                    	//Don't notify listener if message is from the user itself
	                    	if(msg.getSender().equals(user)) {
	                    		continue;
	                    	}
	                    	this.listener.messageRecieved(msg);
	                    }
                    } else {
                    	LOGGER.log(Level.SEVERE, "No ProtocolListener set");
                    }
                    buffer.reset();
                }
            } catch (IOException ex) {
                if (!shutdown) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            }
        } 
        try {
			thread.join();
		} catch (InterruptedException e) {
			LOGGER.log(Level.SEVERE, "Failed to join thread", e);
		}
    }
}
