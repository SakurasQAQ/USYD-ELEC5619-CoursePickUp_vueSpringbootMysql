package com.elec5619.course_pro_backend.service.impl;

import com.elec5619.course_pro_backend.model.Course;
import com.elec5619.course_pro_backend.model.User;
import com.elec5619.course_pro_backend.model.UserCourse;
import com.elec5619.course_pro_backend.repository.CourseRepository;
import com.elec5619.course_pro_backend.repository.UserCourseRepository;
import com.elec5619.course_pro_backend.repository.UserRepository;
import com.elec5619.course_pro_backend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserCourseRepository userCourseRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Course> getAllCourses() {
        // 从数据库获取课程数据
        return courseRepository.findAll();
    }
    @Override
    public Course getCourseById(String CourseId) {

        return courseRepository.findById(CourseId).orElse(null);
    }

    @Override
    public Optional<Course> getCourseOptionalById(String courseId) {
        return courseRepository.findByCourseId(courseId);
    }

    @Override
    public void deleteCourseById(String CourseId) {
        courseRepository.deleteById(CourseId);
    }

    @Override
    public void updateCourse(Course course) {
        courseRepository.save(course);
    }

    @Override
    public boolean existsById(String CourseId) {
        return courseRepository.existsById(CourseId);
    }


    @Override
    public Course addCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public void selectCourse(Integer userId, String courseId) {
        // 检查用户是否存在
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        // 检查课程是否存在
        Optional<Course> courseOptional = courseRepository.findByCourseId(courseId);
        if (courseOptional.isEmpty()) {
            throw new IllegalArgumentException("Course not found");
        }

        User user = userOptional.get();
        Course course = courseOptional.get();

        // 检查是否已经选过该课程
        Optional<UserCourse> existingRecord = userCourseRepository.findByUserAndCourse(user, course);
        if (existingRecord.isPresent()) {
            throw new IllegalStateException("Course already selected");
        }

        // 选课操作
        UserCourse userCourse = new UserCourse();
        userCourse.setUser(user);
        userCourse.setCourse(course);
        userCourseRepository.save(userCourse);
    }

    @Override
    public List<Course> getCoursesByMajor(String major) {
        return courseRepository.findByMajor(major);
    }

}