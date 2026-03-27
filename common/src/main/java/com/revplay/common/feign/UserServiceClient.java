package com.revplay.common.feign;

import com.revplay.common.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "USER-SERVICE")
public interface UserServiceClient {
    
    @GetMapping("/users/{id}")
    UserDto getUserById(@PathVariable("id") Long id);
    
    @GetMapping("/users/username/{username}")
    UserDto getUserByUsername(@PathVariable("username") String username);
    
    @GetMapping("/users")
    List<UserDto> getAllUsers();
    
    @GetMapping("/users/active")
    List<UserDto> getActiveUsers();
    
    @PostMapping("/users")
    UserDto createUser(@RequestBody UserDto userDto);
    
    @PutMapping("/users/{id}")
    UserDto updateUser(@PathVariable("id") Long id, @RequestBody UserDto userDto);
    
    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable("id") Long id);
    
    @GetMapping("/users/exists/{id}")
    boolean userExists(@PathVariable("id") Long id);
    
    @GetMapping("/users/role/{role}")
    List<UserDto> getUsersByRole(@PathVariable("role") String role);
}
