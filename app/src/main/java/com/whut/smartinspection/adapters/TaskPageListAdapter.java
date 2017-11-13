package com.whut.smartinspection.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.whut.smartinspection.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Fortuner on 2017/11/10.
 */

public class TaskPageListAdapter extends BaseAdapter implements View.OnClickListener{
    private List<TaskPageItem> menus;
    private Context mContext;

    public TaskPageListAdapter(Context context, List<TaskPageItem> menus) {
        this.mContext = context;
        this.menus = menus;
    }
    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public Object getItem(int i) {
        return menus.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TaskViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_compeled_task, null);
            holder = new TaskViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (TaskViewHolder) convertView.getTag();
        }

        TaskPageItem menu = menus.get(position);

        holder.item.setText(menu.getText());
        holder.number.setText(menu.getNumber());
        holder.stationName.setText(menu.getStationName());

        return convertView;

    }

    @Override
    public void onClick(View view) {

    }
    public class  TaskViewHolder {
        @BindView(R.id.item)
        public TextView item ;
        @BindView(R.id.textView1)
        public TextView stationName;
        @BindView(R.id.textView2)
        public TextView number;

        public TaskViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
    public static class TaskPageItem {
        private String text ;

        private String stationName;

        private String number;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getStationName() {
            return stationName;
        }

        public String getNumber() {
            return number;
        }

        public void setStationName(String stationName) {
            this.stationName = stationName;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }
}
