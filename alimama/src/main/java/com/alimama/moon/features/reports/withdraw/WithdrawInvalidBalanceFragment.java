package com.alimama.moon.features.reports.withdraw;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.alimama.moon.R;
import com.alimama.union.app.views.AlertMsgDialog;
import java.text.DecimalFormat;

public class WithdrawInvalidBalanceFragment extends Fragment {
    private static final String ARGS_KEY_BALANCE = "ARGS_KEY_BALANCE";
    public static final String TAG = "InvalidBalanceFragment";
    private TextView mAdditionalInfoTextView;
    private TextView mBalanceTextView;

    public static WithdrawInvalidBalanceFragment newInstance(@Nullable Double d) {
        WithdrawInvalidBalanceFragment withdrawInvalidBalanceFragment = new WithdrawInvalidBalanceFragment();
        if (d != null) {
            Bundle bundle = new Bundle();
            bundle.putDouble(ARGS_KEY_BALANCE, d.doubleValue());
            withdrawInvalidBalanceFragment.setArguments(bundle);
        }
        return withdrawInvalidBalanceFragment;
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_withdraw_input, viewGroup, false);
        this.mBalanceTextView = (TextView) inflate.findViewById(R.id.tv_balance);
        this.mAdditionalInfoTextView = (TextView) inflate.findViewById(R.id.tv_additional_info);
        return inflate;
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        Bundle arguments = getArguments();
        double d = arguments != null ? arguments.getDouble(ARGS_KEY_BALANCE) : 0.0d;
        this.mBalanceTextView.setText(new DecimalFormat("0.00").format(d));
        if (d < 0.0d) {
            this.mAdditionalInfoTextView.setText(R.string.withdraw_debts_alert);
            this.mAdditionalInfoTextView.setVisibility(0);
            new AlertMsgDialog(getContext()).title(R.string.withdraw_no_available_balance).content((int) R.string.withdraw_debts_alert).positiveButtonText(R.string.confirm_okay).show();
            return;
        }
        this.mAdditionalInfoTextView.setVisibility(4);
    }
}
