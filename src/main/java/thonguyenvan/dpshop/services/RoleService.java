package thonguyenvan.dpshop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import thonguyenvan.dpshop.models.Role;
import thonguyenvan.dpshop.repositories.RoleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService{
    private final RoleRepository roleRepository;
    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
