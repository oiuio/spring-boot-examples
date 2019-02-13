package cc.oiuio.validator.module;

import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;

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


}
