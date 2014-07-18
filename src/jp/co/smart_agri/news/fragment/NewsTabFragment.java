package jp.co.smart_agri.news.fragment;

import org.json.JSONArray;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.parse.FindCallback;

import jp.co.smart_agri.news.R;
import jp.co.smart_agri.news.activity.MainActivity;
import jp.co.smart_agri.news.activity.NewsWebViewActivity;
import jp.co.smart_agri.news.application.MyApplication;
import jp.co.smart_agri.news.application.MyApplication.TrackerName;
import jp.co.smart_agri.news.config.AppConst;
import jp.co.smart_agri.news.lib.AppUtils;
import jp.co.smart_agri.news.lib.MyFlurry;
import jp.co.smart_agri.news.lib.MyImageCache;
import jp.co.smart_agri.news.lib.MyImageLoader;
import jp.co.smart_agri.news.object.News;
import jp.co.smart_agri.news.object.NewsList;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class NewsTabFragment extends Fragment {

	private static final String ARG_CATEGORY_ID = "category_id";
	private NewsList mNewsList;

	public NewsTabFragment() {
	}

	public static NewsTabFragment newInstance(int newsCategoryId) {
		NewsTabFragment fragment = new NewsTabFragment();

		setNewsCateroyId(fragment, newsCategoryId);
		return fragment;
	}

	private NewsListAdapter mAdapter;
	private ProgressBar mLoadingCircle;
	private TextView mErrMsgView;
	private PullToRefreshListView mListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_news_tab, container,
				false);
		mLoadingCircle = (ProgressBar) rootView
				.findViewById(R.id.progress_circle);

		View topBar = rootView.findViewById(R.id.top_bar);
		topBar.setBackgroundColor(AppUtils.getColorByCid(getNewsCaterogyId()));

		mErrMsgView = (TextView) rootView.findViewById(R.id.errmsg);

		setupListView(rootView);
		loadNews(LOAD_MODE.INIT);

		return rootView;
	}

	private void setupListView(View rootView) {
		mNewsList = new NewsList();
		mAdapter = new NewsListAdapter();
		mListView = (PullToRefreshListView) rootView.findViewById(R.id.list);
		mListView.setAdapter(mAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				News news = (News) mAdapter.getItem((int) id);
				MyFlurry.logEventViewArticle(news.getId(), getNewsCaterogyId());

				logEventViewArticle(getNewsCaterogyId(), Integer.valueOf(news.getId()));

				startNewsWebViewActivity(news);
			}
		});

		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				loadNews(LOAD_MODE.RELOAD);
			}
		});
	}

	private boolean isOnline() {
		MainActivity activity = (MainActivity) getActivity();
		return activity.isOnline();
	}

	private void startNewsWebViewActivity(News news) {
		Intent i = new Intent(getActivity().getApplicationContext(),
				NewsWebViewActivity.class);
		i.putExtra(NewsWebViewActivity.EXTRAS_NEWS_URL_KEY, news.getUrl());
		i.putExtra(NewsWebViewActivity.EXTRAS_NEWS_TITLE_KEY, news.getTitle());
		startActivity(i);
	}

	@Override
	public void onResume() {
		super.onResume();

	};

	private void showError(String msg) {
		mErrMsgView.setVisibility(View.VISIBLE);
		mErrMsgView.setText(msg);
	}

	private void hideError() {
		mErrMsgView.setVisibility(View.GONE);
	}

	private enum LOAD_MODE {
		INIT, RELOAD
	}

	private void loadNews(LOAD_MODE mode) {

		if (isOnline()) {
			hideError();
		} else {
			showError("インターネットに接続されていません");
			return;
		}

		Response.Listener<JSONArray> successListener;
		if (mode == LOAD_MODE.INIT) {
			mLoadingCircle.setVisibility(View.VISIBLE);
			successListener = mInitLoadNewsListener;
		} else {
			successListener = mReloadNewsListener;
		}

		JsonArrayRequest req = new JsonArrayRequest(AppConst.API_BASE_URL
				+ AppUtils.getApiPathByCid(getNewsCaterogyId()),
				successListener, mResponseErrorListener);

		MyApplication.getInstance().addToRequestQueue(req);
	}

	private Response.Listener<JSONArray> mInitLoadNewsListener = new Response.Listener<JSONArray>() {
		@Override
		public void onResponse(JSONArray response) {
			mNewsList.convertFromJsonArray(response);
			mAdapter.notifyDataSetChanged();
			mLoadingCircle.setVisibility(View.GONE);
		}
	};

	private Response.Listener<JSONArray> mReloadNewsListener = new Response.Listener<JSONArray>() {
		@Override
		public void onResponse(JSONArray response) {
			mNewsList.convertFromJsonArray(response);
			mAdapter.notifyDataSetChanged();
			mListView.onRefreshComplete();
		}
	};

	private Response.ErrorListener mResponseErrorListener = new Response.ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			mLoadingCircle.setVisibility(View.GONE);
			showError("エラーが発生しました");
		}
	};

	@Override
	public void onPause() {
		super.onPause();
	};

	private static void setNewsCateroyId(NewsTabFragment fg, int sectionNumber) {
		Bundle args = new Bundle();
		args.putInt(ARG_CATEGORY_ID, sectionNumber);
		fg.setArguments(args);
	}

	private int getNewsCaterogyId() {
		Bundle args = getArguments();
		return args.getInt(ARG_CATEGORY_ID);
	}

	private void logEventViewArticle(int categoryId, int articleId) {
		Tracker t = ((MyApplication) getActivity().getApplication())
				.getTracker(TrackerName.APP_TRACKER);
		// Build and send an Event.
		t.send(new HitBuilders.EventBuilder()
				.setCategory("VIEW_ARTICLE")
				.setAction("VIEW_ARTICLE")
				.setLabel("CATEGORY_ID").setValue(categoryId)
				.setLabel("ARTICLE_ID").setValue(articleId)
				.build());
	}

	private class NewsListAdapter extends BaseAdapter {

		public NewsListAdapter() {

		}

		@Override
		public int getCount() {
			return mNewsList.getCount();
		}

		@Override
		public Object getItem(int position) {
			return mNewsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			News news = (News) getItem(position);
			NewsListRowViewBuilderBase listRowViewBuilder;
			if (position == 0) {
				listRowViewBuilder = new TopListRowViewBilder(parent);
			} else {
				listRowViewBuilder = new NormalListRowViewBilder(parent);
			}

			return listRowViewBuilder.getView(news);
		}
	}

	class TopListRowViewBilder extends NewsListRowViewBuilderBase {

		public TopListRowViewBilder(ViewGroup parent) {
			super(R.layout.list_news_top, parent);
		}
	}

	class NormalListRowViewBilder extends NewsListRowViewBuilderBase {

		public NormalListRowViewBilder(ViewGroup parent) {
			super(R.layout.list_news_normal, parent);
		}
	}

	/**
	 * newsのViewを生成する共通クラス
	 * 
	 * @author shigayuuichi
	 */
	abstract class NewsListRowViewBuilderBase {

		View mRootView;
		ImageLoader mImageLoader = new ImageLoader(MyApplication.getInstance()
				.getRequestQueue(), new MyImageCache());

		public NewsListRowViewBuilderBase(int layoutId, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mRootView = inflater.inflate(layoutId, parent, false);

		}

		public View getView(News news) {
			setTitle(news);
			setSrc(news);
			setThumnailView(news);
			return mRootView;
		}

		protected void setTitle(News news) {
			TextView titleView = (TextView) mRootView.findViewById(R.id.title);
			titleView.setText(news.getTitle());
		}

		protected void setSrc(News news) {
			TextView srcView = (TextView) mRootView.findViewById(R.id.src);
			if (!news.hasVia()) {
				srcView.setVisibility(View.GONE);
				return;
			}
			srcView.setVisibility(View.VISIBLE);
			srcView.setText(news.getVia());
		}

		protected void setThumnailView(News news) {
			ImageView thumnailView = (ImageView) mRootView
					.findViewById(R.id.thumnail);
			ProgressBar thumnailProgressBar = (ProgressBar) mRootView
					.findViewById(R.id.progress_bar);
			ViewGroup thumnailArea = (ViewGroup) mRootView
					.findViewById(R.id.thumnail_area);

			if (!news.hasImage()) {
				thumnailArea.setVisibility(View.GONE);
				thumnailView.setVisibility(View.GONE);
				thumnailProgressBar.setVisibility(View.GONE);
				return;
			}

			thumnailArea.setVisibility(View.VISIBLE);

			ImageListener listener = MyImageLoader.getImageListener(
					thumnailView, thumnailProgressBar);
			mImageLoader.get(news.getImageUrl(), listener);
		}
	}
}