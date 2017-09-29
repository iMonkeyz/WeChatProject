package com.imonkeyz.demo.annotation;

import com.imonkeyz.demo.constants.WeChatConstants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Jesse on 2017/9/28.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface WeChatAdminRequired {

	String failed() default "/authorization";
	String args() default WeChatConstants.AUTH_EXPIRED;

}
