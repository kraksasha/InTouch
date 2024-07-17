package com.production.intouchcompany.Service;

import com.production.intouchcompany.Entity.CommentPhoto;
import com.production.intouchcompany.Entity.User;
import com.production.intouchcompany.Repository.CommentPhotoRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentPhotoService {
    private CommentPhotoRepository commentPhotoRepository;
    private UserService userService;

    public CommentPhotoService(CommentPhotoRepository commentPhotoRepository, UserService userService) {
        this.commentPhotoRepository = commentPhotoRepository;
        this.userService = userService;
    }

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
