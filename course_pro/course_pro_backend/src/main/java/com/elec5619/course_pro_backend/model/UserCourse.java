package com.elec5619.course_pro_backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_courses")
@Data
public class UserCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 关联到 User 表
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    // 关联到 Course 表
    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private Course course;



    public String getCourseId() {
        return course.getCourseId();
    }

    // 获取课程名称
    public String getCourseName() {
        return course.getCourseName();
    }

}
