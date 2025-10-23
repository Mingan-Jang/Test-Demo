package com.jjk.spring_data.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jjk.spring_data.po.ImportPO;

public interface ImportDAO extends JpaRepository<ImportPO, Long> {

	@Query("SELECT i FROM ImportPO i WHERE i.age > 0")
	List<ImportPO> findImportPOsWithAgeGreaterThanZero();

	@Query(value = "SELECT * FROM spring_data_demo WHERE the_age> 0", nativeQuery = true)
	List<ImportPO> findImportPOsWithAgeGreaterThanZeroSQL();

	@Query("SELECT new ImportPO(i.name,i.age) FROM ImportPO i WHERE i.age > 0")
	List<ImportPO> findImportPOsWithAgeGreaterThanZeroPart();

	// 问题出现在您使用原生SQL查询时，返回类型应该是List<Object[]>
	// 而不是List<ImportPO>。这是因为原生SQL查询返回的是每行结果的字段数组
	// 而不是完整的实体对象。
	@Query(value = "SELECT the_name as name, the_age as age FROM spring_data_demo WHERE the_age > 0", nativeQuery = true)
//	@Query(value = "SELECT the_name, the_age FROM spring_data_demo WHERE the_age > 0", nativeQuery = true)
//	List<ImportPO> findImportPOsWithAgeGreaterThanZeroSQLPart();
//	 List<Object[]> findImportPOsWithAgeGreaterThanZeroSQLPart();
	 List<Map<String,Object>> findImportPOsWithAgeGreaterThanZeroSQLPart();

}