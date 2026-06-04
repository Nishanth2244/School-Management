package com.project.student.education.repository;

import com.project.student.education.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, String> {
    boolean existsByPhoneNo(String phoneNo);
}