package live.cottons.lypheng.service;

import live.cottons.lypheng.model.BarcodeType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BarcodeServiceTest {

    private final BarcodeService service = new BarcodeService();

    @Test
    void generate_code128_returnsValidPng() throws Exception {
        byte[] result = service.generate("TEST123", BarcodeType.CODE_128);

        assertNotNull(result);
        assertTrue(result.length > 0);
        // PNG magic bytes
        assertEquals((byte) 0x89, result[0]);
        assertEquals((byte) 0x50, result[1]);
    }

    @Test
    void generate_ean13_returnsValidPng() throws Exception {
        // EAN-13 requires exactly 12 or 13 digits
        byte[] result = service.generate("5901234123457", BarcodeType.EAN_13);

        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    void generate_code128_withCustomSize_returnsBytes() throws Exception {
        byte[] result = service.generate("ABC", BarcodeType.CODE_128, 400, 150);

        assertNotNull(result);
        assertTrue(result.length > 0);
    }
}
