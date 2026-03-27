package com.revplay.playbackservice.repository;

import com.revplay.playbackservice.entity.QueueItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QueueItemRepository extends JpaRepository<QueueItem, Long> {
    
    List<QueueItem> findByQueueIdOrderByPositionInQueueAsc(Long queueId);
    
    Optional<QueueItem> findByQueueIdAndPositionInQueue(Long queueId, Integer position);
    
    void deleteByQueueId(Long queueId);
    
    @Modifying
    @Query("UPDATE QueueItem q SET q.positionInQueue = q.positionInQueue - 1 WHERE q.queueId = :queueId AND q.positionInQueue > :position")
    void shiftItemsUp(@Param("queueId") Long queueId, @Param("position") Integer position);
    
    @Modifying
    @Query("UPDATE QueueItem q SET q.positionInQueue = q.positionInQueue + 1 WHERE q.queueId = :queueId AND q.positionInQueue >= :position")
    void shiftItemsDown(@Param("queueId") Long queueId, @Param("position") Integer position);
    
    @Query("SELECT COUNT(q) FROM QueueItem q WHERE q.queueId = :queueId")
    Long countByQueueId(@Param("queueId") Long queueId);
}
