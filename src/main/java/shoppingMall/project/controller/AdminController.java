package shoppingMall.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shoppingMall.project.domain.ItemDto;
import shoppingMall.project.domain.SearchDto;
import shoppingMall.project.entity.Item;

import shoppingMall.project.service.ItemService;


import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {
    @Autowired
    ItemService itemService;

    @GetMapping("/admin/item/new")
    public String itemsReg(Model model){
        System.out.println("아이템 등록");
        model.addAttribute("itemDto",new ItemDto());
      return "itemNew";
    }

    //상품 등록
    @PostMapping("/admin/item/new")
    public String itemsReg(@Valid ItemDto dto, BindingResult bindingResult, Model model,@RequestParam("itemImgFile") List<MultipartFile> multipartFileList)throws Exception {
       if (bindingResult.hasErrors()){
           return "itemNew";
       }
       if (multipartFileList.get(0).isEmpty()){
           model.addAttribute("errorMessage","대표사진은 필수값입니다.");
           return "itemNew";
       }
       try {
           itemService.uploadItem(dto,multipartFileList);
       }catch (Exception e){
           model.addAttribute("errorMessage",e.getMessage());
           return "itemNew";
       }
        return "itemNew";
    }

    //관리자 상품 상세 보기
    @GetMapping("/admin/item/{id}")
    public String itemsDtl(@PathVariable("id")Long id,Model model){
        try {
            ItemDto dto = itemService.itemDtl(id);
            System.out.println("상품 상세 보기:"+ dto);
            model.addAttribute("itemDto",dto);
        }catch (EntityNotFoundException e){
            model.addAttribute("errorMessage",e.getMessage());
            return "itemNew";
        }
        return "itemNew";
    }

    //상품수정
    @PostMapping("/admin/item/upd")
    public String itemsUpdate(@Valid ItemDto itemDto,BindingResult bindingResult,Model model,@RequestParam("itemImgFile") List<MultipartFile> multipartFileList){
        System.out.println("수정 컨트롤러");
        System.out.println("vo"+ itemDto);
        if (bindingResult.hasErrors()){
            return "itemNew";
        }
        if (multipartFileList.get(0).isEmpty() && itemDto.getId() == null){
            model.addAttribute("errorMessage","대표사진은 필수값입니다.");
            return "itemNew";
        }
        try {
            itemService.updateItem(itemDto,multipartFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage",e.getMessage());
            return "itemNew";
        }
        return "redirect:/";
    }

    //검색
    @GetMapping(value = {"/admin/items","/admin/items/{page}"})
    public String itemsPageWsearch(SearchDto dto, Model model,
                                  @PathVariable("page") Optional<Integer> page){
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0,3);

        Page<Item> items = itemService.searchWithItemPage(dto,pageable);
       /* List<itemsDto> items = new ArrayList<>();
        for (items items: itemss){
            itemsDto itemsDto = itemsDto.etd(items);
            items.add(itemsDto);
        }*/
        model.addAttribute("items",items);
        model.addAttribute("searchDto",dto);
        model.addAttribute("maxPage",5);

        return "itemMg";
    }

}
