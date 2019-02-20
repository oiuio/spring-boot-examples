package cc.oiuio.validator.module;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

/**
 * 数值检查  建议使用在String Integer类型上 ,不建议使用在int类型上，因为表单值为“”时无法转换为int，但可以转换为Stirng为"",Integer为null
 *
 * @author YunTianXiang
 * @Date 2019/2/15
 */
@Data
public class NumericValidatorVO {
	@Max(value = 100)
	private String withMax;
	@Min(value = 10)
	private String withMin;
	@DecimalMax(value = "100.0")
	private String withDecimalMax;
	@DecimalMin(value = "10.0")
	private String withDecimalMin;
	@Digits(integer = 5, fraction = 2)
	private String withDigits;
	@Range(min = 10, max = 100)
	private String withRange;
}
