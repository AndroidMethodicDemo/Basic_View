package com.example.welcomepages;

import java.util.Currency;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class GV_WelcomeViewPager extends RelativeLayout {

//	private FrameLayout root;
	private ViewPager mViewPager;
	private ImageView mPoint1;
	private ImageView mPoint2;
	private ImageView mPoint3;
	private ImageView[] mPoints;
	private ImageView mPage1;
	private ImageView mPage2;
	private ImageView mPage3;
	private ImageView[] mPages={mPage1,mPage2,mPage3};
	
	private LayoutInflater mInflater;
	private PagerAdapter myAdapter;
	private int[] mImages={R.drawable.main_005_guide_multiple_chat,R.drawable.main_005_guide_mutilple_conference,R.drawable.main_005_guide_start_chat};
	private int mCurrentIndex;
	
	private Animation animMagnify;
	private Animation animMinify;
	
	public GV_WelcomeViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews();
		bindEvents();
		initDatas();
	}
	private void initViews(){
		mInflater=LayoutInflater.from(getContext());
		mInflater.inflate(R.layout.main_gv_welcom_viewpager, this);
		mViewPager=(ViewPager) findViewById(R.id.mViewPager);
		mPoint1=(ImageView) findViewById(R.id.mPoint1);
		mPoint2=(ImageView) findViewById(R.id.mPoint2);
		mPoint3=(ImageView) findViewById(R.id.mPoint3);
		mPoints=new ImageView[]{mPoint1,mPoint2,mPoint3};
		for (int i = 0; i < mImages.length; i++) {
			mPages[i]=new ImageView(getContext());
			mPages[i].setImageResource(mImages[i]);
		}
		initPointAnim();
	}
	private void initPointAnim() {
		animMagnify=AnimationUtils.loadAnimation(getContext(), R.anim.main_anim_set_002_magnify);
		animMinify=AnimationUtils.loadAnimation(getContext(), R.anim.main_anim_set_001_minify);
		mPoints[0].startAnimation(animMagnify);
	}
	private void bindEvents(){
		mViewPager.setOnPageChangeListener(new  OnPageChangeListener(){

			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}

			@Override
			public void onPageSelected(int arg0) {
				
				setCurrentPoint(arg0);
				mCurrentIndex=arg0;
			}

			
		});
	}
	private void setCurrentPoint(int position) {
		mPoints[position].startAnimation(animMagnify);
		mPoints[position].setImageResource(R.drawable.main_005_guide_cur_point);
		mPoints[mCurrentIndex].startAnimation(animMinify);
		mPoints[mCurrentIndex].setImageResource(R.drawable.main_005_guide_point);
	}

	private void initDatas(){
		myAdapter=new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0==arg1;
			}
			
			@Override
			public int getCount() {
				return mPages.length;
			}
			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				((ViewPager)container).addView(mPages[position]);
				return mPages[position];
			}
			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				((ViewPager)container).removeView(mPages[position]);
			}
		};
		mViewPager.setAdapter(myAdapter);
	}

	
}
