package com.project.student.education.controller;

import com.project.student.education.DTO.LessonPlanRequestDTO;
import com.project.student.education.DTO.LessonPlanResponseDTO;
import com.project.student.education.service.LessonPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/student/lesson-plans")
@RequiredArgsConstructor
public class LessonPlanController {
    private final LessonPlanService service;

    // Create a new topic
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','SUPER_ADMIN')")
    public ResponseEntity<LessonPlanResponseDTO> create(@RequestBody LessonPlanRequestDTO dto) {
        return new ResponseEntity<>(service.createLessonPlan(dto), HttpStatus.CREATED);
    }

    // Update topic details or completion status (No PATCH needed)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','SUPER_ADMIN')")
    public ResponseEntity<LessonPlanResponseDTO> update(@PathVariable String id, @RequestBody LessonPlanRequestDTO dto) {
        return ResponseEntity.ok(service.updateLessonPlan(id, dto));
    }

    // Delete a topic
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','SUPER_ADMIN')")
    public ResponseEntity<String> delete(@PathVariable String id) {
        service.deleteLessonPlan(id);
        return ResponseEntity.ok("Lesson plan deleted successfully.");
    }

    // List all topics for a class/subject
    @GetMapping("/list/{classId}/{subId}")
    public ResponseEntity<List<LessonPlanResponseDTO>> getTopics(@PathVariable String classId, @PathVariable String subId) {
        return ResponseEntity.ok(service.getTopics(classId, subId));
    }
    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'PRINCIPAL', 'TEACHER')")
    public ResponseEntity<List<LessonPlanResponseDTO>> getTeacherPlans(@PathVariable String teacherId) {
        return ResponseEntity.ok(service.getLessonPlansByTeacher(teacherId));
    }
}