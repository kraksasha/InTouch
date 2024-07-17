package com.production.intouchcompany.Repository;

import com.production.intouchcompany.Entity.CommentPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentPhotoRepository extends JpaRepository<CommentPhoto, Long> {
}
