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
		 wv.getSettings().setJavaScriptEnabled(true);//����JS  
	     wv.getSettings().setSupportZoom(true);
	     wv.getSettings().setBuiltInZoomControls(true);
	     wv.getSettings().setDisplayZoomControls(false);//����Zoom���Ű�ť
	     wv.setScrollBarStyle(0);//���������Ϊ0���ǲ������������ռ䣬��������������ҳ��
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
