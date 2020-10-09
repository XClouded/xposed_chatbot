package in.srain.cube.ptr;

import android.content.Context;
import android.util.AttributeSet;

public class PtrClassicFrameLayout extends PtrFrameLayout {
    private PtrClassicDefaultHeader mPtrClassicHeader;

    public PtrClassicFrameLayout(Context context) {
        super(context);
        initViews();
    }

    public PtrClassicFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initViews();
    }

    public PtrClassicFrameLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews();
    }

    private void initViews() {
        this.mPtrClassicHeader = new PtrClassicDefaultHeader(getContext());
        setHeaderView(this.mPtrClassicHeader);
        addPtrUIHandler(this.mPtrClassicHeader);
    }

    public PtrClassicDefaultHeader getHeader() {
        return this.mPtrClassicHeader;
    }

    public void setLastUpdateTimeKey(String str) {
        if (this.mPtrClassicHeader != null) {
            this.mPtrClassicHeader.setLastUpdateTimeKey(str);
        }
    }

    public void setLastUpdateTimeRelateObject(Object obj) {
        if (this.mPtrClassicHeader != null) {
            this.mPtrClassicHeader.setLastUpdateTimeRelateObject(obj);
        }
    }
}
