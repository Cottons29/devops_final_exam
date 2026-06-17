package live.cottons.lypheng.service;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.google.zxing.WriterException;
import live.cottons.lypheng.model.Profile;
import live.cottons.lypheng.model.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Generates ID card PDFs using iText 7.
 */
@Service
@RequiredArgsConstructor
public class IdCardPdfService {

    private final QrCodeService qrCodeService;
    private final BarcodeService barcodeService;
    private final PhotoStorageService photoStorageService;

    private static final float CARD_WIDTH = 243;   // ~86mm in points
    private static final float CARD_HEIGHT = 386;  // ~136mm in points

    /**
     * Generate a single ID card PDF for the given profile.
     */
    public byte[] generatePdf(Profile profile) throws IOException, WriterException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc, new PageSize(CARD_WIDTH, CARD_HEIGHT));
        doc.setMargins(8, 8, 8, 8);

        Template tpl = profile.getTemplate();
        DeviceRgb primary = parseColor(tpl != null ? tpl.getPrimaryColor() : "#1d4ed8");
        DeviceRgb secondary = parseColor(tpl != null ? tpl.getSecondaryColor() : "#e0e7ff");
        DeviceRgb textColor = parseColor(tpl != null ? tpl.getTextColor() : "#111827");
        String orgName = tpl != null ? tpl.getOrganizationName() : "Organization";

        try {
            // Header bar
            Table header = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
            header.setBackgroundColor(primary);
            header.addCell(createCell(orgName, ColorConstants.WHITE, 14, true, TextAlignment.CENTER));
            if (tpl != null && tpl.getTagline() != null && !tpl.getTagline().isBlank()) {
                header.addCell(createCell(tpl.getTagline(), ColorConstants.WHITE, 8, false, TextAlignment.CENTER));
            }
            doc.add(header);

            // Photo
            if (profile.hasPhoto()) {
                Path photoPath = photoStorageService.load(profile.getPhotoFileName());
                if (Files.exists(photoPath)) {
                    Image photo = new Image(ImageDataFactory.create(photoPath.toAbsolutePath().toString()));
                    photo.setFixedPosition(10, CARD_HEIGHT - 170);
                    photo.scaleToFit(80, 100);
                    doc.add(photo);
                }
            }

            // Profile info
            float infoX = 95;
            float infoY = CARD_HEIGHT - 80;

            addText(doc, profile.getFullName(), infoX, infoY, textColor, 11, true);
            addText(doc, profile.getType().name(), infoX, infoY - 14, textColor, 8, false);

            if (profile.getDepartment() != null) {
                addText(doc, "Dept: " + profile.getDepartment(), infoX, infoY - 26, textColor, 7, false);
            }
            if (profile.getTitle() != null) {
                addText(doc, profile.getTitle(), infoX, infoY - 36, textColor, 7, false);
            }

            // Registration number
            addText(doc, "Reg: " + profile.getRegistrationNumber(), 10, CARD_HEIGHT - 185, primary, 9, true);

            // QR code
            String qrContent = "https://verify.lypheng.com/profile/" + profile.getUuid();
            byte[] qrBytes = qrCodeService.generate(qrContent, 60, 60);
            Image qrImage = new Image(ImageDataFactory.create(qrBytes));
            qrImage.setFixedPosition(10, 50);
            doc.add(qrImage);

            // Barcode
            byte[] barcodeBytes = barcodeService.generate(
                    profile.getRegistrationNumber(), profile.getBarcodeType(), 150, 40);
            Image barcodeImage = new Image(ImageDataFactory.create(barcodeBytes));
            barcodeImage.setFixedPosition(75, 55);
            doc.add(barcodeImage);

            // Footer
            Table footer = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
            footer.setBackgroundColor(secondary);
            footer.addCell(createCell(
                    "Issued: " + (profile.getIssueDate() != null ? profile.getIssueDate() : "N/A")
                            + " | Exp: " + (profile.getExpiryDate() != null ? profile.getExpiryDate() : "N/A"),
                    textColor, 7, false, TextAlignment.CENTER));
            doc.add(footer);

        } finally {
            doc.close();
        }

        return out.toByteArray();
    }

    private void addText(Document doc, String text, float x, float y,
                         DeviceRgb color, float size, boolean bold) throws IOException {
        try {
            var font = bold
                    ? PdfFontFactory.createFont("Helvetica-Bold")
                    : PdfFontFactory.createFont("Helvetica");
            Paragraph p = new Paragraph(text)
                    .setFont(font)
                    .setFontSize(size)
                    .setFontColor(color)
                    .setFixedPosition(x, y, 150);
            doc.add(p);
        } catch (Exception e) {
            // Fallback — just skip the text
        }
    }

    private com.itextpdf.layout.element.Cell createCell(
            String text, com.itextpdf.kernel.colors.Color color,
            float fontSize, boolean bold, TextAlignment align) {
        try {
            var font = bold
                    ? PdfFontFactory.createFont("Helvetica-Bold")
                    : PdfFontFactory.createFont("Helvetica");
            Paragraph p = new Paragraph(text)
                    .setFont(font)
                    .setFontSize(fontSize)
                    .setFontColor(color)
                    .setTextAlignment(align);
            var cell = new com.itextpdf.layout.element.Cell().add(p);
            cell.setPadding(4);
            cell.setBorder(com.itextpdf.layout.borders.Border.NO_BORDER);
            cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
            cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
            return cell;
        } catch (Exception e) {
            var cell = new com.itextpdf.layout.element.Cell().add(new Paragraph(text).setFontSize(fontSize));
            cell.setPadding(4);
            return cell;
        }
    }

    private DeviceRgb parseColor(String hex) {
        if (hex == null || hex.length() < 7) return new DeviceRgb(29, 78, 216);
        int r = Integer.parseInt(hex.substring(1, 3), 16);
        int g = Integer.parseInt(hex.substring(3, 5), 16);
        int b = Integer.parseInt(hex.substring(5, 7), 16);
        return new DeviceRgb(r, g, b);
    }
}
