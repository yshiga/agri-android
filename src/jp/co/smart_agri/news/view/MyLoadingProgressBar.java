package jp.co.smart_agri.news.view;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class MyLoadingProgressBar extends ProgressBar {

	public MyLoadingProgressBar(Context context) {
		super(context);
	}

	public MyLoadingProgressBar(Context context, AttributeSet attrs){
		super(context, attrs);
	}
	
	public MyLoadingProgressBar(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}
	
	@Override
	public void setProgress(int progress) {
		super.setProgress(progress);
		if (progress == 100) {
			setVisibility(GONE);
		} else {
			setVisibility(VISIBLE);
		}
	}
}