package live.cottons.lypheng.controller;

import live.cottons.lypheng.model.Template;
import live.cottons.lypheng.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    @PostMapping
    public ResponseEntity<Template> create(@RequestBody Template template) {
        return ResponseEntity.status(HttpStatus.CREATED).body(templateService.create(template));
    }

    @GetMapping
    public ResponseEntity<List<Template>> listAll(@RequestParam(required = false) String search) {
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(templateService.search(search));
        }
        return ResponseEntity.ok(templateService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Template> getById(@PathVariable Long id) {
        return ResponseEntity.ok(templateService.getById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Template> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(templateService.getByCode(code));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Template> update(@PathVariable Long id, @RequestBody Template updates) {
        return ResponseEntity.ok(templateService.update(id, updates));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        templateService.delete(id);
    }
}
