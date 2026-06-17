package live.cottons.lypheng.dto;

import live.cottons.lypheng.model.BarcodeType;
import live.cottons.lypheng.model.ProfileType;

import java.time.LocalDate;

public record ProfileUpdateRequest(
        ProfileType type,
        String fullName,
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
