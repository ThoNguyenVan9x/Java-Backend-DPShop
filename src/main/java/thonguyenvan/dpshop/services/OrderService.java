package thonguyenvan.dpshop.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import thonguyenvan.dpshop.dtos.CartItemDTO;
import thonguyenvan.dpshop.dtos.OrderDTO;
import thonguyenvan.dpshop.exeptions.DataNotFoundException;
import thonguyenvan.dpshop.models.*;
import thonguyenvan.dpshop.repositories.OrderDetailRepository;
import thonguyenvan.dpshop.repositories.OrderRepository;
import thonguyenvan.dpshop.repositories.ProductRepository;
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
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;

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
        order.setTotalMoney(orderDTO.getTotalMoney());
        orderRepository.save(order);
        OrderResponse orderResponse = new OrderResponse();
        modelMapper.typeMap(Order.class, OrderResponse.class);
        modelMapper.map(order, orderResponse);
        // tao danh sach cac doi tuong OrderDetail tu CartItems
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItemDTO cartItemDTO : orderDTO.getCartItems()) {
            // tao mot doi tuong OrderDetail tu CartItemDTO
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            // lay thong tin san pham tu CartItemDTO
            Long productId = cartItemDTO.getProductId();
            Long quantity = cartItemDTO.getQuantity();
            // tim thong tin san pham tu co so du lieu (hoac su dung cache neu can)
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new DataNotFoundException("Cannot find Product with id = " + productId));
            // dat thong tin cho OrderDetail
            orderDetail.setProduct(product);
            orderDetail.setNumberOfProducts(quantity);
            // cac truong khac cua OrderDetail neu can
            orderDetail.setPrice(product.getPrice());
            // them OrderDetail vao danh sach
            orderDetails.add(orderDetail);
        }
        // luu danh sach OrderDetail vao database.
        orderDetailRepository.saveAll(orderDetails);
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

    @Override
    public Page<Order> getListOrders(String keyword, PageRequest pageRequest) {
        return orderRepository.findAllListOrders(keyword, pageRequest);
    }
}
