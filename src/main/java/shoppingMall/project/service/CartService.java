package shoppingMall.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;
import shoppingMall.project.domain.CartDetailDto;
import shoppingMall.project.domain.CartOrderDto;
import shoppingMall.project.domain.CartOrderOr;
import shoppingMall.project.domain.OrderDto;
import shoppingMall.project.entity.Cart;
import shoppingMall.project.entity.CartItem;
import shoppingMall.project.entity.Item;
import shoppingMall.project.entity.User;
import shoppingMall.project.repository.CartItemRepository;
import shoppingMall.project.repository.CartRepository;
import shoppingMall.project.repository.ItemRepository;
import shoppingMall.project.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    OrderService orderService;


    //상품 장바구니에 추가
    public Long addCartItem(CartOrderDto dto, String username) {
        Cart cart = checkCart(username);
        Item item = itemRepository.findById(dto.getItemId()).orElseThrow(EntityNotFoundException::new);
        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), dto.getItemId());
        if (cartItem == null) {
            cartItem = CartItem.createCartItem(item, dto.getCount(), cart);
            cartItemRepository.save(cartItem);
        }
        cartItem.addCount(dto.getCount());
        cartItemRepository.save(cartItem);
        return cartItem.getId();
    }

    //장바구니 중복확인
    public Cart checkCart(String username) {

        User user = userRepository.findByUsername(username);
        Cart cart = cartRepository.findByUserId(user.getId());
        if (cart == null) {
            cart = Cart.createCart(user);
            cartRepository.save(cart);
        }
        return cart;
    }

    //장바구니에 상품 중복 확인
    public boolean checkStock(String username, Long itemId) {
        User user = userRepository.findByUsername(username);
        Cart cart = cartRepository.findByUserId(user.getId());
        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), itemId);
        if (cartItem != null) {
            return true;
        }
        return false;
    }

    // 장바구니 화면 - 어떤 사람의 장바구니를 가져올건지
    public List<CartDetailDto> cartItemList(String username) {
        List<CartDetailDto> cartDetailDtos = new ArrayList<>();
        User user = userRepository.findByUsername(username);
        Cart cart = cartRepository.findByUserId(user.getId());
        if (cart == null) {
            return cartDetailDtos;
        }
        cartDetailDtos = cartItemRepository.findCartDetailDtoList(cart.getId());
        return cartDetailDtos;
    }

    //장바구니에 있는 상품 수량 변경
    //수정하려는 상품이 내가 추가한 상품이 맞는지 확인
    public void updateCartItemCount(Long cartItemId, int count) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItem.updateCount(count);
        cartItemRepository.save(cartItem);
    }

    //어떤 상품 삭제
    //삭제하려는 상품이 내가 넣어논 상품이 맞는지
    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }

    @Transactional(readOnly = true)
    public boolean checkVail(Long cartItemId, String username) {
        User user = userRepository.findByUsername(username);
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        User savedUser = cartItem.getCart().getUser();
        if (!StringUtils.equals(savedUser.getUsername(), user.getUsername())) {
            return false;
        }
        return true;
    }

    public void cartOrder(List<CartOrderOr> cartOrderOrs, String username) throws Exception {
        List<OrderDto> orderDtos = new ArrayList<>();
      for (CartOrderOr or :cartOrderOrs){
            Long id = or.getCartItemId();
            CartItem cartItem = cartItemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            Long itemId = cartItem.getItem().getId();
            int count = cartItem.getCount();
            OrderDto orderDto = new OrderDto();
            orderDto.setCount(count);
            orderDto.setItemId(itemId);
            orderDtos.add(orderDto);
        }
        orderService.getCartOrder(orderDtos, username);

        for (CartOrderOr or :cartOrderOrs){
            Long id = or.getCartItemId();
            CartItem cartItem = cartItemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }

    }
}