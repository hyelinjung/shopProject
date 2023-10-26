package shoppingMall.project.domain;

import lombok.Data;
import org.modelmapper.ModelMapper;
import shoppingMall.project.constant.SellStatus;
import shoppingMall.project.entity.Item;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class ItemDto {
    private Long id;
    @NotBlank(message = "필수 입력값")
    private String itemNm;
    @NotNull(message = "필수 입력값")
    private int stock;
    @NotBlank(message = "필수 입력값")
    private String itemDtl;
    @NotNull(message = "필수 입력값")
    private int price;
    private SellStatus sellStatus;
    private String largeItemType; //O,T,P,D,ACT
    private String subItemType; //
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();
    private List<Long> imgId = new ArrayList<>(); //이미지 수정 시

    private static ModelMapper modelMapper = new ModelMapper();
    public  Item doe(ItemDto itemDto){
        return modelMapper.map(itemDto, Item.class);
    }
    public static ItemDto etd(Item item){
        return modelMapper.map(item, ItemDto.class);
    }

}
