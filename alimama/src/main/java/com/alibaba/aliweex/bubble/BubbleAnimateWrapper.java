package com.alibaba.aliweex.bubble;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import androidx.annotation.NonNull;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import com.alibaba.aliweex.bubble.BubbleEventCenter;
import com.alibaba.aliweex.bubble.SpringSet;
import com.taobao.uikit.extend.component.unify.Toast.TBToast;
import com.taobao.uikit.feature.features.FeatureFactory;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.utils.WXViewUtils;
import java.util.Random;

public class BubbleAnimateWrapper implements Comparable<BubbleAnimateWrapper> {
    private static final String TAG = "BubbleAnimateWrapper";
    public static final int sDirectionLeft = 256;
    public static final int sDirectionRight = 512;
    private static final float[] sFloatDistanceCandidates = {5.0f, 6.0f, 7.0f};
    private static final long[] sFloatDurationCandidates = {2000, 2500, TBToast.Duration.MEDIUM};
    private static final float sMoveDamping = 200.0f;
    private boolean hasBeenShowed = false;
    private float mBounceValue = -1.0f;
    private SpringSet.ISpringSetListener mEdgeLeftBounceListener;
    private SpringSet.ISpringSetListener mEdgeRightBounceListener;
    private SpringSet.ISpringSetListener mFastMoveEndListener;
    private Animation mFloatingAnim;
    private SpringSet mLastBounce;
    private SpringSet mLastGravityAnimation;
    /* access modifiers changed from: private */
    public SpringSet mLastGravityBackAnimation;
    private SpringSet mLastMoveSpring;
    private SpringSet.ISpringSetListener mMoveLeftEndListener;
    private SpringSet.ISpringSetListener mMoveRightEndListener;
    private BubblePosition mPosition;
    private Random mRandom = new Random();
    private SpringSet.ISpringSetListener mScaleListener;
    private SpringSet.ISpringSetListener mScrollLeftEndListener;
    private SpringSet.ISpringSetListener mScrollRightEndListener;
    private View mView;
    private int mViewIndex;

    BubbleAnimateWrapper(@NonNull View view, int i) {
        this.mView = view;
        this.mFastMoveEndListener = new SpringSet.ISpringSetListener() {
            public void onSpringStart(SpringSet springSet) {
            }

            public void onSpringEnd(SpringSet springSet) {
                BubbleAnimateWrapper.this.mLastGravityBackAnimation.start();
            }
        };
        this.mMoveLeftEndListener = new SpringSet.ISpringSetListener() {
            public void onSpringStart(SpringSet springSet) {
                BubbleEventCenter.getEventCenter().fireAnimationStart(BubbleEventCenter.AnimationType.MoveLeft, BubbleAnimateWrapper.this);
            }

            public void onSpringEnd(SpringSet springSet) {
                springSet.removeSpringSetListener(this);
                BubbleEventCenter.getEventCenter().fireAnimationEnd(BubbleEventCenter.AnimationType.MoveLeft, BubbleAnimateWrapper.this);
            }
        };
        this.mMoveRightEndListener = new SpringSet.ISpringSetListener() {
            public void onSpringStart(SpringSet springSet) {
                BubbleEventCenter.getEventCenter().fireAnimationStart(BubbleEventCenter.AnimationType.MoveRight, BubbleAnimateWrapper.this);
            }

            public void onSpringEnd(SpringSet springSet) {
                springSet.removeSpringSetListener(this);
                BubbleEventCenter.getEventCenter().fireAnimationEnd(BubbleEventCenter.AnimationType.MoveRight, BubbleAnimateWrapper.this);
            }
        };
        this.mScrollLeftEndListener = new SpringSet.ISpringSetListener() {
            public void onSpringStart(SpringSet springSet) {
                BubbleEventCenter.getEventCenter().fireAnimationStart(BubbleEventCenter.AnimationType.ScrollLeft, BubbleAnimateWrapper.this);
            }

            public void onSpringEnd(SpringSet springSet) {
                springSet.removeSpringSetListener(this);
                BubbleEventCenter.getEventCenter().fireAnimationEnd(BubbleEventCenter.AnimationType.ScrollLeft, BubbleAnimateWrapper.this);
            }
        };
        this.mScrollRightEndListener = new SpringSet.ISpringSetListener() {
            public void onSpringStart(SpringSet springSet) {
                BubbleEventCenter.getEventCenter().fireAnimationStart(BubbleEventCenter.AnimationType.ScrollRight, BubbleAnimateWrapper.this);
            }

            public void onSpringEnd(SpringSet springSet) {
                springSet.removeSpringSetListener(this);
                BubbleEventCenter.getEventCenter().fireAnimationEnd(BubbleEventCenter.AnimationType.ScrollRight, BubbleAnimateWrapper.this);
            }
        };
        this.mEdgeLeftBounceListener = new SpringSet.ISpringSetListener() {
            public void onSpringStart(SpringSet springSet) {
                BubbleEventCenter.getEventCenter().fireAnimationStart(BubbleEventCenter.AnimationType.EdgeBounceLeft, BubbleAnimateWrapper.this);
            }

            public void onSpringEnd(SpringSet springSet) {
                springSet.removeSpringSetListener(this);
                BubbleEventCenter.getEventCenter().fireAnimationEnd(BubbleEventCenter.AnimationType.EdgeBounceLeft, BubbleAnimateWrapper.this);
            }
        };
        this.mEdgeRightBounceListener = new SpringSet.ISpringSetListener() {
            public void onSpringStart(SpringSet springSet) {
                BubbleEventCenter.getEventCenter().fireAnimationStart(BubbleEventCenter.AnimationType.EdgeBounceRight, BubbleAnimateWrapper.this);
            }

            public void onSpringEnd(SpringSet springSet) {
                springSet.removeSpringSetListener(this);
                BubbleEventCenter.getEventCenter().fireAnimationEnd(BubbleEventCenter.AnimationType.EdgeBounceRight, BubbleAnimateWrapper.this);
            }
        };
        this.mScaleListener = new SpringSet.ISpringSetListener() {
            public void onSpringStart(SpringSet springSet) {
                BubbleEventCenter.getEventCenter().fireAnimationStart(BubbleEventCenter.AnimationType.ReplaceScale, BubbleAnimateWrapper.this);
            }

            public void onSpringEnd(SpringSet springSet) {
                springSet.removeSpringSetListener(this);
                BubbleEventCenter.getEventCenter().fireAnimationEnd(BubbleEventCenter.AnimationType.ReplaceScale, BubbleAnimateWrapper.this);
            }
        };
        this.mViewIndex = i;
        this.mFloatingAnim = new TranslateAnimation(0.0f, 0.0f, this.mView.getTranslationY(), sFloatDistanceCandidates[this.mRandom.nextInt(sFloatDistanceCandidates.length)] * this.mView.getContext().getResources().getDisplayMetrics().density);
        this.mFloatingAnim.setDuration(sFloatDurationCandidates[this.mRandom.nextInt(sFloatDurationCandidates.length)]);
        this.mFloatingAnim.setInterpolator(new LinearInterpolator());
        this.mFloatingAnim.setRepeatMode(2);
        this.mFloatingAnim.setRepeatCount(-1);
    }

    /* access modifiers changed from: package-private */
    public BubblePosition getPosition() {
        return this.mPosition;
    }

    /* access modifiers changed from: package-private */
    public void setBubblePosition(BubblePosition bubblePosition) {
        this.mPosition = bubblePosition;
    }

    public View getCurrentView() {
        return this.mView;
    }

    public void resetSpringAnimation(float f) {
        if (this.mLastMoveSpring != null && this.mLastMoveSpring.isRunning()) {
            if (this.mLastMoveSpring.removeSpringSetListener(this.mMoveLeftEndListener)) {
                BubbleEventCenter.getEventCenter().fireAnimationEnd(BubbleEventCenter.AnimationType.MoveLeft, this);
            } else if (this.mLastMoveSpring.removeSpringSetListener(this.mMoveRightEndListener)) {
                BubbleEventCenter.getEventCenter().fireAnimationEnd(BubbleEventCenter.AnimationType.MoveRight, this);
            }
            this.mLastMoveSpring.fastMove();
        }
        if (this.mLastGravityAnimation != null && this.mLastGravityAnimation.isRunning()) {
            this.mLastGravityAnimation.removeSpringSetListener(this.mFastMoveEndListener);
            this.mLastGravityAnimation.fastMove();
        }
        SpringSet springSet = new SpringSet();
        springSet.playTogether(SpringUtils.createSpring(this.mView, DynamicAnimation.SCALE_X, this.mPosition.width / ((float) this.mView.getWidth()), f, 0.5f), SpringUtils.createSpring(this.mView, DynamicAnimation.SCALE_Y, this.mPosition.height / ((float) this.mView.getHeight()), f, 0.5f), SpringUtils.createSpring(this.mView, DynamicAnimation.X, this.mPosition.x, f, 0.5f), SpringUtils.createSpring(this.mView, DynamicAnimation.Y, this.mPosition.y, f, 0.5f));
        springSet.start();
    }

    public void move(int i, boolean z, boolean z2, boolean z3) {
        int i2 = i;
        if (this.mView != null && this.mPosition != null) {
            if (this.mLastMoveSpring != null && this.mLastMoveSpring.isRunning()) {
                if (this.mLastMoveSpring.removeSpringSetListener(this.mMoveLeftEndListener)) {
                    BubbleEventCenter.getEventCenter().fireAnimationEnd(BubbleEventCenter.AnimationType.MoveLeft, this);
                } else if (this.mLastMoveSpring.removeSpringSetListener(this.mMoveRightEndListener)) {
                    BubbleEventCenter.getEventCenter().fireAnimationEnd(BubbleEventCenter.AnimationType.MoveRight, this);
                } else if (this.mLastMoveSpring.removeSpringSetListener(this.mScrollLeftEndListener)) {
                    BubbleEventCenter.getEventCenter().fireAnimationEnd(BubbleEventCenter.AnimationType.ScrollLeft, this);
                } else if (this.mLastMoveSpring.removeSpringSetListener(this.mScrollRightEndListener)) {
                    BubbleEventCenter.getEventCenter().fireAnimationEnd(BubbleEventCenter.AnimationType.ScrollRight, this);
                }
                this.mLastMoveSpring.fastMove();
            }
            float f = 1.0f;
            if (i2 == 256) {
                if (this.mPosition.mLeft != null) {
                    SpringSet springSet = new SpringSet();
                    if (!z2 || this.hasBeenShowed) {
                        springSet.playTogether(SpringUtils.createSpring(this.mView, DynamicAnimation.SCALE_X, this.mPosition.mLeft.width / ((float) this.mView.getWidth()), 200.0f, 1.0f), SpringUtils.createSpring(this.mView, DynamicAnimation.SCALE_Y, this.mPosition.mLeft.height / ((float) this.mView.getHeight()), 200.0f, 1.0f), SpringUtils.createSpring(this.mView, DynamicAnimation.X, this.mPosition.mLeft.x + ((this.mPosition.mLeft.width - ((float) this.mView.getWidth())) / 2.0f), 200.0f, 1.0f), SpringUtils.createSpring(this.mView, DynamicAnimation.Y, this.mPosition.mLeft.y + ((this.mPosition.mLeft.height - ((float) this.mView.getHeight())) / 2.0f), 200.0f, 1.0f));
                    } else {
                        BubblePosition bubblePosition = this.mPosition.mLeft;
                        float width = this.mPosition != null ? bubblePosition.width / ((float) this.mView.getWidth()) : 1.0f;
                        if (this.mPosition != null) {
                            f = bubblePosition.height / ((float) this.mView.getHeight());
                        }
                        this.mView.setX(bubblePosition.x + ((bubblePosition.width - ((float) this.mView.getWidth())) / 2.0f));
                        this.mView.setY(bubblePosition.y + ((bubblePosition.height - ((float) this.mView.getHeight())) / 2.0f));
                        SpringAnimation createSpring = SpringUtils.createSpring(this.mView, DynamicAnimation.SCALE_X, width, 50.0f, 0.5f);
                        createSpring.setStartValue(0.0f);
                        SpringAnimation createSpring2 = SpringUtils.createSpring(this.mView, DynamicAnimation.SCALE_Y, f, 50.0f, 0.5f);
                        createSpring2.setStartValue(0.0f);
                        springSet.playTogether(createSpring, createSpring2);
                    }
                    if (z) {
                        if (z3) {
                            springSet.addSpringSetListener(this.mScrollLeftEndListener);
                        } else {
                            springSet.addSpringSetListener(this.mMoveLeftEndListener);
                        }
                    }
                    springSet.start();
                    this.mLastMoveSpring = springSet;
                    if (!(this.mPosition == null || this.mPosition.mLeft == null)) {
                        this.mPosition = this.mPosition.mLeft;
                    }
                }
            } else if (i2 == 512 && this.mPosition.mRight != null) {
                SpringSet springSet2 = new SpringSet();
                springSet2.playTogether(SpringUtils.createSpring(this.mView, DynamicAnimation.SCALE_X, this.mPosition.mRight.width / ((float) this.mView.getWidth()), 200.0f, 1.0f), SpringUtils.createSpring(this.mView, DynamicAnimation.SCALE_Y, this.mPosition.mRight.height / ((float) this.mView.getHeight()), 200.0f, 1.0f), SpringUtils.createSpring(this.mView, DynamicAnimation.X, this.mPosition.mRight.x + ((this.mPosition.mRight.width - ((float) this.mView.getWidth())) / 2.0f), 200.0f, 1.0f), SpringUtils.createSpring(this.mView, DynamicAnimation.Y, this.mPosition.mRight.y + ((this.mPosition.mRight.height - ((float) this.mView.getHeight())) / 2.0f), 200.0f, 1.0f));
                if (z) {
                    if (z3) {
                        springSet2.addSpringSetListener(this.mScrollRightEndListener);
                    } else {
                        springSet2.addSpringSetListener(this.mMoveRightEndListener);
                    }
                }
                springSet2.start();
                this.mLastMoveSpring = springSet2;
                if (!(this.mPosition == null || this.mPosition.mRight == null)) {
                    this.mPosition = this.mPosition.mRight;
                }
            }
            this.hasBeenShowed = true;
        }
    }

    /* access modifiers changed from: package-private */
    public int getViewIndex() {
        return this.mViewIndex;
    }

    /* access modifiers changed from: package-private */
    public void gravityMove(BubblePosition bubblePosition) {
        if (bubblePosition != null && this.mPosition != null && this.mView != null) {
            float realPxByWidth = WXViewUtils.getRealPxByWidth(16.0f, FeatureFactory.PRIORITY_ABOVE_NORMAL);
            float f = this.mPosition.width;
            float f2 = this.mPosition.x;
            float f3 = this.mPosition.y;
            float f4 = bubblePosition.x;
            float f5 = bubblePosition.y;
            float f6 = f2 - f4;
            float f7 = f3 - f5;
            float sqrt = (float) Math.sqrt((double) ((f6 * f6) + (f7 * f7)));
            float f8 = realPxByWidth * f * f;
            float f9 = sqrt * sqrt * sqrt;
            float f10 = ((f4 - f2) * f8) / f9;
            float f11 = (f8 * (f5 - f3)) / f9;
            SpringSet springSet = new SpringSet();
            float translationX = this.mView.getTranslationX();
            float translationY = this.mView.getTranslationY();
            springSet.playTogether(SpringUtils.createSpring(this.mView, DynamicAnimation.TRANSLATION_X, f10 + translationX, 200.0f, 0.75f), SpringUtils.createSpring(this.mView, DynamicAnimation.TRANSLATION_Y, f11 + translationY, 200.0f, 0.75f));
            SpringSet springSet2 = new SpringSet();
            springSet2.playTogether(SpringUtils.createSpring(this.mView, DynamicAnimation.TRANSLATION_X, translationX, 300.0f, 0.5f), SpringUtils.createSpring(this.mView, DynamicAnimation.TRANSLATION_Y, translationY, 300.0f, 0.5f));
            springSet.addSpringSetListener(this.mFastMoveEndListener);
            springSet.start();
            this.mLastGravityBackAnimation = springSet2;
            this.mLastGravityAnimation = springSet;
        }
    }

    /* access modifiers changed from: package-private */
    public void scaleBounce(BubblePosition bubblePosition) {
        if (bubblePosition != null) {
            float f = 1.0f;
            float width = this.mPosition != null ? bubblePosition.width / ((float) this.mView.getWidth()) : 1.0f;
            if (this.mPosition != null) {
                f = bubblePosition.height / ((float) this.mView.getHeight());
            }
            SpringSet springSet = new SpringSet();
            this.mView.setX(bubblePosition.x + ((bubblePosition.width - ((float) this.mView.getWidth())) / 2.0f));
            this.mView.setY(bubblePosition.y + ((bubblePosition.height - ((float) this.mView.getHeight())) / 2.0f));
            this.mPosition = bubblePosition;
            SpringAnimation createSpring = SpringUtils.createSpring(this.mView, DynamicAnimation.SCALE_X, width, 200.0f, 0.5f);
            createSpring.setStartValue(0.0f);
            SpringAnimation createSpring2 = SpringUtils.createSpring(this.mView, DynamicAnimation.SCALE_Y, f, 200.0f, 0.5f);
            createSpring2.setStartValue(0.0f);
            springSet.playTogether(createSpring, createSpring2);
            springSet.addSpringSetListener(this.mScaleListener);
            springSet.start();
        }
    }

    public boolean inBounceMode() {
        return this.mLastBounce != null && this.mLastBounce.isRunning();
    }

    /* access modifiers changed from: package-private */
    public void edgeBounce(int i) {
        if (this.mLastMoveSpring == null || !this.mLastMoveSpring.isRunning()) {
            boolean z = false;
            if (this.mLastBounce != null && this.mLastBounce.isRunning()) {
                if (this.mLastBounce.removeSpringSetListener(this.mEdgeLeftBounceListener)) {
                    BubbleEventCenter.getEventCenter().fireAnimationEnd(BubbleEventCenter.AnimationType.EdgeBounceLeft, this);
                } else if (this.mLastBounce.removeSpringSetListener(this.mEdgeRightBounceListener)) {
                    BubbleEventCenter.getEventCenter().fireAnimationEnd(BubbleEventCenter.AnimationType.EdgeBounceRight, this);
                }
                this.mLastBounce.fastMove();
                z = true;
            }
            if (!z) {
                this.mBounceValue = this.mView.getX();
            }
            if (i == 256) {
                if (this.mView != null) {
                    SpringSet springSet = new SpringSet();
                    SpringAnimation createSpring = SpringUtils.createSpring(this.mView, DynamicAnimation.X, this.mBounceValue, 50.0f, 0.5f);
                    createSpring.setStartValue(this.mBounceValue - 100.0f);
                    springSet.play(createSpring);
                    springSet.addSpringSetListener(this.mEdgeLeftBounceListener);
                    springSet.start();
                    this.mLastBounce = springSet;
                }
            } else if (i == 512 && this.mView != null) {
                SpringSet springSet2 = new SpringSet();
                SpringAnimation createSpring2 = SpringUtils.createSpring(this.mView, DynamicAnimation.X, this.mBounceValue, 40.0f, 0.5f);
                createSpring2.setStartValue(this.mBounceValue + 100.0f);
                springSet2.play(createSpring2);
                springSet2.addSpringSetListener(this.mEdgeRightBounceListener);
                springSet2.start();
                this.mLastBounce = springSet2;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void enableFloating(boolean z) {
        if (this.mPosition != null && this.mView != null) {
            if (z) {
                this.mFloatingAnim.reset();
                this.mView.startAnimation(this.mFloatingAnim);
                return;
            }
            this.mFloatingAnim.cancel();
        }
    }

    public int compareTo(@NonNull BubbleAnimateWrapper bubbleAnimateWrapper) {
        if (bubbleAnimateWrapper.mPosition == null) {
            return 1;
        }
        if (this.mPosition == null) {
            return -1;
        }
        return this.mPosition.compareTo(bubbleAnimateWrapper.getPosition());
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append(Operators.ARRAY_START_STR);
        sb.append(this.mViewIndex);
        sb.append(",");
        if (this.mPosition == null) {
            str = "NaN, NaN]";
        } else {
            str = this.mPosition.row + "," + this.mPosition.column + Operators.ARRAY_END_STR;
        }
        sb.append(str);
        return sb.toString();
    }
}
