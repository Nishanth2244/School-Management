package com.project.student.education.service;

import com.project.student.education.DTO.*;
import com.project.student.education.entity.*;
import com.project.student.education.enums.ExamAttendanceStatus;
import com.project.student.education.enums.ExamStatus;
import com.project.student.education.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final IdGenerator idGenerator;
    private final ModelMapper modelMapper;

    private final ExamMasterRepository examRepo;
    private final ExamSubjectRepository examSubjectRepo;
    private final ExamScheduleRepository examScheduleRepo;
    private final ExamMarkRepository examMarkRepo;
    private final ResultRepository resultRepo;
    private final ClassSectionRepository classSectionRepo;
    private final StudentRepository studentRepo;
    private final NotificationService notificationService;

    private String getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // Step 1: Create Exam
    @Transactional
    public ExamMasterDTO createExam(ExamMasterDTO dto) {
        if (examRepo.existsByExamNameAndAcademicYear(dto.getExamName(), dto.getAcademicYear())) {
            throw new RuntimeException("Exam already exists for this academic year");
        }
        ExamMaster exam = modelMapper.map(dto, ExamMaster.class);
        exam.setExamId(idGenerator.generateId("EXM"));
        exam.setStatus(ExamStatus.CREATED);
        exam.setCreatedBy(getCurrentUser());
        exam.setCreatedAt(LocalDateTime.now());

        return modelMapper.map(examRepo.save(exam), ExamMasterDTO.class);
    }

    // Step 2: Add Subjects to Exam
    @Transactional
    public ExamSubjectDTO addSubjectToExam(String examId, AddSubjectDTO dto) {
        ExamMaster exam = examRepo.findById(examId)
                .orElseThrow(() -> new EntityNotFoundException("Exam not found with ID: " + examId));

        ExamSubject examSubject = new ExamSubject();
        examSubject.setExamSubjectId(idGenerator.generateId("EXS"));
        examSubject.setExamId(exam.getExamId());
        examSubject.setSubjectId(dto.getSubjectId());
        examSubject.setTeacherId(dto.getTeacherId());
        examSubject.setMaxMarks(dto.getMaxMarks());
        examSubject.setPassingMarks(dto.getPassingMarks());

        return modelMapper.map(examSubjectRepo.save(examSubject), ExamSubjectDTO.class);
    }

    // Step 3: Assign Classes
    @Transactional
    public void assignClasses(String examId, List<String> classSectionIds) {
        ExamMaster exam = examRepo.findById(examId)
                .orElseThrow(() -> new EntityNotFoundException("Exam not found"));

        // Assuming database tracking array mapping exists or linking entity update logic
        exam.setAssignedClassSectionIds(classSectionIds);
        examRepo.save(exam);
    }

    // Step 4: Schedule Exam Timetable
    @Transactional
    public ExamScheduleDTO scheduleTimetable(ScheduleTimetableDTO dto) {
        ExamSchedule schedule = new ExamSchedule();
        schedule.setScheduleId(idGenerator.generateId("SCH"));
        schedule.setExamId(dto.getExamId());
        schedule.setClassSectionId(dto.getClassSectionId());
        schedule.setSubjectId(dto.getSubjectId());
        schedule.setExamDate(dto.getExamDate());
        schedule.setStartTime(dto.getStartTime());
        schedule.setEndTime(dto.getEndTime());

        return modelMapper.map(examScheduleRepo.save(schedule), ExamScheduleDTO.class);
    }

    // Step 5: Teacher Dashboard
    public List<TeacherSubjectResponseDTO> getSubjectsForCurrentTeacher() {
        String teacherUsername = getCurrentUser();
        List<ExamSubject> assignments = examSubjectRepo.findByTeacherId(teacherUsername);

        return assignments.stream().map(asgn -> {
            ExamMaster exam = examRepo.findById(asgn.getExamId()).orElse(null);
            return TeacherSubjectResponseDTO.builder()
                    .examSubjectId(asgn.getExamSubjectId())
                    .examName(exam != null ? exam.getExamName() : "N/A")
                    .subjectId(asgn.getSubjectId())
                    .maxMarks(asgn.getMaxMarks())
                    .build();
        }).collect(Collectors.toList());
    }

    // Step 6: Teacher Gets Student List
    public List<StudentResponseDTO> getStudentsForExamSubject(String examSubjectId) {
        ExamSubject assignment = examSubjectRepo.findById(examSubjectId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment tracking element not found."));

        // Guard Check: Verify identity constraints
        if (!assignment.getTeacherId().equalsIgnoreCase(getCurrentUser())) {
            throw new AccessDeniedException("Access Denied: You are not assigned to this course setup.");
        }

        // Fetch student lists map configuration records linked to target Exam Configuration context mappings
        ExamMaster exam = examRepo.findById(assignment.getExamId()).orElseThrow();
        List<Student> students = studentRepo.findByClassSection_ClassSectionIdIn(exam.getAssignedClassSectionIds());

        return students.stream()
                .map(s -> new StudentResponseDTO(s.getStudentId(), s.getFullName(), s.getRollNumber()))
                .collect(Collectors.toList());
    }

    // Step 7: Teacher Enters Marks
    @Transactional
    public void enterMarks(SubmitMarksDTO dto) {
        ExamSubject assignment = examSubjectRepo.findById(dto.getExamSubjectId())
                .orElseThrow(() -> new EntityNotFoundException("Subject entry reference parameters missing"));

        // Guard Check: Security enforcement
        if (!assignment.getTeacherId().equalsIgnoreCase(getCurrentUser())) {
            throw new AccessDeniedException("Access Denied: You are not authorized to submit records for this group.");
        }

        for (StudentMarkDTO markEntry : dto.getMarks()) {
            // Check if record exists, if yes update, otherwise create new tracking instance
            ExamMark mark = examMarkRepo.findByExamIdAndStudentIdAndSubjectId(
                            assignment.getExamId(), markEntry.getStudentId(), assignment.getSubjectId())
                    .orElse(new ExamMark());

            mark.setMarkId(mark.getMarkId() == null ? idGenerator.generateId("MRK") : mark.getMarkId());
            mark.setExamId(assignment.getExamId());
            mark.setStudentId(markEntry.getStudentId());
            mark.setSubjectId(assignment.getSubjectId());
            mark.setTeacherId(assignment.getTeacherId());
            mark.setObtainedMarks(markEntry.getObtainedMarks());
            mark.setAttendanceStatus(markEntry.getObtainedMarks() != null ? ExamAttendanceStatus.PRESENT : ExamAttendanceStatus.ABSENT);
            mark.setRemarks(markEntry.getRemarks());

            examMarkRepo.save(mark);
        }
    }

    // Step 8: Admin View All Marks
    public List<AdminMarksResponseDTO> getAllMarksForAdmin(String examId, String classId, String subjectId, String teacherId) {
        // Business filter abstraction layers logic using custom specs or repository parameter filters
        List<ExamMark> marks = examMarkRepo.findFilteredMarks(examId, classId, subjectId, teacherId);

        return marks.stream().map(m -> AdminMarksResponseDTO.builder()
                .markId(m.getMarkId())
                .studentId(m.getStudentId())
                .studentName(studentRepo.findNameById(m.getStudentId()))
                .subjectId(m.getSubjectId())
                .obtainedMarks(m.getObtainedMarks())
                .attendanceStatus(m.getAttendanceStatus())
                .build()).collect(Collectors.toList());
    }

    // Step 9: Parent View - Available Exams
    public List<ParentExamResponseDTO> getExamsForParent(String studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student parameters not found."));

        List<ExamMaster> exams = examRepo.findExamsByClassSectionId(student.getClassSectionId());
        return exams.stream().map(e -> new ParentExamResponseDTO(e.getExamName(), e.getStartDate()))
                .collect(Collectors.toList());
    }

    // Step 9: Parent View - Timetable
    public List<ExamScheduleDTO> getTimetableForStudent(String studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student entity not found"));

        List<ExamSchedule> schedules = examScheduleRepo.findByClassSectionId(student.getClassSectionId());
        return schedules.stream().map(s -> modelMapper.map(s, ExamScheduleDTO.class)).collect(Collectors.toList());
    }

    // Step 9: Parent View - Compile & Fetch Result
    public ParentResultResponseDTO getResultForParent(String examId, String studentId) {
        Result result = resultRepo.findByExamIdAndStudentId(examId, studentId)
                .orElseThrow(() -> new RuntimeException("Results are not compiled or published for this exam."));

        if (!result.getPublished()) {
            throw new RuntimeException("Results for this exam have not been made public yet.");
        }

        List<ExamMark> finalMarks = examMarkRepo.findByExamIdAndStudentId(examId, studentId);
        List<ParentSubjectMarkDTO> parsedSubjects = finalMarks.stream().map(m -> {
            ExamSubject config = examSubjectRepo.findByExamIdAndSubjectId(examId, m.getSubjectId()).orElse(null);
            return ParentSubjectMarkDTO.builder()
                    .subject(m.getSubjectId())
                    .marks(m.getObtainedMarks())
                    .maxMarks(config != null ? config.getMaxMarks() : 100)
                    .build();
        }).collect(Collectors.toList());

        return ParentResultResponseDTO.builder()
                .studentName(studentRepo.findNameById(studentId))
                .percentage(result.getPercentage())
                .rank(result.getRank())
                .subjects(parsedSubjects)
                .build();
    }

    // Core Processing Workflow: Processing individual grades and establishing final ranking positions
    @Transactional
    public void publishClassResults(String examId, String classSectionId) {
        List<Student> students = studentRepo.findByClassSection_ClassSectionId(classSectionId);
        List<Result> calculatedResults = new ArrayList<>();

        for (Student student : students) {
            List<ExamMark> studentMarks = examMarkRepo.findByExamIdAndStudentId(examId, student.getStudentId());
            if (studentMarks.isEmpty()) continue;

            double marksEarned = studentMarks.stream().mapToDouble(m -> m.getObtainedMarks() != null ? m.getObtainedMarks() : 0.0).sum();

            // Collect the max criteria targets via dynamic structural iterations
            double totalPointsPossible = studentMarks.stream().mapToDouble(m -> {
                return examSubjectRepo.findByExamIdAndSubjectId(examId, m.getSubjectId()).map(ExamSubject::getMaxMarks).orElse(100);
            }).sum();

            Result result = resultRepo.findByExamIdAndStudentId(examId, student.getStudentId()).orElse(new Result());
            result.setResultId(result.getResultId() == null ? idGenerator.generateId("RES") : result.getResultId());
            result.setExamId(examId);
            result.setStudentId(student.getStudentId());
            result.setTotalMarks(marksEarned);
            result.setPercentage(totalPointsPossible > 0 ? (marksEarned / totalPointsPossible) * 100 : 0.0);
            result.setPublished(true);

            calculatedResults.add(result);
        }

        // Apply Rank Calculations relative to performance inside group
        calculatedResults.sort((r1, r2) -> Double.compare(r2.getTotalMarks(), r1.getTotalMarks()));
        for (int i = 0; i < calculatedResults.size(); i++) {
            calculatedResults.get(i).setRank(i + 1);
            resultRepo.save(calculatedResults.get(i));

            // Notify students of final updates
            notificationService.sendNotification(
                    calculatedResults.get(i).getStudentId(),
                    "Results Out",
                    "Your metrics report data cards are active.",
                    "EXAM"
            );
        }
    }

    public List<ExamMaster> getExamsForTeacher(String teacherId, List<String> assignedClassSectionIds) {
        if (assignedClassSectionIds == null || assignedClassSectionIds.isEmpty()) {
            // Fallback: If no classes are explicitly passed, find exams where they teach a subject
            return examRepo.findExamsByTeacherAndClasses(teacherId, List.of(""));
        }

        return examRepo.findExamsByTeacherAndClasses(teacherId, assignedClassSectionIds);
    }

}