package shoppingMall.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import shoppingMall.project.domain.CartDetailDto;
import shoppingMall.project.domain.CartOrderDto;
import shoppingMall.project.domain.CartOrderOr;
import shoppingMall.project.service.CartService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class CartController {
    @Autowired
    CartService cartService;

    @PostMapping("/cart/add") //장바구니의 상품 추가
    @ResponseBody
    public ResponseEntity<String> addCartItem(@RequestBody @Valid CartOrderDto dto, BindingResult bindingResult, Principal principal){
        System.out.println("장바구니의 추가");
        //System.out.println(principal.getName());
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors){
                stringBuilder.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<>(stringBuilder.toString(), HttpStatus.BAD_REQUEST);
        }
        try {
           Long id= cartService.addCartItem(dto,principal.getName());
            System.out.println(id);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
    //장바구니 정보 가져오기
    @GetMapping("/user/cart")
    public String getCart(Principal principal, Model model){
        System.out.println("장바구니 페이지 가져오기");
        List<CartDetailDto> cartDetailDtos = cartService.cartItemList(principal.getName());
        model.addAttribute("cartItems",cartDetailDtos);
        return "shoping-cart";
    }

    //장바구니 수량 수정하기
    @PatchMapping("/user/cart/update/{cartItemId}")
    @ResponseBody
    public ResponseEntity<String> updateCount(@PathVariable("cartItemId")Long id,Principal principal,int count){
        System.out.println("장바구니 수정");
        System.out.println(count);
        if (count<=0){
            return new ResponseEntity<>("최소 1개 이상 가능합니다.",HttpStatus.BAD_REQUEST);
        }
       if (cartService.checkVail(id, principal.getName())){
            cartService.updateCartItemCount(id,count);
        }else {
            return new ResponseEntity<>("false",HttpStatus.FORBIDDEN);
        }
          return new ResponseEntity<>("success",HttpStatus.OK);

    }
    //장바구니 삭제
    @DeleteMapping("/user/cart/{cartItemId}")
    @ResponseBody
    public ResponseEntity<String> deleteCartItem(@PathVariable("cartItemId")Long id,Principal principal){
        System.out.println("장바구니 상품 삭제");
        if (!cartService.checkVail(id, principal.getName())) {
            return new ResponseEntity<>("false",HttpStatus.FORBIDDEN);
        }
        cartService.deleteCartItem(id);
        return new ResponseEntity<>("success",HttpStatus.OK);
    }

    @PostMapping("/cart/order")
    @ResponseBody
    public ResponseEntity<String> cartOrder(@RequestBody CartOrderOr ors, Principal principal){
        System.out.println("장바구니에서 주문하기");
        List<CartOrderOr> cartOrderOrList = ors.getCartOrderOrList();
        if (cartOrderOrList.size() == 0 || cartOrderOrList == null){
            return new ResponseEntity<>("주문할 상품을 선택해주세요",HttpStatus.FORBIDDEN);
        }
        try {
            cartService.cartOrder(cartOrderOrList, principal.getName());
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("주문이 완료되었습니다.", HttpStatus.OK);
    }
}
