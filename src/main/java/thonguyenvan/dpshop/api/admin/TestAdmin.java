package thonguyenvan.dpshop.api.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class TestAdmin {

    @GetMapping
    public String test() {
        return "Ban co quyen";
    }
}
