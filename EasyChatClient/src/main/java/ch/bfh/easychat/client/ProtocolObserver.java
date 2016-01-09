package ch.bfh.easychat.client;

import ch.bfh.easychat.common.EasyMessage;

public interface ProtocolObserver {
	public void onMessageRecieved(EasyMessage msg);
}
