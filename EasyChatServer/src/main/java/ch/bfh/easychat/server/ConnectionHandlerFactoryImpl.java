package ch.bfh.easychat.server;

import ch.bfh.easychat.common.EasyMessage;
import ch.bfh.easychat.server.core.ConnectionHandler;
import ch.bfh.easychat.server.core.ConnectionHandlerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates an instance of the ConnectionHandlerImpl class.
 * 
 * @author Samuel Egger
 */
public class ConnectionHandlerFactoryImpl implements ConnectionHandlerFactory {

    /**
     * We simply use a static list as global message source.
     */
    private static final List<EasyMessage> MESSAGE_SOURCE = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public ConnectionHandler create(Server server) {
        MessageProvider provider = new MessageProvider(MESSAGE_SOURCE);
        return new ConnectionHandlerImpl(provider);
    }
}
