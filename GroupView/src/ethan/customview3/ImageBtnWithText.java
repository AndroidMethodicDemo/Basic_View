package ethan.customview3;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageBtnWithText extends LinearLayout {

	private ImageButton mBtn;
	private TextView mTv;

	public ImageBtnWithText(Context context) {
		super(context);
	}

	public ImageBtnWithText(Context context, AttributeSet attrs) {
		super(context, attrs);
		View view = LayoutInflater.from(context).inflate(R.layout.imagebtn_with_text, this, true);
		mTv = (TextView) view.findViewById(R.id.tvImageBtnWithText);
		mBtn = (ImageButton) view.findViewById(R.id.imageBtnImageBtnWithText);
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageBtnWithText);
		CharSequence text = a.getText(R.styleable.ImageBtnWithText_android_text);
		if(text!=null) mTv.setText(text);
		Drawable drawable = a.getDrawable(R.styleable.ImageBtnWithText_android_src);
		if(drawable != null) mBtn.setImageDrawable(drawable);
		a.recycle();
	}

	public void setImageResource(int resId) {
		mBtn.setImageResource(resId);
	}

	public void setText(String text) {
		mTv.setText(text);
	}

}
