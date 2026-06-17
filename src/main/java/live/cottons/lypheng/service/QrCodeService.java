package live.cottons.lypheng.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Generates QR codes using ZXing.
 */
@Service
public class QrCodeService {

    private static final int DEFAULT_SIZE = 200;

    /**
     * Generate a QR code PNG as byte array.
     *
     * @param content the text/URL to encode
     * @param width   pixel width
     * @param height  pixel height
     * @return PNG bytes
     */
    public byte[] generate(String content, int width, int height) throws WriterException, IOException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", out);
        return out.toByteArray();
    }

    /**
     * Generate a QR code with default size (200×200).
     */
    public byte[] generate(String content) throws WriterException, IOException {
        return generate(content, DEFAULT_SIZE, DEFAULT_SIZE);
    }
}
