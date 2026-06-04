package com.project.student.education.service;


import com.project.student.education.DTO.TimetableMiniDTO;
import com.project.student.education.DTO.WeeklyTimetableDTO;
import com.project.student.education.entity.Timetable;
import com.project.student.education.repository.TimetableRepository;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TimetableService {

    private final TimetableRepository timetableRepository;

    public TimetableService(TimetableRepository timetableRepository) {
        this.timetableRepository = timetableRepository;
    }

    public List<WeeklyTimetableDTO> getClassTimetable(String classSectionId) {
        List<Timetable> timetableList = timetableRepository
                .findByClassSection_ClassSectionIdOrderByDayAscStartTimeAsc(classSectionId);

        // Grouping logic (similar to how you built student weekly timetable)
        Map<String, WeeklyTimetableDTO> grouped = new LinkedHashMap<>();
        for (Timetable t : timetableList) {
            grouped.putIfAbsent(t.getDay(), WeeklyTimetableDTO.builder()
                    .day(t.getDay())
                    .list(new ArrayList<>())
                    .build());

            TimetableMiniDTO mini = new TimetableMiniDTO(
                    t.getDay(), t.getStartTime(), t.getEndTime(),
                    t.getSubject().getSubjectName(), t.getTeacher().getTeacherName()
            );
            grouped.get(t.getDay()).getList().add(mini);
        }
        return new ArrayList<>(grouped.values());
    }
}
