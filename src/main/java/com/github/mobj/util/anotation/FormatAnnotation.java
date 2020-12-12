package com.github.mobj.util.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author luoruihua
 * @date 2020/12/12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FormatAnnotation {
 
    /**
     * 日期的格式
     * @return
     */
    String dateFormat() default "yyyy-MM-dd hh:mm:ss";
 
    /**
     * list集合的长度
     * @return
     */
    String listLength() default "4";
 
    /**
     * map集合的长度
     * @return
     */
    String mapLength() default "4";

    /**
     * 值
     * @return
     */
    String value() default "0";
}
