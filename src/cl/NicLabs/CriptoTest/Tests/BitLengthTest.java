package cl.NicLabs.CriptoTest.Tests;

import java.io.File;
import java.math.BigInteger;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;

import android.util.Log;
import cl.NicLabs.CriptoTest.Utils.IOUtils;
import cl.NicLabs.CriptoTest.Utils.InfoUtils;
import cl.NicLabs.HomomorphicEncryption.EncryptedValue;
import cl.NicLabs.HomomorphicEncryption.EncryptionParameters;

public class BitLengthTest extends GenericTest {

	// Experiment Parameters
	static public boolean running = false;
	public EncryptionParameters encryptionParameters;
	public boolean integrity = true;

	// results for experiment
	public int[] bitLengths = new int[InfoData.differentBitLength];
	public long[][] encryptTime = new long[InfoData.differentBitLength][InfoData.randomNumbersForBitLength];
	public long[][] decryptTime = new long[InfoData.differentBitLength][InfoData.randomNumbersForBitLength];
	public long[][] addTime = new long[InfoData.differentBitLength][InfoData.randomNumbersForBitLength];
	public long[][] scaleTime = new long[InfoData.differentBitLength][InfoData.randomNumbersForBitLength];

	@Override
	public void run() throws Exception {
		running = true;
		startTime = DateFormat.getDateTimeInstance().format(
				Calendar.getInstance().getTime());
		timeStampStart = System.currentTimeMillis();

		/**
		 * Read an EncryptionParameters and use it to test the cryptoOperations
		 * over a set of random values with diferent bitlength. Foreach value we
		 * review the time for: Encrypt, add (it's self), scale (it's self) and
		 * Decrypt
		 */
		String parametersPath = IOUtils.getPathFiles()
				+ InfoData.pathEncryptedData
				+ InfoData.fileNameEncryptionParametersStandAlone;
		encryptionParameters = LocalDataManager
				.loadEncryptionParameters(parametersPath);
		String valuesPath = IOUtils.getPathFiles()
				+ InfoData.pathRandomDataByBitLength;
		File directory = new File(valuesPath);
		File[] files = directory.listFiles();
		TestUtils.sortFilesByName(files);

		for (int i = 0; i < files.length; ++i) {
			Log.d("bitLengthTest", "working at file: " + files[i].getName());

			bitLengths[i] = Integer
					.parseInt(files[i].getName().split("[.]")[0]);
			BigInteger[] values = LocalDataManager
					.loadRandomNumberData(files[i].getAbsolutePath());
			for (int j = 0; j < InfoData.randomNumbersForBitLength; ++j) {
				measureTimes(values[j], i, j);
			}

		}

		endTime = DateFormat.getDateTimeInstance().format(
				Calendar.getInstance().getTime());
		timeStampEnd = System.currentTimeMillis();

		saveResults();
		running = false;
	}

	private void measureTimes(BigInteger value, int i, int j) {
		long tAux;
		tAux = System.currentTimeMillis();
		EncryptedValue ev = new EncryptedValue(value, encryptionParameters);
		encryptTime[i][j] = System.currentTimeMillis() - tAux;

		tAux = System.currentTimeMillis();
		EncryptedValue evAdd = ev.addValue(ev);
		addTime[i][j] = System.currentTimeMillis() - tAux;

		tAux = System.currentTimeMillis();
		EncryptedValue evScale = ev.scale(value);
		scaleTime[i][j] = System.currentTimeMillis() - tAux;

		tAux = System.currentTimeMillis();
		BigInteger recuperedValue = ev.decrypt(encryptionParameters
				.getSecretKey());
		decryptTime[i][j] = System.currentTimeMillis() - tAux;

		// integrity
		integrity &= recuperedValue.compareTo(value) == 0;
		integrity &= evAdd.decrypt(encryptionParameters.getSecretKey())
				.compareTo(value.add(value)) == 0;
		integrity &= evScale.decrypt(encryptionParameters.getSecretKey())
				.compareTo(value.multiply(value)) == 0;

	}

	@Override
	protected void saveResults() {
		String filename;
		String path = InfoData.pathForResults
				+ InfoData.fileNameDatabitLenghtTest + "/";
		// 0) Values of BitLength Opeated
		filename = "differentBitLength.txt";
		IOUtils.saveLogFile(TestUtils.arrayToList(bitLengths), filename, path);
		// 1) times for Encrypt
		filename = "EncryptTimes.txt";
		IOUtils.saveLogFile(TestUtils.arrayOfArraysToListString(encryptTime),
				filename, path);
		// 2) times for Add
		filename = "AddTimes.txt";
		IOUtils.saveLogFile(TestUtils.arrayOfArraysToListString(addTime),
				filename, path);
		// 3) times for scale
		filename = "ScaleTimes.txt";
		IOUtils.saveLogFile(TestUtils.arrayOfArraysToListString(scaleTime),
				filename, path);
		// 4) times for Decrypt
		filename = "Decrypt.txt";
		IOUtils.saveLogFile(TestUtils.arrayOfArraysToListString(decryptTime),
				filename, path);
		// 5) integrity
		filename = "Integrity.txt";
		IOUtils.saveLogFile(integrity + "", filename, path);
		// 6) device Details
		filename = "deviceDetails.txt";
		IOUtils.saveLogFile(InfoUtils.getPhoneDetails(), filename, path);
		// 7) zip
		IOUtils.zipFolder(path, InfoData.fileNameDatabitLenghtTest + ".zip");
	}

	// TODO: TERMINARLOOOO
	static public String getHtmlDetails() {
		String ret = "";
		ret += "Este Test verifica el comportamiento de las operaciones homomórficas "
				+ "sobre valores de distinto tamaño de bitLength.";
		return ret;
	}

}
