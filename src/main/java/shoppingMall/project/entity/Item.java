package shoppingMall.project.entity;

import lombok.Data;
import shoppingMall.project.constant.SellStatus;
import shoppingMall.project.domain.ItemDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="item")
public class Item extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    private Long id;
    @Column(nullable = false, length = 50)
    private String itemNm;
    @Lob
    @Column(nullable = false)
    private String itemDtl;
    @Column(name="price", nullable = false)
    private int price;
    @Column(nullable = false)
    private int stock;
    @Enumerated(EnumType.STRING)
    private SellStatus sellStatus;
    private String largeItemType;
    private String subItemType;

    public static Item createItem(ItemDto itemDto){
        Item item = new Item();
        item.setItemDtl(item.getItemDtl());
        item.setItemNm(item.getItemNm());
        item.setPrice(item.getPrice());
        item.setStock(item.getStock());
        item.setSellStatus(itemDto.getSellStatus());
        return item;
    }

    public void updateItem(ItemDto itemDto){
        this.price = itemDto.getPrice();
        this.itemDtl = itemDto.getItemDtl();
        this.itemNm = itemDto.getItemNm();
        this.stock = itemDto.getStock();
        this.sellStatus = itemDto.getSellStatus();
        this.largeItemType =itemDto.getLargeItemType();
        this.subItemType =itemDto.getSubItemType();
    }

    //재고관리
    public void minStock(int count) throws Exception {
        this.stock -= count;
        if (this.getStock()<count){
            throw new Exception("재고가 부족합니다");
        }
    }
    public void addStock(int count){
        this.stock += count;
    }

}
