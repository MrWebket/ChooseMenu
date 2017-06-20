package com.hope.menu.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hope.menu.able.IDataSourceAble;
import com.hope.menu.adapter.SingleAdapter;

import java.util.List;

/**
 * 单个List
 *
 * Created by hopeliao on 2017/6/16.
 */

public class SingleView extends BaseCategoryView implements AdapterView.OnItemClickListener{

    private ListView mListView;

    private SingleAdapter mSingleAdapter;

    private int mLastPosition;

    public SingleView(Context context) {
        super(context);
        init(null, 0);
    }

    public SingleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SingleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        mListView = new ListView(getContext());
        mListView.setDivider(null);
        mListView.setDividerHeight(0);
        mListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mListView.setOnItemClickListener(this);
        mListView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mSingleAdapter = new SingleAdapter(getContext());
        mListView.setAdapter(mSingleAdapter);

        addView(mListView);
    }

    public ListView getListView() {
        return mListView;
    }

    public void setDataSource(List<? extends IDataSourceAble> list) {
        if(mSingleAdapter != null) {
            mSingleAdapter.setDataSource((List<IDataSourceAble>) list);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        IDataSourceAble able = (IDataSourceAble) parent.getItemAtPosition(position);
        if(able != null) {
            mSingleAdapter.setSelected(position);

            mLastPosition = position;
            if(mDelegate != null) {
                mDelegate.onChoose(this, mLastPosition, able.getText());
            }
        }
    }

    public int getPosition() {
        return mLastPosition;
    }

    public void setPosition(int pos) {
        this.mLastPosition = pos;

        if(mSingleAdapter != null) {
            mSingleAdapter.setSelected(mLastPosition);
        }
    }
}
