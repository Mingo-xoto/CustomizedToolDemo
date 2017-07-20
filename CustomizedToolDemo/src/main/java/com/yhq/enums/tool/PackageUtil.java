package com.yhq.enums.tool;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.yhq.enums.IEnum;
import com.yhq.enums.constants.EnumConstant;

/**
 * @author HuaQi.Yang
 * @date 2017年7月14日
 */
public class PackageUtil {

	private PackageUtil() {

	}

	private final static String PACKAGE_NAMES[] = { "com.yhq.enums", "com.yhq.enums1" };

	/**
	 * 创建枚举类型类名称和枚举值映射关系表
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年7月14日
	 * @param filePath
	 * @throws ClassNotFoundException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IOException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static void listFilesAndCreateMap(String packageName) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, SecurityException, IOException, NoSuchMethodException, InvocationTargetException {
		String filePath = null;
		Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(packageName.replaceAll("\\.", "/"));
		while (urls.hasMoreElements()) {
			URL url = urls.nextElement();
			if (url == null) {
				continue;
			}
			if ("file".equals(url.getProtocol())) {
				filePath = url.getPath();
				File file = new File(filePath);
				File[] childFiles = file.listFiles();
				if (childFiles == null) {
					return;
				}
				buildByDirectory(packageName, filePath, childFiles);
			} else {
				buildByJar(packageName, url);
			}
		}
	}

	/**
	 * jar形式
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年7月14日
	 * @param packageName
	 * @param url
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 */
	private static void buildByJar(String packageName, URL url) throws IOException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException,
			SecurityException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {
		JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
		JarFile jarFile = jarURLConnection.getJarFile();
		Enumeration<JarEntry> jarEntries = jarFile.entries();
		while (jarEntries.hasMoreElements()) {
			JarEntry jarEntry = jarEntries.nextElement();
			String jarEntryName = jarEntry.getName();
			String clazzName = jarEntryName.replace("/", ".");
			int endIndex = clazzName.lastIndexOf(".");
			String prefix = null;
			if (endIndex > 0) {
				String prefix_name = clazzName.substring(0, endIndex);
				endIndex = prefix_name.lastIndexOf(".");
				if (endIndex > 0) {
					prefix = prefix_name.substring(0, endIndex);
				}
			}
			if (prefix != null && jarEntryName.endsWith(".class")) {
				if (prefix.indexOf(packageName) >= 0) {
					Class<?> clz = Class.forName(clazzName.replaceAll(".class", ""));
					if (needContinue(clz)) {
						continue;
					}
					put(clz);
				}
			}
		}
	}

	/**
	 * 文件夹形式
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年7月14日
	 * @param packageName
	 * @param filePath
	 * @param childFiles
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 * @throws IOException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 */
	private static void buildByDirectory(String packageName, String filePath, File[] childFiles) throws ClassNotFoundException, IllegalAccessException,
			NoSuchFieldException, IOException, SecurityException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {
		for (File file2 : childFiles) {
			if (file2.isDirectory()) {
				listFilesAndCreateMap(file2.getAbsolutePath());
			} else {
				// 获取class类的全包路径名称
				String className = packageName
						+ file2.getAbsolutePath().split("\\.")[0].replaceAll("\\\\", "/").replaceAll(filePath.substring(1), "").replaceAll("/", "\\.");
				Class<?> clz = Class.forName(className);
				if (needContinue(clz)) {
					continue;
				}
				put(clz);
			}
		}
	}

	private static boolean needContinue(Class<?> clz) {
		if (clz.isInterface()) {
			return true;
		}
		Class<?> is[] = clz.getInterfaces();
		for (Class<?> class1 : is) {
			if (class1.hashCode() == IEnum.class.hashCode()) {
				return false;
			}
		}
		return true;
	}

	public static void put(Class<?> clz)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		HashMap<Integer, String> map;
		String name = clz.getName();
		if (EnumConstant.ENUM_MAP.containsKey(name)) {
			map = EnumConstant.ENUM_MAP.get(name);
		} else {
			map = new HashMap<>();
			EnumConstant.ENUM_MAP.put(name, map);
		}
		// 枚举类型名称---->该枚举类型数组
		Method method = clz.getMethod("values");
		IEnum ienums[] = (IEnum[]) method.invoke(clz, (Object[]) null);
		for (IEnum iEnum : ienums) {
			map.put(iEnum.getCode(), iEnum.getText());
		}
	}

	public static void buildMap() throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException, IllegalArgumentException, SecurityException,
			IOException, NoSuchMethodException, InvocationTargetException {
		for (String packageName : PACKAGE_NAMES) {
			listFilesAndCreateMap(packageName);
		}
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		EnumConstant.init();
		for (Entry<String, HashMap<Integer, String>> entry : EnumConstant.ENUM_MAP.entrySet()) {
			System.out.println(entry.getKey());
			HashMap<Integer, String> value = entry.getValue();
			for (Map.Entry<Integer, String> e2 : value.entrySet()) {
				System.out.println(e2.getKey() + ":" + e2.getValue());
			}
		}
	}

}