package thonguyenvan.dpshop.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thonguyenvan.dpshop.components.LocalizationUtils;
import thonguyenvan.dpshop.dtos.UserDTO;
import thonguyenvan.dpshop.dtos.UserLoginDTO;
import thonguyenvan.dpshop.models.User;
import thonguyenvan.dpshop.responses.LoginResponse;
import thonguyenvan.dpshop.responses.LoginUserResponse;
import thonguyenvan.dpshop.responses.RegisterResponse;
import thonguyenvan.dpshop.services.IUserServices;
import thonguyenvan.dpshop.utils.MessageKeys;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final IUserServices userService;
    private final LocalizationUtils localizationUtils;

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO,
                                        BindingResult result) {
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
                return ResponseEntity.badRequest().body(localizationUtils.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH));
            }
            userDTO.setFacebookAccountId(0);
            userDTO.setGoogleAccountId(0);
            User user = userService.createUser(userDTO);

            return ResponseEntity.ok(RegisterResponse.builder()
                            .user(user)
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_SUCCESSFULLY))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RegisterResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_FAILED, e.getMessage()))
                    .build());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginDTO userLoginDTO,
                                               HttpServletRequest request) {
        try {
            String token = userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword());
            User user = userService.findByPhoneNumber(userLoginDTO.getPhoneNumber());
            LoginUserResponse loginUserResponse = new LoginUserResponse();
            BeanUtils.copyProperties(user, loginUserResponse);
            loginUserResponse.setRole(user.getRole().getName());
            return ResponseEntity.ok(LoginResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                            .user(loginUserResponse)
                            .token(token)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(LoginResponse.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_FAILED, e.getMessage()))
                    .build());
        }
    }
}
