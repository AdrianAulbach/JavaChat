package ch.bfh.easychat.common;

import java.util.LinkedList;
import java.util.List;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

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
    
    public static List<EasyMessage> loadFromArray(String json) {
    	JsonArray array = Json.parse(json).asArray();
    	List<EasyMessage> messages = new LinkedList<EasyMessage>();
    	for(JsonValue obj : array) {
    		EasyMessage msg = EasyMessage.load(obj.asString());
    		if(msg != null) {
    			messages.add(msg);
    		}
    	}
    	return messages;
    }
}
