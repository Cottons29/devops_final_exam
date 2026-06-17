package live.cottons.lypheng.service;

import live.cottons.lypheng.model.Template;
import live.cottons.lypheng.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TemplateService {

    private final TemplateRepository templateRepository;

    public Template create(Template template) {
        if (templateRepository.existsByCode(template.getCode())) {
            throw new IllegalArgumentException("Template code already exists: " + template.getCode());
        }
        return templateRepository.save(template);
    }

    public Template getById(Long id) {
        return templateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Template not found with id: " + id));
    }

    public Template getByCode(String code) {
        return templateRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Template not found with code: " + code));
    }

    @Transactional(readOnly = true)
    public List<Template> listAll() {
        return templateRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Template> search(String query) {
        return templateRepository.findByNameContainingIgnoreCase(query);
    }

    public Template update(Long id, Template updates) {
        Template existing = getById(id);
        if (updates.getName() != null) existing.setName(updates.getName());
        if (updates.getOrganizationName() != null) existing.setOrganizationName(updates.getOrganizationName());
        if (updates.getLayout() != null) existing.setLayout(updates.getLayout());
        if (updates.getPrimaryColor() != null) existing.setPrimaryColor(updates.getPrimaryColor());
        if (updates.getSecondaryColor() != null) existing.setSecondaryColor(updates.getSecondaryColor());
        if (updates.getTextColor() != null) existing.setTextColor(updates.getTextColor());
        if (updates.getTagline() != null) existing.setTagline(updates.getTagline());
        return templateRepository.save(existing);
    }

    public void delete(Long id) {
        if (!templateRepository.existsById(id)) {
            throw new IllegalArgumentException("Template not found with id: " + id);
        }
        templateRepository.deleteById(id);
    }
}
