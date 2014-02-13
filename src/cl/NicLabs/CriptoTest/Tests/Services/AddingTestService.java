package cl.NicLabs.CriptoTest.Tests.Services;

import cl.NicLabs.CriptoTest.HomeActivity;
import cl.NicLabs.CriptoTest.R;
import cl.NicLabs.CriptoTest.TestManager;
import cl.NicLabs.CriptoTest.Tests.AddingEncryptedNumbersTest;
import cl.NicLabs.CriptoTest.Tests.GenericTest;
import cl.NicLabs.CriptoTest.Utils.UIUtils;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AddingTestService extends Service {

	GenericTest addingTest = new AddingEncryptedNumbersTest();
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.setForeground();
		this.startService();
	}

	private void startService() {
		System.out.println("started AddingTestService");
		new Thread(new Runnable() {
			public void run() {
				if(!AddingEncryptedNumbersTest.running){
					try {
						addingTest.run();
						UIUtils.launchBaseNotification(getApplicationContext(),
								getString(R.string.adding_test_title_notification),
								getString(R.string.adding_test_title_notification),
								getString(R.string.adding_test_body_notification),
								HomeActivity.class);
					} catch (Exception e) {
						e.printStackTrace();
						UIUtils.launchBaseNotification(getApplicationContext(),
								getString(R.string.adding_test_error_title),
								getString(R.string.adding_test_error_title),
								getString(R.string.adding_test_error_body),
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
		System.out.println("Adding test killed");
	}
	
	private void setForeground() {
		int id = 14;
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				getApplicationContext());
		mBuilder.setSmallIcon(R.drawable.ic_launcher);
		mBuilder.setTicker(getString(R.string.starting_test));
		mBuilder.setContentTitle(getString(R.string.adding_test_name));
		mBuilder.setContentText(getString(R.string.test_working));
	    PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
	            new Intent(this, TestManager.class), 0);
	    mBuilder.setContentIntent(contentIntent);
		
		startForeground(id,mBuilder.build());
	}
}
