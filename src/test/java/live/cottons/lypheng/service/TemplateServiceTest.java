package live.cottons.lypheng.service;

import live.cottons.lypheng.model.Template;
import live.cottons.lypheng.repository.TemplateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TemplateServiceTest {

    @Mock
    private TemplateRepository templateRepository;

    @InjectMocks
    private TemplateService templateService;

    @Test
    void create_newTemplate_succeeds() {
        Template template = Template.builder()
                .code("DEFAULT")
                .name("Default Template")
                .build();

        when(templateRepository.existsByCode("DEFAULT")).thenReturn(false);
        when(templateRepository.save(any(Template.class))).thenAnswer(inv -> {
            Template t = inv.getArgument(0);
            t.setId(1L);
            return t;
        });

        Template result = templateService.create(template);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("DEFAULT", result.getCode());
        verify(templateRepository).save(any());
    }

    @Test
    void create_duplicateCode_throwsException() {
        Template template = Template.builder().code("EXISTING").name("Test").build();
        when(templateRepository.existsByCode("EXISTING")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> templateService.create(template));
        verify(templateRepository, never()).save(any());
    }

    @Test
    void getById_found_returnsTemplate() {
        Template template = Template.builder().id(1L).code("TEST").name("Test").build();
        when(templateRepository.findById(1L)).thenReturn(Optional.of(template));

        Template result = templateService.getById(1L);

        assertEquals("TEST", result.getCode());
    }

    @Test
    void getById_notFound_throwsException() {
        when(templateRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> templateService.getById(99L));
    }

    @Test
    void listAll_returnsAllTemplates() {
        when(templateRepository.findAll()).thenReturn(List.of(
                Template.builder().id(1L).code("A").name("Alpha").build(),
                Template.builder().id(2L).code("B").name("Beta").build()
        ));

        List<Template> result = templateService.listAll();

        assertEquals(2, result.size());
    }

    @Test
    void update_existingTemplate_updatesFields() {
        Template existing = Template.builder()
                .id(1L).code("T1").name("Old Name").primaryColor("#000000").build();
        when(templateRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(templateRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Template updates = Template.builder().name("New Name").primaryColor("#ff0000").build();
        Template result = templateService.update(1L, updates);

        assertEquals("New Name", result.getName());
        assertEquals("#ff0000", result.getPrimaryColor());
        assertEquals("T1", result.getCode()); // unchanged
    }

    @Test
    void delete_existingTemplate_deletes() {
        when(templateRepository.existsById(1L)).thenReturn(true);

        templateService.delete(1L);

        verify(templateRepository).deleteById(1L);
    }

    @Test
    void delete_notFound_throwsException() {
        when(templateRepository.existsById(99L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> templateService.delete(99L));
    }
}
