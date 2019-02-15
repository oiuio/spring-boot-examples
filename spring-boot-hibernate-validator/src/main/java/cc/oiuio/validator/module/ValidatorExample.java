package cc.oiuio.validator.module;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author YunTianXiang
 * @Date 2019/2/13
 */
@Data
public class ValidatorExample implements Serializable {

	//空检查
	@Null(message = "对象需为null")
	private String withNull;
	@NotNull(message = "对象需不为null")
	private String withNotNull;
	@NotBlank(message = "字符串需不为null且被trim长度大于0")
	private String withNotBlank;
	@NotEmpty(message = "元素为null或者empty")
	private String withNotEmpty;

	//Boolean检查
	@AssertTrue(message = "对象需为true")
	private Boolean withTrue;
	@AssertFalse(message = "对象需为false")
	private Boolean withFalse;

	//日期检查
	@Past(message = "Date 和 Calendar 对象过去时间")
	private Date withPast;
	@Future(message = "Date 和 Calendar 对象未来时间")
	private Date withFuture;
	@Pattern(regexp = "", message = "对象不符合正则表达式")
	private Date withPattern;

	//数值检查 建议使用在String Integer类型上 ,不建议使用在int类型上，因为表单值为“”时无法转换为int，但可以转换为Stirng为"",Integer为null
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
	@Range(min = 10,max = 100)
	private String withRange;



}
