package com.yhq.tool;

import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

@SuppressWarnings("deprecation")
public class ExcelUtils {

	/**
	 * 在第row行第cellnum列创建一个style样式的单元格，内容为value
	 */
	public static HSSFCell createCell(HSSFRow row, short cellnum, HSSFCellStyle style, String value) {
		HSSFCell cell = row.createCell(cellnum++);
		// cell.setEncoding(HSSFCell.ENCODING_UTF_16);//处理中文问题
		cell.setCellStyle(style);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(value);
		return cell;
	}

	public static HSSFCell createCell(HSSFRow row, short cellnum, HSSFCellStyle style, int value) {
		HSSFCell cell = row.createCell(cellnum++);
		// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellStyle(style);
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(value);
		return cell;
	}

	public static HSSFCell createCell(HSSFRow row, short cellnum, HSSFCellStyle style, double value) {
		HSSFCell cell = row.createCell(cellnum++);
		// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellStyle(style);
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(value);
		return cell;
	}

	public static HSSFCell createCell(HSSFRow row, short cellnum, HSSFCellStyle style, Date value) {
		HSSFCell cell = row.createCell(cellnum++);
		// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellStyle(style);
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		if (value != null) {
			cell.setCellValue(value);
		} else {
			cell.setCellValue("");
		}
		return cell;
	}

	/**
	 * 生成样式
	 */
	public static HSSFCellStyle getStyle(HSSFWorkbook workbook, String fontName, short fontSize, short weight, short border, short align, short valign) {
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont f = workbook.createFont();
		f.setFontName(fontName);
		f.setFontHeightInPoints(fontSize);// 高度
		f.setBoldweight(weight);// 粗细
		style.setFont(f);
		style.setAlignment(align);// 左右居中
		style.setVerticalAlignment(valign);// 上下居中
		style.setBorderBottom(border);// 下边框
		style.setBorderLeft(border);// 左边框
		style.setBorderRight(border);// 右边框
		style.setBorderTop(border);// 上边框
		return style;
	}

	/**
	 * 标题内容样式
	 */
	public static HSSFCellStyle getTitleStyle(HSSFWorkbook workbook) {
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont f = workbook.createFont();
		f.setFontName("黑体");
		f.setFontHeightInPoints((short) 20);// 字号
		f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		style.setFont(f);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		return style;
	}

	public static HSSFCellStyle getHeadStyle(HSSFWorkbook workbook) {
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont f = workbook.createFont();
		f.setFontHeightInPoints((short) 10);// 字号
		f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		style.setFont(f);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		short type = 1;
		style.setBorderBottom(type);// 下边框
		style.setBorderLeft(type);// 左边框
		style.setBorderRight(type);// 右边框
		style.setBorderTop(type);// 上边框

		// 背景填充颜色
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		style.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
		return style;
	}

	/**
	 * 普通内容样式
	 */
	public static HSSFCellStyle getTextStyle(HSSFWorkbook workbook) {
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont f = workbook.createFont();
		f.setFontHeightInPoints((short) 10);// 字号
		style.setFont(f);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		short type = 1;
		style.setBorderBottom(type);// 下边框
		style.setBorderLeft(type);// 左边框
		style.setBorderRight(type);// 右边框
		style.setBorderTop(type);// 上边框
		return style;
	}
	
	public static HSSFCellStyle getStatisStyle(HSSFWorkbook workbook) {
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont f = workbook.createFont();
		f.setFontHeightInPoints((short) 10);// 字号
		f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		style.setFont(f);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		short type = 1;
		style.setBorderBottom(type);// 下边框
		style.setBorderLeft(type);// 左边框
		style.setBorderRight(type);// 右边框
		style.setBorderTop(type);// 上边框

		return style;
	}

	/**
	 * 普通内容左对齐样式
	 */
	public static HSSFCellStyle getTextStyleAlignLeft(HSSFWorkbook workbook) {
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont f = workbook.createFont();
		f.setFontHeightInPoints((short) 10);// 字号
		style.setFont(f);
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 左右居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		short type = 1;
		style.setBorderBottom(type);// 下边框
		style.setBorderLeft(type);// 左边框
		style.setBorderRight(type);// 右边框
		style.setBorderTop(type);// 上边框
		return style;
	}

	/**
	 * 普通内容样式--没有边框
	 */
	public static HSSFCellStyle getTextStyleNoBorder(HSSFWorkbook workbook) {
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont f = workbook.createFont();
		f.setFontHeightInPoints((short) 10);// 字号
		style.setFont(f);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		return style;
	}

	/**
	 * 普通数字内容样式(右对齐)
	 */
	public static HSSFCellStyle getNumberStyle(HSSFWorkbook workbook) {
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont f = workbook.createFont();
		f.setFontHeightInPoints((short) 10);// 字号
		style.setFont(f);
		style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);// 左右--右对齐
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		short type = 1;
		style.setBorderBottom(type);// 下边框
		style.setBorderLeft(type);// 左边框
		style.setBorderRight(type);// 右边框
		style.setBorderTop(type);// 上边框
		return style;
	}

	/**
	 * 金额内容样式(右对齐)
	 */
	public static HSSFCellStyle getMoneyFormatStyle(HSSFWorkbook workbook) {
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont f = workbook.createFont();
		f.setFontHeightInPoints((short) 10);// 字号
		style.setFont(f);
		style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);// 左右--右对齐
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		short type = 1;
		style.setBorderBottom(type);// 下边框
		style.setBorderLeft(type);// 左边框
		style.setBorderRight(type);// 右边框
		style.setBorderTop(type);// 上边框

		// 格式化
		style.setDataFormat((short) 4);
		return style;
	}

	/**
	 * 获取行row 内第cellNum列的内容
	 * 
	 * @param row
	 * @param cellNum
	 * @return
	 */
	public static String getCellStrValue(HSSFRow row, short cellNum) {
		String value = null;
		if (row != null) {
			HSSFCell cell = row.getCell(cellNum);
			if (cell != null) {
				try {
					value = cell.getStringCellValue();
				} catch (Exception e) {
					try {
						value = String.valueOf((long) cell.getNumericCellValue());
					} catch (Exception e2) {
						value = null;
					}
				}
				if (value != null) {
					value = value.trim();
				}
			}
		}
		return value;
	}

}