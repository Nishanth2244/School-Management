package com.project.student.education.repository;

import com.project.student.education.entity.ExamSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamSubjectRepository extends JpaRepository<ExamSubject, String> {
    
    // Used in Step 5 to load all subject configurations assigned to a logged-in teacher
    List<ExamSubject> findByTeacherId(String teacherId);

    // Used in Step 9 to get configuration details (like maxMarks) for a specific subject in an exam
    Optional<ExamSubject> findByExamIdAndSubjectId(String examId, String subjectId);
    
    // Checks if a subject is already added to a specific exam
    boolean existsByExamIdAndSubjectId(String examId, String subjectId);
}