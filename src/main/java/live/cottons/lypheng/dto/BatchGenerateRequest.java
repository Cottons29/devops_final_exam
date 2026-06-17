package live.cottons.lypheng.dto;

import jakarta.validation.constraints.NotEmpty;
import live.cottons.lypheng.model.ProfileType;

import java.util.List;

/**
 * Request to batch-generate ID cards.
 * Each item in {@code profiles} is a simplified profile data map.
 */
public record BatchGenerateRequest(
        ProfileType defaultType,
        String defaultDepartment,
        Long templateId,
        @NotEmpty List<ProfileCreateRequest> profiles
) {}
