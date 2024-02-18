package thonguyenvan.dpshop.repository.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import thonguyenvan.dpshop.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByUsername(String username);

    List<Account> findAll();

    @Modifying
    void deleteByUsername(String username);
}
