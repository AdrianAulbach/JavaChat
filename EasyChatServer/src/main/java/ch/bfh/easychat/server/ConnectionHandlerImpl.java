package ch.bfh.easychat.server;

import ch.bfh.easychat.server.core.InputFilter;
import ch.bfh.easychat.common.InputBuffer;
import ch.bfh.easychat.common.EasyMessage;
import ch.bfh.easychat.server.core.ConnectionHandler;
import ch.bfh.easychat.server.core.PlainInput;
import com.eclipsesource.json.JsonArray;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 * Default implementation of the ConnectionHandler interface.
 *
 * @author Samuel Egger
 */
public class ConnectionHandlerImpl implements ConnectionHandler {

    private final static String STREAM_ENCODING = "UTF-8";
    private final static String WELCOME_MESSAGE = "Willkommen im EasyChat.";

    private Socket socket = null;
    private boolean shutdown = false;
    private final MessageProvider messageProvider;
    private final List<InputFilter> streamHandler = new ArrayList<>();

    /**
     * The default constructor.
     *
     * @param messageProvider the MessageProvider.
     */
    public ConnectionHandlerImpl(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
        streamHandler.add(new MessageFilter(messageProvider));
        streamHandler.add(new RequestTopFilter(messageProvider));
        streamHandler.add(new RequestUntilFilter(messageProvider));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(Socket socket) throws IOException {
        this.socket = socket;
        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();

        handleInternal(in, out);
        shutdown();
    }

    /**
     * Implementation of the client-server protocol.
     *
     * @param in input stream
     * @param out output stream
     *
     * @throws IOException
     */
    private void handleInternal(InputStream in, OutputStream out) throws IOException {
        InputBuffer buffer = new InputBuffer();

        EasyMessage welcomeMessage = new EasyMessage(WELCOME_MESSAGE, "Server");
        out.write(welcomeMessage.toJson().getBytes(STREAM_ENCODING));
        out.write(0);
        out.flush();

        while (!shutdown) {
            if (in.available() > 0) {
                byte data = (byte) in.read();
                if (data < 1) {
                    continue;
                }
                buffer.buffer(data);
            } else if (!buffer.isEmpty()) {
                PlainInput plain = new PlainInput(buffer.asString(STREAM_ENCODING));
                for (InputFilter filter : streamHandler) {
                    String output = filter.filter(plain);
                    if (output.length() > 0) {
                        out.write(output.getBytes(STREAM_ENCODING));
                        out.write(0);
                    }
                }
                if (!plain.isHandled()) {
                    Server.LOGGER.log(Level.FINE, "Invalid client data: {0}", plain.getPainInput());
                }
                out.flush();
                buffer.reset();
            } else {
                fetchAndSend(out);
            }
        }
    }

    /**
     * Fetches all new messages from the message provider and send them to the
     * client.
     *
     * @param out output stream used to write the messages
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    private void fetchAndSend(OutputStream out) throws UnsupportedEncodingException, IOException {
        if (messageProvider.any()) {
            EasyMessage[] messages;
            messages = messageProvider.fetch();
            if (messages.length > 0) {
                JsonArray json = new JsonArray();
                for (EasyMessage message : messages) {
                    json.add(message.toJsonObject());
                }
                
                byte[] payload = json.toString().getBytes(STREAM_ENCODING);
                // protocol requires ending zero byte
                byte[] data = Arrays.copyOf(payload, payload.length + 1);
                
                try {
                    out.write(data);
                    out.flush();
                } catch (SocketException ex) {
                    messageProvider.rollback();
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isShutdown() {
        return shutdown;
    }

    /**
     * {@inheritDoc}
     *
     * @throws java.io.IOException
     */
    @Override
    public void shutdown() throws IOException {
        if (!shutdown) {
            shutdown = true;
            if (socket != null) {
                socket.close();
            }
        }
    }
}
