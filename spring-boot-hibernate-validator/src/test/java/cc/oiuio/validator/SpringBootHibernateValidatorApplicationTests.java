package cc.oiuio.validator;

import cc.oiuio.validator.module.NullValidatorVO;
import com.alibaba.fastjson.JSON;
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

import java.util.ArrayList;
import java.util.List;

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
	public void nullValidatorTest() throws Exception {
		NullValidatorVO vo = new NullValidatorVO();
//		vo.setWithNull("111");
		vo.setWithNotNull("111");
		vo.setWithNotBlank("111");
		List<String> list = new ArrayList<>();
		list.add("1");
		vo.setWithNotEmpty(list);
		System.out.println(vo);
		MvcResult result = mvc.perform(post("/nullValidator")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(JSONObject.toJSONString(vo)))
				.andExpect(status().isOk())
				.andReturn();

		System.out.println(result.getResponse().getContentAsString());
	}


}

