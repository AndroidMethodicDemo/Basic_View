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
	
	private int previousPosition = 0;	// ǰһ����ѡ��ҳ���position
	
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
        			System.out.println("�л���һ��ҳ��");
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
		
		// ��ʼ��ͼƬ��������Ϣ��.
		int[] imageResIDs = {
				R.drawable.a,
				R.drawable.b,
				R.drawable.c,
				R.drawable.d,
				R.drawable.e
		};
		
		imageDescriptionArrays = new String[] {
				"���������ף��ҾͲ��ܵ���",
				"�����ֻ��������ٳ������ϸ������˴�ϳ�",
				"���ر�����Ӱ�������",
				"������TV�������",
				"��Ѫ��˿�ķ�ɱ"
		};
		
		imageViewList = new ArrayList<ImageView>();
		
		ImageView iv;
		View view;
		for (int i = 0; i < imageResIDs.length; i++) {
			iv = new ImageView(this);
			iv.setBackgroundResource(imageResIDs[i]);
			imageViewList.add(iv);
			
			// ÿ��ѭ�����һ����
			view = new View(this);
			view.setBackgroundResource(R.drawable.point_background);
			LayoutParams params = new LayoutParams(5, 5);
			params.leftMargin = 5;
			view.setLayoutParams(params);
			llPointGroup.addView(view);
		}
		
		// ����������viewpager�ؼ���������. 
		mViewPager.setAdapter(new MyPagerAdapter());
		mViewPager.setOnPageChangeListener(this);
		
		// ��ʼ��ͼƬ������Ϣ�͵��ѡ��״̬
		tvImageDescription.setText(imageDescriptionArrays[0]);
		llPointGroup.getChildAt(0).setEnabled(false);
		
		// ���ó�ʼ��ѡ�е�item�� 2147483647 / 2 % 5  = 3;
//		int currentItem = Integer.MAX_VALUE / 2 - ((Integer.MAX_VALUE / 2) % imageViewList.size());
		int currentItem = Integer.MAX_VALUE / 2 - 3;
		mViewPager.setCurrentItem(currentItem);	// ���õ�ǰ��ʾ��itemҳ����0
	}
    
	class MyPagerAdapter extends PagerAdapter {


		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		/**
		 * ����true������view����
		 * �ٷ�����д�� return view == obj;
		 */
		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}

		/**
		 * ���ٶ����item
		 * position���ǽ�Ҫ�����ٵ�item������
		 */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
//			super.destroyItem(container, position, object);	// ����Ҫ�����ʵ�ִ˷���. ��ʵ�ֻ����쳣.
			Log.i(TAG, "destroyItem position:"+position+";position % imageViewList.size()"+position % imageViewList.size());
//			Log.i(TAG, "container.equals(mViewPager):"+container.equals(mViewPager));
//			Log.i(TAG, ";object.equals(imageViewList.get(position% imageViewList.size())):"+object.equals(imageViewList.get(position% imageViewList.size())));
			mViewPager.removeView(imageViewList.get(position % imageViewList.size()));
		}

		/**
		 * ����Ҫ����itemʱ�����˷���
		 * position ���ǽ�Ҫ�����ص�item������
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
	 * ��ҳ�����״̬�ı�ʱ
	 */
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	/**
	 * ��ҳ�����ʱ
	 */
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	/**
	 * ��ҳ�汻ѡ��ʱ�����˷���
	 */
	@Override
	public void onPageSelected(int position) {
		int newPosition = position % imageViewList.size();
		
		// �л�����Ӧ��������Ϣ, ��ѡ�� ��
		tvImageDescription.setText(imageDescriptionArrays[newPosition]);
		llPointGroup.getChildAt(previousPosition).setEnabled(true);
		llPointGroup.getChildAt(newPosition).setEnabled(false);
		
		previousPosition = newPosition;
	}
}
