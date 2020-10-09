package com.taobao.weex.appfram.storage;

import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.appfram.storage.IWXStorageAdapter;
import com.taobao.weex.bridge.JSCallback;
import java.util.Map;

public class WXStorageModule extends WXSDKEngine.DestroyableModule implements IWXStorage {
    IWXStorageAdapter mStorageAdapter;

    private IWXStorageAdapter ability() {
        if (this.mStorageAdapter != null) {
            return this.mStorageAdapter;
        }
        this.mStorageAdapter = WXSDKEngine.getIWXStorageAdapter();
        return this.mStorageAdapter;
    }

    @JSMethod(uiThread = false)
    public void setItem(String str, String str2, @Nullable final JSCallback jSCallback) {
        if (TextUtils.isEmpty(str) || str2 == null) {
            StorageResultHandler.handleInvalidParam(jSCallback);
            return;
        }
        IWXStorageAdapter ability = ability();
        if (ability == null) {
            StorageResultHandler.handleNoHandlerError(jSCallback);
        } else {
            ability.setItem(str, str2, new IWXStorageAdapter.OnResultReceivedListener() {
                public void onReceived(Map<String, Object> map) {
                    if (jSCallback != null) {
                        jSCallback.invoke(map);
                    }
                }
            });
        }
    }

    @JSMethod(uiThread = false)
    public void getItem(String str, @Nullable final JSCallback jSCallback) {
        if (TextUtils.isEmpty(str)) {
            StorageResultHandler.handleInvalidParam(jSCallback);
            return;
        }
        IWXStorageAdapter ability = ability();
        if (ability == null) {
            StorageResultHandler.handleNoHandlerError(jSCallback);
        } else {
            ability.getItem(str, new IWXStorageAdapter.OnResultReceivedListener() {
                public void onReceived(Map<String, Object> map) {
                    if (jSCallback != null) {
                        jSCallback.invoke(map);
                    }
                }
            });
        }
    }

    @JSMethod(uiThread = false)
    public void removeItem(String str, @Nullable final JSCallback jSCallback) {
        if (TextUtils.isEmpty(str)) {
            StorageResultHandler.handleInvalidParam(jSCallback);
            return;
        }
        IWXStorageAdapter ability = ability();
        if (ability == null) {
            StorageResultHandler.handleNoHandlerError(jSCallback);
        } else {
            ability.removeItem(str, new IWXStorageAdapter.OnResultReceivedListener() {
                public void onReceived(Map<String, Object> map) {
                    if (jSCallback != null) {
                        jSCallback.invoke(map);
                    }
                }
            });
        }
    }

    @JSMethod(uiThread = false)
    public void length(@Nullable final JSCallback jSCallback) {
        IWXStorageAdapter ability = ability();
        if (ability == null) {
            StorageResultHandler.handleNoHandlerError(jSCallback);
        } else {
            ability.length(new IWXStorageAdapter.OnResultReceivedListener() {
                public void onReceived(Map<String, Object> map) {
                    if (jSCallback != null) {
                        jSCallback.invoke(map);
                    }
                }
            });
        }
    }

    @JSMethod(uiThread = false)
    public void getAllKeys(@Nullable final JSCallback jSCallback) {
        IWXStorageAdapter ability = ability();
        if (ability == null) {
            StorageResultHandler.handleNoHandlerError(jSCallback);
        } else {
            ability.getAllKeys(new IWXStorageAdapter.OnResultReceivedListener() {
                public void onReceived(Map<String, Object> map) {
                    if (jSCallback != null) {
                        jSCallback.invoke(map);
                    }
                }
            });
        }
    }

    @JSMethod(uiThread = false)
    public void setItemPersistent(String str, String str2, @Nullable final JSCallback jSCallback) {
        if (TextUtils.isEmpty(str) || str2 == null) {
            StorageResultHandler.handleInvalidParam(jSCallback);
            return;
        }
        IWXStorageAdapter ability = ability();
        if (ability == null) {
            StorageResultHandler.handleNoHandlerError(jSCallback);
        } else {
            ability.setItemPersistent(str, str2, new IWXStorageAdapter.OnResultReceivedListener() {
                public void onReceived(Map<String, Object> map) {
                    if (jSCallback != null) {
                        jSCallback.invoke(map);
                    }
                }
            });
        }
    }

    public void destroy() {
        IWXStorageAdapter ability = ability();
        if (ability != null) {
            ability.close();
        }
    }
}
