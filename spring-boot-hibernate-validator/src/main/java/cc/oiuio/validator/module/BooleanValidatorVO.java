package cc.oiuio.validator.module;

import lombok.Data;

import javax.validation.constraints.*;

/**
 * 布尔检查
 *
 * @author YunTianXiang
 * @Date 2019/2/15
 */
@Data
public class BooleanValidatorVO {

	@AssertTrue(message = "对象需为true")
	private Boolean withTrue;
	@AssertFalse(message = "对象需为false")
	private Boolean withFalse;
}
