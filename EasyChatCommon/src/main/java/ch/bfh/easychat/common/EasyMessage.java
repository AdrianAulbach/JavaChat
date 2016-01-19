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
    private String sender;

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
    
    /**
     * @return the sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * @param user the sender to set
     */
    public void setSender(String user) {
        this.sender = user;
    }
    
    public EasyMessage(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    // For JSON see: https://github.com/ralfstx/minimal-json
    public String toJson() {
        JsonObject object = Json.object()
                .add("message", message)
                .add("sender", sender);
        return object.toString();
    }

    public static EasyMessage load(String json) {
        try {
            JsonObject object = Json.parse(json).asObject();
            String message = object.get("message").toString();
            String sender = object.get("sender").toString();

            return new EasyMessage(message, sender);
        } catch (Exception ex) {
            return null;
        }
    }
    
    public static List<EasyMessage> loadFromArray(String json) {
    	JsonValue value = Json.parse(json);
    	List<EasyMessage> messages = new LinkedList<EasyMessage>();
    	if(value.isArray()) {
	    	for(JsonValue obj : value.asArray()) {
	    		EasyMessage msg = EasyMessage.load(obj.toString());
	    		if(msg != null) {
	    			messages.add(msg);
	    		}
	    	}
    	} else {
			messages.add(EasyMessage.load(value.toString()));
		}
    	return messages;
    }

}
