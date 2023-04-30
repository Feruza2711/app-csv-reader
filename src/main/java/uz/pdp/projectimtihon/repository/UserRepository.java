package uz.pdp.projectimtihon.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.projectimtihon.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByVerificationCode(String code);

    Optional<User> findByVerificationCodeForPassword(String code);
}
