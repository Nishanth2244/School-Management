package com.project.student.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//import com.google.common.base.Optional;
import com.project.student.education.entity.AdminInvitation;

@Repository
public interface AdminInvitationRepo extends JpaRepository<AdminInvitation, Long> {
	
	AdminInvitation findByToken(String token);

}
