package com.whut.smartinspection.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.whut.smartinspection.R;
import com.whut.smartinspection.adapters.MainPageMenuAdapter;
import com.whut.smartlibrary.base.SwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xiongbin on 2017/10/30.
 */
public class PowerStationCheckActivity extends SwipeBackActivity {
    @BindView(R.id.tv_power_station_check_back)
    TextView tvPowerStationCheckBack; // 返回

    @BindView(R.id.gv_power_station_check_menu)
    GridView gvPowerStationCheckMenu; // 宫格菜单

    private MainPageMenuAdapter mMainPageMenuAdapter;
    private List<MainPageMenuAdapter.MainPageMenu> menus = new ArrayList<MainPageMenuAdapter.MainPageMenu>();

    private String[] menusText = {"全面巡视", "例行巡视", "熄灯巡视", "专业巡视", "特殊巡视",
            "我的任务"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_station_check);
        ButterKnife.bind(this);

        initView();
        initData();
    }

    private void initView() {
        mMainPageMenuAdapter = new MainPageMenuAdapter(this, menus);
        gvPowerStationCheckMenu.setAdapter(mMainPageMenuAdapter);
        gvPowerStationCheckMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String menuName = menusText[position];
                Intent intent;
                switch (position) {
                    // 进入全面巡视页面
                    case 0:
                        intent = new Intent(PowerStationCheckActivity.this, FullInspectionActivity.class);
                        startActivity(intent);
                        break;

                    // 我的任务
                    case 5:
                        intent = new Intent(PowerStationCheckActivity.this, MyTaskActivity.class);
                        startActivity(intent);
                        break;

                    default:
                        break;
                }
            }
        });
    }

    private void initData() {
        showMenu();
    }

    /***
     * 显示主页菜单
     */
    private void showMenu() {
        menus.clear();
        for (int i = 0; i < menusText.length; i++) {
            MainPageMenuAdapter.MainPageMenu menu = new MainPageMenuAdapter.MainPageMenu();
            menu.setMenuName(menusText[i]);
            menu.setImageUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508997652313&di=0029fd23a3dd0e88b49babc5cada2181&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F019f9c5542b8fc0000019ae980d080.jpg%401280w_1l_2o_100sh.jpg");
            menus.add(menu);
        }
        mMainPageMenuAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.tv_power_station_check_back})
    public void onClick(View view) {
        switch (view.getId()) {
            // 返回
            case R.id.tv_power_station_check_back:
                finish();
                break;
            default:
                break;
        }
    }
}
