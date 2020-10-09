package com.alimama.moon.features.home.item;

import android.os.Handler;
import android.os.Looper;

public class UnionAutoPlayer {
    private PlayDirection mDirection = PlayDirection.to_right;
    public boolean mPaused = false;
    private PlayRecycleMode mPlayRecycleMode = PlayRecycleMode.repeat_from_start;
    private Playable mPlayable;
    /* access modifiers changed from: private */
    public boolean mPlaying = false;
    private boolean mSkipNext = false;
    /* access modifiers changed from: private */
    public int mTimeInterval = 5000;
    /* access modifiers changed from: private */
    public final Runnable mTimerTask = new Runnable() {
        public void run() {
            if (!UnionAutoPlayer.this.mPaused) {
                UnionAutoPlayer.this.playNextFrame();
            }
            if (UnionAutoPlayer.this.mPlaying) {
                UnionAutoPlayer.this.mUiHandler.postDelayed(UnionAutoPlayer.this.mTimerTask, (long) UnionAutoPlayer.this.mTimeInterval);
            }
        }
    };
    private int mTotal;
    /* access modifiers changed from: private */
    public final Handler mUiHandler = new Handler(Looper.getMainLooper());

    public enum PlayDirection {
        to_left,
        to_right
    }

    public enum PlayRecycleMode {
        repeat_from_start,
        play_back
    }

    public interface Playable {
        int getCurrent();

        int getTotal();

        void playNext();

        void playPrevious();

        void playTo(int i);
    }

    public UnionAutoPlayer(Playable playable) {
        this.mPlayable = playable;
    }

    public void play() {
        play(0, PlayDirection.to_right);
    }

    public void skipNext() {
        this.mSkipNext = true;
    }

    private void play(int i, PlayDirection playDirection) {
        if (!this.mPlaying) {
            this.mTotal = this.mPlayable.getTotal();
            if (this.mTotal > 1) {
                this.mPlaying = true;
                playTo(i);
                this.mUiHandler.removeCallbacks(this.mTimerTask);
                this.mUiHandler.postDelayed(this.mTimerTask, (long) this.mTimeInterval);
            }
        }
    }

    public void play(int i) {
        play(i, PlayDirection.to_right);
    }

    public void stop() {
        if (this.mPlaying) {
            this.mPlaying = false;
            this.mUiHandler.removeCallbacks(this.mTimerTask);
        }
    }

    public UnionAutoPlayer setTimeInterval(int i) {
        this.mTimeInterval = i;
        return this;
    }

    public UnionAutoPlayer setPlayRecycleMode(PlayRecycleMode playRecycleMode) {
        this.mPlayRecycleMode = playRecycleMode;
        return this;
    }

    /* access modifiers changed from: private */
    public void playNextFrame() {
        if (this.mSkipNext) {
            this.mSkipNext = false;
            return;
        }
        int current = this.mPlayable.getCurrent();
        if (this.mDirection == PlayDirection.to_right) {
            if (current != this.mTotal - 1) {
                playNext();
            } else if (this.mPlayRecycleMode == PlayRecycleMode.play_back) {
                this.mDirection = PlayDirection.to_left;
                playNextFrame();
            } else {
                playTo(0);
            }
        } else if (current != 0) {
            playPrevious();
        } else if (this.mPlayRecycleMode == PlayRecycleMode.play_back) {
            this.mDirection = PlayDirection.to_right;
            playNextFrame();
        } else {
            playTo(this.mTotal - 1);
        }
    }

    public void pause() {
        this.mPaused = true;
    }

    public void resume() {
        this.mPaused = false;
    }

    private void playTo(int i) {
        this.mPlayable.playTo(i);
    }

    private void playNext() {
        this.mPlayable.playNext();
    }

    private void playPrevious() {
        this.mPlayable.playPrevious();
    }
}
