package shoppingMall.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shoppingMall.project.domain.CateDto;
import shoppingMall.project.domain.ItemDto;
import shoppingMall.project.domain.MainDto;
import shoppingMall.project.domain.SearchDto;
import shoppingMall.project.service.ItemService;

import javax.websocket.server.PathParam;
import java.util.Optional;

@Controller
public class ItemController {
    @Autowired
    ItemService itemService;

    @GetMapping("/item/{id}")
    public String itemDtl(@PathVariable("id")Long id, Model model){
        System.out.println("아이템 디테일");
        ItemDto itemDto = itemService.itemDtl(id);
        model.addAttribute("itemDto",itemDto);
        return "shop-details";

    }
   @GetMapping("/item/cate")
    public String itemCate(String largeItemType, String subItemType, String sm, Optional<Integer> page, Model model){

        System.out.println("카테고리");
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0,5);
        Page<MainDto> mainDtos = itemService.itemsCate(largeItemType,subItemType,sm,pageable);
        int count = itemService.getCount(largeItemType,subItemType);
        if (!(sm == null)){
            if (sm.equals("1"))
            mainDtos = itemService.itemsCate2(largeItemType,subItemType,sm,pageable);
        }
       CateDto cateDto = new CateDto();
        if (subItemType == null){
            subItemType ="";
            cateDto.setLargeItemType(largeItemType);
            cateDto.setSubItemType(subItemType);
        }else {
            cateDto.setLargeItemType(largeItemType);
            cateDto.setSubItemType(subItemType);
        }
        System.out.println("cate:"+ mainDtos);
        model.addAttribute("item",mainDtos);
        model.addAttribute("cateDto",cateDto);
        model.addAttribute("count",count);
        model.addAttribute("sm",sm);
        model.addAttribute("maxPage",10);
       return "shop-grid";
    }

    @GetMapping("/item/search")
    public String searchItemCon( SearchDto dto,String sm, Optional<Integer> page, Model model){
        System.out.println("검색");
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0,5);
        Page<MainDto> mainDtos = itemService.itemSearch(dto,sm,pageable);
        System.out.println("main:"+ mainDtos);
        int count = itemService.getSearchCount(dto);
        if (sm.equals("1")){
            System.out.println("검색 들어옴");
            mainDtos =itemService.mainPageWsearch(dto,pageable);
        }

        model.addAttribute("item",mainDtos);
        model.addAttribute("searchDto",dto);
        model.addAttribute("count",count);
        model.addAttribute("sm",sm);
        model.addAttribute("maxPage",10);
        return "search";
    }
}
