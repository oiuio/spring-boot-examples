package cc.oiuio.validator.validation.constraintvalidators;

import cc.oiuio.validator.validation.constraints.Sex;
import org.apache.commons.lang3.StringUtils;

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

		if (require && StringUtils.isBlank(s)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("性别为空")
					.addConstraintViolation();
			return false;
		}
		if (StringUtils.isNotBlank(s)) {
			cc.oiuio.validator.constant.Sex sex = cc.oiuio.validator.constant.Sex.parse(s);
			if (sex == null) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("性别有误")
						.addConstraintViolation();
				return false;
			}
		}


		return true;
	}
}
