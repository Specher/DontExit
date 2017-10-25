package com.specher.stopclosing;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;


public class AboutActivity extends Activity {
	WebView wv ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_about);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		wv = (WebView) findViewById(R.id.webView1);
		 wv.getSettings().setJavaScriptEnabled(true);//可用JS  
	     wv.getSettings().setSupportZoom(true);
	     wv.getSettings().setBuiltInZoomControls(true);
	     wv.getSettings().setDisplayZoomControls(false);//隐藏Zoom缩放按钮
	     wv.setScrollBarStyle(0);//滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上
	     String url = "file:///android_asset/about.html";
	     wv.loadUrl(url);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

	
}
