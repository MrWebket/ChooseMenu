package com.hope.menu.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hope.menu.able.IDataSourceAble;
import com.hope.menu.able.IDoubleSourceAble;
import com.hope.menu.adapter.SingleAdapter;
import com.hope.menu.utils.MenuUtil;

import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.media.CamcorderProfile.get;

/**
 * 多列
 * <p>
 * Created by hopeliao on 2017/6/16.
 */

public class DoubleView extends BaseCategoryView {

    private static final int ID_DEFAULT_LEFT_LISTVIEW = 1;
    private static final int ID_DEFAULT_RIGHT_LISTVIEW = 2;

    private static final int DEFAULT_LEFT_WIDTH = 150;

    private int mLeftWidth;

    private ListView mLeftListView, mRightListView;

    private SingleAdapter mLeftAdapter, mRightAdapter;

    private int mLeftPoistion, mRightPoistion;

    private SparseArray<Integer> mCheckedCache = new SparseArray<Integer>();

    public DoubleView(Context context) {
        super(context);
        init(null, 0);
    }

    public DoubleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DoubleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        mLeftWidth = MenuUtil.dip2px(getContext(), DEFAULT_LEFT_WIDTH);

        mLeftListView = new ListView(getContext());
        mLeftListView.setId(ID_DEFAULT_LEFT_LISTVIEW);
        mLeftListView.setDivider(null);
        mLeftListView.setDividerHeight(0);
        mLeftListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mLeftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mLeftAdapter.setSelected(position);
                mLeftPoistion = position;
                updateRightAdapter();
            }
        });
        RelativeLayout.LayoutParams leftParam = new LayoutParams(mLeftWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLeftListView.setLayoutParams(leftParam);
        mLeftAdapter = new SingleAdapter(getContext());
        mLeftListView.setAdapter(mLeftAdapter);

        addView(mLeftListView);

        mRightListView = new ListView(getContext());
        mRightListView.setId(ID_DEFAULT_RIGHT_LISTVIEW);
        mRightListView.setDivider(null);
        mRightListView.setDividerHeight(0);
        mRightListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mRightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IDataSourceAble able = (IDataSourceAble) parent.getItemAtPosition(position);
                if (able != null) {
                    mRightPoistion = position;

                    mRightAdapter.setSelected(position);

                    updateRightPoisition(position);

                    if (mDelegate != null) {
                        mDelegate.onChoose(DoubleView.this, position, able.getText());
                    }
                }
            }
        });
        RelativeLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.RIGHT_OF, ID_DEFAULT_LEFT_LISTVIEW);

        mRightListView.setLayoutParams(lp);
        mRightAdapter = new SingleAdapter(getContext());
        mRightAdapter.setSelected(-1);
        mRightListView.setAdapter(mRightAdapter);

        addView(mRightListView);

        lp.addRule(RelativeLayout.ALIGN_BOTTOM, ID_DEFAULT_LEFT_LISTVIEW);
        mRightListView.setLayoutParams(lp);
//        leftParam.addRule(RelativeLayout.ALIGN_TOP, ID_DEFAULT_RIGHT_LISTVIEW);

    }

    public void setLeftDataSource(List<? extends IDataSourceAble> list) {
        if (mLeftAdapter != null) {
            mLeftAdapter.setDataSource((List<IDataSourceAble>) list);

            mLeftPoistion = 0;
            updateRightAdapter();
        }
    }

    private void updateRightPoisition(int pos) {
        mCheckedCache.clear();
        mCheckedCache.put(mLeftPoistion, pos);
    }

    private void updateRightAdapter() {
        if (mLeftAdapter.getDataSource().size() > 0) {
            IDataSourceAble able = mLeftAdapter.getDataSource().get(mLeftPoistion);

            int rightPoisition = -1;
            if (mCheckedCache.get(mLeftPoistion) != null) {
                rightPoisition =  mCheckedCache.get(mLeftPoistion);
            } else {
                mCheckedCache.put(mLeftPoistion, -1);
            }

            if (able instanceof IDoubleSourceAble) {
                IDoubleSourceAble doubleAble = (IDoubleSourceAble) able;
                mRightAdapter.setDataSource(doubleAble.getChildDataSource());

                mRightAdapter.setSelected(rightPoisition);
                mRightListView.setSelection(rightPoisition);
            }
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        for(int i = 0; i < mCheckedCache.size(); i++) {
            int key = mCheckedCache.keyAt(i);
            int value = mCheckedCache.get(key);

            if(value != -1) {
                mLeftPoistion = key;

                mLeftAdapter.setSelected(mLeftPoistion);
                mLeftListView.setSelection(mLeftPoistion);

                updateRightAdapter();
            }
        }
    }

    public void setLeftListWidth(int leftWidth) {
        mLeftWidth = leftWidth;

        ViewGroup.LayoutParams lp = mLeftListView.getLayoutParams();
        if(lp != null) {
            lp.width = mLeftWidth;
            mLeftListView.setLayoutParams(lp);
        }
    }

    public int getLeftPoisition() {
        return mLeftPoistion;
    }

    public int getRightPosition() {
        return mRightPoistion;
    }

}
