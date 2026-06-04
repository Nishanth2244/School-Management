package com.project.student.education.repository;

import com.project.student.education.entity.DriverIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverIssueRepository extends JpaRepository<DriverIssue, String> {
}