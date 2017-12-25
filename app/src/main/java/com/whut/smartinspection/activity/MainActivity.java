package com.whut.smartinspection.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.whut.baidu.location.LocationService;
import com.whut.smartinspection.R;
import com.whut.smartinspection.adapters.MainPageMenuAdapter;
import com.whut.smartinspection.services.HttpService;
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

    private String[] menusText = {"变电巡视", "倒闸操作", "运维", "带电检测", "智能安全帽",
            "数据上传", "知识中心","无线门禁","设置"};
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //启动后台服务
        serviceIntent = new Intent(MainActivity.this, HttpService.class);
        startService(serviceIntent);
//        serviceIntent = new Intent();
//        serviceIntent.setAction("com.whut.smartinspection.services");
//        bindService(serviceIntent, null, Service.BIND_AUTO_CREATE);
//        startService(serviceIntent);

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
        //延迟开启蓝牙
        TimerTask task = new TimerTask(){
            public void run(){
                //execute the task
                openBT();//打开蓝牙
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 200);

        showMenu();
        showBanner();

    }
    private void openBT(){
        //请求打开蓝牙
//        Intent intent =new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//        startActivityForResult(intent,1);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.enable();//强制开启蓝牙
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
//            menu.setImageUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508997652313&di=0029fd23a3dd0e88b49babc5cada2181&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F019f9c5542b8fc0000019ae980d080.jpg%401280w_1l_2o_100sh.jpg");
            switch (i){
                case 0:
                    menu.setImageLocal(R.drawable.bian_dian);
                    break;
                case 1:
                    menu.setImageLocal(R.drawable.dao_zha);
                    break;
                case 2:
                    menu.setImageLocal(R.drawable.yun_wei);
                    break;
                case 3:
                    menu.setImageLocal(R.drawable.patrol);
                    break;
                case 4:
                    menu.setImageLocal(R.drawable.excel_safe_cat);
                    break;
                case 5:
                    menu.setImageLocal(R.drawable.data_up_load);
                    break;
                case 6:
                    menu.setImageLocal(R.drawable.knowledge_center);
                    break;
                case 7:
                    menu.setImageLocal(R.drawable.wireless_door_ban1);
                    break;
                case 8:
                    menu.setImageLocal(R.drawable.system_setting1);
                    break;
                default:
                    break;
            }

            menus.add(menu);
        }
        mMainPageMenuAdapter.notifyDataSetChanged();
    }

    private void showBanner() {
        List<Integer> urlList = new ArrayList<>();
//        for (int i = 0;i < 2;i++) {
//            urlList.add("http://img.taopic.com/uploads/allimg/120727/201995-120HG1030762.jpg");
            urlList.add(R.drawable.oop1);
            urlList.add(R.drawable.oop2);
//        }
        loopSlidingView.setImageOverride(urlList);
        loopSlidingView.startTurning(4000);
    }
}
