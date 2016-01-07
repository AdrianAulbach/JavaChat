package ch.bfh.easychat.server;

import ch.bfh.easychat.common.EasyMessage;
import ch.bfh.easychat.server.core.ConnectionHandler;
import ch.bfh.easychat.server.core.ConnectionHandlerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Samuel Egger
 */
public class ConnectionHandlerFactoryImpl implements ConnectionHandlerFactory {

    private static final List<EasyMessage> messageSource = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public ConnectionHandler create(Server server) {
        MessageProvider provider = new MessageProvider(messageSource);
        return new ConnectionHandlerImpl(provider);
    }
}
