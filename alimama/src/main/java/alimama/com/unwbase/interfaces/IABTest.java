package alimama.com.unwbase.interfaces;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;

public interface IABTest extends IInitAction {
    void activate(String str, String str2);

    void activateServerAbTest(String str);

    String changeArgs(String str, String str2);

    Map<String, String> changeArgs(String str, Map<String, String> map);

    String changeSpm(String str);

    String getUrl(String str);

    HashMap<String, String> parseClient(String str, String str2);

    void parseServer(JSONArray jSONArray);
}
