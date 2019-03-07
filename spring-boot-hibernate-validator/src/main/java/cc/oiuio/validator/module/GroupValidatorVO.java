package cc.oiuio.validator.module;

import cc.oiuio.validator.validation.constraints.Sex;
import cc.oiuio.validator.validation.groups.ModeB;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.groups.Default;

/**
 * @author YunTianXiang
 * @Date 2019/3/4
 */
@Data
public class GroupValidatorVO {
	@NotBlank(message = "id为空", groups = Default.class)
	private String id;
	@NotBlank(message = "姓名为空")
	private String name;
	@NotBlank(message = "性别为空", groups = {ModeB.class})
	@Sex(message = "性别有误")
	private String sex;
}
