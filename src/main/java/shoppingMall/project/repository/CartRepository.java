package shoppingMall.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingMall.project.entity.Cart;
import shoppingMall.project.entity.User;

public interface CartRepository extends JpaRepository<Cart,Long> {
    public Cart findByUserId(Long userId);
}
