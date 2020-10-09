package alimama.com.unwviewbase.pullandrefrsh.views.recycler.accessories;

import android.view.View;

public class FixedViewInfo {
    private int hashCode;
    private View view;

    public FixedViewInfo(View view2) {
        this.view = view2;
        this.hashCode = view2.hashCode();
    }

    public View getView() {
        return this.view;
    }

    public int getHashCode() {
        return this.hashCode;
    }
}
