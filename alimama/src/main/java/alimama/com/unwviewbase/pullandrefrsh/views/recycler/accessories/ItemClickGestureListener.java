package alimama.com.unwviewbase.pullandrefrsh.views.recycler.accessories;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ItemClickGestureListener extends GestureDetector.SimpleOnGestureListener {
    private RecyclerView mHostView;
    private View mTargetChild;

    public abstract boolean performItemClick(RecyclerView recyclerView, View view, int i, long j);

    public abstract boolean performItemLongClick(RecyclerView recyclerView, View view, int i, long j);

    public ItemClickGestureListener(RecyclerView recyclerView) {
        this.mHostView = recyclerView;
    }

    public boolean onDown(MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        if (this.mHostView != null) {
            this.mTargetChild = this.mHostView.findChildViewUnder((float) x, (float) y);
        }
        return this.mTargetChild != null;
    }

    public void onShowPress(MotionEvent motionEvent) {
        if (this.mTargetChild != null) {
            this.mTargetChild.setPressed(true);
        }
    }

    public boolean onSingleTapUp(MotionEvent motionEvent) {
        if (this.mTargetChild == null || this.mHostView == null) {
            return false;
        }
        this.mTargetChild.setPressed(false);
        int childLayoutPosition = this.mHostView.getChildLayoutPosition(this.mTargetChild);
        boolean performItemClick = performItemClick(this.mHostView, this.mTargetChild, childLayoutPosition, this.mHostView.getAdapter().getItemId(childLayoutPosition));
        this.mTargetChild = null;
        return performItemClick;
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (this.mTargetChild == null) {
            return false;
        }
        this.mTargetChild.setPressed(false);
        this.mTargetChild = null;
        return true;
    }

    public void onLongPress(MotionEvent motionEvent) {
        if (this.mTargetChild != null && this.mHostView != null) {
            int childLayoutPosition = this.mHostView.getChildLayoutPosition(this.mTargetChild);
            if (performItemLongClick(this.mHostView, this.mTargetChild, childLayoutPosition, this.mHostView.getAdapter().getItemId(childLayoutPosition))) {
                this.mTargetChild.setPressed(false);
                this.mTargetChild = null;
            }
        }
    }
}
