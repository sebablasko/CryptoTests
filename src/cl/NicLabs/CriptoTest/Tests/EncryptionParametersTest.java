package cl.NicLabs.CriptoTest.Tests;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;

import android.util.Log;

import cl.NicLabs.CriptoTest.Utils.IOUtils;
import cl.NicLabs.CriptoTest.Utils.InfoUtils;
import cl.NicLabs.HomomorphicEncryption.EncryptionParameters;

public class EncryptionParametersTest extends GenericTest {

	// Experiment Parameters
	static public boolean running = false;
	public EncryptionParameters encryptedParameters[][];

	// results for experiment
	public int[] bitLengthRequired = new int[InfoData.lengthsOfDiferentEncryptionParamters.length];
	public int[][] bitLengthResulted = new int[InfoData.lengthsOfDiferentEncryptionParamters.length][InfoData.attempsForEncryptionParameters];
	public long[][] timesPerConstruction = new long[InfoData.lengthsOfDiferentEncryptionParamters.length][InfoData.attempsForEncryptionParameters];
	public int[][] attempsPerPrimeSearch = new int[InfoData.lengthsOfDiferentEncryptionParamters.length][InfoData.attempsForEncryptionParameters];

	@Override
	public void run() throws Exception {
		running = true;
		startTime = DateFormat.getDateTimeInstance().format(
				Calendar.getInstance().getTime());
		timeStampStart = System.currentTimeMillis();

		/**
		 * Generate multiEncription Parameters and save the stats for the
		 * creation of every one
		 */
		encryptedParameters = new EncryptionParameters[InfoData.lengthsOfDiferentEncryptionParamters.length][InfoData.attempsForEncryptionParameters];
		for (int i = 0; i < InfoData.lengthsOfDiferentEncryptionParamters.length; ++i) {
			Log.d("encryptionParametersTest", "building parameters of length "
					+ InfoData.lengthsOfDiferentEncryptionParamters[i]);
			for (int j = 0; j < InfoData.attempsForEncryptionParameters; ++j) {
				encryptedParameters[i][j] = new EncryptionParameters(
						InfoData.lengthsOfDiferentEncryptionParamters[i],
						InfoData.sValue);
			}
		}
		resumeResults();
		Log.d("encryptionParametersTest", "done");

		endTime = DateFormat.getDateTimeInstance().format(
				Calendar.getInstance().getTime());
		timeStampEnd = System.currentTimeMillis();

		saveResults();
		running = false;
	}

	private void resumeResults() {
		for (int i = 0; i < encryptedParameters.length; ++i) {
			bitLengthRequired[i] = encryptedParameters[i][0].requiredBitLength;
			for (int j = 0; j < encryptedParameters[0].length; ++j) {
				bitLengthResulted[i][j] = encryptedParameters[i][j].bitLength;
				timesPerConstruction[i][j] = encryptedParameters[i][j].constructionTime;
				attempsPerPrimeSearch[i][j] = encryptedParameters[i][j].attemptsPrimeFactors;
			}
		}
	}

	@Override
	protected void saveResults() {
		String filename;
		String path = InfoData.pathForResults
				+ InfoData.fileNameDataParametersTest + "/";
		// 0) Total of attemps to build EncryptionParameters
		filename = "attempsToBuildParameters.txt";
		IOUtils.saveLogFile(InfoData.attempsForEncryptionParameters + "",
				filename, path);
		// 1) Lengths required
		filename = "LengthRequired.txt";
		IOUtils.saveLogFile(TestUtils.arrayToList(bitLengthRequired), filename,
				path);
		// 2) Lengths Resulted
		filename = "LengthResulted.txt";
		IOUtils.saveLogFile(
				TestUtils.arrayOfArraysToListString(bitLengthResulted),
				filename, path);
		// 3) Times per construction
		filename = "timesPerConstruction.txt";
		IOUtils.saveLogFile(
				TestUtils.arrayOfArraysToListString(timesPerConstruction),
				filename, path);
		// 4) Attemps Per Prime Search
		filename = "attempsPerPrimeSearch.txt";
		IOUtils.saveLogFile(
				TestUtils.arrayOfArraysToListString(attempsPerPrimeSearch),
				filename, path);
		// 5) device Details
		filename = "deviceDetails.txt";
		IOUtils.saveLogFile(InfoUtils.getPhoneDetails(), filename, path);
		// 6) zip
		IOUtils.zipFolder(path, InfoData.fileNameDataParametersTest + ".zip");

	}

	static public String getHtmlDetails() {
		String ret = "";
		ret += "Este Test evalúa la construccion de parametros de encriptacion del modelo homomórfico.<br>";
		ret += "Los parametros de prueba son:<br>";
		ret += "Valores de bitLength a solicitar: "
				+ "<b>" + Arrays.toString(InfoData.lengthsOfDiferentEncryptionParamters) +"</b><br>"
				+ "Repeticiones de intento de construcción: "
				+ "<b>" +InfoData.attempsForEncryptionParameters + "</b><br>";
		return ret;
	}

}
