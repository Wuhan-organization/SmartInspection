package com.whut.smartinspection.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.whut.smartinspection.R;
import com.whut.smartlibrary.base.SwipeBackActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 全面巡视页面
 * Created by xiongbin on 2017/11/2.
 */
public class FullInspectionActivity extends SwipeBackActivity {

    @BindView(R.id.tv_full_inspection_back)
    TextView tvFullInspectionBack; // 返回

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_inspection);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_full_inspection_back})
    public void onClick(View view) {
        switch (view.getId()) {
            // 返回
            case R.id.tv_full_inspection_back:
                finish();
                break;
            default:
                break;
        }
    }
}
