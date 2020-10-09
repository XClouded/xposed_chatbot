package com.taobao.accs;

import androidx.annotation.Keep;
import java.io.Serializable;
import java.util.Map;

@Keep
public interface IAppReceiver extends Serializable {
    Map<String, String> getAllServices();

    String getService(String str);

    void onBindApp(int i);

    void onBindUser(String str, int i);

    void onData(String str, String str2, byte[] bArr);

    void onSendData(String str, int i);

    void onUnbindApp(int i);

    void onUnbindUser(int i);
}
