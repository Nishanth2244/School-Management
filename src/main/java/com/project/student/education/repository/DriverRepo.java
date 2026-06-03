package com.project.student.education.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.student.education.entity.Driver;
import com.project.student.education.entity.User;
import com.project.student.education.entity.superAdmin;

@Repository
public interface DriverRepo extends JpaRepository<Driver, String> {

	Optional<Driver> findByUser(User user);

}
