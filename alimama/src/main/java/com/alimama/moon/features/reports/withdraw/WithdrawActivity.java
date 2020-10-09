package com.alimama.moon.features.reports.withdraw;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import com.alimama.moon.R;
import com.alimama.moon.features.reports.ErrorInfoFragment;
import com.alimama.moon.features.reports.withdraw.contracts.IWithdrawContract;
import com.alimama.moon.features.reports.withdraw.network.GetBalanceRequest;
import com.alimama.moon.features.reports.withdraw.network.GetBalanceResponse;
import com.alimama.moon.ui.BaseActivity;
import com.alimama.moon.utils.ActivityUtil;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimama.union.app.rxnetwork.RxMtopResponse;
import com.alimama.union.app.views.ISViewContainer;

public class WithdrawActivity extends BaseActivity implements RxMtopRequest.RxMtopResult<GetBalanceResponse>, IWithdrawContract.IView, ISViewContainer.IViewContainerRefreshListener {
    private static final String TAG = "WithdrawActivity";
    private GetBalanceRequest mGetBalanceRequest;
    private IWithdrawContract.IPresenter mPresenter;
    private ISViewContainer mStatusViewContainer;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_withdraw);
        this.mStatusViewContainer = (ISViewContainer) findViewById(R.id.status_view_container);
        this.mStatusViewContainer.setContainerRefreshListener(this);
        this.mPresenter = new WithdrawPresenter(this);
        ActivityUtil.setupToolbar(this, (Toolbar) findViewById(R.id.toolbar), true);
        this.mGetBalanceRequest = new GetBalanceRequest();
        this.mGetBalanceRequest.setRxMtopResult(this);
        this.mGetBalanceRequest.sendRequest();
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        finish();
        return true;
    }

    public void refreshPage() {
        if (this.mGetBalanceRequest == null) {
            this.mGetBalanceRequest = new GetBalanceRequest();
            this.mGetBalanceRequest.setRxMtopResult(this);
        }
        this.mGetBalanceRequest.sendRequest();
    }

    public void displayRetryError() {
        this.mStatusViewContainer.onDataLoadError();
    }

    public void displayWithdrawError(int i, int i2) {
        displayWithdrawError((CharSequence) getString(i), (CharSequence) getString(i2));
    }

    public void displayWithdrawError(@NonNull CharSequence charSequence, @NonNull CharSequence charSequence2) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        if (!supportFragmentManager.isStateSaved()) {
            this.mStatusViewContainer.onDataLoaded();
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ErrorInfoFragment.newInstance(charSequence, charSequence2)).commit();
        }
    }

    public void goToWithdrawAccountPage(@NonNull GetBalanceResponse getBalanceResponse) {
        this.mStatusViewContainer.onDataLoaded();
        Double balance = getBalanceResponse.getBalance();
        if (balance == null || balance.doubleValue() <= 0.0d) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, WithdrawInvalidBalanceFragment.newInstance(balance), WithdrawInvalidBalanceFragment.TAG).commitAllowingStateLoss();
        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, WithdrawAccountFragment.newInstance(getBalanceResponse), WithdrawAccountFragment.TAG).commitAllowingStateLoss();
        }
    }

    public void goToWithdrawResultPage(double d, double d2, String str) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, WithdrawResultFragment.newInstance(d, d2, str), WithdrawResultFragment.class.getName()).commitAllowingStateLoss();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i != 4) {
            return super.onKeyDown(i, keyEvent);
        }
        finish();
        return true;
    }

    public void goToLoginActivity() {
        super.login();
    }

    public void result(RxMtopResponse<GetBalanceResponse> rxMtopResponse) {
        if (!isFinishing()) {
            this.mPresenter.onGetBalanceResponse(rxMtopResponse, this);
        }
    }
}
