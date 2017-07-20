package com.yhq.tool;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记实体bean属性反射动作注解类
 * 
 * @author YHQ
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface ReflactAnnotion {

	/**
	 * 字段名
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年7月13日
	 * @return
	 */
	String columnName() default "";

	/**
	 * 索引
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年7月13日
	 * @return
	 */
	int index() default 0;

	/**
	 * 是否需要进行枚举code--->text的转换
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年7月13日
	 * @return
	 */
	boolean convert() default false;

	/**
	 * 转换枚举类型类名称(含包名)
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年7月13日
	 * @return
	 */
	String enumClassName() default "";

	/**
	 * 格式化 时间格式：yyyy-MM-dd hh:mm:ss 浮点格式：%.2f
	 * 
	 * @author 庄植雄 2017年7月14日 下午1:56:53 copyright @frontpay 2017
	 *
	 */
	String format() default "";

	/**
	 * 是否自定义类
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年7月19日
	 * @return
	 */
	boolean customizedClass() default false;
}
