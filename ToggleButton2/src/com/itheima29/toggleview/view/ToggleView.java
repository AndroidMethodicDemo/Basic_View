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
	
	private Bitmap switchBackground;		// �������صı���ͼƬ
	private Bitmap slideButtonBackground;		// ������ı���ͼƬ

	private boolean toggleState = false;		// ���ص�״̬, Ĭ��ֵΪ: �ر�
	private boolean isSliding = false;		// �Ƿ����ڻ�����
	private int currentX;
	
	private OnToggleStateChangeListener mOnToggleStateChangeListener;
	
	/**
	 * ʵ�ֿ����ڲ����ļ��������ؼ��Ĺ��캯��
	 * @param context
	 * @param attrs
	 */
	public ToggleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// ȡ���Զ�������
		
		int count = attrs.getAttributeCount();
		System.out.println("���е����Ը���: " + count);
		
		// �����Լ���(AttributeSet)�����������Զ�������(ToggleView)��ȡ����
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ToggleView);
		
		// ���Զ������������е��������ܸ���ȡ����.
		int indexCount = ta.getIndexCount();
		System.out.println("�Զ������Եĸ���: " + indexCount);
		
		for (int i = 0; i < indexCount; i++) {
			int index = ta.getIndex(i); 	// ��ȡ��i�����Ե����Զ������������е�����
			
			switch (index) {
			case R.styleable.ToggleView_slideButtonBackground: 		// ��ǰ�ǻ�����ı���
				int slideButtonBackgroundID = ta.getResourceId(index, -1);
				setSlideButtonBackgroundID(slideButtonBackgroundID);
				break;
			case R.styleable.ToggleView_switchBackground: 		// ��ǰ�ǻ������ر���������
				int switchBackgroundID = ta.getResourceId(index, -1);
				setSwitchBackgroundID(switchBackgroundID);
				break;
			case R.styleable.ToggleView_toggleState: 		// ��ǰ�ǿ��ص�״̬����
				toggleState = ta.getBoolean(index, false);
				break;
			default:
				break;
			}
		}
		
	}

	/**
	 * ���ÿ��صı���ͼƬ
	 * @param switchBackground
	 */
	public void setSwitchBackgroundID(int switchBackgroundID) {
		switchBackground = BitmapFactory.decodeResource(getResources(), switchBackgroundID);
	}

	/**
	 * ���û�����ı���ͼƬid
	 * @param slideButtonBackground
	 */
	public void setSlideButtonBackgroundID(int slideButtonBackgroundID) {
		this.slideButtonBackground = BitmapFactory.decodeResource(getResources(), slideButtonBackgroundID);
	}

	/**
	 * ���ÿ��ص�״̬
	 * @param b true Ϊ��
	 * 			false Ϊ�ر�
	 */
	public void setToggleState(boolean b) {
		toggleState = b;
	}

	/**
	 * ���ؼ���ʼ����ʱ�ص��˷���
	 * �������������õ�ǰ�ؼ��Ŀ�Ⱥ͸߶�
	 * ��ǰ�ؼ��Ŀ�Ⱥ͸߶��Ǹ��ݻ��������Ŀ�Ⱥ͸߶����õ�
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		// ���õ�ǰ�ؼ��Ŀ�Ⱥ͸߶��Ǳ����Ŀ�Ⱥ͸߶�
		setMeasuredDimension(switchBackground.getWidth(), switchBackground.getHeight());
	}

	/**
	 * �����Ƶ�ǰ�ؼ�ʱ�ص��˷���
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// ���Ʊ���ͼƬ
		canvas.drawBitmap(switchBackground, 0, 0, null);

		// ���ƻ�����ͼƬ
		
		// �ж��Ƿ����ڻ�����. 
		if(isSliding) {
			// ������ڻ�����, ����currentX���ƻ������λ��
			
			// �ѻ���������ĵ��ƶ�����ָ���λ����
			int left = currentX - slideButtonBackground.getWidth() / 2;
			
			// ������߽���ұ߽�
			if(left < 0) {
				left = 0;
			} else if(left > switchBackground.getWidth() - slideButtonBackground.getWidth()) {
				left = switchBackground.getWidth() - slideButtonBackground.getWidth();
			}
			
			canvas.drawBitmap(slideButtonBackground, left, 0, null);
		} else {
			// û�����ڻ���, ��Ҫ����toggleStateȥ���ƻ������λ��
			if(toggleState) {
				// ���ƴ򿪵�״̬
				int left = switchBackground.getWidth() - slideButtonBackground.getWidth();
				canvas.drawBitmap(slideButtonBackground, left, 0, null);
			} else {
				// ���ƹرյ�״̬
				canvas.drawBitmap(slideButtonBackground, 0, 0, null);
			}
		}
	}

	/**
	 * ��������ťʱ�����˷���
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:	// ����
			currentX = (int) event.getX();
			isSliding = true;		// �ѵ�ǰ״̬��Ϊ: ���ڻ�����
			break;
		case MotionEvent.ACTION_MOVE:	// �ƶ�
			currentX = (int) event.getX();
			
			break;	
		case MotionEvent.ACTION_UP:	// ̧��
			currentX = (int) event.getX();
			isSliding = false;		// �ѵ�ǰ״̬��Ϊ: ֹͣ����
			
			// ����toggleState����
			boolean state = currentX > switchBackground.getWidth() / 2;
			
			// �����û��Ļص��¼�
			if(state != toggleState 
					&& mOnToggleStateChangeListener != null) {	// �����ǰ����״̬��ǰһ��״̬��һ�����Ҽ�������Ϊnull, ���ûص�����
				mOnToggleStateChangeListener.onToggleStateChange(state);
			}
			
			toggleState = state;
			break;
		default:
			break;
		}
		invalidate();		// �����onDraw����
		return true;
	}
	
	/**
	 * ���õ�ǰ����״̬�ı�ļ����¼�
	 * @param listener
	 */
	public void setOnToggleStateChangeListener(OnToggleStateChangeListener listener) {
		mOnToggleStateChangeListener = listener;
	}
}
