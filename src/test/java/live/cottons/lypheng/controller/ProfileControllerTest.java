package live.cottons.lypheng.controller;

import live.cottons.lypheng.dto.ProfileCreateRequest;
import live.cottons.lypheng.dto.ProfileResponse;
import live.cottons.lypheng.model.ProfileType;
import live.cottons.lypheng.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private ProfileService profileService;

    @Mock
    private PhotoStorageService photoStorageService;

    @Mock
    private IdCardPreviewService idCardPreviewService;

    @Mock
    private IdCardPdfService idCardPdfService;

    @Mock
    private QrCodeService qrCodeService;

    @Mock
    private BarcodeService barcodeService;

    @InjectMocks
    private ProfileController profileController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(profileController)
                .setControllerAdvice(new live.cottons.lypheng.exception.GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    private ProfileResponse sampleResponse() {
        return new ProfileResponse(
                1L, "uuid-123", "2026-ENG-001", ProfileType.STUDENT,
                "John Doe", "Engineering", "BSc",
                "john@test.com", "123", "O+",
                LocalDate.of(2000, 1, 1),
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2027, 1, 1),
                null, null, null, null, null,
                LocalDateTime.now(), LocalDateTime.now()
        );
    }

    @Test
    void create_validRequest_returns201() throws Exception {
        ProfileCreateRequest request = new ProfileCreateRequest(
                ProfileType.STUDENT, "John Doe", "Engineering",
                "BSc", "john@test.com", "123", "O+",
                null, null, null, null
        );

        when(profileService.create(any())).thenReturn(sampleResponse());

        mockMvc.perform(post("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("John Doe"))
                .andExpect(jsonPath("$.registrationNumber").value("2026-ENG-001"))
                .andExpect(jsonPath("$.type").value("STUDENT"));
    }

    @Test
    void listAll_returnsProfiles() throws Exception {
        when(profileService.listAll()).thenReturn(List.of(sampleResponse()));

        mockMvc.perform(get("/api/profiles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].fullName").value("John Doe"));
    }

    @Test
    void getById_existingId_returnsProfile() throws Exception {
        when(profileService.getById(1L)).thenReturn(sampleResponse());

        mockMvc.perform(get("/api/profiles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("John Doe"));
    }

    @Test
    void getById_notFound_returns404() throws Exception {
        when(profileService.getById(99L))
                .thenThrow(new IllegalArgumentException("Profile not found"));

        mockMvc.perform(get("/api/profiles/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void search_byName_returnsResults() throws Exception {
        when(profileService.search("John")).thenReturn(List.of(sampleResponse()));

        mockMvc.perform(get("/api/profiles").param("search", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void preview_returnsHtml() throws Exception {
        when(profileService.getEntityById(1L)).thenReturn(
                live.cottons.lypheng.model.Profile.builder()
                        .id(1L).uuid("uuid-123").registrationNumber("2026-ENG-001")
                        .type(ProfileType.STUDENT).fullName("John Doe")
                        .build());
        when(idCardPreviewService.generatePreviewHtml(any()))
                .thenReturn("<div>Preview</div>");

        mockMvc.perform(get("/api/profiles/1/preview"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html"));
    }

    @Test
    void delete_existingProfile_returns204() throws Exception {
        mockMvc.perform(delete("/api/profiles/1"))
                .andExpect(status().isNoContent());
    }
}
