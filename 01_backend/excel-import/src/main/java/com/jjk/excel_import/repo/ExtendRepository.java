package com.jjk.excel_import.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jjk.excel_import.po.ExtendPO;

@Repository
public interface ExtendRepository extends JpaRepository<ExtendPO, Long> {
}
