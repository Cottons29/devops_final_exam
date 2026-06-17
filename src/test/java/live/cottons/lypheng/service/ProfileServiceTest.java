package live.cottons.lypheng.service;

import live.cottons.lypheng.dto.ProfileCreateRequest;
import live.cottons.lypheng.dto.ProfileResponse;
import live.cottons.lypheng.dto.ProfileUpdateRequest;
import live.cottons.lypheng.model.BarcodeType;
import live.cottons.lypheng.model.Profile;
import live.cottons.lypheng.model.ProfileType;
import live.cottons.lypheng.repository.ProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private RegistrationNumberService registrationNumberService;

    @Mock
    private PhotoStorageService photoStorageService;

    @Mock
    private TemplateService templateService;

    @InjectMocks
    private ProfileService profileService;

    @Test
    void create_validRequest_returnsProfileResponse() {
        ProfileCreateRequest request = new ProfileCreateRequest(
                ProfileType.STUDENT, "John Doe", "Engineering",
                "BSc", "john@test.com", "123", "O+",
                null, null, null, null
        );

        when(registrationNumberService.generate(ProfileType.STUDENT, "Engineering"))
                .thenReturn("2026-ENG-001");
        when(profileRepository.save(any(Profile.class))).thenAnswer(inv -> {
            Profile p = inv.getArgument(0);
            p.setId(1L);
            return p;
        });

        ProfileResponse response = profileService.create(request);

        assertNotNull(response);
        assertEquals("John Doe", response.fullName());
        assertEquals("2026-ENG-001", response.registrationNumber());
        assertEquals(ProfileType.STUDENT, response.type());
        verify(profileRepository).save(any());
    }

    @Test
    void getById_existingId_returnsProfile() {
        Profile profile = Profile.builder()
                .id(1L).uuid("abc-123").registrationNumber("2026-ENG-001")
                .type(ProfileType.STUDENT).fullName("Jane Doe")
                .build();
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));

        ProfileResponse response = profileService.getById(1L);

        assertEquals("Jane Doe", response.fullName());
        assertEquals("abc-123", response.uuid());
    }

    @Test
    void getById_notFound_throwsException() {
        when(profileRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> profileService.getById(99L));
    }

    @Test
    void update_existingProfile_updatesFields() {
        Profile existing = Profile.builder()
                .id(1L).uuid("abc-123").registrationNumber("2026-ENG-001")
                .type(ProfileType.STUDENT).fullName("Old Name")
                .barcodeType(BarcodeType.CODE_128)
                .build();
        when(profileRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(profileRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ProfileUpdateRequest request = new ProfileUpdateRequest(
                null, "New Name", null, null, null, null, null, null, null, null, null
        );
        ProfileResponse response = profileService.update(1L, request);

        assertEquals("New Name", response.fullName());
        assertEquals("2026-ENG-001", response.registrationNumber()); // unchanged
    }

    @Test
    void delete_existingProfile_deletesAndCleansUpPhoto() {
        Profile profile = Profile.builder()
                .id(1L).photoFileName("photo.jpg")
                .fullName("Test").registrationNumber("REG-001")
                .type(ProfileType.USER)
                .build();
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));

        profileService.delete(1L);

        verify(profileRepository).deleteById(1L);
    }

    @Test
    void listAll_returnsMappedProfiles() {
        Profile p1 = Profile.builder()
                .id(1L).uuid("u1").registrationNumber("R1")
                .type(ProfileType.STUDENT).fullName("Alice")
                .build();
        Profile p2 = Profile.builder()
                .id(2L).uuid("u2").registrationNumber("R2")
                .type(ProfileType.EMPLOYEE).fullName("Bob")
                .build();
        when(profileRepository.findAll()).thenReturn(List.of(p1, p2));

        List<ProfileResponse> results = profileService.listAll();

        assertEquals(2, results.size());
        assertEquals("Alice", results.get(0).fullName());
        assertEquals("Bob", results.get(1).fullName());
    }

    @Test
    void batchCreate_multipleRequests_createsAll() {
        ProfileCreateRequest req1 = new ProfileCreateRequest(
                ProfileType.STUDENT, "Alice", "ENG", null, null, null, null, null, null, null, null);
        ProfileCreateRequest req2 = new ProfileCreateRequest(
                ProfileType.EMPLOYEE, "Bob", "HR", null, null, null, null, null, null, null, null);

        when(registrationNumberService.generate(any(), any()))
                .thenReturn("2026-ENG-001", "2026-HR-001");
        when(profileRepository.save(any(Profile.class))).thenAnswer(inv -> {
            Profile p = inv.getArgument(0);
            if (p.getId() == null) p.setId(1L);
            return p;
        });

        List<ProfileResponse> results = profileService.batchCreate(List.of(req1, req2));

        assertEquals(2, results.size());
        verify(profileRepository, times(2)).save(any());
    }
}
