package com.whut.smartinspection.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.whut.smartinspection.R;
import com.whut.smartinspection.adapters.MainPageMenuAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DefectManagActivity extends Activity {

    @BindView(R.id.gv_defect_manag_menu)
    GridView gvDefectManagMenu; // 宫格菜单

    private MainPageMenuAdapter mMainPageMenuAdapter;
    private List<MainPageMenuAdapter.MainPageMenu> menus = new ArrayList<MainPageMenuAdapter.MainPageMenu>();

    private String[] menusText = {"缺陷登记", "缺陷审核", "缺陷处理","缺陷验收","历史缺陷"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defect_manag);
        ButterKnife.bind(this);

        initView();
        initData();


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
    private void initView(){
        mMainPageMenuAdapter = new MainPageMenuAdapter(this, menus);
        gvDefectManagMenu.setAdapter(mMainPageMenuAdapter);
        gvDefectManagMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String menuName = menusText[position];
                Intent intent;
                switch (position) {
                    // 缺陷登记
                    case 0:
                        intent = new Intent(DefectManagActivity.this, DefectRegisterActivity.class);
                        startActivity(intent);
                        break;

                    // 缺陷审核
                    case 1:
                        intent = new Intent(DefectManagActivity.this, DefectExamineActivity.class);
                        startActivity(intent);
                        break;
                    // 缺陷处理
                    case 2:
                        intent = new Intent(DefectManagActivity.this, DefectHandleActivity.class);
                        startActivity(intent);
                        break;
                    //缺陷验收
                    case 3:
                        intent = new Intent(DefectManagActivity.this,DefectAcceptanceActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @OnClick({R.id.tv_defect_manag_back})
    public void onClick(View view) {
        switch (view.getId()) {
            // 返回
            case R.id.tv_defect_manag_back:
                finish();
                break;
            default:
                break;
        }
    }
}
