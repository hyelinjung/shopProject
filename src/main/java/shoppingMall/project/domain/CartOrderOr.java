package shoppingMall.project.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CartOrderOr {
    private Long cartItemId;
    private List<CartOrderOr> cartOrderOrList = new ArrayList<>();
}
