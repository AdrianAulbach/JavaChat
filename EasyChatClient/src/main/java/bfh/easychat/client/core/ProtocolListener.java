package bfh.easychat.client.core;

import ch.bfh.easychat.common.EasyMessage;

public interface ProtocolListener {

    void messageRecieved(EasyMessage msg);
    
    void connectionLost();
}
