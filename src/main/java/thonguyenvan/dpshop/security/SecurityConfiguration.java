package thonguyenvan.dpshop.security;

import lombok.RequiredArgsConstructor;
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
import thonguyenvan.dpshop.enums.Role;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final TokenProvider tokenProvider;

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
                        .requestMatchers("/api/login").permitAll()
                        .requestMatchers("/api/products/list").permitAll()
                        .requestMatchers("/api/products/detail/**").permitAll()
                        .requestMatchers("/api/products/add").hasRole(Role.ADMIN.name())
                        .requestMatchers("/api/products/edit").hasRole(Role.ADMIN.name())
                        .requestMatchers("/api/accounts/**").hasAnyRole(Role.ADMIN.name(), Role.CUSTOMER.name())
                        .requestMatchers("/api/admin/**").hasRole(Role.ADMIN.name())
                        .anyRequest().authenticated())
                .logout(config -> config.logoutUrl("/api/logout").logoutSuccessUrl("/api/login?logout").permitAll())

                .httpBasic(Customizer.withDefaults())
                .apply(new JWTFilterConfiguration(tokenProvider));
        return security.build();
    }
}
