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

    public boolean any() {
        int size;
        synchronized (LOCK_MESSAGE_SOURCE) {
            size = messagesSource.size();
        }
        return currentMessagePointer < size;
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

    /**
     * Returns the last top messages.
     * 
     * @param top top of messages to retrieve
     * @return an array with the last top messages
     */
    public EasyMessage[] queryTop(int top) {
        EasyMessage[] messages;
        synchronized (LOCK_MESSAGE_SOURCE) {
            int size = messagesSource.size();
            messages = new EasyMessage[Math.min(top, size)];
            for (int i = 0; i < messages.length; i++) {
                messages[i] = messagesSource.get(size - (i + 1));
            }
        }
        return messages;
    }
}
