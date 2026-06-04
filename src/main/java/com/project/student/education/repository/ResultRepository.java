package com.project.student.education.repository;

import com.project.student.education.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<Result, String> {

    // Used in Step 9 to get the final published performance sheet for parents and students
    Optional<Result> findByExamIdAndStudentId(String examId, String studentId);
    
    // Checks whether an entire class profile processing instance has already been compiled
    boolean existsByExamIdAndStudentId(String examId, String studentId);
}