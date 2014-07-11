package jp.co.smart_agri.news.lib;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

public class MyImageLoader extends ImageLoader {

	public MyImageLoader(RequestQueue queue, ImageCache imageCache) {
		super(queue, imageCache);
	}

	public static ImageListener getImageListener(final ImageView view,
			final ProgressBar progressBar) {

		return new ImageListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				view.setVisibility(View.INVISIBLE);
				if (progressBar != null) {
					progressBar.setVisibility(View.GONE);
				}
			}

			@Override
			public void onResponse(ImageContainer response, boolean isImmediate) {
				if (response.getBitmap() != null) {
					view.setImageBitmap(response.getBitmap());
					if (progressBar != null) {
						progressBar.setVisibility(View.GONE);
					}
				}
			}
		};
	}
}