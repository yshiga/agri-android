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
				MyFlurry.logEventViewArticle(news.getId());

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

		private ImageLoader mImageLoader;

		public NewsListAdapter() {
			mImageLoader = new ImageLoader(MyApplication.getInstance()
					.getRequestQueue(), new MyImageCache());
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

			LayoutInflater inflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rootView = inflater.inflate(R.layout.list_news, parent, false);

			setTitle(rootView, news);
			setSrc(rootView, news);
			setThumnailView(rootView, news);

			return rootView;
		}

		private void setTitle(View rootView, News news) {
			TextView title = (TextView) rootView.findViewById(R.id.title);
			title.setText(news.getTitle());
		}

		private void setSrc(View rootView, News news) {
			TextView src = (TextView) rootView.findViewById(R.id.src);
			if (news.hasSrc()) {
				src.setVisibility(View.GONE);
				return;
			}
			
			src.setVisibility(View.VISIBLE);
			src.setText(news.getSrc());
		}

		private void setThumnailView(View rootView, News news) {

			ImageView thumnail = (ImageView) rootView
					.findViewById(R.id.thumnail);

			if (!news.hasImage()) {
				thumnail.setVisibility(View.GONE);
				return;
			}

			thumnail.setVisibility(View.VISIBLE);
			ImageListener listener = ImageLoader.getImageListener(thumnail,
					android.R.drawable.spinner_background,
					android.R.drawable.ic_dialog_alert);
			mImageLoader.get(news.getImageUrl(), listener);
		}
	}
}