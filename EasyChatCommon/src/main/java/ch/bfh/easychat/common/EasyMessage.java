package ch.bfh.easychat.common;

import java.util.LinkedList;
import java.util.List;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.ParseException;

/**
 *
 * @author Samuel Egger
 */
public class EasyMessage {

    private String message;
    private String sender;
    private long id;

    /**
     * @return the id
     */
    public long getId(){
        return id;
    }
    
    /**
     * @param id the id to set
     */
    public void setId(long id){
        this.id = id;
    }
    
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
    
    public EasyMessage(String message, String sender, long id) {
        this.message = message;
        this.sender = sender;
        this.id = id;
    }

    public boolean hasId(){
        return id > 0;
    }
    
    public String toJson() {
        return toJsonObject().toString();
    }

    public JsonObject toJsonObject() {
        return Json.object()
                .add("message", message)
                .add("sender", sender);
    }

    public static EasyMessage load(String json) {
        try {
            JsonObject object = Json.parse(json).asObject();
            String message = object.get("message").asString();
            String sender = object.get("sender").asString();

            long id = 0;
            if(object.get("id") != null){
                id = object.get("id").asLong();
            }
            return new EasyMessage(message, sender, id);
        } catch (Exception ex) {
            return null;
        }
    }

    public static List<EasyMessage> loadFromArray(String json) {
        List<EasyMessage> messages = new LinkedList<EasyMessage>();
        try {
        JsonValue value = Json.parse(json);
        if (value.isArray()) {
            for (JsonValue obj : value.asArray()) {
                EasyMessage msg = EasyMessage.load(obj.toString());
                if (msg != null) {
                    messages.add(msg);
                }
            }
        } else {
            messages.add(EasyMessage.load(value.toString()));
        }
        } catch(ParseException e) {
        	//ToDo
        }
        return messages;
    }

}
