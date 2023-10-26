package shoppingMall.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shoppingMall.project.domain.LikesDto;
import shoppingMall.project.entity.Item;
import shoppingMall.project.entity.Likes;
import shoppingMall.project.entity.User;
import shoppingMall.project.repository.ItemRepository;
import shoppingMall.project.repository.LikesRepository;
import shoppingMall.project.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class LikesService {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LikesRepository  likesRepository;
    @Autowired
    UserService userService;

    public void addLikes(Long itemId,String username){
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        User user = userRepository.findByUsername(username);
        Likes likes = Likes.createLike(item,user);
        likesRepository.save(likes);
    }

    public User findU(String username){
        return userRepository.findByUsername(username);
    }
    public boolean checkLikes(Long itemId,String username){
        User user = userRepository.findByUsername(username);
        Likes likes = likesRepository.findByItemIdAndUserId(itemId,user.getId());
        if (likes != null){
            return false;
        }
        return true;
    }
    public boolean deleteLikes(Long id,String username){
        Likes likes = likesRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        User user = likes.getUser();
        User user1 = userRepository.findByUsername(username);
        if (user != user1){
            return false;
        }
        likesRepository.delete(likes);
        return true;
    }

    public List<LikesDto> likesDtos(String username){
        List<LikesDto> likesDtoList = new ArrayList<>();
        likesDtoList = likesRepository.findLikesList(username);
        if (likesDtoList.isEmpty()){
            return likesDtoList;
        }
        return likesDtoList;
    }
}
