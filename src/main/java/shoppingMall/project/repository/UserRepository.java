package shoppingMall.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingMall.project.entity.User;

public interface UserRepository extends JpaRepository<User,Long> {

     User findByUsername(String username);
}
