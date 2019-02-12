package cc.oiuio.validator.validation;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author YunTianXiang
 * @Date 2019/1/17
 */
public class ValidationUtil {

	private static Validator validator = Validation.byProvider(HibernateValidator.class)
			.configure()
			.failFast(true)//快速失败返回模式 true:默认 有一个检验失败就返回 false:关闭 全部校验结束后返回所有错误信息
			.buildValidatorFactory()
			.getValidator();

	//默认
	public static <T> String validate(T object) {
		if (object == null) {
			return "参数为空";
		}
		Set<ConstraintViolation<T>> allErrors = validator.validate(object);
		if (!allErrors.isEmpty()) {
			return StringUtils.strip(allErrors.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()).toString(), "[]");
		}
		return null;
	}

	//指定分组
	public static <T> String validate(T object, Class<?>... groups) {
		if (object == null) {
			return "参数为空";
		}
		validator.validate(object, groups);
		Set<ConstraintViolation<T>> allErrors = validator.validate(object, groups);
		if (!allErrors.isEmpty()) {
			return StringUtils.strip(allErrors.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()).toString(), "[]");
		}
		return null;
	}
}
