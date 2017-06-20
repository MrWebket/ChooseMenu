package com.hope.menu;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.hope.menu.adapter.ChooseMenuAdapter;
import com.hope.menu.delegate.IItemChooseDelegate;
import com.hope.menu.utils.MenuUtil;
import com.hope.menu.widget.BaseCategoryView;
import com.hope.menu.widget.TabIndicator;

/**
 * Menu
 * @author hopeliao
 *
 */
public class ChooseMenuView extends RelativeLayout implements IItemChooseDelegate, View.OnClickListener {

    private static final int DURATION = 200;

    private static final int DEFAULT_TAB_INDICATOR_ID = 1;

    private static final int DEFAULT_INDICATOR_HEIGHT = 45;

    private static final int DEFAULT_CONTENT_BOTTOM_MARGIN = 50;

    private TabIndicator mTabIndicator;

    private int mIndicatorHeight;

    private FrameLayout mContentView;

    private ChooseMenuAdapter mAdapter;

    private int mContentBottomMargin;

    private TranslateAnimation mDisplayAnim, mHideAnim;

    private AlphaAnimation mToDisplayAlphaAnim, mToHideAlphaAnim;

    private View mCurrentView;

    private OnDismissSwitchListener mListener;


    public ChooseMenuView(Context context) {
        super(context);
        init(null, 0);
    }

    public ChooseMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ChooseMenuView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        mIndicatorHeight = MenuUtil.dip2px(getContext(), DEFAULT_INDICATOR_HEIGHT);

        mContentBottomMargin = MenuUtil.dip2px(getContext(), DEFAULT_CONTENT_BOTTOM_MARGIN);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        generateAnimatable(getHeight());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        createContentView();
    }

    private void createContentView() {
        removeAllViews();

        mTabIndicator = new TabIndicator(getContext());
        mTabIndicator.setId(DEFAULT_TAB_INDICATOR_ID);
        mTabIndicator.setOnClickListener(new TabIndicator.OnClickListener() {
            @Override
            public void OnClick(View v, int position) {
                if(isChildShowing(position)) {
                    dismiss();
                } else {
                    if(mTabIndicator != null) {
                        mTabIndicator.setDismiss(false);
                    }

                    hideAllChildView();

                    mCurrentView = mContentView.getChildAt(position);

                    if(isShowing()) {
                        mCurrentView.setVisibility(View.VISIBLE);
                        mContentView.getChildAt(mTabIndicator.getLastIndicatorPosition()).setVisibility(View.INVISIBLE);
                    } else {
                        mCurrentView.setVisibility(View.INVISIBLE);
                        mContentView.startAnimation(mToDisplayAlphaAnim);
                        mCurrentView.startAnimation(mDisplayAnim);
                    }
                }
            }
        });
        LayoutParams tabIndicatorParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mIndicatorHeight);
        addView(mTabIndicator, tabIndicatorParams);

        mContentView = new FrameLayout(getContext());
        LayoutParams contentParam = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        contentParam.addRule(BELOW, DEFAULT_TAB_INDICATOR_ID);
        mContentView.setBackgroundColor(Color.parseColor("#77000000"));
        addView(mContentView, contentParam);

        mContentView.setOnClickListener(this);

        mContentView.setVisibility(View.INVISIBLE); //先隐藏
    }

    private boolean isChildShowing(int pos) {
        View view = mContentView.getChildAt(pos);
        return view != null && view.isShown();
    }

    private void generateAnimatable(int height) {
        if(mDisplayAnim == null) {
            mDisplayAnim = new TranslateAnimation(0, 0, -height, 0);
            mDisplayAnim.setDuration(DURATION);
            mDisplayAnim.setFillAfter(true);
            mDisplayAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mContentView.setVisibility(View.VISIBLE);

                    if(mCurrentView != null) {
                        mCurrentView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if(mListener != null) {
                        mListener.onShow(ChooseMenuView.this);
                    }

                    noticeChildView(false);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        if(mHideAnim == null) {
            mHideAnim = new TranslateAnimation(0, 0, 0, -height);
            mHideAnim.setDuration(DURATION);
            mHideAnim.setFillAfter(true);
            mHideAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mContentView.setVisibility(View.VISIBLE);

                    if(mCurrentView != null) {
                        mCurrentView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mContentView.setVisibility(View.INVISIBLE);

                    hideAllChildView();

                    noticeChildView(true);

                    if(mListener != null) {
                        mListener.onDismiss(ChooseMenuView.this);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        if(mToDisplayAlphaAnim == null) {
            mToDisplayAlphaAnim = new AlphaAnimation(0.0F, 1.0F);
            mToDisplayAlphaAnim.setDuration(DURATION);
            mToDisplayAlphaAnim.setFillAfter(true);
        }
        if(mToHideAlphaAnim == null) {
            mToHideAlphaAnim = new AlphaAnimation(1.0F, 0.0F);
            mToHideAlphaAnim.setDuration(DURATION);
            mToHideAlphaAnim.setFillAfter(true);
        }
    }

    private void hideAllChildView() {
        int count = mContentView.getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = mContentView.getChildAt(i);
            if(childView != null) {
                childView.clearAnimation();
                childView.setVisibility(View.INVISIBLE);
            }
        }
    }

    public int getTabIndicatorHeight() {
        return mIndicatorHeight;
    }

    public void setTabIndicatorHeight(int height) {
        ViewGroup.LayoutParams lp = mTabIndicator.getLayoutParams();
        if(lp != null) {
            lp.height = height;

            mTabIndicator.setLayoutParams(lp);
        }
    }


    public boolean isShowing() {
        return mContentView.isShown();
    }


    public boolean isClosed() {
        return !isShowing();
    }

    private void dismiss() {
        if(isClosed()) {
            return;
        }

        if(mTabIndicator != null) {
            mTabIndicator.resetCurrentPos();
            mTabIndicator.resetTextState();
            mTabIndicator.setDismiss(true);
        }
        mContentView.startAnimation(mToHideAlphaAnim);

        if(mCurrentView != null) {
            mCurrentView.startAnimation(mHideAnim);
        }
    }

    public void setAdapter(ChooseMenuAdapter menuAdapter) {
        mAdapter = menuAdapter;

        mTabIndicator.setTitles(mAdapter);

        createContentChildView();
    }

    private void noticeChildView(boolean isDismiss) {
        int count = mContentView.getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = mContentView.getChildAt(i);
            if(childView instanceof BaseCategoryView) {
                if(isDismiss) {
                    ((BaseCategoryView) childView).dismiss();
                } else {
                    ((BaseCategoryView) childView).show();
                }

            }
        }
    }

    private void createContentChildView() {
        if(mAdapter != null) {
            mContentView.removeAllViews();

            int count = mAdapter.getMenuCount();
            for (int i = 0; i < count; i++) {
                View childView = mContentView.getChildAt(i);
                if(childView == null) {
                    childView = mAdapter.getView(i);
                }
                if(childView != null) {
                    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.bottomMargin = mContentBottomMargin;

                    if(childView instanceof BaseCategoryView) {
                        ((BaseCategoryView) childView).setDelegate(this);
                    }

                    childView.setVisibility(View.INVISIBLE);
                    mContentView.addView(childView, lp);
                }
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        float y = ev.getY();
        int top = getPaddingTop();
        if(y + top <= mIndicatorHeight) {
            return true;
        }
        return isShowing();
    }


    public void setContentBottomMargin(int margin) {
        if(mContentView != null && mContentView.getChildCount() > 0) {
            int count = mContentView.getChildCount();
            for (int i = 0; i < count; i++) {
                View childView = mContentView.getChildAt(i);
                if(childView != null && childView.getLayoutParams() != null) {
                    FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) childView.getLayoutParams();
                    lp.bottomMargin = margin;

                    childView.setLayoutParams(lp);
                }
            }
        }
    }

    @Override
    public void onChoose(BaseCategoryView view, int poistion, String text) {
        if(isShowing()) {
            dismiss();

            mTabIndicator.setCurrentText(text);
        }
    }

    public void setTabText(int pos, String text) {
        mTabIndicator.setPositionText(pos, text);
    }

    public void setTabText(String text) {
        setTabText(mTabIndicator.getCurrentIndicatorPosition(), text);
    }

    public void setOnDismissSwitchListener(OnDismissSwitchListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    public interface OnDismissSwitchListener {

        void onDismiss(ChooseMenuView menuView);

        void onShow(ChooseMenuView menuView);
    }
}
