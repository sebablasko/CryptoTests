package cl.NicLabs.CriptoTest.Tests;

import java.io.File;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;

public class TestUtils {
	
	static public void sortFilesByName(File[] files){
		Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				int n1 = extractNumber(o1.getName());
				int n2 = extractNumber(o2.getName());
				return n1 - n2;
			}

			private int extractNumber(String name) {
				int i = 0;
				try {
					int s = name.indexOf('_') + 1;
					int e = name.lastIndexOf('.');
					String number = name.substring(s, e);
					i = Integer.parseInt(number);
				} catch (Exception e) {
					i = 0; // if filename does not match the format
							// then default to 0
				}
				return i;
			}
		});
	}

	static public boolean sameValues(BigInteger[] values) {
		boolean res = true;
		BigInteger v = values[0];
		for (BigInteger i : values)
			res &= v.compareTo(i) == 0 ? true : false;
		return res;
	}

	static public long average(long[] partialTime) {
		long ret = 0;
		for (long l : partialTime)
			ret += l;
		return ret / partialTime.length;
	}

	static public String arrayToList(long[] array) {
		String ret = "";
		for (long s : array)
			ret += s + "\n";
		return ret;
	}

	static public String arrayToList(int[] array) {
		String ret = "";
		for (int s : array)
			ret += s + "\n";
		return ret;
	}

	static public String arrayOfArraysToListString(long[][] matrix) {
		String ret = "";
		for (long[] s : matrix) {
			for (long i : s)
				ret += i + "\t";
			ret += "\n";
		}
		return ret;
	}
	
	static public String arrayOfArraysToListString(int[][] matrix) {
		String ret = "";
		for (int[] s : matrix) {
			for (int i : s)
				ret += i + "\t";
			ret += "\n";
		}
		return ret;
	}
}