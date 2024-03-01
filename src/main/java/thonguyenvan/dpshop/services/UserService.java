package thonguyenvan.dpshop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import thonguyenvan.dpshop.security.TokenProvider;
//import thonguyenvan.dpshop.components.JwtTokenUtils;
import thonguyenvan.dpshop.dtos.UserDTO;
import thonguyenvan.dpshop.exeptions.DataNotFoundException;
import thonguyenvan.dpshop.exeptions.PermissionDenyException;
import thonguyenvan.dpshop.models.Role;
import thonguyenvan.dpshop.models.User;
import thonguyenvan.dpshop.repositories.RoleRepository;
import thonguyenvan.dpshop.repositories.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserServices{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
//    private final JwtTokenUtils jwtTokenUtils;
//    private final AuthenticationManager authenticationManager;
    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        String phoneNumber = userDTO.getPhoneNumber();
        // kiem tra sdt da ton tai hay chua.
        if(userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        // kiem tra Role
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        if (role.getName().toUpperCase().equals(Role.ADMIN)) {
            throw new PermissionDenyException("You cannot register a ADMIN account");
        }
        // convert sang user
        User user = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .address(userDTO.getAddress())
                .password(userDTO.getPassword())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getFacebookAccountId())
                .active(true)
                .build();

        user.setRole(role);
        // kiem tra neu co account social Id, khong yeu cau password
        if(userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0) {
            String password = userDTO.getPassword();
             String encodedPassword = passwordEncoder.encode(password);
             user.setPassword(encodedPassword);
        }
        return userRepository.save(user);
    }

    @Override
    public User findByPhoneNumber(String phoneNumber) throws DataNotFoundException {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new DataNotFoundException("Cannot find User with phone number = " + phoneNumber));
    }

    @Override
    public String login(String phoneNumber, String password) throws DataNotFoundException {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if (optionalUser.isEmpty()) {
            throw new DataNotFoundException("Invalid phone number or password");
        }
        User existingUser = optionalUser.get();
        // check password
        if (existingUser.getFacebookAccountId() == 0 || existingUser.getGoogleAccountId() == 0) {
            if (!passwordEncoder.matches(password, existingUser.getPassword())) {
                throw new BadCredentialsException("Wrong phone number or password");
            }
        }
        // authenticate with Java spring security
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(phoneNumber, password, existingUser.getAuthorities());
        Authentication authentication = authenticationManagerBuilder
                .getObject().authenticate(authenticationToken);
        return tokenProvider.generateAccessToken(authentication);

//        authenticationManager.authenticate(authenticationToken);
//        return jwtTokenUtils.generateToken(existingUser);
    }
}
