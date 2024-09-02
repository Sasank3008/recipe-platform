package com.user.UserService.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController
{
    @GetMapping("health")
    public ResponseEntity<String> test()
    {
        return new ResponseEntity<>("User controller is working", HttpStatus.OK);
    }
}
