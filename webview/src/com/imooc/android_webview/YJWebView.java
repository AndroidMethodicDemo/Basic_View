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
	 * ��������
	 */
	private void init(){
		WebSettings settings = getSettings();
		//��������
		settings.setSupportZoom(true);
		//����script֧��
		settings.setJavaScriptEnabled(true);
		//���û���
		settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
	}
	private void bindCallback(){
		//�����ڴ�webView��url
		setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		//���ý��ȶԻ���
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
