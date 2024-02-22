package thonguyenvan.dpshop.api.account;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thonguyenvan.dpshop.dto.AccountInfoDTO;
import thonguyenvan.dpshop.dto.LoginRequestDTO;
import thonguyenvan.dpshop.entity.Account;
import thonguyenvan.dpshop.security.TokenProvider;
import thonguyenvan.dpshop.service.account.AccountService;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginApi {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO){

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword()
                );
        // call the implement of UserDetailService
        Authentication authentication = authenticationManagerBuilder
                .getObject().authenticate(authenticationToken);
        String accessToken = tokenProvider.generateAccessToken(authentication);
        System.out.println(accessToken);

        if(accessToken != "") {
            Account account = accountService.getAccountByUsername(loginRequestDTO.getUsername()).orElse(null);
            if(account != null) {
                AccountInfoDTO accountInfoDTO = new AccountInfoDTO();
                BeanUtils.copyProperties(account, accountInfoDTO);
                accountInfoDTO.setToken(accessToken);
                return ResponseEntity.ok(accountInfoDTO);
            }
        }
        return ResponseEntity.ok(accessToken);
    }

}
