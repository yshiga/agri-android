package jp.co.smart_agri.news.activity;

import java.util.Set;

import com.crittercism.app.Crittercism;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.PushService;

import jp.co.smart_agri.news.R;
import jp.co.smart_agri.news.config.AppConst;
import jp.co.smart_agri.news.fragment.NewsTabFragment;
import jp.co.smart_agri.news.lib.AppUtils;
import jp.co.smart_agri.news.lib.MyFlurry;
import jp.co.smart_agri.news.lib.MyParse;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	ViewPager mViewPager;
	PagerAdapter mPagerAdapter;

	private final static int TAB_COLOR_UNSELECTED = Color.parseColor("#F0F0EE");

	private void setupCrittercism(){
		Crittercism.initialize(getApplicationContext(), AppConst.CRITTERCISM_KEY);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		MyParse.setupParsePush(getApplicationContext(), getIntent());
		setupCrittercism();

		setTitle(getResources().getString(R.string.app_name));

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		setupViewPager();
		setupTab();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent i = new Intent(getApplicationContext(),
					SettingActivity.class);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
		mViewPager.setCurrentItem(tab.getPosition());
		resetTabStyle();
		setTabStyleOnSelected(tab.getPosition());

		MyFlurry.logEventSwitchTab(AppUtils.tabPosi2Cid(tab.getPosition()));
	}

	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {

	}

	@Override
	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {

	}

	public class NewsTabPagerAdapter extends FragmentPagerAdapter {

		public NewsTabPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public android.support.v4.app.Fragment getItem(int position) {
			return NewsTabFragment.newInstance(AppUtils.tabPosi2Cid(position));
		}

		@Override
		public int getCount() {
			return AppUtils.getTabCount();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return AppUtils.getTabNameByTabPosi(position);
		}
	}

	private void setupViewPager() {

		final ActionBar actionBar = getActionBar();
		mPagerAdapter = new NewsTabPagerAdapter(getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});
	}

	private void setupTab() {
		final ActionBar actionBar = getActionBar();
		for (int i = 0; i < mPagerAdapter.getCount(); i++) {
			RelativeLayout view = (RelativeLayout) getLayoutInflater().inflate(
					R.layout.action_bar_tab_label, null);

			TextView tabTitle = (TextView) view.findViewById(R.id.title);

			tabTitle.setText(mPagerAdapter.getPageTitle(i));
			tabTitle.setTextColor(AppUtils.getColorByTabPosi(i));
			view.setBackgroundColor(TAB_COLOR_UNSELECTED);

			actionBar.addTab(actionBar.newTab().setTabListener(this)
					.setCustomView(view));

		}
	}

	private void resetTabStyle() {
		final ActionBar actionBar = getActionBar();
		for (int i = 0; i < actionBar.getTabCount(); i++) {
			ActionBar.Tab tmpTab = actionBar.getTabAt(i);
			tmpTab.getCustomView().setBackgroundColor(TAB_COLOR_UNSELECTED);
			TextView title = (TextView)tmpTab.getCustomView().findViewById(R.id.title);
			title.setTextColor(AppUtils.getColorByTabPosi(i));
		}
	}

	/**
	 * 指定したポジションのタブの色を選択されている状態のものに変更する
	 * 
	 * @param position
	 */
	private void setTabStyleOnSelected(int position) {
		final ActionBar actionBar = getActionBar();
		ActionBar.Tab currentTab = actionBar.getTabAt(position);
		currentTab.getCustomView().setBackgroundColor(
				AppUtils.getColorByTabPosi(position));
		TextView title = (TextView)currentTab.getCustomView().findViewById(R.id.title);
		title.setTextColor(Color.WHITE);
	}

}