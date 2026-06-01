package com.project.student.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.student.education.entity.Driver;

@Repository
public interface DriverRepo extends JpaRepository<Driver, String> {

}
