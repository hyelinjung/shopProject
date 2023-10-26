package shoppingMall.project.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shoppingMall.project.domain.MainDto;
import shoppingMall.project.entity.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item,Long>,CustomRepository {

    public Item findById(String id); //상품 상세

    @Query("select new shoppingMall.project.domain.MainDto(i.id, im.imgUrl, i.price, i.itemNm, i.itemDtl) "+
            "from Item i "+
            "join ItemImg im "+
            "on i.id = im.item.id "+
            //"where i.largeItemType = :largeItemType and i.subItemType = :subItemType "+
            "where im.imgYn = 'Y' "+
            "and i.subItemType = :subItemType "+
            "and i.largeItemType = :largeItemType "+
            "order by i.id desc")
    List<MainDto> findCate(@Param("largeItemType")String largeItemType, @Param("subItemType") String subItemType, Pageable pageable);

    @Query("select count(*) from Item i where i.largeItemType = :largeItemType and i.subItemType = :subItemType")
    int itemCount(@Param("largeItemType") String largeItemType ,@Param("subItemType") String subItemType);
}
