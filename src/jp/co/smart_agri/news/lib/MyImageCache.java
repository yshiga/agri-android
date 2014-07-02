package jp.co.smart_agri.news.lib;

import android.graphics.Bitmap;
import android.util.LruCache;
import com.android.volley.toolbox.ImageLoader.ImageCache;

public class MyImageCache implements ImageCache {

	private LruCache<String, Bitmap> mCache;

	public MyImageCache() {
		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		int cacheSize = maxMemory / 8; // 最大メモリに依存

		mCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// 使用キャッシュサイズ(KB単位)
				return bitmap.getByteCount() / 1024;
			}
		};
	}

	// ImageCacheのインターフェイス実装
	@Override
	public Bitmap getBitmap(String url) {
		return mCache.get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		mCache.put(url, bitmap);
	}
}