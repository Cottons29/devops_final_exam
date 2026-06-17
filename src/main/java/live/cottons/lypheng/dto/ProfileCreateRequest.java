package live.cottons.lypheng.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import live.cottons.lypheng.model.BarcodeType;
import live.cottons.lypheng.model.ProfileType;

import java.time.LocalDate;

public record ProfileCreateRequest(
        @NotNull ProfileType type,
        @NotBlank String fullName,
        String department,
        String title,
        String email,
        String phone,
        String bloodGroup,
        LocalDate dateOfBirth,
        LocalDate expiryDate,
        Long templateId,
        BarcodeType barcodeType
) {}
