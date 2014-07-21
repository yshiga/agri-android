package jp.co.smart_agri.news.lib;

import java.util.Calendar;

import com.google.android.gms.internal.co;

import jp.co.smart_agri.news.R;
import jp.co.smart_agri.news.activity.MainActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

public class NotificationUtils {

	private final static int REQUEST_CODE_MAIN_ACTIVITY = 1;
	
	public static void showMorningNotification(Context context) {

		NotificationManager mManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		String ticker = "朝のニュースが届きました";
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, 15);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

		Intent intent = new Intent(context, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(
			            context, REQUEST_CODE_MAIN_ACTIVITY, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(contentIntent);
		
		// ステータスバーに表示されるテキスト
		builder.setTicker(ticker);
		// アイコン
		builder.setSmallIcon(R.drawable.ic_launcher);
		// Notificationを開いたときに表示されるタイトル
		builder.setContentTitle(context.getResources().getString(R.string.app_name));
		// Notificationを開いたときに表示されるサブタイトル
		builder.setContentText("朝のニュースが届きました");
		// Notificationを開いたときに表示されるアイコン
		
		 Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
		builder.setLargeIcon(largeIcon);

		// 通知するタイミング
		builder.setWhen(System.currentTimeMillis());
		// 通知時の音・バイブ・ライト
		builder.setDefaults(Notification.DEFAULT_SOUND
				| Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
		// タップするとキャンセル(消える)
		builder.setAutoCancel(true);

		mManager.notify(1, builder.build());


	}
}