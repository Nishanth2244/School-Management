package com.project.student.education.repository;

import com.project.student.education.entity.TeacherRegistrationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRegistrationTokenRepository
        extends JpaRepository<TeacherRegistrationToken, Long> {

    Optional<TeacherRegistrationToken> findByToken(String token);

    Optional<TeacherRegistrationToken> findByEmail(String email);
}