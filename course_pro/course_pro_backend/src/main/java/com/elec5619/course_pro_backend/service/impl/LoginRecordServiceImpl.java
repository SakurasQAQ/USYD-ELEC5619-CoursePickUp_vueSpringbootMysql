package com.elec5619.course_pro_backend.service.impl;

import com.elec5619.course_pro_backend.model.LoginRecord;
import com.elec5619.course_pro_backend.repository.LoginRecordRepository;
import com.elec5619.course_pro_backend.service.LoginRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginRecordServiceImpl implements LoginRecordService {

    @Autowired
    private LoginRecordRepository loginRecordRepository;

    @Override
    public List<LoginRecord> getAllLoginRecords() {
        return loginRecordRepository.findAll();
    }

    @Override
    public List<LoginRecord> getLoginRecordsByUsername(String username) {
        return loginRecordRepository.findByUsername(username);
    }

    @Override
    public void saveLoginRecord(LoginRecord loginRecord) {
        loginRecordRepository.save(loginRecord);
    }
}