package thonguyenvan.dpshop.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import thonguyenvan.dpshop.models.Role;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpMethod.DELETE;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final TokenProvider tokenProvider;

    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        security
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers(
                                String.format("%s/users/login", apiPrefix),
                                String.format("%s/users/register", apiPrefix)
                        ).permitAll()
                        .requestMatchers(GET, String.format("%s/categories**", apiPrefix)).permitAll()
                        .requestMatchers(POST, String.format("%s/categories/**", apiPrefix)).hasAnyRole(Role.ADMIN)
                        .requestMatchers(PUT, String.format("%s/categories/**", apiPrefix)).hasAnyRole(Role.ADMIN)
                        .requestMatchers(DELETE, String.format("%s/categories/**", apiPrefix)).hasAnyRole(Role.ADMIN)

                        .requestMatchers(GET, String.format("%s/products**", apiPrefix)).permitAll()
                        .requestMatchers(GET, String.format("%s/products/**", apiPrefix)).permitAll()
                        .requestMatchers(POST, String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)
                        .requestMatchers(PUT, String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)
                        .requestMatchers(DELETE, String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)
                        .requestMatchers(GET, String.format("%s/products/images/**", apiPrefix)).permitAll()

                        .requestMatchers(GET, String.format("%s/orders**", apiPrefix)).permitAll()
                        .requestMatchers(POST, String.format("%s/orders/**", apiPrefix)).hasRole(Role.CUSTOMER)
                        .requestMatchers(PUT, String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)
                        .requestMatchers(DELETE, String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)

                        .requestMatchers(GET, String.format("%s/order-details**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.CUSTOMER)
                        .requestMatchers(POST, String.format("%s/order-details/**", apiPrefix)).hasAnyRole(Role.CUSTOMER)
                        .requestMatchers(PUT, String.format("%s/order-details/**", apiPrefix)).hasAnyRole(Role.ADMIN)
                        .requestMatchers(DELETE, String.format("%s/order-details/**", apiPrefix)).hasAnyRole(Role.ADMIN)
                        .anyRequest().authenticated())

                .httpBasic(Customizer.withDefaults())
                .apply(new JWTFilterConfiguration(tokenProvider));
        return security.build();
    }
}
