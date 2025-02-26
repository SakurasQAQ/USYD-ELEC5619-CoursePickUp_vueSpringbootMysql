package com.elec5619.course_pro_backend.repository;

import com.elec5619.course_pro_backend.model.LoginRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginRecordRepository extends JpaRepository<LoginRecord, Long> {
    List<LoginRecord> findByUsername(String username);
}