package com.project.student.education.repository;


import com.project.student.education.config.AssignmentId;
import com.project.student.education.entity.Assignment;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, AssignmentId> {
    boolean existsByTitleAndSubject_SubjectIdAndClassSection_ClassSectionId(String title, String subjectId, String classSectionId);

    List<Assignment> findByTeacher_TeacherId(String teacherId);

    List<Assignment> findBySubject_SubjectId(String subjectSubjectId);

    List<Assignment> findByClassSection_ClassSectionId(String classSectionId);
    
    Long countByTeacherTeacherId(String teacherId);

}
