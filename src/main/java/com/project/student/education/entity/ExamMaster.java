package com.project.student.education.entity;

import com.project.student.education.enums.ExamStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exam_master")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamMaster {

    @Id
    @Column(name = "exam_id", length = 50)
    private String examId;

    @Column(nullable = false)
    private String examName;

    private String academicYear;

    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private ExamStatus status;

    private String createdBy;
    private LocalDateTime createdAt;

    private String updatedBy;
    private LocalDateTime updatedAt;

    // Explicit initialization fixes the builder null-pointer issues during Hibernate schema scanning
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "exam_class_sections", joinColumns = @JoinColumn(name = "exam_id"))
    @Column(name = "class_section_id")
    @Builder.Default
    private List<String> assignedClassSectionIds = new ArrayList<>();
}