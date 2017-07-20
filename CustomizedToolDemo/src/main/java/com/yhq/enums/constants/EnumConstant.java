package com.yhq.enums.constants;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.yhq.enums.MsgSendForm;
import com.yhq.enums.tool.PackageUtil;

/**
 * @author HuaQi.Yang
 * @date 2017年7月14日
 */
public class EnumConstant {

	public static final HashMap<String, HashMap<Integer, String>> ENUM_MAP = new HashMap<>();

	private static boolean INIT_SUCCESS = false;

	private EnumConstant() {

	}

	/**
	 * 初始化枚举值映射集
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年7月17日
	 * @return
	 */
	public static boolean init() {
		if (!INIT_SUCCESS) {
			try {
				PackageUtil.buildMap();
				INIT_SUCCESS = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return INIT_SUCCESS;
	}

	static {
		init();
	}

	public static void main(String[] args) {
		Class<?> clz = MsgSendForm.class;
		Field fields[] = clz.getDeclaredFields();
		for (Field field : fields) {
			System.out.println(field.getName());
		}
	}
}