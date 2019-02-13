package cc.oiuio.validator.controller;

import cc.oiuio.validator.module.Register;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * @author YunTianXiang
 * @Date 2019/2/13
 */
@Controller
public class MainController {

	@PostMapping("/register")
	@ResponseBody
	public Object register(@RequestBody @Valid Register register, BindingResult result) {
		System.out.println(register);
		if (result.hasErrors()) {
			return result.getFieldError().getDefaultMessage();
		}
		return "true";
	}
}
