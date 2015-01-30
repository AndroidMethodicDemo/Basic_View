package ethan.customview3;

import android.app.Activity;
import android.os.Bundle;

public class CustomView3Activity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ImageBtnWithText ibwt = (ImageBtnWithText) findViewById(R.id.imageBtnWithText);
        /*ibwt.setImageResource(R.drawable.icon);
        ibwt.setText("组合控件");*/
    }
}