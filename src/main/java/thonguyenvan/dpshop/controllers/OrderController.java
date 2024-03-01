package thonguyenvan.dpshop.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import thonguyenvan.dpshop.components.LocalizationUtils;
import thonguyenvan.dpshop.dtos.OrderDTO;
import thonguyenvan.dpshop.responses.OrderResponse;
import thonguyenvan.dpshop.services.IOrderService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;
    private final LocalizationUtils localizationUtils;

    @Transactional
    @PostMapping("")
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderDTO orderDTO,
                                         BindingResult result) {
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            OrderResponse orderResponse = orderService.createOrder(orderDTO);
            return ResponseEntity.ok(orderResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getOrders(@Valid @PathVariable("user_id") Long userId) {
        try {
            List<OrderResponse> orderResponses = orderService.getAllOrdersByUserId(userId);
            return ResponseEntity.ok(orderResponses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@Valid @PathVariable("id") Long id) {
        try {
            OrderResponse orderResponse = orderService.getOrder(id);
            return ResponseEntity.ok(orderResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @Transactional
    @PutMapping("/{id}")
    //cong viec cua admin
    public ResponseEntity<?> updateOrder(@Valid @PathVariable Long id,
                                         @Valid @RequestBody OrderDTO orderDTO) {
        try {
            OrderResponse orderResponse = orderService.updateOrder(id, orderDTO);
            return ResponseEntity.ok(orderResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@Valid @PathVariable Long id) {
        // xoa mem => cap nhat truong acive = false
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully");
    }
}
