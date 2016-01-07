package ch.bfh.easychat.server;

import ch.bfh.easychat.common.EasyMessage;
import java.util.List;

/**
 * The message provider encapsulates a message source.
 *
 * @author Samuel Egger
 */
public class MessageProvider {

    private final static Object LOCK_MESSAGE_SOURCE = new Object();
    private int currentMessagePointer = 0;
    private final List<EasyMessage> messagesSource;

    /**
     * The default constructor.
     *
     * @param messagesSource the message source
     */
    public MessageProvider(List<EasyMessage> messagesSource) {
        this.messagesSource = messagesSource;
    }

    /**
     * Returns all new messages that are not yet sent to the client.
     * 
     * @return new messages
     */
    public EasyMessage[] fetch() {
        int size = messagesSource.size();

        if (size == 0) {
            return new EasyMessage[0];
        }

        int numberOfNewMessages = size - currentMessagePointer;
        EasyMessage[] messages = new EasyMessage[numberOfNewMessages];

        int i = 0;
        for (; currentMessagePointer < size; currentMessagePointer++) {
            synchronized (LOCK_MESSAGE_SOURCE) {
                messages[i++] = messagesSource.get(currentMessagePointer);
            }
        }

        return messages;
    }

    /**
     * Queues a message to be broadcasted.
     * 
     * @param message 
     */
    public void broadcast(EasyMessage message) {
        synchronized (LOCK_MESSAGE_SOURCE) {
            messagesSource.add(message);
        }
    }
}
