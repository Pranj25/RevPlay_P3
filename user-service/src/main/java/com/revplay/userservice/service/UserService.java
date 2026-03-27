package com.revplay.userservice.service;

import com.revplay.userservice.entity.User;
import com.revplay.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public User createUser(User user) {
        return userRepository.save(user);
    }
    
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public List<User> getActiveUsers() {
        return userRepository.findByIsActiveTrue();
    }
    
    public List<User> getUsersByRole(com.revplay.userservice.entity.Role role) {
        return userRepository.findByRole(role);
    }
    
    public User updateUser(User user) {
        return userRepository.save(user);
    }
    
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    public boolean userExists(Long id) {
        return userRepository.existsById(id);
    }
    
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public void deactivateUser(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setIsActive(false);
            userRepository.save(user);
        }
    }
    
    public void activateUser(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setIsActive(true);
            userRepository.save(user);
        }
    }
    
    public void updatePlaylistCount(Long userId, int increment) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            int currentCount = user.getTotalPlaylists() != null ? user.getTotalPlaylists() : 0;
            user.setTotalPlaylists(Math.max(0, currentCount + increment));
            userRepository.save(user);
        }
    }
    
    public void updateFavoritesCount(Long userId, int increment) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            int currentCount = user.getTotalFavorites() != null ? user.getTotalFavorites() : 0;
            user.setTotalFavorites(Math.max(0, currentCount + increment));
            userRepository.save(user);
        }
    }
    
    public void updateListeningTime(Long userId, long additionalSeconds) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            long currentTime = user.getListeningTime() != null ? user.getListeningTime() : 0L;
            user.setListeningTime(currentTime + additionalSeconds);
            userRepository.save(user);
        }
    }
    
    public void upgradeToArtist(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setRole(com.revplay.userservice.entity.Role.ARTIST);
            userRepository.save(user);
        }
    }
    
    public void downgradeToListener(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setRole(com.revplay.userservice.entity.Role.LISTENER);
            userRepository.save(user);
        }
    }
}
