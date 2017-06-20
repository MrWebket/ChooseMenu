package com.magic.test.common;

import com.magic.test.bean.Category;
import com.magic.test.bean.DoubleCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hopeliao on 2017/6/16.
 */

public class Common {

    private static String[] data = {"离我最近", "好评优先", "价格最低"};

    private static String[] data1 = {"海淀区","丰台区", "朝阳区", "西城区"};

    private static String[][] data2 = {
            {"上地", "西二旗", "清河", "中关村", "苏州桥", "知春路", "知春里"},
            {"刘家窑", "北京南站", "方庄", "青塔", "公益西桥", "新宫"},
            {"酒仙桥", "管庄", "建外大街", "望京", "亮马桥", "左家庄"},
            {"德外大街", "西单", "菜市口", "新街口", "宣武门"}};

    public static List<Category> getLeftDataSource() {
        List<Category> list = new ArrayList<>();

        for (int i = 0; i < data.length; i++) {
            list.add(new Category(data[i]));
        }
        return list;
    }

    public static List<DoubleCategory> getDoubleDataSource() {
        List<DoubleCategory> list = new ArrayList<>();

        for (int i = 0; i < data1.length; i++) {
            DoubleCategory doubleCategory = new DoubleCategory();
            doubleCategory.name = data1[i];
            doubleCategory.mDataSource = new ArrayList<>();
            for (int j = 0; j < data2[i].length; j++) {
                doubleCategory.mDataSource.add(new Category(data2[i][j]));
            }

            list.add(doubleCategory);
        }
        return list;
    }

    public static List<String> getListDataSource() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add(null);
        }
        return list;
    }
}
