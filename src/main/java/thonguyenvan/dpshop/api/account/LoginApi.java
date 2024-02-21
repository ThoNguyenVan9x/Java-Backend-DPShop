package thonguyenvan.dpshop.api.account;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thonguyenvan.dpshop.dto.LoginRequestDTO;
import thonguyenvan.dpshop.security.TokenProvider;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginApi {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO){

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

        return ResponseEntity.ok(accessToken);
    }

}
