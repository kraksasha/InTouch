package com.production.intouchcompany.Repository;

import com.production.intouchcompany.Entity.LikePhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikePhotoRepository extends JpaRepository<LikePhoto, Long> {
}
