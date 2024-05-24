package com.playground.playground.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.playground.playground.po.UserPO;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<UserPO, Long> {

	@Transactional
	@Modifying
	@Query("UPDATE UserPO u SET u.name = :name WHERE u.id = :id")
	void updateUserNameById(@Param("id") Long id, @Param("name") String name);

	@Transactional
	@Modifying
	@Query("UPDATE UserPO u SET u.age = :age WHERE u.id = :id")
	void updateUserAgeById(@Param("id") Long id, @Param("age") Integer age);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE UserPO u SET u.name = :name WHERE u.id = :id")
	void updateUserNameByIdWithClearAutomatically(@Param("id") Long id, @Param("name") String name);

	@Transactional
	@Modifying(flushAutomatically = true)
	@Query("UPDATE UserPO u SET u.name = :name WHERE u.id = :id")
	void updateUserNameByIdWithFlushAutomatically(@Param("id") Long id, @Param("name") String name);

	@Query("UPDATE UserPO u SET u.name = :name WHERE u.id = :id")
	void updateUserNameByIdWithoutModify(@Param("id") Long id, @Param("name") String name);
}