package com.project.student.education.service;

import com.project.student.education.DTO.ClassSectionDTO;
import com.project.student.education.DTO.ClassSectionRequest;
import com.project.student.education.DTO.StudentDTO;
import com.project.student.education.entity.*;
import com.project.student.education.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassSectionService {

    private final ClassSectionRepository classSectionRepository;
    private final TeacherRepository teacherRepository;
    private final IdGenerator idGenerator;
    private final ModelMapper modelMapper;
    private final StudentRepository studentRepository;
    private final ClassSubjectMappingRepository classSubjectMappingRepository;
    private final SubjectRepository subjectRepository;

    public ClassSectionDTO createClassSection(ClassSectionRequest request) {

        classSectionRepository.findByClassNameAndSectionAndAcademicYear(
                        request.getClassName(), request.getSection(), request.getAcademicYear()
                )
                .ifPresent(existing -> {
                    throw new RuntimeException("Class section already exists for this academic year!");
                });

        String id = idGenerator.generateId("CLS");

//        Teacher classTeacher = null;
//
//        if (request.getClassTeacherId() != null && !request.getClassTeacherId().isBlank()) {
//            classTeacher = teacherRepository.findById(request.getClassTeacherId())
//                    .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + request.getClassTeacherId()));
//        }

        ClassSection classSection = ClassSection.builder()
                .classSectionId(id)
                .className(request.getClassName())
                .section(request.getSection())
                .academicYear(request.getAcademicYear())
//                .classTeacher(classTeacher)
                .capacity(request.getCapacity())
                .currentStrength(request.getCurrentStrength())
                .build();

        ClassSection savedClass = classSectionRepository.save(classSection);

        if (request.getSubjectIds() != null) {
            for (String subjectId : request.getSubjectIds()) {

                Subject subject = subjectRepository.findById(subjectId)
                        .orElseThrow(() -> new RuntimeException("Subject not found: " + subjectId));

                String mappingId = idGenerator.generateId("CSM");

                ClassSubjectMapping mapping = ClassSubjectMapping.builder()
                        .id(mappingId)
                        .classSection(savedClass)
                        .subject(subject)
                        .teacher(null)
                        .build();

                classSubjectMappingRepository.save(mapping);
            }
        }

        return mapToDTO(savedClass);
    }


    // ============================
    // GET ALL CLASS SECTIONS
    // ============================
    public List<ClassSectionDTO> getAllClassSections() {
        return classSectionRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // ============================
    // GET CLASS SECTION
    // ============================
    public ClassSectionDTO getClassSection(String className, String section, String academicYear) {
        ClassSection found = classSectionRepository
                .findByClassNameAndAcademicYear(className, academicYear)
                .orElseThrow(() -> new RuntimeException("Class section not found!"));
        return mapToDTO(found);
    }

    // ============================
    // GET STUDENTS OF CLASS SECTION
    // ============================
    public List<StudentDTO> getStudentsByClassSection(String classSectionId) {
        List<Student> students = studentRepository.findByClassSection_ClassSectionId(classSectionId);

        if (students.isEmpty()) {
            throw new RuntimeException("No students found for class section: " + classSectionId);
        }

        return students.stream()
                .map(student -> modelMapper.map(student, StudentDTO.class))
                .toList();
    }

    // ============================
    // ASSIGN TEACHER TO CLASS SECTION
    // ============================
    public ClassSectionDTO assignTeacher(String classSectionId, String teacherId) {

        // ⭐ CHECK IF TEACHER ALREADY ASSIGNED ANYWHERE
        if (classSectionRepository.existsByClassTeacher_TeacherId(teacherId)) {
            throw new RuntimeException(
                    "Teacher " + teacherId + " is already assigned as class teacher to another class section."
            );
        }

        ClassSection classSection = classSectionRepository.findById(classSectionId)
                .orElseThrow(() -> new RuntimeException("Class section not found"));

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + teacherId));

        classSection.setClassTeacher(teacher);

        ClassSection updated = classSectionRepository.save(classSection);
        return mapToDTO(updated);
    }

    // ============================
    // UPDATE CLASS SECTION
    // ============================
    @Transactional
    public ClassSectionDTO updateClassSection(String id, ClassSectionRequest request) { // <-- 4. Change parameter

        ClassSection existing = classSectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Class section not found"));

        existing.setClassName(request.getClassName());
        existing.setSection(request.getSection());
        existing.setAcademicYear(request.getAcademicYear());
        existing.setCapacity(request.getCapacity());

        // Notice we do NOT update currentStrength here. It is handled by student assignments!

        if (request.getClassTeacherId() != null && !request.getClassTeacherId().isBlank()) {

            // ⭐ CHECK IF TEACHER ALREADY ASSIGNED TO ANOTHER CLASS
            if (classSectionRepository.existsByClassTeacher_TeacherId(request.getClassTeacherId()) &&
                    (existing.getClassTeacher() == null || !existing.getClassTeacher().getTeacherId().equals(request.getClassTeacherId()))) {
                throw new RuntimeException(
                        "Teacher " + request.getClassTeacherId() + " is already assigned as class teacher to another class section."
                );
            }

            Teacher teacher = teacherRepository.findById(request.getClassTeacherId())
                    .orElseThrow(() ->
                            new RuntimeException("Teacher not found: " + request.getClassTeacherId()));
            existing.setClassTeacher(teacher);
        } else {
            existing.setClassTeacher(null);
        }

        // UPDATE SUBJECTS
        classSubjectMappingRepository.deleteByClassSection_ClassSectionId(id);

        if (request.getSubjectIds() != null && !request.getSubjectIds().isEmpty()) {
            for (String subjectId : request.getSubjectIds()) {

                Subject subject = subjectRepository.findById(subjectId)
                        .orElseThrow(() -> new RuntimeException("Subject not found: " + subjectId));

                ClassSubjectMapping mapping = ClassSubjectMapping.builder()
                        .id(idGenerator.generateId("CSM"))
                        .classSection(existing)
                        .subject(subject)
                        .teacher(null)
                        .build();

                classSubjectMappingRepository.save(mapping);
            }
        }

        ClassSection saved = classSectionRepository.save(existing);
        return mapToDTO(saved);
    }

    // ============================
    // MAP ENTITY TO DTO
    // ============================
    private ClassSectionDTO mapToDTO(ClassSection section) {
        ClassSectionDTO dto = modelMapper.map(section, ClassSectionDTO.class);

        if (section.getClassTeacher() != null) {
            dto.setClassTeacherId(section.getClassTeacher().getTeacherId());
            dto.setClassTeacherName(section.getClassTeacher().getTeacherName());
        }

        dto.setCurrentStrength(section.getCurrentStrength());

        List<ClassSubjectMapping> mappings =
                classSubjectMappingRepository.findByClassSection_ClassSectionId(section.getClassSectionId());

        List<String> subjectIds = mappings.stream()
                .map(m -> m.getSubject().getSubjectId())
                .toList();

        dto.setSubjectIds(subjectIds);

        return dto;
    }

    // ============================
    // ASSIGN STUDENT
    // ============================
    public ClassSectionDTO assignStudentToClassSection(String classSectionId, String studentId) {

        ClassSection classSection = classSectionRepository.findById(classSectionId)
                .orElseThrow(() -> new RuntimeException("Class section not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setClassSection(classSection);
        student.setGrade(classSection.getClassName());
        student.setSection(classSection.getSection());
        studentRepository.save(student);

        int current = (classSection.getCurrentStrength() == null) ? 0 : classSection.getCurrentStrength();
        classSection.setCurrentStrength(current + 1);

        classSectionRepository.save(classSection);

        return mapToDTO(classSection);
    }

    // ============================
    // DELETE CLASS SECTION
    // ============================
    @Transactional
    public ClassSectionDTO deleteClassSection(String classSectionId) {

        ClassSection classSection = classSectionRepository.findById(classSectionId)
                .orElseThrow(() -> new RuntimeException("Class section not found"));

        classSubjectMappingRepository.deleteByClassSection_ClassSectionId(classSectionId);
        classSectionRepository.delete(classSection);

        return mapToDTO(classSection);
    }

    // ============================
    // GET UNASSIGNED STUDENTS
    // ============================
    public List<StudentDTO> getUnassignedStudentsByGrade(String grade) {
        return studentRepository.findByGradeAndClassSectionIsNull(grade)
                .stream()
                .map(student -> modelMapper.map(student, StudentDTO.class))
                .toList();
    }
}
