package alimama.com.unwbase.interfaces;

import android.content.Context;
import android.os.Bundle;

public interface ITriver extends IInitAction {
    boolean isTriver(String str);

    boolean open(Context context, String str, Bundle bundle);
}
