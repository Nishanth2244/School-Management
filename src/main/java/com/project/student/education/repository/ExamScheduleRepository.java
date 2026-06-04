package com.project.student.education.repository;

import com.project.student.education.entity.ExamSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamScheduleRepository extends JpaRepository<ExamSchedule, String> {

    // Used in Step 9 to fetch the exam timetable for a student's class section
    List<ExamSchedule> findByClassSectionId(String classSectionId);

    // Fetches the timetable for a specific exam within a targeted class section
    List<ExamSchedule> findByExamIdAndClassSectionId(String examId, String classSectionId);
}