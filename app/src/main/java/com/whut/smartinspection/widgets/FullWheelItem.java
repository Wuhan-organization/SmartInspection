package com.whut.smartinspection.widgets;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wx.wheelview.common.WheelConstants;
import com.wx.wheelview.util.WheelUtils;

/**
 * Created by Fortuner on 2018/1/1.
 */

public class FullWheelItem extends FrameLayout{
    private TextView tvTitle;
    private RadioGroup radioGroup;
    private EditText editText;

    public FullWheelItem(@NonNull Context context) {
        super(context);
        init();
    }

    public FullWheelItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FullWheelItem(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
        LinearLayout layout = new LinearLayout(getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, WheelUtils.dip2px(getContext(),
                WheelConstants
                        .WHEEL_ITEM_HEIGHT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setPadding(WheelConstants.WHEEL_ITEM_PADDING, WheelConstants.WHEEL_ITEM_PADDING, WheelConstants
                .WHEEL_ITEM_PADDING, WheelConstants.WHEEL_ITEM_PADDING);
        layout.setGravity(Gravity.CENTER);
        addView(layout, layoutParams);
        //文本标题
        tvTitle = new TextView(getContext());
        tvTitle.setTag(WheelConstants.WHEEL_ITEM_TEXT_TAG);
        tvTitle.setEllipsize(TextUtils.TruncateAt.END);
        tvTitle.setSingleLine();
        tvTitle.setIncludeFontPadding(false);
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setTextColor(Color.BLACK);
        LayoutParams textParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layout.addView(tvTitle, textParams);
        //文本输入
        editText = new EditText(getContext());

    }
    public void setTvTitle(String string) {
        tvTitle.setText(string);
    }

    public String getEditText() {
        return editText.getText().toString();
    }

    public void setEditText(String string) {
        editText.setText(string);
    }
}
