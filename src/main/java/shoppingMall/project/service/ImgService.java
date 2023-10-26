package shoppingMall.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shoppingMall.project.entity.Item;
import shoppingMall.project.entity.ItemImg;
import shoppingMall.project.repository.ImgRepository;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@Service
public class ImgService {
    @Value("${itemImgLocation}")
     String itemImgLocation;
    @Autowired
    ImgRepository imgRepository;
    @Autowired
    FileService fileService;

    public void uploadImg(ItemImg itemImg, MultipartFile file) throws IOException {
        String imgOriginNm = file.getOriginalFilename();
        String imgUrl ="";
        String imgNm ="";
        if (!file.isEmpty()) {
             imgNm = fileService.loadFile(itemImgLocation, file.getBytes(), imgOriginNm);
             imgUrl = "/img/item/" + imgNm;
        }
        itemImg.updateImg(imgOriginNm,imgUrl,imgNm);
        imgRepository.save(itemImg);
    }

    public void updateImg(Long id, MultipartFile file) throws Exception {
       if (!file.isEmpty()){
        ItemImg img = imgRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (img != null) {
            fileService.deleteFile(itemImgLocation + "/" + img.getImgNm());
        }
            String or = file.getOriginalFilename();
            String imgNm = fileService.loadFile(itemImgLocation, file.getBytes(), or);
            String imgUrl = "img/item/"+ imgNm;
            img.updateImg(or,imgUrl,imgNm);
        }
    }
}
