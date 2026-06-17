package live.cottons.lypheng.service;

import live.cottons.lypheng.model.ProfileType;
import live.cottons.lypheng.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;

/**
 * Generates human-friendly registration numbers in the format YEAR-DEPT-###.
 * Example: 2026-ENG-014
 */
@Service
@RequiredArgsConstructor
public class RegistrationNumberService {

    private final ProfileRepository profileRepository;

    public String generate(ProfileType type, String department) {
        int year = Year.now().getValue();
        String dept = (department != null && !department.isBlank())
                ? department.substring(0, Math.min(department.length(), 3)).toUpperCase()
                : type.name().substring(0, 3);

        String prefix = year + "-" + dept + "-";

        // Count existing registrations with this prefix to determine next sequence
        long existingCount = profileRepository.findAll().stream()
                .filter(p -> p.getRegistrationNumber() != null
                        && p.getRegistrationNumber().startsWith(prefix))
                .count();

        int sequence = (int) existingCount + 1;
        return prefix + String.format("%03d", sequence);
    }
}
