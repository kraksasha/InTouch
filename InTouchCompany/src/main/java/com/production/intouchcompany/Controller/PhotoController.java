package com.production.intouchcompany.Controller;

import com.production.intouchcompany.Entity.Photo;
import com.production.intouchcompany.Service.PhotoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
public class PhotoController {
    private PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @PostMapping(value = "/addPhoto")
    public ResponseEntity<?> addNewPhoto(@RequestParam("file") MultipartFile file) throws IOException {
        photoService.addPhoto(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/myPhotos")
    public ResponseEntity<?> getMyPagePhotos(){
        List<Photo> list = photoService.getMyPhotos();
        if (list.size() != 0){
            return new ResponseEntity<>(list,HttpStatus.OK);
        }
        return new ResponseEntity<>("фотографии не найдены", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/deletePhoto/{id}")
    public ResponseEntity<?> deletePhotoById(@PathVariable(name = "id") Long id){
        photoService.deletePhoto(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/photo/{id}")
    public ResponseEntity<Photo> getPhotoById(@PathVariable(name = "id") Long id){
        Photo photo = photoService.getPhoto(id);
        return new ResponseEntity<>(photo, HttpStatus.OK);
    }

    @GetMapping("/photos/{id}")
    public ResponseEntity<?> getUserPagePhotos(@PathVariable(name = "id") Long id){
        List<Photo> list = photoService.getUserPhotos(id);
        if (list.size() != 0){
            return new ResponseEntity<>(list,HttpStatus.OK);
        }
        return new ResponseEntity<>("фотографии не обнаружены", HttpStatus.BAD_REQUEST);
    }
}
