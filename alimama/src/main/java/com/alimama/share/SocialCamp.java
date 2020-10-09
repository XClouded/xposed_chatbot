package com.alimama.share;

import android.content.Context;
import com.alimama.share.beans.Platform;
import com.alimama.share.interfaces.SocialFunction;
import com.alimama.share.qq.QQ;
import com.alimama.share.weixin.WeiXin;
import java.util.HashMap;

public class SocialCamp {
    private HashMap<Platform, SocialFunction> map;

    private SocialCamp() {
        this.map = new HashMap<>();
    }

    private static class SingletonInstance {
        /* access modifiers changed from: private */
        public static final SocialCamp INSTANCE = new SocialCamp();

        private SingletonInstance() {
        }
    }

    public static SocialCamp getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public void init(Context context) {
        WeiXin weiXin = new WeiXin(context);
        QQ qq = new QQ(context);
        this.map.put(Platform.WEIXIN, weiXin);
        this.map.put(Platform.QQ, qq);
    }

    public SocialFunction dispatchFunction(Platform platform) {
        if (this.map != null) {
            return this.map.get(platform);
        }
        return null;
    }
}
