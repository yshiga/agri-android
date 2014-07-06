package jp.co.smart_agri.news.lib;

import java.util.HashMap;

import jp.co.smart_agri.news.config.AppConst;

import com.flurry.android.FlurryAgent;

import android.content.Context;


public class MyFlurry {
	public static String EVENT_VIEW_ARTICLE = "VIEW_ARTICLE";
	public static String EVENT_SWITCH_TAB = "EVENT_SWITCH_TAB";
	public static String EVENT_LAUNCH_APP = "LAUNCH_APP";
	public static String EVENT_LAUNCH_APP_BY_PUSH = "LAUNCH_BY_PUSH";
	public static String EVENT_LAUNCH_APP_NORMAL = "LAUNCH_NORMAL";

	public static void onStartSession(Context context) {
		FlurryAgent.onStartSession(context, AppConst.FLURRY_KEY);
	}

	public static void onEndSession(Context context) {
		FlurryAgent.onEndSession(context);
	}

	/*
	 * 以下、イベント取得
	 */

	public static void logEventLaunchByPush() {
		logEventLaunchApp(EVENT_LAUNCH_APP_BY_PUSH);
	}

	public static void logEventLaunchNormal() {
		logEventLaunchApp(EVENT_LAUNCH_APP_NORMAL);
	}

	private static void logEventLaunchApp(String src) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("src", src);

		FlurryAgent.logEvent(EVENT_LAUNCH_APP, param);
	}

	public static void logEventViewArticle(String articleId, int categoryId) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("id", articleId);
		param.put("category_id", String.valueOf(categoryId));

		FlurryAgent.logEvent(EVENT_VIEW_ARTICLE, param);
	}

	public static void logEventSwitchTab(int categoryId) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("caterogy_id", String.valueOf(categoryId));

		FlurryAgent.logEvent(EVENT_SWITCH_TAB, param);
	}

}