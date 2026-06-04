package com.project.student.education.service;

import com.project.student.education.entity.Holiday;
import com.project.student.education.repository.HolidayRepository;
import com.project.student.education.repository.StudentRepository;
import com.project.student.education.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidayService {

    private final HolidayRepository holidayRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final NotificationService notificationService;

    @Transactional
    public Holiday createHoliday(Holiday holiday) {

        if (holidayRepository.existsByDate(holiday.getDate())) {
            throw new IllegalArgumentException(
                    "Holiday already exists for date: " + holiday.getDate()
            );
        }

        Holiday savedHoliday = holidayRepository.save(holiday);

        String title = "Holiday Announced";
        String message =
                "Holiday on " + savedHoliday.getDate() +
                        ": " + savedHoliday.getDescription();

        // Notify all students
        studentRepository.findAll().forEach(student ->
                notificationService.sendNotification(
                        student.getStudentId(),
                        title,
                        message,
                        "HOLIDAY"
                )
        );

        // Notify all teachers
        teacherRepository.findAll().forEach(teacher ->
                notificationService.sendNotification(
                        teacher.getTeacherId(),
                        title,
                        message,
                        "HOLIDAY"
                )
        );

        return savedHoliday;
    }

    public List<Holiday> getHolidays(LocalDate start, LocalDate end) {
        return holidayRepository.findByDateBetween(start, end);
    }
}