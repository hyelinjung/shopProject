package shoppingMall.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import shoppingMall.project.config.JwtTokenProvider;
import shoppingMall.project.domain.UserDto;
import shoppingMall.project.entity.User;
import shoppingMall.project.repository.UserRepository;
import shoppingMall.project.service.UserService;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Random;

@Controller
public class MemberController {

    @Value("${spring.mail.username}")
    private String form;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    JavaMailSender javaMailSender;
    @GetMapping("/members/loginForm")
    public String loginForm(@RequestParam(value = "errorMessage", required = false)String errorMessage,Model model){
        System.out.println(errorMessage);
        model.addAttribute("errorMessage",errorMessage);
        return "member/loginForm";
    }
    @PostMapping("/members/login")
    public String loginForm(UserDto dto, HttpServletResponse response, HttpServletRequest request)throws NullPointerException{
        User user = userRepository.findByUsername(dto.getUsername());
        String pwd = passwordEncoder.encode(dto.getPassword());
        System.out.println("pwd:"+pwd);
        if (user == null || !passwordEncoder.matches(dto.getPassword(),user.getPassword())){
          String errorMessage ="false";
            return "redirect:/members/loginForm?errorMessage="+errorMessage;
        }
       String token = jwtTokenProvider.createToken(user.getUsername(),String.valueOf(user.getRole()));
       String refresh_token = jwtTokenProvider.create_refresh_Token(String.valueOf(user.getRole()));
        System.out.println("token:" + token);
        Cookie cookie = new Cookie("accessToken",token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        response.setHeader("Authorization",token);
        return "redirect:/";
    }
    @GetMapping("/members/joinForm")
    public String joinForm(Model model){
        model.addAttribute("userDto",new UserDto());
        return "member/joinForm";
    }
    @PostMapping("/members/join")
    public String joinPost(@Valid UserDto userDto,BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()){
            return "member/joinForm";
        }
        try {
            String rowPwd = userDto.getPassword();
            String pwd = passwordEncoder.encode(rowPwd);
            userDto.setPassword(pwd);
            userService.savedUser(userDto);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "member/joinForm";
        }
        return "redirect:/";
    }
    @GetMapping("/members/mailCheck")
    @ResponseBody
    public String mailCheck(String email){
        System.out.println("메일전송:"+email);

        Random random = new Random();
        int ranNum = random.nextInt(8888) + 1111;
        String num = String.valueOf(ranNum);
        System.out.println(ranNum);

        String setForm = form;
        System.out.println(setForm);
        String toMail = email;
        String title = "회원가입 인증 메일입니다.";
        String content = "NAN SHOP 회원 인증"+
                "<br><br> 인증번호는 "+ ranNum+"입니다."+
                "<br>"+"해당 인증번호를 인증번호 확인란에 기입해주세요";
    try {
        MimeMessage ms = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(ms,true,"utf-8");
        helper.setFrom(setForm);
        helper.setTo(toMail);
        helper.setSubject(title);
        helper.setText(content,true);
        javaMailSender.send(ms);
    }catch (Exception e){
        System.out.println(e.getMessage());
        return e.getMessage();
    }
        return num;
    }

    @PostMapping("/members/logout")
    @ResponseBody
    public void logout(HttpServletResponse response){
        System.out.println("로그아웃");
        Cookie cookie = new Cookie("accessToken",null);
        cookie.setMaxAge(0); // 쿠키의 expiration 타임을 0으로 하여 없앤다.
        cookie.setPath("/"); // 모든 경로에서 삭제 됬음을 알린다.
        response.addCookie(cookie);
    }

    }
