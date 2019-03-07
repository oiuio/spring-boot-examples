package cc.oiuio.validator.validation.constraintvalidators;

import cc.oiuio.validator.validation.constraints.Sex;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author YunTianXiang
 * @Date 2019/2/12
 */
public class SexValidator implements ConstraintValidator<Sex, String> {

	private boolean require;

	@Override
	public void initialize(Sex sex) {
		this.require = sex.require();
	}

	@Override
	public boolean isValid(String s, ConstraintValidatorContext context) {
		cc.oiuio.validator.constant.Sex sex = cc.oiuio.validator.constant.Sex.parse(s);
		if (sex == null) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("性别有误")
					.addConstraintViolation();
			return false;
		}
		return true;
	}
}
