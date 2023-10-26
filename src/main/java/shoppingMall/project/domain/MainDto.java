package shoppingMall.project.domain;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class MainDto {
    private Long id;
    private String itemNm;
    private int price;
    private String imgUrl;
    private String itemDtl;
    @QueryProjection
    public MainDto(Long id,String imgUrl,Integer price,String itemNm,String itemDtl){
        this.id=id;
        this.imgUrl=imgUrl;
        this.price =price;
        this.itemNm = itemNm;
        this.itemDtl = itemDtl;
    }
}
