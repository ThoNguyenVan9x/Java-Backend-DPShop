package thonguyenvan.dpshop.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import thonguyenvan.dpshop.dtos.OrderDTO;
import thonguyenvan.dpshop.exeptions.DataNotFoundException;
import thonguyenvan.dpshop.models.Order;
import thonguyenvan.dpshop.models.OrderStatus;
import thonguyenvan.dpshop.models.User;
import thonguyenvan.dpshop.repositories.OrderRepository;
import thonguyenvan.dpshop.repositories.UserRepository;
import thonguyenvan.dpshop.responses.OrderResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @Override
    public OrderResponse createOrder(OrderDTO orderDTO) throws DataNotFoundException {
        // tim xem user id co ton tai hay khong
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id: " + orderDTO.getUserId()));
        // convert dto => order
        // dung thu vien ModalMapper
        // tao mot luong bang anh xa rieng de kiem soat viec anh xa
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        // cap nhat cac truong cua don hang tu orderDTO
        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        order.setStatus(OrderStatus.PENDING);
        // kiem tra shipping date phai >= ngay hom nay
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now().plusDays(2) : orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFoundException("Shipping date must be present or future");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);
        OrderResponse orderResponse = new OrderResponse();
        modelMapper.typeMap(Order.class, OrderResponse.class);
        modelMapper.map(order, orderResponse);
        return orderResponse;
    }

    @Override
    public OrderResponse getOrder(Long id) throws DataNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find order with id = " + id));
        OrderResponse orderResponse = new OrderResponse();
        modelMapper.typeMap(Order.class, OrderResponse.class);
        modelMapper.map(order, orderResponse);
        return orderResponse;
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find order with id = " + id));
        LocalDate originOrderDate = existingOrder.getOrderDate();

        User existingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id = " + existingOrder.getUser().getId()));
        // tao mot luong bang anh xa rieng de kiem soat viec anh xa
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        // cap nhat cac truong cua don hang tu orderDTO
        modelMapper.map(orderDTO, existingOrder);

        existingOrder.setUser(existingUser);
        existingOrder.setOrderDate(originOrderDate);
        orderRepository.save(existingOrder);
        // convert order => order response
        OrderResponse orderResponse = new OrderResponse();
        modelMapper.typeMap(Order.class, OrderResponse.class);
        modelMapper.map(existingOrder, orderResponse);
        return orderResponse;
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        // khong xoa cung => xoa mem
        if (order != null) {
            order.setActive(false);
            orderRepository.save(order);
        }
    }

    @Override
    public List<OrderResponse> getAllOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        List<OrderResponse> orderResponses = new ArrayList<>();
        modelMapper.typeMap(Order.class, OrderResponse.class);

        for(Order order : orders) {
            OrderResponse orderResponse = new OrderResponse();
            modelMapper.map(order, orderResponse);
            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }
}
