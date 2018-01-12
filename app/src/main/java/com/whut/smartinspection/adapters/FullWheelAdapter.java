package com.whut.smartinspection.adapters;

import android.content.Context;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.whut.smartinspection.R;
import com.whut.smartinspection.activity.FullInspectActivity;
import com.whut.smartinspection.application.SApplication;
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
            if(!SApplication.isPad(mContext)){
                ListView.LayoutParams params = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,270);//设置宽度和高度
                convertView.setLayoutParams(params);
            }
//            convertView.setMinimumHeight(100);
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
            holder.radioGroup3.setVisibility(View.GONE);
        }else if("1".equals(item.getPatrolContentTypeNo())){
            holder.radioGroup.setVisibility(View.GONE);
            holder.llDegree.setVisibility(View.VISIBLE);
            holder.radioGroup3.setVisibility(View.GONE);
        }else if("3".equals(item.getPatrolContentTypeNo())){
            holder.radioGroup.setVisibility(View.GONE);
            holder.llDegree.setVisibility(View.GONE);
            holder.radioGroup3.setVisibility(View.VISIBLE);
            if(holder.radioGroup3.getChildCount()<1) {
                try {
                    String unit = item.getUnit();
                    String[] units = unit.split(",");
                    for (int i = 0; i < units.length; i++) {
                        RadioButton tempButton = new RadioButton(mContext);
                        tempButton.setText(units[i]);
                        tempButton.setTextSize(20);
                        tempButton.setId(i);
                        holder.radioGroup3.addView(tempButton, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    }
                } catch (Exception e) {
                }
            }
        }
        holder.textContent.setText(item.getNo()+"."+item.getContent());
        holder.tvDegree.setText(item.getPatrolContentName());
        if(-1== record.getValueFloat()) {
            holder.etDegree.setText("");
        } else{
            holder.etDegree.setText(String.valueOf(record.getValueFloat()));
        }
        holder.etDegree.setSelection(holder.etDegree.getText().toString().length());//让光标在文本末
        holder.tvUnit.setText(item.getUnit());
        holder.radioGroup.setOnCheckedChangeListener(null);//防止串行混乱
        holder.radioGroup.clearCheck();
        if("T".equals(record.getValueChar())){
            holder.radioGroup.check(R.id.rb_one);
        }else if("F".equals(record.getValueChar())){
            holder.radioGroup.check(R.id.rb_two);
        }else{
            holder.radioGroup.clearCheck();
        }
        //如果已有值 需设置显示
        holder.radioGroup3.setOnCheckedChangeListener(null);//防止串行混乱
        holder.radioGroup3.clearCheck();
        if("0".equals(record.getValueChar())){
            RadioButton r0 = (RadioButton)holder.radioGroup3.getChildAt(0);
            r0.setChecked(true);
        }else if ("1".equals(record.getValueChar())){
            RadioButton r1 = (RadioButton)holder.radioGroup3.getChildAt(1);
            r1.setChecked(true);
        }else{//无值则清空
            holder.radioGroup3.clearCheck();
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
        final TaskViewHolder1 finalHolder = holder;
        holder.radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                Integer pos = (Integer) group.getTag();
                Record record = records.get(pos);
                int i = finalHolder.radioGroup3.getCheckedRadioButtonId();
                String s = ""+i;
                record.setValueChar(s);
                record.setPatrolRecordDate(System.currentTimeMillis());
            }
        });
        holder.radioGroup.setTag(new Integer(position));
        holder.radioGroup3.setTag(new Integer(position));
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
                                record.setValueFloat(Float.parseFloat(etNumber));
                            }
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
        @BindView(R.id.rg_radioGroup3)
        public RadioGroup radioGroup3;//两个以上的选择项
        @BindView(R.id.tv_degree)
        public TextView tvDegree;//输入项提示
        @BindView(R.id.et_degree)
        public ExtendedEditText etDegree;//输入项
        @BindView(R.id.ll_degree)
        public LinearLayout llDegree;//
        @BindView(R.id.unit)
        TextView tvUnit;

        public TaskViewHolder1(View view){
            ButterKnife.bind(this,view);
        }
    }

}
