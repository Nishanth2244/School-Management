package com.project.student.education.repository;

import com.project.student.education.entity.ExamMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamMasterRepository extends JpaRepository<ExamMaster, String> {

    List<ExamMaster> findByAcademicYear(String academicYear);

    boolean existsByExamNameAndAcademicYear(String examName, String academicYear);

    // Fixes line 184 error: Finds exams containing this class section within its list mapping
    @Query("SELECT e FROM ExamMaster e JOIN e.assignedClassSectionIds c WHERE c = :classSectionId")
    List<ExamMaster> findExamsByClassSectionId(@Param("classSectionId") String classSectionId);

    @Query("""
        SELECT DISTINCT e FROM ExamMaster e 
        WHERE e.examId IN (
            SELECT es.examId FROM ExamSubject es WHERE es.teacherId = :teacherId
        )
        OR e.examId IN (
            SELECT sch.examId FROM ExamSchedule sch 
            WHERE sch.classSectionId IN (:classSectionIds)
        )
    """)
    List<ExamMaster> findExamsByTeacherAndClasses(
            @Param("teacherId") String teacherId,
            @Param("classSectionIds") List<String> classSectionIds
            
    );
}