package com.elec5619.course_pro_backend.service;

import com.elec5619.course_pro_backend.dto.CourseData;
import reactor.core.publisher.Mono;

public interface ChatService {
    Mono<String> getChatResponse(String userMessage);

    Mono<String> getStudyPlan(CourseData courseData);

}
