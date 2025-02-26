package com.elec5619.course_pro_backend.service;

import com.elec5619.course_pro_backend.model.User;

import com.elec5619.course_pro_backend.model.UserCourse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface UserService {

    List<User> getAllUsers();

    User registerUser(User user);

    User login(String email, String password);

    User findByUsername(String username);

    User findById(Integer id);

    void deleteUserById(Integer id);

    boolean existsById(Integer id);

    void updateUser(User user);




    Optional<User> getUserById(Integer userId);

    List<UserCourse> getUserCourses(Integer userId);

    User findByEmail(String email);

    boolean hasUserSelectedCourse(Integer userId, String courseId);


    void cancelCourse(String userId, String courseId);




}

