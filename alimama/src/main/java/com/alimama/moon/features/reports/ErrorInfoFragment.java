package com.alimama.moon.features.reports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.alimama.moon.R;

public class ErrorInfoFragment extends Fragment {
    private static final String KEY_CONTENT = "content";
    private static final String KEY_TITLE = "title";
    public static final String TAG = "ErrorInfoFragment";
    private TextView mContentView;
    private TextView mTitleView;

    public static ErrorInfoFragment newInstance(@NonNull CharSequence charSequence, @NonNull CharSequence charSequence2) {
        ErrorInfoFragment errorInfoFragment = new ErrorInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putCharSequence("title", charSequence);
        bundle.putCharSequence("content", charSequence2);
        errorInfoFragment.setArguments(bundle);
        return errorInfoFragment;
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_error_info, viewGroup, false);
        this.mTitleView = (TextView) inflate.findViewById(R.id.tv_title);
        this.mContentView = (TextView) inflate.findViewById(R.id.tv_content);
        return inflate;
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mTitleView.setText(arguments.getCharSequence("title"));
            this.mContentView.setText(arguments.getCharSequence("content"));
        }
    }
}
