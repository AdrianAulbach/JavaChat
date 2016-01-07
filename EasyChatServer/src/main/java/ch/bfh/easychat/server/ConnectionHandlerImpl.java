package ch.bfh.easychat.server;

import ch.bfh.easychat.common.EasyMessage;
import ch.bfh.easychat.server.core.ConnectionHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Samuel Egger
 */
public class ConnectionHandlerImpl implements ConnectionHandler {

    private Socket socket = null;
    private boolean shutdown = false;
    private final MessageProvider messageProvider;

    /**
     * The default constructor.
     *
     * @param messageProvider the MessageProvider.
     */
    public ConnectionHandlerImpl(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(Socket socket) throws IOException {
        this.socket = socket;
        try (
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));) {
            handleInternal(in, out);
        } finally {
            shutdown();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isShutdown() {
        return shutdown;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown() throws IOException {
        if (!shutdown) {
            shutdown = true;
            if (socket != null) {
                socket.close();
            }
        }
    }

    /**
     * Implementation of the client-server protocol.
     * 
     * @param in input stream
     * @param out output stream
     * 
     * @throws IOException 
     */
    private void handleInternal(BufferedReader in, PrintWriter out) throws IOException {
        
        /*
        The basic pattern is as follows:
        
        while(!shutdown){
            read input()
            if(messages pending){
                write messages()
            }
        }
        */

        String line = "";
        while (!shutdown) {
            if (in.ready()) {
                int data = in.read();
                if (data == 0) {
                    continue;
                }
                line += (char) data;
            } else if (!line.isEmpty()) {
                messageProvider.broadcast(new EasyMessage(line));
                line = "";
            } else {
                EasyMessage[] messages = messageProvider.fetch();
                for (EasyMessage message : messages) {
                    out.write(message.getMessage());
                    out.flush();
                }
            }
        }
    }
}
