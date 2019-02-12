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
 * @Date 2019/2/11
 */
@Entity
@Table(name = "t_user_role")
@Getter
@Setter
@ToString
public class UserRole {
	@Id
	@GenericGenerator(name = "idGenerator", strategy = "guid")
	@GeneratedValue(generator = "idGenerator")
	private String id;
	private String userId;
	private String roleId;
}
