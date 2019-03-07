package cc.oiuio.validator.controller;

import cc.oiuio.validator.module.*;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ExampleControllerTest {

	@Autowired
	private WebApplicationContext context;
	private MockMvc mvc;


	@Before
	public void setUp() throws Exception {
		log.debug("test set up");
		//构造MockMvc
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void nullValid() throws Exception {
		NullValidatorVO vo = new NullValidatorVO();
		//这个参数需为null
//		vo.setWithNull("");
		//这个参数需不为null
		vo.setWithNotNull("");
		//这个参数需不为空(not null and and not blank) 一般来说用在字符串上
		vo.setWithNotBlank("");
		List<String> list = new ArrayList<>();
		list.add("1");
		//这个参数的元素不为空 一般来说用在集合上 , 字符串好像也行
		vo.setWithNotEmpty(list);
		MvcResult result = mvc.perform(post("/nullValid")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(JSONObject.toJSONString(vo)))
				.andExpect(status().isOk())
				.andReturn();

		log.debug("result : {}",result.getResponse().getContentAsString());
	}

	@Test
	public void booleanValid() throws Exception {
		BooleanValidatorVO vo = new BooleanValidatorVO();
		//这个参数需要为false 不判断null,需要与@notnull组合使用
		vo.setWithFalse(null);
		//这个参数需要为true
		vo.setWithTrue(null);

		MvcResult result = mvc.perform(post("/booleanValid")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(JSONObject.toJSONString(vo)))
				.andExpect(status().isOk())
				.andReturn();

		log.debug("result : {}",result.getResponse().getContentAsString());
	}

	@Test
	public void dateValid() throws Exception {
		DateValidatorVO vo = new DateValidatorVO();
		//这个参数必须是过去的时间
		DateTime now = new DateTime("2018-1-1");
		vo.setWithPast(now.toDate());
		//这个参数必须是未来的时间
		vo.setWithFuture(new DateTime("2018-1-1").toDate());

		MvcResult result = mvc.perform(post("/dateValid")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(JSONObject.toJSONString(vo)))
				.andExpect(status().isOk())
				.andReturn();

		log.debug("result : {}",result.getResponse().getContentAsString());
	}

	@Test
	public void numericValid() throws Exception {
		NumericValidatorVO vo = new NumericValidatorVO();
		vo.setWithMax(100.1);

		MvcResult result = mvc.perform(post("/numericValid")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(JSONObject.toJSONString(vo)))
				.andExpect(status().isOk())
				.andReturn();

		log.debug("result : {}",result.getResponse().getContentAsString());
	}

	@Test
	public void groupsValid() throws Exception{
		GroupValidatorVO vo = new GroupValidatorVO();
//		vo.setName("姑姑");
//		vo.setId("id");
		vo.setSex("1212");

		MvcResult result = mvc.perform(post("/groupsValid")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(JSONObject.toJSONString(vo)))
				.andExpect(status().isOk())
				.andReturn();

		log.debug("result : {}",result.getResponse().getContentAsString());
	}
}