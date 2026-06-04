package com.project.student.education.service;

import com.project.student.education.DTO.*;
import com.project.student.education.ExceptionHandling.BadRequestException;
import com.project.student.education.ExceptionHandling.ConflictException;
import com.project.student.education.ExceptionHandling.ResourceNotFoundException;
import com.project.student.education.entity.*;
import com.project.student.education.enums.Role;
import com.project.student.education.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherService {

        private final TeacherRepository teacherRepository;
        private final UserRepository userRepository;
        private final IdGenerator idGenerator;
        private final PasswordEncoder passwordEncoder;
        private final ModelMapper modelMapper;
        private final ClassSectionRepository classSectionRepository;
        private final TimetableRepository timetableRepository;
        private final SubjectRepository subjectRepository;
        private final JavaMailSender mailSender;
        private final TeacherRegistrationTokenRepository registrationTokenRepository;
        private final TeacherAttendanceRepository teacherAttendanceRepository;

        private final ClassSubjectMappingRepository classSubjectMappingRepository;

        public TeacherDTO addTeacher(TeacherDTO dto) {
                if (teacherRepository.existsByEmail(dto.getEmail())) {
                        throw new ConflictException("Email already exists");
                }

                String teacherId = idGenerator.generateId("TCH");
                String rawPassword = generateDefaultPassword(teacherId);

                User user = User.builder()
                                .username(teacherId)
                                .password(passwordEncoder.encode(rawPassword))
                                .role(Role.TEACHER)
                                .email(dto.getEmail())
                                .build();

                userRepository.save(user);

                Teacher teacher = Teacher.builder()
                                .teacherId(teacherId)
                                .teacherName(dto.getTeacherName())
                                .email(dto.getEmail())
                                .phone(dto.getPhone())
                                .qualification(dto.getQualification())
                                .gender(dto.getGender())
                                .experience(dto.getExperience())
                                .address(dto.getAddress())
                                .user(user)
                                .build();

                if (dto.getSubjectIds() != null && !dto.getSubjectIds().isEmpty()) {
                        teacher.setSubjectIds(dto.getSubjectIds());
                }

                teacherRepository.save(teacher);

                TeacherDTO response = modelMapper.map(teacher, TeacherDTO.class);
                response.setPassword(rawPassword);
                response.setSubjectIds(teacher.getSubjectIds());
                if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
                        sendTeacherWelcomeEmail(dto.getEmail(), teacherId, rawPassword);
                }

                return response;
        }

        public List<TeacherDTO> getAllTeachers() {
                return teacherRepository.findAll()
                                .stream()
                                .map(t -> modelMapper.map(t, TeacherDTO.class))
                                .collect(Collectors.toList());
        }

        public TeacherDTO getTeacherById(String teacherId) {
                Teacher teacher = teacherRepository.findById(teacherId)
                                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
                return modelMapper.map(teacher, TeacherDTO.class);
        }

        public TeacherDTO updateTeacher(String teacherId, TeacherDTO dto) {
                Teacher teacher = teacherRepository.findById(teacherId)
                                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
                User user = teacher.getUser();

                String oldEmail = teacher.getEmail();
                String newEmail = dto.getEmail();
                boolean emailChanged = newEmail != null && !newEmail.equalsIgnoreCase(oldEmail);

                teacher.setTeacherName(dto.getTeacherName());
                teacher.setEmail(dto.getEmail());
                teacher.setPhone(dto.getPhone());
                teacher.setQualification(dto.getQualification());
                teacher.setGender(dto.getGender());
                teacher.setExperience(dto.getExperience());
                teacher.setAddress(dto.getAddress());

                if (dto.getSubjectIds() != null) {
                        teacher.setSubjectIds(dto.getSubjectIds());
                }
                if (emailChanged) {
                        user.setEmail(newEmail); // THIS IS WHAT WAS MISSING ❗❗
                        userRepository.save(user);
                }

                teacherRepository.save(teacher);

                TeacherDTO response = modelMapper.map(teacher, TeacherDTO.class);
                response.setSubjectIds(teacher.getSubjectIds());

                return response;
        }

        public String deleteTeacher(String teacherId) {
                Teacher teacher = teacherRepository.findById(teacherId)
                                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

                teacherRepository.delete(teacher);
                userRepository.delete(teacher.getUser());

                return "Teacher deleted successfully";
        }

        private String generateDefaultPassword(String teacherId) {
                return "Tch@" + teacherId.substring(teacherId.length() - 4);
        }

        public String assignTeacher(String teacherId, String classSectionId) {
                ClassSection classSection = classSectionRepository.findById(classSectionId)
                                .orElseThrow(() -> new ResourceNotFoundException("Class section not found"));
                Teacher teacher = teacherRepository.findById(teacherId)
                                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
                classSection.setClassTeacher(teacher);
                classSectionRepository.save(classSection);
                return "Teacher  " + teacherId + "  assigned to class  " + classSectionId + "  successfully";
        }

        public String updateClassTeacher(String classSectionId, String teacherId) {
                ClassSection section = classSectionRepository.findById(classSectionId)
                                .orElseThrow(() -> new ResourceNotFoundException("Class section not found"));

                Teacher teacher = teacherRepository.findById(teacherId)
                                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

                section.setClassTeacher(teacher);
                classSectionRepository.save(section);

                return "Class teacher updated for class section " + classSectionId +
                                " to teacher " + teacherId;
        }

        // 🔥 UPDATED LOGIC: Get Classes for BOTH Class Teacher & Subject Teacher
        public List<ClassSectionMiniDTO> getClassesHandledByTeacher(String teacherId) {

                if (!teacherRepository.existsById(teacherId)) {
                        throw new ResourceNotFoundException("Teacher not found: " + teacherId);
                }

                // 1. Classes where they are the MAIN CLASS TEACHER
                List<ClassSection> asClassTeacher = classSectionRepository
                                .findByClassTeacher_TeacherId(teacherId);

                // 2. Classes where they are assigned as a SUBJECT TEACHER
                // (Requires ClassSubjectMappingRepository)
                List<ClassSubjectMapping> subjectMappings = classSubjectMappingRepository
                                .findByTeacher_TeacherId(teacherId);

                List<ClassSection> asSubjectTeacher = subjectMappings.stream()
                                .map(ClassSubjectMapping::getClassSection)
                                .toList();

                Set<ClassSection> uniqueClasses = new HashSet<>(asClassTeacher);
                uniqueClasses.addAll(asSubjectTeacher);

                // 4. Convert to DTO
                return uniqueClasses.stream()
                                .sorted(Comparator.comparing(ClassSection::getClassName)) // Optional: Sort
                                .map(sec -> ClassSectionMiniDTO.builder()
                                                .classSectionId(sec.getClassSectionId())
                                                .className(sec.getClassName())
                                                .sectionName(sec.getSection())
                                                .academicYear(sec.getAcademicYear())
                                                .build())
                                .toList();
        }

        public Long getTeacherCount() {
                return teacherRepository.countTeachers();
        }

        public TeacherWeeklyTimetableDTO getTeacherWeeklyTimetable(String teacherId, LocalDate weekReference) {
                Teacher teacher = teacherRepository.findById(teacherId)
                                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

                List<Timetable> list = timetableRepository
                                .findByTeacher_TeacherIdOrderByDayAscStartTimeAsc(teacherId);

                Map<String, LocalDate> weekDates = getWeekDates(weekReference);

                Map<String, List<Timetable>> grouped = list.stream()
                                .collect(Collectors.groupingBy(Timetable::getDay, LinkedHashMap::new,
                                                Collectors.toList()));

                List<TeacherWeeklyTimetableDTO.DayEntry> weekly = new ArrayList<>();

                for (String dayName : List.of(
                                "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY",
                                "FRIDAY", "SATURDAY")) {
                        LocalDate date = weekDates.get(dayName);
                        List<Timetable> periods = grouped.getOrDefault(dayName, Collections.emptyList());

                        List<TeacherWeeklyTimetableDTO.Period> periodDTOs = periods.stream()
                                        .sorted(Comparator.comparing(Timetable::getStartTime))
                                        .map(t -> TeacherWeeklyTimetableDTO.Period.builder()
                                                        .date(date.toString())
                                                        .classSectionId(t.getClassSection().getClassSectionId())
                                                        .className(t.getClassSection().getClassName())
                                                        .section(t.getClassSection().getSection())
                                                        .subjectId(t.getSubject().getSubjectId())
                                                        .subjectName(t.getSubject().getSubjectName())
                                                        .startTime(toAmPm(t.getStartTime()))
                                                        .endTime(toAmPm(t.getEndTime()))
                                                        .build())
                                        .toList();

                        weekly.add(
                                        TeacherWeeklyTimetableDTO.DayEntry.builder()
                                                        .day(dayName)
                                                        .date(date.toString())
                                                        .periods(periodDTOs)
                                                        .build());
                }

                return TeacherWeeklyTimetableDTO.builder()
                                .teacherId(teacher.getTeacherId())
                                .teacherName(teacher.getTeacherName())
                                .weeklyTimetable(weekly)
                                .build();
        }

        private Map<String, LocalDate> getWeekDates(LocalDate referenceDate) {
                LocalDate monday = referenceDate.with(java.time.DayOfWeek.MONDAY);
                Map<String, LocalDate> map = new LinkedHashMap<>();
                map.put("MONDAY", monday);
                map.put("TUESDAY", monday.plusDays(1));
                map.put("WEDNESDAY", monday.plusDays(2));
                map.put("THURSDAY", monday.plusDays(3));
                map.put("FRIDAY", monday.plusDays(4));
                map.put("SATURDAY", monday.plusDays(5));
                map.put("SUNDAY", monday.plusDays(6));
                return map;
        }

        private String toAmPm(String time) {
                return LocalTime.parse(time)
                                .format(DateTimeFormatter.ofPattern("hh:mm a"));
        }

        public TeacherWeeklyTimetableDTO getClassTeacherTimetable(String teacherId, LocalDate weekStart) {
                Teacher teacher = teacherRepository.findById(teacherId)
                                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

                List<ClassSection> sections = classSectionRepository.findByClassTeacher_TeacherId(teacherId);

                if (sections == null || sections.isEmpty()) {
                        throw new BadRequestException("This teacher is not a class teacher");
                }

                ClassSection section = sections.get(0);

                List<Timetable> timetable = timetableRepository
                                .findByClassSection_ClassSectionIdOrderByDayAscStartTimeAsc(
                                                section.getClassSectionId());

                Map<String, LocalDate> weekDates = getWeekDates(weekStart);
                List<TeacherWeeklyTimetableDTO.DayEntry> weekly = new ArrayList<>();

                for (String day : List.of("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY")) {
                        LocalDate date = weekDates.get(day);
                        List<Timetable> periods = timetable.stream()
                                        .filter(t -> t.getDay().equals(day))
                                        .toList();

                        List<TeacherWeeklyTimetableDTO.Period> periodList = periods.stream()
                                        .map(t -> TeacherWeeklyTimetableDTO.Period.builder()
                                                        .date(date.toString())
                                                        .classSectionId(section.getClassSectionId())
                                                        .className(section.getClassName())
                                                        .section(section.getSection())
                                                        .subjectId(t.getSubject().getSubjectId())
                                                        .subjectName(t.getSubject().getSubjectName())
                                                        .startTime(toAmPm(t.getStartTime()))
                                                        .endTime(toAmPm(t.getEndTime()))
                                                        .build())
                                        .toList();

                        weekly.add(
                                        TeacherWeeklyTimetableDTO.DayEntry.builder()
                                                        .day(day)
                                                        .date(date.toString())
                                                        .periods(periodList)
                                                        .build());
                }

                return TeacherWeeklyTimetableDTO.builder()
                                .teacherId(teacherId)
                                .teacherName(teacher.getTeacherName())
                                .weeklyTimetable(weekly)
                                .build();
        }

        private void sendTeacherWelcomeEmail(String email, String teacherId, String rawPassword) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(email);
                message.setSubject("Teacher Account Created - Login Credentials");
                message.setText(
                                "Dear Teacher,\n\n"
                                                + "Your teacher account has been created successfully.\n\n"
                                                + "Here are your login credentials:\n"
                                                + "Teacher ID: " + teacherId + "\n"
                                                + "Password: " + rawPassword + "\n\n"
                                                + "Please log in and change your password immediately.\n\n"
                                                + "Regards,\n"
                                                + "School Administration");

                mailSender.send(message);
        }

        public void sendRegistrationLink(List<String> emails) {

                // 1. Upfront Validation: Check if ANY emails are already registered
                List<String> alreadyRegistered = new ArrayList<>();
                for (String email : emails) {
                        // Check both teacher and user tables to be safe
                        if (teacherRepository.existsByEmail(email) || userRepository.existsByEmail(email)) {
                                alreadyRegistered.add(email);
                        }
                }

                // If any emails exist, throw an exception immediately BEFORE sending any emails
                if (!alreadyRegistered.isEmpty()) {
                        throw new IllegalArgumentException("Teachers with the following emails are already registered: "
                                + String.join(", ", alreadyRegistered));
                }

                // 2. Process and send emails
                List<String> failedEmails = new ArrayList<>();

                for (String email : emails) {
                        try {
                                String token = UUID.randomUUID().toString();

                                TeacherRegistrationToken registrationToken = TeacherRegistrationToken.builder()
                                        .email(email)
                                        .token(token)
                                        .expiryTime(LocalDateTime.now().plusDays(2))
                                        .used(false)
                                        .build();

                                registrationTokenRepository.save(registrationToken);

                                String registrationLink = "http://localhost:8081/modal?token="
                                        + token + "&role=teacher";

                                SimpleMailMessage message = new SimpleMailMessage();
                                message.setTo(email);
                                message.setSubject("Teacher Registration");
                                message.setText(
                                        "Welcome to School ERP\n\n" +
                                                "Please complete your registration using the link below:\n\n" +
                                                registrationLink +
                                                "\n\nThis link expires in 48 hours."
                                );

                                mailSender.send(message);

                        } catch (Exception e) {
                                log.error("Error sending email to {}: {}", email, e.getMessage());
                                failedEmails.add(email);
                        }
                }

                // 3. Handle actual mail sending failures (e.g., SMTP issues)
                if (!failedEmails.isEmpty()) {
                        throw new ConflictException("Failed to send emails to: " + String.join(", ", failedEmails));
                }
        }

        public TeacherDTO registerTeacher(
                        String token,
                        TeacherRegistrationDTO dto) {

                TeacherRegistrationToken tokenEntity = registrationTokenRepository
                                .findByToken(token)
                                .orElseThrow(() -> new BadRequestException("Invalid token"));

                if (tokenEntity.isUsed()) {
                        throw new ConflictException(
                                        "Registration already completed");
                }

                String email = tokenEntity.getEmail();

                String teacherId = idGenerator.generateId("TCH");

                String rawPassword = generateRandomPassword();

                User user = User.builder()
                                .username(teacherId)
                                .email(email)
                                .password(
                                                passwordEncoder.encode(rawPassword))
                                .role(Role.TEACHER)
                                .build();

                userRepository.save(user);

                Teacher teacher = Teacher.builder()
                                .teacherId(teacherId)
                                .teacherName(dto.getTeacherName())
                                .email(email)
                                .phone(dto.getPhone())
                                .qualification(dto.getQualification())
                                .gender(dto.getGender())
                                .experience(dto.getExperience())
                                .address(dto.getAddress())
                                .subjectIds(dto.getSubjectIds())
                                .user(user)
                                .build();

                teacherRepository.save(teacher);

                sendCredentialsEmail(
                                email,
                                teacherId,
                                rawPassword);

                tokenEntity.setUsed(true);

                registrationTokenRepository.save(tokenEntity);

                return modelMapper.map(
                                teacher,
                                TeacherDTO.class);
        }

        private void sendCredentialsEmail(
                        String email,
                        String username,
                        String password) {

                SimpleMailMessage message = new SimpleMailMessage();

                message.setTo(email);

                message.setSubject(
                                "Teacher Account Created");

                message.setText(
                                "Your account has been created.\n\n" +
                                                "Username : " + username + "\n" +
                                                "Password : " + password + "\n\n" +
                                                "Please login and change password.");

                mailSender.send(message);
        }

        private String generateRandomPassword() {

                return UUID.randomUUID()
                                .toString()
                                .replace("-", "")
                                .substring(0, 10);
        }
        // =========================================================================
        // GET CLASSES AND ASSOCIATED SUBJECTS FOR A SPECIFIC TEACHER ID
        // =========================================================================
        public Map<String, Object> getTeacherClassesWithSubjects(String teacherId) {
                Teacher teacher = teacherRepository.findById(teacherId)
                        .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + teacherId));

                // 1. Fetch all subject mapping assignments for this teacher
                List<ClassSubjectMapping> subjectMappings = classSubjectMappingRepository
                        .findByTeacher_TeacherId(teacherId);

                // 2. Fetch classes where they are designated as the main Class Teacher
                List<ClassSection> asClassTeacher = classSectionRepository
                        .findByClassTeacher_TeacherId(teacherId);

                // 3. Merge both groups into a unique Set of ClassSections
                Set<ClassSection> allSections = new HashSet<>(asClassTeacher);
                subjectMappings.forEach(mapping -> allSections.add(mapping.getClassSection()));

                // 4. Construct the structured array elements
                List<Map<String, Object>> classList = allSections.stream()
                        .map(section -> {
                                Map<String, Object> cMap = new LinkedHashMap<>();
                                cMap.put("classSectionId", section.getClassSectionId());
                                cMap.put("className", section.getClassName());
                                cMap.put("section", section.getSection());
                                cMap.put("academicYear", section.getAcademicYear());

                                // Mark if they are the primary class manager for this room
                                cMap.put("isPrimaryClassTeacher", asClassTeacher.contains(section));

                                // Filter out the subjects THIS teacher teaches in THIS specific class section
                                List<Map<String, String>> subjectsList = subjectMappings.stream()
                                        .filter(m -> m.getClassSection().getClassSectionId().equals(section.getClassSectionId()))
                                        .map(m -> {
                                                Map<String, String> sMap = new LinkedHashMap<>();
                                                sMap.put("subjectId", m.getSubject().getSubjectId());
                                                sMap.put("subjectName", m.getSubject().getSubjectName());
                                                sMap.put("subjectCode", m.getSubject().getSubjectCode());
                                                return sMap;
                                        })
                                        .toList();

                                cMap.put("subjectsTaught", subjectsList);
                                return cMap;
                        })
                        .toList();

                // 5. Wrap inside final root summary object
                Map<String, Object> response = new LinkedHashMap<>();
                response.put("teacherId", teacher.getTeacherId());
                response.put("teacherName", teacher.getTeacherName());
                response.put("classes", classList);

                return response;
        }

    @Transactional
    public String markAttendance(
            MarkTeacherAttendanceRequest request) {

        Teacher teacher = teacherRepository.findById(
                        request.getTeacherId())
                .orElseThrow(() ->
                        new RuntimeException("Teacher not found"));

        LocalDate today = LocalDate.now();

        if (teacherAttendanceRepository
                .findByTeacher_TeacherIdAndAttendanceDate(
                        teacher.getTeacherId(),
                        today)
                .isPresent()) {

            throw new RuntimeException(
                    "Attendance already marked");
        }

        TeacherAttendance attendance =
                new TeacherAttendance();

        attendance.setTeacher(teacher);
        attendance.setAttendanceDate(today);
        attendance.setStatus(request.getStatus());
        attendance.setRemarks(request.getRemarks());

        teacherAttendanceRepository.save(attendance);

        return "Attendance marked successfully";
    }

    public List<TeacherAttendanceResponseDTO> getAttendance(
            String teacherId) {

        return teacherAttendanceRepository
                .findByTeacher_TeacherIdOrderByAttendanceDateDesc(
                        teacherId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }
    private TeacherAttendanceResponseDTO convertToDTO(
            TeacherAttendance attendance) {

        TeacherAttendanceResponseDTO dto =
                new TeacherAttendanceResponseDTO();

        dto.setId(attendance.getId());
        dto.setTeacherId(
                attendance.getTeacher().getTeacherId());

        dto.setTeacherName(
                attendance.getTeacher().getTeacherName());

        dto.setAttendanceDate(
                attendance.getAttendanceDate());

        dto.setStatus(
                attendance.getStatus().name());

        dto.setRemarks(
                attendance.getRemarks());

        return dto;
    }
}