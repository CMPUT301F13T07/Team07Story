package com.example.proto01;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OptionViewActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Create a layout for the Views in side the layout.
		LayoutParams params = 
				new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
		
		// LinearLayout. For decision to why this is chosen see module documentation
		// above.
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		
		TextView tv = new TextView(this);
		tv.setText("This is a TextView");
		tv.setLayoutParams(params);
		
		Button btn = new Button(this);
		btn.setText("this is a button");
		btn.setLayoutParams(params);
		
		layout.addView(tv);
		
		layout.addView(btn);
		
		LinearLayout.LayoutParams layoutParam = 
				new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT );
		
		this.addContentView(layout, layoutParam);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.option_view, menu);
		return true;
	}
}
