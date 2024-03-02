package thonguyenvan.dpshop.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import thonguyenvan.dpshop.dtos.OrderDTO;
import thonguyenvan.dpshop.exeptions.DataNotFoundException;
import thonguyenvan.dpshop.models.Order;
import thonguyenvan.dpshop.responses.OrderResponse;

import java.util.List;

public interface IOrderService {

    OrderResponse createOrder(OrderDTO orderDTO) throws DataNotFoundException;

    OrderResponse getOrder(Long id) throws DataNotFoundException;

    OrderResponse updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException;

    void deleteOrder(Long id);

    List<OrderResponse> getAllOrdersByUserId(Long userId);

    Page<Order> getListOrders(String keyword, PageRequest pageRequest);
}
