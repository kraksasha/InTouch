package com.example.intouch.Service;



import com.example.intouch.Entity.LikePhoto;
import com.example.intouch.Entity.Photo;
import com.example.intouch.Entity.User;
import com.example.intouch.Repository.LikePhotoRepository;
import com.example.intouch.Repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikePhotoService {
    private final LikePhotoRepository likePhotoRepository;
    private final UserService userService;
    private final PhotoRepository photoRepository;

    public void addLike(Long id){
        User user = userService.getMyUser();
        if (checkLike(id, user)){
            LikePhoto likePhoto = new LikePhoto();
            System.out.println(user.getId());
            likePhoto.setAuthorId(user.getId());
            likePhoto.setPhotoId(id);
            likePhotoRepository.save(likePhoto);
        }
    }

    public void deleteLike(Long id){
        likePhotoRepository.deleteById(id);
    }

    private boolean checkLike(Long id, User user){
        Photo photo = photoRepository.findById(id).get();
        List<LikePhoto> list = photo.getLikes();
        for (int i = 0; i < list.size(); i++){
            if (user.getId() == list.get(i).getAuthorId()){
                return false;
            }
        }
        return true;
    }

}
