package com.ccl.ccltools.behavior;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.github.clans.fab.FloatingActionMenu;

public class FABScrollBehavior extends CoordinatorLayout.Behavior<FloatingActionMenu> {
    public static int STATE_SHOW = 0;
    public static int STATE_HIDE = 1;

    public static int mCurrentState = STATE_SHOW;
    private AnimatorSet mHideSet;
    private AnimatorSet mShowSet;
    private float mChildY;

    public FABScrollBehavior() {
    }

    public FABScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionMenu child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, final FloatingActionMenu child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if (dyConsumed > 2 && mCurrentState == STATE_SHOW) {
            Log.e("onNestedScroll", "mCurrentState STATE_SHOW: " + mCurrentState);
            mCurrentState = STATE_HIDE;
            if (mHideSet == null) {
                mHideSet = new AnimatorSet();
                mChildY = child.getY();
                Resources resources = child.getContext().getResources();
                DisplayMetrics dm = resources.getDisplayMetrics();
                int height = dm.heightPixels;
                Log.e("onNestedScroll", "height: " + height);
                mChildY = height - mChildY;
                mHideSet.playTogether(ObjectAnimator.ofFloat(child, "translationY", mChildY),
                        ObjectAnimator.ofFloat(child, "scaleX", 0),
                        ObjectAnimator.ofFloat(child, "scaleY", 0));
                mHideSet.setDuration(200);
            }
            mHideSet.start();
        } else if (dyConsumed < -2 && mCurrentState == STATE_HIDE) {
            Log.e("onNestedScroll", "mCurrentState STATE_HIDE: " + mCurrentState);
            mCurrentState = STATE_SHOW;
            if (mShowSet == null) {
                mShowSet = new AnimatorSet();
                Log.e("onNestedScroll", "mChildY: " + mChildY);
                mShowSet.playTogether(ObjectAnimator.ofFloat(child, "translationY", 0),
                        ObjectAnimator.ofFloat(child, "scaleX", 1),
                        ObjectAnimator.ofFloat(child, "scaleY", 1));

                mShowSet.setDuration(200);
            }
            mShowSet.start();
        }
    }

}
