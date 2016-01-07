package ch.bfh.easychat.common;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

/**
 *
 * @author Samuel Egger
 */
public class EasyUser {

    private String user;

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }
    
    public EasyUser(String user) {
        this.user = user;
    }
    
        // For JSON see: https://github.com/ralfstx/minimal-json
    
    public String toJson(){
        JsonObject object = Json.object().add("user", user);
        return object.asString();
    }
    
    public static EasyUser load(String json){
        JsonObject object = Json.parse(json).asObject();
        String user = object.get("user").asString();
        
        return new EasyUser(user);
    }
}
