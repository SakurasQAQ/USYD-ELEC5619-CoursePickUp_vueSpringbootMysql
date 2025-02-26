package com.elec5619.course_pro_backend.service.impl;

import com.elec5619.course_pro_backend.model.Comment;
import com.elec5619.course_pro_backend.repository.CommentRepository;
import com.elec5619.course_pro_backend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> getCommentsByCourseId(String courseId) {
        return commentRepository.findByCourseId(courseId);
    }

    @Override
    public void addComment(String courseId, Comment comment) {
        comment.setCourseId(courseId);
        commentRepository.save(comment);
    }
}