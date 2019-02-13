package cc.oiuio.validator.validation.constraints;

import cc.oiuio.validator.validation.constraintvalidators.SexValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author YunTianXiang
 * @Date 2019/2/12
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SexValidator.class)
public @interface Sex {
	String message() default "validate Sex message";

	boolean require() default false;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
