package shoppingMall.project.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.UUID;

@Service
public class FileService {

    public String loadFile(String path,byte[] bytes,String imgOriginNm) throws IOException {
        UUID uuid = UUID.randomUUID();
        String img = imgOriginNm.substring(imgOriginNm.lastIndexOf("."));
        String imgNm = uuid.toString()+img;
        String savedImg = path+"/"+imgNm;
        FileOutputStream fileOutputStream = new FileOutputStream(savedImg);
        fileOutputStream.write(bytes);
        return imgNm;
    }


    public void deleteFile(String savedImg) throws Exception{
        File file = new File(savedImg);
        if (file.exists()){
            file.delete();
            System.out.println("파일을 삭제하였습니다");
        }else {
            throw new Exception("파일이 존재하지 않습니다.");
        }
    }
}
