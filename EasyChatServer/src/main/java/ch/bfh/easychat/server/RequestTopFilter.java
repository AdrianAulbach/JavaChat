package ch.bfh.easychat.server;

import ch.bfh.easychat.server.core.InputFilter;
import ch.bfh.easychat.common.EasyMessage;
import ch.bfh.easychat.common.EasyRequestTop;
import ch.bfh.easychat.server.core.PlainInput;
import com.eclipsesource.json.JsonArray;

/**
 * Filter to handle requests for retrieving the last N messages. This might be
 * useful if for example a user connects to the server and retrieves the last 10
 * messages to see whats going on.
 * 
 * @author Samuel
 */
public class RequestTopFilter implements InputFilter {

    /**
     * Reference to the message provider.
     */
    private final MessageProvider provider;

    public RequestTopFilter(MessageProvider provider) {
        this.provider = provider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String filter(PlainInput input) {
        EasyRequestTop request = EasyRequestTop.load(input.getPainInput());
        
        if (request != null && request.getTop() > 0) {
            EasyMessage[] messages = provider.queryTop(request.getTop());
            JsonArray json = new JsonArray();
            
            for (EasyMessage message : messages) {
                json.add(message.toJson());
            }
            
            input.onHandled();
            return json.toString();
        }
        return "";
    }
}
