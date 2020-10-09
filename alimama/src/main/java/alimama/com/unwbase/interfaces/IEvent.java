package alimama.com.unwbase.interfaces;

import android.net.Uri;
import androidx.annotation.NonNull;
import com.alibaba.fastjson.JSONObject;

public interface IEvent<T> extends IInitAction {
    void execute(String str, JSONObject jSONObject, T t);

    int isLegalEvent(@NonNull JSONObject jSONObject);

    void parseJson(@NonNull JSONObject jSONObject);

    void parseJson(@NonNull JSONObject jSONObject, T t);

    boolean parseUrl(@NonNull Uri uri);

    boolean parseUrl(@NonNull Uri uri, T t);
}
