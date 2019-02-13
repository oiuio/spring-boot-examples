package cc.oiuio.validator.module;

import cc.oiuio.validator.validation.constraints.Sex;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YunTianXiang
 * @Date 2019/2/13
 */
@Data
public class Register implements Serializable {

	private String name;
	@Sex
	private String sex;
}
