package com.example.textview;

import java.lang.reflect.Field;
import java.util.Random;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;

public class ACT_SpannableString extends Activity {

	private EditText editText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_spannablestring);
		editText=(EditText) findViewById(R.id.edittext);
	}
	
	public void onClick_RandomFace(View view)
	{
	            //  随机产生1至9的整数
	    int randomId = 1 + new Random().nextInt(9);
	    try
	    {
	        //  根据随机产生的1至9的整数从R.drawable类中获得相应资源ID（静态变量）的Field对象
	        Field field = R.drawable.class.getDeclaredField("face" + randomId);
	        //  获得资源ID的值，也就是静态变量的值
	        int resourceId = Integer.parseInt(field.get(null).toString());
	        //  根据资源ID获得资源图像的Bitmap对象
	        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
	        //  根据Bitmap对象创建ImageSpan对象
	        ImageSpan imageSpan = new ImageSpan(this, bitmap);
	        //  创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
	        SpannableString spannableString = new SpannableString("face");
	        //  用ImageSpan对象替换face
	        spannableString.setSpan(imageSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	        //  将随机获得的图像追加到EditText控件的最后
	        editText.append(spannableString);
	    }
	    catch (Exception e)
	    {
	    }
	}
}
