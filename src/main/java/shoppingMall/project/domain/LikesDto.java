package shoppingMall.project.domain;

import lombok.Data;

@Data
public class LikesDto {
    private Long id;
    private String itemNm;
    private String imgUrl;

    public LikesDto(Long id, String itemNm, String imgUrl) {
        this.id = id;
        this.itemNm = itemNm;
        this.imgUrl = imgUrl;
    }
}
