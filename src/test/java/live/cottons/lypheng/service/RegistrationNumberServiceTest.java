package live.cottons.lypheng.service;

import live.cottons.lypheng.model.Profile;
import live.cottons.lypheng.model.ProfileType;
import live.cottons.lypheng.repository.ProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationNumberServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private RegistrationNumberService service;

    @Test
    void generate_firstProfile_returns001() {
        when(profileRepository.findAll()).thenReturn(List.of());

        String regNo = service.generate(ProfileType.STUDENT, "Engineering");

        int year = Year.now().getValue();
        assertEquals(year + "-ENG-001", regNo);
    }

    @Test
    void generate_afterExistingProfile_returnsNextSequence() {
        Profile existing = Profile.builder()
                .registrationNumber(Year.now().getValue() + "-ENG-001")
                .build();
        when(profileRepository.findAll()).thenReturn(List.of(existing));

        String regNo = service.generate(ProfileType.STUDENT, "Engineering");

        assertEquals(Year.now().getValue() + "-ENG-002", regNo);
    }

    @Test
    void generate_nullDepartment_usesTypePrefix() {
        when(profileRepository.findAll()).thenReturn(List.of());

        String regNo = service.generate(ProfileType.EMPLOYEE, null);

        int year = Year.now().getValue();
        assertEquals(year + "-EMP-001", regNo);
    }

    @Test
    void generate_blankDepartment_usesTypePrefix() {
        when(profileRepository.findAll()).thenReturn(List.of());

        String regNo = service.generate(ProfileType.USER, "   ");

        int year = Year.now().getValue();
        assertEquals(year + "-USE-001", regNo);
    }
}
