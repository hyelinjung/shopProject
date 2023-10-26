package shoppingMall.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shoppingMall.project.domain.MainDto;
import shoppingMall.project.domain.SearchDto;
import shoppingMall.project.entity.Item;


import java.util.List;

public interface CustomRepository {

    Page<Item> getAdminItemWithPage(SearchDto dto, Pageable pageable);

    Page<MainDto> getMainItemWithPage(SearchDto dto,Pageable pageable);
    Page<MainDto> getSearchItemWithPage(SearchDto dto,String sm,Pageable pageable);

    Page<MainDto> getCateItemWithPage(String largeItemType,String subItemType,String sm,Pageable pageable);
    Page<MainDto> getCateItemWithPage2(String largeItemType,String subItemType,String sm,Pageable pageable);

    long getCateItemCount(String largeItemType,String subItemType); //총갯수- 카테고리
    long getSearchItemCount(SearchDto dto); //총갯수- 카테고리
}
