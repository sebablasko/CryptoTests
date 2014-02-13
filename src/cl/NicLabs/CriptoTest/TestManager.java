package cl.NicLabs.CriptoTest;

import cl.NicLabs.CriptoTest.Tests.AddingEncryptedNumbersTest;
import cl.NicLabs.CriptoTest.Tests.BitLengthTest;
import cl.NicLabs.CriptoTest.Tests.CryptoOperationsTest;
import cl.NicLabs.CriptoTest.Tests.EncryptionParametersTest;
import cl.NicLabs.CriptoTest.Tests.InfoData;
import cl.NicLabs.CriptoTest.Tests.Services.AddingTestService;
import cl.NicLabs.CriptoTest.Tests.Services.BitLengthTestService;
import cl.NicLabs.CriptoTest.Tests.Services.CryptoOperationsTestService;
import cl.NicLabs.CriptoTest.Tests.Services.ParametersTestService;
import cl.NicLabs.CriptoTest.Utils.IOUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class TestManager extends Activity {

	// icons for test status
	Drawable done, working, off, error;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_manager);

		// Icons of tests status
		done = getResources().getDrawable(android.R.drawable.presence_online);
		working = getResources().getDrawable(android.R.drawable.presence_away);
		off = getResources().getDrawable(android.R.drawable.presence_offline);
		error = getResources().getDrawable(android.R.drawable.presence_busy);
	}

	protected void onResume() {
		super.onResume();
		refreshIconsTestStatus();
	}

	private void updateTestIcon(boolean testStatus, ImageView icon,
			String resultsPathFile) {
		if (testStatus) {
			icon.setImageDrawable(working);
		} else {
			if (!IOUtils.existResults(resultsPathFile))
				icon.setImageDrawable(off);
			else
				icon.setImageDrawable(done);
		}
	}

	private void refreshIconsTestStatus() {
		// Verify status of every test result
		boolean testStatus;
		ImageView icon;
		String resultsPath;

		// 1.- Parameter test
		testStatus = EncryptionParametersTest.running;
		icon = (ImageView) findViewById(R.id.indicator_test1);
		resultsPath = InfoData.pathForResults
				+ InfoData.fileNameDataParametersTest + ".zip";
		updateTestIcon(testStatus, icon, resultsPath);

		// 2.- bitlength test
		testStatus = BitLengthTest.running;
		icon = (ImageView) findViewById(R.id.indicator_test2);
		resultsPath = InfoData.pathForResults
				+ InfoData.fileNameDatabitLenghtTest + ".zip";
		updateTestIcon(testStatus, icon, resultsPath);

		// 3.- CryptoOperations Test
		testStatus = CryptoOperationsTest.running;
		icon = (ImageView) findViewById(R.id.indicator_test3);
		resultsPath = InfoData.pathForResults
				+ InfoData.fileNameDataCryptoOperationsTest + ".zip";
		updateTestIcon(testStatus, icon, resultsPath);

		// 4.- adding test
		testStatus = AddingEncryptedNumbersTest.running;
		icon = (ImageView) findViewById(R.id.indicator_test4);
		resultsPath = InfoData.pathForResults + InfoData.fileNameDataAddingTest
				+ ".zip";
		updateTestIcon(testStatus, icon, resultsPath);
	}

	/**
	 * Parameters Test Button
	 */
	public void manageTest1(View view) {
		TableRow row = (TableRow) view.getParent();

		String testName = ((TextView) row.findViewById(R.id.title_test))
				.getText().toString();
		String errorLaunching = getString(R.string.parameters_test_error_launched);
		String okLaunched = getString(R.string.parameters_test_ok_launched);
		String infoTest = EncryptionParametersTest.getHtmlDetails();
		String fileNameData = InfoData.fileNameDataParametersTest;

		confirmTextExecution(testName, this, ParametersTestService.class,
				errorLaunching, okLaunched, infoTest, fileNameData);
	}

	/**
	 * bitLength Test Button
	 */
	public void manageTest2(View view) {
		TableRow row = (TableRow) view.getParent();

		String testName = ((TextView) row.findViewById(R.id.title_test))
				.getText().toString();
		String errorLaunching = getString(R.string.bitLength_test_error_launched);
		String okLaunched = getString(R.string.bitLength_test_ok_launched);
		String infoTest = BitLengthTest.getHtmlDetails();
		String fileNameData = InfoData.fileNameDatabitLenghtTest;

		confirmTextExecution(testName, this, BitLengthTestService.class,
				errorLaunching, okLaunched, infoTest, fileNameData);
	}

	/**
	 * CryptoOperations Test Button
	 */
	public void manageTest3(View view) {
		TableRow row = (TableRow) view.getParent();

		String testName = ((TextView) row.findViewById(R.id.title_test))
				.getText().toString();
		String errorLaunching = getString(R.string.crypto_operations_test_error_launched);
		String okLaunched = getString(R.string.crypto_operations_test_ok_launched);
		String infoTest = CryptoOperationsTest.getHtmlDetails();
		String fileNameData = InfoData.fileNameDataCryptoOperationsTest;

		confirmTextExecution(testName, this, CryptoOperationsTestService.class,
				errorLaunching, okLaunched, infoTest, fileNameData);
	}

	/**
	 * Adding Test Button
	 */
	public void manageTest4(View view) {
		TableRow row = (TableRow) view.getParent();

		String testName = ((TextView) row.findViewById(R.id.title_test))
				.getText().toString();
		String errorLaunching = getString(R.string.adding_test_error_launched);
		String okLaunched = getString(R.string.adding_test_ok_launched);
		String infoTest = AddingEncryptedNumbersTest.getHtmlDetails();
		String fileNameData = InfoData.fileNameDataAddingTest;

		confirmTextExecution(testName, this, AddingTestService.class,
				errorLaunching, okLaunched, infoTest, fileNameData);
	}

	public void confirmTextExecution(String titleTest, Context context,
			final Class<?> testService, final String errorLaunchingTest,
			final String okLaunchedTest, String infoTest,
			final String fileNameDataToSaveResults) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(Html.fromHtml(infoTest))
				.setTitle(titleTest)
				.setCancelable(false)
				.setNegativeButton(getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						})
				.setNeutralButton(getString(R.string.clean_results),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								String path = InfoData.pathForResults
										+ fileNameDataToSaveResults + ".zip";
								if (IOUtils.existResults(path)) {
									IOUtils.deleteData(path);
								}
							}
						})
				.setPositiveButton(getString(R.string.execute),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent service = new Intent(
										getApplicationContext(), testService);
								if (startService(service) == null)
									Toast.makeText(getApplicationContext(),
											errorLaunchingTest,
											Toast.LENGTH_SHORT).show();
								else
									Toast.makeText(getApplicationContext(),
											okLaunchedTest, Toast.LENGTH_SHORT)
											.show();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
}
