package shoppingMall.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shoppingMall.project.domain.MainDto;
import shoppingMall.project.domain.SearchDto;
import shoppingMall.project.service.ItemService;

import java.util.Optional;

@Controller
public class ShopController {
    @Autowired
    ItemService itemService;

    @GetMapping("/")
    public String index( SearchDto dto, Optional<Integer> page, Model model){
        System.out.println("메인페이지");
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0,8);
        Page<MainDto> mainDtos = itemService.mainPageWsearch(dto,pageable);
        if (dto.getSearchBy() == null){
            dto.setSearchBy("");
        }
        System.out.println("main:"+ mainDtos);
        model.addAttribute("item",mainDtos);
        model.addAttribute("searchDto",dto);
        model.addAttribute("maxPage",10);
        return "index";
    }



    @GetMapping("/shop")
    public String shop(){
        return "shop-grid";
    }

    @GetMapping("/shopDetail")
    public String shopDt(){
        return "shop-details";
    }

    @GetMapping("/blog")
    public String blog(){
        return "blog";
    }
}
