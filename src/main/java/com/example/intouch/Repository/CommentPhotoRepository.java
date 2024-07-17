package com.example.intouch.Repository;


import com.example.intouch.Entity.CommentPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentPhotoRepository extends JpaRepository<CommentPhoto, Long> {
}
