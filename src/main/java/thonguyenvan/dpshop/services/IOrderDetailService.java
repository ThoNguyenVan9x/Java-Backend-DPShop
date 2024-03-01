package thonguyenvan.dpshop.services;

import thonguyenvan.dpshop.dtos.OrderDetailDTO;
import thonguyenvan.dpshop.exeptions.DataNotFoundException;
import thonguyenvan.dpshop.models.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail (OrderDetailDTO orderDetailDTO) throws DataNotFoundException;

    OrderDetail getOrderDetail(Long id) throws DataNotFoundException;

    OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException;

    void deleteById(Long id);

    List<OrderDetail> findAllByOrderId(Long orderId);
}
