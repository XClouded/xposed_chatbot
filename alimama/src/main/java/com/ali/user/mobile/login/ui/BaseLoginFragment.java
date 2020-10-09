package com.ali.user.mobile.login.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.base.ui.BaseActivity;
import com.ali.user.mobile.base.ui.BaseFragment;
import com.ali.user.mobile.common.api.AliUserLogin;
import com.ali.user.mobile.coordinator.CoordinatorWrapper;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.login.model.LoginConstant;
import com.ali.user.mobile.model.RegistParam;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.HistoryAccount;
import com.ali.user.mobile.security.SecurityGuardManagerWraper;
import com.ali.user.mobile.service.NavigatorService;
import com.ali.user.mobile.service.ServiceFactory;
import com.ali.user.mobile.ui.R;
import com.ali.user.mobile.ui.WebConstant;
import com.ali.user.mobile.ui.widget.CircleImageView;
import com.ali.user.mobile.utils.ImageUtil;
import com.ali.user.mobile.utils.LoadImageTask;
import com.ali.user.mobile.utils.MD5Util;
import com.ali.user.mobile.utils.SharedPreferencesUtil;
import com.ali.user.mobile.webview.WebViewActivity;
import com.taobao.login4android.thread.LoginAsyncTask;

public class BaseLoginFragment extends BaseFragment implements View.OnClickListener {
    protected boolean isHistoryMode = false;
    protected CircleImageView mAvatarIV;
    protected ScrollView mContainerSV;
    protected UserLoginActivity mUserLoginActivity;

    /* access modifiers changed from: protected */
    public String getAccountName() {
        return "";
    }

    /* access modifiers changed from: protected */
    public int getLoginSite() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public String getPageName() {
        return ApiConstants.UTConstants.UT_PAGE_FIRST_LOGIN;
    }

    /* access modifiers changed from: protected */
    public void onKeyBoardHide() {
    }

    /* access modifiers changed from: protected */
    public void onKeyBoardShow() {
    }

    /* access modifiers changed from: protected */
    public void showBottomMenu() {
    }

    /* access modifiers changed from: protected */
    public void switchMode(boolean z, HistoryAccount historyAccount) {
    }

    public void initViews(View view) {
        this.mUserLoginActivity = (UserLoginActivity) getActivity();
        this.mAvatarIV = (CircleImageView) view.findViewById(R.id.aliuser_login_avatar);
        this.mAvatarIV.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View view) {
                BaseLoginFragment.this.onDeleteAccount();
                return false;
            }
        });
    }

    public void onResume() {
        super.onResume();
        UserTrackAdapter.updatePageName(getActivity(), getPageName());
    }

    public HistoryAccount getHistoryAccount() {
        if (this.mUserLoginActivity != null) {
            return this.mUserLoginActivity.mHistoryAccount;
        }
        return null;
    }

    public boolean isHistoryMode() {
        return this.isHistoryMode;
    }

    /* access modifiers changed from: protected */
    public void showPushLogoutAlertIfHas() {
        if (getContext() != null) {
            if (System.currentTimeMillis() - ((Long) SharedPreferencesUtil.getData(getContext().getApplicationContext(), "pushLogoutTime", 0L)).longValue() < 60000) {
                String str = (String) SharedPreferencesUtil.getData(getContext().getApplicationContext(), "pushLogoutContent", "");
                if (!TextUtils.isEmpty(str)) {
                    alert("", str, getString(R.string.aliuser_common_ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            BaseLoginFragment.this.dismissAlertDialog();
                        }
                    }, (String) null, (DialogInterface.OnClickListener) null);
                }
            }
            new CoordinatorWrapper().execute(new LoginAsyncTask<Object, Void, Void>() {
                public Void excuteTask(Object... objArr) {
                    SharedPreferencesUtil.saveData(BaseLoginFragment.this.getContext(), "pushLogoutContent", "");
                    return null;
                }
            }, new Object[0]);
        }
    }

    /* access modifiers changed from: protected */
    public void setOnClickListener(View... viewArr) {
        for (View view : viewArr) {
            if (view != null) {
                view.setClickable(true);
                view.setOnClickListener(this);
            }
        }
    }

    public void showLoading() {
        showProgress("");
    }

    public void dismissLoading() {
        if (getActivity() != null && !getActivity().isFinishing()) {
            dismissProgress();
        }
    }

    public void toast(String str, int i) {
        super.toast(str, i);
    }

    public void alert(String str, String str2, String str3, DialogInterface.OnClickListener onClickListener, String str4, DialogInterface.OnClickListener onClickListener2) {
        super.alert(str, str2, str3, onClickListener, str4, onClickListener2);
    }

    public void alertList(String[] strArr, DialogInterface.OnClickListener onClickListener, boolean z) {
        super.alertList(strArr, onClickListener, z);
    }

    public void dismissAlertDialog() {
        super.dismissAlertDialog();
    }

    public boolean isActive() {
        return isActivityAvaiable();
    }

    public BaseActivity getBaseActivity() {
        return this.mAttachedActivity;
    }

    public void onPrepareOptionsMenu(Menu menu) {
        if (!(menu.findItem(R.id.aliuser_menu_item_help) == null || menu.findItem(R.id.aliuser_menu_item_more) == null)) {
            if (this.isHistoryMode) {
                menu.findItem(R.id.aliuser_menu_item_help).setVisible(false);
                menu.findItem(R.id.aliuser_menu_item_more).setVisible(true);
            } else {
                menu.findItem(R.id.aliuser_menu_item_more).setVisible(false);
                if (AliUserLogin.mAppreanceExtentions == null || AliUserLogin.mAppreanceExtentions.needHelp()) {
                    menu.findItem(R.id.aliuser_menu_item_help).setVisible(true);
                } else {
                    menu.findItem(R.id.aliuser_menu_item_help).setVisible(false);
                }
            }
        }
        super.onPrepareOptionsMenu(menu);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menu.clear();
        menuInflater.inflate(R.menu.aliuser_login_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.aliuser_menu_item_help) {
            openHelp();
        } else if (itemId == R.id.aliuser_menu_item_more) {
            UserTrackAdapter.sendControlUT(getPageName(), "Button-More");
            showBottomMenu();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /* access modifiers changed from: protected */
    public void openHelp() {
        String str;
        if (isActivityAvaiable()) {
            UserTrackAdapter.sendControlUT(getPageName(), "Button-Help");
            if (getLoginSite() == 3) {
                str = LoginConstant.CBU_HELP_URL;
            } else {
                str = "https://ihelp.taobao.com/pocket/visitorServicePortal.htm?from=n_signin_taobao";
                String accountName = getAccountName();
                if (!TextUtils.isEmpty(accountName)) {
                    str = str + "&bizUserName=" + accountName;
                }
            }
            Intent intent = new Intent(this.mAttachedActivity, WebViewActivity.class);
            intent.putExtra(WebConstant.WEBURL, str);
            startActivity(intent);
        }
    }

    public void onClick(View view) {
        if (view.getId() == R.id.aliuser_reg_tv) {
            UserTrackAdapter.sendControlUT(getPageName(), "Button-Reg");
            RegistParam registParam = new RegistParam();
            registParam.registSite = getLoginSite();
            ((NavigatorService) ServiceFactory.getService(NavigatorService.class)).openRegisterPage(this.mAttachedActivity, registParam);
        }
    }

    /* access modifiers changed from: protected */
    public void updateAvatar(String str) {
        if (!TextUtils.isEmpty(str)) {
            Bitmap bitmapFromMemoryCache = ImageUtil.getBitmapFromMemoryCache(MD5Util.getMD5(str));
            if (bitmapFromMemoryCache != null) {
                this.mAvatarIV.setImageBitmap(bitmapFromMemoryCache);
                return;
            }
            new LoadImageTask(DataProviderFactory.getApplicationContext(), this.mAvatarIV, "HeadImages", 160).execute(new String[]{str});
            this.mAvatarIV.setImageResource(R.drawable.aliuser_placeholder);
        }
    }

    /* access modifiers changed from: protected */
    public void onDeleteAccount() {
        if (isActivityAvaiable()) {
            AnonymousClass4 r5 = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    UserTrackAdapter.sendControlUT(BaseLoginFragment.this.getPageName(), "Button-DeleteUser");
                    BaseLoginFragment.this.deleteAccount(BaseLoginFragment.this.mUserLoginActivity.mHistoryAccount);
                }
            };
            alert(getActivity().getString(R.string.aliuser_account_remove_title), getActivity().getString(R.string.aliuser_account_remove_info), getActivity().getString(R.string.aliuser_account_remove_delete), r5, getActivity().getString(R.string.aliuser_confirm_cancel), (DialogInterface.OnClickListener) null);
        }
    }

    /* access modifiers changed from: protected */
    public void deleteAccount(HistoryAccount historyAccount) {
        new CoordinatorWrapper().execute(new AsyncTask<Object, Void, Void>() {
            /* access modifiers changed from: protected */
            public Void doInBackground(Object... objArr) {
                SecurityGuardManagerWraper.removeAllHistoryAccount();
                return null;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Void voidR) {
                if (BaseLoginFragment.this.isActive()) {
                    BaseLoginFragment.this.mUserLoginActivity.mHistoryAccount = null;
                    BaseLoginFragment.this.isHistoryMode = false;
                    BaseLoginFragment.this.switchMode(BaseLoginFragment.this.isHistoryMode, (HistoryAccount) null);
                }
            }
        }, new Object[0]);
    }

    /* access modifiers changed from: private */
    public boolean isKeyboardShown(View view) {
        Rect rect = new Rect();
        this.mUserLoginActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        DisplayMetrics displayMetrics = view.getResources().getDisplayMetrics();
        return ((float) (displayMetrics.heightPixels - rect.bottom)) > displayMetrics.density * 100.0f;
    }

    /* access modifiers changed from: protected */
    public void setKeyboardStateListener(final View view) {
        if (this.mUserLoginActivity != null) {
            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            if (BaseLoginFragment.this.isKeyboardShown(view)) {
                                BaseLoginFragment.this.onKeyBoardShow();
                            } else {
                                BaseLoginFragment.this.onKeyBoardHide();
                            }
                        }
                    }, 200);
                }
            });
        }
    }
}
