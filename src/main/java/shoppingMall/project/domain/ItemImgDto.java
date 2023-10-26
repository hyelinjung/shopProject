package shoppingMall.project.domain;

import lombok.Data;
import org.modelmapper.ModelMapper;
import shoppingMall.project.entity.ItemImg;

@Data
public class ItemImgDto {
    private Long id;
   private String itemOriginNm; //원본 제목
    private String imgNm; //uui+ 제목
    private String imgYn; // 대표 사진
    private String itemUrl;
    private static ModelMapper modelMapper =new ModelMapper();

    public static ItemImgDto eTd(ItemImg itemImg){
        return modelMapper.map(itemImg, ItemImgDto.class);
    }
}
