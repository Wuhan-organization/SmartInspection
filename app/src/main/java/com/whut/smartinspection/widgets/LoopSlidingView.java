package com.whut.smartinspection.widgets;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.whut.smartinspection.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiongbin
 * @date 2015-07-08
 */
public class LoopSlidingView extends RelativeLayout {

	private ViewPager mViewPager;
	private MyPagerAdapter mPagerAdapter;
	private OnItemClickListenr mOnItemClickListenr;
	private List<ImageView> views = new ArrayList<ImageView>();
	private List<View> viewDots = new ArrayList<View>();
	private List<String> urlList;
	private int currIndex = 0;//正播放的图片位置
	private boolean turning;
	private long autoTurningTime;
	
	private LinearLayout bottomLayout;
	private Context mContext;
	
	private int pace;
	private Handler mHandler = new Handler();
	private Runnable mRunnable = new Runnable() {
		
		@Override
		public void run() {
			if (views.size() <= 1) {
				return;
			}
			if (currIndex == 0) {
				pace = 1;
			}
			if (currIndex == views.size() - 1) {
				pace = -1;
			}
			currIndex += pace;
			mViewPager.setCurrentItem(currIndex);
			setViewDotsBackground();
			mHandler.postDelayed(mRunnable, autoTurningTime);
		}
	};

	public LoopSlidingView(Context context) {
		this(context, null);
	}

	public LoopSlidingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mViewPager = new ViewPager(context);
		LayoutParams params1 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		addView(mViewPager, params1);
		
		LayoutParams params2 = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params2.addRule(RelativeLayout.CENTER_HORIZONTAL);
		//params.rightMargin = 10;
		params2.bottomMargin = 10;
		bottomLayout = new LinearLayout(context);
		addView(bottomLayout, params2);
	}
	
	/****
	 * 添加需要轮播的图片集合
	 * @param data
	 */
	public void setImage(List<String> data){
		if (data == null || data.size() == 0) {
			return;
		}
		urlList = data;
		views.clear();
		viewDots.clear();
		bottomLayout.removeAllViews();
		LayoutParams params = new LayoutParams(20,20);
		params.rightMargin = 10;
		params.leftMargin = 10;
		
		for(int i = 0;i < data.size();i++){
			ImageViewWithPosition view = new ImageViewWithPosition(mContext);
			view.setPosition(i);
			Glide.with(mContext).load(data.get(i))
					.placeholder(R.drawable.ic_launcher)
					.error(R.drawable.ic_launcher)
					.into(view);
			views.add(view);
			View viewDot = new View(mContext);
			if (i == 0) {
				viewDot.setBackgroundResource(R.drawable.icon_gallery_point_white);
			} else {
				viewDot.setBackgroundResource(R.drawable.icon_gallery_point_grey);
			}
			viewDots.add(viewDot);
			bottomLayout.addView(viewDot,params);
		}		
		mPagerAdapter = new MyPagerAdapter(views);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mViewPager.setCurrentItem(currIndex);
	}

	/***
	 * 轮播图片被点击的监听器设置
	 * @param mOnItemClickListenr
	 */
	public void setmOnItemClickListenr(OnItemClickListenr mOnItemClickListenr) {
		this.mOnItemClickListenr = mOnItemClickListenr;
	}

	/***
	 * 开始轮播
	 * @param autoTurningTime
	 * 						轮播转换时间
	 */
	public void startTurning(long autoTurningTime){
		if (turning) {
			stopTurning();
		}
		this.autoTurningTime = autoTurningTime;
		turning = true;
		mHandler.postDelayed(mRunnable, autoTurningTime);// 开启定时轮播
	}

	/****
	 * 停止轮播
	 */
	public void stopTurning(){
		turning = false;
		mHandler.removeCallbacks(mRunnable);
	}
	
	/****
	 * 设置导航点背景颜色
	 */
	private void setViewDotsBackground(){
		for (int i = 0; i < viewDots.size(); i++) {
			if (i == currIndex) {
				viewDots.get(i).setBackgroundResource(R.drawable.icon_gallery_point_white);
			} else {
				viewDots.get(i).setBackgroundResource(R.drawable.icon_gallery_point_grey);
			}
		}
	}
	
	private class MyPagerAdapter extends PagerAdapter {
		private List<? extends View> views;

		public MyPagerAdapter(List<? extends View> views) {
			this.views = views;
		}

		@Override
		public int getCount() {
			return views.size();
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view = (ImageView) views.get(position);
			view.setScaleType(ScaleType.FIT_XY);//使图片不按比例扩大/缩小到view的大小
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			container.addView(view, 0, params);
			view.setOnClickListener(MyOnClickedListener.getInstance(mOnItemClickListenr));
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(views.get(position));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
	}
	
	private class MyOnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			currIndex = arg0;
			setViewDotsBackground();
		}
		
	}
	
	
	/***
	 * @author xiongbin
	 * 单例监听器
	 */
	private static class MyOnClickedListener implements OnClickListener{
		private static MyOnClickedListener myOnClickedListener;
		private OnItemClickListenr mOnItemClickListenr;
		
		private MyOnClickedListener(OnItemClickListenr mOnItemClickListenr){
			this.mOnItemClickListenr = mOnItemClickListenr;
		}
		
		public static MyOnClickedListener getInstance(OnItemClickListenr mOnItemClickListenr){
			if (myOnClickedListener == null) {
				myOnClickedListener = new MyOnClickedListener(mOnItemClickListenr);
			}
			return myOnClickedListener;
		}
		
		@Override
		public void onClick(View v) {
			if (v instanceof ImageViewWithPosition) {
				ImageViewWithPosition iv = (ImageViewWithPosition) v;
				if (mOnItemClickListenr != null) {
					mOnItemClickListenr.onItemClick(iv.getPosition());
				}
			}
		}
		
	}
	
	/***
	 * banner每项被点击
	 * @author xiongbin
	 */
	public interface OnItemClickListenr{
		public void onItemClick(int positon);
	}
	
	private class ImageViewWithPosition extends ImageView{
		private int position;

		public ImageViewWithPosition(Context context, AttributeSet attrs,
				int defStyle) {
			super(context, attrs, defStyle);
		}

		public ImageViewWithPosition(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		public ImageViewWithPosition(Context context) {
			super(context);
		}

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}
		
	}

	
}
