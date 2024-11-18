package com.example.intouch.Repository;

import com.example.intouch.Entity.UserMusic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMusicRepository extends JpaRepository<UserMusic, Long> {
}
