package shoppingMall.project.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shoppingMall.project.constant.SellStatus;
import shoppingMall.project.domain.ItemDto;
import shoppingMall.project.entity.Item;
import shoppingMall.project.entity.ItemImg;
import shoppingMall.project.repository.ImgRepository;
import shoppingMall.project.repository.ItemRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class ItemServiceTest {

    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ImgRepository imgRepository;

    List<MultipartFile> multipartFileList() throws Exception {
        List<MultipartFile> files = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            String path = "C:/shop/item/";
            String imgNm = "img" + i + ".jpg";
            MockMultipartFile file = new MockMultipartFile(path, imgNm,
                    "image/jpg", new byte[]{1, 2, 3, 4});
            files.add(file);
        }
        return files;
    }

    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void saveItem() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setItemNm("테스트상품");
        itemDto.setSellStatus(SellStatus.SELL);
        itemDto.setItemDtl("테스트 상품 입니다.");
        itemDto.setPrice(1000);
        itemDto.setStock(100);

        List<MultipartFile> multipartFileList = multipartFileList();
        Long id = itemService.uploadItem(itemDto, multipartFileList);
        List<ItemImg> itemImgs = imgRepository.findByItemId(id);

        Item item = itemRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        assertEquals(itemDto.getItemNm(), item.getItemNm());
        assertEquals(itemDto.getSellStatus(), item.getSellStatus());
        assertEquals(itemDto.getItemDtl(), item.getItemDtl());
        assertEquals(itemDto.getPrice(), item.getPrice());
        assertEquals(itemDto.getStock(), item.getStock());
        assertEquals(multipartFileList.get(0).getOriginalFilename(), itemImgs.get(0).getImgOriginNm());
    }
}