package shoppingMall.project.entity;

import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import shoppingMall.project.constant.Role;
import shoppingMall.project.domain.UserDto;

import javax.persistence.*;

@Entity
@Data
@Table(name = "member")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;

    private String username;
    private String password;
    private String email;
    private String addr;
    @Enumerated(EnumType.STRING)
    private Role role; //user, admin
    private String provider; //oauth2
    private String providerId;

    public static User createUser(UserDto userDto, String provider, String providerId){
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRole(Role.ROLE_USER);
        user.setUsername(userDto.getUsername());
        user.setProvider(provider);
        user.setProviderId(providerId);
        String add = userDto.getAddr()+userDto.getAddr1()+userDto.getAddr2();
        user.setAddr(add);
        return user;
    }
}
