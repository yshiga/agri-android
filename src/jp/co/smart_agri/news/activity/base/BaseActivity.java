package jp.co.smart_agri.news.activity.base;

import jp.co.smart_agri.news.lib.MyFlurry;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();
		MyFlurry.onStartSession(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		MyFlurry.onEndSession(this);
	}
	
	protected void startSettingActivity(){

	}
	

}