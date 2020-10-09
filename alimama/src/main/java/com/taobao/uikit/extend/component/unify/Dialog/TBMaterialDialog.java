package com.taobao.uikit.extend.component.unify.Dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.ArrayRes;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.UiThread;
import androidx.core.content.res.ResourcesCompat;
import com.taobao.uikit.extend.R;
import com.taobao.uikit.extend.component.unify.TBButtonType;
import com.taobao.uikit.extend.utils.ResourceUtils;
import com.taobao.uikit.extend.utils.RippleHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TBMaterialDialog extends TBDialogBase implements View.OnClickListener, AdapterView.OnItemClickListener {
    protected ImageView closeButton;
    protected TextView content;
    protected FrameLayout customViewFrame;
    protected ImageView icon;
    protected ListType listType;
    protected ListView listView;
    protected final Builder mBuilder;
    private final Handler mHandler = new Handler();
    protected TBDialogButton negativeButton;
    protected TBDialogButton neutralButton;
    protected TBDialogButton positiveButton;
    protected List<Integer> selectedIndicesList;
    protected TextView title;
    protected View titleFrame;

    public interface ListCallback {
        void onSelection(TBMaterialDialog tBMaterialDialog, View view, int i, TBSimpleListItem tBSimpleListItem);
    }

    public interface ListCallbackMultiChoice {
        boolean onSelection(TBMaterialDialog tBMaterialDialog, Integer[] numArr, CharSequence[] charSequenceArr);
    }

    public interface ListCallbackSingleChoice {
        boolean onSelection(TBMaterialDialog tBMaterialDialog, View view, int i, CharSequence charSequence);
    }

    public interface SingleButtonCallback {
        void onClick(@NonNull TBMaterialDialog tBMaterialDialog, @NonNull DialogAction dialogAction);
    }

    public final Builder getBuilder() {
        return this.mBuilder;
    }

    @SuppressLint({"InflateParams"})
    protected TBMaterialDialog(Builder builder) {
        super(builder.context, TBDialogInit.getTheme(builder));
        this.mBuilder = builder;
        this.view = (TBDialogRootLayout) LayoutInflater.from(builder.context).inflate(TBDialogInit.getInflateLayout(builder), (ViewGroup) null);
        TBDialogInit.init(this);
    }

    /* access modifiers changed from: protected */
    public final void checkIfListInitScroll() {
        if (this.listView != null) {
            this.listView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    int i;
                    if (Build.VERSION.SDK_INT < 16) {
                        TBMaterialDialog.this.listView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        TBMaterialDialog.this.listView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    if (TBMaterialDialog.this.listType == ListType.SINGLE || TBMaterialDialog.this.listType == ListType.MULTI) {
                        if (TBMaterialDialog.this.listType == ListType.SINGLE) {
                            if (TBMaterialDialog.this.mBuilder.selectedIndex >= 0) {
                                i = TBMaterialDialog.this.mBuilder.selectedIndex;
                            } else {
                                return;
                            }
                        } else if (TBMaterialDialog.this.selectedIndicesList != null && TBMaterialDialog.this.selectedIndicesList.size() != 0) {
                            Collections.sort(TBMaterialDialog.this.selectedIndicesList);
                            i = TBMaterialDialog.this.selectedIndicesList.get(0).intValue();
                        } else {
                            return;
                        }
                        if (TBMaterialDialog.this.listView.getLastVisiblePosition() < i) {
                            final int lastVisiblePosition = i - ((TBMaterialDialog.this.listView.getLastVisiblePosition() - TBMaterialDialog.this.listView.getFirstVisiblePosition()) / 2);
                            if (lastVisiblePosition < 0) {
                                lastVisiblePosition = 0;
                            }
                            TBMaterialDialog.this.listView.post(new Runnable() {
                                public void run() {
                                    TBMaterialDialog.this.listView.requestFocus();
                                    TBMaterialDialog.this.listView.setSelection(lastVisiblePosition);
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public final void invalidateList() {
        if (this.listView != null) {
            if ((this.mBuilder.items != null && this.mBuilder.items.length != 0) || this.mBuilder.adapter != null) {
                this.listView.setAdapter(this.mBuilder.adapter);
                if (this.listType != null || this.mBuilder.listCallbackCustom != null) {
                    this.listView.setOnItemClickListener(this);
                }
            }
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        boolean z;
        if (this.mBuilder.listCallbackCustom != null) {
            this.mBuilder.listCallbackCustom.onSelection(this, view, i, this.mBuilder.items[i]);
        } else if (this.listType == null || this.listType == ListType.REGULAR) {
            if (this.mBuilder.autoDismiss) {
                dismiss();
            }
            if (this.mBuilder.listCallback != null) {
                this.mBuilder.listCallback.onSelection(this, view, i, this.mBuilder.items[i]);
            }
        } else if (this.listType == ListType.MULTI) {
            boolean z2 = !this.selectedIndicesList.contains(Integer.valueOf(i));
            CheckBox checkBox = (CheckBox) view.findViewById(R.id.uik_mdControl);
            if (z2) {
                this.selectedIndicesList.add(Integer.valueOf(i));
                if (!this.mBuilder.alwaysCallMultiChoiceCallback) {
                    checkBox.setChecked(true);
                } else if (sendMultichoiceCallback()) {
                    checkBox.setChecked(true);
                } else {
                    this.selectedIndicesList.remove(Integer.valueOf(i));
                }
            } else {
                this.selectedIndicesList.remove(Integer.valueOf(i));
                checkBox.setChecked(false);
                if (this.mBuilder.alwaysCallMultiChoiceCallback) {
                    sendMultichoiceCallback();
                }
            }
        } else if (this.listType == ListType.SINGLE) {
            TBDialogDefaultAdapter tBDialogDefaultAdapter = (TBDialogDefaultAdapter) this.mBuilder.adapter;
            RadioButton radioButton = (RadioButton) view.findViewById(R.id.uik_mdControl);
            if (this.mBuilder.autoDismiss && this.mBuilder.positiveText == null) {
                dismiss();
                this.mBuilder.selectedIndex = i;
                sendSingleChoiceCallback(view);
                z = false;
            } else if (this.mBuilder.alwaysCallSingleChoiceCallback) {
                int i2 = this.mBuilder.selectedIndex;
                this.mBuilder.selectedIndex = i;
                z = sendSingleChoiceCallback(view);
                this.mBuilder.selectedIndex = i2;
            } else {
                z = true;
            }
            if (z) {
                this.mBuilder.selectedIndex = i;
                radioButton.setChecked(true);
                tBDialogDefaultAdapter.notifyDataSetChanged();
            }
        }
    }

    public static class NotImplementedException extends Error {
        public NotImplementedException(String str) {
            super(str);
        }
    }

    public static class DialogException extends WindowManager.BadTokenException {
        public DialogException(String str) {
            super(str);
        }
    }

    /* access modifiers changed from: protected */
    public final Drawable getListSelector() {
        if (this.mBuilder.listSelector != 0) {
            return ResourcesCompat.getDrawable(this.mBuilder.context.getResources(), this.mBuilder.listSelector, (Resources.Theme) null);
        }
        Drawable resolveDrawable = ResourceUtils.resolveDrawable(this.mBuilder.context, R.attr.uik_mdListSelector);
        if (resolveDrawable != null) {
            return resolveDrawable;
        }
        return ResourceUtils.resolveDrawable(getContext(), R.attr.uik_mdListSelector);
    }

    /* access modifiers changed from: package-private */
    public Drawable getButtonSelector(DialogAction dialogAction, boolean z) {
        if (!z) {
            switch (dialogAction) {
                case NEUTRAL:
                    if (this.mBuilder.btnSelectorNeutral != 0) {
                        return ResourcesCompat.getDrawable(this.mBuilder.context.getResources(), this.mBuilder.btnSelectorNeutral, (Resources.Theme) null);
                    }
                    Drawable resolveDrawable = ResourceUtils.resolveDrawable(this.mBuilder.context, R.attr.uik_mdBtnNeutralSelector);
                    if (resolveDrawable != null) {
                        return resolveDrawable;
                    }
                    Drawable resolveDrawable2 = ResourceUtils.resolveDrawable(getContext(), R.attr.uik_mdBtnNeutralSelector);
                    if (Build.VERSION.SDK_INT >= 21) {
                        RippleHelper.applyColor(resolveDrawable2, this.mBuilder.buttonRippleColor);
                    }
                    return resolveDrawable2;
                case NEGATIVE:
                    if (this.mBuilder.btnSelectorNegative != 0) {
                        return ResourcesCompat.getDrawable(this.mBuilder.context.getResources(), this.mBuilder.btnSelectorNegative, (Resources.Theme) null);
                    }
                    Drawable resolveDrawable3 = ResourceUtils.resolveDrawable(this.mBuilder.context, R.attr.uik_mdBtnNegativeSelector);
                    if (resolveDrawable3 != null) {
                        return resolveDrawable3;
                    }
                    Drawable resolveDrawable4 = ResourceUtils.resolveDrawable(getContext(), R.attr.uik_mdBtnNegativeSelector);
                    if (Build.VERSION.SDK_INT >= 21) {
                        RippleHelper.applyColor(resolveDrawable4, this.mBuilder.buttonRippleColor);
                    }
                    return resolveDrawable4;
                default:
                    if (this.mBuilder.btnSelectorPositive != 0) {
                        return ResourcesCompat.getDrawable(this.mBuilder.context.getResources(), this.mBuilder.btnSelectorPositive, (Resources.Theme) null);
                    }
                    Drawable resolveDrawable5 = ResourceUtils.resolveDrawable(this.mBuilder.context, R.attr.uik_mdBtnPositiveSelector);
                    if (resolveDrawable5 != null) {
                        return resolveDrawable5;
                    }
                    Drawable resolveDrawable6 = ResourceUtils.resolveDrawable(getContext(), R.attr.uik_mdBtnPositiveSelector);
                    if (Build.VERSION.SDK_INT >= 21) {
                        RippleHelper.applyColor(resolveDrawable6, this.mBuilder.buttonRippleColor);
                    }
                    return resolveDrawable6;
            }
        } else if (this.mBuilder.btnSelectorStacked != 0) {
            return ResourcesCompat.getDrawable(this.mBuilder.context.getResources(), this.mBuilder.btnSelectorStacked, (Resources.Theme) null);
        } else {
            Drawable resolveDrawable7 = ResourceUtils.resolveDrawable(this.mBuilder.context, R.attr.uik_mdBtnStackedSelector);
            if (resolveDrawable7 != null) {
                return resolveDrawable7;
            }
            return ResourceUtils.resolveDrawable(getContext(), R.attr.uik_mdBtnStackedSelector);
        }
    }

    private boolean sendSingleChoiceCallback(View view) {
        if (this.mBuilder.listCallbackSingleChoice == null) {
            return false;
        }
        String str = null;
        if (this.mBuilder.selectedIndex >= 0 && this.mBuilder.selectedIndex < this.mBuilder.items.length) {
            str = this.mBuilder.items[this.mBuilder.selectedIndex].getText();
        }
        return this.mBuilder.listCallbackSingleChoice.onSelection(this, view, this.mBuilder.selectedIndex, str);
    }

    private boolean sendMultichoiceCallback() {
        if (this.mBuilder.listCallbackMultiChoice == null) {
            return false;
        }
        Collections.sort(this.selectedIndicesList);
        ArrayList arrayList = new ArrayList();
        for (Integer next : this.selectedIndicesList) {
            if (next.intValue() >= 0 && next.intValue() <= this.mBuilder.items.length - 1) {
                arrayList.add(this.mBuilder.items[next.intValue()].getText());
            }
        }
        return this.mBuilder.listCallbackMultiChoice.onSelection(this, (Integer[]) this.selectedIndicesList.toArray(new Integer[this.selectedIndicesList.size()]), (CharSequence[]) arrayList.toArray(new CharSequence[arrayList.size()]));
    }

    public final void onClick(View view) {
        DialogAction dialogAction = (DialogAction) view.getTag();
        switch (dialogAction) {
            case NEUTRAL:
                if (this.mBuilder.callback != null) {
                    this.mBuilder.callback.onAny(this);
                    this.mBuilder.callback.onNeutral(this);
                }
                if (this.mBuilder.onNeutralCallback != null) {
                    this.mBuilder.onNeutralCallback.onClick(this, dialogAction);
                }
                if (this.mBuilder.autoDismiss) {
                    dismiss();
                    break;
                }
                break;
            case NEGATIVE:
                if (this.mBuilder.callback != null) {
                    this.mBuilder.callback.onAny(this);
                    this.mBuilder.callback.onNegative(this);
                }
                if (this.mBuilder.onNegativeCallback != null) {
                    this.mBuilder.onNegativeCallback.onClick(this, dialogAction);
                }
                if (this.mBuilder.autoDismiss) {
                    dismiss();
                    break;
                }
                break;
            case POSITIVE:
                if (this.mBuilder.callback != null) {
                    this.mBuilder.callback.onAny(this);
                    this.mBuilder.callback.onPositive(this);
                }
                if (this.mBuilder.onPositiveCallback != null) {
                    this.mBuilder.onPositiveCallback.onClick(this, dialogAction);
                }
                sendSingleChoiceCallback(view);
                sendMultichoiceCallback();
                if (this.mBuilder.autoDismiss) {
                    dismiss();
                    break;
                }
                break;
            case CLOSE:
                if (this.mBuilder.autoDismiss) {
                    dismiss();
                    break;
                }
                break;
        }
        if (this.mBuilder.onAnyCallback != null) {
            this.mBuilder.onAnyCallback.onClick(this, dialogAction);
        }
    }

    public static class Builder {
        protected ListAdapter adapter;
        protected boolean alwaysCallMultiChoiceCallback;
        protected boolean alwaysCallSingleChoiceCallback;
        protected boolean autoDismiss;
        protected int backgroundColor;
        @DrawableRes
        protected int btnSelectorNegative;
        @DrawableRes
        protected int btnSelectorNeutral;
        @DrawableRes
        protected int btnSelectorPositive;
        @DrawableRes
        protected int btnSelectorStacked;
        protected GravityEnum btnStackedGravity = GravityEnum.END;
        protected int buttonRippleColor;
        protected GravityEnum buttonsGravity = GravityEnum.START;
        protected ButtonCallback callback;
        protected DialogInterface.OnCancelListener cancelListener;
        protected boolean cancelable;
        protected boolean cardDialog;
        protected CharSequence content;
        protected int contentColor;
        protected boolean contentColorSet;
        protected GravityEnum contentGravity = GravityEnum.START;
        protected float contentLineSpacingMultiplier;
        protected final Context context;
        protected View customView;
        protected DialogInterface.OnDismissListener dismissListener;
        protected int dividerColor;
        protected boolean dividerColorSet;
        protected boolean forceStacking;
        protected Drawable icon;
        protected int itemColor;
        protected boolean itemColorSet;
        protected int[] itemIds;
        protected TBSimpleListItem[] items;
        protected GravityEnum itemsGravity = GravityEnum.START;
        protected DialogInterface.OnKeyListener keyListener;
        protected boolean limitIconToDefaultSize;
        protected ColorStateList linkColor;
        protected ListCallback listCallback;
        protected ListCallback listCallbackCustom;
        protected ListCallbackMultiChoice listCallbackMultiChoice;
        protected ListCallbackSingleChoice listCallbackSingleChoice;
        @DrawableRes
        protected int listSelector;
        protected TBMaterialDialog mMaterialDialog = null;
        protected int maxIconSize;
        protected ColorStateList negativeColor;
        protected boolean negativeColorSet;
        protected CharSequence negativeText;
        protected ColorStateList neutralColor;
        protected boolean neutralColorSet;
        protected CharSequence neutralText;
        protected SingleButtonCallback onAnyCallback;
        protected SingleButtonCallback onNegativeCallback;
        protected SingleButtonCallback onNeutralCallback;
        protected SingleButtonCallback onPositiveCallback;
        protected ColorStateList positiveColor;
        protected boolean positiveColorSet;
        protected CharSequence positiveText;
        protected int selectedIndex;
        protected Integer[] selectedIndices;
        protected DialogInterface.OnShowListener showListener;
        protected boolean showMinMax;
        protected Theme theme;
        protected CharSequence title;
        protected int titleColor;
        protected boolean titleColorSet;
        protected GravityEnum titleGravity = GravityEnum.START;
        protected int widgetColor;
        protected boolean widgetColorSet;
        protected boolean wrapCustomViewInScroll;

        public final Context getContext() {
            return this.context;
        }

        public final int getItemColor() {
            return this.itemColor;
        }

        public Builder(@NonNull Context context2) {
            int i = 0;
            this.buttonRippleColor = 0;
            this.titleColor = -1;
            this.contentColor = -1;
            this.alwaysCallMultiChoiceCallback = false;
            this.alwaysCallSingleChoiceCallback = false;
            this.theme = Theme.LIGHT;
            this.cancelable = true;
            this.contentLineSpacingMultiplier = 1.2f;
            this.selectedIndex = -1;
            this.selectedIndices = null;
            this.autoDismiss = true;
            this.maxIconSize = -1;
            this.titleColorSet = false;
            this.contentColorSet = false;
            this.itemColorSet = false;
            this.positiveColorSet = false;
            this.neutralColorSet = false;
            this.negativeColorSet = false;
            this.widgetColorSet = false;
            this.dividerColorSet = false;
            this.context = context2;
            this.widgetColor = ResourceUtils.resolveColor(context2, R.attr.colorAccent, ResourceUtils.getColor(context2, R.color.uik_mdMaterialBlue600));
            if (Build.VERSION.SDK_INT >= 21) {
                this.widgetColor = ResourceUtils.resolveColor(context2, 16843829, this.widgetColor);
            }
            this.positiveColor = ResourceUtils.getActionTextStateList(context2, this.widgetColor);
            this.negativeColor = ResourceUtils.getActionTextStateList(context2, this.widgetColor);
            this.neutralColor = ResourceUtils.getActionTextStateList(context2, this.widgetColor);
            this.linkColor = ResourceUtils.getActionTextStateList(context2, ResourceUtils.resolveColor(context2, R.attr.uik_mdLinkColor, this.widgetColor));
            this.buttonRippleColor = ResourceUtils.resolveColor(context2, R.attr.uik_mdBtnRippleColor, ResourceUtils.resolveColor(context2, R.attr.colorControlHighlight, Build.VERSION.SDK_INT >= 21 ? ResourceUtils.resolveColor(context2, 16843820) : i));
            this.theme = ResourceUtils.isColorDark(ResourceUtils.resolveColor(context2, 16842806)) ? Theme.LIGHT : Theme.DARK;
            checkSingleton();
            this.titleGravity = ResourceUtils.resolveGravityEnum(context2, R.attr.uik_mdTitleGravity, this.titleGravity);
            this.contentGravity = ResourceUtils.resolveGravityEnum(context2, R.attr.uik_mdContentGravity, this.contentGravity);
            this.btnStackedGravity = ResourceUtils.resolveGravityEnum(context2, R.attr.uik_mdBtnstackedGravity, this.btnStackedGravity);
            this.itemsGravity = ResourceUtils.resolveGravityEnum(context2, R.attr.uik_mdItemsGravity, this.itemsGravity);
            this.buttonsGravity = ResourceUtils.resolveGravityEnum(context2, R.attr.uik_mdButtonsGravity, this.buttonsGravity);
        }

        private void checkSingleton() {
            if (ThemeSingleton.get(false) != null) {
                ThemeSingleton themeSingleton = ThemeSingleton.get();
                if (themeSingleton.darkTheme) {
                    this.theme = Theme.DARK;
                }
                if (themeSingleton.titleColor != 0) {
                    this.titleColor = themeSingleton.titleColor;
                }
                if (themeSingleton.contentColor != 0) {
                    this.contentColor = themeSingleton.contentColor;
                }
                if (themeSingleton.positiveColor != null) {
                    this.positiveColor = themeSingleton.positiveColor;
                }
                if (themeSingleton.neutralColor != null) {
                    this.neutralColor = themeSingleton.neutralColor;
                }
                if (themeSingleton.negativeColor != null) {
                    this.negativeColor = themeSingleton.negativeColor;
                }
                if (themeSingleton.itemColor != 0) {
                    this.itemColor = themeSingleton.itemColor;
                }
                if (themeSingleton.icon != null) {
                    this.icon = themeSingleton.icon;
                }
                if (themeSingleton.backgroundColor != 0) {
                    this.backgroundColor = themeSingleton.backgroundColor;
                }
                if (themeSingleton.dividerColor != 0) {
                    this.dividerColor = themeSingleton.dividerColor;
                }
                if (themeSingleton.btnSelectorStacked != 0) {
                    this.btnSelectorStacked = themeSingleton.btnSelectorStacked;
                }
                if (themeSingleton.listSelector != 0) {
                    this.listSelector = themeSingleton.listSelector;
                }
                if (themeSingleton.btnSelectorPositive != 0) {
                    this.btnSelectorPositive = themeSingleton.btnSelectorPositive;
                }
                if (themeSingleton.btnSelectorNeutral != 0) {
                    this.btnSelectorNeutral = themeSingleton.btnSelectorNeutral;
                }
                if (themeSingleton.btnSelectorNegative != 0) {
                    this.btnSelectorNegative = themeSingleton.btnSelectorNegative;
                }
                if (themeSingleton.widgetColor != 0) {
                    this.widgetColor = themeSingleton.widgetColor;
                }
                if (themeSingleton.linkColor != null) {
                    this.linkColor = themeSingleton.linkColor;
                }
                this.titleGravity = themeSingleton.titleGravity;
                this.contentGravity = themeSingleton.contentGravity;
                this.btnStackedGravity = themeSingleton.btnStackedGravity;
                this.itemsGravity = themeSingleton.itemsGravity;
                this.buttonsGravity = themeSingleton.buttonsGravity;
            }
        }

        public Builder title(@StringRes int i) {
            title(this.context.getText(i));
            return this;
        }

        public Builder title(@NonNull CharSequence charSequence) {
            this.title = charSequence;
            return this;
        }

        public Builder titleGravity(@NonNull GravityEnum gravityEnum) {
            this.titleGravity = gravityEnum;
            return this;
        }

        public Builder buttonRippleColor(@ColorInt int i) {
            this.buttonRippleColor = i;
            return this;
        }

        public Builder buttonRippleColorRes(@ColorRes int i) {
            return buttonRippleColor(ResourceUtils.getColor(this.context, i));
        }

        public Builder buttonRippleColorAttr(@AttrRes int i) {
            return buttonRippleColor(ResourceUtils.resolveColor(this.context, i));
        }

        public Builder titleColor(@ColorInt int i) {
            this.titleColor = i;
            this.titleColorSet = true;
            return this;
        }

        public Builder titleColorRes(@ColorRes int i) {
            return titleColor(ResourceUtils.getColor(this.context, i));
        }

        public Builder titleColorAttr(@AttrRes int i) {
            return titleColor(ResourceUtils.resolveColor(this.context, i));
        }

        public Builder icon(@NonNull Drawable drawable) {
            this.icon = drawable;
            return this;
        }

        public Builder iconRes(@DrawableRes int i) {
            this.icon = ResourcesCompat.getDrawable(this.context.getResources(), i, (Resources.Theme) null);
            return this;
        }

        public Builder iconAttr(@AttrRes int i) {
            this.icon = ResourceUtils.resolveDrawable(this.context, i);
            return this;
        }

        public Builder content(@StringRes int i) {
            content(this.context.getText(i));
            return this;
        }

        public Builder content(@NonNull CharSequence charSequence) {
            if (this.customView == null) {
                this.content = charSequence;
                return this;
            }
            throw new IllegalStateException("You cannot set content() when you're using a custom view.");
        }

        public Builder content(@StringRes int i, Object... objArr) {
            content((CharSequence) this.context.getString(i, objArr));
            return this;
        }

        public Builder contentColor(@ColorInt int i) {
            this.contentColor = i;
            this.contentColorSet = true;
            return this;
        }

        public Builder contentColorRes(@ColorRes int i) {
            contentColor(ResourceUtils.getColor(this.context, i));
            return this;
        }

        public Builder contentColorAttr(@AttrRes int i) {
            contentColor(ResourceUtils.resolveColor(this.context, i));
            return this;
        }

        public Builder contentGravity(@NonNull GravityEnum gravityEnum) {
            this.contentGravity = gravityEnum;
            return this;
        }

        public Builder contentLineSpacing(float f) {
            this.contentLineSpacingMultiplier = f;
            return this;
        }

        public Builder items(@ArrayRes int i) {
            CharSequence[] textArray = this.context.getResources().getTextArray(i);
            TBSimpleListItem[] tBSimpleListItemArr = new TBSimpleListItem[textArray.length];
            for (int i2 = 0; i2 < textArray.length; i2++) {
                tBSimpleListItemArr[i2] = new TBSimpleListItem();
                tBSimpleListItemArr[i2].setText(textArray[i2].toString());
            }
            return items(tBSimpleListItemArr);
        }

        public Builder items(@NonNull TBSimpleListItem... tBSimpleListItemArr) {
            if (this.customView == null) {
                this.items = tBSimpleListItemArr;
                return this;
            }
            throw new IllegalStateException("You cannot set items() when you're using a custom view.");
        }

        public Builder itemsCallback(@NonNull ListCallback listCallback2) {
            this.listCallback = listCallback2;
            this.listCallbackSingleChoice = null;
            this.listCallbackMultiChoice = null;
            return this;
        }

        public Builder itemsColor(@ColorInt int i) {
            this.itemColor = i;
            this.itemColorSet = true;
            return this;
        }

        @Deprecated
        public Builder itemColor(@ColorInt int i) {
            return itemsColor(i);
        }

        public Builder itemsColorRes(@ColorRes int i) {
            return itemsColor(ResourceUtils.getColor(this.context, i));
        }

        @Deprecated
        public Builder itemColorRes(@ColorRes int i) {
            return itemsColorRes(i);
        }

        public Builder itemsColorAttr(@AttrRes int i) {
            return itemsColor(ResourceUtils.resolveColor(this.context, i));
        }

        @Deprecated
        public Builder itemColorAttr(@AttrRes int i) {
            return itemsColorAttr(i);
        }

        public Builder itemsGravity(@NonNull GravityEnum gravityEnum) {
            this.itemsGravity = gravityEnum;
            return this;
        }

        public Builder itemsIds(@NonNull int[] iArr) {
            this.itemIds = iArr;
            return this;
        }

        public Builder itemsIds(@ArrayRes int i) {
            return itemsIds(this.context.getResources().getIntArray(i));
        }

        public Builder buttonsGravity(@NonNull GravityEnum gravityEnum) {
            this.buttonsGravity = gravityEnum;
            return this;
        }

        public Builder itemsCallbackSingleChoice(int i, @NonNull ListCallbackSingleChoice listCallbackSingleChoice2) {
            this.selectedIndex = i;
            this.listCallback = null;
            this.listCallbackSingleChoice = listCallbackSingleChoice2;
            this.listCallbackMultiChoice = null;
            return this;
        }

        public Builder alwaysCallSingleChoiceCallback() {
            this.alwaysCallSingleChoiceCallback = true;
            return this;
        }

        public Builder itemsCallbackMultiChoice(@Nullable Integer[] numArr, @NonNull ListCallbackMultiChoice listCallbackMultiChoice2) {
            this.selectedIndices = numArr;
            this.listCallback = null;
            this.listCallbackSingleChoice = null;
            this.listCallbackMultiChoice = listCallbackMultiChoice2;
            return this;
        }

        public Builder alwaysCallMultiChoiceCallback() {
            this.alwaysCallMultiChoiceCallback = true;
            return this;
        }

        public Builder positiveText(@StringRes int i) {
            if (i == 0) {
                return this;
            }
            positiveText(this.context.getText(i));
            return this;
        }

        public Builder positiveText(@NonNull CharSequence charSequence) {
            this.positiveText = charSequence;
            return this;
        }

        public Builder positiveType(@NonNull TBButtonType tBButtonType) {
            switch (tBButtonType) {
                case NORMAL:
                    this.positiveColor = ResourceUtils.getActionTextColorStateList(getContext(), R.color.uik_btnNormal);
                    break;
                case SECONDARY:
                    this.positiveColor = ResourceUtils.getActionTextColorStateList(getContext(), R.color.uik_btnSecondary);
                    break;
                case ALERT:
                    this.positiveColor = ResourceUtils.getActionTextColorStateList(getContext(), R.color.uik_btnAlert);
                    break;
                case DISABLED:
                    this.positiveColor = ResourceUtils.getActionTextColorStateList(getContext(), R.color.uik_btnDisabled);
                    break;
            }
            return this;
        }

        public Builder positiveColor(@ColorInt int i) {
            return positiveColor(ResourceUtils.getActionTextStateList(this.context, i));
        }

        public Builder positiveColorRes(@ColorRes int i) {
            return positiveColor(ResourceUtils.getActionTextColorStateList(this.context, i));
        }

        public Builder positiveColorAttr(@AttrRes int i) {
            return positiveColor(ResourceUtils.resolveActionTextColorStateList(this.context, i, (ColorStateList) null));
        }

        public Builder positiveColor(@NonNull ColorStateList colorStateList) {
            this.positiveColor = colorStateList;
            this.positiveColorSet = true;
            return this;
        }

        public Builder neutralText(@StringRes int i) {
            return i == 0 ? this : neutralText(this.context.getText(i));
        }

        public Builder neutralText(@NonNull CharSequence charSequence) {
            this.neutralText = charSequence;
            return this;
        }

        public Builder negativeColor(@ColorInt int i) {
            return negativeColor(ResourceUtils.getActionTextStateList(this.context, i));
        }

        public Builder negativeType(@NonNull TBButtonType tBButtonType) {
            switch (tBButtonType) {
                case NORMAL:
                    this.negativeColor = ResourceUtils.getActionTextColorStateList(getContext(), R.color.uik_btnNormal);
                    break;
                case SECONDARY:
                    this.negativeColor = ResourceUtils.getActionTextColorStateList(getContext(), R.color.uik_btnSecondary);
                    break;
                case ALERT:
                    this.negativeColor = ResourceUtils.getActionTextColorStateList(getContext(), R.color.uik_btnAlert);
                    break;
                case DISABLED:
                    this.negativeColor = ResourceUtils.getActionTextColorStateList(getContext(), R.color.uik_btnDisabled);
                    break;
            }
            return this;
        }

        public Builder negativeColorRes(@ColorRes int i) {
            return negativeColor(ResourceUtils.getActionTextColorStateList(this.context, i));
        }

        public Builder negativeColorAttr(@AttrRes int i) {
            return negativeColor(ResourceUtils.resolveActionTextColorStateList(this.context, i, (ColorStateList) null));
        }

        public Builder negativeColor(@NonNull ColorStateList colorStateList) {
            this.negativeColor = colorStateList;
            this.negativeColorSet = true;
            return this;
        }

        public Builder negativeText(@StringRes int i) {
            return i == 0 ? this : negativeText(this.context.getText(i));
        }

        public Builder negativeText(@NonNull CharSequence charSequence) {
            this.negativeText = charSequence;
            return this;
        }

        public Builder neutralColor(@ColorInt int i) {
            return neutralColor(ResourceUtils.getActionTextStateList(this.context, i));
        }

        public Builder neutralColorRes(@ColorRes int i) {
            return neutralColor(ResourceUtils.getActionTextColorStateList(this.context, i));
        }

        public Builder neutralColorAttr(@AttrRes int i) {
            return neutralColor(ResourceUtils.resolveActionTextColorStateList(this.context, i, (ColorStateList) null));
        }

        public Builder neutralColor(@NonNull ColorStateList colorStateList) {
            this.neutralColor = colorStateList;
            this.neutralColorSet = true;
            return this;
        }

        public Builder linkColor(@ColorInt int i) {
            return linkColor(ResourceUtils.getActionTextStateList(this.context, i));
        }

        public Builder linkColorRes(@ColorRes int i) {
            return linkColor(ResourceUtils.getActionTextColorStateList(this.context, i));
        }

        public Builder linkColorAttr(@AttrRes int i) {
            return linkColor(ResourceUtils.resolveActionTextColorStateList(this.context, i, (ColorStateList) null));
        }

        public Builder linkColor(@NonNull ColorStateList colorStateList) {
            this.linkColor = colorStateList;
            return this;
        }

        public Builder listSelector(@DrawableRes int i) {
            this.listSelector = i;
            return this;
        }

        public Builder btnSelectorStacked(@DrawableRes int i) {
            this.btnSelectorStacked = i;
            return this;
        }

        public Builder btnSelector(@DrawableRes int i) {
            this.btnSelectorPositive = i;
            this.btnSelectorNeutral = i;
            this.btnSelectorNegative = i;
            return this;
        }

        public Builder btnSelector(@DrawableRes int i, @NonNull DialogAction dialogAction) {
            switch (dialogAction) {
                case NEUTRAL:
                    this.btnSelectorNeutral = i;
                    break;
                case NEGATIVE:
                    this.btnSelectorNegative = i;
                    break;
                default:
                    this.btnSelectorPositive = i;
                    break;
            }
            return this;
        }

        public Builder btnStackedGravity(@NonNull GravityEnum gravityEnum) {
            this.btnStackedGravity = gravityEnum;
            return this;
        }

        public Builder customView(@LayoutRes int i, boolean z) {
            return customView(LayoutInflater.from(this.context).inflate(i, (ViewGroup) null), z);
        }

        public Builder customView(@NonNull View view, boolean z) {
            if (this.content != null) {
                throw new IllegalStateException("You cannot use customView() when you have content set.");
            } else if (this.items == null) {
                if (view.getParent() != null && (view.getParent() instanceof ViewGroup)) {
                    ((ViewGroup) view.getParent()).removeView(view);
                }
                this.customView = view;
                this.wrapCustomViewInScroll = z;
                return this;
            } else {
                throw new IllegalStateException("You cannot use customView() when you have items set.");
            }
        }

        public Builder cardDialog(boolean z) {
            this.cardDialog = z;
            return this;
        }

        public Builder widgetColor(@ColorInt int i) {
            this.widgetColor = i;
            this.widgetColorSet = true;
            return this;
        }

        public Builder widgetColorRes(@ColorRes int i) {
            return widgetColor(ResourceUtils.getColor(this.context, i));
        }

        public Builder widgetColorAttr(@AttrRes int i) {
            return widgetColorRes(ResourceUtils.resolveColor(this.context, i));
        }

        public Builder dividerColor(@ColorInt int i) {
            this.dividerColor = i;
            this.dividerColorSet = true;
            return this;
        }

        public Builder dividerColorRes(@ColorRes int i) {
            return dividerColor(ResourceUtils.getColor(this.context, i));
        }

        public Builder dividerColorAttr(@AttrRes int i) {
            return dividerColor(ResourceUtils.resolveColor(this.context, i));
        }

        public Builder backgroundColor(@ColorInt int i) {
            this.backgroundColor = i;
            return this;
        }

        public Builder backgroundColorRes(@ColorRes int i) {
            return backgroundColor(ResourceUtils.getColor(this.context, i));
        }

        public Builder backgroundColorAttr(@AttrRes int i) {
            return backgroundColor(ResourceUtils.resolveColor(this.context, i));
        }

        public Builder callback(@NonNull ButtonCallback buttonCallback) {
            this.callback = buttonCallback;
            return this;
        }

        public Builder onPositive(@NonNull SingleButtonCallback singleButtonCallback) {
            this.onPositiveCallback = singleButtonCallback;
            return this;
        }

        public Builder onNegative(@NonNull SingleButtonCallback singleButtonCallback) {
            this.onNegativeCallback = singleButtonCallback;
            return this;
        }

        public Builder onNeutral(@NonNull SingleButtonCallback singleButtonCallback) {
            this.onNeutralCallback = singleButtonCallback;
            return this;
        }

        public Builder onAny(@NonNull SingleButtonCallback singleButtonCallback) {
            this.onAnyCallback = singleButtonCallback;
            return this;
        }

        public Builder theme(@NonNull Theme theme2) {
            this.theme = theme2;
            return this;
        }

        public Builder cancelable(boolean z) {
            this.cancelable = z;
            return this;
        }

        public Builder autoDismiss(boolean z) {
            this.autoDismiss = z;
            return this;
        }

        public Builder adapter(@NonNull ListAdapter listAdapter, @Nullable ListCallback listCallback2) {
            if (this.customView == null) {
                this.adapter = listAdapter;
                this.listCallbackCustom = listCallback2;
                return this;
            }
            throw new IllegalStateException("You cannot set adapter() when you're using a custom view.");
        }

        public Builder limitIconToDefaultSize() {
            this.limitIconToDefaultSize = true;
            return this;
        }

        public Builder maxIconSize(int i) {
            this.maxIconSize = i;
            return this;
        }

        public Builder maxIconSizeRes(@DimenRes int i) {
            return maxIconSize((int) this.context.getResources().getDimension(i));
        }

        public Builder showListener(@NonNull DialogInterface.OnShowListener onShowListener) {
            this.showListener = onShowListener;
            return this;
        }

        public Builder dismissListener(@NonNull DialogInterface.OnDismissListener onDismissListener) {
            this.dismissListener = onDismissListener;
            return this;
        }

        public Builder cancelListener(@NonNull DialogInterface.OnCancelListener onCancelListener) {
            this.cancelListener = onCancelListener;
            return this;
        }

        public Builder keyListener(@NonNull DialogInterface.OnKeyListener onKeyListener) {
            this.keyListener = onKeyListener;
            return this;
        }

        public Builder forceStacking(boolean z) {
            this.forceStacking = z;
            return this;
        }

        @UiThread
        public TBMaterialDialog build() {
            this.mMaterialDialog = new TBMaterialDialog(this);
            if (this.cardDialog) {
                this.mMaterialDialog.getWindow().setBackgroundDrawable(this.context.getResources().getDrawable(17170445));
                this.mMaterialDialog.getWindow().setWindowAnimations(R.style.TBMD_CardAnimation);
            }
            return this.mMaterialDialog;
        }

        @UiThread
        public TBMaterialDialog show() {
            if (this.mMaterialDialog == null) {
                this.mMaterialDialog = build();
            }
            this.mMaterialDialog.show();
            return this.mMaterialDialog;
        }
    }

    @UiThread
    public void show() {
        try {
            super.show();
        } catch (WindowManager.BadTokenException unused) {
            throw new DialogException("Bad window token, you cannot show a dialog before an Activity is created or after it's hidden.");
        }
    }

    public final TBDialogButton getActionButton(@NonNull DialogAction dialogAction) {
        switch (dialogAction) {
            case NEUTRAL:
                return this.neutralButton;
            case NEGATIVE:
                return this.negativeButton;
            default:
                return this.positiveButton;
        }
    }

    public final View getView() {
        return this.view;
    }

    @Nullable
    public final ListView getListView() {
        return this.listView;
    }

    public final TextView getTitleView() {
        return this.title;
    }

    public ImageView getIconView() {
        return this.icon;
    }

    @Nullable
    public final TextView getContentView() {
        return this.content;
    }

    @Nullable
    public final View getCustomView() {
        return this.mBuilder.customView;
    }

    @UiThread
    public final void setActionButton(@NonNull DialogAction dialogAction, CharSequence charSequence) {
        int i = 0;
        switch (dialogAction) {
            case NEUTRAL:
                this.mBuilder.neutralText = charSequence;
                this.neutralButton.setText(charSequence);
                TBDialogButton tBDialogButton = this.neutralButton;
                if (charSequence == null) {
                    i = 8;
                }
                tBDialogButton.setVisibility(i);
                return;
            case NEGATIVE:
                this.mBuilder.negativeText = charSequence;
                this.negativeButton.setText(charSequence);
                TBDialogButton tBDialogButton2 = this.negativeButton;
                if (charSequence == null) {
                    i = 8;
                }
                tBDialogButton2.setVisibility(i);
                return;
            default:
                this.mBuilder.positiveText = charSequence;
                this.positiveButton.setText(charSequence);
                TBDialogButton tBDialogButton3 = this.positiveButton;
                if (charSequence == null) {
                    i = 8;
                }
                tBDialogButton3.setVisibility(i);
                return;
        }
    }

    public final void setActionButton(DialogAction dialogAction, @StringRes int i) {
        setActionButton(dialogAction, getContext().getText(i));
    }

    public final boolean hasActionButtons() {
        return numberOfActionButtons() > 0;
    }

    public final int numberOfActionButtons() {
        int i = (this.mBuilder.positiveText == null || this.positiveButton.getVisibility() != 0) ? 0 : 1;
        if (this.mBuilder.neutralText != null && this.neutralButton.getVisibility() == 0) {
            i++;
        }
        return (this.mBuilder.negativeText == null || this.negativeButton.getVisibility() != 0) ? i : i + 1;
    }

    @UiThread
    public final void setTitle(@NonNull CharSequence charSequence) {
        this.title.setText(charSequence);
    }

    @UiThread
    public final void setTitle(@StringRes int i) {
        setTitle((CharSequence) this.mBuilder.context.getString(i));
    }

    @UiThread
    public final void setTitle(@StringRes int i, @Nullable Object... objArr) {
        setTitle((CharSequence) this.mBuilder.context.getString(i, objArr));
    }

    @UiThread
    public void setIcon(@DrawableRes int i) {
        this.icon.setImageResource(i);
        this.icon.setVisibility(i != 0 ? 0 : 8);
    }

    @UiThread
    public void setIcon(Drawable drawable) {
        this.icon.setImageDrawable(drawable);
        this.icon.setVisibility(drawable != null ? 0 : 8);
    }

    @UiThread
    public void setIconAttribute(@AttrRes int i) {
        setIcon(ResourceUtils.resolveDrawable(this.mBuilder.context, i));
    }

    @UiThread
    public final void setContent(CharSequence charSequence) {
        this.content.setText(charSequence);
        this.content.setVisibility(TextUtils.isEmpty(charSequence) ? 8 : 0);
    }

    @UiThread
    public final void setContent(@StringRes int i) {
        setContent((CharSequence) this.mBuilder.context.getString(i));
    }

    @UiThread
    public final void setContent(@StringRes int i, @Nullable Object... objArr) {
        setContent((CharSequence) this.mBuilder.context.getString(i, objArr));
    }

    @Deprecated
    public void setMessage(CharSequence charSequence) {
        setContent(charSequence);
    }

    @UiThread
    public final void setItems(TBSimpleListItem... tBSimpleListItemArr) {
        if (this.mBuilder.adapter != null) {
            this.mBuilder.items = tBSimpleListItemArr;
            if (this.mBuilder.adapter instanceof TBDialogDefaultAdapter) {
                this.mBuilder.adapter = new TBDialogDefaultAdapter(this, ListType.getLayoutForType(this.listType));
                this.listView.setAdapter(this.mBuilder.adapter);
                return;
            }
            throw new IllegalStateException("When using a custom adapter, setItems() cannot be used. Set items through the adapter instead.");
        }
        throw new IllegalStateException("This TBMaterialDialog instance does not yet have an adapter set to it. You cannot use setItems().");
    }

    public final boolean isCancelled() {
        return !isShowing();
    }

    public int getSelectedIndex() {
        if (this.mBuilder.listCallbackSingleChoice != null) {
            return this.mBuilder.selectedIndex;
        }
        return -1;
    }

    @Nullable
    public Integer[] getSelectedIndices() {
        if (this.mBuilder.listCallbackMultiChoice != null) {
            return (Integer[]) this.selectedIndicesList.toArray(new Integer[this.selectedIndicesList.size()]);
        }
        return null;
    }

    @UiThread
    public void setSelectedIndex(int i) {
        this.mBuilder.selectedIndex = i;
        if (this.mBuilder.adapter == null || !(this.mBuilder.adapter instanceof TBDialogDefaultAdapter)) {
            throw new IllegalStateException("You can only use setSelectedIndex() with the default adapter implementation.");
        }
        ((TBDialogDefaultAdapter) this.mBuilder.adapter).notifyDataSetChanged();
    }

    @UiThread
    public void setSelectedIndices(@NonNull Integer[] numArr) {
        this.selectedIndicesList = new ArrayList(Arrays.asList(numArr));
        if (this.mBuilder.adapter == null || !(this.mBuilder.adapter instanceof TBDialogDefaultAdapter)) {
            throw new IllegalStateException("You can only use setSelectedIndices() with the default adapter implementation.");
        }
        ((TBDialogDefaultAdapter) this.mBuilder.adapter).notifyDataSetChanged();
    }

    public void clearSelectedIndices() {
        clearSelectedIndices(true);
    }

    public void clearSelectedIndices(boolean z) {
        if (this.listType == null || this.listType != ListType.MULTI) {
            throw new IllegalStateException("You can only use clearSelectedIndices() with multi choice list dialogs.");
        } else if (this.mBuilder.adapter == null || !(this.mBuilder.adapter instanceof TBDialogDefaultAdapter)) {
            throw new IllegalStateException("You can only use clearSelectedIndices() with the default adapter implementation.");
        } else {
            if (this.selectedIndicesList != null) {
                this.selectedIndicesList.clear();
            }
            ((TBDialogDefaultAdapter) this.mBuilder.adapter).notifyDataSetChanged();
            if (z && this.mBuilder.listCallbackMultiChoice != null) {
                sendMultichoiceCallback();
            }
        }
    }

    public void selectAllIndicies() {
        selectAllIndicies(true);
    }

    public void selectAllIndicies(boolean z) {
        if (this.listType == null || this.listType != ListType.MULTI) {
            throw new IllegalStateException("You can only use selectAllIndicies() with multi choice list dialogs.");
        } else if (this.mBuilder.adapter == null || !(this.mBuilder.adapter instanceof TBDialogDefaultAdapter)) {
            throw new IllegalStateException("You can only use selectAllIndicies() with the default adapter implementation.");
        } else {
            if (this.selectedIndicesList == null) {
                this.selectedIndicesList = new ArrayList();
            }
            for (int i = 0; i < this.mBuilder.adapter.getCount(); i++) {
                if (!this.selectedIndicesList.contains(Integer.valueOf(i))) {
                    this.selectedIndicesList.add(Integer.valueOf(i));
                }
            }
            ((TBDialogDefaultAdapter) this.mBuilder.adapter).notifyDataSetChanged();
            if (z && this.mBuilder.listCallbackMultiChoice != null) {
                sendMultichoiceCallback();
            }
        }
    }

    protected enum ListType {
        REGULAR,
        SINGLE,
        MULTI;

        public static int getLayoutForType(ListType listType) {
            switch (listType) {
                case REGULAR:
                    return R.layout.uik_md_listitem;
                case SINGLE:
                    return R.layout.uik_md_listitem_singlechoice;
                case MULTI:
                    return R.layout.uik_md_listitem_multichoice;
                default:
                    throw new IllegalArgumentException("Not a valid list type");
            }
        }
    }

    @Deprecated
    public static abstract class ButtonCallback {
        @Deprecated
        public void onAny(TBMaterialDialog tBMaterialDialog) {
        }

        @Deprecated
        public void onNegative(TBMaterialDialog tBMaterialDialog) {
        }

        @Deprecated
        public void onNeutral(TBMaterialDialog tBMaterialDialog) {
        }

        @Deprecated
        public void onPositive(TBMaterialDialog tBMaterialDialog) {
        }

        /* access modifiers changed from: protected */
        public final Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public final boolean equals(Object obj) {
            return super.equals(obj);
        }

        /* access modifiers changed from: protected */
        public final void finalize() throws Throwable {
            super.finalize();
        }

        public final int hashCode() {
            return super.hashCode();
        }

        public final String toString() {
            return super.toString();
        }
    }
}
