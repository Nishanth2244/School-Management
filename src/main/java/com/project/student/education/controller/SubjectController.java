package com.project.student.education.controller;


import com.project.student.education.DTO.*;
import com.project.student.education.entity.Subject;
import com.project.student.education.service.StudentService;
import com.project.student.education.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("api/student/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private StudentService studentService;


    // ADMIN ONLY
    @PostMapping("/createSubject")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','PRINCIPAL')")
    public ResponseEntity<SubjectResponseDTO> createSubject(
            @RequestBody SubjectDTO subject) {

        SubjectResponseDTO response =
                subjectService.createSubject(subject);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    // ADMIN ONLY
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PutMapping("/{subjectId}")
    public ResponseEntity<SubjectDTO> updateSubject(
            @PathVariable String subjectId,
            @RequestBody SubjectDTO subjectDTO) {
        return ResponseEntity.ok(subjectService.updateSubject(subjectId, subjectDTO));
    }


    // ADMIN + TEACHER + STUDENT + PARENT
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','PRINCIPAL','STUDENT','PARENT','SUPER_ADMIN')")
    @GetMapping("/allSubjects")
    public ResponseEntity<List<SubjectResponseDTO>> getAllSubjects() {
        return ResponseEntity.ok(subjectService.getAllSubjects());
    }


    // ADMIN + TEACHER + STUDENT + PARENT
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','PRINCIPAL','STUDENT','PARENT','SUPER_ADMIN')")
    @GetMapping("/{subjectId}")
    public ResponseEntity<SubjectResponseDTO> getSubject(@PathVariable String subjectId) {
        return ResponseEntity.ok(subjectService.getSubjectById(subjectId));
    }


    // ADMIN ONLY
    @PreAuthorize("hasAnyRole('ADMIN','PRINCIPAL','SUPER_ADMIN')")
    @DeleteMapping("/{subjectId}")
    public ResponseEntity<Void> deleteSubject(@PathVariable String subjectId) {
        subjectService.deleteSubject(subjectId);
        return ResponseEntity.noContent().build();
    }


    // ADMIN ONLY - Assign subjects to a class
    @PreAuthorize("hasAnyRole('ADMIN','PRINCIPAL','SUPER_ADMIN')")
    @PostMapping("/assign")
    public ResponseEntity<List<ClassSubjectMappingDTO>> assignSubjectsToClass(
            @RequestBody ClassSubjectAssignRequest request) {

        return ResponseEntity.ok(subjectService.assignSubjects(request));
    }


    // ADMIN ONLY - Update subject + teacher mapping
    @PreAuthorize("hasAnyRole('ADMIN','PRINCIPAL','SUPER_ADMIN')")
    @PutMapping("/assign")
    public ResponseEntity<List<ClassSubjectMappingDTO>> updateAssignSubjectsToClass(
            @RequestBody ClassSubjectAssignRequest request) {

        return ResponseEntity.ok(subjectService.updateSubjectsAndTeachers(request));
    }


    // ADMIN + TEACHER
    @PreAuthorize("hasAnyRole('ADMIN','PRINCIPAL','TEACHER','SUPER_ADMIN')")
    @GetMapping("/assign/{classSectionId}")
    public ResponseEntity<List<ClassSubjectMappingDTO>> getAssignedSubjects(
            @PathVariable String classSectionId) {

        return ResponseEntity.ok(subjectService.getAssignedSubjects(classSectionId));
    }


    // ADMIN ONLY
    @PreAuthorize("hasAnyRole('ADMIN','PRINCIPAL','SUPER_ADMIN')")
    @PostMapping("/assignSubjectTeacher")
    public ResponseEntity<?> assignTeacher(@RequestBody AssignSubjectTeacherDTO dto) {
        return ResponseEntity.ok(Map.of("message", subjectService.assignTeacherToSubject(dto)));
    }


    // ADMIN + TEACHER
    @PreAuthorize("hasAnyRole('ADMIN','PRINCIPAL','TEACHER','SUPER_ADMIN')")
    @GetMapping("/{classSectionId}/teachers")
    public ResponseEntity<?> getSubjectTeacherMapping(@PathVariable String classSectionId) {
        return ResponseEntity.ok(subjectService.getMappingForClass(classSectionId));
    }

}
