package com.project.student.education.service;

import com.project.student.education.DTO.LessonPlanRequestDTO;
import com.project.student.education.DTO.LessonPlanResponseDTO;
import com.project.student.education.entity.*;
import com.project.student.education.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LessonPlanService {

    private final LessonPlanRepository repository;
    private final ClassSectionRepository csRepo;
    private final SubjectRepository subRepo;
    private final TeacherRepository tchRepo;
    private final IdGenerator idGenerator;

    // CREATE: Professional topic assignment
    public LessonPlanResponseDTO createLessonPlan(LessonPlanRequestDTO dto) {
        var cs = csRepo.findById(dto.getClassSectionId())
                .orElseThrow(() -> new RuntimeException("Class section not found"));
        var sub = subRepo.findById(dto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        var tch = tchRepo.findById(dto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        LessonPlan plan = LessonPlan.builder()
                .lessonPlanId(idGenerator.generateId("LPN"))
                .classSection(cs)
                .subject(sub)
                .teacher(tch)
                .topicName(dto.getTopicName())
                .isCompleted(false)
                .build();

        return mapToDTO(repository.save(plan));
    }

    // UPDATE: Full edit including topic name and completion status
    public LessonPlanResponseDTO updateLessonPlan(String id, LessonPlanRequestDTO dto) {
        LessonPlan plan = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson plan not found"));

        plan.setTopicName(dto.getTopicName());
        plan.setIsCompleted(dto.getIsCompleted());

        return mapToDTO(repository.save(plan));
    }

    // READ: List all topics for a specific class-subject pair
    @Transactional(readOnly = true)
    public List<LessonPlanResponseDTO> getTopics(String classId, String subId) {
        return repository.findByClassSection_ClassSectionIdAndSubject_SubjectId(classId, subId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // DELETE: Remove a topic
    public void deleteLessonPlan(String id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Lesson plan not found");
        }
        repository.deleteById(id);
    }

    private LessonPlanResponseDTO mapToDTO(LessonPlan p) {
        return LessonPlanResponseDTO.builder()
                .lessonPlanId(p.getLessonPlanId())
                .classSectionId(p.getClassSection().getClassSectionId())
                .className(p.getClassSection().getClassName())
                .section(p.getClassSection().getSection())
                .subjectName(p.getSubject().getSubjectName())
                .teacherName(p.getTeacher().getTeacherName())
                .topicName(p.getTopicName())
                .isCompleted(p.getIsCompleted())
                .build();
    }

    @Transactional(readOnly = true)
    public List<LessonPlanResponseDTO> getLessonPlansByTeacher(String teacherId) {
        return repository.findByTeacher_TeacherIdOrderByPlannedDateDesc(teacherId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }
}