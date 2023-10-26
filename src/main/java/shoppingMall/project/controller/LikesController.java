package shoppingMall.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shoppingMall.project.domain.LikesDto;
import shoppingMall.project.repository.UserRepository;
import shoppingMall.project.service.LikesService;
import shoppingMall.project.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
public class LikesController {
    @Autowired
    LikesService likesService;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @PostMapping("/cart/likes/{itemId}")
    public @ResponseBody ResponseEntity<String> addLikeCon(@PathVariable("itemId") Long id, Principal principal){
        System.out.println("좋아요");
        System.out.println(id);
        if (!likesService.checkLikes(id, principal.getName())){
            return new ResponseEntity<>("이미 등록된 상품입니다.", HttpStatus.BAD_REQUEST);
        }
        String username = principal.getName();
        try {
            likesService.addLikes(id, username);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
    @GetMapping("/user/likePage")
    public String likeListCon(Principal principal, Model model){
        System.out.println("좋아요 리스트");
        List<LikesDto> likesDtoList = likesService.likesDtos(principal.getName());
        model.addAttribute("likes",likesDtoList);
        return "likes";
    }
    @DeleteMapping("/likes/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteLikeCon(@PathVariable("id") Long id,Principal principal){
        System.out.println("좋아요 삭제");
        System.out.println("id :"+ id);
        if (!likesService.deleteLikes(id, principal.getName())){
            return new ResponseEntity<>("fails", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
