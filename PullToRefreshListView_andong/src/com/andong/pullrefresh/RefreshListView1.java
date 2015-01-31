package com.andong.pullrefresh;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RefreshListView1 extends ListView implements OnScrollListener {
	
	private int firstVisibleItem;
	private int downY;
	private int headerViewHeight;
	private View headerView;
	private ImageView ivArrow;
	private ProgressBar pbProgress;
	private TextView tvState;
	private TextView tvLastUpdateTime;
	
	private final int DOWN_PULL = 0;
	private final int RELEASE_REFRESH = 1;
	private final int REFRESHING = 2;
	
	private int currentState = DOWN_PULL;
	private RotateAnimation upAnimation;
	private RotateAnimation downAnimation;
	private LoadDataCallback mOnRefreshListener;
	private boolean isScroll2Bottom;
	private View footerView;
	private int footerViewHeight;
	private boolean isLoadingMore = false;

	public RefreshListView1(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initHeader();
		initFooter();
		
		setOnScrollListener(this);
	}

	private void initFooter() {
		footerView = View.inflate(getContext(), R.layout.listview_footer, null);
		footerView.measure(0, 0);
		
		footerViewHeight = footerView.getMeasuredHeight();
		
		footerView.setPadding(0, -footerViewHeight, 0, 0);
		
		addFooterView(footerView);
	}

	private void initHeader() {
		headerView = View.inflate(getContext(), R.layout.listview_header, null);
		ivArrow = (ImageView) headerView.findViewById(R.id.iv_listview_header_arrow);
		pbProgress = (ProgressBar) headerView.findViewById(R.id.pb_progress);
		tvState = (TextView) headerView.findViewById(R.id.tv_refresh_state);
		tvLastUpdateTime = (TextView) headerView.findViewById(R.id.tv_last_update_time);
		
		headerView.measure(0, 0);
		
		headerViewHeight = headerView.getMeasuredHeight();
		
		headerView.setPadding(0, -headerViewHeight, 0, 0);
		
		addHeaderView(headerView);
		
		initAnimation();
	}
	
	/**
	 * ��ʼ������
	 */
	private void initAnimation() {
		upAnimation = new RotateAnimation(
				0f, -180f, 
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
		upAnimation.setDuration(500);
		upAnimation.setFillAfter(true);
		
		downAnimation = new RotateAnimation(
				-180f, -360f, 
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
		downAnimation.setDuration(500);
		downAnimation.setFillAfter(true);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveY = (int) ev.getY();
			
			int diff = (moveY - downY) / 2;
			
			if(firstVisibleItem == 0 && diff > 0 && currentState != REFRESHING) {
				int paddingTop = -headerViewHeight + diff;
				
				if(paddingTop > 0 && currentState == DOWN_PULL) {
					currentState = RELEASE_REFRESH;
					refreshHeaderViewState();
				} else if(paddingTop < 0 && currentState == RELEASE_REFRESH) {
					currentState = DOWN_PULL;
					refreshHeaderViewState();
				}
				
				headerView.setPadding(0, paddingTop, 0, 0);
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			if(currentState == RELEASE_REFRESH) {
				currentState = REFRESHING;
				refreshHeaderViewState();
				headerView.setPadding(0, 0, 0, 0);
				
				if(mOnRefreshListener != null) {
					mOnRefreshListener.loadNewData();
				}
			} else if(currentState == DOWN_PULL) {
				headerView.setPadding(0, -headerViewHeight, 0, 0);
			}
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	public void onRefreshFinish() {
		if(isLoadingMore) {
			isLoadingMore = false;
			footerView.setPadding(0, -footerViewHeight, 0, 0);
		} else {
			headerView.setPadding(0, -headerViewHeight, 0, 0);
			currentState = DOWN_PULL;
			ivArrow.setVisibility(View.VISIBLE);
			pbProgress.setVisibility(View.GONE);
			tvLastUpdateTime.setText("���ˢ��ʱ��: " + getLastUpdateTime());
		}
	}

	/**
	 * ������µ�ʱ��
	 * @return
	 */
	private String getLastUpdateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
	
	/**
	 * 
	 */
	private void refreshHeaderViewState() {
		if(currentState == DOWN_PULL) {	// ����ˢ��״̬
			ivArrow.startAnimation(downAnimation);
			tvState.setText("����ˢ��");
		} else if(currentState == RELEASE_REFRESH) {	// �ͷ�ˢ��
			ivArrow.startAnimation(upAnimation);
			tvState.setText("�ɿ�ˢ��");
		} else if(currentState == REFRESHING) {	// ����ˢ��
			ivArrow.clearAnimation();
			ivArrow.setVisibility(View.GONE);
			pbProgress.setVisibility(View.VISIBLE);
			tvState.setText("����ˢ��");
		}
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(isScroll2Bottom && 
				(scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING)) {
//			System.out.println("�������ײ�");

			if(!isLoadingMore) {
				isLoadingMore = true;
				footerView.setPadding(0, 0, 0, 0);
				setSelection(getCount() -1);
				
				if(mOnRefreshListener != null) {
					mOnRefreshListener.loadOldData();
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.firstVisibleItem = firstVisibleItem;
		
		if(getLastVisiblePosition() == (getCount() -1)) {
			isScroll2Bottom = true;
		} else {
			isScroll2Bottom = false;
		}
	}

	public void setOnRefreshListener(LoadDataCallback listener) {
		this.mOnRefreshListener = listener;
	}
}
