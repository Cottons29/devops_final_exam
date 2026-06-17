package live.cottons.lypheng.repository;

import live.cottons.lypheng.model.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

    boolean existsByCode(String code);

    Optional<Template> findByCode(String code);

    List<Template> findByNameContainingIgnoreCase(String query);
}
