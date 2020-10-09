package in.srain.cube.views.banner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager.widget.PagerAdapter;

public abstract class BannerAdapter extends PagerAdapter {
    public int getPositionForIndicator(int i) {
        return i;
    }

    public abstract View getView(LayoutInflater layoutInflater, int i);

    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    public Object instantiateItem(ViewGroup viewGroup, int i) {
        View view = getView(LayoutInflater.from(viewGroup.getContext()), i);
        viewGroup.addView(view);
        return view;
    }

    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeView((View) obj);
    }
}
