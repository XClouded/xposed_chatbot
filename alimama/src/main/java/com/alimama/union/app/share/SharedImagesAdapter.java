package com.alimama.union.app.share;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.alimama.moon.R;
import com.alimama.moon.utils.ToastUtil;
import com.alimama.unionwl.base.etaodrawee.EtaoDraweeView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SharedImagesAdapter extends RecyclerView.Adapter<ImageViewHolder> {
    private static final String TAG = "SharedImagesAdapter";
    private final View.OnClickListener mCheckmarkClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            Integer num = (Integer) view.getTag();
            boolean contains = SharedImagesAdapter.this.mSelectedImages.contains(num);
            if (!contains || SharedImagesAdapter.this.mSelectedImages.size() != 1) {
                view.setSelected(!contains);
                if (contains) {
                    SharedImagesAdapter.this.mSelectedImages.remove(num);
                } else {
                    SharedImagesAdapter.this.mSelectedImages.add(num);
                }
                if (SharedImagesAdapter.this.mImageSelectListener != null) {
                    SharedImagesAdapter.this.mImageSelectListener.onClick(view);
                    return;
                }
                return;
            }
            ToastUtil.showToast(view.getContext(), (int) R.string.share_select_at_least_one_image);
        }
    };
    /* access modifiers changed from: private */
    @Nullable
    public View.OnClickListener mImageSelectListener;
    @NonNull
    private final List<String> mImagesUrl = new ArrayList();
    /* access modifiers changed from: private */
    public final List<Integer> mSelectedImages = new ArrayList();

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public ImageView checkmark;
        /* access modifiers changed from: private */
        public EtaoDraweeView itemImageView;

        ImageViewHolder(View view) {
            super(view);
            this.checkmark = (ImageView) view.findViewById(R.id.iv_checkmark);
            this.itemImageView = (EtaoDraweeView) view.findViewById(R.id.iv_item);
        }
    }

    public SharedImagesAdapter(@Nullable View.OnClickListener onClickListener) {
        this.mImageSelectListener = onClickListener;
    }

    public void setImages(@NonNull List<String> list) {
        this.mImagesUrl.clear();
        this.mImagesUrl.addAll(list);
        this.mSelectedImages.clear();
        if (!this.mImagesUrl.isEmpty()) {
            this.mSelectedImages.add(0);
        }
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return this.mSelectedImages.size();
    }

    public List<String> getSelectedImagesSortByIndex() {
        ArrayList arrayList = new ArrayList();
        Collections.sort(this.mSelectedImages);
        for (Integer next : this.mSelectedImages) {
            if (next.intValue() >= 0 && next.intValue() < this.mImagesUrl.size()) {
                arrayList.add(this.mImagesUrl.get(next.intValue()));
            }
        }
        return arrayList;
    }

    public ImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ImageViewHolder imageViewHolder = new ImageViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.include_selectable_imageview, viewGroup, false));
        imageViewHolder.checkmark.setOnClickListener(this.mCheckmarkClickListener);
        return imageViewHolder;
    }

    public void onBindViewHolder(ImageViewHolder imageViewHolder, int i) {
        if (i < 0 || i >= this.mImagesUrl.size()) {
            Log.e(TAG, "onBindViewHolder position out of bounds");
            return;
        }
        imageViewHolder.itemImageView.setAnyImageUrl(this.mImagesUrl.get(i));
        imageViewHolder.checkmark.setTag(Integer.valueOf(i));
        imageViewHolder.checkmark.setSelected(this.mSelectedImages.contains(Integer.valueOf(i)));
    }

    public int getItemCount() {
        return this.mImagesUrl.size();
    }
}
