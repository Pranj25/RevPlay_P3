package com.revplay.common.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "FAVOURITE-SERVICE")
public interface FavouriteServiceClient {
    
    @GetMapping("/favourites/user/{userId}")
    List<Map<String, Object>> getUserFavourites(@PathVariable("userId") Long userId);
    
    @GetMapping("/favourites/song/{songId}")
    List<Map<String, Object>> getSongFavourites(@PathVariable("songId") Long songId);
    
    @GetMapping("/favourites/song/{songId}/count")
    Map<String, Long> getSongLikeCount(@PathVariable("songId") Long songId);
    
    @GetMapping("/favourites/user/{userId}/count")
    Map<String, Long> getUserFavouriteCount(@PathVariable("userId") Long userId);
    
    @GetMapping("/favourites/check")
    Map<String, Boolean> checkIfLiked(@RequestParam("userId") Long userId, 
                                     @RequestParam("songId") Long songId);
    
    @PostMapping("/favourites/batch/likes")
    Map<Long, Long> getMultipleSongLikeCounts(@RequestBody List<Long> songIds);
    
    @GetMapping("/favourites/most-liked")
    List<Object[]> getMostLikedSongs();
}
