package ch.bfh.easychat.common;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

/**
 *
 * @author Samuel Egger
 */
public class EasyRequest {

    private int top;

    /**
     * @return the top
     */
    public int getTop() {
        return top;
    }

    /**
     * @param top the top to set
     */
    public void setTop(int top) {
        this.top = top;
    }

    public EasyRequest(int top) {
        this.top = top;
    }

    // For JSON see: https://github.com/ralfstx/minimal-json
    public String toJson() {
        JsonObject object = Json.object().add("top", top);
        return object.toString();
    }

    public static EasyRequest load(String json) {
        try {
            JsonObject object = Json.parse(json).asObject();
            int top = object.get("top").asInt();

            return new EasyRequest(top);
        } catch (Exception ex) {
            return null;
        }
    }
}
