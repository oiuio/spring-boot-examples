package cc.oiuio.jpa.repository;

import cc.oiuio.jpa.domain.TbDistrict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author YunTianXiang
 * @Date 2019/2/14
 */
@Repository
public interface TbDistrictRepository extends JpaRepository<TbDistrict, String> {

	List<TbDistrict> findAllByLevelEquals(String level);
}
