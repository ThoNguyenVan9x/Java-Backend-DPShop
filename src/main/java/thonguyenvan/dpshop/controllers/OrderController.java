package thonguyenvan.dpshop.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import thonguyenvan.dpshop.components.LocalizationUtils;
import thonguyenvan.dpshop.dtos.OrderDTO;
import thonguyenvan.dpshop.models.Order;
import thonguyenvan.dpshop.responses.OrderListResponse;
import thonguyenvan.dpshop.responses.OrderResponse;
import thonguyenvan.dpshop.services.IOrderService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;
    private final LocalizationUtils localizationUtils;
    private final ModelMapper modelMapper;

    @Transactional
    @PostMapping("")
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderDTO orderDTO,
                                         BindingResult result, HttpServletRequest request) {
        System.out.println("object: " + orderDTO.toString());
        System.out.println("content type: " + request.getContentType());
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
//        return null;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllOrders(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "keyword", defaultValue = "") String keyword
    ) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").descending());
        Page<Order> orderPage = orderService.getListOrders(keyword, pageRequest);
        int totalPages = orderPage.getTotalPages();
        List<Order> orders = orderPage.getContent();
        List<OrderResponse> orderResponses = new ArrayList<>();
        modelMapper.typeMap(Order.class, OrderResponse.class);

        for(Order order : orders) {
            OrderResponse orderResponse = new OrderResponse();
            modelMapper.map(order, orderResponse);
            orderResponses.add(orderResponse);
        }
        return ResponseEntity.ok(OrderListResponse.builder()
                        .orderResponses(orderResponses)
                        .totalPages(totalPages)
                .build());
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
