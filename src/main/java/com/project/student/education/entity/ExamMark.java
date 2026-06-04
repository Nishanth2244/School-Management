package com.project.student.education.entity;

import com.project.student.education.enums.ExamAttendanceStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exam_marks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamMark {

    @Id
    @Column(name = "mark_id", length = 50)
    private String markId;

    @Column(name = "exam_id", nullable = false, length = 50)
    private String examId;

    @Column(name = "student_id", nullable = false, length = 50)
    private String studentId;

    @Column(name = "subject_id", nullable = false, length = 50)
    private String subjectId;

    @Column(name = "teacher_id", nullable = false, length = 50)
    private String teacherId;

    // MUST BE DOUBLE object wrapper to match your service implementation metrics
    @Column(name = "obtained_marks")
    private Double obtainedMarks;

    @Enumerated(EnumType.STRING)
    @Column(name = "attendance_status", nullable = false, length = 20)
    private ExamAttendanceStatus attendanceStatus;

    @Column(name = "remarks", length = 255)
    private String remarks;
}