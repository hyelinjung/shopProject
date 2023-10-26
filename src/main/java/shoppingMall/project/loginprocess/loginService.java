package shoppingMall.project.loginprocess;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import shoppingMall.project.entity.User;
import shoppingMall.project.repository.UserRepository;

@Service
public class loginService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= userRepository.findByUsername(username);
        System.out.println("user:"+user);
            if (user != null) {
                return new CustomUser(user);
            }

        return null;
    }
}
