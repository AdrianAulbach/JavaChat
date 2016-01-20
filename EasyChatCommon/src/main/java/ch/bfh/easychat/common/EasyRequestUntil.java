package ch.bfh.easychat.common;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

/**
 *
 * @author Samuel Egger
 */
public class EasyRequestUntil {

    private long untilId;

    /**
     * @return the untilId
     */
    public long getUntilId() {
        return untilId;
    }

    public EasyRequestUntil(long untilId) {
        this.untilId = untilId;
    }

    /**
     * @param untilId the untilId to set
     */
    public void setUntilId(long untilId) {
        this.untilId = untilId;
    }

    public String toJson() {
        JsonObject object = Json.object().add("until", untilId);
        return object.toString();
    }

    public static EasyRequestUntil load(String json) {
        try {
            JsonObject object = Json.parse(json).asObject();
            long untilId = object.get("until").asLong();

            return new EasyRequestUntil(untilId);
        } catch (Exception ex) {
            return null;
        }
    }
}
