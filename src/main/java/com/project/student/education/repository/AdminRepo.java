package com.project.student.education.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.student.education.entity.Admin;
import com.project.student.education.entity.User;

@Repository
public interface AdminRepo extends JpaRepository<Admin, Long> {

	Optional<Admin> findByUser(User user);

}
