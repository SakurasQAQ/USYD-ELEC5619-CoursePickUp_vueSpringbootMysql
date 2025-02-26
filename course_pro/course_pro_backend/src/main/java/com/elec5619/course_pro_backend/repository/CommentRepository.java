package com.elec5619.course_pro_backend.repository;

import com.elec5619.course_pro_backend.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByCourseId(String courseId);
}