package com.hope.menu.able;

import java.util.List;

/**
 * 获取二级分类
 *
 * Created by hopeliao on 2017/6/16.
 */

public interface IDoubleSourceAble<T extends IDataSourceAble> extends IDataSourceAble {

    List<T> getChildDataSource();
}
