package alimama.com.unweventparse;

import alimama.com.unweventparse.model.BaseResourceData;
import alimama.com.unweventparse.model.LottieImageData;
import alimama.com.unweventparse.model.SplashResourceData;
import alimama.com.unweventparse.popup.DialogData;
import android.net.Uri;
import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;

public class ResourceFactory {
    public static final String TYPE_LOTTIEIMAGE = "lottieimage";
    public static final String TYPE_POPUP = "NOAH_POPUP";
    public static final String TYPE_SPLASH = "NOAH_SCREEN_IMG";
    private static ResourceFactory sInstance = new ResourceFactory();
    private HashMap<String, Class<?>> map = new HashMap<>();

    public static ResourceFactory getInstance() {
        return sInstance;
    }

    private ResourceFactory() {
        this.map.put(TYPE_LOTTIEIMAGE, LottieImageData.class);
        this.map.put(TYPE_POPUP, DialogData.class);
        this.map.put(TYPE_SPLASH, SplashResourceData.class);
    }

    public void register(String str, Class cls) {
        this.map.put(str, cls);
    }

    public BaseResourceData create(String str, JSONObject jSONObject) {
        Class cls = this.map.get(str);
        if (cls == null) {
            return null;
        }
        try {
            return ((BaseResourceData) cls.newInstance()).make(jSONObject);
        } catch (Exception unused) {
            return null;
        }
    }

    public BaseResourceData create(String str, Uri uri) {
        Class cls = this.map.get(str);
        if (cls == null) {
            return null;
        }
        try {
            return ((BaseResourceData) cls.newInstance()).make(uri);
        } catch (Exception unused) {
            return null;
        }
    }
}
