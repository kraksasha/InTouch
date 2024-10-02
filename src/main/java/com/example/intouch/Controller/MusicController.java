package com.example.intouch.Controller;

import com.example.intouch.DTO.Filter;
import com.example.intouch.Entity.Music;
import com.example.intouch.Service.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MusicController {

    private final MusicService musicService;

    @PostMapping("/addMusic")
    public ResponseEntity<?> addNewMusic(@RequestParam("file") MultipartFile file) throws IOException {
        musicService.addMusic(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/myMusics")
    public ResponseEntity<?> getMyPageMusic(){
        List<Music> list = musicService.getMyMusics();
        if (list.size() != 0){
            return new ResponseEntity<>(list,HttpStatus.OK);
        }
        return new ResponseEntity<>("У вас нет музыки", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/deleteMusic/{id}")
    public ResponseEntity<?> deleteMusicById(@PathVariable(name = "id") Long id){
        musicService.deleteMusic(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/musics/{id}")
    public ResponseEntity<?> getUserPageMusics(@PathVariable(name = "id") Long id){
        List<Music> list = musicService.getUserMusics(id);
        if (list.size() != 0){
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
        return new ResponseEntity<>("У пользователя нет музыки", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/downloadMusic/{id}")
    public ResponseEntity<?> downloadMusicById(@PathVariable(name = "id") Long id) throws IOException {
        byte[] array = musicService.downloadMusic(id);
        return new ResponseEntity<>(array,HttpStatus.OK);
    }

    @GetMapping("/searchMusic")
    public ResponseEntity<?> getSearchMusicByName(@RequestBody Filter filter){
        List<Music> list = musicService.getSearchMusic(filter.getName());
        if (list.size() != 0){
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
        return new ResponseEntity<>("Ничего не найдено", HttpStatus.BAD_REQUEST);
    }
}
