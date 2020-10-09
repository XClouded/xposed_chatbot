package in.srain.cube.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alimama.unionwl.uiframe.R;

public class TitleHeaderBar extends RelativeLayout {
    protected TextView mCenterTitleTextView;
    private RelativeLayout mCenterViewContainer;
    private ImageView mLeftReturnImageView;
    private RelativeLayout mLeftViewContainer;
    private RelativeLayout mRightViewContainer;
    private String mTitle;
    public View mTopView;

    public TitleHeaderBar(Context context) {
        this(context, (AttributeSet) null);
    }

    public TitleHeaderBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TitleHeaderBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mTopView = LayoutInflater.from(context).inflate(getHeaderViewLayoutId(), this);
        this.mLeftViewContainer = (RelativeLayout) findViewById(R.id.ly_title_bar_left);
        this.mCenterViewContainer = (RelativeLayout) findViewById(R.id.ly_title_bar_center);
        this.mRightViewContainer = (RelativeLayout) findViewById(R.id.ly_title_bar_right);
        this.mLeftReturnImageView = (ImageView) findViewById(R.id.iv_title_bar_left);
        this.mCenterTitleTextView = (TextView) findViewById(R.id.tv_title_bar_title);
    }

    /* access modifiers changed from: protected */
    public int getHeaderViewLayoutId() {
        return R.layout.cube_mints_base_header_bar_title;
    }

    public ImageView getLeftImageView() {
        return this.mLeftReturnImageView;
    }

    public TextView getTitleTextView() {
        return this.mCenterTitleTextView;
    }

    public void setTitle(String str) {
        this.mTitle = str;
        this.mCenterTitleTextView.setText(str);
    }

    public String getTitle() {
        return this.mTitle;
    }

    private RelativeLayout.LayoutParams makeLayoutParams(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            return new RelativeLayout.LayoutParams(-2, -2);
        }
        return new RelativeLayout.LayoutParams(layoutParams.width, layoutParams.height);
    }

    public void setCustomizedLeftView(View view) {
        this.mLeftReturnImageView.setVisibility(8);
        RelativeLayout.LayoutParams makeLayoutParams = makeLayoutParams(view);
        makeLayoutParams.addRule(15);
        makeLayoutParams.addRule(9);
        getLeftViewContainer().addView(view, makeLayoutParams);
    }

    public void setCustomizedLeftView(int i) {
        setCustomizedLeftView(inflate(getContext(), i, (ViewGroup) null));
    }

    public void setCustomizedCenterView(View view) {
        this.mCenterViewContainer.removeAllViews();
        RelativeLayout.LayoutParams makeLayoutParams = makeLayoutParams(view);
        makeLayoutParams.addRule(13);
        getCenterViewContainer().addView(view, makeLayoutParams);
    }

    public void setCustomizedCenterView(int i) {
        setCustomizedCenterView(inflate(getContext(), i, (ViewGroup) null));
    }

    public void setCustomizedRightView(View view) {
        RelativeLayout.LayoutParams makeLayoutParams = makeLayoutParams(view);
        makeLayoutParams.addRule(15);
        makeLayoutParams.addRule(11);
        getRightViewContainer().addView(view, makeLayoutParams);
    }

    public RelativeLayout getLeftViewContainer() {
        return this.mLeftViewContainer;
    }

    public RelativeLayout getCenterViewContainer() {
        return this.mCenterViewContainer;
    }

    public RelativeLayout getRightViewContainer() {
        return this.mRightViewContainer;
    }

    public void setLeftOnClickListener(View.OnClickListener onClickListener) {
        this.mLeftViewContainer.setOnClickListener(onClickListener);
    }

    public void setCenterOnClickListener(View.OnClickListener onClickListener) {
        this.mCenterViewContainer.setOnClickListener(onClickListener);
    }

    public void setRightOnClickListener(View.OnClickListener onClickListener) {
        this.mRightViewContainer.setOnClickListener(onClickListener);
    }
}
