package ch.bfh.easychat.server;

import ch.bfh.easychat.common.EasyMessage;
import ch.bfh.easychat.common.EasyRequest;
import com.eclipsesource.json.JsonArray;

/**
 *
 * @author Samuel
 */
public class RequestFilter implements InputFilter {

    private final MessageProvider provider;

    public RequestFilter(MessageProvider provider) {
        this.provider = provider;
    }

    @Override
    public String filter(String input) {
        EasyRequest request = EasyRequest.load(input);
        
        if (request != null && request.getTop() > 0) {
            EasyMessage[] messages = provider.queryTop(request.getTop());
            JsonArray json = new JsonArray();
            
            for (EasyMessage message : messages) {
                json.add(message.toJson());
            }
            return json.toString();
        }
        return "";
    }
}