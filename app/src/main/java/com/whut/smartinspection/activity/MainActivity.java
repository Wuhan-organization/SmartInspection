package com.whut.smartinspection.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

import com.whut.smartinspection.R;
import com.whut.smartinspection.adapters.MainPageMenuAdapter;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends Activity {

	// private DisplayImageOptions options;

	private MainPageMenuAdapter mMainPageMenuAdapter;
	private List<MainPageMenuAdapter.MainPageMenu> menus = new ArrayList<MainPageMenuAdapter.MainPageMenu>();

	@BindView(R.id.gd_main_page_menu)
	GridView gdMainPageMenu;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
	}

	private void initView() {
//		options = new DisplayImageOptions.Builder()
//				.resetViewBeforeLoading(false).cacheInMemory(true)
//				.cacheOnDisk(true).considerExifParams(true)
//				.imageScaleType(ImageScaleType.NONE)
//				.bitmapConfig(Bitmap.Config.ARGB_8888).build();
		mMainPageMenuAdapter = new MainPageMenuAdapter(this, menus);
		gdMainPageMenu.setAdapter(mMainPageMenuAdapter);

		// OkHttpUtils.get().
	}

	@Override
	protected void onResume() {
		super.onResume();
		showMenu();
	}

	/***
	 * 显示主页菜单
	 * 
	 * @param utils
	 */
	private void showMenu() {
		menus.clear();
		for (int i = 0; i < 10; i++) {
			MainPageMenuAdapter.MainPageMenu menu = new MainPageMenuAdapter.MainPageMenu();
			menu.setMenuName("菜单" + i);
			menu.setImageUrl("http://blog.csdn.net/turkeyzhou/article/details/5602620");
			menus.add(menu);
		}
		mMainPageMenuAdapter.notifyDataSetChanged();
	}
}
