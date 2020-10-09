package alimama.com.unweventparse.interfaces;

import android.net.Uri;
import com.alibaba.fastjson.JSONObject;

public abstract class BaseCommand implements ICommand {
    public ICommand nextFailCommand;
    public ICommand nextSuccessCommand;
    public JSONObject object;
    public Uri uri;

    public BaseCommand(JSONObject jSONObject) {
        this.object = jSONObject;
    }

    public BaseCommand(Uri uri2) {
        this.uri = uri2;
    }
}
