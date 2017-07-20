/**
 * 
 */
package com.yhq.enums;

/**
 * 发送形式
 * @author 庄植雄
 * 2016年10月31日 下午10:09:54
 * copyright @frontpay 2016
 */
public enum MsgSendForm implements IEnum{
	Template(1, "模板"),
	Common(2, "普通");
	
	private MsgSendForm(int code, String text) {
		this.code = code;
		this.text = text;
	}
	
	private int code;
	
	public int getCode() {
		return this.code;
	}
	
	private String text;
	
	public String getText() {
		return this.text;
	}
}