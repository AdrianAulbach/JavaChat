package ch.bfh.easychat.server;

import ch.bfh.easychat.common.EasyMessage;
import java.util.ArrayList;
import java.util.List;

/**
 * The message provider encapsulates a message source and provides utilities to
 * get and send messages.
 *
 * @author Samuel Egger
 */
public class MessageProvider {

    private final static Object LOCK_MESSAGE_SOURCE = new Object();
    private int restoreCurrentMessagePointer;
    private int currentMessagePointer;
    private final List<EasyMessage> messagesSource;

    /**
     * Creates a new MessageProvider object.
     *
     * @param messagesSource the message source
     */
    public MessageProvider(List<EasyMessage> messagesSource) {
        this.messagesSource = messagesSource;
        
        int size;
        synchronized (LOCK_MESSAGE_SOURCE) {
            size = messagesSource.size();
        }
        
        currentMessagePointer = size;
        restoreCurrentMessagePointer = currentMessagePointer;
    }

    /**
     * Returns all new messages that are not yet sent to the client.
     *
     * @return new messages
     */
    public EasyMessage[] fetch() {
        int size = messagesSource.size();
        restoreCurrentMessagePointer = currentMessagePointer;
        
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
     * Undo last fetch. Call if processing of the fetched messages failed.
     */
    public void rollback(){
        currentMessagePointer = restoreCurrentMessagePointer;
    }

    /**
     * Checks if there is any message not yet handled.
     *
     * @return true if there is any new message, otherwise false
     */
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
     * @param message message to broadcast
     */
    public void broadcast(EasyMessage message) {
        synchronized (LOCK_MESSAGE_SOURCE) {
            // We simply use the actual index of the message as its id. But this
            // might as well be any other unique number.
            int size = messagesSource.size();
            message.setId(size);

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
            for (int i = messages.length - 1; i >= 0; i--) {
                messages[i] = messagesSource.get(size - (i + 1));
            }
        }
        return messages;
    }

    /**
     * Returns all top messages until (exclusively!) the message with the
     * specified id. The method returns maximal 100 messages.
     *
     * @param id
     * @return all top messages until (exclusively!) the message with the
     * specified id
     */
    public EasyMessage[] queryUntilId(long id) {
        List<EasyMessage> messages = new ArrayList<>();
        synchronized (LOCK_MESSAGE_SOURCE) {
            for (int i = messagesSource.size() - 1; i <= messagesSource.size() - 101; i--) {
                EasyMessage msg = messagesSource.get(i);
                if (id == msg.getId()) {
                    break;
                }

                messages.add(msg);
            }
        }

        return messages.toArray(new EasyMessage[messages.size()]);
    }
}
