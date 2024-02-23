package thonguyenvan.dpshop.service.orderItem.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import thonguyenvan.dpshop.entity.OrderItem;
import thonguyenvan.dpshop.repository.orderItem.OrderItemRepository;
import thonguyenvan.dpshop.service.orderItem.OrderItemService;

@Service
@RequiredArgsConstructor
public class OrderItemImpl implements OrderItemService {

    private  final OrderItemRepository orderItemRepository;
    @Override
    public OrderItem addOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }
}
