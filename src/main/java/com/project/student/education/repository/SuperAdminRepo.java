package com.project.student.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.student.education.entity.superAdmin;

@Repository
public interface SuperAdminRepo extends JpaRepository<superAdmin, Long> {

}
