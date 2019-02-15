package cc.oiuio.validator.config;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;

/**
 * @author YunTianXiang
 * @Date 2019/2/15
 */
@Configuration
public class ValidationConfig {

	@Bean
	public Validator getValidator() {
		return Validation.byProvider(HibernateValidator.class)
				.configure()
				.failFast(true)
				.messageInterpolator(new ResourceBundleMessageInterpolator(new PlatformResourceBundleLocator("i18n/validation/cutomerValidationMessage")))
				.buildValidatorFactory()
				.getValidator();
	}
}
