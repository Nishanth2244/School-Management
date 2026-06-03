package com.project.student.education.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "class_sections",
        uniqueConstraints = @UniqueConstraint(columnNames = {"class_name", "section", "academic_year"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassSection {

    @Id
    private String classSectionId;

    @Column(name = "class_name", nullable = false)
    private String className;

    private String section;

    @Column(name = "academic_year", nullable = false)
    private String academicYear;



    private Integer capacity;
    private Integer currentStrength;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


    @OneToMany(mappedBy = "classSection", cascade = CascadeType.ALL)
    private List<Student> students = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "class_teacher_id")
    private Teacher classTeacher;
    
    private Boolean isActive = true;



}
