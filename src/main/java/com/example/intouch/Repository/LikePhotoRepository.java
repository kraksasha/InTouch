package com.example.intouch.Repository;

import com.example.intouch.Entity.LikePhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikePhotoRepository extends JpaRepository<LikePhoto, Long> {
}
