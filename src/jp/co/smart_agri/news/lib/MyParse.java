package jp.co.smart_agri.news.lib;

import java.util.Iterator;
import java.util.Set;

import jp.co.smart_agri.news.activity.MainActivity;
import jp.co.smart_agri.news.config.AppConst;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.PushService;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

public class MyParse {

	public static final String CHANNEL_ALL = "all"; // 全員購読するチャンネル
	public static final String CHANNEL_MORNING = "morning"; // 朝のお知らせプッシュ用

	public static final Class<? extends Activity> DEF_LAUNCH_ACTIVITY = MainActivity.class;

	public static boolean isSubscribeNoChannel(Context context) {
		Set<String> chanels = PushService.getSubscriptions(context);

		// 初回起動時
		// 必ず1つ以上のチャンネルを設定するので、チャンネルが0の場合は初回起動
		if (chanels == null || chanels.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static void setupParsePush(Context context) {
		Parse.initialize(context, AppConst.PARSE_APP_ID,
				AppConst.PARSE_CLIENT_KEY);

		// チャンネルが0 = 初回起動なので、朝PUSHのチャンネルを設定
		if (isSubscribeNoChannel(context)) {
			subscribeMorning(context);
		}

		// 全員向けのpushのチャンネルを設定
		PushService.subscribe(context, CHANNEL_ALL, DEF_LAUNCH_ACTIVITY);
		PushService.setDefaultPushCallback(context, DEF_LAUNCH_ACTIVITY);
		ParseInstallation.getCurrentInstallation().saveInBackground();
	}
	
	public static void setSubscribeMorning(Context context, boolean isSubscribe) {
		if(isSubscribe){
			subscribeMorning(context);
		}else {
			unsubscribeMorning(context);
		}
	}

	public static void subscribeMorning(Context context) {
		PushService.subscribe(context, CHANNEL_MORNING, DEF_LAUNCH_ACTIVITY);
	}

	public static void unsubscribeMorning(Context context) {
		PushService.unsubscribe(context, CHANNEL_MORNING);
	}
	
	public static boolean isSubscribeMorning(Context context) {
		return isSubscribeChannel(context, CHANNEL_MORNING);
	}

	/**
	 * チャンネルを購読しているか判定
	 * @param context
	 * @param channel
	 * @return
	 */
	public static boolean isSubscribeChannel(Context context, String channel) {

		Set<String> chanels = PushService.getSubscriptions(context);
		Iterator<String> it = chanels.iterator();
		while (it.hasNext()) {
			String ch = it.next();
			if (ch.equals(channel)) {
				return true;
			}
		}
		return false;
	}

}
