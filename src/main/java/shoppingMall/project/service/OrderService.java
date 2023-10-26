package shoppingMall.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;
import shoppingMall.project.domain.OrderDto;
import shoppingMall.project.domain.OrderHistoDto;
import shoppingMall.project.domain.OrderItemDto;
import shoppingMall.project.entity.*;
import shoppingMall.project.repository.*;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ImgRepository imgRepository;
    @Autowired
    OrderItemRepository orderItemRepository;



        public void getOrder(OrderDto orderDto,String username) throws Exception {
            Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
            List<OrderItem> orderItems = new ArrayList<>();
            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
            orderItemRepository.save(orderItem);
            orderItems.add(orderItem);
            User user = userRepository.findByUsername(username);

            Order order = Order.createOrder(user,orderItems);
            orderRepository.save(order);
        }

        public void getCartOrder(List<OrderDto> orderDtos,String username) throws Exception {
            User user = userRepository.findByUsername(username);
            List<OrderItem> orderItemList = new ArrayList<>();
            for (OrderDto orderDto :orderDtos){
                Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
                OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
                orderItemRepository.save(orderItem);
                orderItemList.add(orderItem);
            }
            Order order = Order.createOrder(user,orderItemList);
            orderRepository.save(order);
        }
        @Transactional(readOnly = true)
        public Page<OrderHistoDto> getOrderList(String username, Pageable pageable){
        List<Order> orders = orderRepository.findOrder(username,pageable);
        Long count = orderRepository.findOrderCount(username);
        List<OrderHistoDto> orderHistoDtos = new ArrayList<>();
        for (Order order : orders){
            List<OrderItemDto> orderItemDtos = new ArrayList<>();
            for (OrderItem orderItem : order.getOrderItems()){
                Long id = orderItem.getItem().getId();
                String y = "Y";
                ItemImg itemImg =imgRepository.findByItemIdAndImgYn(id,y);
                String imgUrl = itemImg.getImgUrl();
                OrderItemDto orderItemDto = new OrderItemDto(orderItem,imgUrl);
                orderItemDtos.add(orderItemDto);
            }
            OrderHistoDto dto = new OrderHistoDto(orderItemDtos,order);
            orderHistoDtos.add(dto);
        }
        return new PageImpl<OrderHistoDto>(orderHistoDtos,pageable,count);
        }

        public boolean checkVail(String username,Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        String savedUser = order.getUser().getUsername();
        if (!StringUtils.equals(username,savedUser)){
            return false;
        }
        return true;
        }

        public void cancelOrder(Long orderId){
            Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
            order.orderCancel();
            orderRepository.save(order);
        }
        @Transactional(readOnly = true)
        public OrderHistoDto orderDetl(Long id ){
            System.out.println("주문 상품 디테일 ");
            Order order = orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            List<OrderItemDto> orderItemDtos = new ArrayList<>();
            for (OrderItem orderItem : order.getOrderItems()){
                Item item = orderItem.getItem();
                String yn = "Y";
                ItemImg itemImg = imgRepository.findByItemIdAndImgYn(item.getId(),yn);
                OrderItemDto dto = new OrderItemDto(orderItem,itemImg.getImgUrl());
                orderItemDtos.add(dto);
            }
            System.out.println("orderItemDtos :" +orderItemDtos);
            OrderHistoDto histoDto = new OrderHistoDto(orderItemDtos,order);
           return histoDto;
        }
}
