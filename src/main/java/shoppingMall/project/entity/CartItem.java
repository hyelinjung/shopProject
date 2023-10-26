package shoppingMall.project.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cart_Item_id")
    private Long id;
    private int count; //갯수
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;


    //장바구니 재고 관리
    public void getCount(int count) throws Exception{
        Item item = new Item();
        if (item.getStock()<count){
            throw new Exception("재고가 부족합니다");
        }
        item.minStock(count);
    }


    public static CartItem createCartItem(Item item,int count,Cart cart){
       CartItem cartItem =new CartItem();
       cartItem.setCart(cart);
       cartItem.setItem(item);
       cartItem.setCount(count);
       return cartItem;
    }
    public void addCount(int count){
        this.count+=count;
    }
    public void updateCount(int count){
        this.count = count;
    }
}
