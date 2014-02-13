package cl.NicLabs.CriptoTest.Tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;

import cl.NicLabs.HomomorphicEncryption.EncryptedValue;
import cl.NicLabs.HomomorphicEncryption.EncryptionParameters;

public class LocalDataManager {

	// methods to read data
	public static BigInteger[] loadRandomNumberData(String path)
			throws ClassNotFoundException, IOException {
		System.out.println(path);
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
		BigInteger[] p = (BigInteger[]) ois.readObject();
		ois.close();
		return p;
	}

	public static EncryptionParameters loadEncryptionParameters(String fullPath)
			throws ClassNotFoundException, IOException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				fullPath));
		EncryptionParameters p = (EncryptionParameters) ois.readObject();
		ois.close();
		return p;
	}

	public static EncryptedValue[] loadHEEncryptedData(String fullPath)
			throws ClassNotFoundException, IOException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				fullPath));
		EncryptedValue[] p = (EncryptedValue[]) ois.readObject();
		ois.close();
		return p;
	}

	public static byte[][] loadRSAorAESEncryptedData(String fullPath)
			throws ClassNotFoundException, IOException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				fullPath));
		byte[][] p = (byte[][]) ois.readObject();
		ois.close();
		return p;
	}
}
