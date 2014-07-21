package jp.co.smart_agri.news.lib;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppSettings {

	private final static String PREF_NAME = "settings";
	private final static String MORNING_ALARM_KEY = "morning_alarm_key";
	
	private final Context mContext;
	private final SharedPreferences mPref;

	public AppSettings(Context context){
		mContext = context;
		mPref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
	}
	
	public void setMorningAlarm(boolean isOn) {
		Editor e = mPref.edit();
		e.putBoolean(MORNING_ALARM_KEY, isOn);
		e.commit();
	}

	public boolean isMorningAlarmOn() {
		return mPref.getBoolean(MORNING_ALARM_KEY, true);
	}
}