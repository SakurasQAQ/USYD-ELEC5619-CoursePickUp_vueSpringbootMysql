package com.elec5619.course_pro_backend.service;

import com.elec5619.course_pro_backend.model.LoginRecord;

import java.util.List;

public interface LoginRecordService {
    List<LoginRecord> getAllLoginRecords();
    List<LoginRecord> getLoginRecordsByUsername(String username);
    void saveLoginRecord(LoginRecord loginRecord);
}