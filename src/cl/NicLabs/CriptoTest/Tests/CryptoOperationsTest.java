package cl.NicLabs.CriptoTest.Tests;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Array;
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

public class CryptoOperationsTest extends GenericTest {

	// Experiment Parameters
	static public boolean running = false;
	public EncryptionParameters encryptionParameters;
	public BigInteger[] values;
	public boolean integrity = true;

	// Results
	public int[] obtainedBitLength = new int[InfoData.repetitionsEncryptionParametersCryptoOp];
	public long[][] encryptTime = new long[InfoData.repetitionsEncryptionParametersCryptoOp][InfoData.numberOfValuesToCryptoOperate];
	public long[][] decryptTime = new long[InfoData.repetitionsEncryptionParametersCryptoOp][InfoData.numberOfValuesToCryptoOperate];
	public long[][] addTime = new long[InfoData.repetitionsEncryptionParametersCryptoOp][InfoData.numberOfValuesToCryptoOperate];
	public long[][] scaleTime = new long[InfoData.repetitionsEncryptionParametersCryptoOp][InfoData.numberOfValuesToCryptoOperate];

	@Override
	public void run() throws Exception {
		running = true;
		startTime = DateFormat.getDateTimeInstance().format(
				Calendar.getInstance().getTime());
		timeStampStart = System.currentTimeMillis();

		/**
		 * 1.- Load The Values to work on every test. 2.- Load
		 * EncryptionParameters from the directory of data for this test. 3.-
		 * mark the cryptoOperations of every value
		 */
		String pathValuesData = IOUtils.getPathFiles()
				+ InfoData.pathDataCryptoOperations
				+ InfoData.fileNameOriginalData
				+ InfoData.extensionOriginalData;
		values = LocalDataManager.loadRandomNumberData(pathValuesData);

		String pathEncryptionParameters = IOUtils.getPathFiles()
				+ InfoData.pathDataCryptoOperations;
		File directory = new File(pathEncryptionParameters);
		File[] files = directory.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(InfoData.extensionEncryptionParameters);
			}
		});
		TestUtils.sortFilesByName(files);
		
		for (int i = 0; i < files.length; ++i) {
			Log.d("cryptoOperations", "working at " + files[i].getName());
			encryptionParameters = LocalDataManager
					.loadEncryptionParameters(files[i].getAbsolutePath());
			obtainedBitLength[i] = encryptionParameters.bitLength;
			TestCryptoOperations(i);
		}

		endTime = DateFormat.getDateTimeInstance().format(
				Calendar.getInstance().getTime());
		timeStampEnd = System.currentTimeMillis();

		saveResults();
		running = false;
	}

	private void TestCryptoOperations(int i) {
		long tAux;

		for (int j = 0; j < values.length; ++j) {
			tAux = System.currentTimeMillis();
			EncryptedValue ev = new EncryptedValue(values[j],
					encryptionParameters);
			encryptTime[i][j] = System.currentTimeMillis() - tAux;

			tAux = System.currentTimeMillis();
			EncryptedValue evAdd = ev.addValue(ev);
			addTime[i][j] = System.currentTimeMillis() - tAux;

			tAux = System.currentTimeMillis();
			EncryptedValue evScale = ev.scale(values[j]);
			scaleTime[i][j] = System.currentTimeMillis() - tAux;

			tAux = System.currentTimeMillis();
			BigInteger recuperedValue = ev.decrypt(encryptionParameters
					.getSecretKey());
			decryptTime[i][j] = System.currentTimeMillis() - tAux;

			// integrity
			integrity &= recuperedValue.compareTo(values[j]) == 0;
			integrity &= evAdd.decrypt(encryptionParameters.getSecretKey())
					.compareTo(values[j].add(values[j])) == 0;
			integrity &= evScale.decrypt(encryptionParameters.getSecretKey())
					.compareTo(values[j].multiply(values[j])) == 0;
		}
	}

	@Override
	protected void saveResults() {
		String filename;
		String path = InfoData.pathForResults
				+ InfoData.fileNameDataCryptoOperationsTest + "/";
		// 0) Values of BitLength Opeated
		filename = "bitLengthObtained.txt";
		IOUtils.saveLogFile(TestUtils.arrayToList(obtainedBitLength), filename,
				path);
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
		IOUtils.zipFolder(path, InfoData.fileNameDataCryptoOperationsTest
				+ ".zip");
	}

	static public String getHtmlDetails() {
		String ret = "";
		ret += "Este test evalúa las operaciones criptográficas sobre un set de datos definido de datos, empleando distintos parametros de encriptacion definidos por el BitLength requerido.<br>";
		ret += "Los bitlength solicitados son: ";
		ret += "Para cada bitlength solicitado se construyen XXXXXX veces los parametros.";
		return ret;
	}

}
