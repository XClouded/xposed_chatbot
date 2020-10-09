package com.alibaba.aliweex.adapter.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.alibaba.aliweex.R;
import com.taobao.weex.ui.view.WXHorizontalScrollView;
import com.taobao.weex.utils.WXViewUtils;
import java.util.ArrayList;
import java.util.List;

public class Elevator {
    private ElevatorAdapter adapter;
    private int allItemWidth;
    /* access modifiers changed from: private */
    public Animation backAnimation;
    private int barWidth;
    private Context context;
    /* access modifiers changed from: private */
    public PopupWindow downPopupWindow;
    private List<ElevatorText> elevatorTextList = new ArrayList();
    private Animation firstAnimation;
    private WXHorizontalScrollView horizontalScrollView;
    private FrameLayout itemBar;
    /* access modifiers changed from: private */
    public TextView itemBarTv;
    /* access modifiers changed from: private */
    public List<ElevatorItem> itemList = new ArrayList();
    private GridView item_gridView;
    private LinearLayout item_linearBG;
    /* access modifiers changed from: private */
    public LinearLayout item_linearLayout;
    /* access modifiers changed from: private */
    public ElevatorOnClicklistener mListener;
    private ViewGroup mRootView;
    /* access modifiers changed from: private */
    public IWATabHeaderChanged mWATabHeaderChanged;
    private String normalColor;
    /* access modifiers changed from: private */
    public ViewGroup pullButton;
    private ImageView pullImage;
    int remainWidth = 0;
    int scrollWidth = 0;
    private String selectedColor;
    /* access modifiers changed from: private */
    public int textHeight;

    public interface ElevatorOnClicklistener {
        void OnClick(ElevatorItem elevatorItem);
    }

    public interface IWATabHeaderChanged {
        void changed();
    }

    public Elevator(Context context2) {
        this.context = context2;
        this.selectedColor = "#EE0A3B";
        this.normalColor = "#333333";
        this.firstAnimation = AnimationUtils.loadAnimation(context2, R.anim.huichang_elevator_first_rotate);
        this.backAnimation = AnimationUtils.loadAnimation(context2, R.anim.huichang_elevator_back_rotate);
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        this.firstAnimation.setInterpolator(linearInterpolator);
        this.backAnimation.setInterpolator(linearInterpolator);
        this.firstAnimation.setFillAfter(true);
        this.backAnimation.setFillAfter(true);
        this.mRootView = (ViewGroup) LayoutInflater.from(context2).inflate(R.layout.huichang_elevator_layout, (ViewGroup) null);
        this.item_linearLayout = (LinearLayout) this.mRootView.findViewById(R.id.linear);
        this.item_linearLayout.setGravity(16);
        this.item_linearBG = (LinearLayout) this.mRootView.findViewById(R.id.linear_bg);
        this.itemBar = (FrameLayout) this.mRootView.findViewById(R.id.itembar);
        this.horizontalScrollView = (WXHorizontalScrollView) this.mRootView.findViewById(R.id.horizontalscroll);
        this.item_gridView = (GridView) this.mRootView.findViewById(R.id.gridView);
        this.pullButton = (ViewGroup) this.mRootView.findViewById(R.id.pullButton);
        this.pullButton.setVisibility(4);
        this.pullImage = (ImageView) this.mRootView.findViewById(R.id.pullImage);
        this.itemBarTv = (TextView) this.mRootView.findViewById(R.id.downText);
        this.itemBar.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
        this.barWidth = this.itemBar.getMeasuredWidth();
        this.adapter = new ElevatorAdapter(context2, R.layout.huichang_tbelevatortext_layout, this.itemList);
        View inflate = LayoutInflater.from(context2).inflate(R.layout.downpop_window, (ViewGroup) null);
        this.downPopupWindow = new PopupWindow(inflate, -1, -1);
        this.downPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                Elevator.this.itemBarTv.setVisibility(4);
                Elevator.this.item_linearLayout.setVisibility(0);
                Elevator.this.pullButton.startAnimation(Elevator.this.backAnimation);
            }
        });
        this.downPopupWindow.setTouchable(true);
        this.downPopupWindow.setFocusable(true);
        this.firstAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                if (Elevator.this.mWATabHeaderChanged != null) {
                    Elevator.this.mWATabHeaderChanged.changed();
                }
            }
        });
        this.backAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                if (Elevator.this.mWATabHeaderChanged != null) {
                    Elevator.this.mWATabHeaderChanged.changed();
                }
            }
        });
        this.downPopupWindow.getContentView().setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Elevator.this.downPopupWindow.setFocusable(false);
                Elevator.this.dismissPopWindow();
                if (Elevator.this.mWATabHeaderChanged == null) {
                    return true;
                }
                Elevator.this.mWATabHeaderChanged.changed();
                return true;
            }
        });
        this.horizontalScrollView.setScrollViewListener(new WXHorizontalScrollView.ScrollViewListener() {
            public void onScrollChanged(WXHorizontalScrollView wXHorizontalScrollView, int i, int i2, int i3, int i4) {
                if (Elevator.this.mWATabHeaderChanged != null) {
                    Elevator.this.mWATabHeaderChanged.changed();
                }
            }
        });
        ((LinearLayout) inflate.findViewById(R.id.downMongolia)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Elevator.this.dismissPopWindow();
                if (Elevator.this.mWATabHeaderChanged != null) {
                    Elevator.this.mWATabHeaderChanged.changed();
                }
            }
        });
        this.item_gridView = (GridView) inflate.findViewById(R.id.gridView);
        this.item_gridView.setAdapter(this.adapter);
        this.item_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (Elevator.this.mListener != null) {
                    Elevator.this.mListener.OnClick((ElevatorItem) Elevator.this.itemList.get(i));
                }
                Elevator.this.dismissPopWindow();
            }
        });
        final int[] iArr = new int[2];
        this.pullButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"NewApi"})
            public void onClick(View view) {
                Elevator.this.downPopupWindow.setFocusable(true);
                view.getLocationOnScreen(iArr);
                if (Elevator.this.downPopupWindow.isShowing()) {
                    Elevator.this.dismissPopWindow();
                } else {
                    int[] iArr = new int[2];
                    view.getLocationOnScreen(iArr);
                    view.getWindowVisibleDisplayFrame(new Rect());
                    int access$900 = (iArr[1] - (Elevator.this.textHeight / 2)) - WXViewUtils.dip2px(46.5f);
                    if (Build.VERSION.SDK_INT >= 24) {
                        int[] iArr2 = new int[2];
                        view.getLocationInWindow(iArr2);
                        Elevator.this.downPopupWindow.showAtLocation(view, 0, 0, access$900 < 0 ? (-access$900) + iArr2[1] + view.getHeight() : iArr2[1] + view.getHeight());
                    } else {
                        Elevator.this.downPopupWindow.showAsDropDown(view, 0, 0);
                    }
                    Elevator.this.showPopWindow();
                }
                if (Elevator.this.mWATabHeaderChanged != null) {
                    Elevator.this.mWATabHeaderChanged.changed();
                }
            }
        });
    }

    public ViewGroup getRootView() {
        return this.mRootView;
    }

    public void setList(List<ElevatorItem> list) {
        boolean z;
        this.itemList.clear();
        this.itemList.addAll(list);
        this.item_linearLayout.removeAllViews();
        this.elevatorTextList.clear();
        this.adapter.notifyDataSetChanged();
        int size = this.itemList.size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            ElevatorItem elevatorItem = this.itemList.get(i2);
            ElevatorText elevatorText = new ElevatorText(this.context);
            int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
            int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(0, 0);
            elevatorText.setText(elevatorItem.getName());
            elevatorText.setSelectedColor(this.selectedColor);
            elevatorText.setNormalColor(this.normalColor);
            elevatorText.measure(makeMeasureSpec, makeMeasureSpec2);
            elevatorItem.setWidth(elevatorText.getMeasuredWidth());
            elevatorItem.setId(i);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, this.textHeight > 0 ? this.textHeight : -1);
            layoutParams.setMargins(WXViewUtils.dip2px(6.0f), 0, 0, 0);
            elevatorText.setLayoutParams(layoutParams);
            this.elevatorTextList.add(elevatorText);
            elevatorText.setTag(elevatorItem);
            elevatorText.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    ElevatorItem elevatorItem = (ElevatorItem) view.getTag();
                    if (Elevator.this.mListener != null) {
                        Elevator.this.mListener.OnClick(elevatorItem);
                    }
                }
            });
            this.item_linearLayout.addView(elevatorText);
            i++;
        }
        int size2 = this.elevatorTextList.size();
        for (int i3 = 0; i3 < size2; i3++) {
            this.elevatorTextList.get(i3).hide();
        }
        this.elevatorTextList.get(0).show();
        this.allItemWidth = 0;
        for (int i4 = 0; i4 < size; i4++) {
            ElevatorItem elevatorItem2 = this.itemList.get(i4);
            elevatorItem2.setIsHighLight(false);
            elevatorItem2.setIsImgShow(false);
            this.allItemWidth += elevatorItem2.getWidth();
        }
        int i5 = this.context.getResources().getDisplayMetrics().widthPixels;
        if (this.allItemWidth + (WXViewUtils.dip2px(6.0f) * this.elevatorTextList.size()) <= i5 - ((int) TypedValue.applyDimension(1, 24.0f, this.context.getResources().getDisplayMetrics()))) {
            int dip2px = (i5 - (WXViewUtils.dip2px(6.0f) * this.elevatorTextList.size())) / this.elevatorTextList.size();
            ArrayList arrayList = new ArrayList();
            int i6 = 0;
            for (int i7 = 0; i7 < this.itemList.size(); i7++) {
                ElevatorItem elevatorItem3 = this.itemList.get(i7);
                if (elevatorItem3.getWidth() > dip2px) {
                    i6 += elevatorItem3.getWidth();
                    arrayList.add(Integer.valueOf(i7));
                }
            }
            if (this.elevatorTextList.size() > arrayList.size()) {
                int dip2px2 = ((i5 - (WXViewUtils.dip2px(6.0f) * this.elevatorTextList.size())) - i6) / (this.elevatorTextList.size() - arrayList.size());
                for (int i8 = 0; i8 < this.itemList.size(); i8++) {
                    int i9 = 0;
                    while (true) {
                        if (i9 >= arrayList.size()) {
                            z = false;
                            break;
                        } else if (((Integer) arrayList.get(i9)).intValue() == i8) {
                            z = true;
                            break;
                        } else {
                            i9++;
                        }
                    }
                    if (!z) {
                        this.itemList.get(i8).setWidth(dip2px2);
                    }
                }
            }
            for (int i10 = 0; i10 < this.elevatorTextList.size(); i10++) {
                ElevatorText elevatorText2 = this.elevatorTextList.get(i10);
                ViewGroup.LayoutParams layoutParams2 = elevatorText2.getLayoutParams();
                if (layoutParams2 != null) {
                    layoutParams2.width = this.itemList.get(i10).getWidth();
                    elevatorText2.setLayoutParams(layoutParams2);
                }
            }
            this.pullButton.setVisibility(8);
        } else {
            this.pullButton.setVisibility(0);
        }
        this.itemList.get(0).setIsHighLight(true);
        this.itemList.get(0).setIsImgShow(true);
        this.adapter.notifyDataSetChanged();
    }

    public void setElevatorOnClickListener(ElevatorOnClicklistener elevatorOnClicklistener) {
        this.mListener = elevatorOnClicklistener;
    }

    public void setSelectedColor(String str) {
        this.selectedColor = str;
        if (this.elevatorTextList != null) {
            for (int i = 0; i < this.elevatorTextList.size(); i++) {
                ElevatorText elevatorText = this.elevatorTextList.get(i);
                elevatorText.setSelectedColor(str);
                if (i < this.itemList.size()) {
                    if (this.itemList.get(i).getIsHighLight()) {
                        elevatorText.show();
                    } else {
                        elevatorText.hide();
                    }
                }
            }
        }
        this.adapter.setSelectedColor(str);
    }

    public void setNormalColor(String str) {
        this.normalColor = str;
        if (this.elevatorTextList != null) {
            for (int i = 0; i < this.elevatorTextList.size(); i++) {
                ElevatorText elevatorText = this.elevatorTextList.get(i);
                elevatorText.setNormalColor(this.selectedColor);
                if (i < this.itemList.size()) {
                    if (this.itemList.get(i).getIsHighLight()) {
                        elevatorText.show();
                    } else {
                        elevatorText.hide();
                    }
                }
            }
        }
        this.adapter.setNormalColor(str);
    }

    public void setBackgroundColor(String str) {
        this.item_linearBG.setBackgroundColor(Color.parseColor(str));
        if (this.downPopupWindow.getContentView() != null) {
            this.item_gridView.setBackgroundColor(Color.parseColor(str));
        }
    }

    public void setTabAlpha(float f) {
        this.item_linearBG.setAlpha(f);
    }

    public void setLocation(int i) {
        int size = this.elevatorTextList.size();
        if (i >= 0 && i < size) {
            for (ElevatorText hide : this.elevatorTextList) {
                hide.hide();
            }
            this.elevatorTextList.get(i).show();
            for (ElevatorItem next : this.itemList) {
                next.setIsHighLight(false);
                next.setIsImgShow(false);
            }
            this.itemList.get(i).setIsHighLight(true);
            this.itemList.get(i).setIsImgShow(true);
            this.remainWidth = 0;
            this.scrollWidth = 0;
            for (int i2 = i; i2 < this.itemList.size(); i2++) {
                this.remainWidth += this.itemList.get(i).getWidth();
            }
            for (int i3 = 0; i3 < i; i3++) {
                this.scrollWidth += this.itemList.get(i3).getWidth() + WXViewUtils.dip2px(6.0f);
            }
            this.horizontalScrollView.smoothScrollTo(this.scrollWidth - (i > 0 ? (this.context.getResources().getDisplayMetrics().widthPixels / 2) - (this.itemList.get(i - 1).getWidth() / 2) : 0), 0);
            if (this.mWATabHeaderChanged != null) {
                this.mWATabHeaderChanged.changed();
            }
            this.adapter.notifyDataSetChanged();
        }
    }

    public void setTextHeight(int i) {
        this.textHeight = i;
    }

    public void setIWATabHeaderChanged(IWATabHeaderChanged iWATabHeaderChanged) {
        this.mWATabHeaderChanged = iWATabHeaderChanged;
    }

    public void reBindImage() {
        this.pullImage.setImageResource(R.drawable.huichang_elevator_pulldown);
    }

    /* access modifiers changed from: private */
    public void dismissPopWindow() {
        this.downPopupWindow.dismiss();
    }

    /* access modifiers changed from: private */
    public void showPopWindow() {
        this.itemBarTv.setVisibility(0);
        this.item_linearLayout.setVisibility(4);
        this.pullButton.startAnimation(this.firstAnimation);
    }
}
