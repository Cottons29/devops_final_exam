package live.cottons.lypheng.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QrCodeServiceTest {

    private final QrCodeService service = new QrCodeService();

    @Test
    void generate_returnsNonNullBytes() throws Exception {
        byte[] result = service.generate("https://example.com");

        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    void generate_withCustomSize_returnsValidPng() throws Exception {
        byte[] result = service.generate("test-content", 300, 300);

        assertNotNull(result);
        // PNG magic bytes: 0x89 0x50 0x4E 0x47
        assertTrue(result.length > 8);
        assertEquals((byte) 0x89, result[0]);
        assertEquals((byte) 0x50, result[1]); // 'P'
        assertEquals((byte) 0x4E, result[2]); // 'N'
        assertEquals((byte) 0x47, result[3]); // 'G'
    }
}
