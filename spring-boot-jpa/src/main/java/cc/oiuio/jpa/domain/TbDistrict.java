package cc.oiuio.jpa.domain;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "tb_district")
public class TbDistrict {

	@Id
	private String dataId;
	private String dataEnable;
	private Date dataInsertTime;
	private String cityCode;
	private String adCode;
	private String name;
	private String level;
	private String center;
	private String parentAdCode;
	private String pinyin;

}