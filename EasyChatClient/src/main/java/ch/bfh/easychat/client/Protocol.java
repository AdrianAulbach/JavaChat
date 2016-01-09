package ch.bfh.easychat.client;

import java.util.AbstractList;
import ch.bfh.easychat.common.EasyMessage;

public interface Protocol {
	public boolean sendMessage(EasyMessage msg);
	public void addObserver(ProtocolObserver mainApp);
	public boolean connect(String host, int port, String user);
	public void disconnect();
}
