package com.fh.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {

	public static void main(String[] args) {
		String dirName = "d:/FH/topic/";// 创建目录
		FileUtil.createDir(dirName);
	}

	/**
	 * 创建目录
	 * 
	 * @param destDirName
	 *            目标目录名
	 * @return 目录创建成功返回true，否则返回false
	 */
	public static boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.exists()) {
			return false;
		}
		if (!destDirName.endsWith(File.separator)) {
			destDirName = destDirName + File.separator;
		}
		// 创建单个目录
		if (dir.mkdirs()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filePathAndName
	 *            String 文件路径及名称 如c:/fqf.txt
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	public static void delFile(String filePathAndName) {
		try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			java.io.File myDelFile = new java.io.File(filePath);
			myDelFile.delete();

		} catch (Exception e) {
			System.out.println("删除文件操作出错");
			e.printStackTrace();

		}

	}

	/**
	 * 读取到字节数组0
	 * 
	 * @param filePath //路径
	 * @throws IOException
	 */
	public static byte[] getContent(String filePath) throws IOException {
		File file = new File(filePath);
		long fileSize = file.length();
		if (fileSize > Integer.MAX_VALUE) {
			System.out.println("file too big...");
			return null;
		}
		FileInputStream fi = new FileInputStream(file);
		byte[] buffer = new byte[(int) fileSize];
		int offset = 0;
		int numRead = 0;
		while (offset < buffer.length
				&& (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
			offset += numRead;
		}
		// 确保所有数据均被读取
		if (offset != buffer.length) {
			throw new IOException("Could not completely read file "
					+ file.getName());
		}
		fi.close();
		return buffer;
	}

	/**
	 * 读取到字节数组1
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray(String filePath) throws IOException {

		File f = new File(filePath);
		if (!f.exists()) {
			throw new FileNotFoundException(filePath);
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(f));
			int buf_size = 1024;
			byte[] buffer = new byte[buf_size];
			int len = 0;
			while (-1 != (len = in.read(buffer, 0, buf_size))) {
				bos.write(buffer, 0, len);
			}
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			bos.close();
		}
	}

	/**
	 * 读取到字节数组2
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray2(String filePath) throws IOException {

		File f = new File(filePath);
		if (!f.exists()) {
			throw new FileNotFoundException(filePath);
		}

		FileChannel channel = null;
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(f);
			channel = fs.getChannel();
			ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
			while ((channel.read(byteBuffer)) > 0) {
				// do nothing
				// System.out.println("reading");
			}
			return byteBuffer.array();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Mapped File way MappedByteBuffer 可以在处理大文件时，提升性能
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray3(String filePath) throws IOException {

		FileChannel fc = null;
		RandomAccessFile rf = null;
		try {
			rf = new RandomAccessFile(filePath, "r");
			fc = rf.getChannel();
			MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0,
					fc.size()).load();
			//System.out.println(byteBuffer.isLoaded());
			byte[] result = new byte[(int) fc.size()];
			if (byteBuffer.remaining() > 0) {
				// System.out.println("remain");
				byteBuffer.get(result, 0, byteBuffer.remaining());
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				rf.close();
				fc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * isFileExists
	 * 
	 * @param fileName
	 *            fileName
	 * @return boolean : true exists;flase not exists
	 */
	public static boolean isFileExists(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}

	/**
	 * judge is a File
	 * 
	 * @param fileName
	 *            fileName
	 * @return boolean : true exists;flase not exists
	 */
	public static boolean isFile(String fileName) {
		File file = new File(fileName);
		return file.isFile();
	}

	/**
	 * judge is a Directory
	 * 
	 * @param fileName
	 *            fileName
	 * @return boolean : true is a Directory;flase isn't a Directory
	 */
	public static boolean isDirectory(String fileName) {
		File file = new File(fileName);
		return file.isDirectory();
	}

	/**
	 * mkdirs
	 * 
	 * @param fileName
	 *            fileName
	 */
	public static File createFolder(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	/**
	 * create a new File
	 * 
	 * @param fileName
	 *            fileName
	 * @throws IOException
	 *             IOException
	 */
	public static void createNewFile(String fileName) throws IOException {
		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}

	}

	/**
	 * create a new Folder with Date
	 * 
	 * @param path
	 *            path
	 */
	public static String createNowDateFolder(String path) {
		String currentDay = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
		String filePath = path + "/" + currentDay;
		File file = createFolder(filePath);
		return file.getAbsolutePath();
	}

	/**
	 * 删除一个文件或者文件夹。
	 * 
	 * @param filename filename
	 * @throws IOException IOException
	 */ 
	public static void deleteFile(String filename) throws IOException {
		File file = new File(filename);
//		log.trace("Delete file = " + filename);
		if (file.isDirectory()) {
			file.delete();
			throw new IOException(
					"IOException -> BadInputException: not a file.");

		}
		if (file.exists() == false) {
			throw new IOException(
					"IOException -> BadInputException: file is not exist.");
		}
		if (file.delete() == false) {
			throw new IOException("Cannot delete file. filename = " + filename);
		}
	}
	
	/**
	 * get File Name
	 * 
	 * @param filePathName
	 *            filePathName
	 * @return String fileName such as a.txt
	 */
	public static String getFileName(String filePathName) {
		File file = new File(filePathName);
		String fileName = "";
		boolean flag = isFile(filePathName);
		if (flag) {
			fileName = file.getName();
		}
		return fileName;
	}
	
	/**
	 * 创建单个文件夹。
	 * 
	 * @param directory directory
	 * @param ignoreIfExist ignoreIfExist
	 *            true 表示如果文件夹存在就不再创建了。false是重新创建。
	 * @throws IOException IOException
	 */
	public static void createDir(String directory, boolean ignoreIfExist) throws IOException {
		File file = new File(directory);
		if (ignoreIfExist && file.exists()) {
			return;
		}
		if (file.mkdir() == false) {
			throw new IOException("Cannot create the directory = " + directory);
		}
	}

	/**
	 * 创建多个文件夹,也就是可以在不存在的目录中创建文件夹
	 * 
	 * @param directory directory
	 * @param ignoreIfExist ignoreIfExist
	 * @throws IOException IOException
	 */
	public static void createDirs(String directory, boolean ignoreIfExist) throws IOException {
		File file = new File(directory);
		if (ignoreIfExist && file.exists()) {
			return;
		}
		if (file.mkdirs() == false) {
			throw new IOException("Cannot create directories = " + directory);
		}
	}
	
	/**
	 * 删除文件夹及其下面的子文件夹 和所有文件
	 * 
	 * @param directory directory
	 * @throws IOException IOException
	 */
	public static void deleteDir(File directory) throws IOException {
		if (directory.isFile()) {
			throw new IOException(
					"IOException -> BadInputException: not a directory.");
		}
		File[] files = directory.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isFile()) {
					file.delete();
				} else {
					deleteDir(file);
				}
			}
		}// if
		directory.delete();
	}
	
	/**
	 * 获取一个文件的大小
	 * 
	 * @param fileName fileName
	 * @return long length
	 */
	public static long getFileLength(String fileName) {
		long length = 0;
		File file = new File(fileName);
		if (file.isFile()) {
			if (file != null) {
				length = file.length();
			}
		}

		return length;
	}
	
	/**
	 * 将文件清空。
	 * @param srcFilename srcFilename
	 * @throws IOException IOException
	 */
	public static void emptyFile(String srcFilename) throws IOException {
		File srcFile = new File(srcFilename);
		if (!srcFile.exists()) {
			throw new FileNotFoundException("Cannot find the file: "
					+ srcFile.getAbsolutePath());
		}
		if (!srcFile.canWrite()) {
			throw new IOException("Cannot write the file: "
					+ srcFile.getAbsolutePath());
		}
		FileOutputStream outputStream = new FileOutputStream(srcFilename);
		outputStream.close();
	}
	
	/**
	 * Write content to a fileName with the destEncoding 写文件。如果此文件不存在就创建一个。
	 * 
	 * @param content
	 *            String
	 * @param fileName
	 *            String
	 * @param destEncoding
	 *            String
	 * @throws FileNotFoundException
	 * @throws IOException FileNotFoundException IOException
	 */
	public static void writeFile(String content, String fileName,
			String destEncoding) throws IOException {
		File file = null;
		try {
			file = new File(fileName);
			if (!file.exists()) {
				if (file.createNewFile() == false) {
					throw new IOException("create file '" + fileName
							+ "' failure.");
				}
			}
			if (file.isFile() == false) {
				throw new IOException("'" + fileName + "' is not a file.");
			}
			if (file.canWrite() == false) {
				throw new IOException("'" + fileName + "' is a read-only file.");
			}
		} finally {
			// we dont have to close File here
		}
		BufferedWriter out = null;
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			out = new BufferedWriter(new OutputStreamWriter(fos, destEncoding));
			out.write(content);
			out.flush();
		} catch (FileNotFoundException fe) {
//			log.error("Error", fe);
			throw fe;
		} catch (IOException e) {
//			log.error("Error", e);
			throw e;
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException ex) {
			}
		}
	}

	/**
	 * 读取文件的内容，并将文件内容以字符串的形式返回。
	 * 
	 * @param fileName fileName
	 * @param srcEncoding srcEncoding
	 * @return String 
	 * @throws FileNotFoundException
	 * @throws IOException FileNotFoundException
	 */
	public static String readFile(String fileName, String srcEncoding) throws IOException {
		File file = null;
		try {
			file = new File(fileName);
			if (file.isFile() == false) {
				throw new IOException("'" + fileName + "' is not a file.");
			}
		} finally {
			// we dont have to close File here
		}
		BufferedReader reader = null;
		try {
			StringBuffer result = new StringBuffer(1024);
			FileInputStream fis = new FileInputStream(fileName);
			reader = new BufferedReader(new InputStreamReader(fis, srcEncoding));
			char[] block = new char[512];
			while (true) {
				int readLength = reader.read(block);
				if (readLength == -1) {
					break;// end of file
				}
				result.append(block, 0, readLength);
			}
			return result.toString();
		} catch (FileNotFoundException fe) {
//			log.error("Error", fe);
			throw fe;
		} catch (IOException e) {
//			log.error("Error", e);
			throw e;
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 单个文件拷贝。
	 * 
	 * @param srcFilename srcFilename
	 * @param destFilename destFilename
	 * @param overwrite overwrite
	 * @throws IOException IOException
	 */
	public static void copyFile(
			String srcFilename, String destFilename,
			boolean overwrite) throws IOException {
		File srcFile = new File(srcFilename);
		// 首先判断源文件是否存在
		if (!srcFile.exists()) {
			throw new FileNotFoundException("Cannot find the source file: "
					+ srcFile.getAbsolutePath());
		}
		// 判断源文件是否可读
		if (!srcFile.canRead()) {
			throw new IOException("Cannot read the source file: "
					+ srcFile.getAbsolutePath());
		}
		File destFile = new File(destFilename);
		if (overwrite == false) {
			// 目标文件存在就不覆盖
			if (destFile.exists()) {
				return;
			}
		} else {
			// 如果要覆盖已经存在的目标文件，首先判断是否目标文件可写。
			if (destFile.exists()) {
				if (!destFile.canWrite()) {
					throw new IOException("Cannot write the destination file: "
							+ destFile.getAbsolutePath());
				}
			} else {
				// 不存在就创建一个新的空文件。
				if (!destFile.createNewFile()) {
					throw new IOException("Cannot write the destination file: "
							+ destFile.getAbsolutePath());
				}
			}
		}
		BufferedInputStream inputStream = null;
		BufferedOutputStream outputStream = null;
		byte[] block = new byte[1024];
		try {
			inputStream = new BufferedInputStream(new FileInputStream(srcFile));
			outputStream = new BufferedOutputStream(new FileOutputStream(
					destFile));
			while (true) {
				int readLength = inputStream.read(block);
				if (readLength == -1) {
					break;// end of file
				}
				outputStream.write(block, 0, readLength);
			}
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}

	/**
	 * 单个文件拷贝。
	 * 
	 * @param srcFile srcFile
	 * @param destFile destFile
	 * @param overwrite
	 *            是否覆盖目的文件
	 * @throws IOException IOException
	 */
	public static void copyFile(File srcFile, File destFile, boolean overwrite) throws IOException {
		// 首先判断源文件是否存在
		if (!srcFile.exists()) {
			throw new FileNotFoundException("Cannot find the source file: "
					+ srcFile.getAbsolutePath());
		}
		// 判断源文件是否可读
		if (!srcFile.canRead()) {
			throw new IOException("Cannot read the source file: "
					+ srcFile.getAbsolutePath());
		}
		if (overwrite == false) {
			// 目标文件存在就不覆盖
			if (destFile.exists()) {
				return;
			}
		} else {
			// 如果要覆盖已经存在的目标文件，首先判断是否目标文件可写。
			if (destFile.exists()) {
				if (!destFile.canWrite()) {
					throw new IOException("Cannot write the destination file: "
							+ destFile.getAbsolutePath());
				}
			} else {
				// 不存在就创建一个新的空文件。
				if (!destFile.createNewFile()) {
					throw new IOException("Cannot write the destination file: "
							+ destFile.getAbsolutePath());
				}
			}
		}
		BufferedInputStream inputStream = null;
		BufferedOutputStream outputStream = null;
		byte[] block = new byte[1024];
		try {
			inputStream = new BufferedInputStream(new FileInputStream(srcFile));
			outputStream = new BufferedOutputStream(new FileOutputStream(
					destFile));
			while (true) {
				int readLength = inputStream.read(block);
				if (readLength == -1) {
					break;// end of file
				}
				outputStream.write(block, 0, readLength);
			}
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException ex) {
					// just ignore
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException ex) {
					// just ignore
				}
			}
		}
	}

	/**
	 * 拷贝文件，从源文件夹拷贝文件到目的文件夹。 <br>
	 * 参数源文件夹和目的文件夹，最后都不要带文件路径符号，例如：c:/aa正确，c:/aa/错误。
	 * 
	 * @param srcDirName
	 *            源文件夹名称 ,例如：c:/test/aa 或者c://test//aa
	 * @param destDirName
	 *            目的文件夹名称,例如：c:/test/aa 或者c://test//aa
	 * @param overwrite
	 *            是否覆盖目的文件夹下面的文件。
	 * @throws IOException IOException
	 */
	public static void copyFiles(String srcDirName, String destDirName,
			boolean overwrite) throws IOException {
		File srcDir = new File(srcDirName);// 声明源文件夹
		// 首先判断源文件夹是否存在
		if (!srcDir.exists()) {
			throw new FileNotFoundException(
					"Cannot find the source directory: "
							+ srcDir.getAbsolutePath());
		}
		File destDir = new File(destDirName);
		if (overwrite == false) {
			if (destDir.exists()) {
				// do nothing
			} else {
				if (destDir.mkdirs() == false) {
					throw new IOException(
							"Cannot create the destination directories = "
									+ destDir);
				}
			}
		} else {
			// 覆盖存在的目的文件夹
			if (destDir.exists()) {
				// do nothing
			} else {
				// create a new directory
				if (destDir.mkdirs() == false) {
					throw new IOException(
							"Cannot create the destination directories = "
									+ destDir);
				}
			}
		}
		// 循环查找源文件夹目录下面的文件（屏蔽子文件夹），然后将其拷贝到指定的目的文件夹下面。
		File[] srcFiles = srcDir.listFiles();
		if (srcFiles == null || srcFiles.length < 1) {
			// throw new IOException ("Cannot find any file from source
			// directory!!!");
			return;// do nothing
		}
		// 开始复制文件
		int SRCLEN = srcFiles.length;
		for (int i = 0; i < SRCLEN; i++) {
			// File tempSrcFile = srcFiles[i];
			File destFile = new File(destDirName + File.separator
					+ srcFiles[i].getName());
			// 注意构造文件对象时候，文件名字符串中不能包含文件路径分隔符";".
			// log.debug(destFile);
			if (srcFiles[i].isFile()) {
				copyFile(srcFiles[i], destFile, overwrite);
			} else {
				// 在这里进行递归调用，就可以实现子文件夹的拷贝
				copyFiles(srcFiles[i].getAbsolutePath(), destDirName
						+ File.separator + srcFiles[i].getName(), overwrite);
			}
		}
	}

}