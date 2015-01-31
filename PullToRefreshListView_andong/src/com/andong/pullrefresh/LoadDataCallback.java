package com.andong.pullrefresh;

import android.widget.BaseAdapter;

/**
 * @author andong
 * RefreshListViewˢ���¼��ļ���
 */
public interface LoadDataCallback {

	/**
	 * ������������
	 */
	public BaseAdapter loadNewData();
	
	/**
	 * ������ǰ������
	 */
	public BaseAdapter loadOldData();
}
