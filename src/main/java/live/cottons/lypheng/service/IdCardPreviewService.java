package live.cottons.lypheng.service;

import live.cottons.lypheng.model.Profile;
import live.cottons.lypheng.model.Template;
import org.springframework.stereotype.Service;

/**
 * Generates an HTML preview of the ID card using inline styles.
 * This gives the user an instant live preview without needing PDF rendering.
 */
@Service
public class IdCardPreviewService {

    /**
     * Generate an HTML string for the ID card preview.
     */
    public String generatePreviewHtml(Profile profile) {
        Template tpl = profile.getTemplate();
        String primary = tpl != null ? tpl.getPrimaryColor() : "#1d4ed8";
        String secondary = tpl != null ? tpl.getSecondaryColor() : "#e0e7ff";
        String text = tpl != null ? tpl.getTextColor() : "#111827";
        String org = tpl != null ? tpl.getOrganizationName() : "Organization";
        String tagline = tpl != null ? tpl.getTagline() : "";
        String layout = tpl != null ? tpl.getLayout() : "VERTICAL";

        boolean isVertical = "VERTICAL".equalsIgnoreCase(layout);

        String photoSrc = profile.hasPhoto()
                ? "/api/profiles/" + profile.getId() + "/photo"
                : "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxMDAiIGhlaWdodD0iMTAwIiB2aWV3Qm94PSIwIDAgMTAwIDEwMCI+PHJlY3Qgd2lkdGg9IjEwMCIgaGVpZ2h0PSIxMDAiIGZpbGw9IiNlNWU3ZWIiLz48dGV4dCB4PSI1MCIgeT0iNTUiIHRleHQtYW5jaG9yPSJtaWRkbGUiIGZpbGw9IiM5Y2EzYWYiIGZvbnQtc2l6ZT0iMTQiPk5vIFBob3RvPC90ZXh0Pjwvc3ZnPg==";

        int cardWidth = isVertical ? 340 : 540;
        int cardHeight = isVertical ? 540 : 340;

        return """
                <div style="width:%dpx;height:%dpx;border-radius:16px;overflow:hidden;
                    font-family:'Segoe UI',sans-serif;box-shadow:0 4px 24px rgba(0,0,0,0.15);
                    background:%s;color:%s;display:%s;">
                    <!-- Header -->
                    <div style="background:%s;color:white;padding:16px 20px;text-align:center;
                        %s">
                        <div style="font-size:18px;font-weight:700;">%s</div>
                        %s
                    </div>
                    <!-- Photo + Info -->
                    <div style="display:flex;%s padding:20px;gap:16px;flex:1;align-items:center;">
                        <img src="%s" alt="Photo"
                            style="width:100px;height:120px;object-fit:cover;border-radius:8px;
                            border:2px solid %s;"/>
                        <div style="flex:1;">
                            <div style="font-size:16px;font-weight:700;margin-bottom:4px;">%s</div>
                            <div style="font-size:12px;color:%s;margin-bottom:2px;">%s</div>
                            <div style="font-size:12px;margin-bottom:2px;">Reg: <b>%s</b></div>
                            %s%s%s%s
                        </div>
                    </div>
                    <!-- Footer -->
                    <div style="background:%s;padding:8px 20px;text-align:center;
                        font-size:10px;color:white;">
                        ID: %s | Issued: %s | Expires: %s
                    </div>
                </div>
                """.formatted(
                cardWidth, cardHeight, secondary, text,
                isVertical ? "flex;flex-direction:column" : "flex;flex-direction:row",
                primary,
                isVertical ? "" : "width:120px;writing-mode:vertical-lr;text-align:center;",
                org,
                tagline != null && !tagline.isBlank()
                        ? "<div style=\"font-size:11px;opacity:0.85;margin-top:2px;\">%s</div>".formatted(tagline)
                        : "",
                isVertical ? "flex-direction:column;align-items:center;" : "",
                photoSrc, primary,
                escape(profile.getFullName()),
                primary,
                profile.getType() != null ? profile.getType().name() : "",
                profile.getRegistrationNumber() != null ? profile.getRegistrationNumber() : "",
                profile.getDepartment() != null
                        ? "<div style=\"font-size:11px;\">Dept: %s</div>".formatted(escape(profile.getDepartment()))
                        : "",
                profile.getTitle() != null
                        ? "<div style=\"font-size:11px;\">%s</div>".formatted(escape(profile.getTitle()))
                        : "",
                profile.getEmail() != null
                        ? "<div style=\"font-size:11px;\">%s</div>".formatted(escape(profile.getEmail()))
                        : "",
                profile.getPhone() != null
                        ? "<div style=\"font-size:11px;\">Ph: %s</div>".formatted(escape(profile.getPhone()))
                        : "",
                primary,
                profile.getUuid() != null ? profile.getUuid() : "",
                profile.getIssueDate() != null ? profile.getIssueDate().toString() : "N/A",
                profile.getExpiryDate() != null ? profile.getExpiryDate().toString() : "N/A"
        );
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
