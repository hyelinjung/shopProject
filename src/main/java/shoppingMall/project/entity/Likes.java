package shoppingMall.project.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "likes")
public class Likes extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "likes_id")
    private Long id;
    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @OneToOne
    @JoinColumn(name = "member_id")
    private User user;

    public static Likes createLike(Item item, User user){
        Likes like = new Likes();
        like.setUser(user);
        like.setItem(item);
        like.setRegDateTime(LocalDateTime.now());
        return like;
    }
}
