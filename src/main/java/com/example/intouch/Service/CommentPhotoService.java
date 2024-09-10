package com.example.intouch.Service;

import com.example.intouch.Entity.CommentPhoto;
import com.example.intouch.Entity.User;
import com.example.intouch.Repository.CommentPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentPhotoService {
    private final CommentPhotoRepository commentPhotoRepository;
    private final UserService userService;

    public void addComment(CommentPhoto commentPhoto, Long id){
        User user = userService.getMyUser();
        commentPhoto.setUserAuthorId(user.getId());
        commentPhoto.setPhotoId(id);
        commentPhotoRepository.save(commentPhoto);
    }

    public void deleteComment(Long id){
        commentPhotoRepository.deleteById(id);
    }
}
