package alimama.com.unwbase.interfaces;

import com.taobao.orange.OCandidate;
import com.taobao.orange.OConfigListener;
import java.util.Map;

public interface IOrange extends IInitAction {
    void addCandidate(OCandidate oCandidate);

    String getConfig(String str, String str2, String str3);

    Map<String, String> getConfigs(String str);

    String getCustomConfig(String str, String str2);

    void registerListener(String[] strArr, OConfigListener oConfigListener, boolean z);

    void unregisterListener(String[] strArr);

    void unregisterListener(String[] strArr, OConfigListener oConfigListener);
}
