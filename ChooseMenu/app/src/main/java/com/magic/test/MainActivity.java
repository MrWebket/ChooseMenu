package com.magic.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hope.menu.ChooseMenuView;
import com.hope.menu.adapter.ChooseMenuAdapter;
import com.hope.menu.widget.DoubleView;
import com.hope.menu.widget.SingleView;
import com.magic.test.adapter.ListAdapter;
import com.magic.test.common.Common;

/**
 * Created by hopeliao on 2017/6/15.
 */

public class MainActivity extends AppCompatActivity {

    private ChooseMenuView mMenuView;

    private DoubleView mDoubleView;
    private SingleView mSingleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ListView mListView = (ListView) findViewById(R.id.list_view);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "ItemClick", Toast.LENGTH_SHORT).show();
            }
        });
        ListAdapter mAdapter = new ListAdapter(this);
        mListView.setAdapter(mAdapter);

        mSingleView = new SingleView(this);
        mSingleView.setDataSource(Common.getLeftDataSource());

        mDoubleView = new DoubleView(this);
        mDoubleView.setLeftDataSource(Common.getDoubleDataSource());

        mMenuView = (ChooseMenuView) findViewById(R.id.menu_view);
        mMenuView.setAdapter(new ChooseMenuAdapter() {

            String[] titles = {"智能排序", "区域", "筛选"};
            @Override
            public int getMenuCount() {
                return 3;
            }

            @Override
            public String getMenuTitleByPosition(int position) {
                return titles[position];
            }

            @Override
            public View getView(int position) {
                switch (position) {
                    case 0:
                        return mSingleView;
                    case 1:
                        return mDoubleView;
                    case 2:
                        return LayoutInflater.from(MainActivity.this).inflate(R.layout.custom, null);

                }
                return mSingleView;
            }
        });


        mAdapter.setDataSource(Common.getListDataSource());
    }
}
