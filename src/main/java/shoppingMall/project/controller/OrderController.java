package shoppingMall.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import shoppingMall.project.domain.OrderDto;
import shoppingMall.project.domain.OrderHistoDto;
import shoppingMall.project.domain.OrderItemDto;
import shoppingMall.project.service.OrderService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class OrderController {
    @Autowired
    OrderService orderService;


    @PostMapping("/order/item")
    @ResponseBody
    public ResponseEntity<String> getOrder(@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult, Principal principal){
        System.out.println("주문");
        if (bindingResult.hasErrors()){
            StringBuilder stringBuilder = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors){
                stringBuilder.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<>(stringBuilder.toString(), HttpStatus.BAD_REQUEST);
        }
        try {
            orderService.getOrder(orderDto, principal.getName());
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHistPage(@PathVariable("page") Optional<Integer> page, Model model, Principal principal){
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0,4);
        try {
            Page<OrderHistoDto> orderHistoDtos = orderService.getOrderList(principal.getName(),pageable);
            model.addAttribute("orders",orderHistoDtos);
            model.addAttribute("page",pageable.getPageNumber());
            model.addAttribute("maxPage",5);
        }catch (Exception e){
            model.addAttribute("errorMessage",e.getMessage());
            return "order";
        }
        return "order";
    }
    @DeleteMapping("/order/cancel/{orderId}")
    @ResponseBody
    public ResponseEntity<String> cancelOrder(@PathVariable("orderId")Long orderId,Principal principal){
        System.out.println("주문 취소");
        if (!orderService.checkVail(principal.getName(),orderId)){
            return new ResponseEntity<>("권한이 없습니다",HttpStatus.FORBIDDEN);
        }
        orderService.cancelOrder(orderId);
        return new ResponseEntity<>("주문이 취소되었습니다.",HttpStatus.OK);
    }
    @GetMapping("/order/item/{orderId}")
    public String orderItemL(@PathVariable("orderId")Long id,Model model,Principal principal){
        System.out.println("주문 상품 컨트롤러!!!");
        /*if (!orderService.checkVail(principal.getName(),id)){
             model.addAttribute("errorMessage","권한이없습니다");
            return "orderItem";
        }*/
        OrderHistoDto dto = orderService.orderDetl(id);
        List<OrderItemDto> orderItemDtoList = dto.getOrderItemDtos();
        System.out.println("orderItemDtoList!!:" +orderItemDtoList);
        model.addAttribute("dto",dto);
        model.addAttribute("orderItem",orderItemDtoList);
        return "orderItem";
    }
}
