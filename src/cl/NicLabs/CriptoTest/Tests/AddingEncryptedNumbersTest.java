package cl.NicLabs.CriptoTest.Tests;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
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
import cl.NicLabs.OtherCryptoModels.AESModel;
import cl.NicLabs.OtherCryptoModels.RSAModel;

public class AddingEncryptedNumbersTest extends GenericTest {

	// Experiment Parameters
	static public boolean running = false;
	public EncryptionParameters[] encryptedParameters;
	public EncryptedValue[] encryptedValues;

	// times per experiment
	public long tAux = 0;
	public long[] simpleAdding = new long[InfoData.numberOfRandomData.length];
	public long[] AESAdding = new long[InfoData.numberOfRandomData.length];
	public long[] RSAAdding = new long[InfoData.numberOfRandomData.length];
	public long[][] HEAdding = new long[InfoData.numberOfRandomData.length][];

	// results
	public BigInteger[] resultsTotal = new BigInteger[4];

	// methods
	@Override
	public void run() throws Exception {
		running = true;
		startTime = DateFormat.getDateTimeInstance().format(
				Calendar.getInstance().getTime());
		timeStampStart = System.currentTimeMillis();

		/**
		 * Comparison between: -Simple adding numbers -Decrypt AES numbers and
		 * simple adding -Encrypt numbers to RSA, then Decrypt RSA numbers and
		 * finally simple adding -Homomorphic adding numbers and then decrypt
		 */

		simpleAddingNumbers();
		Log.d("addingTest", "Finished SimpleAddingTest");
		AESaddingNumbers();
		Log.d("addingTest", "Finished AESAddingTest");
		RSAaddingNumbers();
		Log.d("addingTest", "Finished RSAAddingTest");
		HEaddingNumbers();
		Log.d("addingTest", "Finished HEAddingTest");

		endTime = DateFormat.getDateTimeInstance().format(
				Calendar.getInstance().getTime());
		timeStampEnd = System.currentTimeMillis();

		saveResults();
		running = false;

		// long[] averageHEAdding = new long[HEAdding.length];
		// for (int i = 0; i < averageHEAdding.length; ++i)
		// averageHEAdding[i] = average(HEAdding[i]);
		// System.out.println("Simple:\t" + Arrays.toString(simpleAdding));
		// System.out.println("AES:\t" + Arrays.toString(AESAdding));
		// System.out.println("RSA:\t" + Arrays.toString(RSAAdding));
		// System.out.println("HE:\t" + Arrays.toString(averageHEAdding));
		// System.out.println("integrity: " + sameValues(resultsTotal));
	}

	@Override
	protected void saveResults() {
		String filename;
		String path = InfoData.pathForResults + InfoData.fileNameDataAddingTest
				+ "/";
		// 0) Number of random numbers added
		filename = "datasetQuantity.txt";
		IOUtils.saveLogFile(TestUtils.arrayToList(InfoData.numberOfRandomData),
				filename, path);
		// 1) SimpleAdding Results
		filename = "simpleTest.txt";
		IOUtils.saveLogFile(TestUtils.arrayToList(simpleAdding), filename, path);
		// 2) AESAdding Results
		filename = "AESTest.txt";
		IOUtils.saveLogFile(TestUtils.arrayToList(AESAdding), filename, path);
		// 3) RSAAdding Results
		filename = "RSATest.txt";
		IOUtils.saveLogFile(TestUtils.arrayToList(RSAAdding), filename, path);
		// 4) HEAdding Results
		filename = "HETest.txt";
		IOUtils.saveLogFile(TestUtils.arrayOfArraysToListString(HEAdding),
				filename, path);
		// 5) Integrity
		filename = "Integrity.txt";
		IOUtils.saveLogFile(TestUtils.sameValues(resultsTotal) + "", filename,
				path);
		// 6) device Details
		filename = "deviceDetails.txt";
		IOUtils.saveLogFile(InfoUtils.getPhoneDetails(), filename, path);
		// 7) zip
		IOUtils.zipFolder(path, InfoData.fileNameDataAddingTest + ".zip");
	}

	/**
	 * 1.-Load random numbers <start time> 2.-add all the numbers <end time>
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void simpleAddingNumbers() throws Exception {
		for (int i = 0; i < InfoData.numberOfRandomData.length; ++i) {
			String filename = InfoData.numberOfRandomData[i]
					+ InfoData.extensionRandomNumberFile;
			String path = IOUtils.getPathFiles() + InfoData.pathRandomNumbers
					+ filename;
			BigInteger[] values = LocalDataManager.loadRandomNumberData(path);
			// calc the total value
			tAux = System.currentTimeMillis();
			BigInteger total = BigInteger.ZERO;
			for (BigInteger j : values) {
				total = total.add(j);
				
			}
			simpleAdding[i] = System.currentTimeMillis() - tAux;
			resultsTotal[0] = total;
			// System.out.println(total);
		}
	}

	/**
	 * 1.- load encrypted data <start time> 2.- decrypt every data 3.- add it to
	 * the total <stop time>
	 */
	private void AESaddingNumbers() throws Exception {
		for (int i = 0; i < InfoData.numberOfRandomData.length; ++i) {
			String path = IOUtils.getPathFiles()
					+ InfoData.pathEncryptedDataAESModel
					+ InfoData.numberOfRandomData[i] + "/";
			File directory = new File(path);
			File[] files = directory.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(InfoData.extensionEncryptedData);
				}
			});
			for (File file : files) {
				String pathEncryptedData = file.getAbsolutePath();
				// get an instance of aes
				AESModel aes = new AESModel();
				// load encrypted Data
				byte[][] encryptedValues = LocalDataManager
						.loadRSAorAESEncryptedData(pathEncryptedData);
				//decrypt encrypted data array
				tAux = System.currentTimeMillis();
				BigInteger total = BigInteger.ZERO;
				for (int j = 0; j < encryptedValues.length; ++j) {
					total = total.add(aes.decrypt(encryptedValues[j]));
				}
				AESAdding[i] = System.currentTimeMillis() - tAux;
				resultsTotal[1] = total;
				// System.out.println(total);
			}
		}
	}

	/**
	 * 1.-Load random data file 2.-encrypt random data <start time> 3.-decrypt
	 * every encrypted value 4.- adding it to the total <stop time>
	 */
	private void RSAaddingNumbers() throws Exception {
		for (int i = 0; i < InfoData.numberOfRandomData.length; ++i) {
			// get an instance of RSA
			RSAModel rsa = new RSAModel(InfoData.rsaBitLength);
			// get the data
			String filename = InfoData.numberOfRandomData[i]
					+ InfoData.extensionRandomNumberFile;
			String path = IOUtils.getPathFiles() + InfoData.pathRandomNumbers
					+ filename;
			BigInteger[] originalData = LocalDataManager
					.loadRandomNumberData(path);
			// encrypt the data
			byte[][] encryptedValues = new byte[originalData.length][];
			for (int j = 0; j < originalData.length; ++j) {
				encryptedValues[j] = rsa.encrypt(originalData[j]);
			}
			// start time and decrypt
			tAux = System.currentTimeMillis();
			BigInteger total = BigInteger.ZERO;
			for (int j = 0; j < encryptedValues.length; ++j) {
				total = total.add(rsa.decrypt(encryptedValues[j]));
			}
			RSAAdding[i] = System.currentTimeMillis() - tAux;
			resultsTotal[2] = total;
			// System.out.println(total);
		}
	}

	/**
	 * 1.- Load encryptionParameters 2.- foreach encryption parameter load
	 * Encrypted data <start partial time> 3.-add the encrypted data 4.- decrypt
	 * the result <stop partial time> 5.- save average of partial times
	 */
	private void HEaddingNumbers() throws Exception {
		for (int i = 0; i < InfoData.numberOfRandomData.length; ++i) {
			String path = IOUtils.getPathFiles()
					+ InfoData.pathEncryptedDataHomomorphicModel
					+ InfoData.numberOfRandomData[i] + "/";
			File directory = new File(path);
			File[] files = directory.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(InfoData.extensionEncryptedData);
				}
			});
			// record to save partial time
			long partialTime[] = new long[files.length];
			BigInteger partialResult[] = new BigInteger[files.length];
			TestUtils.sortFilesByName(files);
			
			int pos = 0;
			for (File file : files) {
				Log.d("addingTest", "working at " + pos
						+ " EncryptionParameter");
				String filename = file.getName().split("[.]")[0];
				String pathEncryptionParameters = path + filename
						+ InfoData.extensionEncryptionParameters;
				String pathEncryptedData = path + filename
						+ InfoData.extensionEncryptedData;
				// load EncryptionParameters and encryptedData
				EncryptionParameters parameters = LocalDataManager
						.loadEncryptionParameters(pathEncryptionParameters);
				EncryptedValue[] encryptedValues = LocalDataManager
						.loadHEEncryptedData(pathEncryptedData);
				// decrypt encrypted data array
				EncryptedValue total = new EncryptedValue(new BigInteger("0"),
						parameters);
				tAux = System.currentTimeMillis();
				BigInteger finalTotal = BigInteger.ZERO;
				for (int j = 0; j < encryptedValues.length; ++j) {
					total = total.addValue(encryptedValues[j]);
				}
				finalTotal = total.decrypt(parameters.getSecretKey());
				partialTime[pos] = System.currentTimeMillis() - tAux;

				partialResult[pos++] = finalTotal;
				// System.out.println(finalTotal);
			}
			resultsTotal[3] = TestUtils.sameValues(partialResult) ? partialResult[0]
					: BigInteger.ZERO;
			HEAdding[i] = partialTime;
		}
	}

	static public String getHtmlDetails() {
		String ret = "";
		ret += "Este Test evalúa el desempeño de los enfoques criptográficos: ";
		ret += "<b>RSA</b>, <b>AES</b> y <b>Encriptación Homomórfica</b> ";
		ret += "en comparación con la <b>suma directa</b> para la operacion de suma sobre un conjunto "
				+ "de valores previamente codificados segun cada enfoque presentado.<br><br>";
		ret += "Los experimentos se realizan sumando grupos de numeros aleatorios con los valores descritos segun el arreglo:<br> "
				+ "<b>"
				+ Arrays.toString(InfoData.numberOfRandomData)
				+ "</b><br>";
		return ret;
	}
}