package live.cottons.lypheng.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

/**
 * Handles photo upload, validation, storage, and retrieval on local filesystem.
 */
@Service
public class PhotoStorageService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg", "image/png"
    );

    private final Path photoDir;
    private final long maxFileSizeBytes;

    public PhotoStorageService(
            @Value("${app.photo.dir:./uploads/photos}") String photoDir,
            @Value("${app.photo.max-size-mb:5}") int maxSizeMb
    ) throws IOException {
        this.photoDir = Paths.get(photoDir).toAbsolutePath().normalize();
        this.maxFileSizeBytes = maxSizeMb * 1024L * 1024L;
        Files.createDirectories(this.photoDir);
    }

    /**
     * Validates and stores the uploaded photo. Returns the generated file name.
     */
    public String store(MultipartFile file, String profileUuid) throws IOException {
        validate(file);

        String ext = resolveExtension(file.getContentType());
        String fileName = profileUuid + "_" + UUID.randomUUID() + ext;

        Path target = photoDir.resolve(fileName);
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }

        return fileName;
    }

    /**
     * Returns the Path to a stored photo file.
     */
    public Path load(String fileName) {
        return photoDir.resolve(fileName).normalize();
    }

    /**
     * Deletes a photo file if it exists.
     */
    public void delete(String fileName) throws IOException {
        if (fileName == null || fileName.isBlank()) return;
        Path file = photoDir.resolve(fileName).normalize();
        Files.deleteIfExists(file);
    }

    private void validate(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }
        if (file.getSize() > maxFileSizeBytes) {
            throw new IllegalArgumentException(
                    "File size exceeds maximum of " + (maxFileSizeBytes / 1024 / 1024) + "MB");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException(
                    "Invalid file type. Only JPEG and PNG are allowed.");
        }
    }

    private String resolveExtension(String contentType) {
        if ("image/png".equals(contentType)) return ".png";
        return ".jpg";
    }
}
