package thonguyenvan.dpshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import thonguyenvan.dpshop.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumber(String phoneNumber);
}
