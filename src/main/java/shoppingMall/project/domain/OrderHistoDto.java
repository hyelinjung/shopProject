package shoppingMall.project.domain;

import lombok.Data;
import shoppingMall.project.constant.OrderStatus;
import shoppingMall.project.entity.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderHistoDto { //주문 상품 덩어리 -> 첫주문,두번째 주문...


    private Long orderId;
    private OrderStatus orderStatus;
    private int totalOrderPrice;
    private LocalDateTime localDateTime;
    private List<OrderItemDto> orderItemDtos = new ArrayList<>();

    public void addOrderItem(List<OrderItemDto> orderItemDtoList){
        for (OrderItemDto dto : orderItemDtoList){
            orderItemDtos.add(dto);
        }
    }

    public OrderHistoDto(List<OrderItemDto> orderItemDtoList, Order order) {
        this.orderId = order.getId();
        this.orderStatus = order.getOrderStatus();
        this.totalOrderPrice = order.getOrderTotalPrice();
        this.localDateTime = order.getOrderDateTime();
        addOrderItem(orderItemDtoList);
    }
}
