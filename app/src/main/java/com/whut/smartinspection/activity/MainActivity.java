package com.whut.smartinspection.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.whut.baidu.location.LocationService;
import com.whut.smartinspection.R;
import com.whut.smartinspection.adapters.MainPageMenuAdapter;
import com.whut.smartinspection.widgets.LoopSlidingView;
import com.whut.smartinspection.widgets.WrapContentGridView;
import com.whut.smartlibrary.base.SwipeBackActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import tw.com.a_i_t.IPCamViewer.HelmetActivity;

/***
 * 主页面
 */
public class MainActivity extends SwipeBackActivity {

    private MainPageMenuAdapter mMainPageMenuAdapter;
    private List<MainPageMenuAdapter.MainPageMenu> menus = new ArrayList<MainPageMenuAdapter.MainPageMenu>();

    @BindView(R.id.lsv_main_page)
    LoopSlidingView loopSlidingView;

    @BindView(R.id.gd_main_page_menu)
    WrapContentGridView gdMainPageMenu;

    private String[] menusText = {"变电巡视", "闸刀操作", "运维", "带电检测", "智能安全帽",
            "设置", "知识中心","蓝牙开锁"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
        initData();
        //延迟开启蓝牙
        TimerTask task = new TimerTask(){
            public void run(){
                //execute the task
                openBT();//打开蓝牙
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 200);

    }
    private void openBT(){
        //请求打开蓝牙
//        Intent intent =new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//        startActivityForResult(intent,1);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.enable();//强制开启蓝牙
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
                        intent = new Intent(MainActivity.this, HelmetActivity.class);
                        startActivity(intent);
                        break;
                    case 7:
                        intent = new Intent(MainActivity.this,BluetoothActivity.class);
                        startActivity(intent);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationService.getInstance(this).stopLocation();
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
