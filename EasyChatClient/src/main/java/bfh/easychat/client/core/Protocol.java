package bfh.easychat.client.core;

import ch.bfh.easychat.common.EasyMessage;

/**
 * Interface between client/GUI and implementation of communication with server
 * @author Adrian Aulbach
 *
 */
public interface Protocol {
	/**
	 * Sends a Message of type EasyMessage to the Server
	 * @param message the message to send
	 * @return true on success
	 */
	public boolean sendMessage(EasyMessage message);
	/**
	 * Sends a String as message to the Server
	 * @param message String of message content
	 * @return true on success
	 */
    public boolean sendMessage(String message);

    /**
     * Connects to a server.
     * @param String host Server's host (IP, URL, localhost)
     * @param int port Port on which the server is listening
     * @param String user The username that will be added as sender
     * @return true on success
     */
    public boolean connect(String host, int port, String user);

    /**
     * Disconnect from the server
     */
    public void disconnect();

    /**
     * sets the listener class, that will be notified when a message is recieved or the connection is lost
     * @param mainApp
     */
    public void setProtocolListener(ProtocolListener mainApp);
}
