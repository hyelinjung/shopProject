package shoppingMall.project.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import shoppingMall.project.config.JwtTokenProvider;
import shoppingMall.project.domain.UserDto;
import shoppingMall.project.entity.User;
import shoppingMall.project.repository.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRequestMapper userRequestMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        UserDto userDto = userRequestMapper.dto(oAuth2User);
        String role = "ROLE_USER";
        String token = jwtTokenProvider.createToken(userDto.getUsername(),role);
        log.info("{}",token);
        writeTokenResponse(response, token);
    }
    private void writeTokenResponse(HttpServletResponse response,String token)
            throws IOException {
       /* response.setContentType("text/html;charset=UTF-8");

        response.addHeader("Authorization", token);
        response.setContentType("application/json;charset=UTF-8");

        var writer = response.getWriter();
        writer.flush();*/

        Cookie cookie = new Cookie("accessToken",token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        response.setHeader("Authorization",token);
        response.sendRedirect("/");
    }
}
