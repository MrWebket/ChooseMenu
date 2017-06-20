package com.magic.test.bean;

import com.hope.menu.able.IDataSourceAble;

/**
 * Created by hopeliao on 2017/6/16.
 */

public class Category implements IDataSourceAble {

    public String name = "测试";

    public Category(String name) {
        this.name = name;
    }

    public Category() {

    }

    @Override
    public String getText() {
        return name;
    }
}
