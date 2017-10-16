package com.vfaceuser.utils;

import android.os.Environment;
import android.os.StatFs;

import com.vfaceuser.commen.MyApplication;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

public class FileManager {

	// CIQ root directory in SD/RAM

	private final static String CIQ = ".CIQ2";

	private final static String COURSE_PATH = "Course/";
	private final static String COVER = "Cover/";

	private final static String LEARNING_PATH = "Learning/";

	private final static String CATEGORY_PATH = "Category/";

	private final static String WELCOME_PATH = "Welcome/";

	private final static String AVATAR_PATH = "Avatar/";

	private String ciqBaseDirectory = "";

	private String userFullPath = "";

	private String learningFullPath = "";

	private String categoryFullPath = "";

	private String welcomeFullPath = "";

	private String avatarFullPath = "";

	private static FileManager fm = null;

	private FileManager() {
	}

	public static FileManager getInstance() {
		if (fm == null) {
			fm = new FileManager();
			String deviceDirectory = fm.getDeviceDirectory();
			fm.initBasePath(deviceDirectory);
		}
		return fm;
	}
	
	/**
	 * ��ȡʣ��ռ�
	 * @return
	 */
	public long getFreeSpace(){
		String path = getDeviceDirectory();
		long space = getAvailableSize(path);
		return space / 1024/ 1024;
	}

	private String getDeviceDirectory() {
		String deviceDirectory;
		if (isSDExisted()) {
			deviceDirectory = Environment.getExternalStorageDirectory()
					.getPath();
		} else {
			deviceDirectory = MyApplication.getInstance().getFilesDir()
					.getAbsolutePath();
		}
		return deviceDirectory;
	}

	/**
	 * ɾ��CIQ�ϰ汾
	 * 
	 * @param base
	 */
	void deleteOldCIQFilePath(String base) {
		ciqBaseDirectory = base + File.separator + "CIQ" + File.separator;
		File file = new File(ciqBaseDirectory);
		if (file.isDirectory() && file.exists()) {
			// deleteFile(file);
			deleteDirectory(ciqBaseDirectory);
		}

		ciqBaseDirectory = base + File.separator + ".CIQ" + File.separator;
		file = new File(ciqBaseDirectory);
		if (file.isDirectory() && file.exists()) {
			// deleteFile(file);
			deleteDirectory(ciqBaseDirectory);
		}
	}

	public boolean initBasePath(String base) {
		// Category & welcome images paths are common path.
		try {
			deleteOldCIQFilePath(base);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ciqBaseDirectory = base + File.separator + CIQ + File.separator;
		File file = new File(ciqBaseDirectory + CATEGORY_PATH);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		categoryFullPath = ciqBaseDirectory + CATEGORY_PATH;

		file = new File(ciqBaseDirectory + WELCOME_PATH);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		welcomeFullPath = ciqBaseDirectory + WELCOME_PATH;

		file = new File(ciqBaseDirectory + AVATAR_PATH);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		avatarFullPath = ciqBaseDirectory + AVATAR_PATH;

		return true;
	}

	// ---learning path is user specific---//
	public boolean initLearningPath(String userID) {
		File file;
		// CIQ user path
		file = new File(ciqBaseDirectory + userID);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		// Quiz & Learning path
		userFullPath = ciqBaseDirectory + userID;
		learningFullPath = userFullPath + File.separator + LEARNING_PATH;
		file = new File(learningFullPath);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		return true;
	}

	/**
	 * Delete user folder for leave office user
	 * 
	 * @param userID
	 * @return
	 */
	public boolean deleteUserFolder(String userID) {
		File file = new File(ciqBaseDirectory + userID);
		if (file.isDirectory() && file.exists()) {
			return file.delete();
		}
		return true;
	}

	public String createCoverPath() {

		File file = new File(ciqBaseDirectory + COVER);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		return file.getAbsolutePath() + File.separator;
	}

	public String createBookPath(int bookID) {
		return createBookPath(bookID + "");
	}

	public boolean deleteBookPath(String bookId) {
		File file = new File(ciqBaseDirectory + COURSE_PATH + bookId);
		if (file.isDirectory() && file.exists()) {
			return file.delete();
		}
		return true;
	}

	public String createBookPath(String bookID) {
		File file = new File(ciqBaseDirectory + COURSE_PATH + bookID);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		return file.getAbsolutePath() + File.separator;
	}

	public String getUserPath() {
		return userFullPath;
	}

	public String getLearningPath() {
		return learningFullPath;
	}

	public String getCategoryIconPath() {
		return categoryFullPath;
	}

	public String getWelcomeIconPath() {
		return welcomeFullPath;
	}

	public String getAvatarPath() {
		return avatarFullPath;
	}

	// ---common functions---//
	public boolean isFileExisted(String path) {
		File file = new File(path);
		return file.exists();
	}

	public boolean isSDExisted() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	// --MD5 generate for a file
	public byte[] createChecksum(String filename) throws Exception {

		InputStream fis = new FileInputStream(filename);

		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;

		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);

		fis.close();
		return complete.digest();
	}

	// get MD5 checksum :a byte array to a HEX string
	public String getMD5Checksum(String filename) throws Exception {
		byte[] b = createChecksum(filename);
		String result = "";

		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}

	public void deleteFile(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}

		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				deleteFile(childFiles[i]);
			}
			file.delete();
		}
	}

	public String deleteDirectory(String dir) {
		ShellExecute cmdexe = new ShellExecute();
		String result = "";
		try {
			String[] ARGS = { "rm", "-R", dir };
			result = cmdexe.execute(ARGS, "/");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private class ShellExecute {
		/*
		 * args[0] : shell ���� ��"ls" ��"ls -1"; args[1] : ����ִ��·�� ��"/" ;
		 */
		public String execute(String[] cmmand, String directory)
				throws IOException {
			String result = "";
			try {
				ProcessBuilder builder = new ProcessBuilder(cmmand);

				if (directory != null)
					builder.directory(new File(directory));
				builder.redirectErrorStream(true);
				Process process = builder.start();

				// �õ�����ִ�к�Ľ��
				InputStream is = process.getInputStream();
				byte[] buffer = new byte[1024];
				while (is.read(buffer) != -1) {
					result = result + new String(buffer);
				}
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	// ���ļ�
	public String readFile(String fileName) {
		String res = "";
		File file = new File(fileName);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			int length = fis.available();
			byte[] buffer = new byte[length];
			fis.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return res;
	}

	// д�ļ�
	public void writeFile(String fileName, String write_str) throws IOException {
		File file = new File(fileName);
		FileOutputStream fos = new FileOutputStream(file);
		byte[] bytes = write_str.getBytes();
		fos.write(bytes);
		fos.close();
	}

	public void deleteAllCourse() {
		try {
			File file = new File(ciqBaseDirectory + COURSE_PATH);
			file.delete();
			file.mkdirs();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 
     * ����ʣ��ռ� 
     * @param path 
     * @return 
     */  
    @SuppressWarnings("deprecation")
	private static long getAvailableSize(String path)  
    {  
        StatFs fileStats = new StatFs(path);  
        fileStats.restat(path);  
        return (long) fileStats.getAvailableBlocks() * fileStats.getBlockSize(); // ע����fileStats.getFreeBlocks()������  
    }  
}
