package live.cottons.lypheng.service;

import live.cottons.lypheng.dto.ProfileCreateRequest;
import live.cottons.lypheng.dto.ProfileResponse;
import live.cottons.lypheng.dto.ProfileUpdateRequest;
import live.cottons.lypheng.model.BarcodeType;
import live.cottons.lypheng.model.Profile;
import live.cottons.lypheng.model.ProfileType;
import live.cottons.lypheng.model.Template;
import live.cottons.lypheng.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final RegistrationNumberService registrationNumberService;
    private final PhotoStorageService photoStorageService;
    private final TemplateService templateService;

    public ProfileResponse create(ProfileCreateRequest request) {
        Template template = null;
        if (request.templateId() != null) {
            template = templateService.getById(request.templateId());
        }

        Profile profile = Profile.builder()
                .uuid(UUID.randomUUID().toString())
                .registrationNumber(registrationNumberService.generate(
                        request.type(), request.department()))
                .type(request.type())
                .fullName(request.fullName())
                .department(request.department())
                .title(request.title())
                .email(request.email())
                .phone(request.phone())
                .bloodGroup(request.bloodGroup())
                .dateOfBirth(request.dateOfBirth())
                .expiryDate(request.expiryDate())
                .template(template)
                .barcodeType(request.barcodeType() != null ? request.barcodeType() : BarcodeType.CODE_128)
                .build();

        profile = profileRepository.save(profile);
        return ProfileResponse.from(profile);
    }

    @Transactional(readOnly = true)
    public ProfileResponse getById(Long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found with id: " + id));
        return ProfileResponse.from(profile);
    }

    @Transactional(readOnly = true)
    public Profile getEntityById(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public ProfileResponse getByUuid(String uuid) {
        Profile profile = profileRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found with uuid: " + uuid));
        return ProfileResponse.from(profile);
    }

    @Transactional(readOnly = true)
    public List<ProfileResponse> listAll() {
        return profileRepository.findAll().stream()
                .map(ProfileResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProfileResponse> search(String query) {
        return profileRepository.findByFullNameContainingIgnoreCase(query).stream()
                .map(ProfileResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProfileResponse> getByType(ProfileType type) {
        return profileRepository.findByType(type).stream()
                .map(ProfileResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProfileResponse> getByDepartment(String department) {
        return profileRepository.findByDepartment(department).stream()
                .map(ProfileResponse::from)
                .toList();
    }

    public ProfileResponse update(Long id, ProfileUpdateRequest request) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found with id: " + id));

        if (request.type() != null) profile.setType(request.type());
        if (request.fullName() != null) profile.setFullName(request.fullName());
        if (request.department() != null) profile.setDepartment(request.department());
        if (request.title() != null) profile.setTitle(request.title());
        if (request.email() != null) profile.setEmail(request.email());
        if (request.phone() != null) profile.setPhone(request.phone());
        if (request.bloodGroup() != null) profile.setBloodGroup(request.bloodGroup());
        if (request.dateOfBirth() != null) profile.setDateOfBirth(request.dateOfBirth());
        if (request.expiryDate() != null) profile.setExpiryDate(request.expiryDate());
        if (request.barcodeType() != null) profile.setBarcodeType(request.barcodeType());

        if (request.templateId() != null) {
            Template template = templateService.getById(request.templateId());
            profile.setTemplate(template);
        }

        profile = profileRepository.save(profile);
        return ProfileResponse.from(profile);
    }

    public void delete(Long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found with id: " + id));

        // Delete associated photo
        try {
            photoStorageService.delete(profile.getPhotoFileName());
        } catch (Exception e) {
            // Log and continue — photo cleanup failure shouldn't block deletion
        }

        profileRepository.deleteById(id);
    }

    public Profile save(Profile profile) {
        return profileRepository.save(profile);
    }

    /**
     * Batch-generate profiles from a list of create requests.
     */
    public List<ProfileResponse> batchCreate(List<ProfileCreateRequest> requests) {
        List<ProfileResponse> results = new ArrayList<>();
        for (ProfileCreateRequest request : requests) {
            results.add(create(request));
        }
        return results;
    }
}
