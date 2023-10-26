package shoppingMall.project.domain;

import lombok.Data;
import shoppingMall.project.entity.OrderItem;

@Data
public class OrderItemDto { //주문 상품
    private Long orderItemId;
    private String itemNm;
    private int orderPrice;
    private int count;
    private String imgUrl;

    public OrderItemDto(OrderItem orderItem, String imgUrl) {
        this.orderItemId = orderItem.getId();
        this.itemNm = orderItem.getItem().getItemNm();
        this.orderPrice = orderItem.getOrderPrice();
        this.count = orderItem.getCount();
        this.imgUrl = imgUrl;
    }
}
