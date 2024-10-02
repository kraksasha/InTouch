package com.example.intouch.Repository;

import com.example.intouch.Entity.LikeMusic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeMusicRepository extends JpaRepository<LikeMusic, Long> {
}
