package com.project.student.education.repository;

import com.project.student.education.entity.Teacher;
import com.project.student.education.entity.User;
import com.project.student.education.entity.superAdmin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, String> {
    boolean existsByEmail(String email);

    @Query("SELECT COUNT(t) from Teacher t")
    Long countTeachers();


    Optional<Teacher> findByUser_Username(String loginName);

    Optional<Teacher> findByUser_Id(long id);

    @Query("SELECT t.teacherId FROM Teacher t")
    List<String> findAllTeacherIds();

	Optional<Teacher> findByUser(User user);
    @Query("SELECT t.teacherId FROM Teacher t JOIN t.subjectIds s WHERE s = :subjectId")
    List<String> findTeacherIdsBySubjectId(@Param("subjectId") String subjectId);

    // 2. Get full Teacher Objects for a single subject ID (If needed later)
    @Query("SELECT t FROM Teacher t JOIN t.subjectIds s WHERE s = :subjectId")
    List<Teacher> findTeachersBySubjectId(@Param("subjectId") String subjectId);
}
