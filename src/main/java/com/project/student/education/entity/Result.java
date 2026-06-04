package com.project.student.education.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "results")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result {

    @Id
    @Column(name = "result_id", length = 50)
    private String resultId;

    @Column(name = "exam_id", nullable = false, length = 50)
    private String examId;

    @Column(name = "student_id", nullable = false, length = 50)
    private String studentId;

    @Column(name = "total_marks", nullable = false)
    private Double totalMarks;

    @Column(name = "percentage", nullable = false)
    private Double percentage;

    @Column(name = "rank")
    private Integer rank;

    @Column(name = "is_published", nullable = false)
    private Boolean published = false;
}