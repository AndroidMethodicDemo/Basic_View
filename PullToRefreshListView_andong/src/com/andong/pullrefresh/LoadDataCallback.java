package com.andong.pullrefresh;

import android.widget.BaseAdapter;

/**
 * @author andong
 * RefreshListView刷新事件的监听
 */
public interface LoadDataCallback {

	/**
	 * 加载最新数据
	 */
	public BaseAdapter loadNewData();
	
	/**
	 * 加载以前的数据
	 */
	public BaseAdapter loadOldData();
}
