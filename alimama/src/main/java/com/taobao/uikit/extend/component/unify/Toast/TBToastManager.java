package com.taobao.uikit.extend.component.unify.Toast;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class TBToastManager extends Handler {
    private static final String TAG = "ManagerTBToast";
    private static TBToastManager mTBToastManager;
    /* access modifiers changed from: private */
    public float mCurrentScale = 1.0f;
    private final Queue<TBToast> mQueue = new LinkedBlockingQueue();
    private float mSmallerScale = 0.8f;

    private static final class Messages {
        private static final int ADD_TBToast = 4281172;
        private static final int DISPLAY_TBToast = 4477780;
        private static final int REMOVE_TBToast = 5395284;
        private static final int SMALLER_SCALE = 0;

        private Messages() {
        }
    }

    private TBToastManager() {
    }

    protected static synchronized TBToastManager getInstance() {
        synchronized (TBToastManager.class) {
            if (mTBToastManager != null) {
                TBToastManager tBToastManager = mTBToastManager;
                return tBToastManager;
            }
            mTBToastManager = new TBToastManager();
            TBToastManager tBToastManager2 = mTBToastManager;
            return tBToastManager2;
        }
    }

    /* access modifiers changed from: protected */
    public void add(TBToast tBToast) {
        this.mQueue.add(tBToast);
        showNextTBToast();
    }

    private void showNextTBToast() {
        if (!this.mQueue.isEmpty()) {
            TBToast peek = this.mQueue.peek();
            if (!peek.isShowing()) {
                Message obtainMessage = obtainMessage(4281172);
                obtainMessage.obj = peek;
                sendMessage(obtainMessage);
                return;
            }
            sendMessageDelayed(peek, 4477780, getDuration(peek));
        }
    }

    /* access modifiers changed from: private */
    public void sendMessageDelayed(TBToast tBToast, int i, long j) {
        Message obtainMessage = obtainMessage(i);
        obtainMessage.obj = tBToast;
        sendMessageDelayed(obtainMessage, j);
    }

    private long getDuration(TBToast tBToast) {
        return tBToast.getDuration() + 1000;
    }

    public void handleMessage(Message message) {
        TBToast tBToast = (TBToast) message.obj;
        int i = message.what;
        if (i == 0) {
            startSmallerAnim(tBToast);
        } else if (i == 4281172) {
            displayTBToast(tBToast);
        } else if (i == 4477780) {
            showNextTBToast();
        } else if (i != 5395284) {
            super.handleMessage(message);
        } else {
            removeTBToast(tBToast);
        }
    }

    private void displayTBToast(TBToast tBToast) {
        if (!tBToast.isShowing()) {
            WindowManager windowManager = tBToast.getWindowManager();
            View view = tBToast.getView();
            WindowManager.LayoutParams windowManagerParams = tBToast.getWindowManagerParams();
            if (windowManager != null) {
                if (tBToast instanceof TBActivityToast) {
                    try {
                        Activity activity = ((TBActivityToast) tBToast).getActivity();
                        if (activity == null || activity.isFinishing()) {
                            sendMessageDelayed(tBToast, 5395284, 0);
                            return;
                        } else if (Build.VERSION.SDK_INT < 17) {
                            windowManager.addView(view, windowManagerParams);
                            sendMessageDelayed(tBToast, 5395284, tBToast.getDuration());
                            return;
                        } else if (!activity.isDestroyed()) {
                            windowManager.addView(view, windowManagerParams);
                            sendMessageDelayed(tBToast, 5395284, tBToast.getDuration());
                            return;
                        } else {
                            sendMessageDelayed(tBToast, 5395284, 0);
                            return;
                        }
                    } catch (Throwable unused) {
                        return;
                    }
                } else {
                    windowManager.addView(view, windowManagerParams);
                }
            }
            sendMessageDelayed(tBToast, 0, 1600);
        }
    }

    /* access modifiers changed from: protected */
    public void removeTBToast(TBToast tBToast) {
        if (this.mQueue.contains(tBToast)) {
            WindowManager windowManager = tBToast.getWindowManager();
            if (windowManager == null || !(tBToast instanceof TBActivityToast)) {
                View view = tBToast.getView();
                view.setClickable(false);
                view.setLongClickable(false);
                if (windowManager != null) {
                    this.mQueue.poll();
                    windowManager.removeView(view);
                    sendMessageDelayed(tBToast, 4477780, 500);
                    return;
                }
                return;
            }
            try {
                Activity activity = ((TBActivityToast) tBToast).getActivity();
                if (activity != null && !activity.isFinishing()) {
                    if (Build.VERSION.SDK_INT < 17) {
                        windowManager.removeView(tBToast.getView());
                    } else if (!activity.isDestroyed()) {
                        windowManager.removeView(tBToast.getView());
                    }
                }
                this.mQueue.poll();
                sendMessageDelayed(tBToast, 4477780, 500);
            } catch (Throwable unused) {
            }
        }
    }

    /* access modifiers changed from: protected */
    public void cancelAllTBToasts() {
        removeMessages(4281172);
        removeMessages(4477780);
        removeMessages(5395284);
        for (TBToast tBToast : this.mQueue) {
            if (tBToast.isShowing()) {
                tBToast.getWindowManager().removeView(tBToast.getView());
            }
        }
        this.mQueue.clear();
    }

    /* access modifiers changed from: protected */
    public void startSmallerAnim(final TBToast tBToast) {
        final View view = tBToast.getView();
        view.setClickable(true);
        view.setLongClickable(true);
        final WindowManager windowManager = tBToast.getWindowManager();
        final WindowManager.LayoutParams windowManagerParams = tBToast.getWindowManagerParams();
        final ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{1.0f, this.mSmallerScale});
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (view.getWindowToken() == null) {
                    valueAnimator.cancel();
                    return;
                }
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                tBToast.mAnimView.setScaleX(floatValue);
                tBToast.mAnimView.setScaleY(floatValue);
                float unused = TBToastManager.this.mCurrentScale = floatValue;
            }
        });
        ofFloat.addListener(new Animator.AnimatorListener() {
            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
                TBToastManager.this.sendMessageDelayed(tBToast, 5395284, 800);
            }
        });
        ofFloat.setInterpolator(new AccelerateDecelerateInterpolator());
        ofFloat.setDuration(300);
        ofFloat.start();
        final TBToast tBToast2 = tBToast;
        final View view2 = view;
        view.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View view) {
                if (!tBToast2.mLongClicked) {
                    if (ofFloat.isRunning()) {
                        ofFloat.cancel();
                    } else {
                        TBToastManager.this.removeMessages(5395284, tBToast2);
                    }
                    ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{TBToastManager.this.mCurrentScale, 1.0f});
                    ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            if (view2.getWindowToken() == null) {
                                valueAnimator.cancel();
                                return;
                            }
                            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                            tBToast2.mAnimView.setScaleX(floatValue);
                            tBToast2.mAnimView.setScaleY(floatValue);
                            float unused = TBToastManager.this.mCurrentScale = floatValue;
                            windowManagerParams.height = view2.getHeight();
                            windowManagerParams.width = view2.getWidth();
                            windowManager.updateViewLayout(view2, windowManagerParams);
                        }
                    });
                    ofFloat.addListener(new Animator.AnimatorListener() {
                        public void onAnimationCancel(Animator animator) {
                        }

                        public void onAnimationEnd(Animator animator) {
                        }

                        public void onAnimationRepeat(Animator animator) {
                        }

                        public void onAnimationStart(Animator animator) {
                        }
                    });
                    ofFloat.setInterpolator(new AccelerateDecelerateInterpolator());
                    ofFloat.setDuration(300);
                    ofFloat.start();
                    tBToast2.mLongClicked = true;
                }
                return true;
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 1 && tBToast.mLongClicked) {
                    view.setClickable(false);
                    view.setLongClickable(false);
                    TBToastManager.this.sendMessageDelayed(tBToast, 5395284, 800);
                }
                return false;
            }
        });
    }
}
