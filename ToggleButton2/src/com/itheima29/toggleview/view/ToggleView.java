package com.itheima29.toggleview.view;

import com.itheima29.toggleview.R;
import com.itheima29.toggleview.interf.OnToggleStateChangeListener;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ToggleView extends View {
	
	private Bitmap switchBackground;		// 滑动开关的背景图片
	private Bitmap slideButtonBackground;		// 滑动块的背景图片

	private boolean toggleState = false;		// 开关的状态, 默认值为: 关闭
	private boolean isSliding = false;		// 是否正在滑动中
	private int currentX;
	
	private OnToggleStateChangeListener mOnToggleStateChangeListener;
	
	/**
	 * 实现可以在布局文件中声明控件的构造函数
	 * @param context
	 * @param attrs
	 */
	public ToggleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// 取出自定义属性
		
		int count = attrs.getAttributeCount();
		System.out.println("所有的属性个数: " + count);
		
		// 把属性集合(AttributeSet)中所有属于自定义属性(ToggleView)的取出来
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ToggleView);
		
		// 把自定义属性数组中的索引的总个数取出来.
		int indexCount = ta.getIndexCount();
		System.out.println("自定义属性的个数: " + indexCount);
		
		for (int i = 0; i < indexCount; i++) {
			int index = ta.getIndex(i); 	// 获取第i个属性的在自定义属性数组中的索引
			
			switch (index) {
			case R.styleable.ToggleView_slideButtonBackground: 		// 当前是滑动块的背景
				int slideButtonBackgroundID = ta.getResourceId(index, -1);
				setSlideButtonBackgroundID(slideButtonBackgroundID);
				break;
			case R.styleable.ToggleView_switchBackground: 		// 当前是滑动开关背景的属性
				int switchBackgroundID = ta.getResourceId(index, -1);
				setSwitchBackgroundID(switchBackgroundID);
				break;
			case R.styleable.ToggleView_toggleState: 		// 当前是开关的状态属性
				toggleState = ta.getBoolean(index, false);
				break;
			default:
				break;
			}
		}
		
	}

	/**
	 * 设置开关的背景图片
	 * @param switchBackground
	 */
	public void setSwitchBackgroundID(int switchBackgroundID) {
		switchBackground = BitmapFactory.decodeResource(getResources(), switchBackgroundID);
	}

	/**
	 * 设置滑动块的背景图片id
	 * @param slideButtonBackground
	 */
	public void setSlideButtonBackgroundID(int slideButtonBackgroundID) {
		this.slideButtonBackground = BitmapFactory.decodeResource(getResources(), slideButtonBackgroundID);
	}

	/**
	 * 设置开关的状态
	 * @param b true 为打开
	 * 			false 为关闭
	 */
	public void setToggleState(boolean b) {
		toggleState = b;
	}

	/**
	 * 当控件开始测量时回调此方法
	 * 我们在这里设置当前控件的宽度和高度
	 * 当前控件的宽度和高度是根据滑动背景的宽度和高度设置的
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		// 设置当前控件的宽度和高度是背景的宽度和高度
		setMeasuredDimension(switchBackground.getWidth(), switchBackground.getHeight());
	}

	/**
	 * 当绘制当前控件时回调此方法
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// 绘制背景图片
		canvas.drawBitmap(switchBackground, 0, 0, null);

		// 绘制滑动块图片
		
		// 判断是否正在滑动中. 
		if(isSliding) {
			// 如果正在滑动中, 根据currentX绘制滑动块的位置
			
			// 把滑动块的中心点移动到手指点的位置上
			int left = currentX - slideButtonBackground.getWidth() / 2;
			
			// 限制左边界和右边界
			if(left < 0) {
				left = 0;
			} else if(left > switchBackground.getWidth() - slideButtonBackground.getWidth()) {
				left = switchBackground.getWidth() - slideButtonBackground.getWidth();
			}
			
			canvas.drawBitmap(slideButtonBackground, left, 0, null);
		} else {
			// 没有正在滑动, 需要根据toggleState去绘制滑动块的位置
			if(toggleState) {
				// 绘制打开的状态
				int left = switchBackground.getWidth() - slideButtonBackground.getWidth();
				canvas.drawBitmap(slideButtonBackground, left, 0, null);
			} else {
				// 绘制关闭的状态
				canvas.drawBitmap(slideButtonBackground, 0, 0, null);
			}
		}
	}

	/**
	 * 当触摸按钮时互调此方法
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:	// 按下
			currentX = (int) event.getX();
			isSliding = true;		// 把当前状态置为: 正在滑动中
			break;
		case MotionEvent.ACTION_MOVE:	// 移动
			currentX = (int) event.getX();
			
			break;	
		case MotionEvent.ACTION_UP:	// 抬起
			currentX = (int) event.getX();
			isSliding = false;		// 把当前状态置为: 停止滑动
			
			// 重置toggleState变量
			boolean state = currentX > switchBackground.getWidth() / 2;
			
			// 调用用户的回调事件
			if(state != toggleState 
					&& mOnToggleStateChangeListener != null) {	// 如果当前开关状态和前一个状态不一样并且监听对象不为null, 调用回调方法
				mOnToggleStateChangeListener.onToggleStateChange(state);
			}
			
			toggleState = state;
			break;
		default:
			break;
		}
		invalidate();		// 会调用onDraw方法
		return true;
	}
	
	/**
	 * 设置当前开关状态改变的监听事件
	 * @param listener
	 */
	public void setOnToggleStateChangeListener(OnToggleStateChangeListener listener) {
		mOnToggleStateChangeListener = listener;
	}
}
