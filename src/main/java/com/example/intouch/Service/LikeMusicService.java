package com.example.intouch.Service;

import com.example.intouch.Entity.*;
import com.example.intouch.Repository.LikeMusicRepository;
import com.example.intouch.Repository.MusicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeMusicService {

    private final LikeMusicRepository likeMusicRepository;
    private final UserService userService;
    private final MusicRepository musicRepository;

    public void addLike(Long id){
        User user = userService.getMyUser();
        if (checkLike(id, user)){
            LikeMusic likeMusic = new LikeMusic();
            System.out.println(user.getId());
            likeMusic.setAuthorId(user.getId());
            likeMusic.setMusicId(id);
            likeMusicRepository.save(likeMusic);
        }
    }

    public void deleteLike(Long id){
        likeMusicRepository.deleteById(id);
    }

    private boolean checkLike(Long id, User user){
        Music music = musicRepository.findById(id).get();
        List<LikeMusic> list = music.getLikes();
        for (int i = 0; i < list.size(); i++){
            if (user.getId() == list.get(i).getAuthorId()){
                return false;
            }
        }
        return true;
    }
}
