package com.suke.czx.common.annotation;

import java.lang.annotation.*;

/**
 * 忽略XSS过滤
 * @author ywj
 * @email ywj
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface XssIgnore {

}
