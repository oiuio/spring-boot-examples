package cc.oiuio.validator.validation.constraintvalidators;

import cc.oiuio.validator.validation.constraints.Sex;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author YunTianXiang
 * @Date 2019/2/12
 */
public class SexValidator implements ConstraintValidator<Sex,String> {

	@Override
	public void initialize(Sex constraintAnnotation) {

	}

	@Override
	public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
		return false;
	}
}
