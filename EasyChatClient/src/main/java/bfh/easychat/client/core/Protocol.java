package bfh.easychat.client.core;

import ch.bfh.easychat.common.EasyMessage;

public interface Protocol {

    public boolean sendMessage(String message);

    public boolean connect(String host, int port, String user);

    public void disconnect();

    public void setProtocolListener(ProtocolListener mainApp);
}
