package thonguyenvan.dpshop.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import thonguyenvan.dpshop.enums.Role;
import thonguyenvan.dpshop.exeptions.UnauthorizedException;

import java.util.Optional;

public class SecurityUtils {
    public static Optional<String> getCurrentAccountOpt() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        Object object = authentication.getPrincipal();
        if (object instanceof User) {
            return Optional.of(((User) object).getUsername());
        }
        return Optional.empty();
    }

    public static String getCurrentAccount() {
        return getCurrentAccountOpt().orElseThrow(UnauthorizedException::new);
    }

    public static Optional<Role> getRoleCurrentAccountOpt() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .map(authority -> Role.valueOf(authority.getAuthority().replaceAll("ROLE_", "")))
                .findFirst();
    }

    public static Role getRoleCurrentAccount() {
        return getRoleCurrentAccountOpt().orElseThrow(UnauthorizedException::new);
    }

    public static boolean isAdmin() {
        return getRoleCurrentAccount() == Role.ADMIN;
    }

    public static boolean isCustomer() {
        return getRoleCurrentAccount() == Role.CUSTOMER;
    }
}
