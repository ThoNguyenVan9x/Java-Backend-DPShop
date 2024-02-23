package thonguyenvan.dpshop.api.orders;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thonguyenvan.dpshop.dto.OrderDTO;
import thonguyenvan.dpshop.dto.ProductDTO;
import thonguyenvan.dpshop.entity.OrderItem;
import thonguyenvan.dpshop.entity.Orders;
import thonguyenvan.dpshop.entity.Product;
import thonguyenvan.dpshop.enums.OrderStatusEnum;
import thonguyenvan.dpshop.service.orderItem.OrderItemService;
import thonguyenvan.dpshop.service.orders.OrderService;
import thonguyenvan.dpshop.service.product.ProductService;

import java.security.Key;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderApi {

    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final ProductService productService;

    @PostMapping
    public Boolean orderCart(@RequestBody HashMap reqBody) {
        System.out.println("da chay vao ham order");
        HashMap data = new HashMap();

        Set<String> keySet = reqBody.keySet();

        for (String key : keySet) {
            data = (HashMap) reqBody.get(key);
        }

        HashMap customerInfo = (HashMap) data.get("customerInfo");
        System.out.println("customerInfo : " + customerInfo);
        Orders order = new Orders();
        order.setFullName((String) customerInfo.get("fullName"));
        order.setEmail((String) customerInfo.get("email"));
        order.setPhone((String) customerInfo.get("phone"));
        order.setAddress((String) customerInfo.get("address"));
        order.setNote((String) customerInfo.get("note"));
        order.setStatus(OrderStatusEnum.PENDING);
        order.setOrderDate(LocalDate.now());
        Orders orders1 = orderService.addOrder(order);
        System.out.println("order 1 id: " + orders1.getId());

        List<OrderItem> orderItemList = new ArrayList<>();

        ArrayList<HashMap> listCartItem = (ArrayList<HashMap>) data.get("cartItems");
        for(int i =0; i<listCartItem.size(); i++) {
            System.out.println("cart item " + i + " : " + listCartItem.get(i));
//            Integer id = (Integer) ;
            Optional<Product> product = productService.getProductById((Integer) listCartItem.get(i).get("id"));
            System.out.println("product name: "+ product.get().getName());
            OrderItem orderItem = new OrderItem();
            orderItem.setPrice((Integer) listCartItem.get(i).get("price"));
            orderItem.setQuantity((Integer) listCartItem.get(i).get("qty"));
            orderItem.setOrders(order);
            orderItem.setProduct(product.get());
            OrderItem orderItem1 = orderItemService.addOrderItem(orderItem);
            orderItemList.add(orderItem1);
        }
        System.out.println("total order item: " + orderItemList.size());




//        System.out.println(order.get("customerInfo"));
//        Set<String> keySet1 = order.keySet();
//        for (String key : keySet1) {
//            System.out.println(key + " - " + order.get(key));
//        }

//        order.forEach((key, value) -> {
//            System.out.println("key: " + key + ", value: " + value);
//
//        });

        return true;
    }
}
