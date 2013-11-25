package edu.ualberta.adventstory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.webkit.WebView;

public class HelpActivity extends Activity {
	WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mWebView = new WebView(this);
		setContentView(mWebView);
		
		if(savedInstanceState == null)
			loadPage("index.html");
			
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		loadPage(bundle.getString("htmlPage"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help, menu);
		return true;
	}

	private void loadPage(String page) {
	    StringBuilder buf=new StringBuilder();
	    InputStream html;
		try {
			html = getAssets().open("HelpHtml/"+page);
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
	    BufferedReader in=
	        new BufferedReader(new InputStreamReader(html));
	    String str;

	    try {
			while ((str=in.readLine()) != null) {
			  buf.append(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	    try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	    
	    String htmlPage = buf.toString();
	    
		mWebView.loadDataWithBaseURL("file:///android_asset/HelpHtml/", htmlPage, "text/html", "UTF-8", null);
	}
}
