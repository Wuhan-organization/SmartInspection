package com.whut.smartinspection.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.whut.smartinspection.R;
import com.whut.smartinspection.adapters.MainPageMenuAdapter;
import com.whut.smartinspection.widgets.LoopSlidingView;
import com.whut.smartlibrary.base.SwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tw.com.a_i_t.IPCamViewer.Splash;

/***
 * 主页面
 */
public class MainActivity extends SwipeBackActivity {

    private MainPageMenuAdapter mMainPageMenuAdapter;
    private List<MainPageMenuAdapter.MainPageMenu> menus = new ArrayList<MainPageMenuAdapter.MainPageMenu>();

    @BindView(R.id.lsv_main_page)
    LoopSlidingView loopSlidingView;

    @BindView(R.id.gd_main_page_menu)
    GridView gdMainPageMenu;

    private String[] menusText = {"变电巡视", "闸刀操作", "运维", "带电检测", "智能安全帽",
            "设置", "知识中心"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
        initData();
    }

    private void initView() {
        mMainPageMenuAdapter = new MainPageMenuAdapter(this, menus);
        gdMainPageMenu.setAdapter(mMainPageMenuAdapter);
        gdMainPageMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String menuName = menusText[position];
                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(MainActivity.this, PowerStationCheckActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        break;
                    case 4:
                        intent = new Intent(MainActivity.this, Splash.class);
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
        showBanner();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loopSlidingView.startTurning(4000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        loopSlidingView.stopTurning();
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

    private void showBanner() {
        List<String> urlList = new ArrayList<String>();
        for (int i = 0;i < 5;i++) {
            urlList.add("http://img.taopic.com/uploads/allimg/120727/201995-120HG1030762.jpg");
        }
        loopSlidingView.setImage(urlList);
        loopSlidingView.startTurning(4000);
    }
}
