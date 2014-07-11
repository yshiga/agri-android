package jp.co.smart_agri.news.fragment;

import org.json.JSONArray;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.ImageLoader.ImageListener;

import jp.co.smart_agri.news.R;
import jp.co.smart_agri.news.activity.NewsWebViewActivity;
import jp.co.smart_agri.news.application.MyApplication;
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

	NewsListAdapter mAdapter;

	private ProgressBar mLoadingCircle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_news_tab, container,
				false);
		mAdapter = new NewsListAdapter();

		mLoadingCircle = (ProgressBar) rootView
				.findViewById(R.id.progress_circle);

		View topBar = rootView.findViewById(R.id.top_bar);
		topBar.setBackgroundColor(AppUtils.getColorByCid(getNewsCaterogyId()));

		mNewsList = new NewsList();
		ListView listView = (ListView) rootView.findViewById(R.id.list);
		listView.setAdapter(mAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				News news = mNewsList.get(position);
				MyFlurry.logEventViewArticle(news.getId(), getNewsCaterogyId());

				startNewsWebViewActivity(news);
			}
		});
		getNews();

		return rootView;
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

	private void getNews() {
		// TODO fragment内で通信するのはよくないが、一旦これで実装
		JsonArrayRequest req = new JsonArrayRequest(AppConst.API_BASE_URL
				+ AppUtils.getApiPathByCid(getNewsCaterogyId()),
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						mNewsList.convertFromJsonArray(response);
						mAdapter.notifyDataSetChanged();
						mLoadingCircle.setVisibility(View.GONE);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						mLoadingCircle.setVisibility(View.GONE);
					}
				});

		MyApplication.getInstance().addToRequestQueue(req);
		mLoadingCircle.setVisibility(View.VISIBLE);
	}

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
			NewsListRowViewBuilderBase listRowViewBuilder = new NormalListRowViewBilder(R.layout.list_news, parent); 
			return listRowViewBuilder.getView(news);
		}
	}

	class NormalListRowViewBilder extends NewsListRowViewBuilderBase {

		public NormalListRowViewBilder(int layoutId, ViewGroup parent) {
			super(layoutId, parent);
		}

		@Override
		public void setTitle(News news) {
			mTitleView.setText(news.getTitle());
		}

		@Override
		public void setSrc(News news) {
			if (!news.hasVia()) {
				mSrcView.setVisibility(View.GONE);
				return;
			}
			mSrcView.setVisibility(View.VISIBLE);
			mSrcView.setText(news.getVia());
		}

		@Override
		public void setThumnailView(News news) {

			if (!news.hasImage()) {
				mThumnailView.setVisibility(View.GONE);
				mThumnailProgressBar.setVisibility(View.GONE);
				return;
			}

			mThumnailView.setVisibility(View.VISIBLE);
			mThumnailProgressBar.setVisibility(View.VISIBLE);

			ImageListener listener = MyImageLoader.getImageListener(mThumnailView, 
					mThumnailProgressBar);
			mImageLoader.get(news.getImageUrl(), listener);
		}
	}

	/**
	 * newsのViewを生成する共通クラス
	 * @author shigayuuichi
	 */
	abstract class NewsListRowViewBuilderBase {

		View mRootView;
		ImageLoader mImageLoader = new ImageLoader(MyApplication
				.getInstance().getRequestQueue(), new MyImageCache());
		TextView mTitleView;
		TextView mSrcView;
		ImageView mThumnailView;
		ProgressBar mThumnailProgressBar;
		
		public NewsListRowViewBuilderBase(int layoutId, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mRootView = inflater.inflate(R.layout.list_news, parent, false);
			mTitleView = (TextView) mRootView.findViewById(R.id.title);
			mSrcView = (TextView) mRootView.findViewById(R.id.src);
			mThumnailView = (ImageView) mRootView 
					.findViewById(R.id.thumnail);
			mThumnailProgressBar = (ProgressBar) mRootView 
					.findViewById(R.id.progress_bar);
		}

		public View getView(News news) {
			setTitle(news);
			setSrc(news);
			setThumnailView(news);
			return mRootView;
		}

		public abstract void setTitle(News news);

		public abstract void setSrc(News news);

		public abstract void setThumnailView(News news);
	}
}
