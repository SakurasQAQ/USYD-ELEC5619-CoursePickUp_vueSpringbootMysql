package com.elec5619.course_pro_backend.controller;

import com.elec5619.course_pro_backend.model.Comment;
import com.elec5619.course_pro_backend.model.Course;
import com.elec5619.course_pro_backend.model.User;
import com.elec5619.course_pro_backend.model.UserCourse;
import com.elec5619.course_pro_backend.repository.UserCourseRepository;
import com.elec5619.course_pro_backend.response.ApiResponse;
import com.elec5619.course_pro_backend.service.AIChatService;
import com.elec5619.course_pro_backend.service.CommentService;
import com.elec5619.course_pro_backend.service.CourseService;
import com.elec5619.course_pro_backend.service.UserService;
import com.elec5619.course_pro_backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:5173")
public class CourseController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private UserCourseRepository userCourseRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private AIChatService aiChatService;

    @GetMapping
    public ResponseEntity<List<Course>> getCourses(@RequestHeader("Authorization") String token) {
        // 验证 token 是否有效
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            try {
                String username = jwtUtil.extractUsername(token);
                if (jwtUtil.validateToken(token, username)) {
                    List<Course> courses = courseService.getAllCourses();
                    return ResponseEntity.ok(courses);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 如果访问令牌失效或无效
                return ResponseEntity.status(403).body(null);
            }
        }

        return ResponseEntity.status(401).build();
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getCourseById(
            @PathVariable String courseId,
            @RequestHeader("Authorization") String token) {
        // 验证 token 是否有效
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            try {
                String username = jwtUtil.extractUsername(token);
                if (jwtUtil.validateToken(token, username)) {
                    Course course = courseService.getCourseById(courseId);
                    if (course != null) {
                        return ResponseEntity.ok(course);
                    } else {
                        // 如果未找到课程，返回 404 Not Found
                        return ResponseEntity.status(404).build();
                    }
                }
            } catch (Exception e) {
                // 如果访问令牌失效或无效
                return ResponseEntity.status(401).body(null);
            }
        }
        // 如果 token 无效或没有传递，返回 403 Forbidden
        return ResponseEntity.status(403).build();
    }

    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String courseId, @RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            try {
                String username = jwtUtil.extractUsername(token);
                if (jwtUtil.validateToken(token, username)) {
                    courseService.deleteCourseById(courseId);
                    return ResponseEntity.noContent().build(); // 204 No Content
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
    }

    @PutMapping("/edit/{courseId}")
    public ResponseEntity<Void> editCourse(@PathVariable String courseId, @RequestHeader("Authorization") String token, @RequestBody Course updatedCourse) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            try {
                String username = jwtUtil.extractUsername(token);
                if (jwtUtil.validateToken(token, username)) {
                    if (courseService.existsById(courseId)) {
                        Course existingCourse = courseService.getCourseById(courseId);
                        if (existingCourse != null) {

                            existingCourse.setDescription(updatedCourse.getDescription());

                            existingCourse.setCourseName(updatedCourse.getCourseName());
                            existingCourse.setMajor(updatedCourse.getMajor());
                            existingCourse.setLocation(updatedCourse.getLocation());
                            existingCourse.setTimetable(updatedCourse.getTimetable());
                            existingCourse.setLecturer(updatedCourse.getLecturer());

                            courseService.updateCourse(existingCourse);
                        }
                        return ResponseEntity.noContent().build(); // 204 No Content
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
    }


    @PostMapping("/add")
    public ResponseEntity<Course> addCourse(@RequestHeader("Authorization") String token, @RequestBody Course newCourse) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                String username = jwtUtil.extractUsername(token);
                if (jwtUtil.validateToken(token, username)) {
                    Course createdCourse = courseService.addCourse(newCourse);
                    return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // Select a course for a user with token authentication
    @PostMapping("/{courseId}/select")
    public ResponseEntity<String> selectCourse(
            @PathVariable String courseId,
            @RequestParam Integer userId,
            @RequestHeader(value = "Authorization") String token) {

        // 提取实际的Token（移除前面的 "Bearer " 部分）
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token format");
        }

        // 验证token
        try {
            String username = jwtUtil.extractUsername(token);
            if (username == null || jwtUtil.isTokenExpired(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        // 检查用户和课程是否存在
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }

        Optional<Course> courseOptional = courseService.getCourseOptionalById(courseId);
        if (courseOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Course not found");
        }

        User user = userOptional.get();
        Course course = courseOptional.get();

        // 检查用户是否已经选了该课程
        Optional<UserCourse> existingUserCourse = userCourseRepository.findByUserAndCourse(user, course);
        if (existingUserCourse.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already selected this course");
        }

        // 如果用户没有选过该课程，则添加到 user_courses 表
        UserCourse userCourse = new UserCourse();
        userCourse.setUser(user);
        userCourse.setCourse(course);
        userCourseRepository.save(userCourse);

        return ResponseEntity.ok("Course selected successfully");
    }



    @GetMapping("/{courseId}/analyze")
    public ResponseEntity<ApiResponse<String>> analyzeCourse(
            @PathVariable String courseId,
            @RequestHeader(value = "Authorization") String token) {

        // 提取实际的 Token（移除前面的 "Bearer " 部分）
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Invalid token format", null));
        }

        // 验证 Token
        try {
            String email = jwtUtil.extractUsername(token);
            if (email == null || jwtUtil.isTokenExpired(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Invalid or expired token", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Invalid token", null));
        }

        // 根据 courseId 获取课程信息
        Optional<Course> courseOptional = courseService.getCourseOptionalById(courseId);
        if (courseOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, "Course not found", null));
        }
        Course course = courseOptional.get();

        // 构建 AI 请求消息，让 AI 介绍该课程
        String aiMessage = "Please describe the course in detail: " + course.getCourseName();

        // 调用 AI 服务处理消息
        String analysis = aiChatService.processUserMessage(aiMessage);

        // 在分析结果中添加分段符号（假设分段符号为 "\n\n"）
        String formattedAnalysis = analysis.replaceAll("\n", "\n\n");

        // 返回包含分析结果的 JSON 响应
        return ResponseEntity.ok(new ApiResponse<>(true, "Course analysis retrieved successfully", formattedAnalysis));
    }




    @GetMapping("/{courseId}/comments")
    public ResponseEntity<List<Comment>> getComments(
            @PathVariable String courseId,
            @RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String username = jwtUtil.extractUsername(token);

            if (jwtUtil.validateToken(token, username)) {
                List<Comment> comments = commentService.getCommentsByCourseId(courseId);
                return ResponseEntity.ok(comments);
            }
        }
        return ResponseEntity.status(401).build();
    }

    @PostMapping("/{courseId}/add-comments")
    public ResponseEntity<ApiResponse<String>> addComment(
            @PathVariable String courseId,
            @RequestBody Comment comment,
            @RequestHeader("Authorization") String token) {

        // 确保 Authorization 头部存在且格式正确
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String email = jwtUtil.extractUsername(token); // 使用 jwtUtil 获取邮箱

            if (jwtUtil.validateToken(token, email)) {
                // 根据邮箱查找用户对象
                User user = userService.findByEmail(email);
                if (user != null) {
                    // 验证用户是否选择了课程
                    boolean hasSelectedCourse = userService.hasUserSelectedCourse(user.getUserId(), courseId);
                    if (!hasSelectedCourse) {
                        ApiResponse<String> response = new ApiResponse<>(false, "User has not selected the course", null);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    }

                    // 设置用户ID到评论中
                    comment.setUserId(user.getUserId());
                    commentService.addComment(courseId, comment);

                    ApiResponse<String> response = new ApiResponse<>(true, "Comment added successfully", null);
                    return ResponseEntity.ok(response);
                }
            }
        }

        ApiResponse<String> response = new ApiResponse<>(false, "Unauthorized", null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }


    @GetMapping("/{userId}/courses")
    public ResponseEntity<List<UserCourse>> getUserCourses(
            @PathVariable Integer userId,
            @RequestHeader("Authorization") String token) {

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String username = jwtUtil.extractUsername(token);

            if (jwtUtil.validateToken(token, username)) {
                List<UserCourse> userCourses = userService.getUserCourses(userId);
                return ResponseEntity.ok(userCourses);
            }
        }
        return ResponseEntity.status(401).build();
    }





}