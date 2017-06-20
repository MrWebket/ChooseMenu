package com.magic.test.bean;

import com.hope.menu.able.IDoubleSourceAble;

import java.util.ArrayList;
import java.util.List;

/**
 * 多级分类
 *
 * Created by hopeliao on 2017/6/16.
 */

public class DoubleCategory implements IDoubleSourceAble<Category> {

    public List<Category> mDataSource;

    public String name = "开发";

    public DoubleCategory(String name) {
        this.name = name;
    }

    public DoubleCategory() {

    }

    @Override
    public String getText() {
        return name;
    }

    @Override
    public List<Category> getChildDataSource() {
        if(mDataSource == null) {
            mDataSource = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                mDataSource.add(new Category(name + " child " + i));
            }
        }
        return mDataSource;
    }
}
