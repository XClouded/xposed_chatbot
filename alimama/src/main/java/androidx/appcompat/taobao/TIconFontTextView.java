package androidx.appcompat.taobao;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import com.taobao.uikit.actionbar.R;

@Deprecated
public class TIconFontTextView extends TextView {
    private static Typeface sIconfont;
    private static int sReference;

    public TIconFontTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public TIconFontTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TIconFontTextView(Context context) {
        this(context, (AttributeSet) null, 0);
    }

    private void init() {
        setTextSize(1, 24.0f);
        setTextColor(getResources().getColor(R.color.appcompat_default_icon));
        setIncludeFontPadding(false);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (sIconfont == null) {
            try {
                sIconfont = Typeface.createFromAsset(getContext().getAssets(), "uik_iconfont.ttf");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setTypeface(sIconfont);
        sReference++;
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        setTypeface((Typeface) null);
        sReference--;
        if (sReference == 0) {
            sIconfont = null;
        }
        super.onDetachedFromWindow();
    }
}
