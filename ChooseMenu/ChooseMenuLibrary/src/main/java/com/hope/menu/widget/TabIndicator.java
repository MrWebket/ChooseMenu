package com.hope.menu.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hope.menu.R;
import com.hope.menu.adapter.ChooseMenuAdapter;
import com.hope.menu.utils.MenuUtil;

import java.util.Arrays;
import java.util.List;

;

/**
 * Menu Title
 *
 * Created by hopeliao on 2017/6/15.
 */
public class TabIndicator extends LinearLayout {

    private int mTabVisibleCount = 4;// tab数量

    /*
     * 分割线
     */
    private Paint mDividerPaint;

    private int mDividerColor = 0xFFdddddd;// 分割线颜色

    private int mDividerPadding = 14;// 分割线距离上下padding

    /*
     * 上下两条线
     */
    private Paint mLinePaint;

    private float mLineHeight = 1;

    private int mLineColor = 0xFFeeeeee;

    private int mTabTextSize = 13;// 指针文字的大小,sp

    private int mTabDefaultColor = 0xFF666666;// 未选中默认颜色

    private int mTabSelectedColor = 0x50d2c2;// 指针选中颜色

    private int drawableRight = 5;

    private int measureHeight, measuredWidth;

    private int mTabCount;// 设置的条目数量

    private int mCurrentIndicatorPosition;// 上一个指针选中条目

    private int mLastIndicatorPosition;// 上一个指针选中条目

    private OnClickListener mOnClickListener;

    private boolean isDismiss = false;

    public TabIndicator(Context context) {
        this(context, null);
    }

    public TabIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TabIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setOnClickListener(OnClickListener clickListener) {
        this.mOnClickListener = clickListener;
    }

    private void init() {
        mTabSelectedColor = Color.parseColor("#50d2c2");

        setOrientation(LinearLayout.HORIZONTAL);
        setBackgroundColor(Color.WHITE);
        setWillNotDraw(false);

        mDividerPaint = new Paint();
        mDividerPaint.setAntiAlias(true);
        mDividerPaint.setColor(mDividerColor);

        mLinePaint = new Paint();
        mLinePaint.setColor(mLineColor);

        mDividerPadding = MenuUtil.px2dip(getContext(), mDividerPadding);
        drawableRight = MenuUtil.px2dip(getContext(), drawableRight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureHeight = getMeasuredHeight();
        measuredWidth = getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < mTabCount - 1; ++i) {
            final View child = getChildAt(i);
            if (child == null || child.getVisibility() == View.GONE) {
                continue;
            }
            canvas.drawLine(child.getRight(), mDividerPadding, child.getRight(), measureHeight - mDividerPadding, mDividerPaint);
        }
        //上边黑线
        canvas.drawRect(0, 0, measuredWidth, mLineHeight, mLinePaint);

        //下边黑线
        canvas.drawRect(0, measureHeight - mLineHeight, measuredWidth, measureHeight, mLinePaint);
    }

    /**
     * 添加相应的布局进此容器
     */
    public void setTitles(List<String> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalStateException("条目数量位空");
        }
        this.removeAllViews();

        mTabCount = list.size();
        for (int i = 0; i < mTabCount; ++i) {
            addView(generateTextView(list.get(i), i));
        }

        postInvalidate();
    }

    public void setTitles(String[] list) {
        setTitles(Arrays.asList(list));
    }

    public void setTitles(ChooseMenuAdapter menuAdapter) {
        if (menuAdapter == null) {
            return;
        }
        this.removeAllViews();

        mTabCount = menuAdapter.getMenuCount();
        for (int i = 0; i < mTabCount; ++i) {
            addView(generateTextView(menuAdapter.getMenuTitleByPosition(i), i));
        }
        postInvalidate();
    }

    private void switchTab(int position) {
        TextView tv = getChildTextView(position);

        if (mOnClickListener != null) {
            mOnClickListener.OnClick(tv, position);
        }
        if(isDismiss) {
            tv.setSelected(false);
            resetCurrentPos();
            return;
        }
        tv.setSelected(true);

        if (mLastIndicatorPosition == position) {
            // 点击同一个条目时
            tv.setTextColor(mTabSelectedColor);
            return;
        } else {
            TextView lastTv = getChildTextView(mLastIndicatorPosition);
            lastTv.setSelected(false);
        }

        mCurrentIndicatorPosition = position;
        setTextColor(mLastIndicatorPosition);

        tv.setTextColor(mTabSelectedColor);

        mLastIndicatorPosition = position;
    }

    public void setDismiss(boolean isDismiss) {
        this.isDismiss = isDismiss;
    }

    public void resetTextState() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            TextView tv = getChildTextView(i);
            tv.setSelected(false);
        }
    }

    public void setTextSize(int position, int textSize) {
        mTabTextSize = textSize;

        TextView tv = getChildTextView(position);
        tv.setTextSize(mTabTextSize);
    }

    public void setLineColor(int color) {
        mLineColor = color;
        mLinePaint.setColor(mLineColor);
        invalidate();
    }

    public void setLineHeight(int height) {
        mLineHeight = height;
        invalidate();
    }

    /**
     * 重置字体颜色
     */
    public void setTextColor(int position) {
        TextView tv = getChildTextView(position);
        tv.setTextColor(mTabDefaultColor);
    }

    /**
     * 重置当前字体颜色
     */
    public void resetCurrentPos() {
        setTextColor(mCurrentIndicatorPosition);
    }

    /**
     * 获取文本
     */
    public TextView getChildTextView(int position) {
        return (TextView) ((ViewGroup) getChildAt(position)).getChildAt(0);
    }

    /**
     * 直接用TextView使用weight不能控制图片，需要用用父控件包裹
     */
    private View generateTextView(String title, int position) {
        // 子TextView
        TextView tv = new TextView(getContext());
        tv.setGravity(Gravity.CENTER);
        tv.setText(title);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTabTextSize);
        tv.setTextColor(mTabDefaultColor);
        tv.setSingleLine();
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setMaxEms(6);//限制4个字符
        Drawable drawable = getResources().getDrawable(R.drawable.level);
        tv.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        tv.setCompoundDrawablePadding(drawableRight);

        // 将TextView添加到父控件RelativeLayout
        RelativeLayout rl = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        rlParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        rl.addView(tv, rlParams);
        rl.setId(position);

        // 再将RelativeLayout添加到LinearLayout中
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        params.gravity = Gravity.CENTER;
        rl.setLayoutParams(params);

        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 设置点击事件
                switchTab(v.getId());
            }
        });

        return rl;
    }


    /**
     * 高亮字体颜色
     */
    public void highLightPosition(int position) {
        TextView tv = getChildTextView(position);
        tv.setTextColor(mTabSelectedColor);
    }

    public int getCurrentIndicatorPosition() {
        return mCurrentIndicatorPosition;
    }

    public void setCurrentText(String text) {
        setPositionText(mCurrentIndicatorPosition, text);
    }

    public void setPositionText(int position, String text) {
        if (position < 0 || position > mTabCount - 1) {
            throw new IllegalArgumentException("positio faild");
        }
        TextView tv = getChildTextView(position);
        tv.setTextColor(mTabDefaultColor);
        tv.setText(text);
    }

    public int getLastIndicatorPosition() {
        return mLastIndicatorPosition;
    }


    /**
     * 条目点击事件
     */
    public interface OnClickListener {
        /**
         * 回调方法
         *
         * @param v        当前点击的view
         * @param position 当前点击的position
         */
        void OnClick(View v, int position);
    }
}
