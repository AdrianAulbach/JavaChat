package ch.bfh.easychat.server;

import ch.bfh.easychat.common.EasyMessage;
import ch.bfh.easychat.common.EasyRequestUntil;
import ch.bfh.easychat.server.core.InputFilter;
import ch.bfh.easychat.server.core.PlainInput;
import com.eclipsesource.json.JsonArray;

/**
 * Filter to handle requests for retrieving previous chat messages until a given
 * id. This feature is used by the client to restore all messages retrieved
 * during a network interruption.
 *
 * @author Samuel Egger
 */
public class RequestUntilFilter implements InputFilter {

    /**
     * Reference to the message provider.
     */
    private final MessageProvider provider;

    /**
     * Creates a new RequestUntilFilter object.
     *
     * @param provider
     */
    public RequestUntilFilter(MessageProvider provider) {
        this.provider = provider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String filter(PlainInput input) {
        EasyRequestUntil request = EasyRequestUntil.load(input.getPainInput());

        if (request != null && request.getUntilId() > 0) {
            EasyMessage[] messages = provider.queryUntilId(request.getUntilId());
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
