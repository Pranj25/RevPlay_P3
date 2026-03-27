package com.revplay.playbackservice.service;

import com.revplay.playbackservice.entity.PlaybackQueue;
import com.revplay.playbackservice.entity.QueueItem;
import com.revplay.playbackservice.repository.PlaybackQueueRepository;
import com.revplay.playbackservice.repository.QueueItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class QueueService {
    
    @Autowired
    private PlaybackQueueRepository queueRepository;
    
    @Autowired
    private QueueItemRepository queueItemRepository;
    
    public PlaybackQueue getOrCreateQueue(Long userId) {
        Optional<PlaybackQueue> queueOpt = queueRepository.findByUserId(userId);
        if (queueOpt.isEmpty()) {
            PlaybackQueue queue = new PlaybackQueue(userId);
            return queueRepository.save(queue);
        }
        return queueOpt.get();
    }
    
    public List<QueueItem> getQueueItems(Long userId) {
        PlaybackQueue queue = getOrCreateQueue(userId);
        return queueItemRepository.findByQueueIdOrderByPositionInQueueAsc(queue.getId());
    }
    
    public void addToQueue(Long userId, Long songId, String songTitle, String songArtist) {
        PlaybackQueue queue = getOrCreateQueue(userId);
        
        // Get current max position
        List<QueueItem> items = queueItemRepository.findByQueueIdOrderByPositionInQueueAsc(queue.getId());
        int nextPosition = items.size() + 1;
        
        QueueItem queueItem = new QueueItem(queue.getId(), songId, songTitle, songArtist, nextPosition);
        queueItemRepository.save(queueItem);
    }
    
    public void removeFromQueue(Long userId, Integer position) {
        PlaybackQueue queue = getOrCreateQueue(userId);
        Optional<QueueItem> itemOpt = queueItemRepository.findByQueueIdAndPositionInQueue(queue.getId(), position);
        
        if (itemOpt.isPresent()) {
            QueueItem item = itemOpt.get();
            queueItemRepository.delete(item);
            
            // Shift remaining items up
            queueItemRepository.shiftItemsUp(queue.getId(), position);
        }
    }
    
    public void reorderQueue(Long userId, Integer fromPosition, Integer toPosition) {
        PlaybackQueue queue = getOrCreateQueue(userId);
        List<QueueItem> items = queueItemRepository.findByQueueIdOrderByPositionInQueueAsc(queue.getId());
        
        if (fromPosition >= 1 && fromPosition <= items.size() && 
            toPosition >= 1 && toPosition <= items.size()) {
            
            QueueItem itemToMove = items.get(fromPosition - 1);
            
            if (fromPosition < toPosition) {
                // Moving down
                queueItemRepository.shiftItemsUp(queue.getId(), fromPosition);
                itemToMove.setPositionInQueue(toPosition);
            } else {
                // Moving up
                queueItemRepository.shiftItemsDown(queue.getId(), toPosition);
                itemToMove.setPositionInQueue(toPosition);
            }
            
            queueItemRepository.save(itemToMove);
        }
    }
    
    public void clearQueue(Long userId) {
        PlaybackQueue queue = getOrCreateQueue(userId);
        queueItemRepository.deleteByQueueId(queue.getId());
        
        // Reset current song
        queue.setCurrentSongId(null);
        queue.setCurrentPositionSeconds(0);
        queue.setIsPlaying(false);
        queue.setIsPaused(false);
        queueRepository.save(queue);
    }
    
    public void shuffleQueue(Long userId) {
        PlaybackQueue queue = getOrCreateQueue(userId);
        List<QueueItem> items = queueItemRepository.findByQueueIdOrderByPositionInQueueAsc(queue.getId());
        
        if (!items.isEmpty()) {
            Random random = new Random();
            for (int i = items.size() - 1; i > 0; i--) {
                int j = random.nextInt(i + 1);
                QueueItem temp = items.get(i);
                items.set(i, items.get(j));
                items.set(j, temp);
            }
            
            // Update positions
            for (int i = 0; i < items.size(); i++) {
                items.get(i).setPositionInQueue(i + 1);
                queueItemRepository.save(items.get(i));
            }
        }
    }
    
    public QueueItem getNextSong(Long userId) {
        PlaybackQueue queue = getOrCreateQueue(userId);
        List<QueueItem> items = queueItemRepository.findByQueueIdOrderByPositionInQueueAsc(queue.getId());
        
        if (items.isEmpty()) {
            return null;
        }
        
        // Find current song position
        Integer currentPos = null;
        if (queue.getCurrentSongId() != null) {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getSongId().equals(queue.getCurrentSongId())) {
                    currentPos = i + 1;
                    break;
                }
            }
        }
        
        QueueItem nextSong;
        if (currentPos == null) {
            // Start from beginning
            nextSong = items.get(0);
        } else if (currentPos >= items.size()) {
            // End of queue
            if (queue.getRepeatMode() == PlaybackQueue.RepeatMode.ALL) {
                nextSong = items.get(0);
            } else {
                return null;
            }
        } else {
            // Next song
            nextSong = items.get(currentPos);
        }
        
        return nextSong;
    }
    
    public QueueItem getPreviousSong(Long userId) {
        PlaybackQueue queue = getOrCreateQueue(userId);
        List<QueueItem> items = queueItemRepository.findByQueueIdOrderByPositionInQueueAsc(queue.getId());
        
        if (items.isEmpty()) {
            return null;
        }
        
        // Find current song position
        if (queue.getCurrentSongId() == null) {
            return null;
        }
        
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getSongId().equals(queue.getCurrentSongId())) {
                if (i > 0) {
                    return items.get(i - 1);
                } else if (queue.getRepeatMode() == PlaybackQueue.RepeatMode.ALL) {
                    return items.get(items.size() - 1);
                }
                break;
            }
        }
        
        return null;
    }
    
    public void updateQueueState(Long userId, Long songId, Integer position, Boolean isPlaying, Boolean isPaused) {
        PlaybackQueue queue = getOrCreateQueue(userId);
        queue.setCurrentSongId(songId);
        queue.setCurrentPositionSeconds(position);
        queue.setIsPlaying(isPlaying);
        queue.setIsPaused(isPaused);
        queueRepository.save(queue);
    }
    
    public void setVolume(Long userId, Integer volume) {
        PlaybackQueue queue = getOrCreateQueue(userId);
        queue.setVolume(Math.max(0, Math.min(100, volume)));
        queueRepository.save(queue);
    }
    
    public void setRepeatMode(Long userId, PlaybackQueue.RepeatMode repeatMode) {
        PlaybackQueue queue = getOrCreateQueue(userId);
        queue.setRepeatMode(repeatMode);
        queueRepository.save(queue);
    }
    
    public void toggleShuffle(Long userId) {
        PlaybackQueue queue = getOrCreateQueue(userId);
        queue.setIsShuffle(!queue.getIsShuffle());
        queueRepository.save(queue);
        
        if (queue.getIsShuffle()) {
            shuffleQueue(userId);
        }
    }
}
