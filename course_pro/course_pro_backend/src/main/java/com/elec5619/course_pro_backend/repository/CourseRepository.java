package com.elec5619.course_pro_backend.repository;

import com.elec5619.course_pro_backend.model.Course;
import com.elec5619.course_pro_backend.model.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, String>{

    List<Course> findByMajor(String major);  // 按专业查找课程
    Optional<Course> findByCourseId(String courseId);

    @Query("SELECT uc FROM UserCourse uc WHERE uc.user.UserId = :userId")
    List<UserCourse> findCoursesByUserId(@Param("userId") Integer userId);

}
