package jp.co.smart_agri.news.activity;

import jp.co.smart_agri.news.R;
import jp.co.smart_agri.news.fragment.NewsTabFragment;
import jp.co.smart_agri.news.lib.AppUtils;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	ViewPager mViewPager;
	PagerAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setTitle(getResources().getString(R.string.app_name));

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

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
		for (int i = 0; i < mPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
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

}