package alimama.com.unwrouter;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IABTest;
import alimama.com.unwbase.interfaces.IEtaoLogger;
import alimama.com.unwbase.interfaces.IEvent;
import alimama.com.unwbase.interfaces.IRouter;
import alimama.com.unwbase.interfaces.ITriver;
import alimama.com.unwbase.interfaces.IUTAction;
import alimama.com.unwbase.tools.UNWLog;
import alimama.com.unwrouter.constants.RouterConstant;
import alimama.com.unwrouter.interfaces.JumpInterceptor;
import alimama.com.unwrouter.interfaces.PageNeedLoginAction;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import anet.channel.util.AppLifecycle;
import anet.channel.util.HttpConstant;
import com.taobao.vessel.utils.Utils;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import mtopsdk.common.util.SymbolExpUtil;

public class UNWRouter implements IRouter<PageInfo> {
    public static final String PAGE_NAME = "pageName";
    public static final String TAG = "UNWRouter";
    public static boolean isAfterStartup = false;
    private boolean isBackground;
    private boolean isFirstEnterHome;
    private ArrayList<WeakReference<Activity>> mActivityList = new ArrayList<>();
    private Activity mCurrentTop;
    private Class<?> mHomeClass;
    private JumpInterceptor mInterceptor;
    private PageNeedLoginAction mLoginAction;
    private ArrayList<String> mOnResumeActiList;
    private String sAppHeader;
    private String sAppSchema;

    public void init() {
    }

    public UNWRouter(PageNeedLoginAction pageNeedLoginAction, String str) {
        this.mLoginAction = pageNeedLoginAction;
        this.mActivityList = new ArrayList<>();
        this.mOnResumeActiList = new ArrayList<>();
        this.sAppSchema = str;
        this.sAppHeader = this.sAppSchema + HttpConstant.SCHEME_SPLIT;
    }

    public void setInterceptor(JumpInterceptor jumpInterceptor) {
        this.mInterceptor = jumpInterceptor;
    }

    public void setHomeActivity(Class<?> cls) {
        this.mHomeClass = cls;
    }

    public Class<?> getHomeClass() {
        return this.mHomeClass;
    }

    public Activity getCurrentActivity() {
        return this.mCurrentTop;
    }

    public boolean isBackground() {
        return this.isBackground;
    }

    public boolean isFirstEnterHome() {
        return this.isFirstEnterHome;
    }

    public ArrayList<WeakReference<Activity>> getBackStack() {
        return this.mActivityList;
    }

    public boolean isLastPage() {
        return this.mActivityList.size() == 1;
    }

    public PageInfo getCurrentPageInfo() {
        return PageInfo.find(this.mCurrentTop);
    }

    public void onCreate(Activity activity) {
        this.mCurrentTop = activity;
        this.mActivityList.add(new WeakReference(activity));
        if (activity.getClass().equals(this.mHomeClass)) {
            this.isFirstEnterHome = true;
        }
        if (this.mInterceptor != null) {
            this.mInterceptor.onCreateIntercept(this.mActivityList, activity);
        }
    }

    public void onResume(Activity activity) {
        this.isBackground = false;
        this.mCurrentTop = activity;
        if (activity.getClass().equals(this.mHomeClass)) {
            this.isFirstEnterHome = false;
        }
        if (this.mInterceptor != null) {
            this.mInterceptor.onResumeIntercept(activity);
        }
        this.mOnResumeActiList.add(activity.getClass().getSimpleName());
    }

    public void onPause(Activity activity) {
        if (this.mInterceptor != null) {
            this.mInterceptor.onPauseIntercept(activity);
        }
    }

    public void onStop(Activity activity) {
        if (this.mCurrentTop != null && this.mCurrentTop == activity) {
            this.isBackground = true;
            AppLifecycle.onBackground();
        }
    }

    public void onDestroy(Activity activity) {
        popUpLastActivity(activity);
    }

    public void popUpLastActivity(Activity activity) {
        if (this.mActivityList.size() != 0) {
            for (int size = this.mActivityList.size() - 1; size >= 0; size--) {
                Activity activity2 = (Activity) this.mActivityList.get(size).get();
                if (activity2 == null) {
                    this.mActivityList.remove(size);
                } else if (activity2.equals(activity)) {
                    this.mActivityList.remove(size);
                }
            }
        }
    }

    public ArrayList<String> getOnResumeActiList() {
        return this.mOnResumeActiList;
    }

    public void finishAll() {
        if (this.mActivityList.size() != 0) {
            for (int size = this.mActivityList.size() - 1; size >= 0; size--) {
                Activity activity = (Activity) this.mActivityList.get(size).get();
                if (activity != null) {
                    try {
                        activity.finish();
                    } catch (Exception unused) {
                    }
                }
            }
            this.mActivityList.clear();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                public void run() {
                    Process.killProcess(Process.myPid());
                }
            }, 200);
        }
    }

    public void gotoPage(PageInfo pageInfo) {
        gotoPage(pageInfo, -1);
    }

    public void gotoPage(PageInfo pageInfo, int i) {
        gotoPage(pageInfo, (Bundle) null, i);
    }

    public void gotoPage(PageInfo pageInfo, Uri uri) {
        gotoPageWithUrl(pageInfo, uri, new Bundle(), -1);
    }

    public void gotoPage(PageInfo pageInfo, Bundle bundle) {
        gotoPage(pageInfo, bundle, -1);
    }

    public void gotoPage(PageInfo pageInfo, Bundle bundle, int i) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        if (pageInfo != null) {
            gotoPageWithUrl(pageInfo, (Uri) null, bundle, i);
        }
    }

    public void gotoPage(String str, boolean z) {
        if (!z) {
            gotoPage(str);
        } else if (this.mLoginAction != null) {
            this.mLoginAction.loginAction(str);
        }
    }

    public boolean gotoPage(@Nullable String str) {
        return gotoPage(str, (Bundle) null);
    }

    public boolean gotoPage(String str, Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        return gotoPage(str, bundle, 0);
    }

    private boolean noHomeStackHandle(String str) {
        return gotoPage(this.sAppHeader + PageInfo.PAGE_HOME + "?url=" + safeEncode(str, ""));
    }

    private String safeEncode(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return str2;
        }
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException unused) {
            return str2;
        }
    }

    private boolean isHasHomeActivityInstack() {
        if (this.mActivityList != null) {
            int size = this.mActivityList.size();
            for (int i = 0; i < size; i++) {
                if (this.mHomeClass != null && this.mHomeClass.equals(((Activity) this.mActivityList.get(i).get()).getClass())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void gotoPageWithUrl(PageInfo pageInfo, Uri uri, Bundle bundle, int i) {
        gotoPageWithUrl(pageInfo, uri, bundle, i, false);
    }

    public boolean gotoPage(@Nullable String str, Bundle bundle, int i) {
        PageInfo pageInfo;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        IEvent iEvent = (IEvent) UNWManager.getInstance().getService(IEvent.class);
        if (iEvent != null && iEvent.parseUrl(Uri.parse(str))) {
            return true;
        }
        if (((IUTAction) UNWManager.getInstance().getService(IUTAction.class)).isInit()) {
            ((IEtaoLogger) UNWManager.getInstance().getService(IEtaoLogger.class)).info(TAG, "gotoPage", str);
        }
        ITriver iTriver = (ITriver) UNWManager.getInstance().getService(ITriver.class);
        if (iTriver == null) {
            UNWLog.error("跳转中心未注入小程序组件，暂不支持小程序检查");
        } else if (iTriver.isTriver(str)) {
            iTriver.open(this.mCurrentTop, str, bundle);
            return true;
        }
        UNWLog.error("gotoPage:" + str);
        if (str.startsWith("//")) {
            str = Utils.HTTPS_SCHEMA + str;
        }
        IABTest iABTest = (IABTest) UNWManager.getInstance().getService(IABTest.class);
        Uri parse = Uri.parse(str);
        if (iABTest != null) {
            parse = Uri.parse(iABTest.getUrl(str));
        }
        if (parse == null) {
            return false;
        }
        String scheme = parse.getScheme();
        if (TextUtils.isEmpty(scheme) || scheme.contains("&") || scheme.contains(SymbolExpUtil.SYMBOL_EQUAL)) {
            parse = Uri.parse(this.sAppHeader + str);
            scheme = parse.getScheme();
        }
        Uri uri = parse;
        if (TextUtils.isEmpty(scheme)) {
            return false;
        }
        if (bundle == null) {
            bundle = new Bundle();
        }
        Bundle bundle2 = bundle;
        if (scheme.startsWith(this.sAppSchema)) {
            String host = uri.getHost();
            if (!TextUtils.isEmpty(host) && host.contains("&")) {
                host = host.substring(0, host.indexOf("&"));
            }
            pageInfo = PageInfo.find(host);
            if (pageInfo == null) {
                pageInfo = PageInfo.find(PageInfo.PAGE_HOME);
            }
        } else {
            pageInfo = PageInfo.find(PageInfo.PAGE_WEBVIEW);
            bundle2.putString("url", str);
        }
        PageInfo pageInfo2 = pageInfo;
        if (pageInfo2 == null) {
            return false;
        }
        boolean z = bundle2.getBoolean("needhome", true);
        if (pageInfo2 != PageInfo.find(PageInfo.PAGE_HOME) && !isHasHomeActivityInstack() && z) {
            return noHomeStackHandle(str);
        }
        gotoPageWithUrl(pageInfo2, uri, bundle2, i, uri.isHierarchical() && "1".equals(uri.getQueryParameter(RouterConstant.NEEDLOGIN)));
        return true;
    }

    public void gotoPageWithUrl(PageInfo pageInfo, Uri uri, Bundle bundle, int i, boolean z) {
        if (!pageInfo.isNeedLogin() && !z) {
            turnToPage(pageInfo, uri, bundle, i);
        } else if (this.mLoginAction != null) {
            this.mLoginAction.loginAction(pageInfo, uri, bundle, i);
        }
    }

    public void turnToPage(PageInfo pageInfo, Uri uri, Bundle bundle, int i) {
        String str = TAG;
        UNWLog.error(str, "turnToPage uri=" + uri);
        if (pageInfo != null) {
            boolean z = false;
            if (this.mInterceptor != null) {
                z = this.mInterceptor.onIntercept(this.mActivityList, pageInfo, uri, bundle, i);
            }
            if (!z) {
                realRouter(pageInfo, uri, bundle, i);
                return;
            }
            return;
        }
        throw new RuntimeException("PageInfo is null");
    }

    public void setCurrent(Activity activity) {
        this.mCurrentTop = activity;
    }

    private void realRouter(PageInfo pageInfo, Uri uri, Bundle bundle, int i) {
        if (this.mCurrentTop != null && pageInfo != null && bundle != null) {
            Class<?> pageClass = pageInfo.getPageClass();
            String str = TAG;
            UNWLog.error(str, "realRouter pageInfo=" + pageInfo);
            Intent intent = new Intent(this.mCurrentTop, pageClass);
            String str2 = TAG;
            UNWLog.error(str2, " cls=" + pageClass);
            if (uri != null && uri.isHierarchical()) {
                for (String next : uri.getQueryParameterNames()) {
                    bundle.putString(next, uri.getQueryParameter(next));
                }
            }
            intent.putExtras(bundle);
            if (uri != null) {
                intent.setData(uri);
            }
            if (i != -1) {
                this.mCurrentTop.startActivityForResult(intent, i);
            } else {
                this.mCurrentTop.startActivity(intent);
            }
        }
    }
}
