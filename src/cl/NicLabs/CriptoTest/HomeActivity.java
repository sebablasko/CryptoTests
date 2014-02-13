package cl.NicLabs.CriptoTest;

import cl.NicLabs.CriptoTest.Tests.Services.AddingTestService;
import cl.NicLabs.CriptoTest.Utils.InfoUtils;
import cl.NicLabs.CriptoTest.Utils.UIUtils;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
		case R.id.show_device_details:
		UIUtils.showHtmlDialog(HomeActivity.this, "Detalle Dispositivo",
				InfoUtils.getHtmlPhoneDetails());
			break;
		}
		return super.onOptionsItemSelected(item);
	}
    
    
    //apartado, acciones de botones UI
    public void goToCalc(View view){
		Intent intent = new Intent(HomeActivity.this, CalcActivity.class);
        startActivity(intent);
    }
    
    public void goToTestManager(View view){
    	Intent intent = new Intent(HomeActivity.this, TestManager.class);
        startActivity(intent);
    }
    
    
}
