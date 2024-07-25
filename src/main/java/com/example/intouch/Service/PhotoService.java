package com.example.intouch.Service;

import com.example.intouch.Entity.Photo;
import com.example.intouch.Entity.User;
import com.example.intouch.Repository.PhotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
public class PhotoService {
    private PhotoRepository photoRepository;
    private UserService userService;

    public PhotoService(PhotoRepository photoRepository, UserService userService) {
        this.photoRepository = photoRepository;
        this.userService = userService;
    }

    public void addPhoto(MultipartFile file) throws IOException {
        Photo photo = new Photo();
        User user = userService.getMyUser();
        copyFileNewDir(file,photo,user);
        photoRepository.save(photo);
    }

    public List<Photo> getMyPhotos(){
        User user = userService.getMyUser();
        List<Photo> list = user.getPhotos();
        return  list;
    }

    public void deletePhoto(Long id){
        Photo photo = photoRepository.findById(id).get();
        File fileDelete = new File(photo.getPathToPhoto());
        fileDelete.delete();
        photoRepository.deleteById(id);
    }

    public Photo getPhoto(Long id){
        Photo photo = photoRepository.findById(id).get();
        return photo;
    }

    public List<Photo> getUserPhotos(Long id){
        User user = userService.getUser(id);
        List<Photo> list = user.getPhotos();
        return list;
    }

    private void copyFileNewDir(MultipartFile file, Photo photo, User user) throws IOException {
        File fileDirectory = new File("/Users/aleksandrkrahmalev/IdeaProjects/InTouch/Storage/" + user.getEmail() + "/photos");
        fileDirectory.mkdirs();
        File copyFile = new File(fileDirectory.getAbsolutePath() + "/" + file.getOriginalFilename());
        OutputStream os = new FileOutputStream(copyFile);
        os.write(file.getBytes());
        copyFile.mkdirs();
        photo.setNamePhoto(copyFile.getName());
        photo.setPathToPhoto(copyFile.getAbsolutePath());
        photo.setUserId(user.getId());
    }
}
