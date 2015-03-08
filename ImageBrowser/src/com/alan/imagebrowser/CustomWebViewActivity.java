package com.alan.imagebrowser;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CustomWebViewActivity extends Activity {

	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		mWebView = (WebView) findViewById(R.id.my_web);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.addJavascriptInterface(new JavascriptInterface(this),
				"imagelistener");
		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onLoadResource(WebView view, String url) {
				super.onLoadResource(view, url);
				// 监听器加载这是为了防止动态加载图片时新加载的图片无法预览
				addImageClickListener();
			}
		});
		mWebView.loadUrl("http://m.baidu.com/img?tn=bdidxiphone&itj=41&ssid=0&from=844b&bd_page_type=1&uid=0&pu=sz%401320_1001%2Cta%40iphone_2_4.0_3_534");
	}

	// js通信接口
	public class JavascriptInterface {

		private Context context;

		public JavascriptInterface(Context context) {
			this.context = context;
		}

		public void openImage(String object, int position) {
			Intent intent = new Intent();
			intent.putExtra(ShowWebImageActivity.IMAGE_URLS, object);
			intent.putExtra(ShowWebImageActivity.POSITION, position);
			intent.setClass(context, ShowWebImageActivity.class);
			context.startActivity(intent);
		}
	}

	// 注入js函数监听
	private void addImageClickListener() {
		// 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
		String imageloadJS = getFromAssets("imageload.js");
		if (!TextUtils.isEmpty(imageloadJS)) {
			mWebView.loadUrl(imageloadJS);
		}
	}

	// 读取assets中的文件
	private String getFromAssets(String fileName) {
		try {
			InputStreamReader inputReader = new InputStreamReader(
					getResources().getAssets().open(fileName));
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line = "";
			String Result = "";
			while ((line = bufReader.readLine()) != null)
				Result += line;
			return Result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mWebView != null) {
			mWebView.destroy();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()
				&& event.getRepeatCount() == 0) {
			/* 用户按返回键,如果可返回则回退 */
			mWebView.goBack();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
