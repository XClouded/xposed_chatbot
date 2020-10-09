package alimama.com.unwbase.interfaces;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import java.util.HashMap;

public interface IIM<T> extends IInitAction {
    void addDetailObject(String str, T t);

    Fragment getChattingFragment();

    T getDetailObject(String str);

    String getShopNick();

    void goToChat(String str, String str2, String str3, String str4, String str5, String str6);

    void gotoIm(@NonNull HashMap<String, String> hashMap);

    void login(Runnable runnable);

    void logout();

    void setChatActivity(Activity activity);

    void setShopNick(String str);
}
