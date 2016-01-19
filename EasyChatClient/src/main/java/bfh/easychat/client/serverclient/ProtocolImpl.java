package bfh.easychat.client.serverclient;

import bfh.easychat.client.core.Protocol;
import bfh.easychat.client.core.ProtocolListener;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import ch.bfh.easychat.common.EasyMessage;
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
    private boolean shutdown = false;

    @Override
    public boolean sendMessage(String message) {
        if (socket.isConnected() && !socket.isClosed()) {
            try {
                OutputStream out = new BufferedOutputStream(socket.getOutputStream());

                EasyMessage easyMessage = new EasyMessage(message);
                byte[] messageBytes = easyMessage.toJson().getBytes(STREAM_ENCODING);
                //copying messageBytes to longer Array with 0 at the end
                byte[] output = Arrays.copyOf(messageBytes, messageBytes.length + 1);
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

    @Override
    public boolean connect(String host, int port, String user) {
        try {
            shutdown = false;
            this.user = user;
            socket = new Socket(host, port);
            (new Thread(this)).start();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return false;
        }
//        Thread t1 = new Thread(this);
//		t1.start();
        return true;
    }

    @Override
    public void disconnect() {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void setProtocolListener(ProtocolListener listener) {
        this.listener = listener;
    }

    @Override
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
                    buffer.buffer(data);
                    if (in.available() == 0 || data == 0) {
                        break;
                    }
                }

                if (!buffer.isEmpty()) {
                    String line = buffer.asString(STREAM_ENCODING);
                    List<EasyMessage> messages= EasyMessage.loadFromArray(line);
                    for(EasyMessage msg : messages) {
                    	this.listener.messageRecieved(msg);
                    }
                    buffer.reset();
                }
            } catch (IOException ex) {
                if (!shutdown) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
