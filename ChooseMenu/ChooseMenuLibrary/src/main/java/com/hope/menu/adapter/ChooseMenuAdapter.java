package com.hope.menu.adapter;

import android.view.View;

/**
 * Menu adapter
 *
 * Created by hopeliao on 2017/6/15.
 */

public interface ChooseMenuAdapter {

    int getMenuCount();

    String getMenuTitleByPosition(int position);

    View getView(int position);
}
