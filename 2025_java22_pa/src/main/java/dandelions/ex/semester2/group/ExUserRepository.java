package dandelions.ex.semester2.group;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dandelions.ex.ExUserEntity;

@Repository("groupRepository")
public interface ExUserRepository extends JpaRepository<ExUserEntity, String>{

	List<ExUserEntity> findByKanaContainingAndBloodtypeLikeAndAddressStartingWith(String kana, String bloodtype, String address);

	//@Query(name="ex.findByExUserList")
	@Query("SELECT u FROM ExUserEntity u WHERE u.kana LIKE :kana AND u.bloodtype LIKE :bloodtype AND u.address LIKE :address ORDER BY u.kana")
	List<ExUserEntity> findByExUserList(@Param("kana")String kana, @Param("bloodtype")String bloodtype, @Param("address")String address);

}
