package cc.oiuio.jpa.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author YunTianXiang
 * @Date 2019/1/24
 */
@Entity
@Table(name = "t_user")
@Getter
@Setter
@ToString
public class User {
	@Id
	@GenericGenerator(name = "idGenerator", strategy = "guid")
	@GeneratedValue(generator = "idGenerator")
	private String id;
	private String name;
	private String password;
}
