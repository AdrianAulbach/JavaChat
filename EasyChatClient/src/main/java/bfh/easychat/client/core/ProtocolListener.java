package bfh.easychat.client.core;

import ch.bfh.easychat.common.EasyMessage;
/**
 * Interface that the listener that is passed to a protocol (interface Protocol) must implement 
 * @author Adrian Aulbach
 *
 */
public interface ProtocolListener {

	/**
	 * gets called to notify the listener of new messages
	 * @param msg Recieved message
	 */
    void messageRecieved(EasyMessage msg);
    
    /**
     * Gets called to notify the listener when the connection to the server is lost
     */
    void connectionLost();
}
