package com.ali.user.mobile.base.helper;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.exception.RpcException;
import com.ali.user.mobile.security.biz.R;

public class SDKExceptionHelper {
    private static SDKExceptionHelper instance = new SDKExceptionHelper();
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private SDKExceptionHelper() {
    }

    public static SDKExceptionHelper getInstance() {
        return instance;
    }

    private void toast(final String str) {
        this.mHandler.post(new Runnable() {
            public void run() {
                if (!TextUtils.isEmpty(str)) {
                    try {
                        Toast.makeText(DataProviderFactory.getApplicationContext(), str, 0).show();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            }
        });
    }

    public void rpcExceptionHandler(Throwable th) {
        th.printStackTrace();
        if (th instanceof RpcException) {
            RpcException rpcException = (RpcException) th;
            int code = rpcException.getCode();
            switch (code) {
                case 2:
                case 6:
                case 9:
                    toast(DataProviderFactory.getApplicationContext().getString(R.string.aliuser_network_error));
                    return;
                case 3:
                    toast(DataProviderFactory.getApplicationContext().getString(R.string.aliuser_network_error));
                    return;
                case 4:
                case 5:
                case 7:
                    toast(DataProviderFactory.getApplicationContext().getString(R.string.aliuser_network_error));
                    return;
                case 8:
                case 11:
                case 12:
                    return;
                case 10:
                    toast(DataProviderFactory.getApplicationContext().getString(R.string.aliuser_network_error));
                    return;
                case 13:
                    toast(DataProviderFactory.getApplicationContext().getString(R.string.aliuser_network_error));
                    return;
                default:
                    if (code >= 400 && code < 500) {
                        toast(DataProviderFactory.getApplicationContext().getString(R.string.aliuser_network_error));
                        return;
                    } else if (code < 100 || code >= 600) {
                        toast(rpcException.getMsg());
                        return;
                    } else {
                        toast(DataProviderFactory.getApplicationContext().getString(R.string.aliuser_network_error));
                        return;
                    }
            }
        } else {
            toast(DataProviderFactory.getApplicationContext().getString(R.string.aliuser_network_error));
        }
    }
}
