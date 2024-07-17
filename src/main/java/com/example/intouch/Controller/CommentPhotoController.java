package com.example.intouch.Controller;


import com.example.intouch.Entity.CommentPhoto;
import com.example.intouch.Service.CommentPhotoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentPhotoController {
    private CommentPhotoService commentPhotoService;

    public CommentPhotoController(CommentPhotoService commentPhotoService) {
        this.commentPhotoService = commentPhotoService;
    }

    @PostMapping("/addComment/{id}")
    public ResponseEntity<?> addNewComment(@RequestBody CommentPhoto commentPhoto, @PathVariable(name = "id") Long id){
        commentPhotoService.addComment(commentPhoto, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteComment/{id}")
    public ResponseEntity<?> deleteCommentById(@PathVariable(name = "id") Long id){
        commentPhotoService.deleteComment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
