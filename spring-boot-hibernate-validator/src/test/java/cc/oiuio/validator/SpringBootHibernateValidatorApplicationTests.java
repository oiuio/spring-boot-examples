package cc.oiuio.validator;

import cc.oiuio.validator.module.Register;
import com.alibaba.fastjson.JSONObject;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootHibernateValidatorApplicationTests {

	@Autowired
	private WebApplicationContext context;
	private MockMvc mvc;

	@Before
	public void setUo() throws Exception {
		//构造MockMvc
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void register() throws Exception {
		Register register = new Register();
		register.setName("wxf");
		register.setSex("1111");

		System.out.println(JSONObject.toJSONString(register));

		MvcResult result = mvc.perform(post("/register")
				.contentType(MediaType.APPLICATION_JSON_UTF8).content(JSONObject.toJSONString(register)))
				.andExpect(status().isOk()).andReturn();

		System.out.println(result.getResponse().getContentAsString());

	}


}

