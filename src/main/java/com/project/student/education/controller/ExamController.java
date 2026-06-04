package com.project.student.education.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.student.education.DTO.AddSubjectDTO;
import com.project.student.education.DTO.AdminMarksResponseDTO;
import com.project.student.education.DTO.AllExamsResponseDTO;
import com.project.student.education.DTO.AssignClassesDTO;
import com.project.student.education.DTO.DashboardAnalyticsDTO;
import com.project.student.education.DTO.ExamMasterDTO;
import com.project.student.education.DTO.ExamScheduleDTO;
import com.project.student.education.DTO.ExamSubjectDTO;
import com.project.student.education.DTO.HallTicketResponseDTO;
import com.project.student.education.DTO.ParentExamResponseDTO;
import com.project.student.education.DTO.ParentResultResponseDTO;
import com.project.student.education.DTO.ScheduleTimetableDTO;
import com.project.student.education.DTO.StudentReportResponseDTO;
import com.project.student.education.DTO.StudentResponseDTO;
import com.project.student.education.DTO.SubmitMarksDTO;
import com.project.student.education.DTO.TeacherSubjectResponseDTO;
import com.project.student.education.entity.ExamMaster;
import com.project.student.education.service.ExamService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;


    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'PRINCIPAL')")
    @PostMapping("/exams")
    public ResponseEntity<ExamMasterDTO> createExam(@RequestBody ExamMasterDTO examMasterDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(examService.createExam(examMasterDTO));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'PRINCIPAL')")
    @PostMapping("/exams/{examId}/subjects")
    public ResponseEntity<ExamSubjectDTO> addSubjectToExam(
            @PathVariable String examId,
            @RequestBody AddSubjectDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(examService.addSubjectToExam(examId, dto));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'PRINCIPAL')")
    @PostMapping("/exams/{examId}/classes")
    public ResponseEntity<String> assignClasses(
            @PathVariable String examId,
            @RequestBody AssignClassesDTO dto) {
        examService.assignClasses(examId, dto.getClassSectionIds());
        return ResponseEntity.ok("Classes assigned successfully to the exam.");
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'PRINCIPAL')")
    @PostMapping("/exams/timetable")
    public ResponseEntity<ExamScheduleDTO> scheduleTimetable(@RequestBody ScheduleTimetableDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(examService.scheduleTimetable(dto));
    }


    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/exams/my-subjects")
    public ResponseEntity<List<TeacherSubjectResponseDTO>> getMySubjects() {
        return ResponseEntity.ok(examService.getSubjectsForCurrentTeacher());
    }


    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/exams/my-subjects/{examSubjectId}/students")
    public ResponseEntity<List<StudentResponseDTO>> getStudentsForSubject(@PathVariable String examSubjectId) {
        return ResponseEntity.ok(examService.getStudentsForExamSubject(examSubjectId));
    }


    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/exams/marks")
    public ResponseEntity<String> enterMarks(@RequestBody SubmitMarksDTO dto) {
        examService.enterMarks(dto);
        return ResponseEntity.ok("Marks saved successfully.");
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'PRINCIPAL','TEACHER')")
    @GetMapping("/exams/{examId}/marks")
    public ResponseEntity<List<AdminMarksResponseDTO>> getAllMarks(
            @PathVariable String examId,
            @RequestParam(required = false) String classSectionId,
            @RequestParam(required = false) String subjectId,
            @RequestParam(required = false) String teacherId) {
        return ResponseEntity.ok(examService.getAllMarksForAdmin(examId, classSectionId, subjectId, teacherId));
    }


    @PreAuthorize("hasAnyRole('STUDENT', 'PARENT', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL')")
    @GetMapping("/parent/exams")
    public ResponseEntity<List<ParentExamResponseDTO>> getParentExams(@RequestParam String studentId) {
        return ResponseEntity.ok(examService.getExamsForParent(studentId));
    }

    @PreAuthorize("hasAnyRole('STUDENT', 'PARENT', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL')")
    @GetMapping("/parent/exams/timetable")
    public ResponseEntity<List<ExamScheduleDTO>> getParentTimetable(@RequestParam String studentId) {
        return ResponseEntity.ok(examService.getTimetableForStudent(studentId));
    }

    @PreAuthorize("hasAnyRole('STUDENT', 'PARENT', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL')")
    @GetMapping("/parent/exams/result/{examId}")
    public ResponseEntity<ParentResultResponseDTO> getParentResult(
            @PathVariable String examId,
            @RequestParam String studentId) {
        return ResponseEntity.ok(examService.getResultForParent(examId, studentId));
    }

    // Admin Publishing Action (Updates compiled global result records)
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'PRINCIPAL')")
    @PutMapping("/exams/{examId}/publish/{classSectionId}")
    public ResponseEntity<String> publishResult(
            @PathVariable String examId,
            @PathVariable String classSectionId) {
        examService.publishClassResults(examId, classSectionId);
        return ResponseEntity.ok("Results calculated and published successfully.");
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<ExamMaster>> getTeacherExams(
            @PathVariable String teacherId,
            @RequestParam(required = false) List<String> classSectionIds) {

        List<ExamMaster> exams = examService.getExamsForTeacher(teacherId, classSectionIds);
        return ResponseEntity.ok(exams);
    }



    @GetMapping("/student/{studentId}/report")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL')")
    public ResponseEntity<StudentReportResponseDTO> getStudentReportForAdmin(
            @PathVariable String studentId,
            @RequestParam(required = false) String academicYear) {

        StudentReportResponseDTO report = examService.generateStudentReport(studentId, academicYear);
        return ResponseEntity.ok(report);
    }
    @GetMapping("/teacher/student/{studentId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<StudentReportResponseDTO> getStudentReportForTeacher(
            @PathVariable String studentId,
            @RequestParam String teacherId,
            @RequestParam(required = false) String academicYear) {

        StudentReportResponseDTO report = examService.generateStudentReportForTeacher(studentId, teacherId, academicYear);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/teacher/dashboard/{teacherId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<DashboardAnalyticsDTO> getTeacherDashboard(@PathVariable String teacherId) {
        DashboardAnalyticsDTO metrics = examService.getTeacherMetrics(teacherId);
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/global")
    @PreAuthorize("hasAnyRole('PRINCIPAL', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<DashboardAnalyticsDTO> getGlobalDashboard() {
        DashboardAnalyticsDTO metrics = examService.getGlobalMetrics();
        return ResponseEntity.ok(metrics);
    }


    @GetMapping("/exam/{examId}/student/{studentId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER')")
    public ResponseEntity<HallTicketResponseDTO> getIndividualHallTicket(
            @PathVariable String examId,
            @PathVariable String studentId) {

        HallTicketResponseDTO hallTicket = examService.generateHallTicket(examId, studentId);
        return ResponseEntity.ok(hallTicket);
    }


    @GetMapping("/exam/{examId}/class-section/{classSectionId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER')")
    public ResponseEntity<List<HallTicketResponseDTO>> getClassHallTickets(
            @PathVariable String examId,
            @PathVariable String classSectionId) {

        List<HallTicketResponseDTO> hallTickets = examService.generateClassHallTickets(examId, classSectionId);
        return ResponseEntity.ok(hallTickets);
    }

    @GetMapping("/all-exams")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL')")
    public ResponseEntity<List<AllExamsResponseDTO>> getAllExamsForManagement() {
        List<AllExamsResponseDTO> exams = examService.getAllExams();
        return ResponseEntity.ok(exams);
    }
}