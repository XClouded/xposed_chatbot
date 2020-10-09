package com.taobao.android;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import com.taobao.phenix.bitmap.BitmapProcessor;
import com.taobao.phenix.intf.PhenixCreator;
import com.taobao.phenix.intf.event.IRetryHandlerOnFailure;

public class PhenixCreatorAdapter implements AliImageCreatorInterface {
    private final PhenixCreator mPhenixCreator;

    public PhenixCreatorAdapter(PhenixCreator phenixCreator) {
        this.mPhenixCreator = phenixCreator;
    }

    public String url() {
        return this.mPhenixCreator.url();
    }

    public AliImageCreatorInterface placeholder(int i) {
        this.mPhenixCreator.placeholder(i);
        return this;
    }

    public AliImageCreatorInterface placeholder(Drawable drawable) {
        this.mPhenixCreator.placeholder(drawable);
        return this;
    }

    public AliImageCreatorInterface error(int i) {
        this.mPhenixCreator.error(i);
        return this;
    }

    public AliImageCreatorInterface error(Drawable drawable) {
        this.mPhenixCreator.error(drawable);
        return this;
    }

    public AliImageCreatorInterface onlyCache() {
        this.mPhenixCreator.onlyCache();
        return this;
    }

    public static int[] getScreenSize(Context context) {
        return PhenixCreator.getScreenSize(context);
    }

    public AliImageCreatorInterface memOnly(boolean z) {
        this.mPhenixCreator.memOnly(z);
        return this;
    }

    public AliImageCreatorInterface skipCache() {
        this.mPhenixCreator.skipCache();
        return this;
    }

    public AliImageCreatorInterface preloadWithSmall(boolean z) {
        this.mPhenixCreator.preloadWithSmall(z);
        return this;
    }

    public AliImageCreatorInterface scaleFromLarge(boolean z) {
        this.mPhenixCreator.scaleFromLarge(z);
        return this;
    }

    public AliImageCreatorInterface memoryCachePriority(int i) {
        this.mPhenixCreator.memoryCachePriority(i);
        return this;
    }

    public AliImageCreatorInterface diskCachePriority(int i) {
        this.mPhenixCreator.diskCachePriority(i);
        return this;
    }

    public AliImageCreatorInterface schedulePriority(int i) {
        this.mPhenixCreator.schedulePriority(i);
        return this;
    }

    @Deprecated
    public AliImageCreatorInterface notSharedDrawable(boolean z) {
        this.mPhenixCreator.notSharedDrawable(z);
        return this;
    }

    public AliImageCreatorInterface releasableDrawable(boolean z) {
        this.mPhenixCreator.releasableDrawable(z);
        return this;
    }

    public AliImageCreatorInterface asThumbnail(int i, boolean z) {
        this.mPhenixCreator.asThumbnail(i, z);
        return this;
    }

    public AliImageCreatorInterface bitmapProcessors(BitmapProcessor... bitmapProcessorArr) {
        this.mPhenixCreator.bitmapProcessors(bitmapProcessorArr);
        return this;
    }

    public AliImageCreatorInterface forceAnimationToBeStatic(boolean z) {
        this.mPhenixCreator.forceAnimationToBeStatic(z);
        return this;
    }

    public AliImageCreatorInterface secondary(String str) {
        this.mPhenixCreator.secondary(str);
        return this;
    }

    @Deprecated
    public AliImageCreatorInterface setCacheKey4PlaceHolder(String str) {
        this.mPhenixCreator.setCacheKey4PlaceHolder(str);
        return this;
    }

    public AliImageCreatorInterface addLoaderExtra(String str, String str2) {
        this.mPhenixCreator.addLoaderExtra(str, str2);
        return this;
    }

    public AliImageCreatorInterface failListener(AliImageListener<AliImageFailEvent> aliImageListener) {
        this.mPhenixCreator.failListener(new AliImageFailListenerAdapter(aliImageListener));
        return this;
    }

    public AliImageCreatorInterface succListener(AliImageListener<AliImageSuccEvent> aliImageListener) {
        this.mPhenixCreator.succListener(new AliImageSuccListenerAdapter(aliImageListener));
        return this;
    }

    public AliImageCreatorInterface retryHandler(IRetryHandlerOnFailure iRetryHandlerOnFailure) {
        this.mPhenixCreator.retryHandler(iRetryHandlerOnFailure);
        return this;
    }

    public AliImageTicketInterface into(ImageView imageView) {
        return new PhenixTicketAdapter(this.mPhenixCreator.into(imageView));
    }

    public AliImageTicketInterface into(ImageView imageView, float f) {
        return new PhenixTicketAdapter(this.mPhenixCreator.into(imageView, f));
    }

    public AliImageTicketInterface into(ImageView imageView, int i, int i2) {
        return new PhenixTicketAdapter(this.mPhenixCreator.into(imageView, i, i2));
    }

    public AliImageCreatorInterface limitSize(View view) {
        this.mPhenixCreator.limitSize(view);
        return this;
    }

    public AliImageCreatorInterface limitSize(View view, int i, int i2) {
        this.mPhenixCreator.limitSize(view, i, i2);
        return this;
    }

    public AliImageTicketInterface fetch() {
        return new PhenixTicketAdapter(this.mPhenixCreator.fetch());
    }

    public IRetryHandlerOnFailure getRetryHandlerOnFailure() {
        return this.mPhenixCreator.getRetryHandlerOnFailure();
    }

    @Deprecated
    public AliImageCreatorInterface setImageStrategyInfo(Object obj) {
        this.mPhenixCreator.setImageStrategyInfo(obj);
        return this;
    }
}
