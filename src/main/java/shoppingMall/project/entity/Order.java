package shoppingMall.project.entity;

import lombok.Data;
import shoppingMall.project.constant.OrderStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*1. 나의 주문내역에서 모든 주문내역을 알고 싶다 -> 양방향 필요*/
@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private User user;

    private LocalDateTime orderDateTime;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //sold, cancel
    private int orderTotalPrice ;
    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    public void changeOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    public static Order createOrder(User user,List<OrderItem> orderItemList){
        Order order = new Order();
        int op = 0; //orderTotalPrice
        order.setOrderStatus(OrderStatus.SOLD);
        order.setOrderDateTime(LocalDateTime.now());
        order.setUser(user);
        for (OrderItem orderItem : orderItemList){
           order.changeOrderItem(orderItem);
           op += orderItem.getOrderPrice();
        }
        order.setOrderTotalPrice(op);
        return order;
    }

    public void orderCancel(){ //주문 취소는 낱개로 안하고 통채로
        this.orderStatus= OrderStatus.CANCEL;
        for (OrderItem orderItem :orderItems){
            orderItem.addStock();
        }


    }
}
