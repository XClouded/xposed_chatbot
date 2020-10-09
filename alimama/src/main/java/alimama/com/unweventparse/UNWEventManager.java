package alimama.com.unweventparse;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class UNWEventManager {
    public static Map<String, JSONObject> eventsMap = new HashMap();

    public static void putEvent(String str, JSONObject jSONObject) {
        eventsMap.put(str, jSONObject);
    }

    public static JSONObject getEventById(String str) {
        if (eventsMap.isEmpty() || !eventsMap.containsKey(str)) {
            return null;
        }
        return eventsMap.get(str);
    }
}
