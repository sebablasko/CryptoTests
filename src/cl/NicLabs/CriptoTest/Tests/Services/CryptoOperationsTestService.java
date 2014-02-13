package cl.NicLabs.CriptoTest.Tests.Services;

import cl.NicLabs.CriptoTest.HomeActivity;
import cl.NicLabs.CriptoTest.R;
import cl.NicLabs.CriptoTest.TestManager;
import cl.NicLabs.CriptoTest.Tests.BitLengthTest;
import cl.NicLabs.CriptoTest.Tests.CryptoOperationsTest;
import cl.NicLabs.CriptoTest.Tests.GenericTest;
import cl.NicLabs.CriptoTest.Utils.UIUtils;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class CryptoOperationsTestService extends Service {

	GenericTest cryptoOperationsTest = new CryptoOperationsTest();
	
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
		System.out.println("started cryptoOperations Test");
		new Thread(new Runnable() {
			public void run() {
				if(!CryptoOperationsTest.running){
					try {
						cryptoOperationsTest.run();
						UIUtils.launchBaseNotification(getApplicationContext(),
								getString(R.string.crypto_operations_test_title_notification),
								getString(R.string.crypto_operations_test_title_notification),
								getString(R.string.crypto_operations_test_body_notification),
								HomeActivity.class);
					} catch (Exception e) {
						e.printStackTrace();
						UIUtils.launchBaseNotification(getApplicationContext(),
								getString(R.string.crypto_operations_test_error_title),
								getString(R.string.crypto_operations_test_error_title),
								getString(R.string.crypto_operations_test_error_body),
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
		int id = 13;
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				getApplicationContext());
		mBuilder.setSmallIcon(R.drawable.ic_launcher);
		mBuilder.setTicker(getString(R.string.starting_test));
		mBuilder.setContentTitle(getString(R.string.crypto_operations_name));
		mBuilder.setContentText(getString(R.string.test_working));
	    PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
	            new Intent(this, TestManager.class), 0);
	    mBuilder.setContentIntent(contentIntent);
		
		startForeground(id,mBuilder.build());
	}

}
