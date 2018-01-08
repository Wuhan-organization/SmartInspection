package com.whut.smartinspection.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.whut.smartinspection.R;
import com.whut.smartinspection.component.handler.EventCallable;
import com.whut.smartinspection.model.PatrolContent;
import com.whut.smartinspection.model.Record;
import com.whut.smartinspection.widgets.ExtendedEditText;
import com.wx.wheelview.adapter.BaseWheelAdapter;

import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Fortuner on 2018/1/1.
 */

public class FullWheelAdapter extends BaseWheelAdapter<PatrolContent> {
    private List<PatrolContent> patrolContent;
    private List<Record> records;
    private Context mContext;
    private EventCallable mcallable;

    public FullWheelAdapter(Context context) {
        mContext = context;
    }
    public FullWheelAdapter(Context context, List<PatrolContent> patrolContent, List<Record> records) {
        this.mContext = context;
        this.patrolContent = patrolContent;
        this.records = records;
    }
    @Override
    protected View bindView(final int position, View convertView, ViewGroup parent) {
        TaskViewHolder1 holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_full_tour, null);
            holder = new TaskViewHolder1(convertView);
            convertView.setTag(holder);
        } else {
            holder = (TaskViewHolder1) convertView.getTag();
        }
        PatrolContent item = patrolContent.get(position);//巡视内容
        Record record = records.get(position);//巡视结果
        if("0".equals(item.getPatrolContentTypeNo())){
            holder.radioGroup.setVisibility(View.VISIBLE);
            holder.llDegree.setVisibility(View.GONE);
        }else{
            holder.radioGroup.setVisibility(View.GONE);
            holder.llDegree.setVisibility(View.VISIBLE);
        }
        holder.textContent.setText(item.getNo()+"."+item.getContent());
//        holder.textContent.setTextSize(R.dimen.full_inspect_activity_li);
        holder.tvDegree.setText(item.getPatrolContentName());
        holder.etDegree.setText(String.valueOf(record.getValueFloat()));
        holder.etDegree.setSelection(holder.etDegree.getText().toString().length());
        holder.radioGroup.setOnCheckedChangeListener(null);//防止串行混乱
        holder.radioGroup.clearCheck();
        if("T".equals(record.getValueChar())){
//            RadioButton r = (RadioButton) holder.radioGroup.getChildAt(0);
//            r.setChecked(true);
            holder.radioGroup.check(R.id.rb_one);
        }else if("F".equals(record.getValueChar())){
//            RadioButton r = (RadioButton) holder.radioGroup.getChildAt(1);
//            r.setChecked(true);
            holder.radioGroup.check(R.id.rb_two);
        }else{
            holder.radioGroup.clearCheck();
        }
        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                Integer pos = (Integer) group.getTag();
                Record record = records.get(pos);
                if(checkedId == R.id.rb_one){//√
                    record.setValueChar("T");
                }else if(checkedId == R.id.rb_two){//×
                    record.setValueChar("F");
                }
                record.setPatrolRecordDate(System.currentTimeMillis());
            }
        });
        holder.radioGroup.setTag(new Integer(position));
        final TaskViewHolder1 finalHolder = holder;
        holder.etDegree.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    finalHolder.etDegree.addTextChangedListener(new TextWatcher(){
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }
                        @Override
                        public void afterTextChanged(Editable s) {
                            String etNumber = finalHolder.etDegree.getText().toString();
                            Record record = records.get(position);
                            if(isDouble(etNumber)) {
//                                finalHolder.etDegree.setTextColor(Color.red(R.color.black));
                                record.setValueFloat(Float.parseFloat(etNumber));
                            }
//                            else{
//                                finalHolder.etDegree.setTextColor(Color.red(R.color.red));
//                                finalHolder.etDegree.setText("");
//                                Log.i("afterTextChanged", "afterTextChanged: ");
//                            }
                        }
                    });
                }else {
                    finalHolder.etDegree.clearTextChangedListeners();
                }
            }
        });
        return convertView;
    }
    //判断浮点数（double和float）
    private boolean isDouble(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }
    boolean isAllItemEnable=false;
    @Override
    public boolean isEnabled(int position){
        return isAllItemEnable;
    }

    public class TaskViewHolder1 {
        @BindView(R.id.tv_content)
        public TextView textContent;//巡视内容
        @BindView(R.id.rg_radioGroup)
        public RadioGroup radioGroup;//选择项
        @BindView(R.id.tv_degree)
        public TextView tvDegree;//输入项提示
        @BindView(R.id.et_degree)
        public ExtendedEditText etDegree;//输入项
        @BindView(R.id.ll_degree)
        public LinearLayout llDegree;//

        public TaskViewHolder1(View view){
            ButterKnife.bind(this,view);
        }
    }

}
