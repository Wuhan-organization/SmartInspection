package com.whut.smartinspection.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.whut.smartinspection.R;
import com.whut.smartinspection.model.TaskItem;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Fortuner on 2017/11/10.
 */

public class TaskPageListAdapter extends BaseAdapter implements View.OnClickListener{
    private List<TaskItem> menus;
    private Context mContext;

    public TaskPageListAdapter(Context context, List<TaskItem> menus) {
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

        TaskItem menu = menus.get(position);

        holder.taskType.setText(menu.getTaskTypeName());
        holder.workMember.setText(menu.getWorker());
        Glide.with(mContext).load(menu.getTaskIcon()).into(holder.taskIcon);
        return convertView;

    }

    @Override
    public void onClick(View view) {

    }
    public class  TaskViewHolder {
        @BindView(R.id.task_icon)
        public ImageView taskIcon;
        @BindView(R.id.work_member)
        public TextView workMember ;
        @BindView(R.id.task_type)
        public TextView taskType;
        public TaskViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
