package cl.NicLabs.CriptoTest.Tests;

public class InfoData {
	/**
	 * Paths for general results
	 */
	
	public static String pathForResults = "/results/";
	public static String fileNameDataAddingTest = "AddingTest";
	public static String fileNameDatabitLenghtTest = "BitLengthTest";
	public static String fileNameDataParametersTest = "EncryptionParametersTest";
	public static String fileNameDataCryptoOperationsTest = "CryptoOperationsTest";
	
	/**
	 * EncryptionParameters Test Parameters
	 */
	public static int attempsForEncryptionParameters = 4;
	public static int[] lengthsOfDiferentEncryptionParamters = { 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000 };
	
	/**
	 * BitLength Test Parameters
	 */
	public static String fileNameEncryptionParametersStandAlone = "StandAlone.EncryptionParameters";
	public static String pathRandomDataByBitLength = "/data/BitLengthRandomData/";
	public static int differentBitLength = 6;
	public static int randomNumbersForBitLength = 100;
	//building data
	public static int[] bitLengthsValuesTested = { 10, 50, 100, 500, 1000, 5000};
	public static int bitLengthParameter = 1024;
	public static int sValueParameter = 1;
	
	
	/**
	 * CryptoOperations Test Parameters
	 */
	public static String pathDataCryptoOperations = "/data/EncriptedData/CryptoOperations/";
	public static int repetitionsEncryptionParametersCryptoOp = 5;
	public static int numberOfValuesToCryptoOperate = 100;
	//building data
	public static int[] bitLengthsParametersTested = {200, 400, 600, 800, 1000};
	public static int bitLengthValues = 50;
	/**
	 * Adding Test Parameters 
	 */
	public static String pathRandomNumbers = "/data/RandomValues/";
	public static String pathEncryptedData = "/data/EncriptedData/";
	public static String pathEncryptedDataHomomorphicModel = "/data/EncriptedData/HM/";
	public static String pathEncryptedDataAESModel = "/data/EncriptedData/AES/";
	public static String pathEncryptedDataRSAModel = "/data/EncriptedData/RSA/";

	public static String fileNameOriginalData = "Data";
	public static String fileNameAESModelData = "AESData";
	public static String fileNameRSAModelData = "RSAData";
	
	public static int sValue = 1;
	public static int rsaBitLength = 2048;
	public static int bitLengthOfRandomData = 50;
	public static int[] numberOfRandomData = { 10, 50, 100, 500, 1000 };
	public static int bitLengthOfParameterEncryption = 1024;
	public static int numberOfEncryptionParameters = 3;
	
	/**
	 * General Parameters for files
	 */
	public static String extensionEncryptionParameters = ".EncryptionParameters";
	public static String extensionEncryptedData = ".EncryptedData";
	public static String extensionOriginalData = ".OriginalData";
	public static String extensionRandomNumberFile = ".RandomNumbers";
}
