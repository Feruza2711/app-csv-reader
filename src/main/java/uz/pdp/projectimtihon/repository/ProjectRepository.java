package uz.pdp.projectimtihon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.projectimtihon.entity.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project,Integer> {
    List<Project> findByUserId(Integer userId);
}
