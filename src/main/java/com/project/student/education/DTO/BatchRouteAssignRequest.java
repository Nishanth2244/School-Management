package com.project.student.education.DTO;

import lombok.Data;
import java.util.List;

@Data
public class BatchRouteAssignRequest {
    private String routeId;
    private List<StudentAssignmentDetails> students;

    @Data
    public static class StudentAssignmentDetails {
        private String studentId;
        private String pickupStop;
        private String dropStop;
        private String pickupTime;
        private String dropTime;
        private String feeStatus;
    }
}