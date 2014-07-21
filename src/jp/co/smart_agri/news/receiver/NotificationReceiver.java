package jp.co.smart_agri.news.receiver;

import jp.co.smart_agri.news.lib.AlarmManagerUtils;
import jp.co.smart_agri.news.lib.NotificationUtils;
import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		NotificationUtils.showMorningNotification(context);
		AlarmManagerUtils.setMoriningAlarm(context);
	}
}