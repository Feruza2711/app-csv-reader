package uz.pdp.projectimtihon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.projectimtihon.entity.Section;

import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section,Integer> {
    Optional<Section> findByName(String sectionName);
}
