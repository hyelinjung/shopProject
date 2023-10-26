package shoppingMall.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shoppingMall.project.domain.UserDto;
import shoppingMall.project.entity.User;
import shoppingMall.project.repository.UserRepository;

@Service
public class UserService {

    @Autowired
     UserRepository userRepository;



    public void savedUser(UserDto userDto){
        String provider ="";
        String providerId ="";
        if (!doubleCheck(userDto.getUsername())){
            throw new IllegalStateException("이미 가입된 회원입니다");
        }
        User user = User.createUser(userDto,provider,providerId);
        userRepository.save(user);
    }

    public boolean doubleCheck(String username){
        User user = userRepository.findByUsername(username);
        if (user !=null){
            return false;
        }
        return true;
    }
}
