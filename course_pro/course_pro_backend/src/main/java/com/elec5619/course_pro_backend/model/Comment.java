package com.elec5619.course_pro_backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "comments")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String courseId;

    private Integer user_id;

    private String content;
    private Integer likes;

    public Integer getUserId() {
        return user_id;
    }

    public void setUserId(Integer userId) {
        this.user_id = userId;
    }

}
