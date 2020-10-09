package alimama.com.unwbaseimpl;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IABTest;
import alimama.com.unwbase.interfaces.IAppEnvironment;
import android.text.TextUtils;
import com.alibaba.ut.abtest.UTABEnvironment;
import com.alibaba.ut.abtest.UTABMethod;
import com.alibaba.ut.abtest.UTABTest;
import com.alibaba.ut.abtest.Variation;
import com.alibaba.ut.abtest.VariationSet;
import com.alibaba.ut.abtest.internal.ABConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class EtaoABTest implements IABTest {
    private static final String EVENT_NAME = "event";
    private static final String REGEX = "\\|\\|";
    private static HashSet<String> eventArray = new HashSet<>();
    private static HashSet<String> experiments = new HashSet<>();
    private static HashMap<String, String> spmDict = new HashMap<>();

    public void activate(String str, String str2) {
        UTABTest.activate(str, str2);
    }

    public HashMap<String, String> parseClient(String str, String str2) {
        HashMap<String, String> hashMap = new HashMap<>();
        VariationSet activate = UTABTest.activate(str, str2);
        Iterator<Variation> it = activate.iterator();
        HashSet<String> hashSet = experiments;
        hashSet.add(activate.getExperimentBucketId() + "");
        if (it != null) {
            while (it.hasNext()) {
                Variation next = it.next();
                String name = next.getName();
                String valueAsString = next.getValueAsString(name);
                hashMap.put(name, valueAsString);
                if (name.startsWith("1002.")) {
                    spmDict.put(name, valueAsString);
                } else if ("event".equals(name) && !TextUtils.isEmpty(valueAsString)) {
                    String[] split = valueAsString.split(REGEX);
                    if (split.length > 0) {
                        Collections.addAll(eventArray, split);
                    }
                }
            }
        }
        return hashMap;
    }

    public void parseServer(JSONArray jSONArray) {
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONObject optJSONObject = jSONArray.optJSONObject(i);
            if (optJSONObject != null) {
                UTABTest.activateServer(optJSONObject.optString("dataTrack"));
                String optString = optJSONObject.optString("bucketId");
                if (optString != null) {
                    experiments.add(optString);
                }
                JSONObject optJSONObject2 = optJSONObject.optJSONObject("variations");
                if (optJSONObject2 != null) {
                    Iterator<String> keys = optJSONObject2.keys();
                    while (keys.hasNext()) {
                        String obj = keys.next().toString();
                        String optString2 = optJSONObject2.optString(obj);
                        if (optString2 != null && !TextUtils.isEmpty(optString2)) {
                            if (obj.startsWith("1002.")) {
                                spmDict.put(obj, optString2);
                            } else if ("event".equals(obj) && !TextUtils.isEmpty(optString2)) {
                                String[] split = optString2.split(REGEX);
                                if (split.length > 0) {
                                    Collections.addAll(eventArray, split);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public String changeSpm(String str) {
        if (!TextUtils.isEmpty(str)) {
            for (String next : spmDict.keySet()) {
                if (str.startsWith(next) && !str.startsWith(spmDict.get(next))) {
                    return str.replace(next, spmDict.get(next));
                }
            }
        }
        return str;
    }

    public String changeArgs(String str, String str2) {
        Iterator<String> it = eventArray.iterator();
        while (it.hasNext()) {
            String next = it.next();
            if (!TextUtils.isEmpty(next) && next.contains(str)) {
                if (!TextUtils.isEmpty(str2)) {
                    return String.format(str2 + ",etaoAB=%s", new Object[]{experiments.toString()});
                }
                return String.format(str2 + "etaoAB=%s", new Object[]{experiments.toString()});
            }
        }
        return str2;
    }

    public Map<String, String> changeArgs(String str, Map<String, String> map) {
        HashMap hashMap = new HashMap();
        if (map != null) {
            hashMap.putAll(map);
        }
        hashMap.put("etaoAB", experiments.toString());
        return hashMap;
    }

    public String getUrl(String str) {
        Variation variation = UTABTest.activate(UTABTest.COMPONENT_NAV, str).getVariation(ABConstants.BasicConstants.DEFAULT_VARIATION_NAME);
        if (variation != null) {
            String valueAsString = variation.getValueAsString((String) null);
            if (!TextUtils.isEmpty(valueAsString)) {
                return valueAsString;
            }
        }
        return str;
    }

    public void activateServerAbTest(String str) {
        UTABTest.activateServer(str);
    }

    public void init() {
        UTABTest.initialize(UNWManager.getInstance().application, UTABTest.newConfigurationBuilder().setDebugEnable(UNWManager.getInstance().getDebuggable()).setEnvironment(getAbTestEnv()).setMethod(UTABMethod.Pull).create());
    }

    private UTABEnvironment getAbTestEnv() {
        IAppEnvironment iAppEnvironment = (IAppEnvironment) UNWManager.getInstance().getService(IAppEnvironment.class);
        if (iAppEnvironment == null) {
            return UTABEnvironment.Product;
        }
        if (iAppEnvironment.isProd()) {
            return UTABEnvironment.Product;
        }
        if (iAppEnvironment.isPre()) {
            return UTABEnvironment.Prepare;
        }
        if (iAppEnvironment.isDaily()) {
            return UTABEnvironment.Daily;
        }
        return UTABEnvironment.Product;
    }
}
