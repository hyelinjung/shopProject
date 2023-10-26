package shoppingMall.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shoppingMall.project.domain.ItemDto;
import shoppingMall.project.domain.ItemImgDto;
import shoppingMall.project.domain.MainDto;
import shoppingMall.project.domain.SearchDto;
import shoppingMall.project.entity.Item;
import shoppingMall.project.entity.ItemImg;
import shoppingMall.project.repository.ImgRepository;
import shoppingMall.project.repository.ItemRepository;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {
    @Autowired
    ImgService imgService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ImgRepository imgRepository;

    //상품 저장
    public Long uploadItem(ItemDto itemDto, List<MultipartFile> multipartFileList) throws IOException {
        Item item = itemDto.doe(itemDto);
        System.out.println("item======"+item);
        itemRepository.save(item);
        if (multipartFileList.size() >0) {
            for (int i = 0; i < multipartFileList.size(); i++) {
                ItemImg itemImg = new ItemImg();
                if (i == 0) {
                    itemImg.setImgYn("Y");
                } else {
                    itemImg.setImgYn("N");
                }
                itemImg.setItem(item);
                imgService.uploadImg(itemImg, multipartFileList.get(i));
            }
        }
        return item.getId();
    }

    //상품 수정
    public Long updateItem(ItemDto itemDto,List<MultipartFile> multipartFileList) throws Exception {
        System.out.println("상품 수정");
        Item item = itemRepository.findById(itemDto.getId()).orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemDto);
        //itemRepository.save(item);
        System.out.println("item수정"+ item);
        for (int i =0; i< multipartFileList.size(); i++){
            imgService.updateImg(itemDto.getImgId().get(i), multipartFileList.get(i));
        }
        return item.getId();
    }

    //상품 디테일 -> 상품 등록
    public ItemDto itemDtl(Long id){
        List<ItemImg> itemImgs = imgRepository.findByItemId(id);
        Item item = itemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        ItemDto itemDto = new ItemDto();
        itemDto.setItemDtl(item.getItemDtl());
        itemDto.setItemNm(item.getItemNm());
        itemDto.setPrice(item.getPrice());
        itemDto.setStock(item.getStock());
        itemDto.setId(item.getId());

        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for (ItemImg itemImg :itemImgs){
            ItemImgDto dto = ItemImgDto.eTd(itemImg);
            itemImgDtoList.add(dto);
        }
        itemDto.setItemImgDtoList(itemImgDtoList);
        return itemDto;
    }

    // 검색 할 수 있는 관리자 상품 리스트, 어떤 검색어로 어떤 테이블에서 가져올건지
    @Transactional(readOnly = true)
    public Page<Item> searchWithItemPage(SearchDto dto, Pageable pageable){  //관리자 상품 검색 관리
        return itemRepository.getAdminItemWithPage(dto,pageable);
    }

    @Transactional(readOnly = true) //상품 검색 시 sort 1 등록순
    public Page<MainDto> mainPageWsearch(SearchDto dto, Pageable pageable){
        return itemRepository.getMainItemWithPage(dto,pageable);
    }
    @Transactional(readOnly = true) //상품 검색 시 default
    public Page<MainDto> itemSearch(SearchDto dto,String sm, Pageable pageable){
        return itemRepository.getSearchItemWithPage(dto,sm,pageable);
    }

    /*public Page<MainDto> itemCate(String largeItemType,String subItemType,Pageable pageable){
        List<MainDto> mainDtos = itemRepository.findCate(largeItemType,subItemType,pageable);
        System.out.println("service:"+mainDtos);
        int count = itemRepository.itemCount(largeItemType,subItemType);
        System.out.println("count:"+ count);
        return new PageImpl<>(mainDtos,pageable,count);
    }*/

    public Page<MainDto> itemsCate(String largeItemType,String subItemType,String sm,Pageable pageable){
        return itemRepository.getCateItemWithPage(largeItemType, subItemType,sm, pageable);
    }

    public Page<MainDto> itemsCate2(String largeItemType,String subItemType,String sm,Pageable pageable){
        return itemRepository.getCateItemWithPage2(largeItemType, subItemType,sm, pageable);
    }
    public int getCount(String largeItemType,String subItemType){
        int count = (int) itemRepository.getCateItemCount(largeItemType,subItemType);
        System.out.println("카테결과 갯수:"+ count);
        return count;
    }

    public int getSearchCount(SearchDto dto){
        int count = (int) itemRepository.getSearchItemCount(dto);
        System.out.println("검색 갯수:"+ count);
        return count;
    }

}
