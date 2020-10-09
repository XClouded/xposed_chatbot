package com.ali.user.mobile.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.register.ProtocolModel;
import com.ali.user.mobile.register.old.TaoUrlSpan;
import com.ali.user.mobile.ui.R;

public class ProtocolHelper {
    public static String getProtocol() {
        Context applicationContext = DataProviderFactory.getApplicationContext();
        if (18 == DataProviderFactory.getDataProvider().getSite()) {
            return applicationContext.getString(R.string.aliuser_damai_protocol_url);
        }
        return applicationContext.getString(R.string.aliuser_tb_protocal_url);
    }

    public static String getPolicyProtocol() {
        Context applicationContext = DataProviderFactory.getApplicationContext();
        if (18 == DataProviderFactory.getDataProvider().getSite()) {
            return applicationContext.getString(R.string.aliuser_damai_policy_protocol_url);
        }
        return applicationContext.getString(R.string.aliuser_policy_protocal_url);
    }

    public static String getLawProtocol() {
        return DataProviderFactory.getApplicationContext().getString(R.string.aliuser_law_protocal_url);
    }

    public static void generateProtocol(final ProtocolModel protocolModel, final Context context, TextView textView, final String str, final boolean z) {
        if (protocolModel != null && !TextUtils.isEmpty(protocolModel.protocolTitle)) {
            SpannableString spannableString = new SpannableString(protocolModel.protocolTitle);
            if (protocolModel.protocolItems != null) {
                for (String next : protocolModel.protocolItems.keySet()) {
                    try {
                        int indexOf = protocolModel.protocolTitle.indexOf(next);
                        int length = next.length() + indexOf;
                        spannableString.setSpan(new TaoUrlSpan(protocolModel.protocolItems.get(next)), indexOf, length, 33);
                        spannableString.setSpan(new ClickableSpan() {
                            public void updateDrawState(TextPaint textPaint) {
                                super.updateDrawState(textPaint);
                                if (protocolModel.protocolItemColor > 0) {
                                    textPaint.setColor(ContextCompat.getColor(context, protocolModel.protocolItemColor));
                                }
                                textPaint.setUnderlineText(z);
                            }

                            public void onClick(View view) {
                                UserTrackAdapter.sendControlUT(str, "Button-TaobaoProtocol");
                            }
                        }, indexOf, length, 33);
                        if (protocolModel.protocolTitle.length() != length) {
                            spannableString.setSpan(new ClickableSpan() {
                                public void onClick(View view) {
                                }

                                public void updateDrawState(TextPaint textPaint) {
                                    super.updateDrawState(textPaint);
                                    if (protocolModel.protocolItemColor > 0) {
                                        textPaint.setColor(ContextCompat.getColor(context, protocolModel.protocolItemColor));
                                    }
                                    textPaint.setUnderlineText(false);
                                }
                            }, length, length + 1, 33);
                        }
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            }
            textView.setText(spannableString);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}
