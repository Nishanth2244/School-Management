package com.project.student.education.repository;

import com.project.student.education.entity.ExamMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamMarkRepository extends JpaRepository<ExamMark, String> {

    // Used in Step 7 to check for existing marks or update an existing mark record
    Optional<ExamMark> findByExamIdAndStudentIdAndSubjectId(String examId, String studentId, String subjectId);

    // Used in Steps 9 & Result Processing to aggregate all grades obtained by a student across an exam
    List<ExamMark> findByExamIdAndStudentId(String examId, String studentId);

    /**
     * Used in Step 8: Multi-view Admin filter dashboard.
     * Implements safe handling of optional parameters using inline SQL/JPQL null checking.
     */
    @Query("SELECT m FROM ExamMark m JOIN Student s ON m.studentId = s.studentId " +
           "WHERE m.examId = :examId " +
           "AND (:classId IS NULL OR s.classSectionId = :classId) " +
           "AND (:subjectId IS NULL OR m.subjectId = :subjectId) " +
           "AND (:teacherId IS NULL OR m.teacherId = :teacherId)")
    List<ExamMark> findFilteredMarks(
            @Param("examId") String examId,
            @Param("classId") String classId,
            @Param("subjectId") String subjectId,
            @Param("teacherId") String teacherId
    );
}