package cc.oiuio.jpa;

import cc.oiuio.jpa.domain.TbDistrict;
import cc.oiuio.jpa.repository.TbDistrictRepository;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootJpaApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	private TbDistrictRepository tbDistrictRepository;

	@Test
	public void test() {
		List<TbDistrict> list = this.tbDistrictRepository.findAllByLevelEquals("city");
		Map<String, List<TbDistrict>> group = list.stream().collect(Collectors.groupingBy(district -> district.getPinyin().substring(0, 1)));

		List<Map<String, Object>> result = new ArrayList<>();
		for (Map.Entry<String, List<TbDistrict>> stringListEntry : group.entrySet()) {
			Map<String, Object> map = new HashMap<>();
			map.put("title", stringListEntry.getKey());
			map.put("items", stringListEntry.getValue());
			result.add(map);
		}

		System.out.println(JSON.toJSON(result));
	}
}

