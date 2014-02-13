package cl.NicLabs.CriptoTest.Utils;

import cl.NicLabs.CriptoTest.R;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Vibrator;
import android.sax.StartElementListener;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.widget.Toast;

public class UIUtils {

	static public void showHtmlDialog(Context context, String title, String html) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle(title);
		dialog.setMessage(Html.fromHtml(html));
		dialog.setCancelable(true);
		dialog.show();
	}

	static public void launchBaseNotification(Context context, String splashText,
			String notificationTitle, String notificationMsg, Class<?> returnActivity) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context);
		mBuilder.setSmallIcon(R.drawable.test_done_icon);
		mBuilder.setTicker(splashText);
		mBuilder.setContentTitle(notificationTitle);
		mBuilder.setContentText(notificationMsg);
		mBuilder.setSound(RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
		
		Intent intent = new Intent(context, returnActivity);
		mBuilder.setContentIntent(PendingIntent.getActivity(
				context, 0, intent, 0));

		mBuilder.setAutoCancel(true);

		NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(123123, mBuilder.build());
		((Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(700);
	}
}
