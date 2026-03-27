package com.revplay.common.feign;

import com.revplay.common.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "user-service", configuration = com.revplay.common.config.FeignConfig.class)
public interface UserServiceClient {
    
    @GetMapping("/api/users/{id}")
    UserDto getUserById(@PathVariable("id") Long id);
    
    @GetMapping("/api/users/username/{username}")
    UserDto getUserByUsername(@PathVariable("username") String username);
    
    @GetMapping("/api/users")
    List<UserDto> getAllUsers();
    
    @GetMapping("/api/users/active")
    List<UserDto> getActiveUsers();
    
    @PostMapping("/api/users")
    UserDto createUser(@RequestBody UserDto userDto);
    
    @PutMapping("/api/users/{id}")
    UserDto updateUser(@PathVariable("id") Long id, @RequestBody UserDto userDto);
    
    @DeleteMapping("/api/users/{id}")
    void deleteUser(@PathVariable("id") Long id);
    
    @GetMapping("/api/users/exists/{id}")
    boolean userExists(@PathVariable("id") Long id);
    
    @GetMapping("/api/users/role/{role}")
    List<UserDto> getUsersByRole(@PathVariable("role") String role);
}
