package com.hope.menu.delegate;

import com.hope.menu.widget.BaseCategoryView;

/**
 * 单行选中
 *
 * Created by hopeliao on 2017/6/16.
 */

public interface IItemChooseDelegate {

    void onChoose(BaseCategoryView view, int poistion, String text);
}
