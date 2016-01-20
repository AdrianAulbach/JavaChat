package ch.bfh.easychat.server;

import ch.bfh.easychat.server.core.InputFilter;
import ch.bfh.easychat.common.EasyMessage;
import ch.bfh.easychat.server.core.PlainInput;
import java.util.logging.Level;

/**
 * Handles all incoming chat messages.
 * 
 * @author Samuel
 */
public class MessageFilter implements InputFilter {

    /**
     * Reference to message provider.
     */
    private final MessageProvider provider;

    /**
     * Creates a new MessageFilter object.
     *
     * @param provider
     */
    public MessageFilter(MessageProvider provider) {
        this.provider = provider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String filter(PlainInput input) {
        EasyMessage message = EasyMessage.load(input.getPainInput());
        if (message != null) {
            provider.broadcast(message);
            Server.LOGGER.log(Level.FINE, "Received message; {0}", message.getMessage());
            input.onHandled();
        }

        return "";
    }
}
