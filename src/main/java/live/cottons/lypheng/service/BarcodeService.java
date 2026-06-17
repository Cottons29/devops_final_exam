package live.cottons.lypheng.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.oned.EAN13Writer;
import live.cottons.lypheng.model.BarcodeType;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Generates linear barcodes (Code-128, EAN-13) using ZXing.
 */
@Service
public class BarcodeService {

    /**
     * Generate a barcode PNG as byte array.
     *
     * @param content the data to encode
     * @param type    barcode symbology
     * @param width   pixel width
     * @param height  pixel height
     * @return PNG bytes
     */
    public byte[] generate(String content, BarcodeType type, int width, int height)
            throws WriterException, IOException {

        BitMatrix matrix = switch (type) {
            case CODE_128 -> new Code128Writer().encode(content, BarcodeFormat.CODE_128, width, height);
            case EAN_13 -> new EAN13Writer().encode(content, BarcodeFormat.EAN_13, width, height);
        };

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", out);
        return out.toByteArray();
    }

    /**
     * Generate a barcode with default dimensions (300×100).
     */
    public byte[] generate(String content, BarcodeType type) throws WriterException, IOException {
        return generate(content, type, 300, 100);
    }
}
