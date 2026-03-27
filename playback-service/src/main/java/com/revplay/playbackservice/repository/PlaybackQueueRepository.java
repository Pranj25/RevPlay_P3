package com.revplay.playbackservice.repository;

import com.revplay.playbackservice.entity.PlaybackQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaybackQueueRepository extends JpaRepository<PlaybackQueue, Long> {
    
    Optional<PlaybackQueue> findByUserId(Long userId);
    
    void deleteByUserId(Long userId);
}
