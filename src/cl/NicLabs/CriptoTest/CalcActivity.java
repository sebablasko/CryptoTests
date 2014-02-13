package cl.NicLabs.CriptoTest;

import java.math.BigInteger;

import cl.NicLabs.CriptoTest.Utils.UIUtils;
import cl.NicLabs.HomomorphicEncryption.EncryptedValue;
import cl.NicLabs.HomomorphicEncryption.EncryptionParameters;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CalcActivity extends Activity {

	private EncryptionParameters encriptionParameters = null;
	private int valorBitSeguridad = -1, valorParametroS = -1;
	private BigInteger firstValue, secondValue, scaleValue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calc);
		//UIUtils.launchBaseNotification(this, "Notificacion!", "Este es el titulo", "El cuerpo", HomeActivity.class);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.calc, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
		case R.id.show_parameters:
			if (encriptionParameters != null)
				UIUtils.showHtmlDialog(CalcActivity.this, "Detalle Parametros",
						encriptionParameters.toHtml());
			else
				UIUtils.showHtmlDialog(CalcActivity.this, "Detalle Parametros",
						"Parametros No Listos");
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// Apartado recuperacion y construccion de parametros

	/**
	 * get parameters from UI to build encryptationParameters
	 */
	private void getParameters() {
		String val1 = ((EditText) findViewById(R.id.bitLength_value)).getText()
				.toString();
		String val2 = ((EditText) findViewById(R.id.s_value)).getText()
				.toString();
		try {
			this.valorBitSeguridad = Integer.parseInt(val1);
			this.valorParametroS = Integer.parseInt(val2);
		} catch (NumberFormatException e) {
			this.valorBitSeguridad = this.valorParametroS = -1;
		}
	}

	/**
	 * get values from UI to operate into calculator
	 */
	private void getValues() {
		this.firstValue = this.secondValue = this.scaleValue = null;
		String val1 = ((EditText) findViewById(R.id.val1)).getText().toString();
		String val2 = ((EditText) findViewById(R.id.val2)).getText().toString();
		String valEscalar = ((EditText) findViewById(R.id.valEscalar))
				.getText().toString();
		try {
			this.firstValue = new BigInteger(val1);
			this.secondValue = new BigInteger(val2);
			this.scaleValue = new BigInteger(valEscalar);
		} catch (NumberFormatException e) {
		}
	}

	/**
	 * Actived by Button from UI Start Thread to build ecnryptedParameters
	 * 
	 * @param view
	 */
	public void createParameters(View view) {
		this.encriptionParameters = null;
		cleanUI();
		getParameters();
		if (this.valorParametroS < 0 || this.valorBitSeguridad < 0)
			return;
		findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
		new Thread(new Runnable() {
			public void run() {
				encriptionParameters = new EncryptionParameters(
						valorBitSeguridad, valorParametroS);
				runOnUiThread(new Runnable() {
					public void run() {
						findViewById(R.id.loadingPanel)
								.setVisibility(View.GONE);
						Toast.makeText(getApplicationContext(),
								"Parametros Listos", Toast.LENGTH_SHORT).show();
						((Vibrator) getSystemService(Context.VIBRATOR_SERVICE))
								.vibrate(700);
					}
				});
			}
		}).start();
	}

	/**
	 * Clean UI Results
	 */
	public void cleanUI() {
		((TextView) findViewById(R.id.result)).setVisibility(View.GONE);
		((TextView) findViewById(R.id.resumenData)).setVisibility(View.GONE);
		((TextView) findViewById(R.id.val1_desencriptado)).setText("");
		((TextView) findViewById(R.id.val2_desencriptado)).setText("");
	}

	// Apartado de Accion de operaciones criptograficas

	public long[] experimentTimes;
	public BigInteger addResult, scaleResult, firstValueRetrieved,
			secondValueRetrieved;

	/**
	 * Actived by Button from UI Start a thread to calculate stats for crypto
	 * operations *
	 * 
	 * @param view
	 */
	public void calc(View view) {
		if (encriptionParameters == null) {
			Toast.makeText(getApplicationContext(), "No hay Parametros",
					Toast.LENGTH_SHORT).show();
			return;
		}
		getValues();
		if (firstValue == null || secondValue == null || scaleValue == null) {
			Toast.makeText(getApplicationContext(),
					"No hay valores que operar", Toast.LENGTH_SHORT).show();
			return;
		}
		cleanUI();
		findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
		new Thread(new Runnable() {
			public void run() {
				experimentTimes = calculate();
				runOnUiThread(new Runnable() {
					public void run() {
						findViewById(R.id.loadingPanel)
								.setVisibility(View.GONE);
						showResults();
					}
				});
			}
		}).start();
	}

	/**
	 * 
	 * Calculate all the crypto operations over two values measuring the time
	 * between them
	 * @return [t°E(val1), t°E(val2), t°E1*E2, t°E1^k, t°D(E1), t°D(E2),
	 *         t°D(E1*E2), t°D(E1^k)]
	 */
	public long[] calculate() {
		long[] ret = new long[8];
		long tAux;

		// Encrypt
		tAux = System.currentTimeMillis();
		EncryptedValue ev1 = new EncryptedValue(firstValue,
				encriptionParameters);
		ret[0] = System.currentTimeMillis() - tAux;

		tAux = System.currentTimeMillis();
		EncryptedValue ev2 = new EncryptedValue(secondValue,
				encriptionParameters);
		ret[1] = System.currentTimeMillis() - tAux;

		// Crypted Operations
		tAux = System.currentTimeMillis();
		EncryptedValue addTotal = ev1.addValue(ev2);
		ret[2] = System.currentTimeMillis() - tAux;

		tAux = System.currentTimeMillis();
		EncryptedValue scaleTotal = ev1.scale(scaleValue);
		ret[3] = System.currentTimeMillis() - tAux;

		// Decrypt
		tAux = System.currentTimeMillis();
		firstValueRetrieved = ev1.decrypt(encriptionParameters.getSecretKey());
		ret[4] = System.currentTimeMillis() - tAux;

		tAux = System.currentTimeMillis();
		secondValueRetrieved = ev2.decrypt(encriptionParameters.getSecretKey());
		ret[5] = System.currentTimeMillis() - tAux;

		// Decrypt Results Operations
		tAux = System.currentTimeMillis();
		addResult = addTotal.decrypt(encriptionParameters.getSecretKey());
		ret[6] = System.currentTimeMillis() - tAux;

		tAux = System.currentTimeMillis();
		scaleResult = scaleTotal.decrypt(encriptionParameters.getSecretKey());
		ret[7] = System.currentTimeMillis() - tAux;

		// Integrity Verify
		if (firstValueRetrieved.compareTo(firstValue) != 0)
			Log.d("Principal", "Error en el primer valor");
		if (secondValueRetrieved.compareTo(secondValue) != 0)
			Log.d("Principal", "Error en el segundo valor");
		if (firstValue.add(secondValue).compareTo(addResult) != 0)
			Log.d("Principal", "Error en la suma");
		if (firstValue.multiply(scaleValue).compareTo(scaleResult) != 0)
			Log.d("Principal", "Error en la multiplicacion");

		return ret;
	}

	/**
	 * Metodo para ilustrar resultados de calculate() en la UI
	 */
	public void showResults() {
		String res = "";
		res += "+: " + addResult.toString() + "  ";
		res += "x: " + scaleResult.toString() + "  ";

		TextView tx = (TextView) findViewById(R.id.result);
		tx.setVisibility(View.VISIBLE);
		tx.setText(res);
		((TextView) findViewById(R.id.val1_desencriptado))
				.setText(firstValueRetrieved.toString());
		((TextView) findViewById(R.id.val2_desencriptado))
				.setText(secondValueRetrieved.toString());

		String informe = "Estadisticas:\n";
		informe += "-tiempo E(val1): " + experimentTimes[0] + "\n";
		informe += "-tiempo E(val2): " + experimentTimes[1] + "\n";
		informe += "-tiempo Suma E(val1)*E(val2): " + experimentTimes[2] + "\n";
		informe += "-tiempo Ponderacion E(val1)^k: " + experimentTimes[3]
				+ "\n";
		informe += "-tiempo D(E(val1)): " + experimentTimes[4] + "\n";
		informe += "-tiempo D(E(val2)): " + experimentTimes[5] + "\n";
		informe += "-tiempo DesEncriptacion Suma: " + experimentTimes[6] + "\n";
		informe += "-tiempo DesEncriptacion Ponderacion: " + experimentTimes[7]
				+ "\n";
		TextView tx2 = (TextView) findViewById(R.id.resumenData);
		tx2.setText(informe);
		tx2.setVisibility(View.VISIBLE);
	}

}
