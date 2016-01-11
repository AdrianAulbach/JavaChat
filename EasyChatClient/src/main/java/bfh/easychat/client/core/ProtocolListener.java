package bfh.easychat.client.core;

import ch.bfh.easychat.common.EasyMessage;

public interface ProtocolListener {

    public void messageRecieved(EasyMessage msg);
}
