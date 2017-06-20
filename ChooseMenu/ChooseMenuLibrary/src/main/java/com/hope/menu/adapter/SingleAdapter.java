package com.hope.menu.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hope.menu.R;
import com.hope.menu.able.IDataSourceAble;
import com.hope.menu.utils.MenuUtil;

/**
 * 单个adapter
 *
 * Created by hopeliao on 2017/6/16.
 */

public class SingleAdapter extends BaseAbsAdapter<IDataSourceAble>{

    private int mSelected;

    public SingleAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            RelativeLayout root = new RelativeLayout(mContext);
            root.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    MenuUtil.dip2px(mContext, 45)));

            holder = new ViewHolder();
            holder.textView = new TextView(mContext);
            holder.textView.setGravity(Gravity.CENTER);
            holder.textView.setBackgroundResource(R.drawable.menu_single_bg);
            holder.textView.setTextColor(mContext.getResources().getColorStateList(R.color.text_color));
            holder.textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            root.addView(holder.textView);

            convertView = root;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        IDataSourceAble able = mDataSource.get(position);
        holder.textView.setText(able.getText());
        holder.textView.setSelected(mSelected == position);
        return convertView;
    }

    private class ViewHolder {
        private TextView textView;
    }

    public void setSelected(int selected) {
        this.mSelected = selected;
        notifyDataSetChanged();
    }
}
