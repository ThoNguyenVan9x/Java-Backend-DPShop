package thonguyenvan.dpshop.service.account;

import thonguyenvan.dpshop.dto.AccountInfoDTO;
import thonguyenvan.dpshop.entity.Account;

import java.util.Optional;

public interface AccountService {

    Optional<Account> getAccountByUsername(String username);
}
