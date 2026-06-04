package com.project.student.education.controller;

import com.project.student.education.DTO.*;
import com.project.student.education.entity.ExamMaster;
import com.project.student.education.entity.TeacherAttendance;
import com.project.student.education.service.TeacherService;
import com.project.student.education.service.TransportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/student/teacher")
@RequiredArgsConstructor
public class TeacherController {

    @Autowired
    private TeacherService teacherService;
    @Autowired
    private TransportService transportService;


//    // ADMIN ONLY
//    @PreAuthorize("hasRole('ADMIN')")
//    @PostMapping("/add")
//    public ResponseEntity<TeacherDTO> addTeacher(@RequestBody TeacherDTO dto) {
//        return new ResponseEntity<>(teacherService.addTeacher(dto), HttpStatus.CREATED);
//    }

    @PostMapping("/register-link/bulk")
    @PreAuthorize("hasAnyRole('ADMIN','PRINCIPAL','SUPER_ADMIN')")
    public ResponseEntity<?> sendBulkRegistrationLinks(
            @RequestBody BulkEmailRequest request) {

        if (request.getEmails() == null || request.getEmails().isEmpty()) {
            return ResponseEntity.badRequest().body("Email list cannot be empty");
        }

        try {
            teacherService.sendRegistrationLink(request.getEmails());

            return ResponseEntity.ok("Registration links sent successfully to " + request.getEmails().size() + " teachers.");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<TeacherDTO> registerTeacher(
            @RequestParam String token,
            @RequestBody TeacherRegistrationDTO dto) {

        return ResponseEntity.ok(
                teacherService.registerTeacher(token, dto)
        );
    }


    // ADMIN + TEACHER
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','PRINCIPAL','TEACHER')")
    @GetMapping("/all")
    public ResponseEntity<List<TeacherDTO>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }


    // ADMIN + TEACHER
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','TEACHER','PRINCIPAL')")
    @GetMapping("/{teacherId}")
    public ResponseEntity<TeacherDTO> getTeacherById(@PathVariable String teacherId) {
        return ResponseEntity.ok(teacherService.getTeacherById(teacherId));
    }


    // ADMIN ONLY
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','PRINCIPAL')")
    @PutMapping("/{teacherId}")
    public ResponseEntity<TeacherDTO> updateTeacher(
            @PathVariable String teacherId,
            @RequestBody TeacherDTO dto) {
        return ResponseEntity.ok(teacherService.updateTeacher(teacherId, dto));
    }


    // ADMIN ONLY
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','PRINCIPAL')")
    @DeleteMapping("/{teacherId}")
    public ResponseEntity<String> deleteTeacher(@PathVariable String teacherId) {
        return ResponseEntity.ok(teacherService.deleteTeacher(teacherId));
    }


    // ADMIN ONLY
    @PreAuthorize("hasAnyAnyRole('ADMIN','SUPER_ADMIN','PRINCIPAL')")
    @PostMapping("/assign/{teacherId}/{classSectionId}")
    public ResponseEntity<String> assignTeacherToClass(
            @PathVariable String teacherId,
            @PathVariable String classSectionId
    ) {
        return ResponseEntity.ok(teacherService.assignTeacher(teacherId, classSectionId));
    }


    // ADMIN ONLY
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','PRINCIPAL')")
    @PutMapping("/assign/update/{classSectionId}/{teacherId}")
    public ResponseEntity<String> updateClassTeacher(
            @PathVariable String classSectionId,
            @PathVariable String teacherId) {
        return ResponseEntity.ok(teacherService.updateClassTeacher(classSectionId, teacherId));
    }


    // ADMIN + TEACHER
    @PreAuthorize("hasAnyRole('ADMIN','PRINCIPAL','SUPER_ADMIN','TEACHER')")
    @GetMapping("/assigned-classes/{teacherId}")
    public ResponseEntity<List<ClassSectionMiniDTO>> getClassesHandledByTeacher(
            @PathVariable String teacherId) {
        return ResponseEntity.ok(teacherService.getClassesHandledByTeacher(teacherId));
    }




    // ADMIN + TEACHER
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','PRINCIPAL','TEACHER')")
    @GetMapping("/count")
    public ResponseEntity<Long> countTeachers() {
        return ResponseEntity.ok(teacherService.getTeacherCount());
    }


    // ADMIN + SUPER_ADMIN + TEACHER
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','TEACHER')")
    @GetMapping("/{teacherId}/classes-subjects")
    public ResponseEntity<Map<String, Object>> getTeacherClassesWithSubjects(
            @PathVariable String teacherId) {
        return ResponseEntity.ok(teacherService.getTeacherClassesWithSubjects(teacherId));
    }

    @PostMapping("/attendance")
    public String markAttendance(
            @RequestBody MarkTeacherAttendanceRequest request) {

        return teacherService.markAttendance(request);
    }

    @GetMapping("/teacher/{teacherId}/attendance")
    public List<TeacherAttendanceResponseDTO>
    getAttendance(
            @PathVariable String teacherId) {

        return teacherService.getAttendance(teacherId);
    }

}
