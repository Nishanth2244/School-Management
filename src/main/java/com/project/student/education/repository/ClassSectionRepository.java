package com.project.student.education.repository;

import com.project.student.education.entity.ClassSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClassSectionRepository extends JpaRepository<ClassSection, String> {


    List<ClassSection> findByClassTeacher_TeacherId(String teacherId);

    Optional<ClassSection> findByClassNameAndAcademicYear(String className, String academicYear);


    Optional<ClassSection> findByClassNameAndSectionAndAcademicYear(String className, String section, String academicYear);

    boolean existsByClassTeacher_TeacherId(String classTeacherId);

    //boolean existsByClassSectionIdAndClassTeacherId(String classSectionId, String classTeacherId);

//    @Query("SELECT COUNT(cs) > 0 FROM ClassSection cs WHERE cs.classSectionId = :classSectionId AND cs.classTeacherId = :classTeacherId")
//    boolean existsByClassSectionIdAndClassTeacherId(
//            @Param("classSectionId") String classSectionId,
//            @Param("classTeacherId") String classTeacherId
//    );
    
    @Query("SELECT COUNT(cs) > 0 FROM ClassSection cs WHERE cs.classSectionId = :classSectionId AND cs.classTeacher.teacherId = :classTeacherId")
    boolean existsByClassSectionIdAndClassTeacherId(
            @Param("classSectionId") String classSectionId,
            @Param("classTeacherId") String classTeacherId
    );
}
