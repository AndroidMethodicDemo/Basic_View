package com.itheima29.toggleview;

import com.itheima29.toggleview.interf.OnToggleStateChangeListener;
import com.itheima29.toggleview.view.ToggleView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ToggleView mToggleView = (ToggleView) findViewById(R.id.toggleview);
        mToggleView.setSwitchBackgroundID(R.drawable.switch_background);
        mToggleView.setSlideButtonBackgroundID(R.drawable.slide_button_background);
        mToggleView.setToggleState(true);
        
        mToggleView.setOnToggleStateChangeListener(new OnToggleStateChangeListener() {
			
			@Override
			public void onToggleStateChange(boolean state) {
				if(state) {
					Toast.makeText(MainActivity.this, "开关开启", 0).show();
				} else {
					Toast.makeText(MainActivity.this, "开关关闭", 0).show();
				}
			}
		});
    }
    
}
