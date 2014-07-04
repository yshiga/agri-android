package jp.co.smart_agri.news.activity;

import java.util.Iterator;
import java.util.Set;

import com.parse.PushService;

import jp.co.smart_agri.news.R;
import jp.co.smart_agri.news.activity.base.BaseActivity;
import jp.co.smart_agri.news.lib.MyParse;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SettingActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		setupActionBar();
		setTitle("設定画面");

		CheckBox morinigPushBox = (CheckBox) findViewById(R.id.morning_push_checkbox);

		morinigPushBox.setChecked(MyParse.isSubscribeMorning(getApplicationContext()));
		
		morinigPushBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						MyParse.setSubscribeMorning(getApplicationContext(), isChecked);
					}
				});

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