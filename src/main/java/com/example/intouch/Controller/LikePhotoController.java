package com.example.intouch.Controller;


import com.example.intouch.Service.LikePhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikePhotoController {
    private final LikePhotoService likePhotoService;

    @PostMapping("/addLike/{id}")
    public ResponseEntity<?> addNewLike(@PathVariable(name = "id") Long id){
        likePhotoService.addLike(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteLike/{id}")
    public ResponseEntity<?> deleteLikeById(@PathVariable(name = "id") Long id){
        likePhotoService.deleteLike(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
