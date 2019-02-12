package cc.oiuio.validator.validation.constraints;

import cc.oiuio.validator.validation.constraintvalidators.DefaultTemplateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义校验注解
 *
 * @author YunTianXiang
 * @Date 2019/2/12
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DefaultTemplateValidator.class)//指定校验方法
public @interface DefaultTemplate {
	//以下三个为必须字段

	//错误消息
	String message() default "validate Sex message";

	//指定校验分组
	Class<?>[] groups() default {};

	//负载
	Class<? extends Payload>[] payload() default {};

	//自定义字段

	//是否必须
	boolean require() default false;
}
