package com.project.student.education.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exam_subjects")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamSubject {

    @Id
    @Column(name = "exam_subject_id", length = 50)
    private String examSubjectId;

    @Column(name = "exam_id", nullable = false, length = 50)
    private String examId;

    @Column(name = "subject_id", nullable = false, length = 50)
    private String subjectId;

    @Column(name = "teacher_id", nullable = false, length = 50)
    private String teacherId;

    @Column(name = "max_marks", nullable = false)
    private Integer maxMarks;

    @Column(name = "passing_marks", nullable = false)
    private Integer passingMarks;
}