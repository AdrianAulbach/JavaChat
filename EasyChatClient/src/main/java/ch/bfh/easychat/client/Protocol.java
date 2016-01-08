package ch.bfh.easychat.client;

import java.util.AbstractList;
import ch.bfh.easychat.common.EasyMessage;

public interface Protocol {
	public boolean sendMessage(EasyMessage msg);
public void setMessagePool(AbstractList messages);
	public boolean connect(String host, int port, String user);
	public void disconnect();
}
