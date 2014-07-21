package jp.co.smart_agri.news.lib;

import java.util.Calendar;

import jp.co.smart_agri.news.receiver.NotificationReceiver;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmManagerUtils {

	private final static int MORNING_ALARM_REQ_CODE = 1;

	public static void setMoriningAlarm(Context context) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Calendar calendar = createNext8AMCalendar();
		am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
				createMorningAlarmPIntent(context));
	}
	
	public static void resetMoriningAlarm(Context context) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		am.cancel(createMorningAlarmPIntent(context));
	}

	public static PendingIntent createMorningAlarmPIntent(Context context) {
		Intent intent = new Intent(context, NotificationReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context,
				MORNING_ALARM_REQ_CODE, intent, 0);
		return pi;
	}

	public static Calendar createNextDayCalendar(int hourOfDay) {
		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		// 翌日
		if (hourOfDay <= calendar.get(Calendar.HOUR_OF_DAY)) {
			calendar.add(Calendar.DAY_OF_YEAR, 1);
		}
		return calendar;
	}

	public static Calendar createNext8AMCalendar() {
		return createNextDayCalendar(8);
	}
}