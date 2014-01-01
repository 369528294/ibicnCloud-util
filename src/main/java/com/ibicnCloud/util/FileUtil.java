package com.ibicnCloud.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;

//import java.util.zip.ZipEntry;
//import java.util.zip.ZipOutputStream;
//用ant.jar的zip.*可以解决中文文件名问题
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

public class FileUtil {

	/**
	 * 检测指定的文件是否存在；
	 * 
	 * @param path
	 * @return
	 */
	public static boolean isExist(String path) {
		File file = new File(path);
		return file.exists();
	}

	/**
	 * 检测指定的文件是否存在；
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isExist(File file) {
		if (file != null)
			return file.exists();

		return false;
	}

	/**
	 * 按照系统平台规范，格式化目录实际访问路径，规范：d:/directory/；
	 * 
	 * @param realpath
	 * @return
	 */
	public static String formatFolderPath(String realpath) {
		if (StringUtil.isEmpty(realpath))
			return null;
		realpath = realpath.replace('\\', '/');
		if (!realpath.endsWith("/"))
			realpath = realpath + "/";
		return realpath;
	}

	/**
	 * 按照系统平台规范，格式化文件实际访问路径，规范：d:/directory/file.ext；
	 * 
	 * @param realpath
	 * @return
	 */
	public static String formatFilePath(String realpath) {
		if (StringUtil.isEmpty(realpath))
			return null;
		realpath = realpath.replace('\\', '/');
		return realpath;
	}

	/**
	 * 根据指定路径获取一个文件对象 File ；
	 * 
	 * @param path
	 * @return
	 */
	public static File getFile(String path) {
		return new File(path);
	}

	/**
	 * 获取一个指定的文件路径中的文件目录路径，以 "/" 结尾 (无论是一个网络路径URL，还是一个物理路径)；
	 * 
	 * @param path
	 * @return
	 */
	public static String getFolderPath(String path) {
		if (StringUtil.isEmpty(path))
			return null;

		path = path.replace("\\", "/");
		if (path.lastIndexOf("/") > -1)
			return path.substring(0, path.lastIndexOf("/") + 1);

		return path;
	}

	/**
	 * 获取一个指定的文件路径中的文件名，包含扩展名 (无论是一个网络路径URL，还是一个物理路径)；
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileName(String path) {
		if (StringUtil.isEmpty(path))
			return "";

		path = path.replace("\\", "/");
		if (path.lastIndexOf("/") > -1)
			return path.substring(path.lastIndexOf("/") + 1);

		return path;
	}

	/**
	 * 获取一个指定的文件路径中的主文件名，无扩展名 (无论是一个网络路径URL，还是一个物理路径)；
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileMainName(String path) {
		path = getFileName(path);
		if (path.lastIndexOf(".") > -1)
			return path.substring(0, path.indexOf("."));

		return path;
	}

	/**
	 * 根据文件名或文件路径获取文件的扩展名 (无论是一个网络路径URL，还是一个物理路径)；
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileExtName(String path) {
		path = getFileName(path);
		if (path.lastIndexOf(".") > -1)
			return path.substring(path.lastIndexOf(".") + 1);

		return path;
	}

	/**
	 * 获取某个指定路径下的文件名并保存在一个列表中；
	 * 
	 * @param path
	 * @return
	 */
	public static List<String> getFileNameInList(String path) {
		return getFileNameInList(path, null);
	}

	/**
	 * 获取某个指定路径下的文件名并保存在一个列表中，并指定扩展名；
	 * 
	 * @param path
	 * @param ext
	 * @return
	 */
	public static List<String> getFileNameInList(String path, String ext) {
		List<String> list = null;
		File file = new File(path);
		if (file.isDirectory()) {
			list = new LinkedList<String>();
			File[] ary = (File[]) null;
			if (!(StringUtil.isEmpty(ext)))
				ary = file.listFiles(FileNameFilter.getExtensionNameFilter(ext));
			else
				ary = file.listFiles();

			for (int i = 0; i < StringUtil.size(ary); i++) {
				if (ary[i].isFile())
					list.add(ary[i].getName());
			}

		}
		return list;
	}

	/**
	 * 获取某个指定路径下的目录并保存在一个列表中；
	 * 
	 * @param path
	 * @return
	 */
	public static List<String> getFolderNameInList(String path) {
		List<String> list = null;
		File file = new File(path);
		if (file.isDirectory()) {
			list = new LinkedList<String>();
			File[] ary = file.listFiles();
			for (int i = 0; i < StringUtil.size(ary); i++) {
				if (ary[i].isDirectory()) {
					list.add(ary[i].getName());
				}
			}
		}

		return list;
	}

	/**
	 * 重新命名指定文件；
	 * 
	 * @param srcPath
	 * @param aimPath
	 * @throws UtilException
	 */
	public static void renameFile(String srcPath, String aimPath) throws UtilException {
		File srcFile = new File(srcPath);
		if (!(srcFile.exists()))
			throw new UtilException("原始文件不存在：" + srcPath);

		File aimFile = new File(aimPath);
		if (aimFile.exists())
			throw new UtilException("目标文件已存在：" + aimPath);

		srcFile.renameTo(aimFile);
	}

	/**
	 * 重新命名指定文件夹；
	 * 
	 * @param srcPath
	 * @param aimPath
	 * @throws UtilException
	 */
	public static void renameFolder(String srcPath, String aimPath) throws UtilException {
		File srcFile = new File(srcPath);
		if (!(srcFile.exists()))
			throw new UtilException("原始文件夹不存在：" + srcPath);

		File aimFile = new File(aimPath);
		if (aimFile.exists())
			throw new UtilException("目标文件夹已存在：" + aimPath);

		srcFile.renameTo(aimFile);
	}

	/**
	 * 获取某个指定路径下的文件并保存在一个列表中；
	 * 
	 * @param path
	 * @return
	 */
	public static List<File> getFileInList(String path) {
		return getFileInList(path, null);
	}

	/**
	 * 获取某个指定路径下的文件并保存在一个列表中，并指定获取文件的扩展名；
	 * 
	 * @param path
	 * @param ext
	 * @return
	 */
	public static List<File> getFileInList(String path, String ext) {
		List<File> list = null;
		File file = new File(path);
		if (file.isDirectory()) {
			list = new LinkedList<File>();
			File[] ary = (File[]) null;
			if (!(StringUtil.isEmpty(ext)))
				ary = file.listFiles(FileNameFilter.getExtensionNameFilter(ext));
			else
				ary = file.listFiles();

			for (int i = 0; i < StringUtil.size(ary); i++)
				if (ary[i].isFile())
					list.add(ary[i]);
		}

		return list;
	}

	/**
	 * 获取某个指定路径下的文件夹并保存在一个列表中；
	 * 
	 * @param path
	 * @return
	 */
	public static List<File> getFolderInList(String path) {
		List<File> list = null;
		File file = new File(path);
		if (file.isDirectory()) {
			list = new LinkedList<File>();
			File[] ary = file.listFiles();
			for (int i = 0; i < StringUtil.size(ary); i++)
				if (ary[i].isDirectory())
					list.add(ary[i]);
		}

		return list;
	}

	/**
	 * 创建一个指定路径的文件；
	 * 
	 * @param realpath
	 * @throws UtilException
	 */
	public static void createFile(String realpath) throws UtilException {
		File file = new File(realpath);
		if (!(file.exists())) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				throw new UtilException("创建文件失败：", e);
			}
		}
	}

	/**
	 * 删除一个指定路径的文件；
	 * 
	 * @param realpath
	 */
	public static void deleteFile(String realpath) {
		File file = new File(realpath);
		if (file.exists())
			file.delete();
	}

	/**
	 * 创建一个指定路径的文件夹；
	 * 
	 * @param realpath
	 */
	public static void createFolder(String realpath) {
		File folder = new File(realpath);
		if (!(folder.exists())) {
			File parent = folder.getParentFile();
			if (!(parent.exists()))
				parent.mkdirs();
			folder.mkdir();
		}
	}

	/**
	 * 删除一个指定路径的文件夹，如果文件夹内容有文件则一并删除；
	 * 
	 * @param realpath
	 * @throws UtilException
	 */
	public static void deleteFolder(String realpath) throws UtilException {
		deleteFolder(realpath, true);
	}

	/**
	 * 删除一个指定路径的文件夹，并指定如果文件夹内容有文件，是否一并删除；
	 * 
	 * @param realpath
	 * @param deleteAll
	 * @throws UtilException
	 */
	public static void deleteFolder(String realpath, boolean deleteAll) throws UtilException {
		realpath = formatFolderPath(realpath);
		File folder = new File(realpath);
		if ((folder.isDirectory()) && (folder.exists())) {
			File[] aryFile = folder.listFiles();
			if (aryFile.length > 0) {
				if (deleteAll) {
					for (int i = 0; i < StringUtil.size(aryFile); i++) {
						if (aryFile[i].isDirectory())
							deleteFolder(realpath + aryFile[i].getName(), true);
						else
							aryFile[i].delete();
					}
					folder.delete();
				} else {
					throw new UtilException("此文件夹下有其他文件，不能删除！");
				}
			} else {
				folder.delete();
			}
		}
	}

	/**
	 * 拷贝目录，并指定是否保留原来的日期；
	 * 
	 * @param srcDir
	 * @param destDir
	 * @param keepDate
	 * @throws UtilException
	 */
	public static void copyDirectory(String srcDir, String destDir, boolean keepDate) throws UtilException {
		try {
			FileUtils.copyDirectory(new File(srcDir), new File(destDir), keepDate);
		} catch (IOException e) {
			throw new UtilException("拷贝目录失败", e);
		}
	}

	/**
	 * 拷贝文件，并指定是否保留原来的日期；
	 * 
	 * @param src
	 * @param dest
	 * @param keepDate
	 * @throws UtilException
	 */
	public static void copyFile(String src, String dest, boolean keepDate) throws UtilException {
		try {
			FileUtils.copyFile(new File(src), new File(dest), keepDate);
		} catch (IOException e) {
			throw new UtilException("拷贝文件失败", e);
		}
	}

	/**
	 * 抓取文件，从指定的Url地址抓取文件并保存到指定路径；
	 * 
	 * @param url
	 * @param path
	 * @throws UtilException
	 */
	public static void copyUrlToFile(String url, String path) throws UtilException {
		copyUrlToFile(url, new File(path));
	}

	/**
	 * 抓取文件，从指定的Url地址抓取文件并保存到指定文件中；
	 * 
	 * @param url
	 * @param file
	 * @throws UtilException
	 */
	public static void copyUrlToFile(String url, File file) throws UtilException {
		try {
			FileUtils.copyURLToFile(new URL(url), file);
		} catch (IOException e) {
			throw new UtilException("拷贝Url地址文件失败", e);
		}
	}

	/**
	 * 抓取文件，从指定的Url地址抓取文件并保存到指定路径，同时指定保存文件时的字符集；
	 * 
	 * @param url
	 * @param path
	 * @param charset
	 * @throws UtilException
	 */
	public static void copyUrlToFile(String url, String path, String charset) throws UtilException {
		String content = copyUrlToString(url);
		writeStringToFile(new File(path), content, charset);
	}

	/**
	 * 抓取文件，从指定的Url地址抓取文件并保存到字符串中；
	 * 
	 * @param url
	 * @return
	 * @throws UtilException
	 */
	public static String copyUrlToString(String url) throws UtilException {
		StringWriter sw = new StringWriter();
		InputStream input = null;
		try {
			input = new URL(url).openStream();
			IOUtils.copy(input, sw);
		} catch (MalformedURLException e) {
			throw new UtilException("URL文件读取有问题", e);
		} catch (IOException e) {
			throw new UtilException("文件IO问题", e);
		} finally {
			IOUtils.closeQuietly(input);
		}
		return sw.toString();
	}

	/**
	 * 抓取文件，从指定的Url地址抓取文件并保存到字符串中，并指定字符集；
	 * 
	 * @param url
	 * @param charset
	 * @return
	 * @throws UtilException
	 */
	public static String copyUrlToString(String url, String charset) throws UtilException {
		StringWriter sw = new StringWriter();
		InputStream input = null;
		try {
			input = new URL(url).openStream();
			IOUtils.copy(input, sw, charset);
		} catch (IOException e) {
			throw new UtilException("拷贝文件至字符串出错", e);
		} finally {
			IOUtils.closeQuietly(input);
		}

		return sw.toString();
	}

	/**
	 * 将文件内容读取到一个字符串中；
	 * 
	 * @param path
	 * @return
	 * @throws UtilException
	 */
	public static String readFileToString(String path) throws UtilException {
		try {
			return FileUtils.readFileToString(new File(path));
		} catch (IOException e) {
			throw new UtilException("读取文件至字符串出错", e);
		}
	}

	/**
	 * 将文件内容读取到一个字符串中；
	 * 
	 * @param file
	 * @return
	 * @throws UtilException
	 */
	public static String readFileToString(File file) throws UtilException {
		try {
			return FileUtils.readFileToString(file);
		} catch (IOException e) {
			throw new UtilException("读取文件至字符串出错", e);
		}
	}

	/**
	 * 将文件内容读取到一个字符串中，并指定字符编码类型；
	 * 
	 * @param file
	 * @param charset
	 * @return
	 * @throws UtilException
	 */
	public static String readFileToString(File file, String charset) throws UtilException {
		try {
			return FileUtils.readFileToString(file, charset);
		} catch (IOException e) {
			throw new UtilException("读取文件至字符串出错", e);
		}
	}
	
	/**
	 * 将文件内容读取到一个字符串中，并指定字符编码类型；
	 * 
	 * @param file
	 * @param charset
	 * @return
	 * @throws UtilException
	 */
	public static String readFileToString(String file, String charset) throws UtilException {
		try {
			return FileUtils.readFileToString(new File(file), charset);
		} catch (IOException e) {
			throw new UtilException("读取文件至字符串出错", e);
		}
	}

	/**
	 * 将文件中每一行的内容读取到一个列表中；
	 * 
	 * @param path
	 * @return
	 * @throws UtilException
	 */
	public static List<String> readFileToList(String path) throws UtilException {
		try {
			return FileUtils.readLines(new File(path));
		} catch (IOException e) {
			throw new UtilException("", e);
		}
	}

	/**
	 * 将文件中每一行的内容读取到一个列表中；
	 * 
	 * @param file
	 * @return
	 * @throws UtilException
	 */
	public static List<String> readFileToList(File file) throws UtilException {
		try {
			return FileUtils.readLines(file);
		} catch (IOException e) {
			throw new UtilException("", e);
		}
	}

	/**
	 * 将文件中每一行的内容读取到一个列表中，并指定字符编码类型；
	 * 
	 * @param file
	 * @param charset
	 * @return
	 * @throws UtilException
	 */
	public static List<String> readFileToList(File file, String charset) throws UtilException {
		try {
			return FileUtils.readLines(file, charset);
		} catch (IOException e) {
			throw new UtilException("读取文件失败！", e);
		}
	}

	/**
	 * 将字符串中的内容写到一个文件中；
	 * 
	 * @param path
	 * @param content
	 * @throws UtilException
	 */
	public static void writeStringToFile(String path, String content) throws UtilException {
		writeStringToFile(new File(path), content, "UTF-8");
	}

	/**
	 * 将字符串中的内容写到一个文件中；
	 * 
	 * @param file
	 * @param content
	 * @throws UtilException
	 */
	public static void writeStringToFile(File file, String content, String encoding) throws UtilException {
		try {
			FileUtils.writeStringToFile(file, content, encoding);
		} catch (IOException e) {
			throw new UtilException("写入文件错误", e);
		}
	}

	/**
	 * 将列表中的每个项目的 toString() 的内容写到一个文件中；
	 * 
	 * @param path
	 * @param list
	 * @throws UtilException
	 */
	public static void writeListToFile(String path, List list) throws UtilException {
		try {
			FileUtils.writeLines(new File(path), list);
		} catch (IOException e) {
			throw new UtilException("写文件失败！", e);
		}
	}

	/**
	 * 将列表中的每个项目的 toString() 的内容写到一个文件中，并指定每个条目的字符编码类型；
	 * 
	 * @param path
	 * @param list
	 * @param charset
	 * @throws UtilException
	 */
	public static void writeListToFile(String path, List list, String charset) throws UtilException {
		try {
			FileUtils.writeLines(new File(path), list, charset);
		} catch (IOException e) {
			throw new UtilException("写文件失败！", e);
		}
	}

	/**
	 * 将列表中的每个项目的 toString() 的内容写到一个文件中；
	 * 
	 * @param file
	 * @param list
	 * @throws UtilException
	 */
	public static void writeListToFile(File file, List list) throws UtilException {
		try {
			FileUtils.writeLines(file, list);
		} catch (IOException e) {
			throw new UtilException("写文件失败！", e);
		}
	}

	/**
	 * 将列表中的每个项目的 toString() 的内容写到一个文件中，并指定文件和每个条目的字符编码类型；
	 * 
	 * @param file
	 * @param list
	 * @param charset
	 * @throws UtilException
	 */
	public static void writeListToFile(File file, List list, String charset) throws UtilException {
		try {
			FileUtils.writeLines(file, list, charset);
		} catch (IOException e) {
			throw new UtilException("写文件失败！", e);
		}
	}

	/**
	 * 将列表中的每个项目的 toString() 的内容写到一个文件中，并指定文件和每个条目的字符编码类型；
	 * 
	 * @param file
	 * @param fileCharset
	 * @param list
	 * @param lineCharset
	 * @throws UtilException
	 */
	public static void writeListToFile(File file, String fileCharset, List list, String lineCharset) throws UtilException {
		try {
			FileUtils.writeLines(file, fileCharset, list, lineCharset);
		} catch (IOException e) {
			throw new UtilException("写文件失败！", e);
		}
	}

	/**
	 * 将字符串中的内容追加到一个文件中；
	 * 
	 * @param path
	 * @param content
	 * @throws UtilException
	 */
	public static void appendStringToFile(String path, String content) throws UtilException {
		appendStringToFile(new File(path), content);
	}

	/**
	 * 将字符串中的内容追加到一个文件中，并指定编码类型；
	 * 
	 * @param path
	 * @param content
	 * @param charset
	 * @throws UtilException
	 */
	public static void appendStringToFile(String path, String content, String charset) throws UtilException {
		appendStringToFile(new File(path), content, charset);
	}

	/**
	 * 将字符串中的内容追加到一个文件中；
	 * 
	 * @param file
	 * @param content
	 * @throws UtilException
	 */
	public static void appendStringToFile(File file, String content) throws UtilException {
		appendStringToFile(file, content, "UTF-8");
	}

	/**
	 * 将字符串中的内容追加到一个文件中，并指定编码类型；
	 * 
	 * @param file
	 * @param content
	 * @param charset
	 * @throws UtilException
	 */
	public static void appendStringToFile(File file, String content, String charset) throws UtilException {
		try {
			FileWriterWithEncoding fw = new FileWriterWithEncoding(file, charset, true);
			fw.append(content);
			fw.close();
		} catch (IOException e) {
			throw new UtilException("追加到一个文件失败！", e);
		}
		
	}

	/**
	 * 将列表中的每个项目的 toString() 的内容追加到一个文件中；
	 * 
	 * @param path
	 * @param list
	 * @throws UtilException
	 */
	public static void appendListToFile(String path, List list) throws UtilException {
		appendListToFile(new File(path), list);
	}

	/**
	 * 将列表中的每个项目的 toString() 的内容追加到一个文件中；
	 * 
	 * @param file
	 * @param list
	 * @throws UtilException
	 */
	public static void appendListToFile(File file, List list) throws UtilException {
		if (file.exists()) {
			if (file.isDirectory())
				throw new UtilException("文件  '" + file + "' 存在但是个文件夹！");

			if (!file.canWrite())
				throw new UtilException("文件  '" + file + "' 只读！");
		}

		File parent = file.getParentFile();
		if ((parent != null) && (!(parent.exists())) && (!(parent.mkdirs()))) {
			throw new UtilException("文件 '" + file + "' 无法被创建！");
		}

		
		try {
			FileWriterWithEncoding fw = new FileWriterWithEncoding(file, "UTF-8", true);
			for (int i = 0; i < CollectionUtil.size(list); i++) {
				Object o = list.get(i);
				fw.append(o.toString() + "\r\n");
			}
			fw.close();
		} catch (IOException e) {
			throw new UtilException("追加文件失败！", e);
		}
	}
	
	/**
	 * 压缩文件
	 * 
	 * @param srcfile
	 *            File[] 需要压缩的文件列表
	 * @param zipfile
	 *            File 压缩后的文件
	 */
	public static int ZipFiles(File[] srcfile, String zipFile) {
		try {
			// Create the ZIP file
			OutputStream os = new FileOutputStream(zipFile);
			BufferedOutputStream bs = new BufferedOutputStream(os);
			ZipOutputStream out = new ZipOutputStream(bs);
			// Compress the files
			for (int i = 0; i < srcfile.length; i++) {
				zip(srcfile[i], new File(zipFile), out, true, true);
			}
			out.closeEntry();
			// Complete the ZIP file
			out.close();
			// System.out.println("压缩完成.");
			return 1;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
     * @param path 要压缩的路径, 可以是目录, 也可以是文件.
     * @param basePath 如果path是目录,它一般为new File(path), 作用是:使输出的zip文件以此目录为根目录, 如果为null它只压缩文件, 不解压目录.
     * @param zo 压缩输出流
     * @param isRecursive 是否递归
     * @param isOutBlankDir 是否输出空目录, 要使输出空目录为true,同时baseFile不为null.
     * @throws IOException
     */
    public static void zip(File inFile, File basePath, ZipOutputStream zo, boolean isRecursive, boolean isOutBlankDir) throws IOException {
        File[] files = new File[0];
        if(inFile.isDirectory()) {    //是目录
            files = inFile.listFiles();
        } else if(inFile.isFile()) {    //是文件
            files = new File[1];
            files[0] = inFile;
        }
        byte[] buf = new byte[1024];
        int len;
        //System.out.println("baseFile: "+baseFile.getPath());
        for(int i=0; i<files.length; i++) {
            String pathName = "";
            if(basePath != null) {
                if(basePath.isDirectory()) {
                    pathName = files[i].getPath().substring(basePath.getPath().length()+1);
                } else {//文件
                    pathName = files[i].getPath().substring(basePath.getParent().length()+1);
                }
            } else {
                pathName = files[i].getName();
            }
            if(files[i].isDirectory()) {
                if(isOutBlankDir && basePath != null) {    
                    zo.putNextEntry(new ZipEntry(pathName+"/"));    //可以使空目录也放进去
                }
                if(isRecursive) {    //递归
                    zip(files[i], basePath, zo, isRecursive, isOutBlankDir);
                }
            } else {
                FileInputStream fin = new FileInputStream(files[i]);
                zo.putNextEntry(new ZipEntry(pathName));
                while((len=fin.read(buf))>0) {
                    zo.write(buf,0,len);
                }
                fin.close();
            }
        }
    }
}