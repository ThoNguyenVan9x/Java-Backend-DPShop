package thonguyenvan.dpshop.service.account.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import thonguyenvan.dpshop.dto.AccountInfoDTO;
import thonguyenvan.dpshop.entity.Account;
import thonguyenvan.dpshop.repository.account.AccountRepository;
import thonguyenvan.dpshop.service.account.AccountService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;


    @Override
    public Optional<Account> getAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }
}
