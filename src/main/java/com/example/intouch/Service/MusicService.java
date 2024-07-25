package com.example.intouch.Service;

import com.example.intouch.Entity.Music;
import com.example.intouch.Entity.User;
import com.example.intouch.Repository.MusicRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class MusicService {

    private MusicRepository musicRepository;
    private UserService userService;

    public MusicService(MusicRepository musicRepository, UserService userService) {
        this.musicRepository = musicRepository;
        this.userService = userService;
    }

    public void addMusic(MultipartFile file) throws IOException {
        Music music = new Music();
        User user = userService.getMyUser();
        copyFileNewDir(file,music,user);
        musicRepository.save(music);
    }

     public List<Music> getMyMusics(){
        User user = userService.getMyUser();
        List<Music> list = user.getMusics();
        return list;
     }

     public void deleteMusic(Long id){
        Music music = musicRepository.findById(id).get();
        File fileDelete = new File(music.getPathToMusic());
        fileDelete.delete();
        musicRepository.deleteById(id);
     }

     public List<Music> getUserMusics(Long id){
        User user = userService.getMyUser();
        List<Music> list = user.getMusics();
        return list;
     }

     public byte[] downloadMusic(Long id) throws IOException {
        Music music = musicRepository.findById(id).get();
        File downloadFile = new File(music.getPathToMusic());
        Path path = Path.of(downloadFile.getPath());
        byte[] array = Files.readAllBytes(path);
        return array;
     }

    private void copyFileNewDir(MultipartFile file, Music music, User user) throws IOException {
        File fileDirectory = new File("/Users/aleksandrkrahmalev/IdeaProjects/InTouch/Storage/" + user.getEmail() + "/musics");
        fileDirectory.mkdirs();
        File copyFile = new File(fileDirectory.getAbsolutePath() + "/" + file.getOriginalFilename());
        OutputStream os = new FileOutputStream(copyFile);
        os.write(file.getBytes());
        copyFile.mkdirs();
        music.setNameMusic(copyFile.getName());
        music.setPathToMusic(copyFile.getAbsolutePath());
        music.setUserId(user.getId());
    }
}
