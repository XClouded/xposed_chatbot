package com.taobao.uikit.extend.component.unify.Dialog;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.annotation.UiThread;
import com.taobao.uikit.extend.R;
import com.taobao.uikit.extend.component.unify.Dialog.TBMaterialDialog;
import com.taobao.uikit.extend.utils.ResourceUtils;
import java.util.ArrayList;
import java.util.Arrays;

public class TBDialogInit {
    @StyleRes
    public static int getTheme(@NonNull TBMaterialDialog.Builder builder) {
        boolean resolveBoolean = ResourceUtils.resolveBoolean(builder.context, R.attr.uik_mdDarkTheme, builder.theme == Theme.DARK);
        builder.theme = resolveBoolean ? Theme.DARK : Theme.LIGHT;
        return resolveBoolean ? R.style.TBMD_Dark : R.style.TBMD_Light;
    }

    @LayoutRes
    public static int getInflateLayout(TBMaterialDialog.Builder builder) {
        if (builder.customView != null) {
            return R.layout.uik_md_dialog_custom;
        }
        if ((builder.items == null || builder.items.length <= 0) && builder.adapter == null) {
            return R.layout.uik_md_dialog_basic;
        }
        return R.layout.uik_md_dialog_list;
    }

    @UiThread
    public static void init(TBMaterialDialog tBMaterialDialog) {
        boolean z;
        TBMaterialDialog.Builder builder = tBMaterialDialog.mBuilder;
        tBMaterialDialog.setCancelable(builder.cancelable);
        tBMaterialDialog.setCanceledOnTouchOutside(builder.cancelable);
        if (builder.backgroundColor == 0) {
            builder.backgroundColor = ResourceUtils.resolveColor(builder.context, R.attr.uik_mdBackgroundColor);
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setCornerRadius(builder.context.getResources().getDimension(R.dimen.uik_mdBgCornerRadius));
            gradientDrawable.setColor(builder.backgroundColor);
            ResourceUtils.setBackgroundCompat(tBMaterialDialog.view, gradientDrawable);
            tBMaterialDialog.view.setBackgroundColor(ResourceUtils.getColor(builder.context, 17170445));
        }
        if (!builder.positiveColorSet) {
            builder.positiveColor = ResourceUtils.resolveActionTextColorStateList(builder.context, R.attr.uik_mdPositiveColor, builder.positiveColor);
        }
        if (!builder.neutralColorSet) {
            builder.neutralColor = ResourceUtils.resolveActionTextColorStateList(builder.context, R.attr.uik_mdNeutralColor, builder.neutralColor);
        }
        if (!builder.negativeColorSet) {
            builder.negativeColor = ResourceUtils.resolveActionTextColorStateList(builder.context, R.attr.uik_mdNegativeColor, builder.negativeColor);
        }
        if (!builder.widgetColorSet) {
            builder.widgetColor = ResourceUtils.resolveColor(builder.context, R.attr.uik_mdWidgetColor, builder.widgetColor);
        }
        if (!builder.titleColorSet) {
            builder.titleColor = ResourceUtils.resolveColor(builder.context, R.attr.uik_mdTitleColor, ResourceUtils.getColor(tBMaterialDialog.getContext(), R.color.uik_mdContentColor));
        }
        if (!builder.contentColorSet) {
            builder.contentColor = ResourceUtils.resolveColor(builder.context, R.attr.uik_mdContentColor, ResourceUtils.getColor(tBMaterialDialog.getContext(), R.color.uik_mdContentColor));
        }
        if (!builder.itemColorSet) {
            builder.itemColor = ResourceUtils.resolveColor(builder.context, R.attr.uik_mdItemColor, builder.contentColor);
        }
        tBMaterialDialog.title = (TextView) tBMaterialDialog.view.findViewById(R.id.uik_mdTitle);
        tBMaterialDialog.icon = (ImageView) tBMaterialDialog.view.findViewById(R.id.uik_mdIcon);
        tBMaterialDialog.titleFrame = tBMaterialDialog.view.findViewById(R.id.uik_mdTitleFrame);
        tBMaterialDialog.content = (TextView) tBMaterialDialog.view.findViewById(R.id.uik_mdContent);
        tBMaterialDialog.listView = (ListView) tBMaterialDialog.view.findViewById(R.id.uik_mdContentListView);
        tBMaterialDialog.positiveButton = (TBDialogButton) tBMaterialDialog.view.findViewById(R.id.uik_mdButtonDefaultPositive);
        tBMaterialDialog.neutralButton = (TBDialogButton) tBMaterialDialog.view.findViewById(R.id.uik_mdButtonDefaultNeutral);
        tBMaterialDialog.negativeButton = (TBDialogButton) tBMaterialDialog.view.findViewById(R.id.uik_mdButtonDefaultNegative);
        tBMaterialDialog.closeButton = (ImageView) tBMaterialDialog.view.findViewById(R.id.uik_mdButtonClose);
        if (tBMaterialDialog.positiveButton != null) {
            tBMaterialDialog.positiveButton.setVisibility(builder.positiveText != null ? 0 : 8);
        }
        if (tBMaterialDialog.neutralButton != null) {
            tBMaterialDialog.neutralButton.setVisibility(builder.neutralText != null ? 0 : 8);
        }
        if (tBMaterialDialog.negativeButton != null) {
            tBMaterialDialog.negativeButton.setVisibility(builder.negativeText != null ? 0 : 8);
        }
        if (tBMaterialDialog.icon != null) {
            if (builder.icon != null) {
                tBMaterialDialog.icon.setVisibility(0);
                tBMaterialDialog.icon.setImageDrawable(builder.icon);
            } else {
                Drawable resolveDrawable = ResourceUtils.resolveDrawable(builder.context, R.attr.uik_mdIcon);
                if (resolveDrawable != null) {
                    tBMaterialDialog.icon.setVisibility(0);
                    tBMaterialDialog.icon.setImageDrawable(resolveDrawable);
                } else {
                    tBMaterialDialog.icon.setVisibility(8);
                }
            }
            int i = builder.maxIconSize;
            if (i == -1) {
                i = ResourceUtils.resolveDimension(builder.context, R.attr.uik_mdIconMaxSize);
            }
            if (builder.limitIconToDefaultSize || ResourceUtils.resolveBoolean(builder.context, R.attr.uik_mdIconLimitIconToDefaultSize)) {
                i = builder.context.getResources().getDimensionPixelSize(R.dimen.uik_mdIconMaxSize);
            }
            if (i > -1) {
                tBMaterialDialog.icon.setAdjustViewBounds(true);
                tBMaterialDialog.icon.setMaxHeight(i);
                tBMaterialDialog.icon.setMaxWidth(i);
                tBMaterialDialog.icon.requestLayout();
            }
        }
        if (!builder.dividerColorSet) {
            builder.dividerColor = ResourceUtils.resolveColor(builder.context, R.attr.uik_mdDividerColor, ResourceUtils.resolveColor(tBMaterialDialog.getContext(), R.attr.uik_mdDivider));
        }
        tBMaterialDialog.view.setDividerColor(builder.dividerColor);
        if (tBMaterialDialog.title != null) {
            tBMaterialDialog.title.setTextColor(builder.titleColor);
            tBMaterialDialog.title.setGravity(builder.titleGravity.getGravityInt());
            if (Build.VERSION.SDK_INT >= 17) {
                tBMaterialDialog.title.setTextAlignment(builder.titleGravity.getTextAlignment());
            }
            if (builder.title == null) {
                tBMaterialDialog.titleFrame.setVisibility(8);
            } else {
                tBMaterialDialog.title.setText(builder.title);
                tBMaterialDialog.titleFrame.setVisibility(0);
            }
        }
        if (tBMaterialDialog.content != null) {
            tBMaterialDialog.content.setMovementMethod(new LinkMovementMethod());
            tBMaterialDialog.content.setLineSpacing(0.0f, builder.contentLineSpacingMultiplier);
            if (builder.linkColor == null) {
                tBMaterialDialog.content.setLinkTextColor(ResourceUtils.resolveColor(tBMaterialDialog.getContext(), 16842806));
            } else {
                tBMaterialDialog.content.setLinkTextColor(builder.linkColor);
            }
            tBMaterialDialog.content.setTextColor(builder.contentColor);
            tBMaterialDialog.content.setGravity(builder.contentGravity.getGravityInt());
            if (Build.VERSION.SDK_INT >= 17) {
                tBMaterialDialog.content.setTextAlignment(builder.contentGravity.getTextAlignment());
            }
            if (builder.content != null) {
                tBMaterialDialog.content.setText(builder.content);
                tBMaterialDialog.content.setVisibility(0);
            } else {
                tBMaterialDialog.content.setVisibility(8);
            }
        }
        tBMaterialDialog.view.setButtonGravity(builder.buttonsGravity);
        tBMaterialDialog.view.setButtonStackedGravity(builder.btnStackedGravity);
        tBMaterialDialog.view.setForceStack(builder.forceStacking);
        if (Build.VERSION.SDK_INT >= 14) {
            z = ResourceUtils.resolveBoolean(builder.context, 16843660, true);
            if (z) {
                z = ResourceUtils.resolveBoolean(builder.context, 16843660, true);
            }
        } else {
            z = ResourceUtils.resolveBoolean(builder.context, 16843660, true);
        }
        TBDialogButton tBDialogButton = tBMaterialDialog.positiveButton;
        if (tBDialogButton != null) {
            tBDialogButton.setAllCapsCompat(z);
            tBDialogButton.setText(builder.positiveText);
            tBDialogButton.setTextColor(builder.positiveColor);
            tBMaterialDialog.positiveButton.setStackedSelector(tBMaterialDialog.getButtonSelector(DialogAction.POSITIVE, true));
            tBMaterialDialog.positiveButton.setDefaultSelector(tBMaterialDialog.getButtonSelector(DialogAction.POSITIVE, false));
            tBMaterialDialog.positiveButton.setTag(DialogAction.POSITIVE);
            tBMaterialDialog.positiveButton.setOnClickListener(tBMaterialDialog);
            tBMaterialDialog.positiveButton.setVisibility(0);
        }
        TBDialogButton tBDialogButton2 = tBMaterialDialog.negativeButton;
        if (tBDialogButton2 != null) {
            tBDialogButton2.setAllCapsCompat(z);
            tBDialogButton2.setText(builder.negativeText);
            tBDialogButton2.setTextColor(builder.negativeColor);
            tBMaterialDialog.negativeButton.setStackedSelector(tBMaterialDialog.getButtonSelector(DialogAction.NEGATIVE, true));
            tBMaterialDialog.negativeButton.setDefaultSelector(tBMaterialDialog.getButtonSelector(DialogAction.NEGATIVE, false));
            tBMaterialDialog.negativeButton.setTag(DialogAction.NEGATIVE);
            tBMaterialDialog.negativeButton.setOnClickListener(tBMaterialDialog);
            tBMaterialDialog.negativeButton.setVisibility(0);
        }
        TBDialogButton tBDialogButton3 = tBMaterialDialog.neutralButton;
        if (tBDialogButton3 != null) {
            tBDialogButton3.setAllCapsCompat(z);
            tBDialogButton3.setText(builder.neutralText);
            tBDialogButton3.setTextColor(builder.neutralColor);
            tBMaterialDialog.neutralButton.setStackedSelector(tBMaterialDialog.getButtonSelector(DialogAction.NEUTRAL, true));
            tBMaterialDialog.neutralButton.setDefaultSelector(tBMaterialDialog.getButtonSelector(DialogAction.NEUTRAL, false));
            tBMaterialDialog.neutralButton.setTag(DialogAction.NEUTRAL);
            tBMaterialDialog.neutralButton.setOnClickListener(tBMaterialDialog);
            tBMaterialDialog.neutralButton.setVisibility(0);
        }
        ImageView imageView = tBMaterialDialog.closeButton;
        if (imageView != null) {
            imageView.setTag(DialogAction.CLOSE);
            imageView.setOnClickListener(tBMaterialDialog);
        }
        if (builder.listCallbackMultiChoice != null) {
            tBMaterialDialog.selectedIndicesList = new ArrayList();
        }
        if (tBMaterialDialog.listView != null && ((builder.items != null && builder.items.length > 0) || builder.adapter != null)) {
            tBMaterialDialog.listView.setSelector(tBMaterialDialog.getListSelector());
            if (builder.adapter == null) {
                if (builder.listCallbackSingleChoice != null) {
                    tBMaterialDialog.listType = TBMaterialDialog.ListType.SINGLE;
                } else if (builder.listCallbackMultiChoice != null) {
                    tBMaterialDialog.listType = TBMaterialDialog.ListType.MULTI;
                    if (builder.selectedIndices != null) {
                        tBMaterialDialog.selectedIndicesList = new ArrayList(Arrays.asList(builder.selectedIndices));
                        builder.selectedIndices = null;
                    }
                } else {
                    tBMaterialDialog.listType = TBMaterialDialog.ListType.REGULAR;
                }
                builder.adapter = new TBDialogDefaultAdapter(tBMaterialDialog, TBMaterialDialog.ListType.getLayoutForType(tBMaterialDialog.listType));
            } else if (builder.adapter instanceof TBDialogAdapter) {
                ((TBDialogAdapter) builder.adapter).setDialog(tBMaterialDialog);
            }
        }
        tBMaterialDialog.view.mCardDialog = builder.cardDialog;
        if (builder.customView != null) {
            ((TBDialogRootLayout) tBMaterialDialog.view.findViewById(R.id.uik_mdRoot)).noTitleNoPadding();
            FrameLayout frameLayout = (FrameLayout) tBMaterialDialog.view.findViewById(R.id.uik_mdCustomViewFrame);
            tBMaterialDialog.customViewFrame = frameLayout;
            View view = builder.customView;
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
            if (builder.wrapCustomViewInScroll) {
                Resources resources = tBMaterialDialog.getContext().getResources();
                int dimensionPixelSize = resources.getDimensionPixelSize(R.dimen.uik_mdDialogFrameMargin);
                ScrollView scrollView = new ScrollView(tBMaterialDialog.getContext());
                int dimensionPixelSize2 = resources.getDimensionPixelSize(R.dimen.uik_mdContentPaddingTop);
                int dimensionPixelSize3 = resources.getDimensionPixelSize(R.dimen.uik_mdContentPaddingBottom);
                scrollView.setClipToPadding(false);
                if (view instanceof EditText) {
                    scrollView.setPadding(dimensionPixelSize, dimensionPixelSize2, dimensionPixelSize, dimensionPixelSize3);
                } else {
                    scrollView.setPadding(0, dimensionPixelSize2, 0, dimensionPixelSize3);
                    view.setPadding(dimensionPixelSize, 0, dimensionPixelSize, 0);
                }
                scrollView.addView(view, new FrameLayout.LayoutParams(-1, -2));
                view = scrollView;
            }
            frameLayout.addView(view, new ViewGroup.LayoutParams(-1, -2));
        }
        if (builder.showListener != null) {
            tBMaterialDialog.setOnShowListener(builder.showListener);
        }
        if (builder.cancelListener != null) {
            tBMaterialDialog.setOnCancelListener(builder.cancelListener);
        }
        if (builder.dismissListener != null) {
            tBMaterialDialog.setOnDismissListener(builder.dismissListener);
        }
        if (builder.keyListener != null) {
            tBMaterialDialog.setOnKeyListener(builder.keyListener);
        }
        tBMaterialDialog.setOnShowListenerInternal();
        tBMaterialDialog.invalidateList();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -2);
        layoutParams.gravity = 17;
        if (builder.cardDialog) {
            tBMaterialDialog.setViewInternal(tBMaterialDialog.view, layoutParams);
        } else {
            tBMaterialDialog.setViewInternal(tBMaterialDialog.view);
        }
        tBMaterialDialog.checkIfListInitScroll();
    }
}
