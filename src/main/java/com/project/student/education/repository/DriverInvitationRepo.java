package com.project.student.education.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.student.education.entity.DriverInvitation;
import com.project.student.education.entity.User;

@Repository
public interface DriverInvitationRepo extends JpaRepository<DriverInvitation, Long> {

	Optional<DriverInvitation> findByToken(String token);

}
