package com.project.student.education.repository;

import com.project.student.education.entity.LessonPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface LessonPlanRepository extends JpaRepository<LessonPlan, String> {
    List<LessonPlan> findByClassSection_ClassSectionIdAndSubject_SubjectIdOrderByPlannedDateAsc(
            String classSectionId, String subjectId);
    List<LessonPlan> findByTeacher_TeacherIdOrderByPlannedDateDesc(String teacherId);

    List<LessonPlan> findByClassSection_ClassSectionIdAndSubject_SubjectId(String classId, String subId);
}