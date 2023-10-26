package shoppingMall.project.domain;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
@Data
public class OrderDto {

    private Long itemId;
    @Min(value = 1, message = "최소 주문 수량은 1개 입니다.")
    @Max(value = 999, message = "최대 주문 수량은 999개 입니다.")
    private int count;
}
