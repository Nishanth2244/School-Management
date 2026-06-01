package com.project.student.education.entity;

import com.project.student.education.repository.IdSequenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class IdGenerator {

    private final IdSequenceRepository idSequenceRepository;

    /**
     * Generate a unique ID with the current year.
     * Example: ADM2025001 or STU2025001
     *
     * @param prefix e.g., "ADM", "STU"
     * @return generated ID
     */
    public synchronized String generateId(String prefix) {
        String year = String.valueOf(LocalDate.now().getYear());
        String sequenceKey = prefix + year;

        var sequence = idSequenceRepository.findById(sequenceKey)
                .orElseGet(() -> IdSequence.builder()
                        .prefix(sequenceKey)
                        .lastNumber(0)
                        .build());


        long nextNumber = sequence.getLastNumber() + 1;
        sequence.setLastNumber(nextNumber);


        idSequenceRepository.save(sequence);
        return prefix + year + String.format("%03d", nextNumber);
    }
    
    
    public synchronized String generateIdWithoutYear(String prefix) {
        String sequenceKey = prefix;
        var sequence = idSequenceRepository.findById(sequenceKey)
                .orElseGet(() -> IdSequence.builder()
                        .prefix(sequenceKey)
                        .lastNumber(0)
                        .build());
        
        long nextNumber = sequence.getLastNumber() + 1;
        sequence.setLastNumber(nextNumber);
        idSequenceRepository.save(sequence);
        
        // Generates AS-PRI-001, AS-PRI-002, etc.
        return prefix + String.format("%03d", nextNumber); 
    }
}
