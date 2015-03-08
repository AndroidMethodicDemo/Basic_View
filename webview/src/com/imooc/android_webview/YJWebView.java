package com.imooc.android_webview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class YJWebView extends WebView {

	public YJWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		bindCallback();
	}
	
	/**
	 * 基本设置
	 */
	private void init(){
		WebSettings settings = getSettings();
		//设置缩放
		settings.setSupportZoom(true);
		//设置script支持
		settings.setJavaScriptEnabled(true);
		//设置缓存
		settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
	}
	private void bindCallback(){
		//设置在此webView打开url
		setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		//设置进度对话框
		setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if(newProgress!=100){
					openProgressDialog();
				}else{
					closeProgressDialog();
				}
			}

			private void closeProgressDialog() {
				
			}

			private void openProgressDialog() {
				
			}
		});
	}
	
	public void goBackCustom(){
		if(canGoBack()){
			goBack();
		}
	}

}
