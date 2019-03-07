package cc.oiuio.validator.controller;

import cc.oiuio.validator.module.*;
import cc.oiuio.validator.validation.groups.ModeB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.groups.Default;

/**
 * @author YunTianXiang
 * @Date 2019/2/13
 */
@Controller
@Slf4j
public class ExampleController {

	@PostMapping("/nullValid")
	@ResponseBody
	public Object nullValid(@RequestBody @Validated NullValidatorVO vo, BindingResult result) {
		log.debug("get data = {}", vo);
		if (result.hasErrors()) {
			return result.getFieldError().getDefaultMessage();
		}
		return true;
	}

	@PostMapping("/booleanValid")
	@ResponseBody
	public Object booleanValid(@RequestBody @Validated BooleanValidatorVO vo, BindingResult result) {
		log.debug("get data = {}", vo);
		if (result.hasErrors()) {
			return result.getFieldError().getDefaultMessage();
		}
		return true;
	}

	@PostMapping("/dateValid")
	@ResponseBody
	public Object dateValid(@RequestBody @Validated DateValidatorVO vo, BindingResult result) {
		log.debug("get data = {}", vo);
		if (result.hasErrors()) {
			return result.getFieldError().getDefaultMessage();
		}
		return true;
	}

	@PostMapping("/numericValid")
	@ResponseBody
	public Object numericValid(@RequestBody @Validated NumericValidatorVO vo, BindingResult result) {
		log.debug("get data = {}", vo);
		if (result.hasErrors()) {
			return result.getFieldError().getDefaultMessage();
		}
		return true;
	}

	@PostMapping("/groupsValid")
	@ResponseBody
	public Object groupsValid(@RequestBody @Validated(value = {ModeB.class}) GroupValidatorVO vo, BindingResult result) {
		log.debug("get data = {}", vo);
		if (result.hasErrors()) {
			return result.getFieldError().getDefaultMessage();
		}
		return true;
	}
}
