package com.production.intouchcompany.Service;

import com.production.intouchcompany.Entity.LikePhoto;
import com.production.intouchcompany.Entity.Photo;
import com.production.intouchcompany.Entity.User;
import com.production.intouchcompany.Repository.LikePhotoRepository;
import com.production.intouchcompany.Repository.PhotoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikePhotoService {
    private LikePhotoRepository likePhotoRepository;
    private UserService userService;
    private PhotoRepository photoRepository;

    public LikePhotoService(LikePhotoRepository likePhotoRepository, UserService userService, PhotoRepository photoRepository) {
        this.likePhotoRepository = likePhotoRepository;
        this.userService = userService;
        this.photoRepository = photoRepository;
    }

    public void addLike(Long id){
        User user = userService.getMyUser();
        if (checkLike(id)){
            LikePhoto likePhoto = new LikePhoto();
            likePhoto.setAuthorId(user.getId());
            likePhoto.setPhotoId(id);
            likePhotoRepository.save(likePhoto);
        }
    }

    public void deleteLike(Long id){
        likePhotoRepository.deleteById(id);
    }

    private boolean checkLike(Long id){
        Photo photo = photoRepository.findById(id).get();
        User user = userService.getMyUser();
        List<LikePhoto> list = photo.getLikes();
        for (int i = 0; i < list.size(); i++){
            if (user.getId() == list.get(i).getAuthorId()){
                return false;
            }
        }
        return true;
    }

}
