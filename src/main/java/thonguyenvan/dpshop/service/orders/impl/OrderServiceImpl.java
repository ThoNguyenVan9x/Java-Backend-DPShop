package thonguyenvan.dpshop.service.orders.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import thonguyenvan.dpshop.entity.Orders;
import thonguyenvan.dpshop.repository.orders.OrderRepository;
import thonguyenvan.dpshop.service.orders.OrderService;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    @Override
    public Orders addOrder(Orders orders) {
        return orderRepository.save(orders);
    }
}
