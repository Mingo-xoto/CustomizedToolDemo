package com.yhq.enums;

/**
 * 枚举接口
 */
public interface IEnum {

	public int getCode();

	public String getText();

	default String getTextByCode(int code, IEnum[] ienums) {
		for (IEnum e : ienums) {
			if (e.getCode() == code) {
				return e.getText();
			}
		}
		return "";
	}
}
