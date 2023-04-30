package uz.pdp.projectimtihon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.projectimtihon.entity.Task;

public interface TaskRepository extends JpaRepository<Task,Integer> {
}
