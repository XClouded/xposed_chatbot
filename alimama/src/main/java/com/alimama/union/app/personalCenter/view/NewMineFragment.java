package com.alimama.union.app.personalCenter.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.core.internal.view.SupportMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.alimama.moon.App;
import com.alimama.moon.BuildConfig;
import com.alimama.moon.R;
import com.alimama.moon.init.UpdateUtils;
import com.alimama.moon.ui.IBottomNavFragment;
import com.alimama.moon.ui.fragment.BaseFragment;
import com.alimama.moon.ui.uicomponent.SafeAlertDailogBuilder;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.moon.utils.SpmProcessor;
import com.alimama.moon.utils.ToastUtil;
import com.alimama.moon.utils.UnionLensUtil;
import com.alimama.moon.web.WebActivity;
import com.alimama.moon.web.WebPageIntentGenerator;
import com.alimama.union.app.aalogin.ILogin;
import com.alimama.union.app.aalogin.model.User;
import com.alimama.union.app.infrastructure.image.request.TaoImageLoader;
import com.alimama.union.app.infrastructure.permission.Permission;
import com.alimama.union.app.infrastructure.weex.WeexPageGenerator;
import com.alimama.union.app.pagerouter.AppPageInfo;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.alimama.union.app.personalCenter.IPersonalContract;
import com.alimama.union.app.personalCenter.model.MineItemData;
import com.alimama.union.app.personalCenter.presenter.PersonalPresenter;
import com.alimama.union.app.personalCenter.viewmodel.MineViewModel;
import com.ut.mini.UTAnalytics;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewMineFragment extends BaseFragment implements IPersonalContract.IPersonalView, AdapterView.OnItemClickListener, IBottomNavFragment {
    private static final String BUTLER_PRIVILEGE = "butler_privilege";
    private static final String MARKET_DETAILS = "market_details";
    private static final String MOON_ABOUT = "about";
    private static final String WALLET_PRIVILEGE = "wallet_privilege";
    private static final String WEEX_SHARE = "weex_share";
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) NewMineFragment.class);
    private ImageView iconImageView;
    private ListView listView;
    @Inject
    ILogin login;
    private View loginOutView;
    private View mCheckUpdate;
    private TextView mCheckUpdateTv;
    private View mEnvView;
    private TextView mMermberIdText;
    private View mScanView;
    private LinearLayout mUserAreaLogin;
    private TextView mUserNickText;
    private MineAdapter mineAdapter;
    private MineViewModel mineViewModel;
    @Inject
    @Named("CAMERA")
    Permission permission;
    /* access modifiers changed from: private */
    public IPersonalContract.IPersonalPresenter presenter;
    private ImageView userGradeBadgeView;
    private View userGradeGroupView;
    private TextView userTaskView;

    public void initDeveloper() {
    }

    public void updateNotLoginView() {
    }

    public static NewMineFragment newInstance() {
        return new NewMineFragment();
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
    }

    public View returnCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.layout_mine, viewGroup, false);
        initView(inflate);
        return inflate;
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        App.getAppComponent().inject(this);
        this.presenter = new PersonalPresenter(this, this.login);
        this.mineViewModel = (MineViewModel) ViewModelProviders.of((Fragment) this).get(MineViewModel.class);
        this.mineAdapter = new MineAdapter(this.mineViewModel.getMineItemList(), getContext());
        this.listView.setAdapter(this.mineAdapter);
        this.listView.setOnItemClickListener(this);
        updateUserState();
    }

    private void updateUserState() {
        if (this.presenter != null) {
            this.presenter.start();
        }
        User user = this.mineViewModel.getUser(this.login.getUserId());
        try {
            updateMemberGradeView(user);
            checkUserWalletPrivilege(user);
        } catch (Exception e) {
            logger.info("updateUserState: {}", (Object) e.toString());
        }
    }

    private void updateMemberGradeView(User user) {
        if (user != null) {
            String gradeString = user.getGradeString();
            char c = 65535;
            int hashCode = gradeString.hashCode();
            if (hashCode != -1078030475) {
                if (hashCode != -905957840) {
                    if (hashCode == -314765822 && gradeString.equals("primary")) {
                        c = 0;
                    }
                } else if (gradeString.equals("senior")) {
                    c = 2;
                }
            } else if (gradeString.equals("medium")) {
                c = 1;
            }
            switch (c) {
                case 0:
                    this.userGradeBadgeView.setImageResource(R.drawable.ic_primary);
                    break;
                case 1:
                    this.userGradeBadgeView.setImageResource(R.drawable.ic_medium);
                    break;
                case 2:
                    this.userGradeBadgeView.setImageResource(R.drawable.ic_senior);
                    break;
            }
            if (user.getFinishTypeDispDateStartIndex() == null || user.getFinishTypeDispDateEndIndex() == null) {
                this.userTaskView.setText(user.getFinishTypeDisp());
                return;
            }
            SpannableString spannableString = new SpannableString(user.getFinishTypeDisp());
            spannableString.setSpan(new ForegroundColorSpan(SupportMenu.CATEGORY_MASK), user.getFinishTypeDispDateStartIndex().intValue(), user.getFinishTypeDispDateEndIndex().intValue(), 33);
            this.userTaskView.setText(spannableString);
        }
    }

    private void checkUserWalletPrivilege(User user) {
        Integer walletPrivilege;
        if (user != null && (walletPrivilege = user.getWalletPrivilege()) != null) {
            Logger logger2 = logger;
            logger2.info("walletPrivilege = " + walletPrivilege);
            List<MineItemData> dataList = this.mineAdapter.getDataList();
            if (dataList != null) {
                for (MineItemData next : dataList) {
                    if (WALLET_PRIVILEGE.equals(next.getItemType())) {
                        if (walletPrivilege.intValue() == 1) {
                            next.setHidden(false);
                        } else {
                            next.setHidden(true);
                        }
                        this.mineAdapter.notifyDataSetChanged();
                        return;
                    }
                }
            }
        }
    }

    private void checkUserButlerPrivilege(User user) {
        Integer butlerPrivilege;
        if (user != null && (butlerPrivilege = user.getButlerPrivilege()) != null) {
            Logger logger2 = logger;
            logger2.info("ButlerPrivilege = " + butlerPrivilege);
            List<MineItemData> dataList = this.mineAdapter.getDataList();
            if (dataList != null) {
                for (MineItemData next : dataList) {
                    if (BUTLER_PRIVILEGE.equals(next.getItemType())) {
                        if (butlerPrivilege.intValue() == 1) {
                            next.setHidden(false);
                        } else {
                            next.setHidden(true);
                        }
                        this.mineAdapter.notifyDataSetChanged();
                        return;
                    }
                }
            }
        }
    }

    private void initView(View view) {
        this.mUserNickText = (TextView) view.findViewById(R.id.account_nick);
        this.mMermberIdText = (TextView) view.findViewById(R.id.account_id);
        this.iconImageView = (ImageView) view.findViewById(R.id.icon_iv);
        this.mUserAreaLogin = (LinearLayout) view.findViewById(R.id.mine_user_area_login);
        this.userGradeGroupView = view.findViewById(R.id.account_grade_group);
        this.userGradeGroupView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                UTHelper.MinePage.toUserGrades();
                NewMineFragment.this.showThresholdPage();
            }
        });
        this.userGradeBadgeView = (ImageView) view.findViewById(R.id.account_badge);
        this.userTaskView = (TextView) view.findViewById(R.id.account_task_progress);
        this.loginOutView = view.findViewById(R.id.login_out_layout);
        this.loginOutView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SafeAlertDailogBuilder safeAlertDailogBuilder = new SafeAlertDailogBuilder(NewMineFragment.this.getActivity());
                safeAlertDailogBuilder.setMessage(NewMineFragment.this.getString(R.string.login_out_hint));
                safeAlertDailogBuilder.setPositiveButton(NewMineFragment.this.getString(R.string.sure_str), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NewMineFragment.this.presenter.clickLogout();
                        UTHelper.MinePage.clickSignOut();
                    }
                });
                safeAlertDailogBuilder.setNegativeButton(NewMineFragment.this.getString(R.string.cancel_str), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                safeAlertDailogBuilder.show();
            }
        });
        this.mCheckUpdate = view.findViewById(R.id.check_update_layout);
        this.mCheckUpdateTv = (TextView) view.findViewById(R.id.check_update_tv);
        this.mCheckUpdateTv.setText(getString(R.string.check_update, BuildConfig.VERSION_NAME));
        this.mCheckUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                UpdateUtils.checkUpdate(NewMineFragment.this.getActivity(), true);
            }
        });
        this.mScanView = view.findViewById(R.id.menu_scan_layout);
        this.mEnvView = view.findViewById(R.id.menu_env_layout);
        View findViewById = view.findViewById(R.id.tv_developer);
        this.mScanView.setVisibility(8);
        this.mEnvView.setVisibility(8);
        findViewById.setVisibility(8);
        this.listView = (ListView) view.findViewById(R.id.content_listview);
    }

    /* access modifiers changed from: private */
    public void showThresholdPage() {
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.setData(WebPageIntentGenerator.getThresholdUri().buildUpon().appendQueryParameter("title", "规则与权限").appendQueryParameter("spm", "a21wq.9114130.5593238.9160").build());
        getActivity().startActivity(intent);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        MineItemData mineItemData = this.mineViewModel.getMineItemList().get(i);
        if (mineItemData != null && !mineItemData.isEmpty() && !TextUtils.isEmpty(mineItemData.getItemType())) {
            UTHelper.MinePage.toItem(mineItemData.getItemType());
            String itemType = mineItemData.getItemType();
            char c = 65535;
            int hashCode = itemType.hashCode();
            if (hashCode != -1389678273) {
                if (hashCode != 92611469) {
                    if (hashCode == 2106035745 && itemType.equals("weex_share")) {
                        c = 2;
                    }
                } else if (itemType.equals(MOON_ABOUT)) {
                    c = 1;
                }
            } else if (itemType.equals(MARKET_DETAILS)) {
                c = 0;
            }
            switch (c) {
                case 0:
                    try {
                        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getActivity().getPackageName()));
                        List<ResolveInfo> queryIntentActivities = getActivity().getPackageManager().queryIntentActivities(intent, 65536);
                        if (queryIntentActivities == null || queryIntentActivities.size() <= 0) {
                            ToastUtil.toast((Context) getActivity(), (int) R.string.error_go_market);
                            return;
                        }
                        intent.addFlags(268435456);
                        startActivity(intent);
                        return;
                    } catch (Exception e) {
                        ToastUtil.toast((Context) getActivity(), (int) R.string.error_go_market);
                        e.printStackTrace();
                        return;
                    }
                case 1:
                    Uri.Builder buildUpon = Uri.parse(mineItemData.getJumpUrl()).buildUpon();
                    buildUpon.appendQueryParameter("title", mineItemData.getItemName()).appendQueryParameter("version", BuildConfig.VERSION_NAME).build();
                    MoonComponentManager.getInstance().getPageRouter().gotoPage(buildUpon.toString());
                    return;
                case 2:
                    Uri.Builder buildUpon2 = WeexPageGenerator.getShareCreatorUri().buildUpon();
                    buildUpon2.appendQueryParameter("url", "https://pub.alimama.com/app.htm");
                    MoonComponentManager.getInstance().getPageRouter().gotoPage(AppPageInfo.PAGE_WEEX, buildUpon2.build());
                    return;
                default:
                    Uri.Builder buildUpon3 = Uri.parse(mineItemData.getJumpUrl()).buildUpon();
                    buildUpon3.appendQueryParameter("title", mineItemData.getItemName());
                    MoonComponentManager.getInstance().getPageRouter().gotoPage(buildUpon3.toString());
                    return;
            }
        }
    }

    public void updateMemberInfoView(Long l) {
        TextView textView = this.mMermberIdText;
        textView.setText(getString(R.string.account_id) + String.valueOf(l));
    }

    public void updateTaobaoAccountInfoView(User user) {
        this.mUserNickText.setText(user.getUserNick());
        TaoImageLoader.load(user.getAvatarLink()).placeholder(R.drawable.img_loading_bg).error(R.drawable.img_loading_bg).into(this.iconImageView);
    }

    public void setPresenter(IPersonalContract.IPersonalPresenter iPersonalPresenter) {
        this.presenter = iPersonalPresenter;
    }

    public void willBeDisplayed() {
        UTHelper.sendControlHit(UTHelper.MinePage.PAGE_NAME, "showMine");
        UnionLensUtil.generatePrepvid();
        UTAnalytics.getInstance().getDefaultTracker().pageAppearDonotSkip(getActivity(), UTHelper.MinePage.PAGE_NAME);
        this.presenter.getTaobaoAccountInfo();
    }

    public void willBeHidden() {
        UTHelper.sendControlHit(UTHelper.MinePage.PAGE_NAME, "hiddenMine");
        SpmProcessor.pageDisappear(getActivity(), UTHelper.MinePage.SPM_CNT);
    }

    public void refresh() {
        this.presenter.getTaobaoAccountInfo();
    }

    public String currFragmentTitle() {
        return getResources().getString(R.string.tab_mine_title);
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }
}
