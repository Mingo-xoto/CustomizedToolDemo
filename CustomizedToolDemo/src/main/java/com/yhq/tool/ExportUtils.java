package com.yhq.tool;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.yhq.enums.constants.EnumConstant;

/**
 * 导出Dto数据通用工具
 * 
 * @author HuaQi.Yang
 * @date 2017年7月12日
 */
public class ExportUtils<Vo extends Object> {
	public ExportUtils() {

	}

	private static final Pattern INT_PATTERN = Pattern.compile("[0-9]+");

	/**
	 * 元素在数组中
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年7月12日
	 * @param arrays
	 * @param element
	 * @return
	 */
	private boolean contain(Integer[] arrays, int element) {
		for (int i : arrays) {
			if (element == i) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 元素不在数组中
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年7月12日
	 * @param arrays
	 * @param element
	 * @return
	 */
	private boolean exclusive(Integer[] arrays, int element) {
		for (int i : arrays) {
			if (element == i) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 导出excel表格【根据注解的index属性】
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年7月13日
	 * @param list
	 * @param indexs
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public HSSFWorkbook exportExcel(List<Vo> list, Integer[] indexs, short titleHeight, short contentHeight)
			throws IllegalArgumentException, IllegalAccessException {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();

		createTitle(list.get(0).getClass(), indexs, titleHeight, wb, sheet);
		int rowNo = 1;
		HSSFRow row = null;
		HSSFCellStyle style = ExcelUtils.getTextStyle(wb);
		final int size = list.size();
		for (int pos = 0; pos < size; ++pos) {
			Vo vo = list.get(pos);
			row = sheet.createRow(rowNo++);
			row.setHeight(contentHeight);
			int cellNo = 0;
			analysisBeanAndBuildCell(cellNo, row, indexs, style, vo, vo.getClass());
		}
		// 设置列宽自适应
		for (int pos = 0; pos < indexs.length; ++pos) {
			sheet.autoSizeColumn(pos);
		}
		return wb;
	}

	/**
	 * 生成标题行【根据注解的index属性】
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年7月12日
	 * @param list
	 * @param indexs
	 * @param wb
	 * @param sheet
	 * @return
	 */
	private void createTitle(Class<?> clz, Integer[] indexs, short titleHeight, HSSFWorkbook wb, HSSFSheet sheet) {
		HSSFRow row;
		HSSFCellStyle headStyle = ExcelUtils.getHeadStyle(wb);
		// 行高
		int rowNo = 0;
		row = sheet.createRow(rowNo++);
		row.setHeight(titleHeight);
		int pos = 0;
		analysisBeanAndCreateTitle(pos, indexs, row, headStyle, clz);
	}

	/**
	 * 反射解析实体bean属性并生成excel标题【根据注解的index属性】
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年7月20日
	 * @param pos
	 * @param indexs
	 * @param row
	 * @param headStyle
	 * @param clz
	 */
	private void analysisBeanAndCreateTitle(int pos, Integer[] indexs, HSSFRow row, HSSFCellStyle headStyle, Class<?> clz) {
		HSSFCell cell;
		Field fields[] = clz.getDeclaredFields();
		// 生成标题行并设置选中列映射表
		for (Field field : fields) {
			ReflactAnnotion reflactAnnotion = (ReflactAnnotion) field.getAnnotation(ReflactAnnotion.class);
			if (reflactAnnotion == null) {
				continue;
			}
			if (reflactAnnotion.customizedClass()) {
				analysisBeanAndCreateTitle(pos, indexs, row, headStyle, field.getType());
			}
			if (contain(indexs, reflactAnnotion.index())) {
				cell = row.createCell(pos++);
				cell.setCellValue(reflactAnnotion.columnName());
				cell.setCellStyle(headStyle);
			}
		}
	}

	/**
	 * 反射解析类属性并生产Cell单元格【根据注解的index属性】
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年7月20日
	 * @param cellNo
	 * @param row
	 * @param indexs
	 * @param style
	 * @param vo
	 * @param clz
	 * @throws IllegalAccessException
	 */
	private void analysisBeanAndBuildCell(int cellNo, HSSFRow row, Integer[] indexs, HSSFCellStyle style, Object vo, Class<?> clz)
			throws IllegalAccessException {
		HSSFCell cell;
		Field fields[] = clz.getDeclaredFields();
		for (Field field : fields) {
			ReflactAnnotion reflactAnnotion = (ReflactAnnotion) field.getAnnotation(ReflactAnnotion.class);
			if (reflactAnnotion == null) {
				continue;
			}
			// 自定义类
			if (reflactAnnotion.customizedClass()) {
				field.setAccessible(true);
				analysisBeanAndBuildCell(cellNo, row, indexs, style, field.get(vo), field.getType());
			}
			if (contain(indexs, reflactAnnotion.index())) {
				field.setAccessible(true);
				cell = row.createCell(cellNo++);
				setCellValueOfDataType(cell, field.get(vo), reflactAnnotion);
				cell.setCellStyle(style);
			}
		}
	}

	public static <T> HSSFWorkbook exportExcel(List<T> list, Class<T> destinationType) throws IllegalArgumentException, IllegalAccessException {
		Field fields[] = destinationType.getDeclaredFields();
		String properties[] = new String[fields.length];
		for (int i = 0; i < properties.length; i++) {
			properties[i] = fields[i].getName();
		}
		return exportExcel(list, properties, destinationType, (short) 600, (short) 480);
	}

	public static <T> HSSFWorkbook exportExcel(List<T> list, String[] properties, Class<T> destinationType, short titleHeight, short contentHeight)
			throws IllegalArgumentException, IllegalAccessException {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();

		Map<String, Integer> propertyMap = createTitle(destinationType, properties, titleHeight, wb, sheet);
		int rowNo = 1;
		HSSFRow row = null;
		HSSFCell cell = null;
		HSSFCellStyle style = ExcelUtils.getTextStyle(wb);
		final int size = list.size();
		for (int pos = 0; pos < size; ++pos) {
			T t = list.get(pos);
			row = sheet.createRow(rowNo++);
			row.setHeight(contentHeight);

			Field fields[] = destinationType.getDeclaredFields();
			for (Field field : fields) {
				ReflactAnnotion reflactAnnotion = (ReflactAnnotion) field.getAnnotation(ReflactAnnotion.class);
				if (reflactAnnotion == null || !propertyMap.containsKey(field.getName())) {
					continue;
				}
				field.setAccessible(true);
				cell = row.createCell(propertyMap.get(field.getName()));
				setCellValueOfDataType(cell, field.get(t), reflactAnnotion);
				cell.setCellStyle(style);
			}
		}
		// 设置列宽自适应
		for (int pos = 0; pos < properties.length; ++pos) {
			sheet.autoSizeColumn(pos);
		}
		return wb;
	}

	private static <T> Map<String, Integer> createTitle(Class<T> destinationType, String[] properties, short titleHeight, HSSFWorkbook wb, HSSFSheet sheet) {
		HSSFRow row;
		HSSFCell cell;
		HSSFCellStyle headStyle = ExcelUtils.getHeadStyle(wb);
		// 行高
		int rowNo = 0;
		row = sheet.createRow(rowNo++);
		row.setHeight(titleHeight);
		Field fields[] = destinationType.getDeclaredFields();
		Map<String, Integer> propertyMap = new HashMap<String, Integer>();
		// 生成标题行并设置选中列映射表
		for (int pos = 0; pos < properties.length; ++pos) {
			for (Field field : fields) {
				ReflactAnnotion reflactAnnotion = (ReflactAnnotion) field.getAnnotation(ReflactAnnotion.class);
				if (reflactAnnotion == null) {
					continue;
				}
				if (properties[pos].equals(field.getName())) {
					cell = row.createCell(pos);
					cell.setCellValue(reflactAnnotion.columnName());
					cell.setCellStyle(headStyle);
					propertyMap.put(properties[pos], pos);
				}
			}
		}
		return propertyMap;
	}

	/**
	 * 导出excel表格【根据标题】
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年7月13日
	 * @param list
	 * @param titles
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public HSSFWorkbook exportExcel(List<Vo> list, String[] titles, short titleHeight, int contentHeight)
			throws IllegalArgumentException, IllegalAccessException {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		HSSFRow row = null;
		int rowNo = 0;
		Map<String, Integer> titleMap = createTitle(titles, titleHeight, rowNo++, wb, sheet);

		HSSFCellStyle style = ExcelUtils.getTextStyle(wb);
		final int size = list.size();
		// 数据
		for (int pos = 0; pos < size; ++pos) {
			Vo vo = list.get(pos);
			row = sheet.createRow(rowNo++);
			row.setHeight((short) contentHeight);
			// 反射输出用户实体内容
			analysisBeanAndBuildCell(row, titleMap, style, vo, vo.getClass());
		}
		// 设置列宽
		for (int pos = 0; pos < titles.length; ++pos) {
			sheet.autoSizeColumn(pos);
		}
		return wb;
	}

	/**
	 * 生成标题行【根据标题】
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年7月13日
	 * @param titles
	 * @param titleHeight
	 * @param row
	 * @param headStyle
	 * @return
	 */
	private Map<String, Integer> createTitle(String[] titles, short titleHeight, int rowNo, HSSFWorkbook wb, HSSFSheet sheet) {
		HSSFRow row = sheet.createRow(rowNo);
		HSSFCell cell;
		row.setHeight((short) titleHeight);
		Map<String, Integer> titleMap = new HashMap<String, Integer>();
		for (int pos = 0; pos < titles.length; ++pos) {
			cell = row.createCell(pos);
			cell.setCellValue(titles[pos]);
			titleMap.put(titles[pos], pos);
			cell.setCellStyle(ExcelUtils.getHeadStyle(wb));
		}
		return titleMap;
	}

	/**
	 * 反射解析类属性并生产Cell单元格【根据标题】
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年7月19日
	 * @param row
	 * @param titleMap
	 * @param style
	 * @param vo
	 * @param clz
	 * @throws IllegalAccessException
	 */
	private void analysisBeanAndBuildCell(HSSFRow row, Map<String, Integer> titleMap, HSSFCellStyle style, Object vo, Class<?> clz)
			throws IllegalAccessException {
		HSSFCell cell;
		int cellNo;
		Field fields[] = clz.getDeclaredFields();
		for (Field field : fields) {
			ReflactAnnotion reflactAnnotion = (ReflactAnnotion) field.getAnnotation(ReflactAnnotion.class);
			if (reflactAnnotion == null) {
				continue;
			}
			// 自定义类
			if (reflactAnnotion.customizedClass()) {
				field.setAccessible(true);
				analysisBeanAndBuildCell(row, titleMap, style, field.get(vo), field.getType());
			}
			if (titleMap.containsKey(reflactAnnotion.columnName())) {
				// 取得当前属性该输出到excel表格哪一列
				cellNo = titleMap.get(reflactAnnotion.columnName());
				field.setAccessible(true);
				cell = row.createCell(cellNo);
				setCellValueOfDataType(cell, field.get(vo), reflactAnnotion);
				cell.setCellStyle(style);
			}
		}
	}

	/**
	 * 根据属性类型生成相应类型的excel单元表格
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年7月13日
	 * @param cell
	 * @param val
	 */
	private static void setCellValueOfDataType(HSSFCell cell, Object val, ReflactAnnotion reflactAnnotion) {
		val = convertWithEnum(val, reflactAnnotion);

		if (val == null) {
			val = "-";
		}

		if (val instanceof Integer) {
			cell.setCellValue((Integer) val);
		} else if (val instanceof Short) {
			cell.setCellValue((Short) val);
		} else if (val instanceof Byte) {
			cell.setCellValue((Byte) val);
		} else if (val instanceof Float || val instanceof Double) {
			String formatVal = val.toString();
			if (StringUtils.isNotBlank(reflactAnnotion.format())) {
				formatVal = String.format(reflactAnnotion.format(), val);
			}
			cell.setCellValue(formatVal);
		} else if (val instanceof Long) {
			cell.setCellValue((Long) val);
		} else if (val instanceof String || val instanceof Character) {
			cell.setCellValue(val.toString());
		} else if (val instanceof Boolean) {
			cell.setCellValue((boolean) val ? "√" : "×");
		} else if (val instanceof Timestamp || val instanceof Date) {
			String formatVal = val.toString();
			if (StringUtils.isNotBlank(reflactAnnotion.format())) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(reflactAnnotion.format());
				formatVal = simpleDateFormat.format((Date) val);
			}
			cell.setCellValue(formatVal);
		} else {
			cell.setCellValue(val.toString());
		}
	}

	/**
	 * 枚举code-->text映射转换
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年7月14日
	 * @param val
	 * @param reflactAnnotion
	 * @return
	 */
	private static Object convertWithEnum(Object val, ReflactAnnotion reflactAnnotion) {
		// 将注解为需要枚举转换的属性进行code-->text的转换
		if (reflactAnnotion.convert() && EnumConstant.ENUM_MAP.containsKey(reflactAnnotion.enumClassName())) {
			Integer key = null;
			// EnumConstant.ENUM_MAP其Value的键为Integer类型
			if (!(val instanceof Integer)) {
				String tmp = val.toString();
				Matcher m = INT_PATTERN.matcher(tmp);
				if (m.matches()) {
					key = Integer.parseInt(tmp);
				}
			} else {
				key = (Integer) val;
			}
			val = EnumConstant.ENUM_MAP.get(reflactAnnotion.enumClassName()).get(key);
		}
		return val;
	}

}
