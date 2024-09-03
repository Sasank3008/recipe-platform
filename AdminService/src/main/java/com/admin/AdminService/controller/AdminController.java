package com.admin.AdminService.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/admin")
public class AdminController
{
    @GetMapping("health")
    public ResponseEntity<String> test()
    {
        return new ResponseEntity<>("Admin controller is working", HttpStatus.OK);

    }




}
