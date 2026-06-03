package com.project.student.education.DTO;

import lombok.Data;
import java.util.List;

@Data
public class BulkEmailRequest {
    private List<String> emails;
}