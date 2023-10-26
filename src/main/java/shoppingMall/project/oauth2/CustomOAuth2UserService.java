package shoppingMall.project.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import shoppingMall.project.domain.UserDto;
import shoppingMall.project.entity.User;
import shoppingMall.project.loginprocess.CustomUser;
import shoppingMall.project.repository.UserRepository;


import java.util.Collections;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest,OAuth2User> userService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = userService.loadUser(userRequest);
        System.out.println("userRequest1"+userRequest.getAccessToken());
        System.out.println("registrationId"+userRequest.getClientRegistration().getRegistrationId());
        System.out.println("getAttributes:"+oAuth2User.getAttributes());
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        System.out.println("userNameAttributeName:"+userNameAttributeName);

        CustomOauth2 customOauth2 = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")){
            customOauth2 = new GoogleCustom(oAuth2User.getAttributes());
            System.out.println("구글로 로그인");
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
        customOauth2 = new NaverCustom((Map<String, Object>) oAuth2User.getAttributes().get("response"));
            System.out.println("네이버로 로그인");
        }
        String username = customOauth2.username();
        String password = passwordEncoder.encode("12345");
        String email = customOauth2.email();
        String provider = customOauth2.provider();
        String providerId = customOauth2.providerId();
        String role = "ROLE_USER";


        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        userDto.setPassword(password);
        userDto.setUsername(username);
        System.out.println("userDto 실행");
        System.out.println(username);

        User entity = userRepository.findByUsername(username);
        System.out.println("user객체 생성");
        if ( entity == null){
            System.out.println(".....");
            entity = User.createUser(userDto,provider,providerId);
            System.out.println("=========");
            userRepository.save(entity);
            System.out.println("처음 가입하는 회원");
        }
        System.out.println("이미 가입된 회원");
        var memberAttribute = customOauth2.convertToMap();


        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),memberAttribute,"email");   }
}
