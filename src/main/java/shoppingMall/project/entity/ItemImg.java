package shoppingMall.project.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="item_img")
public class ItemImg {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_img_id")
    private Long id;
    private String imgOriginNm;
    private String imgNm;
    private String imgUrl;
    @Column(nullable = false)
    private String imgYn;
   @ManyToOne
   @JoinColumn(name = "item_id")
    private Item item;


   public void updateImg(String imgOriginNm,String imgUrl,String imgNm){
       this.imgNm = imgNm;
       this.imgUrl = imgUrl;
       this.imgOriginNm = imgOriginNm;
   }
}
