package com.imooc.android_webview;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class ACT_WebView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_webview);
		WebView web = (WebView) findViewById(R.id.web);
		web.loadUrl("http://xw.qq.com/m/shijiebei/index.htm");
	}
}
