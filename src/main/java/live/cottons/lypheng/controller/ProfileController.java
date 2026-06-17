package live.cottons.lypheng.controller;

import com.google.zxing.WriterException;
import live.cottons.lypheng.dto.*;
import live.cottons.lypheng.model.Profile;
import live.cottons.lypheng.model.ProfileType;
import live.cottons.lypheng.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final PhotoStorageService photoStorageService;
    private final IdCardPreviewService idCardPreviewService;
    private final IdCardPdfService idCardPdfService;
    private final QrCodeService qrCodeService;
    private final BarcodeService barcodeService;

    // ─── CRUD ───────────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<ProfileResponse> create(@RequestBody ProfileCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(profileService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<ProfileResponse>> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) ProfileType type,
            @RequestParam(required = false) String department
    ) {
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(profileService.search(search));
        }
        if (type != null) {
            return ResponseEntity.ok(profileService.getByType(type));
        }
        if (department != null && !department.isBlank()) {
            return ResponseEntity.ok(profileService.getByDepartment(department));
        }
        return ResponseEntity.ok(profileService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(profileService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileResponse> update(
            @PathVariable Long id, @RequestBody ProfileUpdateRequest request) {
        return ResponseEntity.ok(profileService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        profileService.delete(id);
    }

    // ─── Photo Upload / Serve ───────────────────────────────────────

    @PostMapping("/{id}/photo")
    public ResponseEntity<ProfileResponse> uploadPhoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        Profile profile = profileService.getEntityById(id);

        // Delete old photo if exists
        photoStorageService.delete(profile.getPhotoFileName());

        String fileName = photoStorageService.store(file, profile.getUuid());
        profile.setPhotoFileName(fileName);
        profile.setPhotoContentType(file.getContentType());

        // Save the updated profile entity
        profileService.save(profile);

        return ResponseEntity.ok(ProfileResponse.from(profile));
    }

    @GetMapping("/{id}/photo")
    public ResponseEntity<byte[]> servePhoto(@PathVariable Long id) throws IOException {
        Profile profile = profileService.getEntityById(id);
        if (!profile.hasPhoto()) {
            return ResponseEntity.notFound().build();
        }

        Path photoPath = photoStorageService.load(profile.getPhotoFileName());
        if (!Files.exists(photoPath)) {
            return ResponseEntity.notFound().build();
        }

        byte[] data = Files.readAllBytes(photoPath);
        MediaType mediaType = profile.getPhotoContentType() != null
                ? MediaType.parseMediaType(profile.getPhotoContentType())
                : MediaType.IMAGE_JPEG;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(data);
    }

    // ─── Live Preview ──────────────────────────────────────────────

    @GetMapping("/{id}/preview")
    public ResponseEntity<String> preview(@PathVariable Long id) {
        Profile profile = profileService.getEntityById(id);
        String html = idCardPreviewService.generatePreviewHtml(profile);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }

    // ─── PDF Export ─────────────────────────────────────────────────

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> exportPdf(@PathVariable Long id)
            throws IOException, WriterException {
        Profile profile = profileService.getEntityById(id);
        byte[] pdf = idCardPdfService.generatePdf(profile);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"id-card-" + profile.getRegistrationNumber() + ".pdf\"")
                .body(pdf);
    }

    // ─── QR Code ────────────────────────────────────────────────────

    @GetMapping("/{id}/qrcode")
    public ResponseEntity<byte[]> qrCode(@PathVariable Long id)
            throws IOException, WriterException {
        Profile profile = profileService.getEntityById(id);
        String content = "https://verify.lypheng.com/profile/" + profile.getUuid();
        byte[] image = qrCodeService.generate(content);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image);
    }

    // ─── Barcode ────────────────────────────────────────────────────

    @GetMapping("/{id}/barcode")
    public ResponseEntity<byte[]> barcode(@PathVariable Long id)
            throws IOException, WriterException {
        Profile profile = profileService.getEntityById(id);
        byte[] image = barcodeService.generate(
                profile.getRegistrationNumber(), profile.getBarcodeType());

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image);
    }

    // ─── Batch Generate ─────────────────────────────────────────────

    @PostMapping("/batch")
    public ResponseEntity<List<ProfileResponse>> batchCreate(
            @RequestBody BatchGenerateRequest request) {
        List<ProfileResponse> results = profileService.batchCreate(request.profiles());
        return ResponseEntity.status(HttpStatus.CREATED).body(results);
    }
}
