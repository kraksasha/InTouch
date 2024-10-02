package com.example.intouch.Controller;

import com.example.intouch.DTO.Filter;
import com.example.intouch.Entity.Music;
import com.example.intouch.Service.LikeMusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LikeMusicController {

    private final LikeMusicService likeMusicService;

    @PostMapping("/addLikeMusic/{id}")
    public ResponseEntity<?> addNewLike(@PathVariable(name = "id") Long id){
        likeMusicService.addLike(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteLikeMusic/{id}")
    public ResponseEntity<?> deleteLikeById(@PathVariable(name = "id") Long id){
        likeMusicService.deleteLike(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
