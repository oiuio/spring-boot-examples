package cc.oiuio.validator.module;

import lombok.Data;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * 日期检查
 *
 * @author YunTianXiang
 * @Date 2019/2/15
 */
@Data
public class DateValidatorVO {

	@Past(message = "Date 和 Calendar 对象过去时间")
	private Date withPast;
	@Future(message = "Date 和 Calendar 对象未来时间")
	private Date withFuture;
	@Pattern(regexp = "", message = "对象不符合正则表达式")
	private Date withPattern;
}
