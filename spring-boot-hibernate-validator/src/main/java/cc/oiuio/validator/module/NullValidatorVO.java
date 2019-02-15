package cc.oiuio.validator.module;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

/**
 * @author YunTianXiang
 * @Date 2019/2/15
 */
@Data
public class NullValidatorVO {

	//空检查
	@Null(message = "withNull 对象需为null")
	private String withNull;
	@NotNull(message = "withNotNull 对象需不为null")
	private String withNotNull;
	@NotBlank(message = "字符串需不为null且被trim长度大于0")
	private String withNotBlank;
	@NotEmpty(message = "元素为null或者empty")
	private List withNotEmpty;
}
