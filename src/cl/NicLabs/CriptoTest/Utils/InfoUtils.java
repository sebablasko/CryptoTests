package cl.NicLabs.CriptoTest.Utils;

import java.text.DateFormat;
import java.util.Calendar;

import android.os.Build;

public class InfoUtils {
	static public String getHtmlPhoneDetails() {
		String res = "";
		res += "Device: <b>" + Build.DEVICE + "</b><br>";
		res += "Proveedor: <b>" + Build.MANUFACTURER + "</b><br>";
		res += "Modelo: <b>" + Build.MODEL + "</b> (" + Build.PRODUCT + ")<br>";
		res += "CPU1: <b>" + Build.CPU_ABI + "</b><br>";
		res += "CPU2: <b>" + Build.CPU_ABI2 + "</b><br>";
		res += "Version Android: <b>" + Build.VERSION.RELEASE + "</b><br>";
		res += "Version Android (SDK): <b>" + Build.VERSION.SDK_INT
				+ "</b><br>";
		res += "Tiempo: "
				+ DateFormat.getDateTimeInstance().format(
						Calendar.getInstance().getTime());
		return res;
	}

	static public String getPhoneDetails() {
		String informe = "Detalles de telefono: \n";
		informe += "Device: " + Build.DEVICE + "\n";
		informe += "Proveedor: " + Build.MANUFACTURER + "\n";
		informe += "Modelo: " + Build.MODEL + " (" + Build.PRODUCT + ")\n";
		informe += "CPU1: " + Build.CPU_ABI + "\n";
		informe += "CPU2: " + Build.CPU_ABI2 + "\n";
		informe += "Version Android: " + Build.VERSION.RELEASE + "\n";
		informe += "Version Android (SDK): " + Build.VERSION.SDK_INT + "\n";
		informe += "Tiempo: "
				+ DateFormat.getDateTimeInstance().format(
						Calendar.getInstance().getTime()) + "\n";
		return informe;
	}
}
