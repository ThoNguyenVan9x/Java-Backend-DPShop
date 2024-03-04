package thonguyenvan.dpshop.configurations;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import thonguyenvan.dpshop.dtos.UserDTO;
import thonguyenvan.dpshop.exeptions.DataNotFoundException;
import thonguyenvan.dpshop.models.User;
import thonguyenvan.dpshop.security.TokenProvider;
import thonguyenvan.dpshop.services.IUserServices;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final IUserServices userServices;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Value("${frontend.url}")
    private String frontEndUrl;

    public OAuth2LoginSuccessHandler(@Lazy IUserServices userServices,
                                     @Lazy AuthenticationManagerBuilder authenticationManagerBuilder,
                                     @Lazy TokenProvider tokenProvider,
                                     @Lazy PasswordEncoder passwordEncoder) {
        this.userServices = userServices;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        String responseToken = "";
        String fullName = "";
        String phoneNumber = "";
        try {

            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
            DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = principal.getAttributes();
            if ("google".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
                fullName = attributes.get("family_name").toString() + " " + attributes.get("given_name").toString();
                phoneNumber = attributes.get("email").toString();
            } else if ("github".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
                fullName = attributes.get("login").toString();
                phoneNumber = attributes.get("id").toString();
            }
            User userGenToken = new User();

            Optional<User> existingUser = userServices.findByPhoneNumberSocial(phoneNumber);
            if (existingUser.isPresent()) {
                userGenToken = existingUser.get();
                userGenToken.setGoogleAccountId(1);
            } else {
                UserDTO userDTO = new UserDTO();
                userDTO.setFullName(fullName);
                userDTO.setGoogleAccountId(1);
                userDTO.setRoleId(1L);
                userDTO.setPhoneNumber(phoneNumber);
                userDTO.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                User user = userServices.createUser(userDTO);
                userGenToken = user;
            }
            // authenticate with Java spring security
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userGenToken.getPhoneNumber(),
                            userGenToken.getPassword(), userGenToken.getAuthorities());

            responseToken = tokenProvider.generateAccessToken(authenticationToken);

        } catch (Exception e) {
            e.printStackTrace();
        }

        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl(responseToken != "" ? frontEndUrl + responseToken + "/fullName/" + fullName : frontEndUrl);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
