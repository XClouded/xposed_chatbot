package com.alibaba.aliweex.hc.cache;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.internal.view.SupportMenu;
import androidx.fragment.app.Fragment;
import com.alibaba.android.umbrella.link.export.UMLLCons;
import com.taobao.weex.WXEnvironment;

public class WeexCacheMsgPanel extends Fragment {
    public static final String TAG = "WeexCacheMsgPanel";
    private LogView mLogView;
    /* access modifiers changed from: private */
    public ScrollView mScrollView;

    private interface LogNode {
        LogNode getNext();

        void println(int i, String str, String str2, Throwable th);

        void setNext(LogNode logNode);
    }

    public View inflateViews() {
        this.mScrollView = new ScrollView(getActivity());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, -1);
        this.mScrollView.setLayoutParams(layoutParams);
        this.mLogView = new LogView(getActivity());
        ViewGroup.LayoutParams layoutParams2 = new ViewGroup.LayoutParams(layoutParams);
        layoutParams2.height = -2;
        this.mLogView.setLayoutParams(layoutParams2);
        this.mLogView.setClickable(true);
        this.mLogView.setFocusable(true);
        this.mLogView.setTypeface(Typeface.MONOSPACE);
        double d = (double) getResources().getDisplayMetrics().density;
        double d2 = (double) 16;
        Double.isNaN(d2);
        Double.isNaN(d);
        int i = (int) ((d2 * d) + 0.5d);
        this.mLogView.setPadding(i, i, i, i);
        this.mLogView.setCompoundDrawablePadding(i);
        this.mLogView.setGravity(80);
        this.mLogView.setTextAppearance(getActivity(), 16974081);
        this.mLogView.setTextColor(-1);
        this.mScrollView.addView(this.mLogView);
        return this.mScrollView;
    }

    public void onStart() {
        super.onStart();
        LogWrapper logWrapper = new LogWrapper();
        Log.setLogNode(logWrapper);
        MessageOnlyLogFilter messageOnlyLogFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(messageOnlyLogFilter);
        messageOnlyLogFilter.setNext(getLogView());
    }

    public void onDestroy() {
        super.onDestroy();
        LogNode logNode = Log.getLogNode();
        if (logNode != null) {
            LogNode next = logNode.getNext();
            if (next != null) {
                LogNode next2 = next.getNext();
                if (next2 != null) {
                    next2.setNext((LogNode) null);
                }
                next.setNext((LogNode) null);
            }
            logNode.setNext((LogNode) null);
        }
        Log.setLogNode((LogNode) null);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        final View inflateViews = inflateViews();
        inflateViews.setBackgroundColor(-1442840576);
        this.mLogView.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                WeexCacheMsgPanel.this.mScrollView.fullScroll(130);
            }
        });
        TextView textView = new TextView(getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams((int) TypedValue.applyDimension(1, 80.0f, getResources().getDisplayMetrics()), (int) TypedValue.applyDimension(1, 35.0f, getResources().getDisplayMetrics()));
        layoutParams.topMargin = getResources().getDisplayMetrics().heightPixels / 3;
        textView.setText("msgPanel");
        int applyDimension = (int) TypedValue.applyDimension(1, 5.0f, getResources().getDisplayMetrics());
        textView.setPadding(applyDimension, applyDimension, applyDimension, applyDimension);
        textView.setBackgroundColor(SupportMenu.CATEGORY_MASK);
        textView.setTextColor(-1);
        textView.setTag(1);
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int intValue = ((Integer) view.getTag()).intValue();
                int i = 0;
                inflateViews.setVisibility(intValue == 1 ? 8 : 0);
                if (intValue != 1) {
                    i = 1;
                }
                view.setTag(Integer.valueOf(i));
            }
        });
        frameLayout.addView(inflateViews);
        frameLayout.addView(textView, layoutParams);
        return frameLayout;
    }

    public LogView getLogView() {
        return this.mLogView;
    }

    public static void d(String str) {
        Log.d(str);
    }

    public static class Log {
        public static final int ASSERT = 7;
        public static final int DEBUG = 3;
        public static final int ERROR = 6;
        public static final int INFO = 4;
        public static final int NONE = -1;
        public static final int VERBOSE = 2;
        public static final int WARN = 5;
        private static LogNode mLogNode;

        public static LogNode getLogNode() {
            return mLogNode;
        }

        public static void setLogNode(LogNode logNode) {
            mLogNode = logNode;
        }

        public static void println(int i, String str, String str2, Throwable th) {
            if (mLogNode != null) {
                mLogNode.println(i, str, str2, th);
            }
        }

        public static void println(int i, String str, String str2) {
            println(i, str, str2, (Throwable) null);
        }

        public static void d(String str, String str2, Throwable th) {
            println(4, str, str2, th);
        }

        public static void d(String str, String str2) {
            if (WXEnvironment.isApkDebugable()) {
                d(str, str2, (Throwable) null);
            }
        }

        public static void d(String str) {
            d(WeexCacheMsgPanel.TAG, str, (Throwable) null);
        }
    }

    private static class LogView extends AppCompatTextView implements LogNode {
        LogNode mNext;

        public LogView(Context context) {
            super(context);
        }

        public LogView(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LogView(Context context, AttributeSet attributeSet, int i) {
            super(context, attributeSet, i);
        }

        public void println(int i, String str, String str2, Throwable th) {
            String str3;
            String str4 = null;
            switch (i) {
                case 2:
                    str3 = "VERBOSE";
                    break;
                case 3:
                    str3 = UMLLCons.FEATURE_TYPE_DEBUG;
                    break;
                case 4:
                    str3 = "INFO";
                    break;
                case 5:
                    str3 = "WARN";
                    break;
                case 6:
                    str3 = "ERROR";
                    break;
                case 7:
                    str3 = "ASSERT";
                    break;
                default:
                    str3 = null;
                    break;
            }
            if (th != null) {
                str4 = android.util.Log.getStackTraceString(th);
            }
            final StringBuilder sb = new StringBuilder();
            appendIfNotNull(sb, str3, "\t");
            appendIfNotNull(sb, str, "\t");
            appendIfNotNull(sb, str2, "\t");
            appendIfNotNull(sb, str4, "\t");
            ((Activity) getContext()).runOnUiThread(new Thread(new Runnable() {
                public void run() {
                    LogView.this.appendToLog(sb.toString());
                }
            }));
            if (this.mNext != null) {
                this.mNext.println(i, str, str2, th);
            }
        }

        public LogNode getNext() {
            return this.mNext;
        }

        public void setNext(LogNode logNode) {
            this.mNext = logNode;
        }

        private StringBuilder appendIfNotNull(StringBuilder sb, String str, String str2) {
            if (str == null) {
                return sb;
            }
            if (str.length() == 0) {
                str2 = "";
            }
            sb.append(str);
            sb.append(str2);
            return sb;
        }

        public void appendToLog(String str) {
            append("\n" + str);
        }
    }

    private static class MessageOnlyLogFilter implements LogNode {
        LogNode mNext;

        public MessageOnlyLogFilter(LogNode logNode) {
            this.mNext = logNode;
        }

        public MessageOnlyLogFilter() {
        }

        public void println(int i, String str, String str2, Throwable th) {
            if (this.mNext != null) {
                getNext().println(-1, (String) null, str2, (Throwable) null);
            }
        }

        public LogNode getNext() {
            return this.mNext;
        }

        public void setNext(LogNode logNode) {
            this.mNext = logNode;
        }
    }

    private static class LogWrapper implements LogNode {
        private LogNode mNext;

        private LogWrapper() {
        }

        public LogNode getNext() {
            return this.mNext;
        }

        public void setNext(LogNode logNode) {
            this.mNext = logNode;
        }

        public void println(int i, String str, String str2, Throwable th) {
            String str3 = str2 == null ? "" : str2;
            if (th != null) {
                str2 = str2 + "\n" + android.util.Log.getStackTraceString(th);
            }
            android.util.Log.println(i, str, str3);
            if (this.mNext != null) {
                this.mNext.println(i, str, str2, th);
            }
        }
    }
}
