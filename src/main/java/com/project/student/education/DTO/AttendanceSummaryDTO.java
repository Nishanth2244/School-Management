package com.project.student.education.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceSummaryDTO {

    private long present;

    private long absent;

    private long total;
}