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

	@Past(message = "Date 和 Calendar 对象需要为过去时间")
	private Date withPast;
	@Future(message = "Date 和 Calendar 对象需要为未来时间")
	private Date withFuture;

}
