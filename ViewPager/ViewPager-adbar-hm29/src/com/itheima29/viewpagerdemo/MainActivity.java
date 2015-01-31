package com.itheima29.viewpagerdemo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class MainActivity extends Activity implements OnPageChangeListener {

	private static final String TAG = MainActivity.class.getSimpleName();

    private List<ImageView> imageViewList;
	private ViewPager mViewPager;
	private TextView tvImageDescription;
	private LinearLayout llPointGroup;
	private String[] imageDescriptionArrays;
	
	private int previousPosition = 0;	// 前一个被选中页面的position
	
	private boolean isStop = false;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        init();
        
        new Thread(new Runnable(){
        	@Override
        	public void run() {
        		while(!isStop) {
        			System.out.println("切换下一个页面");
	        		SystemClock.sleep(2000);
	        		
	        		runOnUiThread(new Runnable() {
	        			@Override
	        			public void run() {
	        				int newCurrentPosition = mViewPager.getCurrentItem() + 1;
	        				mViewPager.setCurrentItem(newCurrentPosition);
	        			}
	        		});
        		}
        	}
        }).start();
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isStop = true;
	}

	private void init() {
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		tvImageDescription = (TextView) findViewById(R.id.tv_image_descriptioin);
		llPointGroup = (LinearLayout) findViewById(R.id.ll_point_group);
		
		// 初始化图片和描述信息等.
		int[] imageResIDs = {
				R.drawable.a,
				R.drawable.b,
				R.drawable.c,
				R.drawable.d,
				R.drawable.e
		};
		
		imageDescriptionArrays = new String[] {
				"巩俐不低俗，我就不能低俗",
				"扑树又回来啦！再唱经典老歌引万人大合唱",
				"揭秘北京电影如何升级",
				"乐视网TV版大派送",
				"热血屌丝的反杀"
		};
		
		imageViewList = new ArrayList<ImageView>();
		
		ImageView iv;
		View view;
		for (int i = 0; i < imageResIDs.length; i++) {
			iv = new ImageView(this);
			iv.setBackgroundResource(imageResIDs[i]);
			imageViewList.add(iv);
			
			// 每次循环添加一个点
			view = new View(this);
			view.setBackgroundResource(R.drawable.point_background);
			LayoutParams params = new LayoutParams(5, 5);
			params.leftMargin = 5;
			view.setLayoutParams(params);
			llPointGroup.addView(view);
		}
		
		// 把适配器和viewpager控件关联起来. 
		mViewPager.setAdapter(new MyPagerAdapter());
		mViewPager.setOnPageChangeListener(this);
		
		// 初始化图片描述信息和点的选中状态
		tvImageDescription.setText(imageDescriptionArrays[0]);
		llPointGroup.getChildAt(0).setEnabled(false);
		
		// 设置初始被选中的item是 2147483647 / 2 % 5  = 3;
//		int currentItem = Integer.MAX_VALUE / 2 - ((Integer.MAX_VALUE / 2) % imageViewList.size());
		int currentItem = Integer.MAX_VALUE / 2 - 3;
		mViewPager.setCurrentItem(currentItem);	// 设置当前显示的item页面是0
	}
    
	class MyPagerAdapter extends PagerAdapter {


		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		/**
		 * 返回true代表复用view对象
		 * 官方建议写法 return view == obj;
		 */
		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}

		/**
		 * 销毁多余的item
		 * position就是将要被销毁的item的索引
		 */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
//			super.destroyItem(container, position, object);	// 父类要求必须实现此方法. 不实现会抛异常.
			Log.i(TAG, "destroyItem position:"+position+";position % imageViewList.size()"+position % imageViewList.size());
//			Log.i(TAG, "container.equals(mViewPager):"+container.equals(mViewPager));
//			Log.i(TAG, ";object.equals(imageViewList.get(position% imageViewList.size())):"+object.equals(imageViewList.get(position% imageViewList.size())));
			mViewPager.removeView(imageViewList.get(position % imageViewList.size()));
		}

		/**
		 * 当需要加载item时出发此方法
		 * position 就是将要被加载的item的索引
		 */
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			int currentItem = mViewPager.getCurrentItem();
			Log.v(TAG, "currentItem:"+currentItem);
			Log.d(TAG, "instantiateItem position:"+position+";position % imageViewList.size()"+position % imageViewList.size());
			mViewPager.addView(imageViewList.get(position % imageViewList.size()));
			return imageViewList.get(position % imageViewList.size());
		}
	}

	/**
	 * 当页面滚动状态改变时
	 */
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	/**
	 * 当页面滚动时
	 */
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	/**
	 * 当页面被选中时出发此方法
	 */
	@Override
	public void onPageSelected(int position) {
		int newPosition = position % imageViewList.size();
		
		// 切换到对应的描述信息, 和选中 点
		tvImageDescription.setText(imageDescriptionArrays[newPosition]);
		llPointGroup.getChildAt(previousPosition).setEnabled(true);
		llPointGroup.getChildAt(newPosition).setEnabled(false);
		
		previousPosition = newPosition;
	}
}
