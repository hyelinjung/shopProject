package shoppingMall.project.repository;

import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;
import shoppingMall.project.constant.SellStatus;
import shoppingMall.project.domain.MainDto;
import shoppingMall.project.domain.QMainDto;
import shoppingMall.project.domain.SearchDto;
import shoppingMall.project.entity.Item;
import shoppingMall.project.entity.QItem;
import shoppingMall.project.entity.QItemImg;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

public class CustomRepositoryImpl implements CustomRepository{

    private JPAQueryFactory jpaQueryFactory ;

    public CustomRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    //쿼리문
    private BooleanExpression regDate(String searchDate){
        LocalDateTime localDateTime = LocalDateTime.now();
        if (StringUtils.equals("all",searchDate) || searchDate ==null){
            return  null;
        } else if (StringUtils.equals("1d",searchDate)) {
            localDateTime = localDateTime.minusDays(1);
        } else if (StringUtils.equals("1w",searchDate)) {
            localDateTime = localDateTime.minusWeeks(1);
        }else if(StringUtils.equals("1m",searchDate)){
            localDateTime = localDateTime.minusMonths(1);
        } else if (StringUtils.equals("6m",searchDate)) {
            localDateTime = localDateTime.minusMonths(6);
        }
        return QItem.item.regDateTime.after(localDateTime);
    }


    // 검색 조건 - 상품 타입


    //검색조건- 상품명
    private BooleanExpression itemSearchBy (String searchBy) {
            return StringUtils.isEmpty(searchBy)? null : QItem.item.itemNm.like( searchBy);
    }

    private BooleanExpression sellSearch(SellStatus sellStatus){
        return sellStatus == null? null : QItem.item.sellStatus.eq(sellStatus);
    }


    private BooleanExpression largeCate (String largeItemType) {
        return StringUtils.isEmpty(largeItemType)? null : QItem.item.largeItemType.like(largeItemType);
    }
    private BooleanExpression smallCate (String subItemType) {
        return StringUtils.isEmpty(subItemType)? null : QItem.item.subItemType.like(subItemType);
    }

    @Override
    public Page<Item> getAdminItemWithPage(SearchDto dto, Pageable pageable) {
        List<Item> result = jpaQueryFactory
                .selectFrom(QItem.item)
                .where(regDate(dto.getSearchDate()),
                        sellSearch(dto.getSellStatus())
                        )
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        long total = jpaQueryFactory.select(Wildcard.count).from(QItem.item)
                .where(regDate(dto.getSearchDate()),
                        sellSearch(dto.getSellStatus())
                        )
                .fetchOne();
        return new PageImpl<>(result,pageable,total);
    }

    @Override
    public Page<MainDto> getMainItemWithPage(SearchDto dto, Pageable pageable) { //검색 시 정렬 등록 순
         QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainDto> result =jpaQueryFactory
                .select(
                        new QMainDto(item.id,itemImg.imgUrl, item.price, item.itemNm,item.itemDtl)
                ).from(itemImg)
                .join(itemImg.item,item)
                .where(regDate(dto.getSearchDate()),
                        sellSearch(dto.getSellStatus()),
                        itemSearchBy(dto.getSearchBy()))
                .where(itemImg.imgYn.eq("Y"))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.imgYn.eq("Y"))
                .where(regDate(dto.getSearchDate()),
                sellSearch(dto.getSellStatus()),
                itemSearchBy(dto.getSearchBy()))
                .fetchOne();


        return new PageImpl<>(result,pageable,total);
    }

    @Override
    public Page<MainDto> getSearchItemWithPage(SearchDto dto, String sm, Pageable pageable) { //검색 시 정렬 가격 높은 순
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainDto> result =jpaQueryFactory
                .select(
                        new QMainDto(item.id,itemImg.imgUrl, item.price, item.itemNm,item.itemDtl)
                ).from(itemImg)
                .join(itemImg.item,item)
                .where(regDate(dto.getSearchDate()),
                        sellSearch(dto.getSellStatus()),
                        itemSearchBy(dto.getSearchBy()))
                .where(itemImg.imgYn.eq("Y"))
                .orderBy(item.price.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.imgYn.eq("Y"))
                .where(regDate(dto.getSearchDate()),
                        sellSearch(dto.getSellStatus()),
                        itemSearchBy(dto.getSearchBy()))
                .fetchOne();


        return new PageImpl<>(result,pageable,total);
    }



    @Override
    public Page<MainDto> getCateItemWithPage(String largeItemType, String subItemType,String sm, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainDto> result =jpaQueryFactory
                .select(
                        new QMainDto(item.id,itemImg.imgUrl, item.price, item.itemNm,item.itemDtl)
                ).from(itemImg)
                .join(itemImg.item,item)
                .where(largeCate(largeItemType),smallCate(subItemType))
                .where(itemImg.imgYn.eq("Y"))
                .orderBy( QItem.item.price.desc() )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.imgYn.eq("Y"))
                .where(largeCate(largeItemType),
                        smallCate(subItemType))
                .fetchOne();


        return new PageImpl<>(result,pageable,total);
    }

    @Override
    public Page<MainDto> getCateItemWithPage2(String largeItemType, String subItemType, String sm, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainDto> result =jpaQueryFactory
                .select(
                        new QMainDto(item.id,itemImg.imgUrl, item.price, item.itemNm,item.itemDtl)
                ).from(itemImg)
                .join(itemImg.item,item)
                .where(largeCate(largeItemType),smallCate(subItemType))
                .where(itemImg.imgYn.eq("Y"))
                .orderBy(QItem.item.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.imgYn.eq("Y"))
                .where(largeCate(largeItemType),
                        smallCate(subItemType))
                .fetchOne();


        return new PageImpl<>(result,pageable,total);
    }

    @Override
    public long getCateItemCount(String largeItemType, String subItemType) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        long count = jpaQueryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.imgYn.eq("Y"))
                .where(largeCate(largeItemType),
                        smallCate(subItemType))
                .fetchOne();
        return count;
    }

    @Override
    public long getSearchItemCount(SearchDto dto) { //검색 시 총 갯수
        QItem item = QItem.item;
        long count = jpaQueryFactory
                .select(Wildcard.count)
                .from(item)
                .where( itemSearchBy(dto.getSearchBy()))
                .fetchOne();
        return count;
    }
}
