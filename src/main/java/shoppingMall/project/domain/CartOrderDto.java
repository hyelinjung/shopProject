package shoppingMall.project.domain;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class CartOrderDto {
    private Long itemId;
    @Min(value = 1,message = "최소 1개 이상 선택가능 합니다.")
    private int count;
}
