package jp.co.smart_agri.news.receiver;

import jp.co.smart_agri.news.lib.AlarmManagerUtils;
import jp.co.smart_agri.news.lib.AppSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DeviceStartUpReceiver extends BroadcastReceiver {
	
	private final static String TAG = DeviceStartUpReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		if (new AppSettings(context).isMorningAlarmOn()) {
			AlarmManagerUtils.setMoriningAlarm(context);
		}
	}
}