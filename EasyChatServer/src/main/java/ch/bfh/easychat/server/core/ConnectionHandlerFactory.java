package ch.bfh.easychat.server.core;

import ch.bfh.easychat.server.Server;

/**
 *
 * @author Samuel Egger
 */
public interface ConnectionHandlerFactory {
    
    /**
     * Creates a new ConnectionHandler object.
     * 
     * @param serverInstance
     * @return a ConnectionHandler object.
     */
    ConnectionHandler create(Server serverInstance);
}
