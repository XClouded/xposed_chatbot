package com.alibaba.aliweex.bubble;

import android.view.View;
import androidx.collection.ArrayMap;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SpringSet implements DynamicAnimation.OnAnimationEndListener {
    private SpringAnimation mDelayAnim = SpringUtils.createSpring((View) null, DynamicAnimation.SCALE_X, 1.0f, 1500.0f, 0.5f);
    /* access modifiers changed from: private */
    public boolean mDependencyDirty = false;
    private boolean mIsFastMove = false;
    private ArrayMap<SpringAnimation, Node> mNodeMap = new ArrayMap<>();
    private ArrayList<Node> mNodes = new ArrayList<>();
    private ArrayList<SpringAnimation> mPlayingSet = new ArrayList<>();
    private Node mRootNode;
    private final CopyOnWriteArrayList<ISpringSetListener> mSpringListeners = new CopyOnWriteArrayList<>();
    private boolean mStarted = false;
    private boolean mTerminated = false;

    public interface ISpringSetListener {
        void onSpringEnd(SpringSet springSet);

        void onSpringStart(SpringSet springSet);
    }

    public SpringSet() {
        this.mDelayAnim.setStartValue(0.0f);
        this.mRootNode = new Node(this.mDelayAnim);
        this.mNodeMap.put(this.mDelayAnim, this.mRootNode);
    }

    public void playTogether(SpringAnimation... springAnimationArr) {
        if (springAnimationArr != null) {
            Builder play = play(springAnimationArr[0]);
            for (int i = 1; i < springAnimationArr.length; i++) {
                play.with(springAnimationArr[i]);
            }
        }
    }

    public void playTogether(Collection<SpringAnimation> collection) {
        if (collection != null && collection.size() > 0) {
            Builder builder = null;
            for (SpringAnimation next : collection) {
                if (builder == null) {
                    builder = play(next);
                } else {
                    builder.with(next);
                }
            }
        }
    }

    public void playSequentially(SpringAnimation... springAnimationArr) {
        if (springAnimationArr != null) {
            int i = 0;
            if (springAnimationArr.length == 1) {
                play(springAnimationArr[0]);
                return;
            }
            while (i < springAnimationArr.length - 1) {
                i++;
                play(springAnimationArr[i]).before(springAnimationArr[i]);
            }
        }
    }

    public void playSequentially(List<SpringAnimation> list) {
        if (list != null && list.size() > 0) {
            int i = 0;
            if (list.size() == 1) {
                play(list.get(0));
                return;
            }
            while (i < list.size() - 1) {
                i++;
                play(list.get(i)).before(list.get(i));
            }
        }
    }

    public void playSequentially(SpringSet... springSetArr) {
        if (springSetArr != null && springSetArr.length > 0) {
            Builder builder = null;
            for (int i = 0; i < springSetArr.length - 1; i++) {
                SpringSet springSet = springSetArr[i];
                if (builder == null) {
                    builder = play(springSet.mRootNode.mAnimation);
                } else {
                    play(springSetArr[i].mRootNode.mAnimation).before(springSetArr[i + 1].mRootNode.mAnimation);
                }
            }
        }
    }

    public boolean isRunning() {
        int size = this.mNodes.size();
        for (int i = 0; i < size; i++) {
            Node node = this.mNodes.get(i);
            if (node != this.mRootNode && node.mAnimation != null && node.mAnimation.isRunning()) {
                return true;
            }
        }
        return false;
    }

    public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
        dynamicAnimation.removeEndListener(this);
        this.mPlayingSet.remove(dynamicAnimation);
        onChildAnimatorEnded(dynamicAnimation);
    }

    public void start() {
        this.mTerminated = false;
        this.mStarted = true;
        int size = this.mNodes.size();
        for (int i = 0; i < size; i++) {
            this.mNodes.get(i).mEnded = false;
        }
        createDependencyGraph();
        for (int size2 = this.mSpringListeners.size() - 1; size2 >= 0; size2--) {
            this.mSpringListeners.get(size2).onSpringStart(this);
        }
        onChildAnimatorEnded(this.mDelayAnim);
    }

    private void start(Node node) {
        SpringAnimation springAnimation = node.mAnimation;
        this.mPlayingSet.add(springAnimation);
        springAnimation.addEndListener(this);
        springAnimation.start();
        if (this.mIsFastMove && springAnimation.canSkipToEnd()) {
            springAnimation.skipToEnd();
        }
    }

    private void findSiblings(Node node, ArrayList<Node> arrayList) {
        if (!arrayList.contains(node)) {
            arrayList.add(node);
            if (node.mSiblings != null) {
                for (int i = 0; i < node.mSiblings.size(); i++) {
                    findSiblings(node.mSiblings.get(i), arrayList);
                }
            }
        }
    }

    public void fastMove() {
        this.mIsFastMove = true;
        Iterator<SpringAnimation> it = this.mPlayingSet.iterator();
        while (it.hasNext()) {
            SpringAnimation next = it.next();
            if (next.canSkipToEnd()) {
                next.skipToEnd();
            }
        }
    }

    private void createDependencyGraph() {
        if (this.mDependencyDirty) {
            int size = this.mNodes.size();
            for (int i = 0; i < size; i++) {
                this.mNodes.get(i).mParentsAdded = false;
            }
            for (int i2 = 0; i2 < size; i2++) {
                Node node = this.mNodes.get(i2);
                if (!node.mParentsAdded) {
                    node.mParentsAdded = true;
                    if (node.mSiblings != null) {
                        findSiblings(node, node.mSiblings);
                        node.mSiblings.remove(node);
                        int size2 = node.mSiblings.size();
                        for (int i3 = 0; i3 < size2; i3++) {
                            node.addParents(node.mSiblings.get(i3).mParents);
                        }
                        for (int i4 = 0; i4 < size2; i4++) {
                            Node node2 = node.mSiblings.get(i4);
                            node2.addParents(node.mParents);
                            node2.mParentsAdded = true;
                        }
                    }
                }
            }
            for (int i5 = 0; i5 < size; i5++) {
                Node node3 = this.mNodes.get(i5);
                if (node3 != this.mRootNode && node3.mParents == null) {
                    node3.addParent(this.mRootNode);
                }
            }
            updateLatestParent(this.mRootNode, new ArrayList(this.mNodes.size()));
            this.mDependencyDirty = false;
        }
    }

    public Builder play(SpringAnimation springAnimation) {
        if (springAnimation != null) {
            return new Builder(springAnimation);
        }
        return null;
    }

    public boolean isStarted() {
        return this.mStarted;
    }

    public void cancel() {
        this.mTerminated = true;
        if (isStarted()) {
            Iterator<SpringAnimation> it = this.mPlayingSet.iterator();
            while (it.hasNext()) {
                it.next().cancel();
            }
            this.mStarted = false;
        }
    }

    public boolean addSpringSetListener(ISpringSetListener iSpringSetListener) {
        if (!this.mSpringListeners.contains(iSpringSetListener)) {
            return this.mSpringListeners.add(iSpringSetListener);
        }
        return false;
    }

    public boolean removeSpringSetListener(ISpringSetListener iSpringSetListener) {
        return this.mSpringListeners.remove(iSpringSetListener);
    }

    public void clear() {
        cancel();
        this.mSpringListeners.clear();
        this.mPlayingSet.clear();
        this.mNodeMap.clear();
        this.mNodes.clear();
    }

    private void updateLatestParent(Node node, ArrayList<Node> arrayList) {
        if (node.mChildNodes != null) {
            arrayList.add(node);
            int size = node.mChildNodes.size();
            for (int i = 0; i < size; i++) {
                Node node2 = node.mChildNodes.get(i);
                int indexOf = arrayList.indexOf(node2);
                if (indexOf >= 0) {
                    while (indexOf < arrayList.size()) {
                        arrayList.get(indexOf).mLatestParent = null;
                        indexOf++;
                    }
                    node2.mLatestParent = null;
                } else {
                    node2.mLatestParent = node;
                    updateLatestParent(node2, arrayList);
                }
            }
            arrayList.remove(node);
        }
    }

    private static class Node implements Cloneable {
        SpringAnimation mAnimation;
        ArrayList<Node> mChildNodes = null;
        boolean mEnded = false;
        Node mLatestParent = null;
        ArrayList<Node> mParents;
        boolean mParentsAdded = false;
        ArrayList<Node> mSiblings;

        public Node(SpringAnimation springAnimation) {
            this.mAnimation = springAnimation;
        }

        /* access modifiers changed from: package-private */
        public void addChild(Node node) {
            if (this.mChildNodes == null) {
                this.mChildNodes = new ArrayList<>();
            }
            if (!this.mChildNodes.contains(node)) {
                this.mChildNodes.add(node);
                node.addParent(this);
            }
        }

        public void addSibling(Node node) {
            if (this.mSiblings == null) {
                this.mSiblings = new ArrayList<>();
            }
            if (!this.mSiblings.contains(node)) {
                this.mSiblings.add(node);
                node.addSibling(this);
            }
        }

        public void addParent(Node node) {
            if (this.mParents == null) {
                this.mParents = new ArrayList<>();
            }
            if (!this.mParents.contains(node)) {
                this.mParents.add(node);
                node.addChild(this);
            }
        }

        public void addParents(ArrayList<Node> arrayList) {
            if (arrayList != null) {
                int size = arrayList.size();
                for (int i = 0; i < size; i++) {
                    addParent(arrayList.get(i));
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public Node getNodeForAnimation(SpringAnimation springAnimation) {
        Node node = this.mNodeMap.get(springAnimation);
        if (node != null) {
            return node;
        }
        Node node2 = new Node(springAnimation);
        this.mNodeMap.put(springAnimation, node2);
        this.mNodes.add(node2);
        return node2;
    }

    public class Builder {
        private Node mCurrentNode;

        Builder(SpringAnimation springAnimation) {
            boolean unused = SpringSet.this.mDependencyDirty = true;
            this.mCurrentNode = SpringSet.this.getNodeForAnimation(springAnimation);
        }

        public Builder with(SpringAnimation springAnimation) {
            this.mCurrentNode.addSibling(SpringSet.this.getNodeForAnimation(springAnimation));
            return this;
        }

        public Builder before(SpringAnimation springAnimation) {
            this.mCurrentNode.addChild(SpringSet.this.getNodeForAnimation(springAnimation));
            return this;
        }

        public Builder after(SpringAnimation springAnimation) {
            this.mCurrentNode.addParent(SpringSet.this.getNodeForAnimation(springAnimation));
            return this;
        }
    }

    private void onChildAnimatorEnded(DynamicAnimation dynamicAnimation) {
        int i;
        boolean z;
        Node node = this.mNodeMap.get(dynamicAnimation);
        node.mEnded = true;
        if (!this.mTerminated) {
            ArrayList<Node> arrayList = node.mChildNodes;
            if (arrayList == null) {
                i = 0;
            } else {
                i = arrayList.size();
            }
            for (int i2 = 0; i2 < i; i2++) {
                if (arrayList.get(i2).mLatestParent == node) {
                    start(arrayList.get(i2));
                }
            }
            int size = this.mNodes.size();
            int i3 = 0;
            while (true) {
                if (i3 >= size) {
                    z = true;
                    break;
                } else if (!this.mNodes.get(i3).mEnded) {
                    z = false;
                    break;
                } else {
                    i3++;
                }
            }
            if (z) {
                for (int size2 = this.mSpringListeners.size() - 1; size2 >= 0; size2--) {
                    this.mSpringListeners.get(size2).onSpringEnd(this);
                }
                this.mStarted = false;
                this.mIsFastMove = false;
            }
        }
    }
}
