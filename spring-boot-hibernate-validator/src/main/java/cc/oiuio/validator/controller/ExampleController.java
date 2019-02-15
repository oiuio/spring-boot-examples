package cc.oiuio.validator.controller;

import cc.oiuio.validator.module.NullValidatorVO;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author YunTianXiang
 * @Date 2019/2/13
 */
@Controller
public class ExampleController {


	@PostMapping("/nullValidator")
	@ResponseBody
	public Object nullValidator(@RequestBody @Validated NullValidatorVO vo, BindingResult result) {
		if (result.hasErrors()) {
			return result.getFieldError().getDefaultMessage();
		}
		return true;
	}

}
