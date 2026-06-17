package live.cottons.lypheng.dto;

import live.cottons.lypheng.model.BarcodeType;
import live.cottons.lypheng.model.Profile;
import live.cottons.lypheng.model.ProfileType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProfileResponse(
        Long id,
        String uuid,
        String registrationNumber,
        ProfileType type,
        String fullName,
        String department,
        String title,
        String email,
        String phone,
        String bloodGroup,
        LocalDate dateOfBirth,
        LocalDate issueDate,
        LocalDate expiryDate,
        String photoFileName,
        String photoContentType,
        Long templateId,
        String templateName,
        BarcodeType barcodeType,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ProfileResponse from(Profile p) {
        return new ProfileResponse(
                p.getId(),
                p.getUuid(),
                p.getRegistrationNumber(),
                p.getType(),
                p.getFullName(),
                p.getDepartment(),
                p.getTitle(),
                p.getEmail(),
                p.getPhone(),
                p.getBloodGroup(),
                p.getDateOfBirth(),
                p.getIssueDate(),
                p.getExpiryDate(),
                p.getPhotoFileName(),
                p.getPhotoContentType(),
                p.getTemplate() != null ? p.getTemplate().getId() : null,
                p.getTemplate() != null ? p.getTemplate().getName() : null,
                p.getBarcodeType(),
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }
}
