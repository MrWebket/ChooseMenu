package com.hope.menu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.hope.menu.delegate.IItemChooseDelegate;

/**
 * Base 类别 View
 * Created by hopeliao on 2017/6/16.
 */

public class BaseCategoryView extends RelativeLayout {

    public BaseCategoryView(Context context) {
        super(context);
    }

    public BaseCategoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseCategoryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public IItemChooseDelegate mDelegate;

    public void setDelegate(IItemChooseDelegate delegate) {
        this.mDelegate = delegate;
    }

    public void show() {}

    public void dismiss() {}
}
