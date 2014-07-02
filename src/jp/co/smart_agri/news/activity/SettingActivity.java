package jp.co.smart_agri.news.activity;

import jp.co.smart_agri.news.R;
import jp.co.smart_agri.news.activity.base.BaseActivity;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;

public class SettingActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		setupActionBar();
		setTitle("設定画面");
	}
	
	private void setupActionBar() {
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
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
}