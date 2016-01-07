package ch.bfh.easychat.server;

import ch.bfh.easychat.server.core.ConnectionHandler;
import ch.bfh.easychat.server.core.ConnectionHandlerFactory;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 *
 * @author Samuel Egger
 */
public class Server implements Runnable {

    /**
     * Shutdown connection handlers will be removed from the connectionHandlers
     * list after the threshold has been exceeded.
     */
    private final int COMPLETED_HANDLER_THRESHOLD = 10;

    /**
     * Used to keep track of the created connection handlers.
     */
    private final List<ConnectionHandler> connectionHandlers = new ArrayList<>();

    /**
     * Runs the server loop to listen for incoming connections.
     */
    private Thread serverLoop;

    /**
     * Thread pool for connection handler.
     */
    private ExecutorService clientConnectionsPool;

    /**
     * ServerSocket to accept incoming connections.
     */
    private ServerSocket serverSocket = null;
    private boolean shutdown = false;

    /**
     * Abstract factory to create ConnectionHandler objects.
     */
    private final ConnectionHandlerFactory connectionHandlerFactory;
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    /**
     * The default constructor.
     *
     * @param connectionHandlerFactory an implementation of the
     * ConnectionHandlerFactory interface.
     */
    public Server(ConnectionHandlerFactory connectionHandlerFactory) {
        this.connectionHandlerFactory = connectionHandlerFactory;
    }

    /**
     * @return true if the server is listening for new connections. Otherwise
     * false.
     */
    public boolean isRunning() {
        if (serverLoop != null) {
            return serverLoop.isAlive();
        }

        return false;
    }

    /**
     * @return the number of running client connections
     */
    public int numberOfRunningHandler() {
        return ((ThreadPoolExecutor) clientConnectionsPool).getActiveCount();
    }

    /**
     * @return the number of closed client connections
     */
    public long numberOfCompletedHandler() {
        return ((ThreadPoolExecutor) clientConnectionsPool).getCompletedTaskCount();
    }

    /**
     *
     * @param port the port number, or 0 to use a port number that is
     * automatically allocated.
     *
     * @throws IOException
     */
    public void start(int port) throws IOException {
        if (!isRunning()) {
            initialize(port);
            serverLoop.start();
        }
    }

    /**
     * Initializes the server prior to starting the server loop.
     *
     * @param port the port number, or 0 to use a port number that is
     * automatically allocated.
     *
     * @throws IOException
     */
    private void initialize(int port) throws IOException {
        shutdown = false;
        serverSocket = new ServerSocket(port);
        serverSocket.setReuseAddress(true);
        clientConnectionsPool = Executors.newCachedThreadPool();
        serverLoop = new Thread(this);
    }

    /**
     * Stops the server. This method waits for all connections to close. The
     * server can be restarted after shutdown.
     *
     * @param timeout the maximum time in milliseconds to wait for all
     * connections close.
     *
     * @throws java.lang.InterruptedException if the timeout was exceeded
     */
    public void stop(int timeout) throws InterruptedException {
        shutdown = true;

        LOGGER.fine("Shutdown connections...");
        connectionHandlers.stream().forEach((connection) -> {
            try {
                connection.shutdown();
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });

        connectionHandlers.clear();

        clientConnectionsPool.shutdown();
        clientConnectionsPool.awaitTermination(timeout, TimeUnit.MICROSECONDS);

        try {
            serverSocket.close();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        LOGGER.fine("Shutdown complete");
    }

    /**
     * The server loop listens for incoming connections and delegates them to a
     * ConnectionHandler.
     */
    @Override
    public void run() {
        while (!shutdown) {
            final Socket socket;
            try {
                LOGGER.log(Level.FINE, "Listening on port {0}...", 
                        String.valueOf(serverSocket.getLocalPort()));
                socket = accept();
                if (socket == null) {
                    continue;
                }
                LOGGER.log(Level.FINE, "Connection accepted: {0}", socket.getInetAddress());
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
                continue;
            }

            clientConnectionsPool.execute(() -> {
                try {
                    createHandler().handle(socket);
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    /**
     * Creates a new ConnectionHandler object using the
     * ConnectionHandlerFactory. The method also checks the number of handler
     * that are shutdown against the COMPLETED_HANDLER_THRESHOLD and removes all
     * shutdown handler if the threshold has been exceeded.
     *
     * @return a new ConnectionHandler object
     */
    private ConnectionHandler createHandler() {
        ConnectionHandler handler = connectionHandlerFactory.create(this);

        long completedHandler = numberOfCompletedHandler();
        if (COMPLETED_HANDLER_THRESHOLD > completedHandler) {
            connectionHandlers.removeIf(h -> h.isShutdown());
        }

        connectionHandlers.add(handler);
        return handler;
    }

    /**
     * Wraps the serverSocket.accept() method to allow for proper shutdown by
     * ignoring any IOException after the stop method has been invoked.
     *
     * @return a client socket
     * @throws IOException As long as the server has not been shutdown, any
     * IOException caused by the ServerSocket is thrown.
     */
    private Socket accept() throws IOException {
        Socket socket = null;
        try {
            socket = serverSocket.accept();
        } catch (IOException ex) {
            if (!shutdown) {
                throw ex;
            } else {
                LOGGER.log(Level.FINE, "Stop listening.", ex.getMessage());
            }
        }
        return socket;
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 5200;
        if (args.length == 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException();
            }
        }

        try {
            InputStream configStream = new FileInputStream("logging.properties");
            LogManager.getLogManager().readConfiguration(configStream);
        } catch (IOException | SecurityException ex) {
        }

        Server server = new Server(new ConnectionHandlerFactoryImpl());
        server.start(port);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in));) {
            String line;
            while ((line = in.readLine()) != null) {
                if ("stop".equals(line.toLowerCase())) {
                    server.stop(3600);
                    break;
                } else if ("status".equals(line.toLowerCase())) {
                    System.out.println("SERVER STATUS:");
                    System.out.println("Number of running handler: " + server.numberOfRunningHandler());
                    System.out.println("Number of completed handler: " + server.numberOfCompletedHandler());
                }
            }
        }
    }
}
