package ch.bfh.easychat.common;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

/**
 *
 * @author Samuel Egger
 */
public class EasyMessage {

    private String message;

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public EasyMessage(String message) {
        this.message = message;
    }

    // For JSON see: https://github.com/ralfstx/minimal-json
    public String toJson() {
        JsonObject object = Json.object().add("message", message);
        return object.toString();
    }

    public static EasyMessage load(String json) {
        try {
            JsonObject object = Json.parse(json).asObject();
            String message = object.get("message").toString();

            return new EasyMessage(message);
        } catch (Exception ex) {
            return null;
        }
    }
}
