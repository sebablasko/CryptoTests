package cl.NicLabs.CriptoTest.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import cl.NicLabs.CriptoTest.Tests.InfoData;

import android.net.Uri;
import android.os.Environment;

public class IOUtils {

	// Names of Files and Directories
	static public String appDirectory = "EncriptacionApp";

	//methods
	static public String getPathFiles() {
		if (!Environment.getExternalStorageState().equals("mounted"))
			return null;
		File sdCard = Environment.getExternalStorageDirectory();
		String path = sdCard.getAbsolutePath() + "/" + appDirectory;
		return path;
	}

	static public boolean saveLogFile(String logMsg, String fileName, String dir) {
		File directory, file = null;
		String path = getPathFiles();
		try {
			if (path != null) {
				FileOutputStream fout = null;
				try {
					path += dir;
					directory = new File(path);
					directory.mkdirs();
					file = new File(directory, fileName);
					fout = new FileOutputStream(file);
					OutputStreamWriter ows = new OutputStreamWriter(fout);
					ows.write(logMsg);
					ows.flush();
					ows.close();
					return true;

				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			} else {
				System.out.println("No hay tarjeta!!!");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	static public void zipFolder(String directoryToZip, String outPutName) {
		String sourceFolderName, outputFile;
		try {
			String path;
			if ((path = getPathFiles()) != null) {
				sourceFolderName = path + directoryToZip;
				outputFile = path + InfoData.pathForResults + outPutName;
				
				ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(
						outputFile));
				zos.setLevel(0);
				addFolder(zos, sourceFolderName, sourceFolderName);
				zos.close();			
			} else
				System.out.println("No hay almacenamiento disponible");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static private void addFolder(ZipOutputStream zos, String folderName,
			String baseFolderName) throws Exception {
		File f = new File(folderName);
		if (f.exists()) {
			if (f.isDirectory()) {
				File f2[] = f.listFiles();
				for (int i = 0; i < f2.length; i++) {
					addFolder(zos, f2[i].getAbsolutePath(), baseFolderName);
				}
			} else {
				String entryName = folderName.substring(
						baseFolderName.length() + 1, folderName.length());
				ZipEntry ze = new ZipEntry(entryName);
				zos.putNextEntry(ze);
				FileInputStream in = new FileInputStream(folderName);
				int len;
				byte buffer[] = new byte[1024];
				while ((len = in.read(buffer)) > 0)
					zos.write(buffer, 0, len);
				in.close();
				zos.closeEntry();
			}
		} else {
			System.out.println("File or directory not found " + folderName);
		}
	}

	static public Uri getUriFromZip() {
		String path = getPathFiles();
		if (path == null)
			return null;
		File datos = new File(path + ".zip");
		return Uri.fromFile(datos);
	}

	static public boolean existResults(String resultsFile) {
		String path = getPathFiles();
		if (path == null)
			return false;
		path += resultsFile;
		File datos = new File(path);
		return datos.exists();
	}

	static public boolean deleteData(String resultsFile) {
		String path = getPathFiles();
		if (path == null)
			return false;
		path += resultsFile;
		File directory = new File(path);
		File[] ficheros = directory.listFiles();
		if (ficheros != null)
			for (File f : ficheros)
				f.delete();
		return directory.delete();
	}
}
