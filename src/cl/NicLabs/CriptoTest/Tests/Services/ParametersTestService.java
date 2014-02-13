package cl.NicLabs.CriptoTest.Tests.Services;

import cl.NicLabs.CriptoTest.HomeActivity;
import cl.NicLabs.CriptoTest.R;
import cl.NicLabs.CriptoTest.TestManager;
import cl.NicLabs.CriptoTest.Tests.AddingEncryptedNumbersTest;
import cl.NicLabs.CriptoTest.Tests.EncryptionParametersTest;
import cl.NicLabs.CriptoTest.Tests.GenericTest;
import cl.NicLabs.CriptoTest.Utils.UIUtils;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class ParametersTestService extends Service {

	GenericTest parametersTest = new EncryptionParametersTest();
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.setForeground();
		this.startService();
	}

	private void startService() {
		System.out.println("started EncryptionParametersTest");
		new Thread(new Runnable() {
			public void run() {
				if(!EncryptionParametersTest.running){
					try {
						parametersTest.run();
						UIUtils.launchBaseNotification(getApplicationContext(),
								getString(R.string.parameters_test_title_notification),
								getString(R.string.parameters_test_title_notification),
								getString(R.string.parameters_test_body_notification),
								HomeActivity.class);
					} catch (Exception e) {
						e.printStackTrace();
						UIUtils.launchBaseNotification(getApplicationContext(),
								getString(R.string.parameters_test_error_title),
								getString(R.string.parameters_test_error_title),
								getString(R.string.parameters_test_error_body),
								HomeActivity.class);
					}	
				}
				stopSelf();
			}
		}).start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.finishService();
	}

	private void finishService() {
		// TODO Auto-generated method stub

	}
	
	private void setForeground() {
		int id = 11;
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				getApplicationContext());
		mBuilder.setSmallIcon(R.drawable.ic_launcher);
		mBuilder.setTicker(getString(R.string.starting_test));
		mBuilder.setContentTitle(getString(R.string.parameters_test_name));
		mBuilder.setContentText(getString(R.string.test_working));
	    PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
	            new Intent(this, TestManager.class), 0);
	    mBuilder.setContentIntent(contentIntent);
		
		startForeground(id,mBuilder.build());
	}
}
