package thonguyenvan.dpshop.api.account;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thonguyenvan.dpshop.entity.Account;
import thonguyenvan.dpshop.service.account.AccountService;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class RegisterApi {
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    String register(@RequestBody HashMap reqBody) {
//        System.out.println(reqBody);
        HashMap data = (HashMap) reqBody.get("data");

        System.out.println(data);

        if(!accountService.getAccountByUsername((String) data.get("username")).isEmpty() ||
            !accountService.getAccountByEmail((String) data.get("email")).isEmpty() ||
            !accountService.getAccountByPhone((String) data.get("phone")).isEmpty()
        )
            return "invalid";
        Account account = new Account();
        account.setUsername((String) data.get("username"));
        account.setFullName((String) data.get("fullName"));
        account.setEmail((String) data.get("email"));
        account.setPhone((String) data.get("phone"));
        account.setAddress((String) data.get("address"));
        account.setUsername((String) data.get("username"));

        String password = passwordEncoder.encode((String) data.get("password"));

        account.setPassword(password);

        accountService.addAccount(account);

        return  "valid";
    }
}
