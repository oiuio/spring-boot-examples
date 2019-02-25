package cc.oiuio.validator.module;

import lombok.Data;

import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 正则检查
 *
 * @author YunTianXiang
 * @Date 2019/2/21
 */
@Data
public class PatternValidatorVO {
	@Pattern(regexp = "", message = "对象不符合正则表达式")
	private Date withPattern;
}
