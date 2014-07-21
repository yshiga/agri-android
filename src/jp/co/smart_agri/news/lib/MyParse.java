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
import android.provider.Settings.Secure;

public class MyParse {

	public static final String CHANNEL_ALL = "all"; // 全員購読するチャンネル

	public static final Class<? extends Activity> DEF_LAUNCH_ACTIVITY = MainActivity.class;

	public static void setupParsePush(Context context) {
		Parse.initialize(context, AppConst.PARSE_APP_ID,
				AppConst.PARSE_CLIENT_KEY);

		// 全員向けのpushのチャンネルを設定
		PushService.subscribe(context, CHANNEL_ALL, DEF_LAUNCH_ACTIVITY);
		PushService.setDefaultPushCallback(context, DEF_LAUNCH_ACTIVITY);

		ParseInstallation installation = ParseInstallation
				.getCurrentInstallation();

		// 初回起動時に以下のエラーでフリーズすることがあるため、Android IDを渡す(ユニーク保証されてないけど大丈夫か?)
		// com.parse.ParseException: at least one ID field (installationId,deviceToken) must be specified in this operation
		// 参考 http://stackoverflow.com/questions/22294181/android-parse-push-notification-device-registration-only-one-time-on-one-device/22499551#22499551
		String android_id = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);
		installation.put("UniqueId", android_id);
		installation.saveInBackground();
	}

	/**
	 * チャンネルを購読しているか判定
	 * 
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
