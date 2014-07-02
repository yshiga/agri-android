package jp.co.smart_agri.news.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import jp.co.smart_agri.news.R;
import jp.co.smart_agri.news.activity.base.BaseActivity;
import jp.co.smart_agri.news.view.MyLoadingProgressBar;

public class NewsWebViewActivity extends BaseActivity {

	public final static String EXTRAS_NEWS_URL_KEY = "extras_news_url";
	public final static String EXTRAS_NEWS_TITLE_KEY = "extras_title_url";

	private MyLoadingProgressBar mProgressBar;

	private void setupActionBar() {
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	private void setupWebView() {
		WebView webView = (WebView) findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				mProgressBar.setProgress(progress);
			}
		});
		webView.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
			}
		});

		webView.loadUrl(getNewsUrlFromIntent());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_web_view);

		mProgressBar = (MyLoadingProgressBar)findViewById(R.id.progress);

		setupActionBar();
		setupWebView();
		
		setTitle(getNewsTitleFromIntent());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private String getNewsUrlFromIntent() {
		Intent i = getIntent();
		return i.getStringExtra(EXTRAS_NEWS_URL_KEY);
	}
	
	private String getNewsTitleFromIntent() {
		Intent i = getIntent();
		return i.getStringExtra(EXTRAS_NEWS_TITLE_KEY);
	}
}