package ch.bfh.easychat.server.core;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Samuel Egger
 */
public interface ConnectionHandler {
    
    /**
     * Handles the client connection.
     * 
     * @param socket the client socket
     * 
     * @throws IOException 
     */
    void handle(Socket socket) throws IOException;
    
    /**
     * @return returns true if the ConnectionHandler has been shutdown or
     * the client closed the connection.
     */
    boolean isShutdown();
    
    /**
     * Closes the connection to the client.
     * 
     * @throws IOException 
     */
    void shutdown() throws IOException;
}
