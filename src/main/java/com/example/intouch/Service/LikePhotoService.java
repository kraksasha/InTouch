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
