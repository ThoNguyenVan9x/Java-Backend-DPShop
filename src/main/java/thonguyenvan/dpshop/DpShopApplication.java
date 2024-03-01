package thonguyenvan.dpshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class DpShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(DpShopApplication.class, args);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("1996"));
        System.out.println(LocalDateTime.now());
    }

}
