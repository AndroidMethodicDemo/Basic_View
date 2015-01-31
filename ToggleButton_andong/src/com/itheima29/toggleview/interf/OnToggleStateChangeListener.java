package com.itheima29.toggleview.interf;

/**
 * @author andong
 * 当开关状态改变的监听事件
 */
public interface OnToggleStateChangeListener {

	/**
	 * 当开关状态改变时回调此方法
	 * @param state true 打开 , false 关闭
	 */
	void onToggleStateChange(boolean state);
}
