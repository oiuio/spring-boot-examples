package cc.oiuio.validator.validation.constraintvalidators;

import cc.oiuio.validator.validation.constraints.DefaultTemplate;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 自定义校验方法 实现 ConstraintValidator<注解,参数类型>接口
 *
 * @author YunTianXiang
 * @Date 2019/2/12
 */
public class DefaultTemplateValidator implements ConstraintValidator<DefaultTemplate, String> {

	//自定义属性
	private boolean require;

	//初始化方法 , 可以获取注解有关信息
	@Override
	public void initialize(DefaultTemplate constraintAnnotation) {
		this.require = constraintAnnotation.require();
	}

	//校验方法 s为参数
	@Override
	public boolean isValid(String s, ConstraintValidatorContext context) {
		//自定义
		//如果参数必须且为空
		if (require && StringUtils.isBlank(s)) {
			//自定义校验失败提示信息 , 需要先禁用默认
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("参数为空")
					.addConstraintViolation();//必须有这部 否则提示没有指定模板
			return false;
		}
		//如果参数不为空进行合法性校验
		if (StringUtils.isNotBlank(s)) {
			//相应校验方法
		}

		return true;
	}
}
