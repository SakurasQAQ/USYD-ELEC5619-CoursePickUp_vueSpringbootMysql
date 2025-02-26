package com.elec5619.course_pro_backend.service;

import com.elec5619.course_pro_backend.model.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getCommentsByCourseId(String courseId);
    void addComment(String courseId, Comment comment);
}